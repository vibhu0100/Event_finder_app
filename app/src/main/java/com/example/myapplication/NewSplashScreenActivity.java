package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class NewSplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIMEOUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity
                Intent intent = new Intent(NewSplashScreenActivity.this, MainActivity.class);
                startActivity(intent);

                // Close the splash screen activity
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
    }
