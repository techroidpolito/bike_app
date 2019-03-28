package com.example.lab1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ImageButton edit_button;
    TextView tv_username;
    TextView tv_email_address;
    TextView tv_phone_nb;
    TextView tv_description;
    TextView tv_address;
    TextView tv_id;
    CircleImageView profile_civ;
    Uri profile_picture_uri;
    ArrayList<String> information_array;
    final static int edit_request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        information_array = new ArrayList<String>();
        profile_picture_uri = Uri.parse("");

        if (savedInstanceState != null) {
            information_array = savedInstanceState.getStringArrayList("info_array");
            profile_picture_uri = Uri.parse(savedInstanceState.getString("profile_picture"));
        }

        edit_button = findViewById(R.id.editButton);
        tv_username = findViewById(R.id.userName);
        tv_email_address = findViewById(R.id.emailAddress);
        tv_phone_nb = findViewById(R.id.telephoneNumber);
        tv_description = findViewById(R.id.shortDescription);
        tv_address = findViewById(R.id.address);
        tv_id = findViewById(R.id.identityDocument);
        profile_civ = findViewById(R.id.profilePicture);

        if (information_array.size() == 6) {
            String username = information_array.get(0);
            if (!username.equals("")) {
                tv_username.setText(username);
            }
            String phone_nb = information_array.get(1);
            if (!phone_nb.equals("")) {
                tv_phone_nb.setText(phone_nb);
                ;
            }
            String email_address = information_array.get(2);
            if (!email_address.equals("")) {
                tv_email_address.setText(email_address);
            }
            String address = information_array.get(3);
            if (!address.equals("")) {
                tv_address.setText(address);
            }
            String description = information_array.get(4);
            if (!description.equals("")) {
                tv_description.setText(description);
            }
            String identity_document = information_array.get(5);
            if (!identity_document.equals("")) {
                tv_id.setText(identity_document);
            }

            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(profile_picture_uri));
                profile_civ.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editing_intent = new Intent(
                        MainActivity.this,
                        EditingActivity.class);
                editing_intent.putExtra("array",information_array);
                editing_intent.putExtra("profile_picture",profile_picture_uri.toString());
                startActivityForResult(editing_intent, edit_request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == edit_request && resultCode == RESULT_OK) {
            String action_type = data.getStringExtra("button");
            if (action_type.equals("save")){
                information_array = data.getStringArrayListExtra("string_array");

                //Check if each field is not empty
                String username = information_array.get(0);
                if( !username.equals("") ) {
                    tv_username.setText(username);
                }
                String phone_nb = information_array.get(1);
                if( !phone_nb.equals("") ) {
                    tv_phone_nb.setText(phone_nb);;
                }
                String email_address = information_array.get(2);
                if( !email_address.equals("") ) {
                    tv_email_address.setText(email_address);
                }
                String address = information_array.get(3);
                if( !address.equals("") ) {
                    tv_address.setText(address);
                }
                String description = information_array.get(4);
                if( !description.equals("") ) {
                    tv_description.setText(description);
                }
                String identity_document = information_array.get(5);
                if( !identity_document.equals("") ) {
                    tv_id.setText(identity_document);
                }
                profile_picture_uri = Uri.parse( data.getStringExtra("picture_uri") );
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(profile_picture_uri));
                    profile_civ.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("info_array",information_array);
        outState.putString("profile_picture",profile_picture_uri.toString());
    }

}