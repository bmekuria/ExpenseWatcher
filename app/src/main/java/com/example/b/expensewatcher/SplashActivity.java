package com.example.b.expensewatcher;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        //Initialize Mobile Ads SDK
        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111");

        //Keep the splash screen visible
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },SPLASH_DURATION);

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
