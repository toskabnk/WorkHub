package com.svalero.workhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.workhub.domain.WorkPlace;

public class WorkplaceDetailMap extends AppCompatActivity implements Style.OnStyleLoaded {

    private double gpsLatitude;
    private double gosLongitude;
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    private String username;
    private Long userID;
    private WorkPlace workplace;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplace_detail_map);

        //Conseguir usuario logueado
        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("username");
        userID = intentFrom.getLongExtra("userID", 0L);
        workplace = (WorkPlace) intentFrom.getSerializableExtra("workplace");

        mapView = findViewById(R.id.mapViewDetail);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, this);
        initializePointAnnotationManager();

    }

    private void initializePointAnnotationManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        addMarker(workplace.getLatitude(), workplace.getLongitude(), workplace.getName(), R.mipmap.red_marker);
        setCameraPosition(workplace.getLatitude(), workplace.getLongitude());
        gps();
    }

    private void addMarker(double latitude, double longitude, String title, int id) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude))
                .withIconImage(BitmapFactory.decodeResource(getResources(), id))
                .withTextField(title);
        pointAnnotationManager.create(pointAnnotationOptions);
    }

    private void setCameraPosition(double latitude, double longitude) {
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .pitch(45.0)
                .zoom(15.5)
                .bearing(-17.6)
                .build();
        mapView.getMapboxMap().setCamera(cameraPosition);
    }

    public void back(View view){
        Intent intent = new Intent(this, DetailWorkplace.class);
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        intent.putExtra("workplace", workplace);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void gps() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        gosLongitude = location.getLongitude();
                        gpsLatitude = location.getLatitude();
                        Log.i("gps: ", "+++++++++++");
                        Log.i("gps: ", String.valueOf(location.getLongitude()));
                        Log.i("gps: ", String.valueOf(location.getLatitude()));
                        Log.i("gps: ", String.valueOf(location));

                        addMarker(gpsLatitude, gosLongitude, getString(R.string.gpsMe), R.mipmap.blue_marker_icons_foreground);

                    }
                });

    }
}