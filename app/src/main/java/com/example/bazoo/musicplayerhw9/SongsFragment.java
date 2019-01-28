package com.example.bazoo.musicplayerhw9;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 */
public class SongsFragment extends androidx.fragment.app.Fragment {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private LinearLayout linearLayout;
    private List<Song> songs = new ArrayList<>();
    private SongViewHolder songViewHolder;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private Song song;
    private CallBacks callBacks;

    public SongsFragment() {
        // Required empty public constructor
    }

    public static SongsFragment newInstance() {

        Bundle args = new Bundle();

        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View songView = inflater.inflate(R.layout.fragment_songs, container, false);
        songs = Repository.getInstance(getActivity()).getMusic(getActivity());
        recyclerView = songView.findViewById(R.id.song_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        songAdapter = new SongAdapter(songs);
        recyclerView.setAdapter(songAdapter);



        return songView;
    }


    public void playOrPause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    public void nextSong() {
        Song my_song = songViewHolder.song;
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).equals(my_song)) {
                songViewHolder.song = songs.get(i + 1);
                try {
                    songViewHolder.playSong();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public void perSong() {
        Song my_song = songViewHolder.song;
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).equals(my_song)) {
                my_song = songs.get(i - 1);
                try {
                    songViewHolder.playSong();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public String titleSong(){
        return songViewHolder.song.getTitle();
    }


    public void shafieldNumber(){
        Random r = new Random();
        int a = r.nextInt(0-songs.size());
        songViewHolder.song = songs.get(a);
        try {
            songViewHolder.playSong();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void updateUI() {
        songs = Repository.getInstance(getActivity()).getMusic(getActivity());

        if (songAdapter == null) {
            songAdapter = new SongAdapter(songs);
            recyclerView.setAdapter(songAdapter);
        } else {
            songAdapter.setList(songs);
            songAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CallBacks){
            callBacks = (CallBacks) context;
        callBacks.onClickListener();
        }
        else
            throw new RuntimeException("Activity not impl CallBacks");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBacks = null;
    }


    public interface CallBacks {
        void onClickListener();
        void getTitle(Song song);
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        private TextView songTextView;
        private TextView timeTextView;
        private Song song;


        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(R.id.song_title);
            timeTextView = itemView.findViewById(R.id.song_time);
            linearLayout = itemView.findViewById(R.id.buttons);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        playSong();
                        callBacks.getTitle(song);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });


        }


        private void playSong() throws IOException {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

            mediaPlayer.setDataSource(getActivity(), song.uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            linearLayout.setVisibility(View.VISIBLE);
        }

        private void bind(Song song) {
            this.song = song;
            songTextView.setText(song.getTitle());
            timeTextView.setText("" + song.getDuration());


        }


    }

    class SongAdapter extends RecyclerView.Adapter<SongViewHolder> {

        private List<Song> songs;

        public SongAdapter(List<Song> songs) {

            this.songs = songs;
        }

        public void setList(List<Song> songs) {
            this.songs = songs;
        }

        @NonNull
        @Override
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.song_view_holder, parent, false);
            songViewHolder = new SongViewHolder(view);
            return songViewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
            if (getArguments() != null) {
                Song song = songs.get(position);
                holder.bind(song);

            }
        }


        @Override
        public int getItemCount() {
            return songs.size();
        }
    }
}
