package com.example.lab1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.lab1.R;
import com.example.lab1.model.ProfileInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    Toolbar toolbar;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String bikerId;
    private static final String TAG = ProfileNoEditingActivity.class.getSimpleName();

    final static int edit_request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //profileInfo = new ProfileInfo();
        if (getIntent().getStringExtra("bikerId") != null) {
            bikerId = getIntent().getStringExtra("bikerId");
        } else if (savedInstanceState != null) {
            //profileInfo = new ProfileInfo(Objects.requireNonNull(savedInstanceState.getStringArrayList("profile_info")));
            bikerId = savedInstanceState.getString("bikerId");
        }

        tv_username = findViewById(R.id.userName);
        tv_email_address = findViewById(R.id.emailAddress);
        tv_phone_nb = findViewById(R.id.telephoneNumber);
        tv_description = findViewById(R.id.shortDescription);
        tv_address = findViewById(R.id.address);
        tv_id = findViewById(R.id.identityDocument);
        profile_civ = findViewById(R.id.profilePicture);
        toolbar = findViewById(R.id.noEditToolbar);
        setSupportActionBar(toolbar);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseInstance.getReference("app_title").setValue(R.string.app_name);
        mFirebaseDatabase = mFirebaseInstance.getReference("bikers").child(bikerId);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                profileInfo = dataSnapshot.getValue(ProfileInfo.class);
                setInformation();
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {
                Log.e("The read failed ", databaseError.getMessage());
            }
        });

        //if (profileInfo.isAlready_filled()){
            //setInformation();
        //}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == edit_request && resultCode == RESULT_OK) {
            String action_type = data.getStringExtra("button");
            if (action_type.equals("save")){
                profileInfo = new ProfileInfo(data.getStringArrayListExtra("profile_info"));

                setInformation();
                updateUser(); //database update
            }
        }
    }

    private void setInformation(){
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

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("profile_info",profileInfo.toArrayList());
        outState.putString("bikerId",bikerId);
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
            editing_intent.putExtra("bikerId",bikerId);
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

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileInfo biker = dataSnapshot.getValue(ProfileInfo.class);

                // Check for null
                if (biker == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!");

                profileInfo = biker;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    /**
     * Creating new user node under 'users'
     */
    private void updateUser() {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(bikerId)) {
            bikerId = mFirebaseDatabase.push().getKey();
            Log.e("Unexpected behavior","asked for a new bikerId");
        }

        mFirebaseDatabase.setValue(profileInfo);
        addUserChangeListener();
    }

}