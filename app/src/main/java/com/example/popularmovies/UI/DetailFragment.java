package com.example.popularmovies.UI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmovies.Adapters.TrailerAdapter;
import com.example.popularmovies.Utilities.Config;
import com.example.popularmovies.R;
import com.example.popularmovies.Utilities.MoviesDb;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dilpreet on 24/2/16.
 */
public class DetailFragment extends Fragment {
    ImageView imageView;
    String mTitle="as",mImageLink="as",mSynopsis="as",mRating="as",mRelease="as",mId;
    TextView titleView,ratingView,releaseView,textView,reviewView;
    ArrayList<String> listVideo;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TrailerAdapter trailerAdapter;
    ImageView favImage;
    MoviesDb moviesDb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.detail_fragment,null);
        imageView=(ImageView)view.findViewById(R.id.detail_image);
        titleView=(TextView)view.findViewById(R.id.detail_name);
        ratingView=(TextView)view.findViewById(R.id.detail_rating);
        releaseView=(TextView)view.findViewById(R.id.detail_release);
        textView=(TextView)view.findViewById(R.id.detail_text);
        reviewView=(TextView)view.findViewById(R.id.reviews);
        recyclerView=(RecyclerView)view.findViewById(R.id.video_list);
        favImage=(ImageView)view.findViewById(R.id.fav);

        layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        moviesDb=new MoviesDb(getActivity());

        listVideo=new ArrayList<>();
        trailerAdapter=new TrailerAdapter(listVideo,getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trailerAdapter);
        if (savedInstanceState != null) {
            setFields(savedInstanceState.getString("id"),savedInstanceState.getString("title"),
                    savedInstanceState.getString("image"),savedInstanceState.getString("rating"),savedInstanceState.getString("release"),
                    savedInstanceState.getString("synopsis"));
            reviewView.setText(savedInstanceState.getString("review"));
            listVideo.clear();
            listVideo.addAll(savedInstanceState.getStringArrayList("movies"));
            trailerAdapter.notifyDataSetChanged();


        }
        else {
            try {
                Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
                mId = bundle.getString("id");
                mTitle = bundle.getString("title");
                mImageLink = bundle.getString("image");
                mSynopsis = bundle.getString("text");
                mRating = bundle.getString("rating");
                mRelease = bundle.getString("release");
                setFields(mId, mTitle, mImageLink, mRating, mRelease, mSynopsis);

            } catch (NullPointerException e) {
                Log.d("mytag", "First Run");
            }
        }
        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean res=moviesDb.checkFav(mId);
                if(res==false){
                    moviesDb.addMovie(mId,mTitle,mImageLink,mRating,mRelease,mSynopsis);
                    favImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fav));
                }
                else{
                    moviesDb.removeMovie(mId);
                    favImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.nonfav));
                }

            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("movies",listVideo);
        outState.putString("review",reviewView.getText().toString());
        outState.putString("id",mId);
        outState.putString("title",mTitle);
        outState.putString("rating",mRating);
        outState.putString("image",mImageLink);
        outState.putString("release",mRelease);
        outState.putString("synopsis",mSynopsis);
    }

    public void setFields(String id, String title, String image, String rating, String release, String synopsis){
        mId=id;mTitle=title;mImageLink=image;mRating=rating;mRelease=release;mSynopsis=synopsis;
        Picasso.with(getActivity()).load(Config.IMAGE_URL + image).into(imageView);

        titleView.setText(title);
        ratingView.setText(rating + "/10");
        releaseView.setText(release);
        textView.setText(synopsis);
        loadVideos(id);
        loadReviews(id);
        boolean res=moviesDb.checkFav(id);
        if(res==true)
            favImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fav));
        else
            favImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.nonfav));
    }



    private void loadVideos(String id){
        listVideo.clear();
        StringRequest stringRequest=new StringRequest(Config.SUB_URL +id+ "/videos?api_key="+Config.API_KEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject parentObj;
                        try{
                            parentObj=new JSONObject(response);
                            JSONArray array=parentObj.getJSONArray("results");
                            for( int i=0;i<array.length();i++){
                                listVideo.add(array.getJSONObject(i).getString("key"));
                            }
                            trailerAdapter.notifyDataSetChanged();
                        }catch (JSONException e){
                            Log.d("mytag",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag",error.toString());
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void loadReviews(String id){
        StringRequest stringRequest=new StringRequest(Config.SUB_URL +id+ "/reviews?api_key="+Config.API_KEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject parentObj;
                        try{
                            parentObj=new JSONObject(response);
                            JSONArray array=parentObj.getJSONArray("results");
                            for( int i=0;i<array.length();i++){
                                reviewView.append("-> "+array.getJSONObject(i).getString("content"));
                            }

                        }catch (JSONException e){
                            Log.d("mytag",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag",error.toString());
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

}
