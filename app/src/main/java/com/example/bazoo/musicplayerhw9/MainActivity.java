package com.example.bazoo.musicplayerhw9;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bazoo.musicplayerhw9.Utils.Kind;
import com.example.bazoo.musicplayerhw9.model.Album;
import com.example.bazoo.musicplayerhw9.model.Artist;
import com.example.bazoo.musicplayerhw9.model.Song;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements MediaPlayer.SongCallBacks {

    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppCompatSeekBar compatSeekBar;
    private ConstraintLayout constraintLayout;
    private SongsFragment songsFragment = new SongsFragment();
    private Button nextBtn;
    private Button perBtn;
    private Button playBtn;
    private TextView titleSong;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private int songDuration;
    private Long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collapsingToolbarLayout = findViewById(R.id.collapsing_container);
        compatSeekBar = findViewById(R.id.seek_bar);
        nextBtn = findViewById(R.id.next);
        perBtn = findViewById(R.id.per);
        playBtn = findViewById(R.id.play_pause);
        titleSong = findViewById(R.id.title_song);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.fragment_container);
        constraintLayout = findViewById(R.id.collapse_and_tabs_container);
        tabLayout.setupWithViewPager(viewPager, true);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


      songsFragment = SongsFragment.newInstance(Kind.MAIN.toString());
        adapter.addFrag(songsFragment, "Tracks");

        ArtistFragment artistFragment = ArtistFragment.newInstance(Kind.MAIN.toString());
        adapter.addFrag(artistFragment, "Artists");

        AlbumFragment albumFragment = AlbumFragment.newInstance(Kind.MAIN.toString());
        adapter.addFrag(albumFragment, "Albums");
        viewPager.setAdapter(adapter);


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean play = songsFragment.playOrPause();

                if (play)
                    playBtn.setText(R.string.play);
                else
                    playBtn.setText(R.string.pause);

            }
        });

        perBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"bahmanper",Toast.LENGTH_SHORT).show();
                songsFragment.songViewHolder.song =
                        songsFragment.perSong(getApplicationContext(),songsFragment.songViewHolder.song);

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsFragment.songViewHolder.song =
                        songsFragment.nextSong(getApplicationContext(),songsFragment.songViewHolder.song);
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "open detail", Toast.LENGTH_SHORT).show();
                if (id != null) {
                    Intent intent = MusicDetailPlayer.newIntent(getApplicationContext(), id, Kind.MAIN.toString());
                    startActivity(intent);
                }
            }


        });


    }


    @Override
    public void onClickListener(Song song) {
        playBtn.setText(R.string.pause);
        int time = song.getDuration();
        compatSeekBar.setMax(time);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            compatSeekBar.setMin(0);

        Handler handler = new Handler();

        for (int i = 0; i < songsFragment.currentPosition(); i++) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    compatSeekBar.setProgress(songsFragment.currentPosition());
                    Log.d("delay", songsFragment.currentPosition() + "");
                }
            }, 1000);
        }

    }

    @Override
    public void getSongDetails(Song song) {
        titleSong.setText(song.getTitle());
        id = song.getId();

        songDuration = song.getDuration() / 60000;

    }








class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);

    }
}

}
