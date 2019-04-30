package com.example.lab1.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import com.example.lab1.R;
import com.example.lab1.adapter.DashboardAdapter;
import com.example.lab1.model.DashboardAdapterModel;
import com.example.lab1.util.interfaces.OnItemClickListener;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity implements OnItemClickListener {
    private int DELIVERY = 1;
    private int POSITION = 2;
    private int PROFILE = 3;
    private ActionBar toolbar;
    private RecyclerView userRecyclerView;
    private DashboardAdapter mAdapter;
    private ArrayList<DashboardAdapterModel> dashboardAdapterModel = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    @SuppressLint("ResourceAsColor")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = getSupportActionBar();
        toolbar.setTitle(getResources().getString(R.string.app_name));
        userRecyclerView = findViewById(R.id.userRecyclerView);
        setAdapter();
    }

    private void setAdapter() {
        dashboardAdapterModel.add(new DashboardAdapterModel(DELIVERY,R.color.card_bg,R.drawable.delivery_icon,"Current delivery (to update)"));
        dashboardAdapterModel.add(new DashboardAdapterModel(POSITION,R.color.green_800,R.drawable.position_icon,"Location"));
        dashboardAdapterModel.add(new DashboardAdapterModel(PROFILE,R.color.blue_300,R.drawable.profile_icon,"Profile"));
        mLayoutManager = new GridLayoutManager(DashboardActivity.this,2);
        mAdapter = new DashboardAdapter(dashboardAdapterModel);
        userRecyclerView.setLayoutManager(mLayoutManager);
        userRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(DashboardActivity.this);
    }

    @Override
    public void onItemClick(int position) {
        if(dashboardAdapterModel.get(position).getLayoutType()==DELIVERY){
            //Intent i = new Intent(DashboardActivity.this, UserProfileNoEditActivity.class);
            //startActivity(i);
        }
        if(dashboardAdapterModel.get(position).getLayoutType()==POSITION){
            Intent i = new Intent(DashboardActivity.this, PositioningActivity.class);
            startActivity(i);
        }
        if(dashboardAdapterModel.get(position).getLayoutType()==PROFILE){
            Intent i = new Intent(DashboardActivity.this, ProfileNoEditingActivity.class);
            startActivity(i);
        }
    }
}
