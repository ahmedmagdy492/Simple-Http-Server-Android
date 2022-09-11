package com.magdyradwan.httpserver.utility;

import android.content.Context;
import android.icu.util.Output;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.magdyradwan.httpserver.utility.models.HttpRequestModel;
import com.magdyradwan.httpserver.utility.models.HttpResponseModel;
import com.magdyradwan.httpserver.utility.models.StatusCodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer implements Runnable {
    private static boolean isStarted;
    private ServerSocket serverSocket;
    private final int PORT;
    private final int MAX_THREADS = 64;
    private final IResponseCreator responseCreator;
    private final RequestParser requestParser;
    private final RequestHandler requestHandler;
    private final Context context;
    private HttpResponseModel httpResponseModel;
    private Socket clientSocket;
    private int blockNo = 1;
    private RequestReceived requestReceivedEvent;

    private static final String TAG = "HttpServer";

    public interface RequestReceived
    {
        void invoke(String request);
    }

    public void subscribeToRequestReceivedEvent(RequestReceived requestReceived) {
        this.requestReceivedEvent = requestReceived;
    }

    public HttpServer(Context context) {
        this.context = context;
        isStarted = false;
        serverSocket = null;
        PORT = 45608;
        responseCreator = new HtmlResponseCreator();
        requestParser = new RequestParser();
        requestHandler = new RequestHandler();
    }

    private void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.setReceiveBufferSize(8192);
        String address = Inet4Address.getByName("0.0.0.0").getHostAddress();
        serverSocket.bind(new InetSocketAddress(address, PORT), 20);
        isStarted = true;
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

        while(isStarted) {
            clientSocket = serverSocket.accept();

            // handling the request and sending the response
            executorService.execute(() -> {
                try {
                    InputStreamReader inReader = new InputStreamReader(clientSocket.getInputStream());
                    clientSocket.setSoTimeout(1000);
                    byte[] buffer = new byte[4096];

                    try {
                        int counter = 0;

                        while((buffer[counter] = (byte)inReader.read()) != -1) {
                            counter++;
                        }
                    }
                    catch (SocketTimeoutException ex) {
                        Log.d(TAG, "sockettimeout: " + ex.getMessage());
                    }

                    while(clientSocket.isConnected()) {
                        String request = new String(buffer, StandardCharsets.UTF_8);
                        if(requestReceivedEvent != null) {
                            requestReceivedEvent.invoke(request);
                        }
                        blockNo = 2;
                        // parsing the request
                        HttpRequestModel requestObject = requestParser.parseRequest(request);
                        blockNo = 3;

                        httpResponseModel = requestHandler
                                .handleRequest(context, requestObject);
                        blockNo = 4;

                        if(httpResponseModel.getIsFile()) {
                            blockNo = 5;
                            OutputStream clientOutputStream = clientSocket.getOutputStream();
                            FileInputStream fileInputStream = new FileInputStream(httpResponseModel.getFullPath());
                            String responseHeaders = "HTTP/1.1 200 OK\r\nContent-Type: " + httpResponseModel.getFileType() +"\r\n\r\n";
                            int data;

                            clientOutputStream.write(responseHeaders.getBytes(StandardCharsets.UTF_8));

                            while((data = fileInputStream.read()) != -1) {
                                clientOutputStream.write(data);
                            }

                            clientOutputStream.flush();
                            clientOutputStream.close();
                        }
                        else {
                            blockNo = 6;
                            Dictionary<String, String> headers = httpResponseModel.getHeaders();
                            headers.put("ContentType", "text/html; charset=UTF-8");
                            headers.put("Date", Calendar.getInstance().getTime().toString());
                            responseCreator.setResponseModel(httpResponseModel);
                            String response = responseCreator.createResponse();
                            writeOutputStream(clientSocket.getOutputStream(), response);
                        }
                    }
                    inReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "start execption on block no: " + blockNo + " " + e.getMessage());
                }
            });
        }
    }

    public void dispose() throws IOException {
        isStarted = false;
        if(serverSocket != null)
        {
            serverSocket.close();
        }
    }

    private void writeOutputStream(OutputStream outStream, String data) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outStream);
        BufferedWriter writer = new BufferedWriter(outputStreamWriter);
        writer.write(data);
        writer.flush();
        writer.close();
    }

    @Override
    public void run() {
        try {
            start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
