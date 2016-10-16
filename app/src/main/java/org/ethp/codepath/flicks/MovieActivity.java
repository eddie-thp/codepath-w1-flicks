package org.ethp.codepath.flicks;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ethp.codepath.flicks.adapters.MovieArrayAdapter;
import org.ethp.codepath.flicks.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.transition.move;
import static android.os.Build.VERSION_CODES.M;

public class MovieActivity extends AppCompatActivity {

    private List<Movie> movies;
    private MovieArrayAdapter movieAdapter;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.lvMovies)
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        // Setup the Movies adapter and list view
        movies = new ArrayList<Movie>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        // Setup onMovieClick
        lvItems.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Intent movieDetailsIntent = new Intent(MovieActivity.this, MovieDetailsActivity.class);
                movieDetailsIntent.putExtra("MOVIE", movie);
                startActivity(movieDetailsIntent);
            }
        });

        // Fetch the movies
        fetchMovies();

        // Setup the swipe container view refresh listener to fetch the movies
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.themoviedb.org/3/movie/now_playing").newBuilder();
        urlBuilder.addQueryParameter("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed");
        String url = urlBuilder.build().toString();

        Request request = (new Request.Builder())
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                movies.clear();
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    JSONArray nowPlayingResultsJSON = json.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(nowPlayingResultsJSON));
                } catch (JSONException e) {
                    Log.e("FETCH_MOVIES", "Failed to process response " + e.getMessage(), e);
                }

                // Run view-related code back on the main thread
                MovieActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieAdapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FETCH_MOVIES", "Failed to fetch movies: " + e.getMessage(), e);

                // Run view-related code back on the main thread
                MovieActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MovieActivity.this, R.string.error_fetch_movies, Toast.LENGTH_LONG).show();
                        swipeContainer.setRefreshing(false);
                    }
                });
            }
        });
    }
}
