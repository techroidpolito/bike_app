package com.example.lab1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.lab1.R;
import com.example.lab1.model.ProfileInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.lab1.util.NetworkUtilities.checkNetworkConnection;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    public static final String MyPREFERENCES = "tokenpref" ;
    private SharedPreferences tokenpref;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;
    private FirebaseUser firebaseUser;
    private ProfileInfo bikerModel = new ProfileInfo();
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
        }

        context = SplashScreenActivity.this;
        tokenpref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String email = tokenpref.getString(getString(R.string.email_id_pref), null);
        String password = tokenpref.getString(getString(R.string.password_id_pref), null);
        String bid = tokenpref.getString(getString(R.string.bid_id_pref), null);
        Log.d("splashscreen",email+password+bid);

        initFirebase();
        buildInternetRequestDialog();
        checkNetworkConnection();

        if(email!=null&&password!=null&&bid!=null){
            checkAuth(email,password);
            Log.d("check","auth");
        }
        else {
            Log.d("start","LoginScreen");
            Intent i = new Intent(SplashScreenActivity.this, LoginScreenActivity.class);
            startActivity(i);
            ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

    private void checkAuth(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SplashScreenActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(SplashScreenActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SplashScreenActivity.this, LoginScreenActivity.class);
                            startActivity(i);
                            ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            checkProfileCompletion();
                        }
                    }
                });
    }

    private void initFirebase(){
        /*Authentication*/
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void buildInternetRequestDialog(){
        AlertDialog.Builder internetRequest = new AlertDialog.Builder(SplashScreenActivity.this);
        internetRequest.setTitle(R.string.no_internet_connection);
        internetRequest.setMessage(R.string.network_alert);
        internetRequest.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                    startActivityForResult(settingsIntent, 9003);
                }
        ).setNegativeButton(android.R.string.cancel,
                (dialog, which) -> dialog.dismiss()
        );
    }

    private void checkProfileCompletion() {
        firebaseUser = firebaseAuth.getCurrentUser();
        bikerModel.setBikerID(firebaseUser.getUid() + "");
        Log.d("bikerId",firebaseUser.getUid() + "");
        bikerModel.setEmail_address(firebaseUser.getEmail());
        Log.d("email",firebaseUser.getEmail());

        dbReference = FirebaseDatabase.getInstance().getReference(getString(R.string.biker_details)).child(firebaseUser.getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean biker_completed = (Boolean) dataSnapshot.child(getString(R.string.biker_completed)).getValue();
                if(biker_completed!= null) {
                    if (!biker_completed) {
                        Intent i = new Intent(SplashScreenActivity.this, ProfileNoEditingActivity.class);
                        i.putExtra(getString(R.string.biker_profile_data), bikerModel);
                        startActivity(i);
                        ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        Intent i = new Intent(SplashScreenActivity.this, PendingRequestActivity.class);
                        i.putExtra(getString(R.string.bikerID),firebaseUser.getUid());
                        Log.d("transmitted bid",firebaseUser.getUid());
                        startActivity(i);
                        ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        //Todo: if profile complete Main PAge
                    }
                }
                else{
                    Intent i = new Intent(SplashScreenActivity.this, ProfileNoEditingActivity.class);
                    i.putExtra(getString(R.string.biker_profile_data), bikerModel);
                    startActivity(i);
                    ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SplashScreenActivity.this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
