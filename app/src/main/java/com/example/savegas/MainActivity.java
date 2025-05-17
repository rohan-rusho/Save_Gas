package com.example.savegas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final long SEVEN_DAYS_MILLIS = 7 * 24 * 60 * 60 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check login status when the app starts
        checkLoginStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-check session when the app comes to the foreground
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        long loginTime = sharedPreferences.getLong("loginTime", 0);
        long currentTime = System.currentTimeMillis();

        Intent intent;
        if (isLoggedIn && (currentTime - loginTime < SEVEN_DAYS_MILLIS)) {
            // Still logged in, session valid
            intent = new Intent(MainActivity.this, DashboardActivity.class);
        } else {
            // Not logged in or session expired
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }

        startActivity(intent);
        finish(); // Close MainActivity so user can't return here with back button
    }
}
