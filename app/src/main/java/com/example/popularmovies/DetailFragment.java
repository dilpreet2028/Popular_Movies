package com.example.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by dilpreet on 24/2/16.
 */
public class DetailFragment extends Fragment {
    ImageView imageView;
    String title,imageLink,synopsis,rating,release;
    TextView titleView,ratingView,releaseView,textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.detail_fragment,null);
        imageView=(ImageView)view.findViewById(R.id.detail_image);
        titleView=(TextView)view.findViewById(R.id.detail_name);
        ratingView=(TextView)view.findViewById(R.id.detail_rating);
        releaseView=(TextView)view.findViewById(R.id.detail_release);
        textView=(TextView)view.findViewById(R.id.detail_text);
        Bundle bundle=getActivity().getIntent().getBundleExtra("bundle");
        title=bundle.getString("title");
        imageLink=bundle.getString("image");
        synopsis=bundle.getString("text");
        rating=bundle.getString("rating");
        release=bundle.getString("release");

        Picasso.with(getActivity()).load(Config.IMAGE_URL+imageLink).into(imageView);
        titleView.setText(title);
        ratingView.setText(rating+"/10");
        releaseView.setText(release);
        textView.setText(synopsis);

        return view;
    }
}
