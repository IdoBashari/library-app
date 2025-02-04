package com.github.idobashari.sdk_app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.github.idobashari.rating_sdk.RatingSDK;
import java.util.Map;

public class PreferencesManager {
    private static final String TAG = "PreferencesManager";
    private static final String PREF_NAME = "RatingAppPrefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER = "user_data";
    private static final String KEY_USER_ID = "user_id";

    private final SharedPreferences preferences;
    private final Gson gson;
    private static PreferencesManager instance;

    private PreferencesManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        Log.d(TAG, "PreferencesManager initialized");
    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    public void saveUserDataFromLogin(Map<String, Object> loginResponse) {
        try {
            String token = (String) loginResponse.get("token");
            @SuppressWarnings("unchecked")
            Map<String, Object> userData = (Map<String, Object>) loginResponse.get("user");

            if (token != null && userData != null) {
                // שמירת הטוקן בספרייה
                RatingSDK.getInstance().setAuthToken(token);

                // שמירה מקומית
                preferences.edit()
                        .putString(KEY_AUTH_TOKEN, token)
                        .putString(KEY_USER_ID, (String) userData.get("id"))
                        .putString(KEY_USER, gson.toJson(userData))
                        .apply();

                Log.d(TAG, "User data saved successfully. UserID: " + userData.get("id"));
            } else {
                Log.e(TAG, "Failed to save user data - missing token or user data");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error saving user data: " + e.getMessage());
        }
    }

    public String getAuthToken() {
        String token = preferences.getString(KEY_AUTH_TOKEN, null);
        Log.d(TAG, "Retrieved auth token: " + (token != null ? "exists" : "null"));
        return token;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserData() {
        String userJson = preferences.getString(KEY_USER, null);
        if (userJson != null) {
            try {
                Map<String, Object> userData = gson.fromJson(userJson, Map.class);
                Log.d(TAG, "Retrieved user data successfully");
                return userData;
            } catch (Exception e) {
                Log.e(TAG, "Error parsing user data: " + e.getMessage());
                return null;
            }
        }
        Log.d(TAG, "No user data found");
        return null;
    }

    public String getUserId() {
        String userId = preferences.getString(KEY_USER_ID, null);
        Log.d(TAG, "Retrieved user ID: " + (userId != null ? userId : "null"));
        return userId;
    }

    public boolean isUserLoggedIn() {
        boolean isLoggedIn = getAuthToken() != null && getUserId() != null;
        Log.d(TAG, "User login status: " + isLoggedIn);
        return isLoggedIn;
    }

    public void clearUserData() {
        try {
            preferences.edit()
                    .remove(KEY_AUTH_TOKEN)
                    .remove(KEY_USER)
                    .remove(KEY_USER_ID)
                    .apply();

            // ניקוי הטוקן גם בספרייה
            RatingSDK.getInstance().setAuthToken(null);

            Log.d(TAG, "User data cleared successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing user data: " + e.getMessage());
        }
    }
}