package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;
import static com.svalero.workhub.util.Mapper.spaceMapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.Space;
import com.svalero.workhub.domain.WorkPlace;
import com.svalero.workhub.domain.dto.SpaceDTO;

public class RegisterSpace extends AppCompatActivity {

    private String username;
    private Long userID;
    private WorkPlace workPlace;
    private EditText etName;
    private EditText etDescription;
    private EditText etServices;
    private Button bRegister;
    private Space spaceEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_space);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);
        workPlace = (WorkPlace) intentFrom.getSerializableExtra("workplace");
        spaceEdit = (Space) intentFrom.getSerializableExtra("space");

        etName = findViewById(R.id.spaceRegisterName);
        etDescription = findViewById(R.id.spaceRegisterDescription);
        etServices = findViewById(R.id.spaceRegisterServices);
        bRegister = findViewById(R.id.spaceRegisterRegister);

        if(spaceEdit != null){
            etName.setText(spaceEdit.getName());
            etDescription.setText(spaceEdit.getDescription());
            etServices.setText(spaceEdit.getServices());
            bRegister.setText(R.string.registerEdit);
        }
    }

    public void register(View view){
        String name = etName.getText().toString();
        String description = etDescription.getText().toString();
        String services = etServices.getText().toString();

        SpaceDTO spaceDTO = new SpaceDTO(name, description, services, workPlace.getId());
        Space space = spaceMapper(spaceDTO);

        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        if(spaceEdit != null){
            try{
                space.setId(spaceEdit.getId());
                db.getSpaceDAO().update(space);
                Toast.makeText(this, R.string.correctEdit, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, DetailWorkplace.class);
                intent.putExtra("userID", userID);
                intent.putExtra("username", username);
                intent.putExtra("workplace", workPlace);
                startActivity(intent);
            } catch (SQLiteConstraintException sce) {
                Snackbar.make(etName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }

        } else {
            try {
                db.getSpaceDAO().insert(space);
                Toast.makeText(this, R.string.correctRegister, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, DetailWorkplace.class);
                intent.putExtra("userID", userID);
                intent.putExtra("username", username);
                intent.putExtra("workplace", workPlace);
                startActivity(intent);
            } catch (SQLiteConstraintException sce) {
                Snackbar.make(etName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        }
    }

    public void back(View view){
        Intent intent = new Intent(this, DetailWorkplace.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        intent.putExtra("workplace", workPlace);

        startActivity(intent);
    }
}