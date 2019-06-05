package com.example.lab1.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.example.lab1.fragment.BottomSheetFragment;
import com.example.lab1.model.ProfileInfo;
import com.example.lab1.util.CameraInterface;
import com.example.lab1.util.PhotoInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ProfileEditingActivity extends AppCompatActivity implements CameraInterface, PhotoInterface {
    private ImageButton edit_picture;
    private View contextView;
    private EditText first_name_et, last_name_et, email_address_et, phone_nb_et, description_et, codice_fiscale_et;
    private CircleImageView profileImage;
    private String profileImg,profileLocation = null;
    private Toolbar toolbar;
    Boolean pressed = false;
    BottomSheetFragment bottomSheetFragment;

    private ProfileInfo profileInfo;
    private String bikerId;
    private StorageReference storageReference;
    private static final int REQUEST_PROFILE_IMAGE = 2;

    private static final int ON_SUCCESS = 3;
    private static final int ON_PROGRESS = 4;
    private static final int ON_FAILURE = 5;
    private int upload_state = ON_SUCCESS; //by default


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent i = getIntent();
        profileInfo = (ProfileInfo) i.getSerializableExtra(getString(R.string.biker_profile_data));
        bikerId = i.getStringExtra(getString(R.string.bikerID));

        if (profileInfo == null && savedInstanceState != null) {
            profileInfo = new ProfileInfo( savedInstanceState.getStringArrayList("profile_info" ));
        }

        edit_picture = findViewById(R.id.edit_profile_picture_button);
        registerForContextMenu(edit_picture);
        first_name_et = findViewById(R.id.edit_firstName);
        last_name_et = findViewById(R.id.edit_lastName);
        email_address_et = findViewById(R.id.edit_email_address);
        phone_nb_et = findViewById(R.id.edit_phone_number);
        description_et = findViewById(R.id.edit_short_description);
        codice_fiscale_et = findViewById(R.id.edit_codice_fiscale);
        profileImage = findViewById(R.id.edit_profile_picture);
        toolbar = findViewById(R.id.editToolbar);
        setSupportActionBar(toolbar);

        storageReference = FirebaseStorage.getInstance().getReference();

        Log.d("test_null_profile",String.valueOf(profileInfo==null));
        Log.d("test_complete", String.valueOf((profileInfo.isAlready_filled() || profileInfo.getBiker_completed())));

        String firstname = profileInfo.getFirstName();
        if (firstname != null) {
            first_name_et.setText(firstname);
        }
        Log.d("firstname",firstname);
        String lastname = profileInfo.getLastName();
        if (lastname != null) {
            last_name_et.setText(lastname);
        }
        String phone_nb = profileInfo.getPhone_nb();
        if (phone_nb != null) {
            phone_nb_et.setText(phone_nb);
        }
        String email_address = profileInfo.getEmail_address();
        if (email_address != null) {
            email_address_et.setText(email_address);
        }
        String description = profileInfo.getDescription();
        if (description != null) {
            description_et.setText(description);
        }
        String codice_fiscale = profileInfo.getCodiceFiscale();
        if (codice_fiscale != null){
            codice_fiscale_et.setText(codice_fiscale);
        }
        String pp_uri = profileInfo.getProfile_picture_uri();
        if (pp_uri != null) {
            Log.d("saved path",getString(R.string.biker_profile_image_folder)+pp_uri);
            Log.d("pp_uri",pp_uri);
            profileImg = getString(R.string.biker_profile_image_folder)+pp_uri;
            setProfilePicture(profileImg);
        }

        edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contextView = v;
                pressed = true;
                showProfileBottomSheetDialogFragment();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PROFILE_IMAGE) {

                Uri uri = data.getParcelableExtra(getString(R.string.path_image_uri));
                profileImg = uri.toString();
                profileLocation = uri.getLastPathSegment();
                StorageReference storageReference2 = storageReference.child(getString(R.string.biker_profile_image_folder)+profileLocation);
                storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar snackbar = Snackbar
                                .make(contextView, getString(R.string.upload_success), Snackbar.LENGTH_LONG);

                        snackbar.show();
                        upload_state = ON_SUCCESS;
                        invalidateOptionsMenu();
                        setProfilePicture(profileImg);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar snackbar = Snackbar
                                        .make(contextView, getString(R.string.upload_failed), Snackbar.LENGTH_LONG);

                                snackbar.show();
                                upload_state = ON_FAILURE;
                                invalidateOptionsMenu();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Snackbar snackbar = Snackbar
                                        .make(contextView, getString(R.string.Uploading), Snackbar.LENGTH_LONG);

                                snackbar.show();
                                upload_state = ON_PROGRESS;
                                invalidateOptionsMenu();
                            }
                        });
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
        Log.i("onCreateOptionsMenu","ok in ProfileEditingActivity");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (upload_state){
            case ON_SUCCESS:
                menu.findItem(R.id.saveButton).setVisible(true);
                menu.findItem(R.id.saveButton).setEnabled(true);
                return true;

            case ON_PROGRESS:
                menu.findItem(R.id.saveButton).setVisible(true);
                menu.findItem(R.id.saveButton).setEnabled(false);
                return true;

            case ON_FAILURE:
                menu.findItem(R.id.saveButton).setVisible(false);
                menu.findItem(R.id.saveButton).setEnabled(false);
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
              case R.id.saveButton:
                //setSave();
                if(setValidation()) {
                    String firstname = first_name_et.getText().toString();
                    profileInfo.setFirstName(firstname);
                    String lastname = last_name_et.getText().toString();
                    profileInfo.setLastName(lastname);
                    String phone_nb = phone_nb_et.getText().toString();
                    profileInfo.setPhone_nb(phone_nb);
                    String email_address = email_address_et.getText().toString();
                    profileInfo.setEmail_address(email_address);
                    String description = description_et.getText().toString();
                    if(description != null){
                        profileInfo.setDescription(description);
                    }
                    String codice_fiscale = codice_fiscale_et.getText().toString();
                    profileInfo.setCodiceFiscale(codice_fiscale);
                    if(profileLocation != null) {
                        profileInfo.setProfile_picture_uri(profileLocation);
                    }

                    Intent saveIntent = new Intent();
                    saveIntent.putExtra("button", "save");
                    saveIntent.putExtra(getString(R.string.biker_profile_data), profileInfo);
                    saveIntent.putExtra(getString(R.string.bikerID), bikerId);

                    setResult(RESULT_OK, saveIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Missing information", Toast.LENGTH_SHORT).show();
                }

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
                profileImg = String.valueOf(Uri.fromFile(photo));
                profileLocation = (Uri.fromFile(photo)).getLastPathSegment();
                startActivityForResult(camera_intent, REQUEST_PROFILE_IMAGE);
            case R.id.context_gallery:
                Intent gallery_intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, REQUEST_PROFILE_IMAGE);
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void setProfilePicture(String picture_path){
        Glide.with(this).load(picture_path).into(profileImage);
        profileImage.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
        Log.d("path",picture_path);
    }

    private boolean setValidation(){
        String emailPattern = getString(R.string.email_validation);
        String codiceFiscalePattern = getString(R.string.codice_fiscale_validation);
        if((first_name_et.getText().toString().isEmpty() || first_name_et.getText().length()<=2)){
            first_name_et.setError(getString(R.string.enter_first_name));
            return false;
        }
        if((last_name_et.getText().toString().isEmpty() || last_name_et.getText().length()<=2)){
            last_name_et.setError(getString(R.string.enter_last_name));
            return false;
        }
        if((phone_nb_et.getText().toString().isEmpty()|| phone_nb_et.getText().length()<10)){
            phone_nb_et.setError(getString(R.string.enter_phone));
            return false;
        }
        if((email_address_et.getText().toString().isEmpty() || ! email_address_et.getText().toString().matches(emailPattern))){
            email_address_et.setError(getString(R.string.enter_email));
            return false;
        }
        if((codice_fiscale_et.getText().toString().isEmpty() || ! codice_fiscale_et.getText().toString().matches(codiceFiscalePattern))){
            codice_fiscale_et.setError(getString(R.string.enter_codice_fiscale));
            return false;
        }
        else{
            return true;
        }
    }


    public void showProfileBottomSheetDialogFragment() {
        bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        bottomSheetFragment.setCameraInterfaceC(ProfileEditingActivity.this);
        bottomSheetFragment.setPhotoInterface(ProfileEditingActivity.this);
    }

    @Override
    public void cameraClicked() {
        launchCameraIntent();
    }

    @Override
    public void photoClicked() {
        launchGalleryIntent();
    }

    private void launchCameraIntent() {

        Intent intent = new Intent(ProfileEditingActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        if(pressed==true) {
            startActivityForResult(intent, REQUEST_PROFILE_IMAGE);
        }
        bottomSheetFragment.dismiss();
    }


    private void launchGalleryIntent() {

        Intent intent = new Intent(ProfileEditingActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        if(pressed==true){
            startActivityForResult(intent, REQUEST_PROFILE_IMAGE);
        }
        bottomSheetFragment.dismiss();
    }
}