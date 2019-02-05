package com.example.bazoo.musicplayerhw9;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bazoo.musicplayerhw9.Utils.Kind;
import com.example.bazoo.musicplayerhw9.model.Album;
import com.example.bazoo.musicplayerhw9.model.Repository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AlbumFragment extends MediaPlayer {

    public static final String Album_NAME = "artist name";
    public static final String ARTIST_ID = "artist";
    public static final String TITLE_FILTER = "filter";
    public static final String IS_FAVORITE = "favorite";
    private AlbumViewHolder albumViewHolder;
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private Album album = new Album();
    private List<Album> albums = new ArrayList<>();


    public AlbumFragment() {
        // Required empty public constructor
    }

    public static AlbumFragment newInstance(String kind) {

        Bundle args = new Bundle();
        args.putString(KIND_OF_LIST, kind);
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static AlbumFragment newInstance(String kind, Long artistID) {

        Bundle args = new Bundle();
        args.putLong(ARTIST_ID, artistID);
        args.putString(KIND_OF_LIST, kind);
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AlbumFragment newInstance(String kind, String s) {

        Bundle args = new Bundle();
        args.putString(TITLE_FILTER, s);
        args.putString(KIND_OF_LIST, kind);
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AlbumFragment newInstance(String kind, boolean b) {

        Bundle args = new Bundle();
        args.putBoolean(IS_FAVORITE, b);
        args.putString(KIND_OF_LIST, kind);
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albums = getAlbumList();
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        albumAdapter = new AlbumAdapter(albums);
        recyclerView.setAdapter(albumAdapter);


        return albumView;
    }

    public void updateUI() {
        albums = getAlbumList();

        if (albumAdapter == null) {
            albumAdapter = new AlbumAdapter(albums);
            recyclerView.setAdapter(albumAdapter);
        } else {
            albumAdapter.setList(albums);
            albumAdapter.notifyDataSetChanged();
        }
    }


    public List<Album> getAlbumList() {
        String kind = getArguments().getString(KIND_OF_LIST);
        if (kind.equals(Kind.MAIN.toString()))
            albums = Repository.getInstance(getActivity()).getAllAlbum();
        else if (kind.equals(Kind.ARTIST.toString()))
            albums = Repository.getInstance(getActivity()).getSpecificAlbum(getArguments().getLong(ARTIST_ID));
        else if (kind.equals(Kind.FAVORITE.toString()))
            albums = Repository.getInstance(getActivity()).getAllAlbum();
        else if (kind.equals(Kind.SEARCH.toString()))
            albums = Repository.getInstance(getActivity()).getFilteredAlbum(getArguments().getString(TITLE_FILTER));
        else if (kind.equals(Kind.PLAYLIST.toString()))
            albums = Repository.getInstance(getActivity()).getAllAlbum();



        return albums;
    }


    class AlbumViewHolder extends RecyclerView.ViewHolder {

        private TextView albumTextView;
        private TextView songsCount;
        private TextView albumYear;
        private TextView artistName;
        private AppCompatImageView albumImage;


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
            albumTextView.setText(album1.getTitle() + "( " + album1.getArtistName() + " )");
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
