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

import com.example.bazoo.musicplayerhw9.model.Song;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabItem;
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

public class MainActivity extends AppCompatActivity implements SongsFragment.CallBacks {

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
    private Long song_ID;


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


        final SongsFragment songsFragment = SongsFragment.newInstance();
        adapter.addFrag(songsFragment, "Tracks");

        ArtistFragment artistFragment = ArtistFragment.newInstance();
        adapter.addFrag(artistFragment, "Artists");

        AlbumFragment albumFragment = AlbumFragment.newInstance();
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

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "open detail", Toast.LENGTH_SHORT).show();
                if(song_ID!=null){
                Intent intent = MusicDetailPlayer.newIntent(getApplicationContext(),song_ID);
                startActivity(intent);}
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
//            Log.d("delay", i+"");
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
        song_ID = song.getId();
        songDuration = song.getDuration() / 60000;

    }

    public void hoandlerMethod() {


    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        /**
         * Return the number of views available.
         */
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
