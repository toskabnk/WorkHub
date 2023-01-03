package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;
import static com.svalero.workhub.util.Mapper.userMapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.User;
import com.svalero.workhub.domain.dto.UserDTO;

public class RegisterUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    public void register(View view){
        EditText etName = findViewById(R.id.edit_text_name);
        EditText etSurname = findViewById(R.id.edit_text_surname);
        EditText etUsername = findViewById(R.id.edit_text_username);
        EditText etPassword = findViewById(R.id.edit_text_password);
        EditText etPhone = findViewById(R.id.edit_text_phone);
        EditText etEmail = findViewById(R.id.edit_text_email);
        CheckBox cbAdmin = findViewById(R.id.checkBox_admin);

        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        Boolean admin = cbAdmin.isChecked();

        UserDTO userDTO  = new UserDTO (name, surname, username, password, phone, email, admin);
        User user = userMapper(userDTO);

        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        try{
            db.getUserDAO().insert(user);
            Toast.makeText(this, R.string.correctRegister, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterUser.this, Login.class);
            startActivity(intent);
        } catch (SQLiteConstraintException sce){
            Snackbar.make(etName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    public void back(View view){
        onBackPressed();
    }
}