package com.github.idobashari.rating_sdk.models;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Comment {
    @SerializedName("_id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("item_id")
    private String itemId;

    @SerializedName("content")
    private String content;

    @SerializedName("created_at")
    private Date createdAt;

    // Constructor
    public Comment(String userId, String itemId, String content) {
        this.userId = userId;
        this.itemId = itemId;
        this.content = content;
        this.createdAt = new Date();
    }

    // Getters and Setters

    public void setId(String id) {
        this.id = id;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean validate() {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        if (content.length() > 1000) {
            return false;
        }

        return true;
    }
}