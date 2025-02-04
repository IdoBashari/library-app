package com.github.idobashari.rating_sdk.models;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Rating {
    @SerializedName("_id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("item_id")
    private String itemId;

    @SerializedName("rating")
    private float rating;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private Date createdAt;

    // Constructor
    public Rating(String userId, String itemId, float rating, String description) {
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
        this.description = description;
        this.createdAt = new Date();
    }
    public void setId(String id) {
        this.id = id;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean validate() {
        if (rating < 1 || rating > 5) {
            return false;
        }

        if (description != null && description.length() > 500) {
            return false;
        }

        return true;
    }
}