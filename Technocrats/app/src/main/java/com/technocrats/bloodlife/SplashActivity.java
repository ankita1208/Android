package com.technocrats.bloodlife;

import android.os.Bundle;
import android.os.Handler;

import com.technocrats.bloodlife.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       Handler handler = new Handler();
        Runnable runnable = () -> {
            navigateToLoginActivity(this);
        };
        handler.postDelayed(runnable, 1000);
    }
}