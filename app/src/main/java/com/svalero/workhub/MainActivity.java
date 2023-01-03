package com.svalero.workhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button button2;
    TextView loggerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loggerUser = findViewById(R.id.loggerUser);
        Intent intentFrom = getIntent();
        String username = intentFrom.getStringExtra("username");
        if(username != null){
            loggerUser.setText(username);
        } else {

        }

        button = findViewById(R.id.listWorkplaces);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListWorkplaces.class);
            startActivity(intent);
        });

        //Test Login
        button2 = findViewById(R.id.listReservations);
        button2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });
    }
}