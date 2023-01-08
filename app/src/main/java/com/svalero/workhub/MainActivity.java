package com.svalero.workhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button2;
    private Button bDetailUser;
    private Button bAdministration;
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

        bAdministration = findViewById(R.id.administration);
        bAdministration.setOnClickListener(view -> {
            Toast.makeText(this, "TODO. Sorry.", Toast.LENGTH_LONG).show();
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.menuWorkplaces){
            Intent intent = new Intent(this, ListWorkplaces.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.menuReserves){
            Intent intent = new Intent(this, ListReserves.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
        } else if(item.getItemId() == R.id.menuUser){
            Intent intent = new Intent(this, DetailUser.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
        } else if(item.getItemId() == R.id.menuLogout){
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
            deleteDialog.setMessage(R.string.confirmationMessage).setTitle(R.string.logoutMessage)
                    .setPositiveButton(R.string.confirmationYes, (dialog, id) -> {
                        Intent intent = new Intent(this, Login.class);
                        startActivity(intent);
                    }).setNegativeButton(R.string.confirmationNo, (dialog, id) -> {
                        dialog.dismiss();
                    });
            AlertDialog dialog = deleteDialog.create();
            dialog.show();
        }else if(item.getItemId() == R.id.menuSettings){
            Toast.makeText(this, "TODO. Sorry.", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}