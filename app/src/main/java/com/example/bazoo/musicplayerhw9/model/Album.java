package com.example.bazoo.musicplayerhw9.model;

public class Album {

    public final String artistName;
    public final long id;

    public final String title;

    public Album() {
        this.id = -1;
        this.title = "";
        this.artistName = "";

    }

    public Album(long _id, String _title, String _artistName) {
        this.id = _id;
        this.title = _title;
        this.artistName = _artistName;

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


}
