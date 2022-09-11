package com.magdyradwan.httpserver;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.magdyradwan.httpserver.utility.HttpServer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Button btnServer;
    private TextView drawScreen;
    private boolean isStarted = false;
    private final StringBuilder logStream = new StringBuilder();
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    isStarted = !isStarted;

                    HttpServer.RequestReceived requestReceivedEvent = request -> {
                        logStream.append(request);
                        drawScreen.setText(MainActivity.this.logStream.toString());
                    };

                    if(isStarted) {
                        Intent serviceIntent = new Intent(this, HttpListnerService.class);
                        //serviceIntent.putExtra("data", requestReceivedEvent);
                        ContextCompat.startForegroundService(this, serviceIntent);
                        Toast.makeText(this, "Server is Started", Toast.LENGTH_SHORT).show();
                        logStream.append("Server is Started\n");
                        logStream.append("Server is Listening on All Interfaces on Port 45608...\n");
                        btnServer.setText("Stop Server");
                    }
                    else {
                        Intent serviceIntent = new Intent(this, HttpListnerService.class);
                        stopService(serviceIntent);
                        Toast.makeText(this, "Server is Stopped", Toast.LENGTH_SHORT).show();
                        logStream.append("Server is Stopped\n");
                        btnServer.setText("Start Server");
                    }

                    drawScreen.setText(logStream.toString());
                } else {
                    Toast.makeText(this, "Without Granting Storage Permission the Main Feature of the app to share files is not gonna work", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnServer = findViewById(R.id.btnStart);
        drawScreen = findViewById(R.id.log_area);

        btnServer.setOnClickListener(v -> {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                isStarted = !isStarted;

                HttpServer.RequestReceived requestReceivedEvent = request -> {
                    logStream.append(request);
                    drawScreen.setText(MainActivity.this.logStream.toString());
                };

                if(isStarted) {
                    Intent serviceIntent = new Intent(this, HttpListnerService.class);
                    //serviceIntent.putExtra("data", requestReceivedEvent);
                    ContextCompat.startForegroundService(this, serviceIntent);
                    Toast.makeText(this, "Server is Started", Toast.LENGTH_SHORT).show();
                    logStream.append("Server is Started\n");
                    logStream.append("Server is Listening on All Interfaces on Port 45608...\n");
                    btnServer.setText("Stop Server");
                }
                else {
                    Intent serviceIntent = new Intent(this, HttpListnerService.class);
                    stopService(serviceIntent);
                    Toast.makeText(this, "Server is Stopped", Toast.LENGTH_SHORT).show();
                    logStream.append("Server is Stopped\n");
                    btnServer.setText("Start Server");
                }

                drawScreen.setText(logStream.toString());
            }
            else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return true;
    }
}