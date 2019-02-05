package com.example.bazoo.musicplayerhw9;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.Utils.Kind;
import com.example.bazoo.musicplayerhw9.model.Album;
import com.example.bazoo.musicplayerhw9.model.Artist;
import com.example.bazoo.musicplayerhw9.model.Repository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ArtistFragment extends MediaPlayer {

    private ArtistViewHolder artistViewHolder;
    private ArtistAdapter artistAdapter;
    private RecyclerView recyclerView;
    private Artist artist = new Artist();
    private List<Artist> artists = new ArrayList<>();
    public ArtistFragment() {
        // Required empty public constructor
    }

    public static ArtistFragment newInstance(String kind) {

        Bundle args = new Bundle();
        args.putString(KIND_OF_LIST, kind);
        ArtistFragment fragment = new ArtistFragment();
        fragment.setArguments(args);
        return fragment;
    }






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artists = Repository.getInstance(getActivity()).getArtist(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        recyclerView = view.findViewById(R.id.fragment_artist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        artistAdapter = new ArtistAdapter(artists);
        recyclerView.setAdapter(artistAdapter);


        return view;

    }

    public void updateUI() {
        artists = Repository.getInstance(getActivity()).getArtist(getActivity());

        if (artistAdapter == null) {
            artistAdapter = new ArtistAdapter(artists);
            recyclerView.setAdapter(artistAdapter);
        } else {
            artistAdapter.setList(artists);
            artistAdapter.notifyDataSetChanged();
        }
    }



    class ArtistViewHolder extends RecyclerView.ViewHolder {

        Artist artist;
        private TextView artistName;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_view_holder_artist_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = MusicDetailPlayer.newIntent(getActivity(), artist.getId(), Kind.ARTIST.toString());
                    startActivity(intent);
                }
            });
        }


        public void bind(Artist artist1) {
            artist = artist1;
            artistName.setText(artist1.getName());

        }
    }

    class ArtistAdapter extends RecyclerView.Adapter<ArtistViewHolder> {

        public ArtistAdapter(List<Artist> artistList) {
            artists = artistList;
        }

        public void setList(List<Artist> artistList) {
            artists = artistList;
        }

        @NonNull
        @Override
        public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.artist_view_holder, parent, false);
            artistViewHolder = new ArtistViewHolder(view);
            return artistViewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
            if (getArguments() != null) {
                Artist artist = artists.get(position);
                holder.bind(artist);
            }
        }

        @Override
        public int getItemCount() {
            return artists.size();
        }
    }

}
