package com.example.popularmovies.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.popularmovies.Adapters.MovieAdapter;
import com.example.popularmovies.Movie;

import java.util.ArrayList;

/**
 * Created by dilpreet on 30/4/16.
 */
public class MoviesDb {
    Context context;
    MovieHelper movieHelper;
    public MoviesDb(Context context) {
        super();
        this.context=context;
        movieHelper=new MovieHelper(context);
    }

    public void addMovie(String id,String title,String image,String rating,String release,String synopsis) {

        SQLiteDatabase database = movieHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieHelper.ID, id);
        contentValues.put(MovieHelper.TITLE, title);
        contentValues.put(MovieHelper.IMAGE, image);
        contentValues.put(MovieHelper.RATING, rating);
        contentValues.put(MovieHelper.RELEASE, release);
        contentValues.put(MovieHelper.SYNOPSIS, synopsis);
        database.insert(MovieHelper.TABLENAME, null, contentValues);

    }

    public void removeMovie(String id){
        SQLiteDatabase database=movieHelper.getWritableDatabase();
        database.execSQL("delete from "+MovieHelper.TABLENAME+" where "+MovieHelper.ID+" ='"+id+"';");
    }
    public void getMovies(ArrayList<Movie> movies, MovieAdapter adapter){
        movies.clear();
        SQLiteDatabase database=movieHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from "+MovieHelper.TABLENAME+" ;",null);
        Movie newMovie;
        while(cursor.moveToNext()){
            newMovie=new Movie(cursor.getString(cursor.getColumnIndex(MovieHelper.ID)),
                    cursor.getString(cursor.getColumnIndex(MovieHelper.TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieHelper.IMAGE)),
                    cursor.getString(cursor.getColumnIndex(MovieHelper.SYNOPSIS)),
                    cursor.getString(cursor.getColumnIndex(MovieHelper.RATING)),
                    cursor.getString(cursor.getColumnIndex(MovieHelper.RELEASE)));
            movies.add(newMovie);
            Log.d("mytag",newMovie.getId());
        }

        adapter.notifyDataSetChanged();
        cursor.close();
        database.close();

    }

    public boolean checkFav(String id){
        boolean res=false;
        SQLiteDatabase database=movieHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from "+MovieHelper.TABLENAME+" where "+MovieHelper.ID+"='"+id+"';",null);
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(MovieHelper.ID)).compareToIgnoreCase(id)==0)
                res=true;
        }
        cursor.close();
        database.close();
        return res;
    }
    private class MovieHelper extends SQLiteOpenHelper{

        private static final String DBNAME="moviesdb";
        private static final String TABLENAME="movies";
        private static final int DBVERSION=1;
        private static final String ID="id";
        private static final String TITLE="title";
        private static final String IMAGE="image";
        private static final String RATING="rating";
        private static final String RELEASE="release";
        private static final String SYNOPSIS="synopsis";
        private static final String CREATE_TABLE="CREATE TABLE "+TABLENAME+" ("+ID+" VARCHAR(20),"+TITLE+" VARCHAR(20),"
                                                 +IMAGE+" VARCHAR(20),"+RATING+" VARCHAR(20),"+RELEASE+" VARCHAR(20),"+SYNOPSIS+" TEXT);";
        public MovieHelper(Context context) {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            Log.d("mytag","database created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        }
    }
}
