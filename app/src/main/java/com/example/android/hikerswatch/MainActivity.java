package com.example.android.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView lat,lon,acc,alt,add;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat=(TextView)findViewById(R.id.lat);
        lon=(TextView)findViewById(R.id.lon);
        acc=(TextView)findViewById(R.id.acc);
        alt=(TextView)findViewById(R.id.alt);
        add=(TextView)findViewById(R.id.add);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_LONG).show();


                lat.setText(Double.toString(location.getLatitude()));
                lon.setText(Double.toString(location.getLongitude()));
                alt.setText(Double.toString(location.getAltitude()));
                acc.setText(Double.toString(location.getAccuracy()));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try{
                    List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    if(listAddress!=null && listAddress.size()>0)
                    {
                        String address=" ";
                        if(listAddress.get(0).getThoroughfare()!=null)
                        {
                            address = address +" "+listAddress.get(0).getThoroughfare();
                        }
                        if(listAddress.get(0).getLocality()!=null)
                        {
                            address = address +" "+listAddress.get(0).getLocality();
                        }
                        if(listAddress.get(0).getPostalCode()!=null)
                        {
                            address = address +" "+listAddress.get(0).getPostalCode();
                        }

                        add.setText(address);
                    }


                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }
}
