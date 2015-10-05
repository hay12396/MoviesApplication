package hayzohar.moviesapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import hayzohar.moviesapplication.moviesapp.R;

/**
 * Created by Hay Zohar on 02/10/2015.
 */
public class GridViewAdapter extends ArrayAdapter<Movie> implements Serializable {
    private Context context;

    public GridViewAdapter(Context context, int resource, List<Movie> items) {
        super(context, resource, items);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.grid_item_layout, parent, false);
        final ImageView imageView = (ImageView) row.findViewById(R.id.gridItemImageView);


        final Movie movie = getItem(position);
        if (movie != null) {
            Picasso.with(context).load(movie.getmImageUrl()).into(imageView);
        }

        return row;
    }
}
