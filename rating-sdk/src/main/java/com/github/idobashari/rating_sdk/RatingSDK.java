package com.github.idobashari.rating_sdk;

import com.github.idobashari.rating_sdk.api.ApiClient;
import com.github.idobashari.rating_sdk.api.ApiService;
import com.github.idobashari.rating_sdk.api.ErrorResponse;
import com.github.idobashari.rating_sdk.callbacks.RatingCallback;
import com.github.idobashari.rating_sdk.models.Comment;
import com.github.idobashari.rating_sdk.models.Rating;
import com.github.idobashari.rating_sdk.models.User;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingSDK {
    private final ApiService apiService;
    private static RatingSDK instance;


    private static final int VALIDATION_ERROR = 400;
    private static final int AUTH_ERROR = 401;
    private static final int NOT_FOUND = 404;
    private static final int CONFLICT = 409;
    private static final int SERVER_ERROR = 500;

    private RatingSDK() {
        this.apiService = ApiClient.getApiService();
    }

    public static RatingSDK getInstance() {
        if (instance == null) {
            instance = new RatingSDK();
        }
        return instance;
    }

    public void setAuthToken(String token) {
        ApiClient.setAuthToken(token);
    }


    private ErrorResponse handleNetworkError(Throwable t) {
        return new ErrorResponse(t.getMessage(), SERVER_ERROR);
    }

    private ErrorResponse handleResponseError(Response<?> response) {
        String message = response.message();
        int code = response.code();


        if (code == NOT_FOUND) {
            message = "User not found";
        } else if (code == AUTH_ERROR) {
            message = "Authentication failed";
        } else if (code == CONFLICT) {
            message = "Item already exists";
        }

        return new ErrorResponse(message, code);
    }

    private ErrorResponse handleValidationError(String message) {
        return new ErrorResponse(message, VALIDATION_ERROR);
    }

    // User Methods
    public void register(String email, String password, String name, RatingCallback<Map<String, Object>> callback) {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("name", name);

        apiService.registerUser(userData).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

    public void login(String email, String password, RatingCallback<Map<String, Object>> callback) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        apiService.loginUser(credentials).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

    // Rating Methods
    public void createRating(Rating rating, RatingCallback<Map<String, Object>> callback) {
        if (!rating.validate()) {
            callback.onError(handleValidationError("Invalid rating data"));
            return;
        }

        apiService.createRating(rating).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

    public void getRatings(String userId, String itemId, int page, RatingCallback<Map<String, Object>> callback) {
        apiService.getRatings(userId, itemId, page, 10).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

    // Comment Methods
    public void createComment(Comment comment, RatingCallback<Map<String, Object>> callback) {
        if (!comment.validate()) {
            callback.onError(handleValidationError("Invalid comment data"));
            return;
        }

        apiService.createComment(comment).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

    public void getComments(String userId, String itemId, int page, RatingCallback<Map<String, Object>> callback) {
        apiService.getComments(userId, itemId, page, 10).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }
    // New Methods for Ratings
    public void updateRating(String ratingId, Rating rating, RatingCallback<Map<String, Object>> callback) {
        if (!rating.validate()) {
            callback.onError(handleValidationError("Invalid rating data"));
            return;
        }

        apiService.updateRating(ratingId, rating).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

    public void deleteRating(String ratingId, RatingCallback<Map<String, Object>> callback) {
        apiService.deleteRating(ratingId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

    // New Methods for Comments
    public void updateComment(String commentId, Comment comment, RatingCallback<Map<String, Object>> callback) {
        if (!comment.validate()) {
            callback.onError(handleValidationError("Invalid comment data"));
            return;
        }

        apiService.updateComment(commentId, comment).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

    public void deleteComment(String commentId, RatingCallback<Map<String, Object>> callback) {
        apiService.deleteComment(commentId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(handleResponseError(response));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError(handleNetworkError(t));
            }
        });
    }

}