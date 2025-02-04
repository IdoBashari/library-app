package com.github.idobashari.sdk_app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.idobashari.rating_sdk.models.Rating;
import com.github.idobashari.sdk_app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.RatingViewHolder> {
    private static final String TAG = "RatingsAdapter";
    private final List<Rating> ratings = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private String currentUserId;
    private RatingActionListener actionListener;


    public interface RatingActionListener {
        void onEditRating(Rating rating);
        void onDeleteRating(Rating rating);
    }

    // setters
    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
        notifyDataSetChanged();
    }

    public void setActionListener(RatingActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating rating = ratings.get(position);
        holder.bind(rating);
        Log.d(TAG, "Binding rating at position " + position);
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public void setRatings(List<Rating> newRatings) {
        ratings.clear();
        if (newRatings != null) {
            ratings.addAll(newRatings);
        }
        notifyDataSetChanged();
        Log.d(TAG, "Updated ratings list. Size: " + ratings.size());
    }

    class RatingViewHolder extends RecyclerView.ViewHolder {
        private final RatingBar ratingBar;
        private final TextView descriptionView;
        private final TextView dateView;
        private final ImageButton editButton;
        private final ImageButton deleteButton;
        private final View actionButtons;

        RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.itemRatingBar);
            descriptionView = itemView.findViewById(R.id.ratingDescription);
            dateView = itemView.findViewById(R.id.ratingDate);
            editButton = itemView.findViewById(R.id.editRatingButton);
            deleteButton = itemView.findViewById(R.id.deleteRatingButton);
            actionButtons = itemView.findViewById(R.id.actionButtons);
        }

        void bind(Rating rating) {
            ratingBar.setRating(rating.getRating());

            String description = rating.getDescription();
            if (description != null && !description.isEmpty()) {
                descriptionView.setVisibility(View.VISIBLE);
                descriptionView.setText(description);
            } else {
                descriptionView.setVisibility(View.GONE);
            }

            if (rating.getCreatedAt() != null) {
                dateView.setText(dateFormat.format(rating.getCreatedAt()));
            }


            boolean isCurrentUserRating = currentUserId != null &&
                    currentUserId.equals(rating.getUserId());
            actionButtons.setVisibility(isCurrentUserRating ? View.VISIBLE : View.GONE);

            if (isCurrentUserRating) {
                editButton.setOnClickListener(v -> {
                    if (actionListener != null) {
                        actionListener.onEditRating(rating);
                    }
                });

                deleteButton.setOnClickListener(v -> {
                    if (actionListener != null) {
                        actionListener.onDeleteRating(rating);
                    }
                });
            }

            Log.d(TAG, "Rating bound. User ID: " + rating.getUserId() +
                    ", Current User: " + currentUserId +
                    ", Is Owner: " + isCurrentUserRating);
        }
    }
}