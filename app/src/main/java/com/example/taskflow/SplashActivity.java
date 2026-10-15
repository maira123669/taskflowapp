package com.example.taskflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable navigationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler(Looper.getMainLooper());

        navigationRunnable = () -> {

            SharedPreferences preferences =
                    getSharedPreferences(
                            "TaskFlowPrefs",
                            MODE_PRIVATE
                    );

            boolean isLoggedIn = preferences.getBoolean(
                    "isLoggedIn",
                    false
            );

            Intent intent;

            if (isLoggedIn) {
                intent = new Intent(
                        SplashActivity.this,
                        HomeActivity.class
                );
            } else {
                intent = new Intent(
                        SplashActivity.this,
                        LoginActivity.class
                );
            }

            startActivity(intent);
            finish();
        };

        handler.postDelayed(navigationRunnable, 2000);
    }

    @Override
    protected void onDestroy() {
        if (handler != null && navigationRunnable != null) {
            handler.removeCallbacks(navigationRunnable);
        }

        super.onDestroy();
    }
}