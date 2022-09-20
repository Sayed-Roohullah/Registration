package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterAct extends AppCompatActivity {

    EditText mfullname,mEmail,mPassword,mPhone;
    Button mRegister;
    TextView mLoginbtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_register);

         getSupportActionBar().hide();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mfullname = findViewById(R.id.fullname);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegister = findViewById(R.id.createbtn);
        progressBar = findViewById(R.id.progressbar);
        mLoginbtn = findViewById(R.id.loginbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        mRegister.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is Required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is Required");
                return;
            }
            if (password.length() < 6) {
                mPassword.setError("Password must greater than 6 char");
                return;
            }

        progressBar.setVisibility(View.VISIBLE);

            Bundle bundle = new Bundle();
             bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(mfullname));
            // bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            //bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterAct.this, "User Created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }else {
                        Toast.makeText(RegisterAct.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }
            });

        });

        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterAct.this,loginAct.class);
                startActivity(intent);
            }
        });
    }
}