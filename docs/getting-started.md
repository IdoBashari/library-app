Rating SDK Documentation
Table of Contents

Getting Started

Installation
Basic Setup
Authentication


Core Concepts

SDK Architecture
Data Models
Error Handling
Callbacks


API Reference

RatingSDK Class
Authentication Methods
Rating Methods
Comment Methods
Error Responses


Models

User Model
Rating Model
Comment Model
Validation Rules


Integration Guide

Step-by-Step Integration
Best Practices
Common Use Cases
Error Handling


Demo App Guide

App Architecture
Features Overview
Implementation Examples
UI Components



Getting Started
Installation
Add the JitPack repository to your project's settings.gradle.kts:
groovyCopydependencyResolutionManagement {
repositories {
maven { url = uri("https://jitpack.io") }
}
}
Add the dependency to your app's build.gradle.kts:
groovyCopydependencies {
implementation("com.github.idobashari:rating-sdk:1.0.0")
}
Basic Setup

Initialize the SDK in your Application class or main activity:

javaCopyRatingSDK sdk = RatingSDK.getInstance();

Configure the SDK with your authentication token after user login:

javaCopysdk.setAuthToken("your-auth-token");
Authentication
The SDK provides two authentication methods:
javaCopy// Register new user
sdk.register(email, password, name, new RatingCallback<Map<String, Object>>() {
@Override
public void onSuccess(Map<String, Object> result) {
String token = (String) result.get("token");
// Store token and configure SDK
}

    @Override
    public void onError(ErrorResponse error) {
        // Handle registration error
    }
});

// Login existing user
sdk.login(email, password, new RatingCallback<Map<String, Object>>() {
@Override
public void onSuccess(Map<String, Object> result) {
String token = (String) result.get("token");
// Store token and configure SDK
}

    @Override
    public void onError(ErrorResponse error) {
        // Handle login error
    }
});
