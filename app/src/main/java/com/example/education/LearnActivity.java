package com.example.education;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.education.Class.MusicStatus;
import com.example.education.Fragment.AlphaFragment;

public class LearnActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;

    private MusicStatus musicStatus;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Intent getIntent = getIntent();
        int duration = getIntent.getIntExtra("duration", -1);
        int volume = getIntent.getIntExtra("volume", -1);
        boolean playing = getIntent.getBooleanExtra("playing", false);
        musicStatus = new MusicStatus(duration, volume, playing);
        if (musicStatus.isPlaying()){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background);
            mediaPlayer.setVolume(musicStatus.getVolume(), musicStatus.getVolume());
            mediaPlayer.seekTo(musicStatus.getDuration());
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        fragmentManager = getSupportFragmentManager();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new AlphaFragment()).commit();

    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public int getVolume(){
        return musicStatus.getVolume();
    }

    public boolean getPlaying(){
        return musicStatus.isPlaying();
    }

}