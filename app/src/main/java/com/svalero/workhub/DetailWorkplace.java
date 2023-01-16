package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.svalero.workhub.adapter.SpacesAdapter;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.Space;
import com.svalero.workhub.domain.User;
import com.svalero.workhub.domain.WorkPlace;

import java.util.ArrayList;
import java.util.List;

public class DetailWorkplace extends AppCompatActivity {

    private String username;
    private Long userID;
    private WorkPlace workplace;
    private List<Space> spaces;
    private SpacesAdapter adapter;
    private Boolean admin;

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

        spaces = new ArrayList<>();

        //Conseguir si el usuario es admin de la base de datos
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        try{
            User user = db.getUserDAO().getById(userID);
            admin = user.getAdmin();
        } catch (SQLiteConstraintException sce){

        } finally {
            db.close();
        }

        Log.i("DetailWorkplace", "onCreate - Workplace: " + workplace.toString());

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvSpaces);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SpacesAdapter(this, spaces, intentFrom, admin, workplace);
        recyclerView.setAdapter(adapter);
    }

    public void onResume(){
        super.onResume();
        spaces.clear();
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        spaces.addAll(db.getSpaceDAO().getSpacesByWorklace(workplace.getId()));
        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_details_workplace, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.addSpace){
            Intent intent = new Intent(this, RegisterSpace.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            intent.putExtra("workplace", workplace);
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.menuWorkplaces){
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

    public void seeMap(View view){
        Intent intent = new Intent(this, WorkplaceDetailMap.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        intent.putExtra("workplace", workplace);
        startActivity(intent);
    }
}