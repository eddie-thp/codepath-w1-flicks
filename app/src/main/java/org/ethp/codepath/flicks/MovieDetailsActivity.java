package org.ethp.codepath.flicks;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ethp.codepath.flicks.models.Movie;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.ivMovieImage)
    ImageView backdrop;
    @BindView(R.id.rbPopularity)
    RatingBar popularity;
    @BindView(R.id.tvOverview)
    TextView overview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.movie);
        actionBar.setTitle(R.string.title_movie_details_activity);

        final Movie movie  = (Movie) getIntent().getSerializableExtra("MOVIE");

        backdrop.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie.play(MovieDetailsActivity.this);
            }
        });

        // Vote Average ranges from 0 - 10
        // rating = VoteAverage / (10) / NumStars -> 10/8 = 1.25f
        float rating = new BigDecimal(movie.getVoteAverage()).floatValue()/1.25f;
        popularity.setNumStars(8);
        popularity.setRating(rating);

        overview.setText(movie.getOverview());

        Picasso.with(this).load(movie.getBackdropPath()).
                transform(new RoundedCornersTransformation(10, 10)).
                placeholder(R.mipmap.backdrop_loading).
                error(R.mipmap.backdrop_no_image).
                into(backdrop);
    }
}
