package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.svalero.workhub.adapter.ReservesAdapter;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.Reserve;
import com.svalero.workhub.domain.User;

import java.util.ArrayList;
import java.util.List;

public class ListReserves extends AppCompatActivity {

    private List<Reserve> reserves;
    private ReservesAdapter adapter;
    private String username;
    private Long userID;
    private boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reserves);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);

        Log.i("ListWorkplaces", "ListWorkplaces - Intent Username: " + username);
        Log.i("ListWorkplaces", "ListWorkplaces - Intent userID: " + userID);

        reserves = new ArrayList<>();

        //Conseguir si el usuario es admin de la base de datos
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        try{
            User user = db.getUserDAO().getById(userID);
            admin = user.getAdmin();
        } catch (SQLiteConstraintException sce){

        }

        //RV
        RecyclerView recyclerView = findViewById(R.id.rvListReservers);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ReservesAdapter(this, reserves, intentFrom, admin);
        recyclerView.setAdapter(adapter);
    }

    public void onResume(){
        super.onResume();
        reserves.clear();
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        if(admin){
            reserves.addAll(db.getReserveDAO().getAll());
        } else {
            reserves.addAll(db.getReserveDAO().getReserveByUser(userID));
        }
        adapter.notifyDataSetChanged();
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}