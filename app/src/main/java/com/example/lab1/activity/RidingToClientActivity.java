package com.example.lab1.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lab1.GMapGeocode;
import com.example.lab1.GMapV2Direction;
import com.example.lab1.R;
import com.example.lab1.model.PendingRequestAdapterModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RidingToClientActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button deliveredButton;
    private CardView clientCv, hiddenCv;
    private MapFragment mapFragment;
    private TextView clientNameTv, clientAddressTv, clientPhoneNumberTv, paymentTypeTv, priceTv, orderNumberTv;
    private RelativeLayout clientPhoneRl;
    private PendingRequestAdapterModel deliveryAdapterModel;
    private DatabaseReference orderReference;
    private String bikerId;

    private static final String TAG = RidingToClientActivity.class.getSimpleName();
    private GoogleMap mMap;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng mDefaultLocation = new LatLng(45.054692, 7.659687); //should be restaurant location, by default
    private LatLng mClientLocation = new LatLng(45.062450,7.662360); // student at Polito, by default
    private GMapV2Direction md;
    private GMapGeocode mg;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_CALL_PHONE = 2;
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
        deliveredButton = findViewById(R.id.arrivalButton);
        clientCv = findViewById(R.id.destinationCardView);
        hiddenCv = findViewById(R.id.clientInfoCardView);
        clientNameTv = clientCv.findViewById(R.id.nameTextView);
        clientAddressTv = clientCv.findViewById(R.id.addressTextView);
        clientPhoneNumberTv = clientCv.findViewById(R.id.phoneNumberTextView);
        clientPhoneRl = clientCv.findViewById(R.id.phoneNumberRelativeLayout);
        paymentTypeTv = clientCv.findViewById(R.id.paymentTypeTextView);
        priceTv = clientCv.findViewById(R.id.priceTextView);
        orderNumberTv = clientCv.findViewById(R.id.orderNumberTextview);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        md = new GMapV2Direction(getResources().getString(R.string.google_direction_key), getApplicationContext());
        mg = new GMapGeocode(getResources().getString(R.string.google_api_key), getApplicationContext());

        if(getIntent().getSerializableExtra("info")!=null){
            deliveryAdapterModel = (PendingRequestAdapterModel) getIntent().getSerializableExtra("info");
            setInfo();
        }

        if (savedInstanceState == null) {
            setClientCoordinates();
        }

        orderReference = FirebaseDatabase.getInstance().getReference(getString(R.string.order_details)).child( deliveryAdapterModel.getOrder_id() );

        deliveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderReference.child(getString(R.string.order_status)).setValue(String.valueOf(7)); //6 = order delivered
                orderReference.child(getString(R.string.delivered_status)).setValue(String.valueOf(true));

                Intent i = new Intent(RidingToClientActivity.this, PendingRequestActivity.class);
                i.putExtra(getString(R.string.bikerID),bikerId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

    }

    private void setInfo() {
        clientNameTv.setText(deliveryAdapterModel.getClientName());
        clientAddressTv.setText(deliveryAdapterModel.getClientAddress());
        clientPhoneNumberTv.setText(deliveryAdapterModel.getClientPhoneNumber());
        paymentTypeTv.setText(deliveryAdapterModel.getPaymentType());
        String price = Float.toString(deliveryAdapterModel.getPrice()) + " € ";
        priceTv.setText(price);
        orderNumberTv.setVisibility(View.VISIBLE);
        String orderNumber = "Order n° " + Integer.toString(deliveryAdapterModel.getOrderNumber());
        orderNumberTv.setText(orderNumber);

        hiddenCv.setVisibility(View.GONE);

        deliveredButton.setText(R.string.order_delivered);
        if (deliveryAdapterModel.getClientDistance() < 50.0) { //condition to adapt to the distance unit
            deliveredButton.setVisibility(View.VISIBLE);
        } else {
            deliveredButton.setVisibility(View.GONE);
        }

        clientPhoneRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+deliveryAdapterModel.getClientPhoneNumber()));

                if (ActivityCompat.checkSelfPermission(RidingToClientActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(RidingToClientActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CALL_PHONE);
                    return;
                }
                startActivity(callIntent);
            }
        });
    }

    private void setClientCoordinates(){
        //String clientAddress = deliveryAdapterModel.getClientAddress();
        mClientLocation = new LatLng(deliveryAdapterModel.getClientLatitude(),deliveryAdapterModel.getClientLongitude());
        /*
        LatLng client_coordinates = null;
        String[] info = {clientAddress};
        try {
            client_coordinates = mg.execute(info).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (client_coordinates == null){
            Log.d("coordinates are null", String.valueOf(true));
        } else {
            Log.d("coordinates are null", String.valueOf(false));
            Log.d("coordinates", String.valueOf(client_coordinates));
            mClientLocation = client_coordinates;
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("delivery", deliveryAdapterModel);
        outState.putString("bikerId",bikerId);
        outState.putParcelable("client_coordinates",mClientLocation);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bikerId = savedInstanceState.getString("bikerId");
        deliveryAdapterModel = (PendingRequestAdapterModel) savedInstanceState.getSerializable("delivery");
        mClientLocation = savedInstanceState.getParcelable("client_coordinates");
        if(deliveryAdapterModel!=null) {
            setInfo();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(mClientLocation).title(deliveryAdapterModel.getClientName()));
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
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            String location = String.valueOf(mLastKnownLocation.getLatitude())+"°, "+String.valueOf(mLastKnownLocation.getLongitude())+"°";
                            Log.d("location",location);

                            LatLng mLastKnownLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            String[] direction = {String.valueOf(mLastKnownLatLng.latitude), String.valueOf(mLastKnownLatLng.longitude), String.valueOf(mClientLocation.latitude), String.valueOf(mClientLocation.longitude),GMapV2Direction.MODE_DRIVING};
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
                                PolylineOptions rectLine = new PolylineOptions().width(10).color(0xff9473b6);
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
