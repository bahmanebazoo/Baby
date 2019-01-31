package com.example.bazoo.musicplayerhw9.model;

import android.net.Uri;

public class Song {

    public final long id;
    public final String title;
    public final int duration;
    public final int trackNumber;
    public final Uri uri;
    public final long albumId;
    public final String albumName;
    public final long artistId;
    public final String artistName;

    public Song() {
        this.id = -1;
        this.albumId = -1;
        this.artistId = -1;
        this.title = "";
        this.artistName = "";
        this.albumName = "";
        this.duration = -1;
        this.trackNumber = -1;
        this.uri = null;
    }


    public Song(long _id, long _albumId, long _artistId, String _title, String _artistName, String _albumName, int _duration, int _trackNumber, Uri uri) {
        this.id = _id;
        this.albumId = _albumId;
        this.artistId = _artistId;
        this.title = _title;
        this.artistName = _artistName;
        this.albumName = _albumName;
        this.duration = _duration;
        this.trackNumber = _trackNumber;
        this.uri = uri;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

}
