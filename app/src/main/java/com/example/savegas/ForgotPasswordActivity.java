package com.example.savegas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText resetEmailInput;
    private Button resetPasswordBtn;
    private TextView backToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI components
        resetEmailInput = findViewById(R.id.resetEmailInput);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        backToLogin = findViewById(R.id.backToLogin);
        mAuth = FirebaseAuth.getInstance();

        // Reset password button click listener
        resetPasswordBtn.setOnClickListener(v -> {
            String email = resetEmailInput.getText().toString().trim();

            // Validate email input
            if (email.isEmpty()) {
                resetEmailInput.setError("Email is required");
                resetEmailInput.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                resetEmailInput.setError("Please enter a valid email");
                resetEmailInput.requestFocus();
                return;
            }

            // Send password reset email
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ForgotPassword", "Reset email sent to: " + email);
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Reset link sent to " + email,
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            String errorMessage = "Error sending reset link";
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidUserException) {
                                errorMessage = "No account found with this email";
                            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMessage = "Invalid email format";
                            } else if (exception != null) {
                                errorMessage = exception.getMessage();
                                Log.e("ForgotPassword", "Reset failed: " + errorMessage);
                            }
                            Toast.makeText(ForgotPasswordActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Back to login click listener
        backToLogin.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });
    }
}