package com.github.idobashari.rating_sdk.api;

import com.github.idobashari.rating_sdk.models.Comment;
import com.github.idobashari.rating_sdk.models.Rating;
import com.github.idobashari.rating_sdk.models.User;

import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;
import java.util.Map;

public interface ApiService {



    // Rating Endpoints
    @POST("api/ratings")
    Call<Map<String, Object>> createRating(@Body Rating rating);

    @GET("api/ratings/{id}")
    Call<Rating> getRating(@Path("id") String id);

    @GET("api/ratings")
    Call<Map<String, Object>> getRatings(
            @Query("user_id") String userId,
            @Query("item_id") String itemId,
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @PUT("api/ratings/{id}")
    Call<Map<String, Object>> updateRating(
            @Path("id") String id,
            @Body Rating rating
    );

    @DELETE("api/ratings/{id}")
    Call<Map<String, Object>> deleteRating(@Path("id") String id);

    // Comment Endpoints
    @POST("api/comments")
    Call<Map<String, Object>> createComment(@Body Comment comment);

    @GET("api/comments/{id}")
    Call<Comment> getComment(@Path("id") String id);

    @GET("api/comments")
    Call<Map<String, Object>> getComments(
            @Query("user_id") String userId,
            @Query("item_id") String itemId,
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @PUT("api/comments/{id}")
    Call<Map<String, Object>> updateComment(
            @Path("id") String id,
            @Body Comment comment
    );

    @DELETE("api/comments/{id}")
    Call<Map<String, Object>> deleteComment(@Path("id") String id);

    // User Endpoints
    @POST("api/users/register")
    Call<Map<String, Object>> registerUser(@Body Map<String, String> userData);

    @POST("api/users/login")
    Call<Map<String, Object>> loginUser(@Body Map<String, String> credentials);

    @GET("api/users/{id}")
    Call<User> getUser(@Path("id") String id);
}