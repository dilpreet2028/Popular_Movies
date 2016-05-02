package com.example.popularmovies.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.popularmovies.Adapters.MovieAdapter;
import com.example.popularmovies.Utilities.Config;
import com.example.popularmovies.Movie;
import com.example.popularmovies.R;
import com.example.popularmovies.Utilities.MoviesDb;

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
    ArrayList<Movie> moviesList;
    MovieAdapter adapter;
    GridView gridView;
    OnItemClickData onItemClickData;
    boolean restored;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        gridView=(GridView)view.findViewById(R.id.gridView);
        restored=false;

        moviesList=new ArrayList<>();
        if(savedInstanceState!=null){
            moviesList=savedInstanceState.getParcelableArrayList("data");
            Log.d("mytag","instance");
            restored=true;
        }

        adapter=new MovieAdapter(moviesList,getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Movie currentItem = moviesList.get(position);
                onItemClickData.provideData(currentItem.getId(),currentItem.getTitle(),currentItem.getImageLink(),
                        currentItem.getRating(),currentItem.getRelease(),currentItem.getSynopsis());

            }
        });



        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("data",moviesList);
    }


    public interface OnItemClickData{
        public void provideData(String id,String title,String image,String rating,String release,String synopsis);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onItemClickData=(OnItemClickData)context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()+"must implement OnItemClickData");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!restored)
        fetchMovies();

    }

    private void fetchMovies(){

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType=preferences.getString(getString(R.string.sort_key),getString(R.string.sort_def_value));
        Log.d("mytag","start "+ restored);
        if(sortType.compareToIgnoreCase("fav")!=0)
            new NetworkTask().execute();
        else{
            MoviesDb moviesDb=new MoviesDb(getActivity());
            moviesDb.getMovies(moviesList,adapter);

        }
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
            moviesList.clear();
            resultObj=new JSONObject(jsonString);
            JSONArray moviesArray=resultObj.getJSONArray("results");
            JSONObject movieObject;
            Movie newMovie;
            String title,imageLink,synopsis,rating,release,id;
            for(int i=0;i<moviesArray.length();i++){
                movieObject=moviesArray.getJSONObject(i);
                id=movieObject.getString("id");
                title=movieObject.getString("title");
                imageLink=movieObject.getString("poster_path");
                synopsis=movieObject.getString("overview");
                rating=movieObject.getString("vote_average");
                release=movieObject.getString("release_date");
                newMovie=new Movie(id,title,imageLink,synopsis,rating,release);
                moviesList.add(newMovie);

            }
        }catch (JSONException e){
             Log.d("mytag",e+"");
        }
    }
}
