package com.example.popularmovies.UI;

import android.content.Intent;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main,new MainFragment())
                .commit();




        if(findViewById(R.id.frame_detail)!=null) {
            detailFragment=new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_detail, detailFragment)
                    .commit();
            Log.d("mytag","abcd");
        }




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
