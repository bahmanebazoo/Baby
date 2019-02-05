package com.example.bazoo.musicplayerhw9.model;

import java.util.Random;

public class Album {

    public final String artistName;
    public final long id;
    public final long artist_ID;
    public final String title;

    public Album() {
        this.id = -1;
        Random r = new Random();
        this.artist_ID=r.nextLong() ;
        this.title = "";
        this.artistName = "";

    }

    public Album(long _id, String _title, String _artistName,Long artist_ID) {
        this.id = _id;
        this.title = _title;
        this.artistName = _artistName;
        this.artist_ID = artist_ID;

    }


    public String getArtistName() {
        return artistName;
    }

    public long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public long getArtist_ID() {
        return artist_ID;
    }
}
