package com.example.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dilpreet on 23/2/16.
 */
public class Movie implements Parcelable{
    private String title;
    private String imageLink;
    private String synopsis;
    private String rating;
    private String release;

    private String id;

    public Movie(String id,String title,String imageLink,String synopsis,String rating,String release) {
        super();
        this.id=id;
        this.title=title;
        this.imageLink=imageLink;
        this.synopsis=synopsis;
        this.rating=rating;
        this.release=release;
    }

    private Movie(Parcel in){
        id=in.readString();
        title=in.readString();
        imageLink=in.readString();
        synopsis=in.readString();
        rating=in.readString();
        release=in.readString();
    }
    public String getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getRating() {
        return rating;
    }

    public String getRelease() {
        return release;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(imageLink);
        dest.writeString(synopsis);
        dest.writeString(rating);
        dest.writeString(release);
    }

    public static Parcelable.Creator<Movie> CREATOR=new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
