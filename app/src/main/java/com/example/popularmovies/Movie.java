package com.example.popularmovies;

/**
 * Created by dilpreet on 23/2/16.
 */
public class Movie {
    private String title,imageLink,synopsis,rating,release;

    public Movie(String title,String imageLink,String synopsis,String rating,String release) {
        super();
        this.title=title;
        this.imageLink=imageLink;
        this.synopsis=synopsis;
        this.rating=rating;
        this.release=release;
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
}
