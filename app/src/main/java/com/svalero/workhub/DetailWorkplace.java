package com.svalero.workhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.svalero.workhub.domain.WorkPlace;

import java.util.List;

public class DetailWorkplace extends AppCompatActivity {

    private String username;
    private Long userID;
    private WorkPlace workplace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_workplace);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);
        workplace = (WorkPlace) intentFrom.getSerializableExtra("workplace");

        if(workplace != null){
            fillData(workplace);
        } else {
            return;
        }
    }

    private void fillData(WorkPlace workPlace){
        TextView tName = findViewById(R.id.detailsWorkplaceName);
        TextView tDescription = findViewById(R.id.detailsWorkplaceDescription);
        TextView tPhone = findViewById(R.id.detailsWorkplacePhone);
        TextView tCity = findViewById(R.id.detailsWorkplaceCity);
        TextView tAddress = findViewById(R.id.detailsWorkplaceAddress);
        TextView tSchedule = findViewById(R.id.detailsWorkplaceSchedule);

        tName.setText((workPlace.getName()));
        tDescription.setText(workPlace.getDescription());
        tPhone.setText(workPlace.getPhoneNumber());
        tCity.setText(workPlace.getCity());
        tAddress.setText(workPlace.getAddress());
        tSchedule.setText(workPlace.getSchedule());
    }

    public void back(View view){
        Intent intent = new Intent(this, ListWorkplaces.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}