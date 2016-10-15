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

import java.sql.Types;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.R.attr.type;
import static org.ethp.codepath.flicks.R.id.tvOverview;
import static org.ethp.codepath.flicks.R.id.tvTitle;

/**
 * Created by eddie_thp on 10/12/16.
 */

public class MovieArrayAdapter extends ArrayAdapter {

    private enum MovieTypeEnum { NONPOPULAR, POPULAR};

    /*
     * View holder classes, so that we don't have to execute
     * findViewById multiple times (improving performance)
     */

    /**
     * Base ViewHolder class for displaying movie images.
     * It displays the poster or backdrop, depending on type (popular or not) or orientation
     */
    static class ImageHolder {
        @BindView(R.id.ivMovieImage) ImageView ivMovieImage;

        ImageHolder()
        {
            // Default constructor, ButterKnife.bind will be executed by the child
        }

        public ImageHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /*
     * I was not really happy with the huge switch/case described in the stackoverflow post:
     * http://stackoverflow.com/questions/3514548/creating-viewholders-for-listviews-with-different-item-layouts/3515221#3515221
     * so I'm using inheritance in this case, so that I don't have to duplicate logic
     * - Maybe this is adds complexity if someone needs to maintain the code, but I think it's an interesting test for this case
     * - Note that in order for it to work, both layouts need to have the same ids for the different Movie attributes
     */

    /**
     * Non-popular movies holder displays title and overview information (an poster from superclass)
     */
    static class MovieHolder extends ImageHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

        public MovieHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getViewTypeCount() {
        return MovieTypeEnum.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        int type = MovieTypeEnum.NONPOPULAR.ordinal();

        // Considering we only have 2 types here, I'm only returning 0 or 1 instead of using an enum
        // For future reference, take a look at how the codepath example/tutorial
        // uses enums: "http://guides.codepath.com/android/Implementing-a-Heterogenous-ListView"
        Movie m = (Movie) getItem(position);
        if (m.isPopular()) {
            type = MovieTypeEnum.POPULAR.ordinal();
        }

        return type;
    }

    private View getInflatedLayoutForType(int type) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (type == MovieTypeEnum.NONPOPULAR.ordinal()) {
            return inflater.inflate(R.layout.item_movie, null);
        } else {
            return inflater.inflate(R.layout.item_popular_movie, null);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item type for this position
        int type = getItemViewType(position);
        // Get the data item for this position
        Movie m = (Movie) getItem(position);

        // Check if the existing view is being re-used
        ImageHolder vh; // View lookup cache (stored in tag)
        if (convertView == null) {
            // Inflate XML layout based on the type
            convertView = getInflatedLayoutForType(type);

            // Instantiate ViewHolder, cache/store in the convertView tag (ButterKnife will handle the setup)
            if (type == MovieTypeEnum.NONPOPULAR.ordinal()) {
                vh = new MovieHolder(convertView);
            } else {
                vh = new ImageHolder(convertView);
            }

            convertView.setTag(vh);
        } else {
            vh = (ImageHolder) convertView.getTag();
        }

        // Reset/Clear existing image
        vh.ivMovieImage.setImageResource(0);

        if (type == MovieTypeEnum.NONPOPULAR.ordinal()) { // Non-popular
            MovieHolder mh = (MovieHolder) vh;
            // Populate data into vh references to views
            mh.tvTitle.setText(m.getOriginalTitle());
            mh.tvOverview.setText(m.getOverview());
        }

        // Use Picasso 3rd party lib to get image
        String image = m.getPosterPath();
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (type == MovieTypeEnum.POPULAR.ordinal() || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            image = m.getBackdropPath();
        }

        Picasso.with(getContext()).load(image).
                transform(new RoundedCornersTransformation(10, 10)).
                placeholder(R.drawable.poster_loading).
                error(R.drawable.poster_unavailable).
                into(vh.ivMovieImage);

        return convertView;
    }
}
