package com.github.idobashari.sdk_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.idobashari.rating_sdk.RatingSDK;
import com.github.idobashari.rating_sdk.api.ErrorResponse;
import com.github.idobashari.rating_sdk.callbacks.RatingCallback;
import com.github.idobashari.sdk_app.R;
import com.github.idobashari.sdk_app.utils.PreferencesManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar progressBar;

    private PreferencesManager preferencesManager;
    private RatingSDK ratingSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // אתחול שדות
        initializeViews();

        // אתחול SDK ו-PreferencesManager
        preferencesManager = PreferencesManager.getInstance(this);
        ratingSDK = RatingSDK.getInstance();

        // בדיקה האם המשתמש כבר מחובר
        if (preferencesManager.isUserLoggedIn()) {
            Log.d(TAG, "User already logged in, redirecting to MainActivity");
            startMainActivity();
            return;
        }

        // הגדרת מאזינים
        loginButton.setOnClickListener(v -> handleLogin());
        registerButton.setOnClickListener(v -> handleRegister());
    }

    private void initializeViews() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        setLoading(true);

        ratingSDK.login(email, password, new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.d(TAG, "Login successful");
                runOnUiThread(() -> {
                    setLoading(false);
                    preferencesManager.saveUserDataFromLogin(result);
                    startMainActivity();
                });
            }

            @Override
            public void onError(ErrorResponse error) {
                Log.e(TAG, "Login error: " + error.getMessage());
                runOnUiThread(() -> {
                    setLoading(false);
                    showError(error.getMessage());
                });
            }
        });
    }

    private void handleRegister() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        setLoading(true);

        ratingSDK.register(email, password, null, new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.d(TAG, "Registration successful");
                runOnUiThread(() -> {
                    setLoading(false);
                    preferencesManager.saveUserDataFromLogin(result);
                    startMainActivity();
                });
            }

            @Override
            public void onError(ErrorResponse error) {
                Log.e(TAG, "Registration error: " + error.getMessage());
                runOnUiThread(() -> {
                    setLoading(false);
                    showError(error.getMessage());
                });
            }
        });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields");
            return false;
        }
        if (!email.contains("@")) {
            showError("Please enter a valid email");
            return false;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!isLoading);
        registerButton.setEnabled(!isLoading);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}