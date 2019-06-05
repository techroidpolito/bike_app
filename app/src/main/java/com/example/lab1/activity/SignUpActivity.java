package com.example.lab1.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.lab1.R;
import com.example.lab1.model.ProfileInfo;

import java.util.HashMap;
import java.util.Map;

import static com.example.lab1.activity.SplashScreenActivity.MyPREFERENCES;

public class SignUpActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword,inputConfirmPassword;
    private Button btnSignUp, btnResetPassword;
    private TextView btnSignIn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;
    private FirebaseUser firebaseUser;
    private ProfileInfo bikerModel = new ProfileInfo();
    SharedPreferences sharedpreferences;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
        }
        context = SignUpActivity.this;
        firebaseAuth = FirebaseAuth.getInstance();
        btnSignIn = (TextView) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginScreenActivity.class);
                startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmpassword = inputConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError(getString(R.string.enter_email));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError(getString(R.string.enter_password));
                    return;
                }

                if (password.length() < 8) {
                    inputPassword.setError(getString(R.string.minimum_password));
                    return;
                }
                if(!password.equals(confirmpassword)){
                    Toast.makeText(getApplicationContext(),getString(R.string.password_mismatch) , Toast.LENGTH_SHORT).show();
                    return;
                }
                //create user
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.registration_failed) + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.registered_successfully),
                                            Toast.LENGTH_SHORT).show();

                                    createNewProfile();
                                }
                            }
                        });
            }
        });
    }

    private void createNewProfile(){
        Map<String,Object> biker_entries = new HashMap<>();
        Map<String,Object> biker_dataPlaceholder = new HashMap<>();
        firebaseUser = firebaseAuth.getCurrentUser();
        biker_dataPlaceholder.put(firebaseUser.getUid(), getString(R.string.empty_biker_value));
        biker_entries.put(getString(R.string.bikerEmail),firebaseUser.getEmail());
        biker_entries.put(getString(R.string.biker_completed),false);

        bikerModel.setBikerID(firebaseUser.getUid()+"");
        bikerModel.setEmail_address(firebaseUser.getEmail());
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(getString(R.string.email_id_pref), firebaseUser.getEmail());
        editor.putString(getString(R.string.password_id_pref), inputPassword.getText().toString().trim());
        editor.putString(getString(R.string.bid_id_pref),  firebaseUser.getUid());
        editor.commit();
        dbReference = FirebaseDatabase.getInstance().getReference(getString(R.string.biker_details));
        dbReference.updateChildren(biker_dataPlaceholder, (databaseError, databaseReference) -> {

            if(databaseError == null) {
                dbReference.child(firebaseUser.getUid()).updateChildren(biker_entries);
                Intent i = new Intent(getApplicationContext(), ProfileEditingActivity.class);
                i.putExtra(getString(R.string.biker_profile_data),bikerModel);
                startActivity(i);
                ((AppCompatActivity)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }else {
                Toast.makeText(getApplicationContext(), getString(R.string.internal_error), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
