package com.example.lab1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.lab1.R;
import com.example.lab1.model.ProfileInfo;


public class EditingActivity extends AppCompatActivity {
    ImageButton edit_picture;
    EditText username_et;
    EditText email_address_et;
    EditText phone_nb_et;
    EditText description_et;
    EditText address_et;
    EditText id_et;
    CircleImageView profileImage;

    ProfileInfo profileInfo;
    private Uri imageUri;
    static int picture_request_code = 2;
    static int background_request_code = 3;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent i = getIntent();
        profileInfo = new ProfileInfo(i.getStringArrayListExtra("profile_info"));

        if (savedInstanceState != null) {
            profileInfo = new ProfileInfo( savedInstanceState.getStringArrayList("profile_info" ));
        }

        edit_picture = findViewById(R.id.edit_profile_picture_button);
        registerForContextMenu(edit_picture);
        username_et = findViewById(R.id.edit_username);
        email_address_et = findViewById(R.id.edit_email_address);
        phone_nb_et = findViewById(R.id.edit_phone_number);
        description_et = findViewById(R.id.edit_short_description);
        address_et = findViewById(R.id.edit_address);
        id_et = findViewById(R.id.edit_identity_document);
        profileImage = findViewById(R.id.edit_profile_picture);

        if (profileInfo.isAlready_filled()){
            String username = profileInfo.getUsername();
            if (!username.equals("")) {
                username_et.setText(username);
            }
            String phone_nb = profileInfo.getPhone_nb();
            if (!phone_nb.equals("")) {
                phone_nb_et.setText(phone_nb);
            }
            String email_address = profileInfo.getEmail_address();
            if (!email_address.equals("")) {
                email_address_et.setText(email_address);
            }
            String address = profileInfo.getAddress();
            if (!address.equals("")) {
                address_et.setText(address);
            }
            String description = profileInfo.getDescription();
            if (!description.equals("")) {
                description_et.setText(description);
            }
            String identity_document = profileInfo.getIdentity_document();
            if (!identity_document.equals("")) {
                id_et.setText(identity_document);
            }
            Uri pp_uri = Uri.parse(profileInfo.getProfile_picture_uri());
            setProfilePicture(pp_uri);
        }

        /*
        edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, picture_request_code);
            }
        });
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == picture_request_code) {
                Uri targetUri = data.getData();
                setProfilePicture(targetUri);
                profileInfo.setProfile_picture_uri(targetUri.toString());
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
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        Log.i("onCreateOptionsMenu","ok in EditingActivity");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.cancelButton:
                Intent cancelIntent = new Intent();
                cancelIntent.putExtra("button", "cancel");
                setResult(RESULT_OK, cancelIntent);
                finish();

            case R.id.saveButton:
                String username = username_et.getText().toString();
                profileInfo.setUsername(username);
                String phone_nb = phone_nb_et.getText().toString();
                profileInfo.setPhone_nb(phone_nb);
                String email_address = email_address_et.getText().toString();
                profileInfo.setEmail_address(email_address);
                String address = address_et.getText().toString();
                profileInfo.setAddress(address);
                String description = description_et.getText().toString();
                profileInfo.setDescription(description);
                String identity_document = id_et.getText().toString();
                profileInfo.setIdentity_document(identity_document);

                Intent saveIntent = new Intent();
                saveIntent.putExtra("button", "save");
                saveIntent.putExtra("profile_info", profileInfo.toArrayList());

                setResult(RESULT_OK, saveIntent);
                finish();

            default:
                return false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_picture_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_camera:
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                startActivityForResult(camera_intent, picture_request_code);
            case R.id.context_gallery:
                Intent gallery_intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, picture_request_code);
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void setProfilePicture(Uri picture_uri){
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picture_uri));
            profileImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}