package com.example.lab1.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lab1.GMapGeocode;
import com.example.lab1.GMapV2Direction;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import com.example.lab1.R;
import com.example.lab1.model.PendingRequestAdapterModel;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class RidingToRestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button arrivedButton;
    private CardView restaurantCv, clientCv;
    private MapFragment mapFragment;
    private RelativeLayout clientPaymentRl;
    private TextView restaurantNameTv, restaurantAddressTv, restaurantPhoneNumberTv, paymentTypeTv, priceTv;
    private TextView clientNameTv, clientAddressTv, clientPhoneNumberTv;
    private PendingRequestAdapterModel pendingRequestAdapterModel;
    private String bikerId;

    private static final String TAG = RidingToRestaurantActivity.class.getSimpleName();
    private GoogleMap mMap;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng mDefaultLocation = new LatLng(45.054692, 7.659687);
    private LatLng mRestaurantLocation = new LatLng(45.066820,7.668343); // restaurant Amici Miei
    private LatLng mClientLocation = new LatLng(45.062450,7.662360); // student at Polito
    private GMapV2Direction md;
    private GMapGeocode mg_r; //restaurant
    private GMapGeocode mg_c; //client
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bikerId = getIntent().getStringExtra("bikerId");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_riding_with_gps);
        arrivedButton = findViewById(R.id.arrivalButton);
        restaurantCv = findViewById(R.id.destinationCardView);
        restaurantNameTv = restaurantCv.findViewById(R.id.nameTextView);
        restaurantAddressTv = restaurantCv.findViewById(R.id.addressTextView);
        restaurantPhoneNumberTv = restaurantCv.findViewById(R.id.phoneNumberTextView);
        paymentTypeTv = restaurantCv.findViewById(R.id.paymentTypeTextView);
        priceTv = restaurantCv.findViewById(R.id.priceTextView);
        clientCv = findViewById(R.id.clientInfoCardView);
        clientNameTv = clientCv.findViewById(R.id.nameTextView);
        clientAddressTv = clientCv.findViewById(R.id.addressTextView);
        clientPhoneNumberTv = clientCv.findViewById(R.id.phoneNumberTextView);
        clientPaymentRl = clientCv.findViewById(R.id.paymentRelativeLayout);

        arrivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RidingToRestaurantActivity.this, PickingUpActivity.class);
                i.putExtra("info",pendingRequestAdapterModel);
                i.putExtra("bikerId",bikerId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        md = new GMapV2Direction(getResources().getString(R.string.google_direction_key), getApplicationContext());
        mg_r = new GMapGeocode(getResources().getString(R.string.google_api_key), getApplicationContext());
        mg_c = new GMapGeocode(getResources().getString(R.string.google_api_key), getApplicationContext());

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(getIntent().getSerializableExtra("info")!=null){
            pendingRequestAdapterModel = (PendingRequestAdapterModel) getIntent().getSerializableExtra("info");
            setInfo();
        }

        if (savedInstanceState == null) {
            setRestaurantCoordinates();
            setClientCoordinates();
        }

    }

    private void setInfo(){
        restaurantNameTv.setText(pendingRequestAdapterModel.getRestaurantName());
        restaurantAddressTv.setText(pendingRequestAdapterModel.getRestaurantAddress());
        restaurantPhoneNumberTv.setText(pendingRequestAdapterModel.getRestaurantPhoneNumber());
        paymentTypeTv.setText(pendingRequestAdapterModel.getPaymentType());
        String price = Float.toString(pendingRequestAdapterModel.getPrice()) + " € ";
        priceTv.setText(price);

        clientNameTv.setText(pendingRequestAdapterModel.getClientName());
        clientAddressTv.setText(pendingRequestAdapterModel.getClientAddress());
        clientPhoneNumberTv.setText(pendingRequestAdapterModel.getClientPhoneNumber());
        clientPaymentRl.setVisibility(View.GONE);

        if( pendingRequestAdapterModel.getRestaurantDistance() < 1.0 ){ //condition to adapt to the distance unit
            arrivedButton.setVisibility(View.VISIBLE);
        } else {
            arrivedButton.setVisibility(View.GONE);
        }
    }

    private void setRestaurantCoordinates() {
        String restaurantAddress = pendingRequestAdapterModel.getRestaurantAddress();
        LatLng restaurant_coordinates = null;
        String[] info = {restaurantAddress};
        try {
            restaurant_coordinates = mg_r.execute(info).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (restaurant_coordinates == null) {
            Log.d("coordinates are null", String.valueOf(true));
        } else {
            Log.d("coordinates are null", String.valueOf(false));
            Log.d("coordinates", String.valueOf(restaurant_coordinates));
            mRestaurantLocation = restaurant_coordinates;
        }
    }

    private void setClientCoordinates() {
        String clientAddress = pendingRequestAdapterModel.getClientAddress();
        LatLng client_coordinates = null;
        String[] info = {clientAddress};
        try {
            client_coordinates = mg_c.execute(info).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (client_coordinates == null) {
            Log.d("coordinates are null", String.valueOf(true));
        } else {
            Log.d("coordinates are null", String.valueOf(false));
            Log.d("coordinates", String.valueOf(client_coordinates));
            mClientLocation = client_coordinates;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("delivery", pendingRequestAdapterModel);
        outState.putString("bikerId",bikerId);
        outState.putParcelable("restaurant_coordinates",mRestaurantLocation);
        outState.putParcelable("client_coordinates",mClientLocation);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bikerId = savedInstanceState.getString("bikerId");
        pendingRequestAdapterModel = (PendingRequestAdapterModel) savedInstanceState.getSerializable("delivery");
        mRestaurantLocation = savedInstanceState.getParcelable("restaurant_coordinates");
        mClientLocation = savedInstanceState.getParcelable("client_coordinates");

        if(pendingRequestAdapterModel!=null) {
            setInfo();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(mRestaurantLocation).title(pendingRequestAdapterModel.getRestaurantName()));
        googleMap.addMarker(new MarkerOptions().position(mClientLocation).title(pendingRequestAdapterModel.getClientName()));
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            String location = String.valueOf(mLastKnownLocation.getLatitude())+"°, "+String.valueOf(mLastKnownLocation.getLongitude())+"°";
                            Log.d("location",location);

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            LatLng mLastKnownLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            String[] direction = {String.valueOf(mLastKnownLatLng.latitude), String.valueOf(mLastKnownLatLng.longitude), String.valueOf(mRestaurantLocation.latitude), String.valueOf(mRestaurantLocation.longitude),GMapV2Direction.MODE_DRIVING};
                            Document doc = null;
                            try {
                                doc = md.execute(direction).get();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (doc == null){
                                Log.d("is null", String.valueOf(true));
                            } else {
                                Log.d("is null", String.valueOf(false));
                                ArrayList<LatLng> directionPoint = md.getDirection(doc);
                                PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.GREEN);
                                Log.v("nb steps", String.valueOf(directionPoint.size()));

                                for (int i = 0; i < directionPoint.size(); i++) {
                                    rectLine.add(directionPoint.get(i));
                                }
                                Polyline polyline = mMap.addPolyline(rectLine);
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}