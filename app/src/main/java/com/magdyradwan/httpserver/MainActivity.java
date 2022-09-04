package com.magdyradwan.httpserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.magdyradwan.httpserver.utility.HttpServer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Button btnServer;
    private boolean isStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnServer = findViewById(R.id.btnStart);

        btnServer.setOnClickListener(v -> {
            isStarted = !isStarted;

            if(isStarted) {
                Intent serviceIntent = new Intent(this, HttpListnerService.class);
                ContextCompat.startForegroundService(this, serviceIntent);
                Toast.makeText(this, "Server is Started", Toast.LENGTH_SHORT).show();
                btnServer.setText("Stop Server");
            }
            else {
                Intent serviceIntent = new Intent(this, HttpListnerService.class);
                stopService(serviceIntent);
                Toast.makeText(this, "Server is Stopped", Toast.LENGTH_SHORT).show();
                btnServer.setText("Start Server");
            }
        });
    }
}