package com.example.carpooling;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient ;

    private static final float DEFAULT_ZOOM = 15f;

    private boolean mLocationPermissionsGranted = false;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE =1234;

    //widgets
    private EditText mSearchText;
    private ImageView search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        mSearchText = findViewById(R.id.input_search);
        search = findViewById(R.id.ic_magnify);

        getLocationPermission();

        init();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(mLocationPermissionsGranted)
        {
            getDeviceLocaion();


            mMap.setMyLocationEnabled(true);

            init();
        }

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }



    private void getDeviceLocaion(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            if(mLocationPermissionsGranted) {

                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MapsActivity.this, "Found Location", Toast.LENGTH_SHORT).show();
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,"My Location");
                            //Toast.makeText(MapsActivity.this, "current "+currentLocation.getLatitude(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MapsActivity.this, "Cannot faund", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e)
        {
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void moveCamera(LatLng latLng,float zoom,String title)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionsGranted =true;
                initMap();
            }else
            {
                ActivityCompat.requestPermissions(this,
                        permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else
        {
            ActivityCompat.requestPermissions(this,
                    permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0 ){
                    for(int i=0;i<grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }

                    mLocationPermissionsGranted = true;
                    initMap();
                }

            }
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void init(){
       /* mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        ||actionId == EditorInfo.IME_ACTION_DONE
                        ||event.getAction() == KeyEvent.ACTION_DOWN
                        ||event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    geoLocate();

                }

                return false;
            }
        });*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });
    }

    private void geoLocate(){
        String SearchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();

        try{

            list = geocoder.getFromLocationName(SearchString,1);

        }catch (IOException e)
        {
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

        if(list.size()>0 )
        {
            Address address = list.get(0);

            //Toast.makeText(this, ""+address.getLatitude(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }



}
