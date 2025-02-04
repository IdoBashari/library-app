package com.github.idobashari.sdk_app.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.idobashari.sdk_app.R;
import com.github.idobashari.sdk_app.activities.MovieDetailsActivity;
import com.github.idobashari.sdk_app.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private static final String TAG = "MoviesAdapter";
    private final List<Movie> movies = new ArrayList<>();

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
        Log.d(TAG, "Binding movie: " + movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> newMovies) {
        movies.clear();
        movies.addAll(newMovies);
        notifyDataSetChanged();
        Log.d(TAG, "Updated movies list. Size: " + movies.size());
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descriptionView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.movieTitle);
            descriptionView = itemView.findViewById(R.id.movieDescription);
        }

        void bind(Movie movie) {
            titleView.setText(movie.getTitle());
            descriptionView.setText(movie.getDescription());

            // מעבר למסך פרטי הסרט בלחיצה על הפריט
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), MovieDetailsActivity.class);
                intent.putExtra("movie", movie);
                itemView.getContext().startActivity(intent);
                Log.d(TAG, "Clicked on movie: " + movie.getTitle());
            });
        }
    }
}