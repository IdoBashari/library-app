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
