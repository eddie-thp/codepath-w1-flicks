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

    public Movie(JSONObject movieJson) throws JSONException {
        this.originalTitle = movieJson.getString("original_title");
        this.overview = movieJson.getString("overview");
        this.posterPath = movieJson.getString("poster_path");
        this.backdropPath = movieJson.getString("backdrop_path");
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
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
