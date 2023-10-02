package com.ram.instagramcloneapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ram.instagramcloneapp.Fragments.HomeFragment;
import com.ram.instagramcloneapp.Fragments.NotificationFragment;
import com.ram.instagramcloneapp.Fragments.ProfileFragment;
import com.ram.instagramcloneapp.Fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setBottomNavigationView();
        setHomeAfterLogin();
    }

    private void setBottomNavigationView()
    {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectorFragment = new HomeFragment();
            } else if (itemId == R.id.nav_search) {
                selectorFragment = new SearchFragment();
            } else if (itemId == R.id.nav_add) {
                selectorFragment = null;
                startActivity(new Intent(MainActivity.this, PostActivity.class));
            } else if (itemId == R.id.nav_heart) {
                selectorFragment = new NotificationFragment();
            } else if (itemId == R.id.nav_profile) {
                selectorFragment = new ProfileFragment();
            }

            if(selectorFragment != null)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectorFragment).commit();
            }
            return true;
        });
    }

    private void setHomeAfterLogin()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }
}