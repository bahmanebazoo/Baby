package com.example.bazoo.musicplayerhw9;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.model.Album;
import com.example.bazoo.musicplayerhw9.model.Repository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AlbumFragment extends MediaPlayer {

    private AlbumViewHolder albumViewHolder;
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;

    private List<Album> albums = new ArrayList<>();

    public AlbumFragment() {
        // Required empty public constructor
    }

    public static AlbumFragment newInstance() {

        Bundle args = new Bundle();

        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albums = Repository.getInstance(getActivity()).getAlbum(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View albumView = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = albumView.findViewById(R.id.album_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        albumAdapter = new AlbumAdapter(albums);
        recyclerView.setAdapter(albumAdapter);


        return albumView;
    }

    public void updateUI() {
        albums = Repository.getInstance(getActivity()).getAlbum(getActivity());

        if (albumAdapter == null) {
            albumAdapter = new AlbumAdapter(albums);
            recyclerView.setAdapter(albumAdapter);
        } else {
            albumAdapter.setList(albums);
            albumAdapter.notifyDataSetChanged();
        }
    }


    class AlbumViewHolder extends RecyclerView.ViewHolder {

        private TextView albumTextView;
        private TextView songsCount;
        private TextView albumYear;
        private TextView artistName;
        private AppCompatImageView albumImage;
        private Album album;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumTextView = itemView.findViewById(R.id.album_view_holder_album_name);
            albumYear = itemView.findViewById(R.id.album_view_holder_year_album);
            artistName = itemView.findViewById(R.id.album_view_holder_artist_name);
            songsCount = itemView.findViewById(R.id.album_view_holder_songs_count);
            albumImage = itemView.findViewById(R.id.album_view_holder_album_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(Album album1) {
            String count = "songs count: ";
            String year = "published: ";
            album = album1;
            albumTextView.setText(album1.getTitle()+"( " + album1.getArtistName() + " )");
//            songsCount.setText(count + album1.getSongCount());
            albumImage.setImageBitmap(Repository.getInstance
                    (getActivity()).generateBitmap(getActivity(), album1.getId()));
//            albumYear.setText(year + album1.getYear());
            //artistName.setText("( " + album1.getArtistName() + " )");
        }

    }

    class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

        public AlbumAdapter(List<Album> albumsList) {
            albums = albumsList;
        }

        public void setList(List<Album> albumList) {
            albums = albumList;
        }

        @NonNull
        @Override
        public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.album_view_holder, parent, false);
            albumViewHolder = new AlbumViewHolder(view);
            return albumViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {

            if (getArguments() != null) {
                Album album = albums.get(position);
                holder.bind(album);
            }
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }
    }


}
