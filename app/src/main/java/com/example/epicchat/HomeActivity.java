package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.epicchat.Fragments.DiscoverFragment;
import com.example.epicchat.Fragments.ChatFragment;
import com.example.epicchat.Fragments.FeedFragment;
import com.example.epicchat.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UserInformation userInformationListener = new UserInformation();
        userInformationListener.startFetching();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.feed:
                            selectedFragment = new FeedFragment();
                            break;
                        case R.id.discover:
                            selectedFragment = new DiscoverFragment();
                            break;
                        case R.id.chat:
                            selectedFragment = new ChatFragment();
                            break;
                        case R.id.profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();

                    return true;
                }
            };
}

// Source of code: https://www.youtube.com/watch?v=tPV8xA7m-iw - BottomNavigationView with Fragments - Android Studio Tutorial
// The video above helped with creating the options for the bottom navigation view.