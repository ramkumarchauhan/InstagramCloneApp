package com.ram.instagramcloneapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;


public class OptionActivity extends AppCompatActivity {

    private TextView setting;
    private TextView logout;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        logout = findViewById(R.id.logout);
        setting = findViewById(R.id.settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting title to Options
        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(OptionActivity.this,StartActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });
    }
}