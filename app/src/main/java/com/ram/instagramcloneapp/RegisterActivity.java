package com.ram.instagramcloneapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView alreadyHaveAccount;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: Started");

        init();
        alreadyHaveAccount();
        setBtnRegister();
    }
    private void setBtnRegister() {

        register.setOnClickListener(v -> {

                String txtUsername = username.getText().toString().trim();
                String txtName = name.getText().toString().trim();
                String txtEmail = email.getText().toString().trim();
                String txtPassword = password.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"Couldn't LogIn",Toast.LENGTH_LONG).show();
                        }
                    });
//            }

        });
    }

    private void init() {
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        alreadyHaveAccount = findViewById(R.id.already_have_account);
        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
    }
    private void alreadyHaveAccount() {
        alreadyHaveAccount.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

}