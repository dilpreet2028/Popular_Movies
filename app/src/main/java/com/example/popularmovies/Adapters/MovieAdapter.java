package com.example.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.popularmovies.Utilities.Config;
import com.example.popularmovies.Movie;
import com.example.popularmovies.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dilpreet on 23/2/16.
 */
public class MovieAdapter extends BaseAdapter {
    List<Movie> movieList;
    Context context;
    LayoutInflater inflater;
    TextView movieTitle;
    ImageView movieImage;
    public MovieAdapter(List<Movie> movieList,Context context) {
        super();
        this.movieList=movieList;
        this.context=context;
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.grid_item,null);
            movieImage=(ImageView)convertView.findViewById(R.id.movie_image);
            movieTitle=(TextView)convertView.findViewById(R.id.movie_title);
        }
        Movie currentItem=movieList.get(position);
        String url=currentItem.getImageLink();
        String title=currentItem.getTitle();

        Picasso.with(context).load(Config.IMAGE_URL+url)
                .placeholder(R.drawable.loading).into(movieImage);
       // movieTitle.setText(title);
        return convertView;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }
}
