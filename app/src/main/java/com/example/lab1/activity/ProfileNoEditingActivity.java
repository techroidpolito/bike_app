package com.example.lab1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab1.R;
import com.example.lab1.model.ProfileInfo;

import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileNoEditingActivity extends AppCompatActivity {
    TextView tv_username;
    TextView tv_email_address;
    TextView tv_phone_nb;
    TextView tv_description;
    TextView tv_address;
    TextView tv_id;
    CircleImageView profile_civ;
    ProfileInfo profileInfo;
    Button test_button;
    final static int edit_request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profileInfo = new ProfileInfo();

        if (savedInstanceState != null) {
            profileInfo = new ProfileInfo( savedInstanceState.getStringArrayList("profile_info" ));
        }

        tv_username = findViewById(R.id.userName);
        tv_email_address = findViewById(R.id.emailAddress);
        tv_phone_nb = findViewById(R.id.telephoneNumber);
        tv_description = findViewById(R.id.shortDescription);
        tv_address = findViewById(R.id.address);
        tv_id = findViewById(R.id.identityDocument);
        profile_civ = findViewById(R.id.profilePicture);
        test_button = findViewById(R.id.testButton);

        if (profileInfo.isAlready_filled()){
            String username = profileInfo.getUsername();
            if (!username.equals("")) {
                tv_username.setText(username);
            }
            String phone_nb = profileInfo.getPhone_nb();
            if (!phone_nb.equals("")) {
                tv_phone_nb.setText(phone_nb);
            }
            String email_address = profileInfo.getEmail_address();
            if (!email_address.equals("")) {
                tv_email_address.setText(email_address);
            }
            String address = profileInfo.getAddress();
            if (!address.equals("")) {
                tv_address.setText(address);
            }
            String description = profileInfo.getDescription();
            if (!description.equals("")) {
                tv_description.setText(description);
            }
            String identity_document = profileInfo.getIdentity_document();
            if (!identity_document.equals("")) {
                tv_id.setText(identity_document);
            }

            Uri pp_uri = Uri.parse(profileInfo.getProfile_picture_uri());
            setProfilePicture(pp_uri);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == edit_request && resultCode == RESULT_OK) {
            String action_type = data.getStringExtra("button");
            if (action_type.equals("save")){
                profileInfo = new ProfileInfo(data.getStringArrayListExtra("profile_info"));

                //Check if each field is not empty
                String username = profileInfo.getUsername();
                if (!username.equals("")) {
                    tv_username.setText(username);
                }
                String phone_nb = profileInfo.getPhone_nb();
                if (!phone_nb.equals("")) {
                    tv_phone_nb.setText(phone_nb);
                }
                String email_address = profileInfo.getEmail_address();
                if (!email_address.equals("")) {
                    tv_email_address.setText(email_address);
                }
                String address = profileInfo.getAddress();
                if (!address.equals("")) {
                    tv_address.setText(address);
                }
                String description = profileInfo.getDescription();
                if (!description.equals("")) {
                    tv_description.setText(description);
                }
                String identity_document = profileInfo.getIdentity_document();
                if (!identity_document.equals("")) {
                    tv_id.setText(identity_document);
                }

                Uri pp_uri = Uri.parse(profileInfo.getProfile_picture_uri());
                setProfilePicture(pp_uri);
            }
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("profile_info",profileInfo.toArrayList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.i("onCreateOptionsMenu","ok in ProfileNoEditingActivity");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.editButton:
            Intent editing_intent = new Intent(
                    ProfileNoEditingActivity.this,
                    ProfileEditingActivity.class);
            editing_intent.putExtra("profile_info",profileInfo.toArrayList());
            startActivityForResult(editing_intent, edit_request);
        }
        return(super.onOptionsItemSelected(item));
    }

    private void setProfilePicture(Uri picture_uri){
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picture_uri));
            profile_civ.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}