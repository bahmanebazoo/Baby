package com.example.bazoo.musicplayerhw9;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.model.Song;

import java.text.SimpleDateFormat;
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

    private final int hourPerMS = 3600000;
    private final int minPerMS = 60000;
    private final int secPerMS = 1000;
    SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private MediaPlayer mediaPlayer = new MediaPlayer();
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


    public boolean playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return true;
        } else {
            mediaPlayer.start();
            return false;
        }
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


    public void shafieldNumber() {
        Random r = new Random();
        int a = r.nextInt(0 - songs.size());
        songViewHolder.song = songs.get(a);
        try {
            songViewHolder.playSong();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void repeateAll() {
        if (getSongPosition() == songs.size()) {
            song = songs.get(0);
            try {
                songViewHolder.playSong();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            nextSong();
    }

    public int getSongPosition() {
        for (int i = 0; i < songs.size(); i++) {
            if (song == songs.get(i))
                return i;
        }
        return -1;
    }


    public String titleSong() {
        return songViewHolder.song.getTitle();
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
        if (context instanceof CallBacks) {
            callBacks = (CallBacks) context;
            callBacks.onClickListener();
        } else
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
        private TextView artistTextView;
        private Song song;


        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(R.id.song_title);
            timeTextView = itemView.findViewById(R.id.song_time);
            artistTextView = itemView.findViewById(R.id.song_artist);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSong();
                    callBacks.getTitle(song);


                }
            });


        }


        private void playSong() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

            try {
                mediaPlayer.setDataSource(getActivity(), song.uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void bind(Song song) {
            this.song = song;
            songTextView.setText(song.getTitle());
            timeTextView.setText(takeDurationToMinute(song.getDuration()));
            artistTextView.setText(song.getArtistName());


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
