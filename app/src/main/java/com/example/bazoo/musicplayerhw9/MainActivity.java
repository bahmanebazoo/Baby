package com.example.bazoo.musicplayerhw9;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    TabItem tabItem;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collapsingToolbarLayout = findViewById(R.id.collapsing_container);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.fragment_container);
        tabLayout.setupWithViewPager(viewPager, true);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        SongsFragment songsFragment = SongsFragment.newInstance();
        adapter.addFrag(songsFragment, "Tracks");

        ArtistFragment artistFragment = ArtistFragment.newInstance();
        adapter.addFrag(artistFragment,"Artists");

        AlbumFragment albumFragment=AlbumFragment.newInstance();
        adapter.addFrag(albumFragment,"Albums");
        viewPager.setAdapter(adapter);


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
