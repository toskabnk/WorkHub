package com.svalero.workhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button2;
    private Button bDetailUser;
    private TextView loggerUser;
    private String username;
    private Long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkExternalStoragePermission();

        loggerUser = findViewById(R.id.loggerUser);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);

        Log.i("MainActivity", "MainActivity - Intent Username: " + username);
        Log.i("MainActivity", "MainActivity - Intent userID: " + userID);

        //Escribimos el usuario si esta logueado
        if(username != null){
            loggerUser.setText(username);
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        button = findViewById(R.id.listWorkplaces);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListWorkplaces.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        //Test Login
        button2 = findViewById(R.id.listReservations);
        button2.setOnClickListener(view -> {
            Intent intent = new Intent(this, ListReserves.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        bDetailUser = findViewById(R.id.listUserDetail);
        bDetailUser.setOnClickListener(view -> {
            Intent intent = new Intent(this, DetailUser.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

    private void checkExternalStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},225);
            }
        }
    }
}