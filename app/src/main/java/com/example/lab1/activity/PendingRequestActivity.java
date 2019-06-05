package com.example.lab1.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.adapter.PendingRequestAdapter;
import com.example.lab1.model.PendingRequestAdapterModel;
import com.example.lab1.model.ProfileInfo;
import com.example.lab1.util.interfaces.AcceptClickItemListener;
import com.example.lab1.util.interfaces.RejectClickItemListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class PendingRequestActivity extends AppCompatActivity implements AcceptClickItemListener, RejectClickItemListener {
    private Toolbar toolbar;
    private RecyclerView userRecyclerView;
    private RelativeLayout exceptionRl;
    private NavigationView navView;
    private DrawerLayout dLayout;
    private LinearLayout headerView;
    private TextView drawerTv;
    private CircleImageView drawerIv;
    private PendingRequestAdapter mAdapter;
    private ArrayList<PendingRequestAdapterModel> pendingRequestAdapterModel;
    private RecyclerView.LayoutManager mLayoutManager;
    private String bikerId;
    private DatabaseReference bikerReference;
    private DatabaseReference bikerStatusReference;
    private StorageReference storageReference;
    private DatabaseReference ordersReference;
    private ProfileInfo bikerModel;
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private static final String TAG = PendingRequestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);
        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        userRecyclerView = findViewById(R.id.pendingRequestsRecyclerView);
        exceptionRl = findViewById(R.id.exceptionRl);

        pendingRequestAdapterModel = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference();
        ordersReference = FirebaseDatabase.getInstance().getReference(getString(R.string.order_details));

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        getDeviceLocation();

        if (savedInstanceState != null) {
            pendingRequestAdapterModel = (ArrayList<PendingRequestAdapterModel>) savedInstanceState.getSerializable("requests");
            if (pendingRequestAdapterModel != null && pendingRequestAdapterModel.size() > 0) {
                setAdapter();
            } else {
                exceptionRl.setVisibility(View.VISIBLE);
                userRecyclerView.setVisibility(View.GONE);
            }

            bikerId = savedInstanceState.getString("bikerId");
        } else {
            if (bikerId == null) {
                bikerId = getIntent().getStringExtra(getString(R.string.bikerID));
            }
            setAdapterItem();
        }

        bikerStatusReference = FirebaseDatabase.getInstance().getReference(getString(R.string.biker_status)).child(bikerId);
        bikerReference = FirebaseDatabase.getInstance().getReference(getString(R.string.biker_details)).child(bikerId);
        bikerReference.child(getString(R.string.bikerIsAvailable)).setValue(true);

        ordersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingRequestAdapterModel = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String order_id = childDataSnapshot.getKey();
                    Log.d(TAG, order_id + " : " + childDataSnapshot.getValue()); //displays the key for the node

                    HashMap<String,Object> order = (HashMap<String, Object>) childDataSnapshot.getValue();
                    String fid = String.valueOf(order.get(getString(R.string.foodtype_id)));
                    String bid = String.valueOf(order.get(getString(R.string.bikerID)));
                    Boolean rider_accepted = (Boolean) order.get(getString(R.string.rider_accepted));
                    Boolean delivered_status = (Boolean) order.get(getString(R.string.delivered_status));

                    if( bid.equals(bikerId) && !rider_accepted  && !delivered_status){

                        double restaurant_latitude = Double.parseDouble(String.valueOf(order.get(getString(R.string.restaurant_latitude))));
                        double restaurant_longitude = Double.parseDouble(String.valueOf(order.get(getString(R.string.restaurant_longitude))));
                        double customer_latitude = Double.parseDouble(String.valueOf(order.get(getString(R.string.customer_latitude))));
                        double customer_longitude = Double.parseDouble(String.valueOf(order.get(getString(R.string.customer_longitude))));
                        String restaurant_address = getPlaceName(restaurant_latitude,restaurant_longitude);
                        String customer_address = getPlaceName(customer_latitude,customer_longitude);
                        Log.d("location",mLastKnownLocation.getLatitude()+" "+mLastKnownLocation.getLongitude());
                        Log.d("ra",restaurant_address);
                        Log.d("ca",customer_address);

                        DecimalFormat df = new DecimalFormat("#.##");
                        float[] to_restaurant = new float[5];
                        Location.distanceBetween(
                                mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude(),
                                restaurant_latitude,
                                restaurant_longitude,
                                to_restaurant);
                        float distance_to_restaurant = Float.parseFloat(df.format(to_restaurant[0]/1000)); //to convert mters in kilometers
                        Log.d("2restaurant",distance_to_restaurant+"");

                        float[] to_customer = new float[5];
                        Location.distanceBetween(
                                restaurant_latitude,
                                restaurant_longitude,
                                customer_latitude,
                                customer_longitude,
                                to_customer);
                        float distance_to_customer = Float.parseFloat(df.format(to_customer[0]/1000)); //to convert mters in kilometers
                        Log.d("2customer",distance_to_customer+"");

                        pendingRequestAdapterModel.add(
                                new PendingRequestAdapterModel(
                                        String.valueOf(order.get(getString(R.string.restaurant_name))),
                                        restaurant_address,
                                        restaurant_latitude,
                                        restaurant_longitude,
                                        distance_to_restaurant,
                                        String.valueOf(order.get(getString(R.string.restaurant_phone))),
                                        String.valueOf(order.get(getString(R.string.customer_name))),
                                        customer_address,
                                        customer_latitude,
                                        customer_longitude,
                                        distance_to_customer,
                                        String.valueOf(order.get(getString(R.string.customer_phone))),
                                        String.valueOf(order.get(getString(R.string.payment_type))),
                                        Float.parseFloat(String.valueOf(order.get(getString(R.string.price)))),
                                        0,
                                        order_id,
                                        fid
                                )
                        );
                    }
                }
                setAdapterItem();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed ", databaseError.getMessage());
            }
        });

        setNavigationDrawer();
    }

    private void setNavigationDrawer() {
        dLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navigationView);
        headerView = (LinearLayout) navView.getHeaderView(0);
        drawerTv = headerView.findViewById(R.id.drawerHeaderTv);
        drawerIv = headerView.findViewById(R.id.drawerHeaderIv);

        bikerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> biker_al = new ArrayList<>();

                biker_al.add(dataSnapshot.child(getString(R.string.bikerFirstName)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerLastName)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerPhone)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerEmail)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerCodiceFiscale)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerDescription)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerPictureUri)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerIsAvailable)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerLatitude)).getValue()+"");
                biker_al.add(dataSnapshot.child(getString(R.string.bikerLongitude)).getValue()+"");

                bikerModel = new ProfileInfo(biker_al);
                bikerModel.setBikerID(bikerId);

                drawerTv.setText(bikerModel.getUsername());
                if(bikerModel.getProfile_picture_uri() != null) {
                    setDrawerProfilePicture(bikerModel.getProfile_picture_uri());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                drawerTv.setText("");
                Log.e("The read failed ", databaseError.getMessage());
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dLayout.openDrawer(GravityCompat.START);
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.profileItem) {
                    Intent i = new Intent(PendingRequestActivity.this, ProfileNoEditingActivity.class);
                    i.putExtra("bikerId",bikerId);
                    i.putExtra(getString(R.string.biker_profile_data),bikerModel);
                    startActivity(i);
                    dLayout.closeDrawers();
                    return true;
                } else if (itemId == R.id.logoutItem) {
                    //Log out
                    Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PendingRequestActivity.this, LoginScreenActivity.class);
                    startActivity(i);
                    dLayout.closeDrawers();
                    finish();
                    return true;
                } else {
                    dLayout.closeDrawers();
                    return false;
                }
            }
        });
    }

    private void setAdapterItem(){
        if (pendingRequestAdapterModel != null && pendingRequestAdapterModel.size() > 0) {
            setAdapter();
        }
        else{
            exceptionRl.setVisibility(View.VISIBLE);
            userRecyclerView.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        exceptionRl.setVisibility(View.GONE);
        userRecyclerView.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(PendingRequestActivity.this);
        mAdapter = new PendingRequestAdapter(pendingRequestAdapterModel,PendingRequestActivity.this);
        userRecyclerView.setLayoutManager(mLayoutManager);
        userRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setAcceptClickItemListener(PendingRequestActivity.this);
        mAdapter.setRejectClickItemListener(PendingRequestActivity.this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("requests", pendingRequestAdapterModel);
        outState.putString("bikerId",bikerId);
    }

    @Override
    public void onAcceptClick(int position) {
        pendingRequestAdapterModel.get(position).setAccept(true);
        mAdapter.notifyItemChanged(position);

        String order_id = pendingRequestAdapterModel.get(position).getOrder_id();
        DatabaseReference currentOrderReference = ordersReference.child(order_id);

        currentOrderReference.child(getString(R.string.order_status)).setValue(getString(R.string.order_status_4)); //4 = rider accepted
        currentOrderReference.child(getString(R.string.rider_accepted)).setValue(true);
        currentOrderReference.child(getString(R.string.bikerPhone)).setValue(bikerModel.getPhone_nb());
        currentOrderReference.child(getString(R.string.bikerEmail)).setValue(bikerModel.getEmail_address());
        currentOrderReference.child(getString(R.string.bikerLatitude)).setValue(mLastKnownLocation.getLatitude()+"");
        currentOrderReference.child(getString(R.string.bikerLongitude)).setValue(mLastKnownLocation.getLongitude()+"");
        if (bikerModel.getProfile_picture_uri() != "null" && bikerModel.getProfile_picture_uri() != null) {
            currentOrderReference.child(getString(R.string.bikerPictureUri)).setValue(bikerModel.getProfile_picture_uri());
        }

        Intent intent = new Intent(PendingRequestActivity.this, RidingToRestaurantActivity.class);
        intent.putExtra("info",pendingRequestAdapterModel.get(position));
        intent.putExtra("bikerId",bikerId);
        bikerStatusReference.child(getString(R.string.bikerIsAvailable)).setValue(false);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRejectClick(int position) {
        pendingRequestAdapterModel.get(position).setReject(true);
        mAdapter.notifyItemChanged(position);

        String order_id = pendingRequestAdapterModel.get(position).getOrder_id();
        ordersReference.child(order_id).child(getString(R.string.bikerID)).setValue(null);
        ordersReference.child(order_id).child(getString(R.string.order_status)).setValue(getString(R.string.order_status_5)); //5 = rider rejected
    }

    private void setDrawerProfilePicture(String picture_path){
        Glide.with(this)
                .load(storageReference.child( getString(R.string.biker_profile_image_folder)+picture_path ))
                .into(drawerIv);
    }

    private String getPlaceName(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(PendingRequestActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0);
            return address;
        } else {
            return null;
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
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "Current location is null. Using defaults.");
                        } else {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            String location = String.valueOf(mLastKnownLocation.getLatitude())+"°, "+String.valueOf(mLastKnownLocation.getLongitude())+"°";
                            Log.d("location",location);
                            bikerStatusReference.child(getString(R.string.bikerLatitude)).setValue(mLastKnownLocation.getLatitude()+"");
                            bikerStatusReference.child(getString(R.string.bikerLongitude)).setValue(mLastKnownLocation.getLongitude()+"");
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

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
}