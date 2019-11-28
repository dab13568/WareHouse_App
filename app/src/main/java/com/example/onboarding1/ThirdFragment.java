package com.example.onboarding1;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,    //to the that the connection succesful
        GoogleApiClient.OnConnectionFailedListener,    //when the connection is faild
        LocationListener    //when the location changed
{
    ViewPager viewPager;
    TextView back;
    View view;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code =99;


    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_third, container, false);
        viewPager=getActivity().findViewById(R.id.viewPager);

        return view;
    }
    public void onClick(View view)
    {
        final EditText addressField=view.findViewById(R.id.enter_address);
        String address=addressField.getText().toString();
        List<Address> addressList = null;
        MarkerOptions userMarkerOptions = new MarkerOptions();
        //Button signup=findViewById(R.id.sinnp);




        if(!TextUtils.isEmpty(address))
        {
            Geocoder geocoder = new Geocoder(getContext());
            try
            {
                addressList=geocoder.getFromLocationName(address,1);

                if(addressList != null && !addressList.isEmpty())
                {
                    currentUserLocationMarker.remove();
                    for (int i=0; i<addressList.size(); i++)
                    {
                        Address userAddress = addressList.get(i);
                        LatLng latLng = new LatLng(userAddress.getLatitude(),userAddress.getLongitude());

                        userMarkerOptions.position(latLng);
                        userMarkerOptions.title(address);
                        userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                        mMap.addMarker(userMarkerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                    }
                   // signup.setEnabled(true);
                }
                else
                {
                    Snackbar.make(view.findViewById(android.R.id.content), "הכתובת לא נמצאה", Snackbar.LENGTH_LONG).show();
                    //signup.setEnabled(false);
                }
            }

            catch (IOException e)
            {
                e.printStackTrace();
                //signup.setEnabled(false);
                Snackbar.make(view.findViewById(android.R.id.content), "הכתובת לא נמצאה", Snackbar.LENGTH_LONG).show();
            }

            //when the api return number of results, the user will pick one...
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker)
                {
                    addressField.setText(marker.getTitle());
                    return false;
                }
            });
        }
        else
        {
            Snackbar.make(view.findViewById(android.R.id.content), "נא הכנס כתובת", Snackbar.LENGTH_LONG).show();
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

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }




       /* // Add a marker in israel and move the camera
        LatLng sydney = new LatLng(32.017136, 34.745441);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Israel"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        //Button signup=findViewById(R.id.sinnp);
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null)
                            buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                } else
                {
                    //signup.setEnabled(false);
                    Snackbar.make(view.findViewById(android.R.id.content), "Permission Denied...", Snackbar.LENGTH_LONG).show();
                }
                return;
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        lastLocation=location;
        //Button signup=findViewById(R.id.sinnp);

        if(currentUserLocationMarker != null)   //the location connect to another location
        {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        List<Address> addressList = null;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        Geocoder geocoder = new Geocoder(getContext());
        final EditText addressField=view.findViewById(R.id.enter_address);
        try
        {
            addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);     //move the current address to the list
            String address=addressList.get(0).getAddressLine(0)+" "+ addressList.get(0).getAddressLine(1)+" " +addressList.get(0).getAddressLine(2);
            addressField.setText(address);
            //signup.setEnabled(true);
        } catch (IOException e) {
            Snackbar.make(view.findViewById(android.R.id.content), "לא ניתן למצוא את מיקומך הנוכחי", Snackbar.LENGTH_LONG).show();
            //signup.setEnabled(false);
        }

        markerOptions.title("מיקומך הנוכחי");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        currentUserLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

//        if(googleApiClient != null)
//            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);


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

    @Override
    //this func call whenever the device connected
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(2000);  //milisecond
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest);
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    //this func call whenever the connection is failed...
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        //write messege to user
    }

}
