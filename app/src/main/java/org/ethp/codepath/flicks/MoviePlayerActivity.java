package org.ethp.codepath.flicks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.ethp.codepath.flicks.models.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviePlayerActivity extends YouTubeBaseActivity {

    @BindView(R.id.moviePlayer)
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);

        ButterKnife.bind(this);

        final String trailer = getIntent().getStringExtra("TRAILER");

        youTubePlayerView.initialize(getString(R.string.api_key_youtube_data),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        // a07e22bc18f5cb106bfe4cc1f83ad8ed
                        // https://api.themoviedb.org/3/movie/333484/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed

                        youTubePlayer.loadVideo(trailer);

                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
