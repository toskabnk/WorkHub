package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;
import static com.svalero.workhub.util.Mapper.workplaceMapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.db.dao.WorkPlaceDAO;
import com.svalero.workhub.domain.WorkPlace;
import com.svalero.workhub.domain.dto.WorkPlaceDTO;

public class RegisterWorkplace extends AppCompatActivity {

    private String username;
    private Long userID;
    private WorkPlace workPlaceEdit;
    private EditText etName;
    private EditText etDescription;
    private EditText etPhone;
    private EditText etCity;
    private EditText etAddress;
    private EditText etSchedule;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_workplace);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);
        workPlaceEdit = (WorkPlace) intentFrom.getSerializableExtra("workplace");
        Log.i("RegisterWorkplace", "onCreate - Intent Username: " + username);
        Log.i("RegisterWorkplace", "onCreate - Intent userID: " + userID);
        Log.i("RegisterWorkplace", "onCreate - Intent workplace: " + workPlaceEdit);

        etName = findViewById(R.id.registerWorkplaceName);
        etDescription = findViewById(R.id.registerWorkplaceDescription);
        etPhone = findViewById(R.id.registerWorkplacePhone);
        etCity = findViewById(R.id.registerWorkplaceCity);
        etAddress = findViewById(R.id.registerWorkplaceAddress);
        etSchedule = findViewById(R.id.registerWorkplaceSchedule);
        button = findViewById(R.id.registerWorkplaceRegister);

        if(workPlaceEdit != null){
            etName.setText(workPlaceEdit.getName());
            etDescription.setText(workPlaceEdit.getDescription());
            etPhone.setText(workPlaceEdit.getPhoneNumber());
            etCity.setText(workPlaceEdit.getCity());
            etAddress.setText(workPlaceEdit.getAddress());
            etSchedule.setText(workPlaceEdit.getSchedule());
            button.setText(R.string.registerEdit);
        }
    }

    public void register(View view){


        String name = etName.getText().toString();
        String description = etDescription.getText().toString();
        String phone = etPhone.getText().toString();
        String city = etCity.getText().toString();
        String address = etAddress.getText().toString();
        String schedule = etSchedule.getText().toString();
        WorkPlaceDTO workPlaceDTO = new WorkPlaceDTO(name, description, phone, city, address, schedule);
        WorkPlace workplace = workplaceMapper(workPlaceDTO);

        if(workPlaceEdit != null){

            workplace.setId(workPlaceEdit.getId());

            final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            try{
                db.getWorkPlaceDAO().update(workplace);
                Toast.makeText(this, R.string.correctEdit, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ListWorkplaces.class);
                intent.putExtra("userID", userID);
                intent.putExtra("username", username);
                startActivity(intent);
            } catch (SQLiteConstraintException sce) {
                Snackbar.make(etName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
            }

        } else {

            final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            try {
                db.getWorkPlaceDAO().insert(workplace);
                Toast.makeText(this, R.string.correctRegister, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ListWorkplaces.class);
                intent.putExtra("userID", userID);
                intent.putExtra("username", username);
                startActivity(intent);
            } catch (SQLiteConstraintException sce) {
                Snackbar.make(etName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }
    }

    public void back(View view){
        Intent intent = new Intent(this, ListWorkplaces.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}