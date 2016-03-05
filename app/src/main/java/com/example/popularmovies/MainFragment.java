package com.example.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilpreet on 23/2/16.
 */
public class MainFragment extends Fragment {
    List<Movie> moviesList;
    MovieAdapter adapter;
    GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        gridView=(GridView)view.findViewById(R.id.gridView);

        moviesList=new ArrayList<>();
        adapter=new MovieAdapter(moviesList,getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Movie currentItem = moviesList.get(position);
                bundle.putString("title", currentItem.getTitle());
                bundle.putString("image", currentItem.getImageLink());
                bundle.putString("rating", currentItem.getRating());
                bundle.putString("release", currentItem.getRelease());
                bundle.putString("text", currentItem.getSynopsis());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        moviesList.clear();
        adapter.notifyDataSetChanged();
        new NetworkTask().execute();
    }

    private class NetworkTask extends AsyncTask<Void,Void,Integer>{
        private final String TAG=MainFragment.class.getSimpleName();
        SharedPreferences preferences;
        @Override
        protected Integer doInBackground(Void... params) {
            HttpURLConnection connection=null;
            BufferedReader bufferedReader=null;
            InputStream inputStream=null;
            Integer result=0;
            String moviesJson;

            preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortType=preferences.getString(getString(R.string.sort_key),getString(R.string.sort_def_value));

            try{
                Uri.Builder builder=Uri.parse(Config.BASE_URL).buildUpon()
                                       .appendQueryParameter("sort_by",sortType+".desc")
                                       .appendQueryParameter("api_key", Config.API_KEY);

                URL url=new URL(builder.build().toString());
                connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                inputStream=connection.getInputStream();
                if(inputStream==null){
                    return 0;
                }
                StringBuffer buffer=new StringBuffer();
                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                if(bufferedReader==null){
                    return 0;
                }
                String line;
                while((line=bufferedReader.readLine())!=null){
                    buffer.append(line+"/n");
                }
                moviesJson=buffer.toString();
                parseMovies(moviesJson);
                result=1;

            }catch (IOException e){
                Log.d(TAG,e.toString());
            }finally {
                connection.disconnect();
                try{
                    if(inputStream!=null){
                        inputStream.close();
                    }
                    if(bufferedReader!=null){
                        bufferedReader.close();
                    }
                }catch (IOException e){

                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            if(s==1)
                adapter.notifyDataSetChanged();
        }
    }

    private void parseMovies(String jsonString){
        JSONObject resultObj;
        try{
            resultObj=new JSONObject(jsonString);
            JSONArray moviesArray=resultObj.getJSONArray("results");
            JSONObject movieObject;
            Movie newMovie;
            String title,imageLink,synopsis,rating,release;
            for(int i=0;i<moviesArray.length();i++){
                movieObject=moviesArray.getJSONObject(i);
                title=movieObject.getString("title");
                imageLink=movieObject.getString("poster_path");
                synopsis=movieObject.getString("overview");
                rating=movieObject.getString("vote_average");
                release=movieObject.getString("release_date");
                newMovie=new Movie(title,imageLink,synopsis,rating,release);
                moviesList.add(newMovie);
            }
        }catch (JSONException e){
             Log.d("mytag",e+"");
        }
    }
}
