package com.conor.expensesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button buttonLogin;
    private TextView mForgetPassword;
    private TextView mSignUp;

    private ProgressDialog mDialog;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        } // Keeps user logged in

        mDialog = new ProgressDialog(this);

        // Calling loginDetails within onCreate method
        loginDetails();
    }


    private void loginDetails() {

        mEmail = findViewById(R.id.email_login);
        mPassword = findViewById(R.id.password_login);
        buttonLogin = findViewById(R.id.button_login);
        mForgetPassword = findViewById(R.id.forget_password);
        mSignUp = findViewById(R.id.signup_reg);

        // When Login Button is pressed
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    // Ensures email field is inserted
                    mEmail.setError("Email Required");
                    return; // Returns to login page
                }

                if(TextUtils.isEmpty(password)) {
                    // Ensures email field is inserted
                    mPassword.setError("Password Required");
                    return;
                }

                mDialog.setMessage("Processing");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class)); // If successful, go to HomeActivity
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // When sign up text view is selected - Register activity
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirects user to register UI
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        // When password reset text view is selected - Reset activity
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirects user to reset password UI
                startActivity(new Intent(getApplicationContext(), ResetpassActivity.class));
            }
        });

    }
}