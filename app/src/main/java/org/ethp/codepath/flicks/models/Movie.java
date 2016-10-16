package org.ethp.codepath.flicks.models;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.ethp.codepath.flicks.MovieActivity;
import org.ethp.codepath.flicks.MoviePlayerActivity;
import org.ethp.codepath.flicks.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eddie_thp on 10/12/16.
 */

public class Movie implements Serializable {

    private String id;
    private String originalTitle;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double popularity;
    private double voteAverage;

    public Movie(JSONObject movieJson) throws JSONException {
        this.id = movieJson.getString("id");
        this.originalTitle = movieJson.getString("original_title");
        this.overview = movieJson.getString("overview");
        this.posterPath = movieJson.getString("poster_path");
        this.backdropPath = movieJson.getString("backdrop_path");
        this.popularity = movieJson.getDouble("popularity");
        this.voteAverage = movieJson.getDouble("vote_average");
    }

    public String getId() { return id; }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public boolean isPopular() {
        // Using 10 just to see more than 1 popular item in the list
        // I wasn't getting many popular movies from the API
        return (voteAverage > 6);
    }

    public double getVoteAverage() { return voteAverage;}

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    public static List<Movie> fromJSONArray(JSONArray moviesJson) {
        List<Movie> movies = new ArrayList<Movie>();

        for (int i = 0; i < moviesJson.length(); i++)
        {
            try {
                JSONObject movieJson = moviesJson.getJSONObject(i);
                movies.add(new Movie(movieJson));
            } catch (JSONException jsonEx) {
                Log.e("MOVIE_JSON", "Failed parsing JSON", jsonEx);
            }
        }
        return movies;
    }

    public void play(final Activity callerActivity) {
        // e.g. https://api.themoviedb.org/3/movie/<movieId>/trailers?api_key=<API_KEY>

        final String movieId = this.getId();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.themoviedb.org/3/movie").newBuilder()
                .addPathSegment(movieId)
                .addPathSegment("trailers")
                .addQueryParameter("api_key", callerActivity.getString(R.string.api_key_movie_db));


        String url = urlBuilder.build().toString();

        Request request = (new Request.Builder())
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String youTubeVideo = null;

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    JSONArray youtubeVideos = json.getJSONArray("youtube");

                    // Let's use the first trailer we find
                    for (int i = 0; i < youtubeVideos.length(); i++)
                    {
                        JSONObject videoJson = youtubeVideos.getJSONObject(i);

                        String type = videoJson.getString("type");
                        if ("trailer".equalsIgnoreCase(type)) {
                            youTubeVideo = videoJson.getString("source");
                            break;
                        }
                    }
                } catch (JSONException e) {
                    Log.e("FETCH_MOVIE_TRAILERS", "Failed to process response " + e.getMessage(), e);
                }

                final String intentTrailer = youTubeVideo;

                // Run view-related code back on the main thread
                callerActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (intentTrailer != null)
                        {
                            Intent movieDetailsIntent = new Intent(callerActivity, MoviePlayerActivity.class);
                            movieDetailsIntent.putExtra("TRAILER", intentTrailer);
                            callerActivity.startActivity(movieDetailsIntent);
                        } else {
                            Toast.makeText(callerActivity, R.string.error_unable_to_play_movie_trailer, Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FETCH_MOVIE_TRAILERS", "Failed to fetch movie id(" + movieId + ")  trailers: " + e.getMessage(), e);

                // Run view-related code back on the main thread
                callerActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(callerActivity, R.string.error_fetch_movie_trailers, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
