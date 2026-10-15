package com.example.taskflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignup;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvGoToSignup);

        preferences = getSharedPreferences(
                "TaskFlowPrefs",
                MODE_PRIVATE
        );

        btnLogin.setOnClickListener(v -> loginUser());

        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    SignupActivity.class
            );

            startActivity(intent);
        });
    }

    private void loginUser() {

        String email = etEmail.getText()
                .toString()
                .trim();

        String password = etPassword.getText()
                .toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                    this,
                    "Enter email and password",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }

        String savedEmail = preferences.getString("email", "");
        String savedPassword = preferences.getString("password", "");

        if (!savedEmail.isEmpty()
                && email.equalsIgnoreCase(savedEmail)
                && password.equals(savedPassword)) {

            preferences.edit()
                    .putBoolean("isLoggedIn", true)
                    .apply();

            Intent intent = new Intent(
                    LoginActivity.this,
                    HomeActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Invalid credentials. Please sign up first.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}