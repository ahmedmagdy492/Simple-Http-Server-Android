package com.magdyradwan.httpserver.utility;

import android.icu.util.Output;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static boolean isStarted;
    private ServerSocket serverSocket;
    private final int PORT;
    private static final String TAG = "HttpServer";

    public HttpServer() {
        isStarted = false;
        serverSocket = null;
        PORT = 45608;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket();
        String address = Inet4Address.getByName("0.0.0.0").getHostAddress();
        serverSocket.bind(new InetSocketAddress(address, PORT));
        isStarted = true;

        while(isStarted) {
            Socket client = serverSocket.accept();
            Log.d(TAG, "Http server start: " + client.getRemoteSocketAddress().toString());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    OutputStream outStream = client.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outStream);
                    BufferedWriter writer = new BufferedWriter(outputStreamWriter);
                    writer.write("HTTP/1.1 200 OK\r\n" +
                            "Accept-Ranges: bytes\r\n" +
                            "Age: 247555\r\n" +
                            "Cache-Control: max-age=604800\r\n" +
                            "Content-Type: text/html; charset=UTF-8\r\n" +
                            "Date: Sat, 03 Sep 2022 12:34:25 GMT\r\n" +
                            "Etag: \"3147526947\"\r\n" +
                            "Expires: Sat, 10 Sep 2022 12:34:25 GMT\r\n" +
                            "Last-Modified: Thu, 17 Oct 2019 07:18:26 GMT\r\n" +
                            "Server: ECS (dcb/7EC6)\r\n" +
                            "Vary: Accept-Encoding\r\n" +
                            "Content-Length: 36\r\n\r\n<h1>Hello from the server side</h1>\r\n\r\n");
                    writer.flush();
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void stop() throws IOException {
        dispose();
    }

    private void dispose() throws IOException {
        isStarted = false;
        if(serverSocket != null)
        {
            serverSocket.close();
        }
    }
}
