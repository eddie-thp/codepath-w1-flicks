package org.ethp.codepath.flicks.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eddie_thp on 10/12/16.
 */

public class Movie {
    private String originalTitle;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double popularity;

    public Movie(JSONObject movieJson) throws JSONException {
        this.originalTitle = movieJson.getString("original_title");
        this.overview = movieJson.getString("overview");
        this.posterPath = movieJson.getString("poster_path");
        this.backdropPath = movieJson.getString("backdrop_path");
        this.popularity = movieJson.getDouble("popularity");
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public boolean isPopular() {
        // Using 10 just to see more than 1 popular item in the list
        // I wasn't getting many popular movies from the API
        return (popularity >= 10);
    }

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
}
