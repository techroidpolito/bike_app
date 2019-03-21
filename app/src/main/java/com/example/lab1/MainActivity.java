package com.example.lab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ImageButton edit_button = findViewById(R.id.editButton);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editing_intent = new Intent(
                        getApplicationContext(),
                        EditingActivity.class);

                startActivity(editing_intent);
            }
        });
    }
}