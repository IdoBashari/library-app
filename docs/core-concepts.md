Core Concepts
SDK Architecture
The SDK follows a clean architecture approach with these main components:

API Layer

ApiClient: Handles REST API communication
ApiService: Defines API endpoints
Error handling and response parsing


Models Layer

Data models with validation
Type-safe operations
Serialization/deserialization


Core SDK Layer

Main interface for developers
Business logic implementation
Session management



Data Models
Rating Model
javaCopyRating rating = new Rating(
userId,    // User who created the rating
itemId,    // Item being rated
4.5f,      // Rating value (1-5)
"Great!"   // Optional description
);
Comment Model
javaCopyComment comment = new Comment(
userId,     // User who created the comment
itemId,     // Item being commented on
"Amazing!"  // Comment content
);
Error Handling
The SDK provides standardized error handling through the ErrorResponse class:
javaCopypublic class ErrorResponse {
private String message;    // Error message
private int statusCode;    // HTTP status code

    public boolean isAuthError() {
        return statusCode == 401 || statusCode == 403;
    }

    public boolean isValidationError() {
        return statusCode == 400;
    }
}
API Reference
RatingSDK Methods
Ratings Management
javaCopy// Create rating
void createRating(Rating rating, RatingCallback<Map<String, Object>> callback)

// Get ratings
void getRatings(String userId, String itemId, int page, RatingCallback<Map<String, Object>> callback)

// Update rating
void updateRating(String ratingId, Rating rating, RatingCallback<Map<String, Object>> callback)

// Delete rating
void deleteRating(String ratingId, RatingCallback<Map<String, Object>> callback)
Comments Management
javaCopy// Create comment
void createComment(Comment comment, RatingCallback<Map<String, Object>> callback)

// Get comments
void getComments(String userId, String itemId, int page, RatingCallback<Map<String, Object>> callback)

// Update comment
void updateComment(String commentId, Comment comment, RatingCallback<Map<String, Object>> callback)

// Delete comment
void deleteComment(String commentId, RatingCallback<Map<String, Object>> callback)
