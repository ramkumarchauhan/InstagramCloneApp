package com.ram.instagramcloneapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText email;
    private EditText password;
    private Button login;
    private TextView registerNow;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: Started");
        init();
        setLogin();
        goRegisterYourself();
    }
    private void init() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registerNow = findViewById(R.id.register_now);
        mAuth = FirebaseAuth.getInstance();
    }

    private void goRegisterYourself() {
        registerNow.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });

    }

    //set Login Button
    private void setLogin() {
        login.setOnClickListener(v -> {
            if (validate()) {
                String txtEmail = email.getText().toString().trim();
                String txtPassword = password.getText().toString().trim();
                loginUser(txtEmail, txtPassword);
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }

        }).addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: Failed to login" + e.getMessage());
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    private boolean validate() {
        String txtEmail = email.getText().toString().trim();
        String txtPassword = password.getText().toString().trim();
        boolean flag = true;
        if (TextUtils.isEmpty(txtEmail)) {
            email.setError("Enter Email");
            flag = false;
        }
        if (TextUtils.isEmpty(txtPassword)) {
            password.setError("Enter Password");
            flag = false;
        }

        return flag;
    }

}