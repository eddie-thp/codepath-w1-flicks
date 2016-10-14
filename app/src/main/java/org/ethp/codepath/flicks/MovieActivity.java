package org.ethp.codepath.flicks;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.ethp.codepath.flicks.adapters.MovieArrayAdapter;
import org.ethp.codepath.flicks.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.R.transition.move;

public class MovieActivity extends AppCompatActivity {

    private List<Movie> movies;
    private MovieArrayAdapter movieAdapter;

    private SwipeRefreshLayout swipeContainer;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Setup the Movies adapter and list view
        movies = new ArrayList<Movie>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems = (ListView) findViewById(R.id.lvMovies);
        lvItems.setAdapter(movieAdapter);
        // Fetch the movies
        fetchMovies();

        // Setup the swipe container view refresh listener to fetch the movies
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchMovies();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void fetchMovies()
    {
        String nowPlayingURL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(nowPlayingURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                movies.clear();
                try {
                    JSONArray nowPlayingResultsJSON = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(nowPlayingResultsJSON));
                    movieAdapter.notifyDataSetChanged();
                } catch (JSONException jsonEx) {
                    // TODO error logging
                    jsonEx.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getBaseContext(), R.string.error_fetch_movies, Toast.LENGTH_LONG);
                Log.e("FETCH_MOVIES", "Failed to fetch movies: " + throwable.getMessage(), throwable);
                swipeContainer.setRefreshing(false);
            }
        });

    }
}
