package com.example.onboarding1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,    //to the that the connection succesful
        GoogleApiClient.OnConnectionFailedListener,    //when the connection is faild
        LocationListener    //when the location changed
{
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    static final int Request_User_Location_Code = 99;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        SharedPreferences.Editor editor = getSharedPreferences("maps", MODE_PRIVATE).edit();
        editor.putString("add", "");
        editor.commit();

        sliderAdapter adapter = new sliderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
    }

  /*  public void onClick(View view) {
        final EditText addressField = findViewById(R.id.enter_address);
        String address = addressField.getText().toString();
        List<Address> addressList = null;
        MarkerOptions userMarkerOptions = new MarkerOptions();
        //Button signup=findViewById(R.id.sinnp);


        if (!TextUtils.isEmpty(address)) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(address, 1);

                if (addressList != null && !addressList.isEmpty()) {
                    currentUserLocationMarker.remove();
                    for (int i = 0; i < addressList.size(); i++) {
                        Address userAddress = addressList.get(i);
                        LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                        userMarkerOptions.position(latLng);
                        userMarkerOptions.title(address);
                        userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                        mMap.addMarker(userMarkerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                    }
                    //signup.setEnabled(true);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "הכתובת לא נמצאה", Snackbar.LENGTH_LONG).show();
                    //signup.setEnabled(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //signup.setEnabled(false);
                Snackbar.make(findViewById(android.R.id.content), "הכתובת לא נמצאה", Snackbar.LENGTH_LONG).show();
            }

            //when the api return number of results, the user will pick one...
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    addressField.setText(marker.getTitle());
                    return false;
                }
            });
        } else {
            Snackbar.make(findViewById(android.R.id.content), "נא הכנס כתובת", Snackbar.LENGTH_LONG).show();
        }
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }




       /* // Add a marker in israel and move the camera
        LatLng sydney = new LatLng(32.017136, 34.745441);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Israel"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

    }


    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            return false;
        } else return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Button signup=findViewById(R.id.sinnp);
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null)
                            buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("maps", MODE_PRIVATE).edit();
                    editor.putString("add", "נא הפעל GPS");
                    editor.commit();
                    //signup.setEnabled(false);
                    Snackbar.make(findViewById(android.R.id.content), "Permission Denied...", Snackbar.LENGTH_LONG).show();
                }
                return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        //Button signup=findViewById(R.id.sinnp);

        if (currentUserLocationMarker != null)   //the location connect to another location
        {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        List<Address> addressList = null;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);     //move the current address to the list
            String address = (addressList.get(0).getAddressLine(0) + " " + addressList.get(0).getAddressLine(1) + " " + addressList.get(0).getAddressLine(2)).replaceAll("null","");


            SharedPreferences.Editor editor = getSharedPreferences("maps", MODE_PRIVATE).edit();
            editor.clear();
            editor.putString("add",address);
            editor.commit();


            //signup.setEnabled(true);
        } catch (IOException e) {
            Snackbar.make(findViewById(android.R.id.content), "לא ניתן למצוא את מיקומך הנוכחי", Snackbar.LENGTH_LONG).show();
            //signup.setEnabled(false);
        }

        markerOptions.title("מיקומך הנוכחי");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        currentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);


    }

    @Override
    //this func call whenever the device connected
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);  //milisecond
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    //this func call whenever the connection is failed...
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        SharedPreferences.Editor editor = getSharedPreferences("maps", MODE_PRIVATE).edit();
        editor.putString("add", "נא הפעל GPS");
        editor.apply();    }
}
