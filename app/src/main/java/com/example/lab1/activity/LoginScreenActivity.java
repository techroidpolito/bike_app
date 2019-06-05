package com.example.lab1.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.example.lab1.R;
import com.example.lab1.model.ProfileInfo;

import static com.example.lab1.activity.SplashScreenActivity.MyPREFERENCES;

public class LoginScreenActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private TextView  btnSignup;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbReference;
    SharedPreferences sharedpreferences;
    private Context context;
    private ProfileInfo bikerModel = new ProfileInfo();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
        }
        Log.d("LoginScreen","reached");
        context = LoginScreenActivity.this;
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignup = (TextView) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginScreenActivity.this, SignUpActivity.class);
                startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError(getString(R.string.enter_email));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError(getString(R.string.enter_password));
                    return;
                }

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginScreenActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 8) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginScreenActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    checkProfileCompletion();
                                }
                            }
                        });
            }
        });
    }

    private void checkProfileCompletion() {
        firebaseUser = auth.getCurrentUser();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(getString(R.string.email_id_pref), firebaseUser.getEmail());
        editor.putString(getString(R.string.password_id_pref), inputPassword.getText().toString().trim());
        editor.putString(getString(R.string.bid_id_pref),  firebaseUser.getUid());
        editor.commit();
        bikerModel.setBikerID(firebaseUser.getUid()+"");
        bikerModel.setEmail_address(firebaseUser.getEmail());
        dbReference = FirebaseDatabase.getInstance().getReference(getString(R.string.biker_details)).child(firebaseUser.getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean biker_completed = (Boolean) dataSnapshot.child(getString(R.string.biker_completed)).getValue();

                if(biker_completed!= null) {
                    if (!biker_completed) {
                        Intent i = new Intent(LoginScreenActivity.this, ProfileNoEditingActivity.class);
                        i.putExtra(getString(R.string.biker_profile_data), bikerModel);
                        startActivity(i);
                        ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        Intent i = new Intent(LoginScreenActivity.this, PendingRequestActivity.class);
                        i.putExtra(getString(R.string.bikerID),firebaseUser.getUid());
                        Log.d("bid",firebaseUser.getUid());
                        startActivity(i);
                        ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        //Todo: if profile complete Main PAge
                    }
                }
                else{
                    Intent i = new Intent(LoginScreenActivity.this, ProfileNoEditingActivity.class);
                    i.putExtra(getString(R.string.biker_profile_data), bikerModel);
                    startActivity(i);
                    ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginScreenActivity.this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
            }
        });
    }

}
