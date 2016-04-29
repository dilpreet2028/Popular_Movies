package com.example.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by dilpreet on 29/4/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyHolder> {

    private static ArrayList<String> list;
    private static Context context;
    public TrailerAdapter(ArrayList<String> list,Context context) {
        super();
        this.list=list;
        this.context=context;
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.youtubeV);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TrailerAdapter.context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/watch?v="+TrailerAdapter.list.get(getAdapterPosition()) ) ));
                }
            });
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
