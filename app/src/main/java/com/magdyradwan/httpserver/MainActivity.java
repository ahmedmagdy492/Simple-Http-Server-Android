package com.magdyradwan.httpserver;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.magdyradwan.httpserver.utility.HttpServer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Button btnServer;
    private TextView drawScreen;
    private boolean isStarted = false;
    private static final StringBuilder logStream = new StringBuilder();
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    launchService();
                } else {
                    Toast.makeText(this, "Without Granting Storage Permission the Main Feature of the app to share files is not going to work", Toast.LENGTH_LONG).show();
                }
            });

    private String getMyIP() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    private void launchService() {
        isStarted = !isStarted;

        Intent serviceIntent = new Intent(this, HttpListnerService.class);
        if(isStarted) {
            ContextCompat.startForegroundService(this, serviceIntent);
            Toast.makeText(this, "Server is Started", Toast.LENGTH_SHORT).show();
            logStream.append("Server is Started\n");
            logStream.append("Server is Listening on ");
            logStream.append(getMyIP());
            logStream.append(" on Port 45608...\n");
            btnServer.setText(getString(R.string.stop_server));
        }
        else {
            stopService(serviceIntent);
            Toast.makeText(this, "Server is Stopped", Toast.LENGTH_SHORT).show();
            logStream.append("Server is Stopped\n");
            btnServer.setText(getString(R.string.start_server));
        }

        drawScreen.setText(logStream.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnServer = findViewById(R.id.btnStart);
        drawScreen = findViewById(R.id.log_area);

        btnServer.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                launchService();
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