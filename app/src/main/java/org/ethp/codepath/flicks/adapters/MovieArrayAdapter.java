package org.ethp.codepath.flicks.adapters;

import android.content.Context;
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

import static android.R.attr.resource;
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
    private static class ViewHolder {
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvOverview;
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

            // Instantiate ViewHolder, cache/store in the convertView tag and set it up
            vh = new ViewHolder();
            convertView.setTag(vh);
            vh.ivPoster = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            vh.tvTitle = (TextView) convertView.findViewById(tvTitle);
            vh.tvOverview = (TextView) convertView.findViewById(tvOverview);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // Reset/Clear existing image
        vh.ivPoster.setImageResource(0);

        // Populate data into vh references to views
        vh.tvTitle.setText(m.getOriginalTitle());
        vh.tvOverview.setText(m.getOverview());

        // Use Picasso 3rd party lib to get image
        Picasso.with(getContext()).load(m.getPosterPath()).into(vh.ivPoster);

        return convertView;
    }
}
