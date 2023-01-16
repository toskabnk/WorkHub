package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.User;
import com.svalero.workhub.domain.WorkPlace;

public class DetailUser extends AppCompatActivity {

    private String username;
    private Long userID;
    private User user;
    private EditText tvName;
    private EditText tvSurname;
    private EditText tvUsername;
    private EditText tvPhone;
    private EditText tvEmail;
    private Button bEdit;
    private Button bLogout;
    private MenuItem settingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);

        //Conseguir el usuario
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        try{
            user = db.getUserDAO().getById(userID);
        } catch (SQLiteConstraintException sce){
            Snackbar.make(tvName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
        }

        Log.i("DetailUser", "onCreate - User: " + user.toString());

        tvName = findViewById(R.id.detailUserName);
        tvSurname = findViewById(R.id.detailUserSurname);
        tvUsername = findViewById(R.id.detailUserUsername);
        tvPhone = findViewById(R.id.detailUserPhone);
        tvEmail = findViewById(R.id.detailUserEmail);
        bEdit = findViewById(R.id.detailUserEdit);
        bLogout = findViewById(R.id.detailUserLogout);

        tvName.setText(user.getName());
        tvSurname.setText(user.getSurname());
        tvUsername.setText(user.getUsername());
        tvPhone.setText(user.getPhoneNumber());
        tvEmail.setText(user.getEmail());

        bEdit.setOnClickListener(v -> editUser());
        bLogout.setOnClickListener(v -> logout());
    }

    public void logout(){
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
    }

    public void editUser(){
        String name = tvName.getText().toString();
        String surname = tvSurname.getText().toString();
        String phone = tvPhone.getText().toString();
        String email = tvEmail.getText().toString();

        User userEdit = new User(user.getId(), name, surname, username, user.getPassword(), phone, email, user.getAdmin());

        final WorkHubDatabase db1 = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        try{
            db1.getUserDAO().update(userEdit);
            Toast.makeText(this, R.string.correctEdit, Toast.LENGTH_LONG).show();
        } catch (SQLiteConstraintException sce) {
            Snackbar.make(tvName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        settingMenu = menu.findItem(R.id.menuUser);
        settingMenu.setVisible(false);
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
            Intent intent = new Intent(this, Preferences.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        return false;
    }
}