package com.example.bazoo.musicplayerhw9;

import android.content.Context;
import android.util.Log;

import com.example.bazoo.musicplayerhw9.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MediaPlayer extends Fragment {

    public static final int hourPerMS = 3600000;
    public static final int minPerMS = 60000;
    public static final int secPerMS = 1000;
    public static Song song = new Song();
    public static CallBacks callBacks;
    public static List<Song> songs = new ArrayList<>();

    public static android.media.MediaPlayer mediaPlayer = new android.media.MediaPlayer();


    public static void playSong(Context context, Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        try {
            mediaPlayer.setDataSource(context, song.uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("song Title", song.getTitle() + "");
        Log.d("song Title", "bahman:  " + song.getTitle() + "");
    }

    public static android.media.MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }




    public boolean playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return true;
        } else {
            mediaPlayer.start();
            return false;
        }
    }

    public void nextSong(Context context, Song song) {
        Song my_song = song;
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).equals(my_song)) {
                song = songs.get(i + 1);
                try {
                    playSong(context, song);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public void perSong(Context context, Song song) {
        Song my_song = song;
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).equals(my_song)) {
                my_song = songs.get(i - 1);
                try {
                    playSong(context, my_song);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public void shafieldNumber(Context context) {
        Random r = new Random();
        int a = r.nextInt(0 - songs.size());
        song = songs.get(a);
        try {
            playSong(context, song);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void repeateAll(Context context, Song song) {
        if (getSongPosition() == songs.size()) {
            song = songs.get(0);
            try {
                playSong(context, song);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            nextSong(context, song);
    }

    public int getSongPosition() {
        for (int i = 0; i < songs.size(); i++) {
            if (song == songs.get(i))
                return i;
        }
        return -1;
    }


    public String titleSong() {
        return song.getTitle();
    }

    public String takeDurationToMinute(int duration) {
        String s = "";
        int dur = duration;
        int dph = dur / hourPerMS;

        if (dph > 0) {
            s += dph;
            dur -= (dph * hourPerMS);
        }
        int dpm = dur / minPerMS;

        if ((dpm) >= 0) {
            if (dph > 0)
                s += " : ";
            s += dpm;
            dur -= (dpm * minPerMS);
        }
        int dps = dur / secPerMS;
        if (dps > 0) {
            s += " : " + dps;
        }
        return s;
    }


    public int currentPosition(){
        return mediaPlayer.getCurrentPosition();

    }


    public interface CallBacks {
        void onClickListener(Song song);
        void getSongDetails(Song song);
    }


}
