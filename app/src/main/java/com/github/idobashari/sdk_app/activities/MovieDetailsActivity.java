package com.github.idobashari.sdk_app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.idobashari.rating_sdk.RatingSDK;
import com.github.idobashari.rating_sdk.api.ErrorResponse;
import com.github.idobashari.rating_sdk.callbacks.RatingCallback;
import com.github.idobashari.rating_sdk.models.Comment;
import com.github.idobashari.rating_sdk.models.Rating;
import com.github.idobashari.sdk_app.R;
import com.github.idobashari.sdk_app.adapters.CommentsAdapter;
import com.github.idobashari.sdk_app.adapters.RatingsAdapter;
import com.github.idobashari.sdk_app.models.Movie;
import com.github.idobashari.sdk_app.utils.PreferencesManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDetailsActivity extends AppCompatActivity
        implements RatingsAdapter.RatingActionListener,
        CommentsAdapter.CommentActionListener {

    private static final String TAG = "MovieDetailsActivity";

    private Movie movie;
    private RatingSDK ratingSDK;
    private PreferencesManager preferencesManager;
    private RatingsAdapter ratingsAdapter;
    private CommentsAdapter commentsAdapter;

    // UI Components
    private TextView movieTitle;
    private TextView movieDescription;
    private RatingBar ratingBar;
    private EditText ratingDescription;
    private Button submitRatingButton;
    private EditText commentInput;
    private Button submitCommentButton;
    private RecyclerView ratingsRecyclerView;
    private RecyclerView commentsRecyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movie = getIntent().getParcelableExtra("movie");
        if (movie == null) {
            Log.e(TAG, "No movie data received");
            finish();
            return;
        }

        ratingSDK = RatingSDK.getInstance();
        preferencesManager = PreferencesManager.getInstance(this);

        initViews();
        setupToolbar();
        loadMovieData();
        setupAdapters();
        loadRatingsAndComments();
        setupListeners();
    }

    private void initViews() {
        movieTitle = findViewById(R.id.movieTitle);
        movieDescription = findViewById(R.id.movieDescription);
        ratingBar = findViewById(R.id.ratingBar);
        ratingDescription = findViewById(R.id.ratingDescription);
        submitRatingButton = findViewById(R.id.submitRatingButton);
        commentInput = findViewById(R.id.commentInput);
        submitCommentButton = findViewById(R.id.submitCommentButton);
        ratingsRecyclerView = findViewById(R.id.ratingsRecyclerView);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.title_movie_details));
        }
    }

    private void setupAdapters() {

        ratingsAdapter = new RatingsAdapter();
        commentsAdapter = new CommentsAdapter();

        ratingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ratingsRecyclerView.setAdapter(ratingsAdapter);
        commentsRecyclerView.setAdapter(commentsAdapter);


        String currentUserId = preferencesManager.getUserId();
        ratingsAdapter.setCurrentUserId(currentUserId);
        ratingsAdapter.setActionListener(this);
        commentsAdapter.setCurrentUserId(currentUserId);
        commentsAdapter.setActionListener(this);
    }

    private void loadMovieData() {
        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getDescription());
    }

    private void setupListeners() {
        submitRatingButton.setOnClickListener(v -> submitRating());
        submitCommentButton.setOnClickListener(v -> submitComment());
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        submitRatingButton.setEnabled(!isLoading);
        submitCommentButton.setEnabled(!isLoading);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void submitRating() {
        float rating = ratingBar.getRating();
        String description = ratingDescription.getText().toString().trim();
        String userId = preferencesManager.getUserId();

        if (rating == 0) {
            showError(getString(R.string.error_rating_required));
            return;
        }

        if (userId == null) {
            showError(getString(R.string.error_not_logged_in));
            return;
        }

        setLoading(true);
        Rating newRating = new Rating(userId, movie.getId(), rating, description);

        ratingSDK.createRating(newRating, new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.d(TAG, "Rating submitted successfully");
                runOnUiThread(() -> {
                    setLoading(false);
                    showMessage(getString(R.string.rating_submitted));
                    ratingDescription.setText("");
                    ratingBar.setRating(0);
                    loadRatingsAndComments();
                });
            }

            @Override
            public void onError(ErrorResponse error) {
                Log.e(TAG, "Error submitting rating: " + error.getMessage());
                runOnUiThread(() -> {
                    setLoading(false);
                    showError(error.getMessage());
                });
            }
        });
    }

    private void submitComment() {
        String content = commentInput.getText().toString().trim();
        String userId = preferencesManager.getUserId();

        if (content.isEmpty()) {
            showError(getString(R.string.error_comment_required));
            return;
        }

        if (userId == null) {
            showError(getString(R.string.error_not_logged_in));
            return;
        }

        setLoading(true);
        Comment newComment = new Comment(userId, movie.getId(), content);

        ratingSDK.createComment(newComment, new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.d(TAG, "Comment submitted successfully");
                runOnUiThread(() -> {
                    setLoading(false);
                    showMessage(getString(R.string.comment_submitted));
                    commentInput.setText("");
                    loadRatingsAndComments();
                });
            }

            @Override
            public void onError(ErrorResponse error) {
                Log.e(TAG, "Error submitting comment: " + error.getMessage());
                runOnUiThread(() -> {
                    setLoading(false);
                    showError(error.getMessage());
                });
            }
        });
    }

    private void loadRatingsAndComments() {
        setLoading(true);

        // Load ratings
        ratingSDK.getRatings(null, movie.getId(), 1, new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.d(TAG, "Ratings loaded successfully");
                try {
                    List<Map<String, Object>> ratingsData = (List<Map<String, Object>>) result.get("ratings");
                    if (ratingsData != null) {
                        List<Rating> ratings = new ArrayList<>();
                        for (Map<String, Object> ratingData : ratingsData) {
                            Rating rating = new Rating(
                                    (String) ratingData.get("user_id"),
                                    (String) ratingData.get("item_id"),
                                    ((Number) ratingData.get("rating")).floatValue(),
                                    (String) ratingData.get("description")
                            );
                            rating.setId((String) ratingData.get("_id"));
                            ratings.add(rating);
                        }
                        runOnUiThread(() -> ratingsAdapter.setRatings(ratings));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing ratings: " + e.getMessage());
                }
                setLoading(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                Log.e(TAG, "Error loading ratings: " + error.getMessage());
                runOnUiThread(() -> {
                    showError(getString(R.string.error_loading_ratings));
                    setLoading(false);
                });
            }
        });

        // Load comments
        ratingSDK.getComments(null, movie.getId(), 1, new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.d(TAG, "Comments loaded successfully");
                try {
                    List<Map<String, Object>> commentsData = (List<Map<String, Object>>) result.get("comments");
                    if (commentsData != null) {
                        List<Comment> comments = new ArrayList<>();
                        for (Map<String, Object> commentData : commentsData) {
                            Comment comment = new Comment(
                                    (String) commentData.get("user_id"),
                                    (String) commentData.get("item_id"),
                                    (String) commentData.get("content")
                            );
                            comment.setId((String) commentData.get("_id"));
                            comments.add(comment);
                        }
                        runOnUiThread(() -> commentsAdapter.setComments(comments));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing comments: " + e.getMessage());
                }
                setLoading(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                Log.e(TAG, "Error loading comments: " + error.getMessage());
                runOnUiThread(() -> {
                    showError(getString(R.string.error_loading_comments));
                    setLoading(false);
                });
            }
        });
    }

    // Callbacks for Rating actions
    @Override
    public void onEditRating(Rating rating) {
        showEditRatingDialog(rating);
    }

    @Override
    public void onDeleteRating(Rating rating) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_rating_title)
                .setMessage(R.string.delete_rating_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteRating(rating))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    // Callbacks for Comment actions
    @Override
    public void onEditComment(Comment comment) {
        showEditCommentDialog(comment);
    }

    @Override
    public void onDeleteComment(Comment comment) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_comment_title)
                .setMessage(R.string.delete_comment_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteComment(comment))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    // Dialog methods
    private void showEditRatingDialog(Rating rating) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_rating, null);
        RatingBar dialogRatingBar = dialogView.findViewById(R.id.dialogRatingBar);
        EditText dialogDescription = dialogView.findViewById(R.id.dialogRatingDescription);

        dialogRatingBar.setRating(rating.getRating());
        dialogDescription.setText(rating.getDescription());

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.edit_rating_title)
                .setView(dialogView)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    float newRating = dialogRatingBar.getRating();
                    String newDescription = dialogDescription.getText().toString().trim();

                    if (newRating == 0) {
                        showError(getString(R.string.error_rating_required));
                        return;
                    }

                    Rating updatedRating = new Rating(
                            rating.getUserId(),
                            rating.getItemId(),
                            newRating,
                            newDescription
                    );
                    updateRating(rating.getId(), updatedRating);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showEditCommentDialog(Comment comment) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_comment, null);
        EditText dialogContent = dialogView.findViewById(R.id.dialogCommentContent);

        dialogContent.setText(comment.getContent());

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.edit_comment_title)
                .setView(dialogView)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String newContent = dialogContent.getText().toString().trim();

                    if (newContent.isEmpty()) {
                        showError(getString(R.string.error_comment_required));
                        return;
                    }

                    Comment updatedComment = new Comment(
                            comment.getUserId(),
                            comment.getItemId(),
                            newContent
                    );
                    updateComment(comment.getId(), updatedComment);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    // API methods for updating and deleting
    private void updateRating(String ratingId, Rating updatedRating) {
        setLoading(true);
        Rating ratingWithId = new Rating(
                updatedRating.getUserId(),
                updatedRating.getItemId(),
                updatedRating.getRating(),
                updatedRating.getDescription()
        );

        ratingSDK.updateRating(ratingId, ratingWithId, new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showMessage(getString(R.string.rating_updated));
                    loadRatingsAndComments();
                });
            }

            @Override
            public void onError(ErrorResponse error) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showError(error.getMessage());
                });
            }
        });
    }

    private void deleteRating(Rating rating) {
        setLoading(true);
        ratingSDK.deleteRating(rating.getId(), new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showMessage(getString(R.string.rating_deleted));
                    loadRatingsAndComments();
                });
            }

            @Override
            public void onError(ErrorResponse error) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showError(error.getMessage());
                });
            }
        });
    }

    private void updateComment(String commentId, Comment updatedComment) {
        setLoading(true);
        Comment commentWithId = new Comment(
                updatedComment.getUserId(),
                updatedComment.getItemId(),
                updatedComment.getContent()
        );

        ratingSDK.updateComment(commentId, commentWithId, new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showMessage(getString(R.string.comment_updated));
                    loadRatingsAndComments();
                });
            }

            @Override
            public void onError(ErrorResponse error) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showError(error.getMessage());
                });
            }
        });
    }

    private void deleteComment(Comment comment) {
        setLoading(true);
        ratingSDK.deleteComment(comment.getId(), new RatingCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showMessage(getString(R.string.comment_deleted));
                    loadRatingsAndComments();
                });
            }

            @Override
            public void onError(ErrorResponse error) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showError(error.getMessage());
                });
            }
        });
    }
}