package com.example.bazoo.musicplayerhw9;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.Utils.Kind;
import com.example.bazoo.musicplayerhw9.model.Repository;
import com.example.bazoo.musicplayerhw9.model.Song;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 */
public class SongsFragment extends MediaPlayer {

    public static final String ALBUM_ID = "album";
    public static final String TITLE_FILTER = "filter";
    public static final String IS_FAVORITE = "favorite";
    public static final String PLAYLIST_ID = "playlist id";
    public SongViewHolder songViewHolder;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private Song song=new Song();
//    private List<Song> songs = new ArrayList<>();

    public SongsFragment() {
        // Required empty public constructor
    }


    public static SongsFragment newInstance(String kind) {

        Bundle args = new Bundle();
        args.putString(KIND_OF_LIST, kind);
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static SongsFragment newInstance(String kind, Long albumID) {

        Bundle args = new Bundle();
        args.putLong(ALBUM_ID, albumID);
        args.putString(KIND_OF_LIST, kind);
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SongsFragment newInstance(String kind, String s) {

        Bundle args = new Bundle();
        args.putString(TITLE_FILTER, s);
        args.putString(KIND_OF_LIST, kind);
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SongsFragment newInstance(String kind, boolean b) {

        Bundle args = new Bundle();
        args.putBoolean(IS_FAVORITE, b);
        args.putString(KIND_OF_LIST, kind);
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SongsFragment newInstance(String kind, int playListID) {

        Bundle args = new Bundle();
        args.putInt(PLAYLIST_ID, playListID);
        args.putString(KIND_OF_LIST, kind);
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SongCallBacks) {
            songCallBacks = (SongCallBacks) context;
            songCallBacks.onClickListener(song);
        } else
            throw new RuntimeException("Activity not impl SongCallBacks");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        songCallBacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSongList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View songView = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = songView.findViewById(R.id.song_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        songAdapter = new SongAdapter(songs);
        recyclerView.setAdapter(songAdapter);

        return songView;
    }


    public void updateUI() {
        super.songs = getSongList();

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

    public List<Song> getSongList() {
        String kind = getArguments().getString(KIND_OF_LIST);
        if (kind == Kind.MAIN.toString())
            songs = Repository.getInstance(getActivity()).getAllMusic();
        else if (kind == Kind.ALBUM.toString())
            songs = Repository.getInstance(getActivity()).getSpecificSongs(getArguments().getLong(ALBUM_ID));
        else if (kind == Kind.FAVORITE.toString())
            songs = Repository.getInstance(getActivity()).getAllMusic();
        else if (kind == Kind.SEARCH.toString())
            songs = Repository.getInstance(getActivity()).getFilteredSongs(getArguments().getString(TITLE_FILTER));
        else if (kind == Kind.PLAYLIST.toString())
            songs = Repository.getInstance(getActivity()).getAllMusic();
        else
            songs = null;

            return songs;
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        private TextView songTextView;
        private TextView timeTextView;
        private TextView artistTextView;
        private AppCompatImageView appCompatImageView;
        public Song song;


        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(R.id.song_title);
            timeTextView = itemView.findViewById(R.id.song_time);
            artistTextView = itemView.findViewById(R.id.song_artist);
            appCompatImageView = itemView.findViewById(R.id.song_view_holder_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < songs.size(); i++) {
                        if (songs.get(i).equals(song)) {
                            Log.d("position number", i + "");
                        }
                    }
                    playSong(getActivity(), song);
                    songCallBacks.getSongDetails(song);
                    songCallBacks.onClickListener(song);
                }
            });
        }

        private void bind(Song song1) {
            song = song1;
            songTextView.setText(song.getTitle());
            timeTextView.setText(takeDurationToMinute(song.getDuration()));
            artistTextView.setText(song.getArtistName());
            appCompatImageView.setImageBitmap(Repository
                    .getInstance(getActivity()).generateBitmap(getActivity(), song.getAlbumId()));
        }
    }

    class SongAdapter extends RecyclerView.Adapter<SongViewHolder> {

        private SongAdapter(List<Song> songs1) {
            songs = songs1;
        }

        private void setList(List<Song> songs1) {
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
            Log.d("songs size: ", songs.size() + "");
            return songs.size();
        }
    }
}
