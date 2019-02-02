package com.example.bazoo.musicplayerhw9.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static Repository instance;
    private Context context;
    private Song song;
    private Artist artist;
    private Album album;
    private List<Song> songList = new ArrayList<>();
    private List<Album> albumList = new ArrayList<>();
    private List<Artist>artistList = new ArrayList<>();

    public Repository(Context context) {
        this.context = context;
    }

    public static Repository getInstance(Context context) {
        if (instance == null) {
            instance = new Repository(context);
        }
        return instance;
    }

    public static Bitmap generateBitmap(Activity activity, Long albumId) {

        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

        Bitmap bitmap = null;
        try {

            bitmap = MediaStore.Images.Media.getBitmap(
                    activity.getContentResolver(), albumArtUri);
            /*if (bitmap != null)
                bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);*/

        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return bitmap;
    }




    public static Bitmap blur(Context context, Bitmap image) {

        float BITMAP_SCALE = 0.4f;
        float BLUR_RADIUS = 7.5f;

        int mWidth = Math.round(image.getWidth() * BITMAP_SCALE);
        int mHeight = Math.round(image.getHeight() * BITMAP_SCALE);
        Bitmap givenBitmap = Bitmap.createScaledBitmap(image, mWidth, mHeight, false);
        Bitmap takenBitmap = Bitmap.createBitmap(givenBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, givenBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, takenBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(takenBitmap);
        return takenBitmap;
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
                int srcUri = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                Long currentTitleID = Long.parseLong(songCursor.getString(songID));
                String currentTitle = songCursor.getString(songTitle);
                Long currentArtistID = Long.parseLong(songCursor.getString(artistID));
                String currentArtist = songCursor.getString(songArtist);
                Long currentAlbumID = Long.parseLong(songCursor.getString(albumID));
                String currentAlbum = songCursor.getString(songAlbum);
                int currentDuration = songCursor.getInt(duration);
                int currentTrackNumber = trackNumber;
                Uri currentUri = Uri.parse(songCursor.getString(srcUri));
                song = new Song(currentTitleID, currentAlbumID, currentArtistID, currentTitle, currentArtist, currentAlbum, currentDuration, currentTrackNumber, currentUri);
                songList.add(song);
                Log.i("TAG", "getMusic: " + songList.size());
            } while (songCursor.moveToNext());

            return songList;
        } finally {
            songCursor.close();
        }
    }

    public List<Album> getAlbum(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String whereClause = MediaStore.Audio.Media.ALBUM;
        Cursor songCursor = contentResolver.query(songUri,
                null, null,
                null, null);
        try {

            if (songCursor == null)
                return albumList;

            songCursor.moveToFirst();
            do {
                int albumID = songCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
                int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
                int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
                int songCount = songCursor.getColumnIndex(MediaStore.Audio.Albums._COUNT);



                String currentArtist = songCursor.getString(songArtist);
                Long currentAlbumID = Long.parseLong(songCursor.getString(albumID));
                String currentAlbum = songCursor.getString(songAlbum);
                album = new Album(currentAlbumID, currentAlbum, currentArtist);
                if (albumList.size() == 0)
                    albumList.add(album);
                else{
                    for (int i = 0; i < albumList.size() ; i++) {
                        if (!(albumList.get(i)).equals(album)) {
                            albumList.add(album);
                            Log.i("TAG", "getAlbum: " + albumList.get(albumList.size() - 1));
                            break;
                        }
                    }
                }
            } while (songCursor.moveToNext());

            Log.i("TAG", "getAlbum: " + albumList.size());
            return albumList;

        } finally {
            songCursor.close();
        }
    }

    public List<Artist> getArtist(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        String whereClause = MediaStore.Audio.Media.ARTIST;
        Cursor songCursor = contentResolver.query(songUri,
                null, null,
                null, null);
        try {

            if (songCursor == null)
                return artistList;

            songCursor.moveToFirst();
            do {
                int artistID = songCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
                int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
                int songCount = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
                int albumCount = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);

                String currentArtist = songCursor.getString(songArtist);
                Long currentArtistID = Long.parseLong(songCursor.getString(artistID));
                int numberOfTracks = songCursor.getInt(songCount);
                int numberOfAlbums = songCursor.getInt(albumCount);
                artist = new Artist(currentArtistID,  currentArtist,numberOfTracks,numberOfAlbums);
                Log.i("TAG", "getArtistName: " + artist.getName());

                if (artistList.size() == 0)
                    artistList.add(artist);
                else {
                    for (int i = 0; i < artistList.size(); i++) {
                        if (!(artistList.get(i)).equals(artist)) {
                            artistList.add(artist);
                            Log.i("TAG", "getArtist: " + artistList.get(artistList.size() - 1));
                            break;
                        }
                    }
                }
            } while (songCursor.moveToNext());

            Log.i("TAG", "getAlbum: " + artistList.size());
            return artistList;

        } finally {
            songCursor.close();
        }
    }

}
