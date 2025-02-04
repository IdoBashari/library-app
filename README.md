# Rating SDK & Demo App

A comprehensive Android SDK for managing ratings and comments, along with a demo application showcasing its implementation.

## 🌟 Overview

This project consists of two main components:
- A Rating SDK (`rating-sdk`) that provides a simple interface for managing user ratings and comments
- A Demo Application (`app`) that demonstrates the implementation of the SDK in a movie rating scenario

## 🏗 Architecture

### Rating SDK
The SDK is built with a clean architecture approach and includes:

- **API Layer**: Handles communication with the backend server
    - `ApiClient`: REST API client using Retrofit
    - `ApiService`: Interface defining all API endpoints
    - `ErrorResponse`: Standardized error handling

- **Models**: Core data structures
    - `Rating`: Represents a user rating with validation
    - `Comment`: Represents a user comment with validation
    - `User`: User authentication and management

- **Core SDK**: Main interface for developers
    - `RatingSDK`: Singleton class providing all SDK functionality
    - Handles authentication, ratings, and comments management

### Demo Application
The demo app demonstrates SDK usage in a movie rating context:

- **Activities**:
    - `LoginActivity`: User authentication
    - `MainActivity`: Movie list display
    - `MovieDetailsActivity`: Movie details with ratings and comments

- **Adapters**:
    - `MoviesAdapter`: Displays movie list
    - `RatingsAdapter`: Manages ratings display
    - `CommentsAdapter`: Manages comments display

- **Utils**:
    - `PreferencesManager`: Handles user session management

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- Android SDK level 24 or higher
- JDK 8

### Installation

1. Add the JitPack repository to your project's `settings.gradle.kts`:
```groovy
dependencyResolutionManagement {
    repositories {
        // ... other repositories
        maven { url = uri("https://jitpack.io") }
    }
}
```

2. Add the dependency to your app's `build.gradle.kts`:
```groovy
dependencies {
    implementation("com.github.idobashari:rating-sdk:1.0.0")
}
```

## 💡 Usage

### Initialize the SDK
```java
// Initialize the SDK
RatingSDK ratingSDK = RatingSDK.getInstance();

// Set authentication token (after user login)
ratingSDK.setAuthToken("your-auth-token");
```

### User Authentication
```java
// Login
ratingSDK.login(email, password, new RatingCallback<Map<String, Object>>() {
    @Override
    public void onSuccess(Map<String, Object> result) {
        // Handle successful login
    }

    @Override
    public void onError(ErrorResponse error) {
        // Handle login error
    }
});
```

### Managing Ratings
```java
// Create a rating
Rating rating = new Rating(userId, itemId, 4.5f, "Great movie!");
ratingSDK.createRating(rating, new RatingCallback<Map<String, Object>>() {
    @Override
    public void onSuccess(Map<String, Object> result) {
        // Handle successful rating creation
    }

    @Override
    public void onError(ErrorResponse error) {
        // Handle rating error
    }
});
```

### Managing Comments
```java
// Create a comment
Comment comment = new Comment(userId, itemId, "Amazing performance!");
ratingSDK.createComment(comment, new RatingCallback<Map<String, Object>>() {
    @Override
    public void onSuccess(Map<String, Object> result) {
        // Handle successful comment creation
    }

    @Override
    public void onError(ErrorResponse error) {
        // Handle comment error
    }
});
```

## 📱 Demo App Features

The demo application showcases the following features:
- User authentication (login/register)
- Movie list display
- Detailed movie view
- Rating submission and management
- Comment submission and management
- Real-time updates of ratings and comments

## 📄 Documentation

For detailed documentation, please visit our [Documentation Page](docs/index.md).

## 🔒 Security

- All network communications are done over HTTPS
- User authentication is required for all rating and comment operations
- Input validation is performed both client-side and server-side

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.