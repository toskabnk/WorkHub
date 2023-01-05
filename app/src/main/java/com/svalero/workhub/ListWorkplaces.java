package com.svalero.workhub;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.adapter.WorkplacesAdapter;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.User;
import com.svalero.workhub.domain.WorkPlace;

import java.util.ArrayList;
import java.util.List;

public class ListWorkplaces extends AppCompatActivity {

    private List<WorkPlace> workplaces;
    private WorkplacesAdapter adapter;
    public String username;
    public Long userID;
    public boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_workplaces);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);

        Log.i("ListWorkplaces", "ListWorkplaces - Intent Username: " + username);
        Log.i("ListWorkplaces", "ListWorkplaces - Intent userID: " + userID);
        //Lista de sitios
        workplaces = new ArrayList<>();

        //Conseguir si el usuario es admin de la base de datos
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        try{
            User user = db.getUserDAO().getById(userID);
            admin = user.getAdmin();
        } catch (SQLiteConstraintException sce){

        }


        //RecyclerView
        RecyclerView recyclerView =findViewById(R.id.rvListWorkplaces);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WorkplacesAdapter(this, workplaces, intentFrom, admin);
        recyclerView.setAdapter(adapter);
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        workplaces.clear();
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        workplaces.addAll(db.getWorkPlaceDAO().getAll());
        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_workplaces, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.addWorkplace){
            Intent intent = new Intent(this, RegisterWorkplace.class);
            intent.putExtra("userID", userID);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }
        return false;
    }
}