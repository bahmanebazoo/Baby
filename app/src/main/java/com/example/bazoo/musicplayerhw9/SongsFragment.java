package com.example.bazoo.musicplayerhw9;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.model.Song;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 */
public class SongsFragment extends androidx.fragment.app.Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private Song song;

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
        recyclerView = songView.findViewById(R.id.song_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        songAdapter = new SongAdapter(Repository.getInstance(getActivity()).getMusic(getActivity()));
        recyclerView.setAdapter(songAdapter);


        return songView;
    }


    public void updateUI() {
        List<Song> songList = Repository.getInstance(getActivity()).getMusic(getActivity());


        if (songAdapter == null) {
            songAdapter = new SongAdapter(songList);
            recyclerView.setAdapter(songAdapter);
        } else {
            songAdapter.setList(songList);
            songAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        private TextView songTextView;
        private TextView timeTextView;
        private ImageButton playImageButton;
        private Song song;


        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(R.id.song_title);
            timeTextView = itemView.findViewById(R.id.song_time);
            playImageButton = itemView.findViewById(R.id.play_pause_button);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

        private void bind(Song song) {
            this.song = song;
            songTextView.setText(song.getTitle());
            timeTextView.setText(song.getDuration());


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
            return new SongViewHolder(view);
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
