package com.svalero.workhub;

import static android.content.ContentValues.TAG;
import static com.svalero.workhub.db.Constants.DATABASE_NAME;
import static com.svalero.workhub.util.Mapper.workplaceMapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.db.dao.WorkPlaceDAO;
import com.svalero.workhub.domain.WorkPlace;
import com.svalero.workhub.domain.dto.WorkPlaceDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private Bitmap imageBitmap;
    private ImageView imageView;
    private double latitude;
    private double longitude;
    private boolean mapOK = false;
    private boolean imageOK = false;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_MAP_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_workplace);

        checkCameraPermission();

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
        imageView = findViewById(R.id.workplacePhoto);

        if(workPlaceEdit != null){
            etName.setText(workPlaceEdit.getName());
            etDescription.setText(workPlaceEdit.getDescription());
            etPhone.setText(workPlaceEdit.getPhoneNumber());
            etCity.setText(workPlaceEdit.getCity());
            etAddress.setText(workPlaceEdit.getAddress());
            etSchedule.setText(workPlaceEdit.getSchedule());
            button.setText(R.string.registerEdit);
            Uri imageUri = Uri.fromFile(new File(workPlaceEdit.getFilePath()));
            imageView.setImageURI(imageUri);
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

        //Conseguimos la ruta de almacenamiento, si no existe, la creamos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WorkHub");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        //Le ponemos nombre al archivo y la extension
        File imageFile = new File(storageDir, System.currentTimeMillis() + ".jpg");
        Log.i("RegisterWorkplace", "register - filePath: " + imageFile);

        if(workPlaceEdit != null){

            workplace.setId(workPlaceEdit.getId());

            final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            try{
                if(imageOK){
                    //Guardamos el archivo en el almacenamiento
                    saveBitmapToFile(imageBitmap, imageFile);
                    workplace.setFilePath(imageFile.toString());
                } else {
                    workplace.setFilePath(workPlaceEdit.getFilePath());
                }
                if(mapOK){
                    //Escribimos la latitud y longitud
                    workplace.setLatitude(latitude);
                    workplace.setLongitude(longitude);
                } else {
                    workplace.setLatitude(workPlaceEdit.getLatitude());
                    workplace.setLongitude(workPlaceEdit.getLongitude());
                }
                db.getWorkPlaceDAO().update(workplace);
                Toast.makeText(this, R.string.correctEdit, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ListWorkplaces.class);
                intent.putExtra("userID", userID);
                intent.putExtra("username", username);
                startActivity(intent);
            } catch (SQLiteConstraintException sce) {
                Snackbar.make(etName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }

        } else {

            final WorkHubDatabase db = Room.databaseBuilder(this, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            try {
                if(imageOK && mapOK) {

                    //Guardamos el archivo en el almacenamiento
                    saveBitmapToFile(imageBitmap, imageFile);
                    workplace.setFilePath(imageFile.toString());

                    //Escribimos la latitud y longitud
                    workplace.setLatitude(latitude);
                    workplace.setLongitude(longitude);

                    db.getWorkPlaceDAO().insert(workplace);
                    Toast.makeText(this, R.string.correctRegister, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, ListWorkplaces.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    if(!mapOK){
                        Toast.makeText(this, R.string.mapMessage, Toast.LENGTH_LONG).show();
                    }
                    if(!imageOK){
                        Toast.makeText(this, R.string.photoMessage, Toast.LENGTH_LONG).show();
                    }
                }
            } catch (SQLiteConstraintException sce) {
                Snackbar.make(etName, R.string.errorRegister, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        }
    }

    public void back(View view){
        Intent intent = new Intent(this, ListWorkplaces.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void makePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void registerMap(View view){
        Intent intentMap = new Intent(this, SelectMap.class);
        startActivityForResult(intentMap, REQUEST_MAP_CAPTURE);
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},226);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
            imageOK = true;
        }
        if(requestCode == REQUEST_MAP_CAPTURE && resultCode == RESULT_OK){
            latitude = data.getDoubleExtra("latitude", 0.);
            longitude = data.getDoubleExtra("longitude", 0.);
            Log.i("RegisterWorkplace", "onActivityResult - latitude: " + latitude);
            Log.i("RegisterWorkplace", "onActivityResult - longitude: " + longitude);
            Toast.makeText(this, R.string.registerWorkplaceMapOK, Toast.LENGTH_LONG).show();
            mapOK = true;
        }

        if(requestCode == REQUEST_MAP_CAPTURE && resultCode == RESULT_CANCELED){
            Toast.makeText(this, R.string.registerWorkplaceMapNO, Toast.LENGTH_LONG).show();
        }
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        try {
            // Crea un archivo para la imagen en el directorio público de imágenes del almacenamiento externo
            file.createNewFile();

            // Crea un flujo de salida para el archivo
            FileOutputStream fos = new FileOutputStream(file);

            // Comprime el Bitmap y lo escribe en el flujo de salida
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            // Cierra el flujo de salida
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}