package com.example.taskflow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private Button btnSignup;
    private TextView tvLogin;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Connect XML fields
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        // Shared preferences
        preferences = getSharedPreferences(
                "TaskFlowPrefs",
                MODE_PRIVATE
        );

        // Register button
        btnSignup.setOnClickListener(v -> registerUser());

        // Return to Login screen
        tvLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword =
                etConfirmPassword.getText().toString();

        // Check empty fields
        if (name.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Validate email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        // Validate password length
        if (password.length() < 6) {
            etPassword.setError(
                    "Password must be at least 6 characters"
            );
            etPassword.requestFocus();
            return;
        }

        // Confirm passwords match
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(
                    "Passwords do not match"
            );
            etConfirmPassword.requestFocus();
            return;
        }

        // Check if an account already exists
        String existingEmail =
                preferences.getString("email", "");

        if (!existingEmail.isEmpty()) {

            if (email.equalsIgnoreCase(existingEmail)) {
                Toast.makeText(
                        this,
                        "Account already exists. Please log in.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }
        }

        // Save account details
        preferences.edit()
                .putString("name", name)
                .putString("email", email)
                .putString("password", password)
                .putBoolean("isLoggedIn", false)
                .apply();

        Toast.makeText(
                this,
                "Account created successfully!",
                Toast.LENGTH_SHORT
        ).show();

        // Return to Login screen
        finish();
    }
}