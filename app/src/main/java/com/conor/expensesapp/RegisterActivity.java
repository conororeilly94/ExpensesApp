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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button buttonReg;
    private TextView mSignin;

    private ProgressDialog mDialog;

    // Firebase

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);

        // Calling registration within onCreate method
        registration();
    }

    private void registration() {

        mEmail = findViewById(R.id.email_reg);
        mPassword = findViewById(R.id.password_reg);
        buttonReg = findViewById(R.id.button_reg);
        mSignin = findViewById(R.id.signin);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create variables to get user input
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
                }

                mDialog.setMessage("Processing");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            mDialog.dismiss(); // Removed Dialog progress
                            Toast.makeText(getApplicationContext(), "You have successfully registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class)); // Redirects to HomeActivity
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        // When already have account text view is selected
        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If already have an account, redirect them to MainActivity.class
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
}