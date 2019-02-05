package com.example.bazoo.musicplayerhw9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.bazoo.musicplayerhw9.Utils.Kind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MusicDetailPlayer extends AppCompatActivity {

    public static final String SONG_SENDED = "sended_song";
    public static final String RESOURCE_SENDER = "fragment sender";


    public static Intent newIntent(Context context, Long id, String start) {
        Intent intent = new Intent(context, MusicDetailPlayer.class);
        intent.putExtra(SONG_SENDED, id);
        intent.putExtra(RESOURCE_SENDER, start);
        return intent;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail_player);

        hoandlerMethod();

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_music,
                        MusicDetailPlayerFragment.newInstance(getIntent()
                                .getLongExtra(SONG_SENDED, -1)))
                .commit();*/
    }

    public void hoandlerMethod() {
        String start = getIntent().getStringExtra(RESOURCE_SENDER);

        if (start.equals(Kind.MAIN.toString())) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_music,
                    MusicDetailPlayerFragment.newInstance(getIntent()
                            .getLongExtra(SONG_SENDED, -1)))
                    .commit();
        } else if (start.equals(Kind.ARTIST.toString())) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_music,
                    AlbumFragment.newInstance(start,getIntent()
                            .getLongExtra(SONG_SENDED, -1)))
                    .commit();
        } else if (start.equals(Kind.ALBUM.toString())) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_music,
                    SongsFragment.newInstance(start))
                    .commit();
        }
        else
            Log.d("bahmanLog", "asgharPlayer");

    }

}
