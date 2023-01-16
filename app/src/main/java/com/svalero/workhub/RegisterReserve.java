package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;
import static com.svalero.workhub.util.Mapper.reserveMapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.Reserve;
import com.svalero.workhub.domain.Space;
import com.svalero.workhub.domain.WorkPlace;
import com.svalero.workhub.domain.dto.ReserveDTO;
import com.svalero.workhub.util.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterReserve extends AppCompatActivity {

    private String username;
    private Long userID;
    private WorkPlace workPlace;
    private Space space;
    private TextView tvSpace;
    private EditText etDate;
    private Button register;
    private Reserve editReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_reserve);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);
        workPlace = (WorkPlace) intentFrom.getSerializableExtra("workplace");
        space = (Space) intentFrom.getSerializableExtra("space");
        editReserve = (Reserve) intentFrom.getSerializableExtra("reserve");


        tvSpace = findViewById(R.id.reserveRegisterSpace);
        etDate = findViewById(R.id.reserveRegisterDate);
        register = findViewById(R.id.registerReserve);

        tvSpace.setText(space.getName());

        if (editReserve != null) {
            Date fechaEdit = editReserve.getDate();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String dateFormated = format.format(fechaEdit);
            Log.i("RegisterReserve", "onCreate Fecha a editar: -> " + dateFormated);
            etDate.setText(dateFormated);
            register.setText(R.string.registerEdit);
        }
    }

    public void reserve(View view) {
        String date = etDate.getText().toString();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha;
        try {
            fecha = formato.parse(date);
            Log.i("RegisterReserve", "reserve -> " + date + " Date: " + fecha.toString());
        } catch (ParseException e) {
            Log.e("RegisterReserve", "Formato incorrecto de fecha");
            return;
        }

        ReserveDTO reserveDTO = new ReserveDTO(userID, space.getId(),fecha);
        Reserve reserve = reserveMapper(reserveDTO);
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        if(editReserve != null){
            reserve.setId_user(editReserve.getId_user());
            reserve.setId(editReserve.getId());
            db.getReserveDAO().update(reserve);
            Intent intent = new Intent(this, ListReserves.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            try{
                db.getReserveDAO().insert(reserve);
                Toast.makeText(this, R.string.correctRegister, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, DetailWorkplace.class);
                intent.putExtra("userID", userID);
                intent.putExtra("username", username);
                intent.putExtra("workplace", workPlace);
                startActivity(intent);
            } catch (SQLiteConstraintException sce) {
                Snackbar.make(etDate, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        }
    }

    public void dateSelector(View view) {
        if (view.getId() == R.id.reserveRegisterDate) {
            showDatePickerDialog();
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque enero es el mes 0
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                etDate.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void back(View view){
        Intent intent;
        if(editReserve != null){
            intent = new Intent(this, ListReserves.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
        } else {
            intent = new Intent(this, DetailWorkplace.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            intent.putExtra("workplace", workPlace);
        }
        startActivity(intent);
    }
}