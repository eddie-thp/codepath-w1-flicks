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

/**
 * Created by eddie_thp on 10/12/16.
 */

public class MovieArrayAdapter extends ArrayAdapter {

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Movie m = (Movie) getItem(position);

        // Check if the existing view is bein gre-used
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false); // False means, do not attach
        }

        // Find image view
        ImageView ivPoster = (ImageView) convertView.findViewById(R.id.ivMovieImage);
        ivPoster.setImageResource(0); // Reset/Clear existing convertView

        //
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);

        // Populate data
        tvTitle.setText(m.getOriginalTitle());
        tvOverview.setText(m.getOverview());

        // Use Picasso 3rd party lib to get image
        Picasso.with(getContext()).load(m.getPosterPath()).into(ivPoster);

        return convertView;
    }
}
