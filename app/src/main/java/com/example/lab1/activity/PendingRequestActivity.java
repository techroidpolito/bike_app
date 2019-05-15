package com.example.lab1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab1.R;
import com.example.lab1.adapter.PendingRequestAdapter;
import com.example.lab1.model.PendingRequestAdapterModel;
import com.example.lab1.model.ProfileInfo;
import com.example.lab1.util.interfaces.AcceptClickItemListener;
import com.example.lab1.util.interfaces.RejectClickItemListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    public static final int ACCEPT_CODE = 1;
    private String bikerId;
    private DatabaseReference bikerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);
        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        userRecyclerView = findViewById(R.id.pendingRequestsRecyclerView);
        exceptionRl = findViewById(R.id.exceptionRl);

        if (savedInstanceState != null) {
            pendingRequestAdapterModel = (ArrayList<PendingRequestAdapterModel>) savedInstanceState.getSerializable("requests");
            if (pendingRequestAdapterModel != null && pendingRequestAdapterModel.size() > 0) {
                setAdapter();
            }
            else{
                exceptionRl.setVisibility(View.VISIBLE);
                userRecyclerView.setVisibility(View.GONE);
            }

            bikerId = savedInstanceState.getString("bikerId");
        }
        else {
            setAdapterItem();
        }

        if (bikerId == null){
            bikerId = getIntent().getStringExtra("bikerId");
        }
        bikerReference = FirebaseDatabase.getInstance().getReference("bikers").child(bikerId);
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
                ProfileInfo bikerprofile = dataSnapshot.getValue(ProfileInfo.class);
                drawerTv.setText(bikerprofile.getUsername());
                Uri pictureUri = Uri.parse(bikerprofile.getProfile_picture_uri());
                setDrawerProfilePicture(pictureUri);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                drawerTv.setText("");
                Log.e("The read failed ", databaseError.getMessage());
            }
        });
        // set picture as rider's pp if any

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
                    startActivity(i);
                    dLayout.closeDrawers();
                    return true;
                } else if (itemId == R.id.logoutItem) {
                    //Log out
                    Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                    dLayout.closeDrawers();
                    return true;
                } else {
                    dLayout.closeDrawers();
                    return false;
                }
            }
        });
    }

    private void setAdapterItem(){
        pendingRequestAdapterModel = new ArrayList<>();
        pendingRequestAdapterModel.add(new PendingRequestAdapterModel("Pizzeria di Gratus","via Roma, 42  10129 Torino","011 123 4567",  "Louis", "via Torricelli, 38  10129 Torino","+39 366 93 1234","Cash",25, 127));
        pendingRequestAdapterModel.add(new PendingRequestAdapterModel("Turkish kebab","via Nizza, 13  10129 Torino","011 628 1927",  "Elisa", "via Garibaldi, 97  10128 Torino","+39 366 92 1127","Online",10, 134));
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
        // send notification to restaurant's owner : TO ADD
        // send notification to client : TO ADD
        Intent intent = new Intent(PendingRequestActivity.this, RidingToRestaurantActivity.class);
        intent.putExtra("info",pendingRequestAdapterModel.get(position));
        intent.putExtra("bikerId",bikerId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRejectClick(int position) {
        pendingRequestAdapterModel.get(position).setReject(true);
        mAdapter.notifyItemChanged(position);
    }

    private void setDrawerProfilePicture(Uri picture_uri){
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picture_uri));
            drawerIv.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}