package com.svalero.workhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
}