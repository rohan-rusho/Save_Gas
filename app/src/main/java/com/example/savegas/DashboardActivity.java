package com.example.savegas;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private Switch deviceSwitch;
    private TextView leakageStatus, gasPercentage, connectionStatus, alertsText;
    private ProgressBar gasProgress;
    private Button cutGasButton, resetButton, emergencyButton;
    private ImageView logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // findViewById calls
        deviceSwitch = findViewById(R.id.deviceSwitch);
        leakageStatus = findViewById(R.id.leakageStatus);
        gasPercentage = findViewById(R.id.gasPercentage);
        gasProgress = findViewById(R.id.gasProgress);
        connectionStatus = findViewById(R.id.connectionStatus);
        alertsText = findViewById(R.id.alertsText);
        cutGasButton = findViewById(R.id.cutGasButton);
        resetButton = findViewById(R.id.resetButton);
        emergencyButton = findViewById(R.id.emergencyButton);
        logoutButton = findViewById(R.id.logoutButton);

        // initialize UI
        setLeakageStatus(false);
        animateGasLevel(65);
        setConnectionStatus(true);
        setAlerts("No recent alerts");

        // button listeners
        cutGasButton.setOnClickListener(v -> cutGasSupply());
        resetButton.setOnClickListener(v -> resetSystem());
        emergencyButton.setOnClickListener(v -> contactEmergency());

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(DashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void setLeakageStatus(boolean isLeakageDetected) {
        leakageStatus.setText(isLeakageDetected ? "Leakage detected!" : "No leakage detected");
    }

    private void setConnectionStatus(boolean isConnected) {
        connectionStatus.setText(isConnected ? "Connected to device" : "Disconnected");
    }

    private void setAlerts(String alertMessage) {
        alertsText.setText(alertMessage);
    }

    private void cutGasSupply() {
        Toast.makeText(this, "Gas supply cut", Toast.LENGTH_SHORT).show();
    }

    private void resetSystem() {
        Toast.makeText(this, "System reset", Toast.LENGTH_SHORT).show();
    }

    private void contactEmergency() {
        Toast.makeText(this, "Contacting emergency", Toast.LENGTH_SHORT).show();
    }

    // Animate circular gas progress bar from 0 to target level
    private void animateGasLevel(int level) {
        // Animate progress bar smoothly from 0 to level
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(gasProgress, "progress", 0, level);
        progressAnimator.setDuration(1500); // 1.5 seconds duration
        progressAnimator.start();

        // Animate gas percentage text along with progress
        new Thread(() -> {
            for (int i = 0; i <= level; i++) {
                final int progress = i;
                runOnUiThread(() -> gasPercentage.setText(progress + "%"));
                try {
                    Thread.sleep(1500 / level); // adjust sleep to sync with animation
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
