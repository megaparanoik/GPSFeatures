package com.example.gpsfeatures;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.location.Address;
import android.location.OnNmeaMessageListener;
import android.location.GpsStatus;
import android.content.ContextWrapper;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GpsStatus.NmeaListener, LocationListener {

    Button btnCheck;
    Button btnGetExtras;
    EditText messagebox;
    Address addr;
    Locale locale;
    String TAG = "GPS Feature APP";
    LocationManager locman;
    Location loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheck = findViewById(R.id.id_btnChk);
        btnGetExtras = findViewById(R.id.id_getExtras);
        messagebox = findViewById(R.id.id_editText);

        btnCheck.setOnClickListener(this);
        btnGetExtras.setOnClickListener(this);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            //ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
            Log.e(TAG, "GPS location is DENIED");
        } else {
            Log.e(TAG, "GPS peremission granted!");
        }

        locman = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            locman.addNmeaListener(this);
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage());
        }

        locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        loc = new Location(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_btnChk:
                try {
                    messagebox.setText("");
                } catch (RuntimeException e){
                    Log.e(TAG, "Error " + e.getMessage());
                }
                break;
            case R.id.id_getExtras:
                Log.i(TAG, "Get Extras");

                try {
                    Bundle bundle = new Bundle();
                    bundle = loc.getExtras();

                    //messagebox.setText(String.format("\n%s\n", bundle.toString()));
                    Toast.makeText(this, "M:" + bundle.toString(), Toast.LENGTH_LONG).show();

                } catch (RuntimeException e){
                    Log.e(TAG, "Error " + e.getMessage());
                }
                break;
        }

    }

    @Override
    public void onNmeaReceived(long timestamp, final String nmea) {
        final String out;
        if(nmea.contains("b5 62")) {
            out = String.format("%s\n", nmea);
        } else {
            out = nmea;
        }
        messagebox.post(new Runnable() {
            @Override
            public void run() {
                messagebox.append(String.format("%s", out));
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
