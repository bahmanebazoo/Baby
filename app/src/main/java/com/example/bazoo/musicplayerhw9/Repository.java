package com.example.bazoo.musicplayerhw9;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.bazoo.musicplayerhw9.model.Album;
import com.example.bazoo.musicplayerhw9.model.Artist;
import com.example.bazoo.musicplayerhw9.model.Song;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static Repository instance;
    private Context context;
    private Song song;
    private Artist artist;
    private Album album;
    private List<Song> songList = new ArrayList<>();


    public Repository(Context context) {
        this.context = context;
    }

    public static Repository getInstance(Context context) {
        if (instance == null) {
            instance = new Repository(context);
        }
        return instance;
    }

    public List<Song> getMusic(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri,
                null, null,
                null, null);
        try {

            if (songCursor == null)
                return songList;

            songCursor.moveToFirst();
            do {

                int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int albumID = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int artistID = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
                int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int duration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                int trackNumber = songCursor.getColumnIndex(MediaStore.Audio.Media.TRACK);


                Long currentTitleID = Long.parseLong(songCursor.getString(songID));
                String currentTitle = songCursor.getString(songTitle);
                Long currentArtistID = Long.parseLong(songCursor.getString(artistID));
                String currentArtist = songCursor.getString(songArtist);
                Long currentAlbumID = Long.parseLong(songCursor.getString(albumID));
                String currentAlbum = songCursor.getString(songAlbum);
                int currentDuration = Integer.parseInt(songCursor.getString(duration));
                int currentTrackNumber = trackNumber;
                song = new Song(currentTitleID, currentAlbumID, currentArtistID, currentTitle, currentArtist, currentAlbum, currentDuration, currentTrackNumber);
                songList.add(song);
                Log.i("TAG", "getMusic: " + songList.size());
            } while (songCursor.moveToNext());

            return songList;
        } finally {
            songCursor.close();
        }
    }
}
