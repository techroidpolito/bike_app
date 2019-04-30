package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.lab1.R;

import androidx.appcompat.app.AppCompatActivity;

public class PositioningActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positioning);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
        }
    }
}
