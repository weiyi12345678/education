package com.example.education;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.education.Class.MusicStatus;


public class MainActivity extends AppCompatActivity {
    Button btnLearn, btnPlay, btnQuit, btnRank, btnSetting;
    private CustomDialog customDialog;
    private MediaPlayer mediaPlayer;
    private MusicStatus musicStatus;
    private int musicVolume = 0;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getStatus = getIntent();
        int duration = getStatus.getIntExtra("duration", -1);
        musicVolume = getStatus.getIntExtra("volume", -1);
        boolean isPlaying = getStatus.getBooleanExtra("playing", false);
        musicStatus = new MusicStatus(duration, musicVolume, isPlaying);

        if (!musicStatus.isPlaying()){
            mediaPlayer = MediaPlayer.create(this, R.raw.background);
            mediaPlayer.start();
            musicVolume = 50;
            mediaPlayer.setVolume(musicVolume, musicVolume);
            mediaPlayer.setLooping(true);
            musicStatus.setPlaying(true);
            musicStatus.setVolume(musicVolume);
        }
        else {
            mediaPlayer = MediaPlayer.create(this, R.raw.background);
            mediaPlayer.setVolume(musicStatus.getVolume(), musicStatus.getVolume());
            mediaPlayer.seekTo(musicStatus.getDuration());
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            musicStatus.setPlaying(true);
            musicVolume = musicStatus.getVolume();
        }

        customDialog = new CustomDialog(MainActivity.this);

        btnLearn = findViewById(R.id.btnLearn);
        btnPlay = findViewById(R.id.main_btnPlay);
        btnQuit = findViewById(R.id.main_btnQuit);
        btnRank = findViewById(R.id.main_btnRank);
        btnSetting = findViewById(R.id.main_btnSetting);

        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                intent.putExtra("duration", mediaPlayer.getCurrentPosition());
                intent.putExtra("volume", musicStatus.getVolume());
                intent.putExtra("playing", musicStatus.isPlaying());
                mediaPlayer.stop();
                mediaPlayer.release();
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameMenuActivity.class);
                intent.putExtra("duration", mediaPlayer.getCurrentPosition());
                intent.putExtra("volume", musicStatus.getVolume());
                intent.putExtra("playing", musicStatus.isPlaying());
                mediaPlayer.stop();
                mediaPlayer.release();
                startActivity(intent);
            }
        });

        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RankingMemoryActivity.class);
                intent.putExtra("duration", mediaPlayer.getCurrentPosition());
                intent.putExtra("volume", musicStatus.getVolume());
                intent.putExtra("playing", musicStatus.isPlaying());
                mediaPlayer.stop();
                mediaPlayer.release();
                startActivity(intent);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSettingDialog();
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            }
        });
    }

    private void callSettingDialog(){
        ImageView ivClose;
        Button btnIncrease, btnDecrease;
        TextView tvVolume;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View lview = inflater.inflate(R.layout.setting_dialog, null);
        builder.setView(lview);
        ivClose = lview.findViewById(R.id.setting_iv_close);
        btnIncrease = lview.findViewById(R.id.setting_btnIncrease);
        btnDecrease = lview.findViewById(R.id.setting_btnDecrease);
        tvVolume = lview.findViewById(R.id.setting_currentVolume);
        tvVolume.setText("" + musicStatus.getVolume());
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int volume = musicStatus.getVolume();
                if (volume == 100)
                    Toast.makeText(getApplicationContext(), "It at max volume now", Toast.LENGTH_SHORT).show();
                else {
                    volume += 10;
                    tvVolume.setText("" + volume);
                    musicStatus.setVolume(volume);
                    mediaPlayer.setVolume(musicStatus.getVolume(), musicStatus.getVolume());
                }
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int volume = musicStatus.getVolume();
                if (volume == 10)
                    Toast.makeText(getApplicationContext(), "It at min volume now", Toast.LENGTH_SHORT).show();
                else {
                    volume -= 10;
                    tvVolume.setText("" + volume);
                    musicStatus.setVolume(volume);
                    mediaPlayer.setVolume(musicStatus.getVolume(), musicStatus.getVolume());
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

}