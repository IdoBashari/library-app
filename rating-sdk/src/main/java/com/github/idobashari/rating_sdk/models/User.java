package com.github.idobashari.rating_sdk.models;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class User {
    @SerializedName("_id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("created_at")
    private Date createdAt;

    // Constructor
    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.createdAt = new Date();
    }


    // Getters and Setters

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean validate() {
        if (email == null || !email.contains("@")) {
            return false;
        }

        return true;
    }
}