Demo App Guide
App Architecture
The demo app demonstrates SDK usage in a real-world scenario:

Activities

LoginActivity: User authentication
MainActivity: Movie listing
MovieDetailsActivity: Rating and comment management


Adapters

MoviesAdapter: Movie list display
RatingsAdapter: Ratings display
CommentsAdapter: Comments display


Utils

PreferencesManager: Session management



Implementation Examples
Rating Implementation
javaCopypublic class MovieDetailsActivity extends AppCompatActivity {
private RatingSDK ratingSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ratingSDK = RatingSDK.getInstance();
        
        // Setup rating submission
        submitRatingButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String description = ratingDescription.getText().toString();
            
            Rating newRating = new Rating(
                getUserId(),
                getMovieId(),
                rating,
                description
            );
            
            ratingSDK.createRating(newRating, new RatingCallback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    // Handle success
                }
                
                @Override
                public void onError(ErrorResponse error) {
                    // Handle error
                }
            });
        });
    }
}