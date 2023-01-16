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
import android.widget.CheckBox;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.Preference;
import com.svalero.workhub.domain.User;

public class Preferences extends AppCompatActivity {

    private CheckBox defaultRememberMe;
    private CheckBox defaultautoPlaceMarker;
    private CheckBox defaultDetailCenterMe;
    private Preference preference;
    private String username;
    private Long userID;
    private MenuItem settingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);

        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        try{
            preference = db.getPreferenceDAO().getPreference();
            Log.i("Prefences - onCreate" , "Preferencias cargadas!");
        } catch (SQLiteConstraintException sce) {
            Log.i("Prefences - onCreate" , "No hay preferencias");
            return;
        } finally {
            db.close();
        }

        defaultRememberMe = findViewById(R.id.defaultRememberMe);
        defaultautoPlaceMarker = findViewById(R.id.defaultautoPlaceMarker);
        defaultDetailCenterMe = findViewById(R.id.defaultDetailCenterMe);

        defaultautoPlaceMarker.setChecked(preference.isAutoPlaceMarker());
        defaultRememberMe.setChecked(preference.isRememberMe());
        defaultDetailCenterMe.setChecked(preference.isMapDetailCenterMe());
    }

    public void save(View view){
        final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        Preference newPreference = new Preference();
        newPreference.setId(preference.getId());
        newPreference.setAutoPlaceMarker(defaultautoPlaceMarker.isChecked());
        newPreference.setMapDetailCenterMe(defaultDetailCenterMe.isChecked());
        try {
            if (defaultRememberMe.isChecked()) {

                User user = db.getUserDAO().getById(userID);
                newPreference.setPassword(user.getPassword());
                newPreference.setUsername(username);
                newPreference.setRememberMe(true);
            } else {
                newPreference.setRememberMe(false);
            }

            db.getPreferenceDAO().update(newPreference);
            Snackbar.make(view, R.string.settingsSaved, BaseTransientBottomBar.LENGTH_LONG).show();
        }  catch (SQLiteConstraintException sce) {
            Log.i("Prefences - save" , "Algo ha ocurrido malo");
            Snackbar.make(view, R.string.settingsError, BaseTransientBottomBar.LENGTH_LONG).show();
        } finally {
            db.close();
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
        settingMenu = menu.findItem(R.id.menuSettings);
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