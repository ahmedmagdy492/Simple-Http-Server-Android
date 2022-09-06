package com.magdyradwan.httpserver.utility;

import android.content.Context;
import android.icu.util.Output;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.magdyradwan.httpserver.utility.models.HttpRequestModel;
import com.magdyradwan.httpserver.utility.models.HttpResponseModel;
import com.magdyradwan.httpserver.utility.models.StatusCodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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
    private HttpResponseModel httpResponseModel;
    private static Socket clientSocket;

    private static final String TAG = "HttpServer";


    public HttpServer() {
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
                    // parsing the request
                    String request = ConvertUtil.convertInputStreamToString(clientSocket.getInputStream());
                    HttpRequestModel requestObject = requestParser.parseRequest(request);

                    // TODO: calling the storage manager to get the right file or the entries
                    httpResponseModel = requestHandler.handleRequest(requestObject);

                    if(httpResponseModel.getIsFile()) {
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                        for(int i = 0;i < httpResponseModel.getFileContent().length; i++) {
                            bufferedWriter.write(httpResponseModel.getFileContent()[i]);
                        }

                        bufferedWriter.flush();
                        bufferedWriter.close();
                    }

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    while((bufferedReader.readLine()) != null) {
                        httpResponseModel.setFiles(null);
                        httpResponseModel.setStatusCode(StatusCodes.OK);
                        Dictionary<String, String> headers = httpResponseModel.getHeaders();
                        headers.put("ContentType", "text/html; charset=UTF-8");
                        headers.put("Date", Calendar.getInstance().getTime().toString());
                        responseCreator.setResponseModel(httpResponseModel);
                        String response = responseCreator.createResponse();
                        writeOutputStream(clientSocket.getOutputStream(), response);
                        //clientSocket.shutdownOutput();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
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
//        DataOutputStream dataOutputStream = new DataOutputStream(outStream);
//        dataOutputStream.writeUTF(data);
//        dataOutputStream.flush();
//        dataOutputStream.close();
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
