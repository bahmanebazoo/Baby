package com.example.bazoo.musicplayerhw9;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements SongsFragment.CallBacks {

    TabItem tabItem;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppCompatSeekBar compatSeekBar;
    private Button nextBtn;
    private Button perBtn;
    private Button playBtn;
    private TextView titleSong;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private int songDuration;


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
        tabLayout.setupWithViewPager(viewPager, true);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        SongsFragment songsFragment = SongsFragment.newInstance();
        adapter.addFrag(songsFragment, "Tracks");

        ArtistFragment artistFragment = ArtistFragment.newInstance();
        adapter.addFrag(artistFragment, "Artists");

        AlbumFragment albumFragment = AlbumFragment.newInstance();
        adapter.addFrag(albumFragment, "Albums");
        viewPager.setAdapter(adapter);





    }




    @Override
    public void onClickListener() {
        final SongsFragment songsFragment = (SongsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean play = songsFragment.playOrPause();
                if(play)
                    playBtn.setText(R.string.play);
                else
                    playBtn.setText(R.string.pause);
            }


        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsFragment.nextSong();
                titleSong.setText(songsFragment.titleSong());
            }
        });

        perBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songsFragment.perSong();
                titleSong.setText(songsFragment.titleSong());
            }
        });


    }

    @Override
    public void getTitle(Song song) {
        titleSong.setText(song.getTitle());
        songDuration = song.getDuration() / 60000;
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
