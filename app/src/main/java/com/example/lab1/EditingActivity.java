package com.example.lab1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class EditingActivity extends Activity {
    Button save_button = findViewById(R.id.saveButton);
    ImageButton edit_picture = findViewById(R.id.edit_profile_picture_button);
    ImageButton edit_background = findViewById(R.id.edit_background_button);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedInstanceState.putString("username", "Some value...");
                finish();
            }
        });

        edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });

        edit_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });

    }
}