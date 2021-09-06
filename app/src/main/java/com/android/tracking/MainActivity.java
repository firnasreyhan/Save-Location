package com.android.tracking;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tracking.adapter.LocationAdapter;
import com.android.tracking.model.LocationModel;
import com.android.tracking.viewmodel.MainViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private LocationManager locationManager;
    private TextView latitude, longitude;
    private MaterialButton cekLokasi, simpan;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Pesan");
        progressDialog.setMessage("Mohon tunggu sebentar...");

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        cekLokasi = findViewById(R.id.cekLokasi);
        simpan = findViewById(R.id.simpan);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if(!hasPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 0);
        }
//        else {
//            toMainActivity();
//        }

        mainViewModel.getLocation().observe(this, new Observer<List<LocationModel>>() {
            @Override
            public void onChanged(List<LocationModel> locationModels) {
                recyclerView.setAdapter(new LocationAdapter(locationModels, mainViewModel));
            }
        });

        cekLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            2000,
                            10, locationListenerGPS);
                    isLocationEnabled();
                }
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationModel locationModel = new LocationModel();
                locationModel.latitude = latitude.getText().toString();
                locationModel.longitude = longitude.getText().toString();

                mainViewModel.insertLocation(locationModel);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED
                        || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Izin diperlukan untuk menggunakan aplikasi", Toast.LENGTH_SHORT).show();
                    finish();
                }
//                else {
//                    toMainActivity();
//                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLocationEnabled();
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + " New Longitude: "+longitude;
//            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            Log.e("location", msg);
//            textView.setText(msg);
            MainActivity.this.latitude.setText(String.valueOf(latitude));
            MainActivity.this.longitude.setText(String.valueOf(longitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("a", "a");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("b", "b");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("c", "c");
        }
    };

    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }

    private boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}