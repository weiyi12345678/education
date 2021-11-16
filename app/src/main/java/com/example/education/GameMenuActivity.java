package com.example.education;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.education.Class.MusicStatus;

public class GameMenuActivity extends AppCompatActivity {
    private Button btnMemoryGame, btnFillBlankGame;
    private CustomDialog customDialog;
    private MusicStatus musicStatus;
    private int musicVolume;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        itemInitialize();
        Intent getStatus = getIntent();
        int duration = getStatus.getIntExtra("duration", -1);
        musicVolume = getStatus.getIntExtra("volume", -1);
        boolean playing = getStatus.getBooleanExtra("playing", false);
        musicStatus = new MusicStatus(duration, musicVolume, playing);

        if (musicStatus.isPlaying()){
            mediaPlayer = MediaPlayer.create(this, R.raw.background);
            mediaPlayer.setVolume(musicStatus.getVolume(), musicStatus.getVolume());
            mediaPlayer.seekTo(musicStatus.getDuration());
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        else {
            mediaPlayer = MediaPlayer.create(this, R.raw.background);
            mediaPlayer.setVolume(musicStatus.getVolume(), musicStatus.getVolume());
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }

        btnMemoryGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.callDifficultyDialog(1, mediaPlayer);
            }
        });

        btnFillBlankGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.callDifficultyDialog(2, mediaPlayer);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        getMenuInflater().inflate(R.menu.toolbar_base, topMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("duration", mediaPlayer.getCurrentPosition());
                intent.putExtra("volune", musicStatus.getVolume());
                intent.putExtra("playing", musicStatus.isPlaying());
                startActivity(intent);
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
                break;
        }
        return true;
    }

    private void itemInitialize(){
        btnMemoryGame = findViewById(R.id.btnMemory);
        btnFillBlankGame = findViewById(R.id.btnMatch);
        customDialog = new CustomDialog(GameMenuActivity.this);
    }

}