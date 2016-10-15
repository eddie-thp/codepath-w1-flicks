package org.ethp.codepath.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ethp.codepath.flicks.R;
import org.ethp.codepath.flicks.models.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static org.ethp.codepath.flicks.R.id.tvOverview;
import static org.ethp.codepath.flicks.R.id.tvTitle;

/**
 * Created by eddie_thp on 10/12/16.
 */

public class MovieArrayAdapter extends ArrayAdapter {

    /**
     * View holder class, so that we don't have to execute
     * findViewById multiple times (improving performance)
     */
    static class ViewHolder {
        @BindView(R.id.ivMovieImage) ImageView ivPoster;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Movie m = (Movie) getItem(position);

        // Check if the existing view is being re-used
        ViewHolder vh; // View lookup cache (stored in tag)
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false); // False means, do not attach

            // Instantiate ViewHolder, cache/store in the convertView tag (ButterKnife will handle the setup)
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // Reset/Clear existing image
        vh.ivPoster.setImageResource(0);

        // Populate data into vh references to views
        vh.tvTitle.setText(m.getOriginalTitle());
        vh.tvOverview.setText(m.getOverview());

        // Use Picasso 3rd party lib to get image
        String image = m.getPosterPath();
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            image = m.getBackdropPath();
        }

        Picasso.with(getContext()).load(image).
                transform(new RoundedCornersTransformation(10, 10)).
                placeholder(R.drawable.poster_loading).
                error(R.drawable.poster_unavailable).
                into(vh.ivPoster);

        return convertView;
    }
}
