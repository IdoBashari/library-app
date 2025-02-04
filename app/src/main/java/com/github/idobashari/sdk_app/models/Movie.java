package com.github.idobashari.sdk_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String id;           // ישמש כ-item_id בספרייה
    private String title;
    private String description;

    public Movie(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // Parcelable implementation
    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}