package com.example.popularmovies.UI;

import android.content.Intent;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmovies.R;
import com.example.popularmovies.Utilities.SettingsActivity;

public class MainActivity extends AppCompatActivity implements MainFragment.OnItemClickData{

    Toolbar toolbar;
    DetailFragment detailFragment;
    String id,title, image, rating, release,synopsis;
    public static  boolean restored;
    MainFragment mainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        restored=false;
        setSupportActionBar(toolbar);
        if(savedInstanceState==null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_main, mainFragment)
                    .commit();

        }

        if(findViewById(R.id.frame_detail)!=null) {
            if(savedInstanceState==null) {
                detailFragment = new DetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_detail, detailFragment)
                        .commit();
            }
            else{
                Log.d("mytag","saved retrieved");
                detailFragment=(DetailFragment)getSupportFragmentManager().getFragment(savedInstanceState,"content");
            }
        }



//        if(savedInstanceState!=null&&detailFragment!=null){
//            Log.d("mytag","Asa "+savedInstanceState.getString("image"));
//            detailFragment.setFields(savedInstanceState.getString("id"),savedInstanceState.getString("title"),
//                    savedInstanceState.getString("image"),savedInstanceState.getString("rating"),savedInstanceState.getString("release"),
//                    savedInstanceState.getString("synopsis"));
//        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(detailFragment!=null)
        getSupportFragmentManager().putFragment(outState,"content",detailFragment);
        outState.putString("first","first");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void provideData(String id,String title, String image, String rating, String release, String synopsis) {
        this.id=id;
        this.title=title;
        this.image=image;
        this.rating=rating;
        this.release=release;
        this.synopsis=synopsis;

        if(detailFragment==null){
            Bundle bundle = new Bundle();
            bundle.putString("id",id);
            bundle.putString("title",title);
                bundle.putString("image", image);
                bundle.putString("rating", rating);
                bundle.putString("release",release);
                bundle.putString("text", synopsis);

                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
        }
        else{
            detailFragment.setFields(id,title,image,rating,release,synopsis);
        }
    }
}
