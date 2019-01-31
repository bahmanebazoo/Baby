package com.example.bazoo.musicplayerhw9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MusicDetailPlayer extends AppCompatActivity {

    public static final String SONG_SENDED = "sended_song";


    public static Intent newIntent(Context context, Long id) {
        Intent intent = new Intent(context, MusicDetailPlayer.class);
        intent.putExtra(SONG_SENDED, id);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail_player);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_music,
                        MusicDetailPlayerFragment.newInstance(getIntent()
                                .getLongExtra(SONG_SENDED, -1)))
                .commit();
    }

}
