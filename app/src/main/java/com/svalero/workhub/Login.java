package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.User;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void login(View view){
        EditText etUsernmae = findViewById(R.id.etLoginUsername);
        EditText etPassword = findViewById(R.id.etLoginPassword);

        String username = etUsernmae.getText().toString();
        String password = etPassword.getText().toString();

        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        try{
            User user = db.getUserDAO().login(username, password);
            if(user != null){
                Toast.makeText(this, R.string.loginCorrect, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("userID", user.getId());
                intent.putExtra("username", user.getUsername());
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.loginIncorrect, Toast.LENGTH_LONG).show();
            }
        } catch (SQLiteConstraintException sce){
            Snackbar.make(etUsernmae, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    public void registerUser(View view){
        Intent intent = new Intent(Login.this, RegisterUser.class);
        startActivity(intent);
    }
}