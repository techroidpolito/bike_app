package com.example.lab1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EditingActivity extends Activity {
    Button save_button;
    Button cancel_button;
    ImageButton edit_picture;
    EditText username_et;
    EditText email_address_et;
    EditText phone_nb_et;
    EditText description_et;
    EditText address_et;
    EditText id_et;
    ImageView profileImage;
    String profile_picture_uri;

    ArrayList<String> information_array;
    static int picture_request_code = 2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent i = getIntent();
        information_array = i.getStringArrayListExtra("array");
        profile_picture_uri = i.getStringExtra("profile_picture");

        if (savedInstanceState != null) {
            information_array = savedInstanceState.getStringArrayList("info_array");
            profile_picture_uri = savedInstanceState.getString("profile_picture");
        }

        save_button = findViewById(R.id.saveButton);
        cancel_button = findViewById(R.id.cancelButton);
        edit_picture = findViewById(R.id.edit_profile_picture_button);
        username_et = findViewById(R.id.edit_username);
        email_address_et = findViewById(R.id.edit_email_address);
        phone_nb_et = findViewById(R.id.edit_phone_number);
        description_et = findViewById(R.id.edit_short_description);
        address_et = findViewById(R.id.edit_address);
        id_et = findViewById(R.id.edit_identity_document);
        profileImage = findViewById(R.id.edit_profile_picture);

        if( information_array.size() == 6 ) {
            String username = information_array.get(0);
            if (!username.equals("")) {
                username_et.setText(username);
            }
            String phone_nb = information_array.get(1);
            if (!phone_nb.equals("")) {
                phone_nb_et.setText(phone_nb);
            }
            String email_address = information_array.get(2);
            if (!email_address.equals("")) {
                email_address_et.setText(email_address);
            }
            String address = information_array.get(3);
            if (!address.equals("")) {
                address_et.setText(address);
            }
            String description = information_array.get(4);
            if (!description.equals("")) {
                description_et.setText(description);
            }
            String identity_document = information_array.get(5);
            if (!identity_document.equals("")) {
                id_et.setText(identity_document);
            }
        }

        if( !profile_picture_uri.equals("") ){
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(profile_picture_uri)));
                profileImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                information_array.clear();
                String username = username_et.getText().toString();
                information_array.add(username);
                String phone_nb = phone_nb_et.getText().toString();
                information_array.add(phone_nb);
                String email_address = email_address_et.getText().toString();
                information_array.add(email_address);
                String address = address_et.getText().toString();
                information_array.add(address);
                String description = description_et.getText().toString();
                information_array.add(description);
                String identity_document = id_et.getText().toString();
                information_array.add(identity_document);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("string_array",information_array);
                resultIntent.putExtra("button","save");
                resultIntent.putExtra("picture_uri",profile_picture_uri);

                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("button","cancel");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, picture_request_code);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == picture_request_code) {
                Uri targetUri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    profileImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                profile_picture_uri = targetUri.toString();
            }
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("info_array",information_array);
        outState.putString("profile_picture",profile_picture_uri);
    }
}