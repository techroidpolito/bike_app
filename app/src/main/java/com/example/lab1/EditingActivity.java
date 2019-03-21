package com.example.lab1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Button save_button = findViewById(R.id.saveButton);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}