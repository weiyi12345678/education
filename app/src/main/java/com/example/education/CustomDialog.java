package com.example.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class CustomDialog {
    private Activity activity;
    private AlertDialog dialog;

    //variable for common dialog item
    private ImageView ivClose, btnPlay;

    //variable for difficulty dialog
    private Button btnEasy, btnMedium, btnHard;
    private Intent intentGameType;

    //variable for setting dialog
    private SeekBar sbSound, sbMusic;
    private Button btnIncrease, btnDecrease;

    //variable for pause dialog
    private Button btnForfeit;
    private TextView pauseDialogContent;
    private long remainingTime;

    //variable for start game dialog
    private TextView tvTitle, tvContent;
    private Button btnSGPlay, btnSGBack;

    private MediaPlayer mediaPlayer;

    Runnable runnable;
    Handler handler;

    CustomDialog(Activity myActivity) {
        this.activity = myActivity;
    }

    void callLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lview = inflater.inflate(R.layout.loading_dialog, null);
        builder.setView(lview);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /*
    void callSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lview = inflater.inflate(R.layout.setting_dialog, null);
        builder.setView(lview);
        ivClose = lview.findViewById(R.id.setting_iv_close);
        //sbSound = lview.findViewById(R.id.sound_seekbar);
        sbMusic = lview.findViewById(R.id.music_seekbar);
        btnPlay = lview.findViewById(R.id.btnPlay);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        btnPlay.setOnClickListener(btnClickListen);

        mediaPlayer = new MediaPlayer();

        handler = new Handler();

        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    mediaPlayer.seekTo(i);
                    sbMusic.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private View.OnClickListener btnClickListen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btnPlay.setImageResource(R.drawable.ic_pause);
            PlaySong();
        }
    };

    public void PlaySong(){
        Uri uri = Uri.parse("https://www.bensound.com/bensound-music/bensound-summer.mp3");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();


        try {
            mediaPlayer.setDataSource(String.valueOf(uri));
        }catch (Exception e){
            e.printStackTrace();
        }

        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                sbMusic.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                updateSeekbar();
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                double ratio = percent / 100.0;
                int bufferingLevel = (int)(mediaPlayer.getDuration() * ratio);
                sbMusic.setSecondaryProgress(bufferingLevel);
            }
        });
    }

    public void updateSeekbar(){
        int currPos = mediaPlayer.getCurrentPosition();
        sbMusic.setProgress(currPos);

        runnable = new Runnable() {
            @Override
            public void run() {
                updateSeekbar();
            }
        };
        handler.postDelayed(runnable, 1000);
    }
*/
    void callDifficultyDialog(int gameType, MediaPlayer mp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View lview = inflater.inflate(R.layout.game_difficulty_dialog, null);
        builder.setView(lview);
        ivClose = lview.findViewById(R.id.difficulty_iv_close);
        btnEasy = lview.findViewById(R.id.btnDifficultyEasy);
        btnMedium = lview.findViewById(R.id.btnDifficultyMedium);
        btnHard = lview.findViewById(R.id.btnDifficultyHard);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        switch (gameType) {
            case 1:
                intentGameType = new Intent(activity, GameMemoryActivity.class);
                break;
            case 2:
                intentGameType = new Intent(activity, GameMatchActivity.class);
                break;
            default:
                intentGameType = null;
                break;

        }

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGameType.putExtra("Difficulty", 1);
                mp.stop();
                mp.release();
                activity.startActivity(intentGameType);
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGameType.putExtra("Difficulty", 2);
                mp.stop();
                mp.release();
                activity.startActivity(intentGameType);
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGameType.putExtra("Difficulty", 3);
                activity.startActivity(intentGameType);
            }
        });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    void closeDialog() {
        dialog.dismiss();
    }
}
