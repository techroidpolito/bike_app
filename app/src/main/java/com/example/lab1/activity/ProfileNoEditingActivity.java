package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.model.ProfileInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileNoEditingActivity extends AppCompatActivity {
    TextView tv_username, tv_email_address, tv_phone_nb, tv_codice_fiscale, tv_description;
    CircleImageView profile_civ;
    ProfileInfo profileInfo;
    Toolbar toolbar;
    private DatabaseReference mBikerDetailsDatabase;
    private DatabaseReference mBikerStatusDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private StorageReference storageReference;
    private String bikerId;
    private static final String TAG = ProfileNoEditingActivity.class.getSimpleName();

    final static int edit_request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //profileInfo = new ProfileInfo();
        if (getIntent() != null) {
            profileInfo = (ProfileInfo) getIntent().getSerializableExtra(getString(R.string.biker_profile_data));
            bikerId = profileInfo.getBikerID();

        } else if (savedInstanceState != null) {
            profileInfo = (ProfileInfo) savedInstanceState.getSerializable(getString(R.string.biker_profile_data));
            bikerId = profileInfo.getBikerID();
        }

        tv_username = findViewById(R.id.userName);
        tv_email_address = findViewById(R.id.emailAddress);
        tv_phone_nb = findViewById(R.id.telephoneNumber);
        tv_codice_fiscale  = findViewById(R.id.codiceFiscale);
        tv_description = findViewById(R.id.shortDescription);
        profile_civ = findViewById(R.id.profilePicture);
        toolbar = findViewById(R.id.noEditToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home_intent = new Intent(ProfileNoEditingActivity.this, PendingRequestActivity.class);
                home_intent.putExtra(getString(R.string.biker_profile_data),profileInfo);
                home_intent.putExtra(getString(R.string.bikerID),bikerId);
                startActivity(home_intent);
                finish();
            }
        });

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mBikerDetailsDatabase = mFirebaseInstance.getReference(getString(R.string.biker_details)).child(bikerId);
        mBikerDetailsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                profileInfo.setFirstName(String.valueOf(dataSnapshot.child(getString(R.string.bikerFirstName)).getValue()));
                profileInfo.setLastName(String.valueOf(dataSnapshot.child(getString(R.string.bikerLastName)).getValue()));
                profileInfo.setPhone_nb(String.valueOf(dataSnapshot.child(getString(R.string.bikerPhone)).getValue()));
                profileInfo.setEmail_address(String.valueOf(dataSnapshot.child(getString(R.string.bikerEmail)).getValue()));
                profileInfo.setCodiceFiscale(String.valueOf(dataSnapshot.child(getString(R.string.bikerCodiceFiscale)).getValue()));
                if( dataSnapshot.child(getString(R.string.bikerDescription)).getValue()!=null ) {
                    profileInfo.setDescription(String.valueOf(dataSnapshot.child(getString(R.string.bikerDescription)).getValue()));
                }
                if( dataSnapshot.child(getString(R.string.bikerPictureUri)).getValue()!=null ) {
                    profileInfo.setProfile_picture_uri(String.valueOf(dataSnapshot.child(getString(R.string.bikerPictureUri)).getValue()));
                }
                profileInfo.setBiker_completed((Boolean) dataSnapshot.child(getString(R.string.biker_completed)).getValue());
                setInformation();
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {
                Log.e("The read failed ", databaseError.getMessage());
            }
        });

        mBikerStatusDatabase = mFirebaseInstance.getReference(getString(R.string.biker_status)).child(bikerId);
        mBikerStatusDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            //to fix
            public void onDataChange( DataSnapshot dataSnapshot) {
                if( dataSnapshot.child(getString(R.string.bikerPictureUri)).getValue()!=null ) {
                    profileInfo.setProfile_picture_uri(String.valueOf(dataSnapshot.child(getString(R.string.bikerPictureUri)).getValue()));
                }
                profileInfo.setAvailable((Boolean) dataSnapshot.child(getString(R.string.bikerIsAvailable)).getValue());
                profileInfo.setLatitude(String.valueOf(dataSnapshot.child(getString(R.string.bikerLatitude)).getValue()));
                profileInfo.setLongitude(String.valueOf(dataSnapshot.child(getString(R.string.bikerLongitude)).getValue()));
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {
                Log.e("The read failed ", databaseError.getMessage());
            }
        });

        if (profileInfo.isAlready_filled() || profileInfo.getBiker_completed()){
            setInformation();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == edit_request && resultCode == RESULT_OK) {
            String action_type = data.getStringExtra("button");
            if (action_type.equals("save")){
                profileInfo = (ProfileInfo) data.getSerializableExtra(getString(R.string.biker_profile_data));
                bikerId = data.getStringExtra(getString(R.string.bikerID));
                setInformation();
                updateUser(); //database update
            }
        }
    }

    private void setInformation(){
        //Check if each field is not empty
        String username = profileInfo.getFirstName()+" "+profileInfo.getLastName();
        if (profileInfo.getFirstName()!=null && profileInfo.getLastName()!=null) {
            tv_username.setText(username);
        }
        String phone_nb = profileInfo.getPhone_nb();
        if (phone_nb != null) {
            tv_phone_nb.setText(phone_nb);
        }
        String email_address = profileInfo.getEmail_address();
        if (email_address != null) {
            tv_email_address.setText(email_address);
        }
        String codice_fiscale = profileInfo.getCodiceFiscale();
        if (codice_fiscale != null) {
            tv_codice_fiscale.setText(codice_fiscale);
        }
        String description = profileInfo.getDescription();
        if (description != null) {
            tv_description.setText(description);
        }

        String pp_uri = profileInfo.getProfile_picture_uri();
        if (pp_uri != null) {
            Boolean picture_set_up = setProfilePicture(pp_uri);
        }
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
            editing_intent.putExtra(getString(R.string.biker_profile_data),profileInfo);
            editing_intent.putExtra(getString(R.string.bikerID),bikerId);
            startActivityForResult(editing_intent, edit_request);
        }
        return(super.onOptionsItemSelected(item));
    }

    private boolean setProfilePicture(String picture_uri){
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(getString(R.string.biker_profile_image_folder)+picture_uri);
        /*GlideApp.with(this)
                .load(storageReference)
                .into(profile_civ);
        profile_civ.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));

        return true;*/
        return false;
    }

    /**
     * Creating new user node under 'users'
     */
    private void updateUser() {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(bikerId)) {
            bikerId = mBikerDetailsDatabase.push().getKey();
            Log.e("Unexpected behavior","asked for a new bikerId");
        }

        Map<String,Object> biker_dataPlaceholder = new HashMap<>();
        Map<String,Object> biker_entries = new HashMap<>();
        biker_entries.put(getString(R.string.bikerFirstName),profileInfo.getFirstName());
        biker_entries.put(getString(R.string.bikerLastName),profileInfo.getLastName());
        biker_entries.put(getString(R.string.bikerPhone),profileInfo.getPhone_nb());
        biker_entries.put(getString(R.string.bikerEmail),profileInfo.getEmail_address());
        biker_entries.put(getString(R.string.bikerCodiceFiscale),profileInfo.getCodiceFiscale());
        if(profileInfo.getDescription()!=null && profileInfo.getDescription()!="") {
            biker_entries.put(getString(R.string.bikerDescription), profileInfo.getDescription());
        }
        if(profileInfo.getProfile_picture_uri()!=null && profileInfo.getProfile_picture_uri()!="") {
            biker_entries.put(getString(R.string.bikerPictureUri), profileInfo.getProfile_picture_uri());
        }
        biker_entries.put(getString(R.string.biker_completed),true);
        mBikerDetailsDatabase.updateChildren(biker_dataPlaceholder, (databaseError, databaseReference) -> {
            if(databaseError == null) {
                mBikerDetailsDatabase.updateChildren(biker_entries);
            }else {
                Toast.makeText(getApplicationContext(), getString(R.string.internal_error), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Map<String,Object> biker_status_entries = new HashMap<>();
        biker_status_entries.put(getString(R.string.bikerName),profileInfo.getFirstName()+" "+profileInfo.getLastName());
        if(profileInfo.getProfile_picture_uri()!=null && profileInfo.getProfile_picture_uri()!="") {
            biker_status_entries.put(getString(R.string.bikerPictureUri), profileInfo.getProfile_picture_uri());
        }
        biker_status_entries.put(getString(R.string.bikerIsAvailable),profileInfo.isAvailable());
        biker_status_entries.put(getString(R.string.bikerLatitude),profileInfo.getLatitude());
        biker_status_entries.put(getString(R.string.bikerLongitude),profileInfo.getLongitude());
        mBikerStatusDatabase.updateChildren(biker_dataPlaceholder, (databaseError, databaseReference) -> {
            if(databaseError == null) {
                mBikerStatusDatabase.updateChildren(biker_status_entries);
            }else {
                Toast.makeText(getApplicationContext(), getString(R.string.internal_error), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}