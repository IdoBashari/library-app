package com.github.idobashari.sdk_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.idobashari.rating_sdk.RatingSDK;
import com.github.idobashari.sdk_app.R;
import com.github.idobashari.sdk_app.adapters.MoviesAdapter;
import com.github.idobashari.sdk_app.models.Movie;
import com.github.idobashari.sdk_app.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView moviesRecyclerView;
    private ProgressBar progressBar;
    private MoviesAdapter moviesAdapter;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencesManager = PreferencesManager.getInstance(this);


        if (!preferencesManager.isUserLoggedIn()) {
            Log.d(TAG, "User not logged in, redirecting to login");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupToolbar();
        loadMovies();
    }

    private void initViews() {
        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        progressBar = findViewById(R.id.progressBar);

        moviesAdapter = new MoviesAdapter();
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviesRecyclerView.setAdapter(moviesAdapter);

        Log.d(TAG, "Views initialized");
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Movies");
        }
        Log.d(TAG, "Toolbar setup completed");
    }

    private void loadMovies() {

        setLoading(true);
        List<Movie> movies = getDummyMovies();
        moviesAdapter.setMovies(movies);
        setLoading(false);
        Log.d(TAG, "Movies loaded: " + movies.size());
    }

    private List<Movie> getDummyMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("1", "The Shawshank Redemption", "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));
        movies.add(new Movie("2", "The Godfather", "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son."));
        movies.add(new Movie("3", "The Dark Knight", "When the menace known as The Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham."));
        movies.add(new Movie("4", "Pulp Fiction", "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption."));
        movies.add(new Movie("5", "Fight Club", "An insomniac office worker and a devil-may-care soapmaker form an underground fight club that evolves into something much, much more."));
        return movies;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Log.d(TAG, "Performing logout");
        preferencesManager.clearUserData();
        RatingSDK.getInstance().setAuthToken(null);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}