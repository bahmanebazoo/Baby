package com.example.bazoo.musicplayerhw9;



import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.model.Repository;
import com.example.bazoo.musicplayerhw9.model.Song;
import com.google.android.material.internal.CheckableImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;


public class MusicDetailPlayerFragment extends MediaPlayer {

    //I love OOP

    public static final String SONG_PLAYED_ID = "song_played_ID";

    private CheckableImageButton shufeld;
    private CheckableImageButton reapeat;
    private Button per;
    private Button play;
    private Button next;
    private AppCompatSeekBar seekbar;
    private TextView titleMusic;
    private TextView artist;
    private AppCompatImageView backgroundImage;
    private AppCompatImageView songImage;
    private FrameLayout darkLayer;
    private Song song;
    private Bitmap bitmap;


    public MusicDetailPlayerFragment() {
        // Required empty public constructor
    }

    public static MusicDetailPlayerFragment newInstance(Long id) {

        Bundle args = new Bundle();
        args.putLong(SONG_PLAYED_ID, id);
        MusicDetailPlayerFragment fragment = new MusicDetailPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        findSong(getArguments().getLong(SONG_PLAYED_ID));
        Log.d("attach_fragment", "fragment Attached");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        song=null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap = Repository.getInstance(getActivity())
                .blur(getActivity(), Repository.getInstance(getActivity())
                        .generateBitmap(getActivity(),song.albumId));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View musicView = inflater.inflate(R.layout.fragment_music_detail_player, container, false);
        shufeld = musicView.findViewById(R.id.shufeild_music);
        reapeat = musicView.findViewById(R.id.repeat_music);
        per = musicView.findViewById(R.id.per_music);
        play = musicView.findViewById(R.id.play_music);
        next = musicView.findViewById(R.id.next_music);
        seekbar = musicView.findViewById(R.id.seekbar_music);
        titleMusic = musicView.findViewById(R.id.title_fragment_music);
        artist = musicView.findViewById(R.id.artist_music);
        backgroundImage = musicView.findViewById(R.id.image_background_music);
        songImage = musicView.findViewById(R.id.image_song_music);
        titleMusic.setText(song.getTitle());
        artist.setText(song.getArtistName());
        backgroundImage.setImageBitmap(bitmap);
        songImage.setImageBitmap(Repository.getInstance(getActivity())
                .generateBitmap(getActivity(),song.albumId));


        return musicView;
    }


    private void findSong(Long id) {


        for (int i = 0; i < MediaPlayer.songs.size(); i++) {
            if (songs.get(i).getId() == id) {
                song = songs.get(i);
            }
        }
    }

}
