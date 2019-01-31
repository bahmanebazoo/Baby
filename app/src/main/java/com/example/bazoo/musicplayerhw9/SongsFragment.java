package com.example.bazoo.musicplayerhw9;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.model.Repository;
import com.example.bazoo.musicplayerhw9.model.Song;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 */
public class SongsFragment extends MediaPlayer {

    private SongViewHolder songViewHolder;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;



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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CallBacks) {
            callBacks = (CallBacks) context;
            callBacks.onClickListener(song);
        } else
            throw new RuntimeException("Activity not impl CallBacks");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBacks = null;
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


    public void updateUI() {
        super.songs = Repository.getInstance(getActivity()).getMusic(getActivity());

        if (songAdapter == null) {
            songAdapter = new SongAdapter(songs);
            recyclerView.setAdapter(songAdapter);
        } else {
            songAdapter.setList(super.songs);
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
                    for (int i =0; i<songs.size();i++) {
                        if (songs.get(i).equals(song)) {
                            Log.d("position number",i+"" );
                        }
                    }
                    playSong(getActivity(),song);
                    callBacks.getSongDetails(song);
                   callBacks.onClickListener(song);
                }
            });
        }

        private void bind(Song song1) {
            song = song1;
            songTextView.setText(song.getTitle());
            timeTextView.setText(takeDurationToMinute(song.getDuration()));
            artistTextView.setText(song.getArtistName());
        }
    }

    class SongAdapter extends RecyclerView.Adapter<SongViewHolder> {

        public SongAdapter(List<Song> songs1) {
            songs = songs1;
        }

        public void setList(List<Song> songs1) {
            songs = songs1;
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
            Log.d("ssongs size: ",songs.size()+"");
            return songs.size();
        }
    }
}
