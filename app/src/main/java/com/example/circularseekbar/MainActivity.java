package com.example.circularseekbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.VolumeProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {


    CircularSeekBar seekBar;
    Handler handler = new Handler();
    Runnable runnable;
    MediaPlayer mediaPlayer;
    Toolbar toolbar;
    ImageView pause, repeat;
    SeekBar volume;
    boolean isLooping = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekbar);
        toolbar = findViewById(R.id.tool);
        pause = findViewById(R.id.pause);
        volume = findViewById(R.id.volume_seek);
        repeat = findViewById(R.id.repeat);
        setSupportActionBar(toolbar);


        //initializing Media Player ------------------------------->
        mediaPlayer = MediaPlayer.create(this, R.raw.song);

        //Set Max Progress to Seekbar------------------------------>
        seekBar.setMax(mediaPlayer.getDuration());

        //Initial Volume to media player---------------------------->
        mediaPlayer.setVolume(0.5f, 0.5f);

        //Initially Looping of a song is false----------------------->
        mediaPlayer.setLooping(false);


        //Changing volume according to Volume Seekbar Progress--------------------->
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                mediaPlayer.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Handling Play/Pause of the Audio------------------------->
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                } else {
                    pause.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                    changeSeekbar();
                }


            }
        });


        //Changing progress of the Music Player according to Seekbar---------------->
        seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo((int) progress);
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });


        //Handling repeat button changes---------------->
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isLooping()) {
                    repeat.setImageResource(R.drawable.ic_baseline_repeat_24);
                    Toast.makeText(MainActivity.this, "Repeat Mode is OFF", Toast.LENGTH_SHORT).show();
                    mediaPlayer.setLooping(false);

                } else if (!mediaPlayer.isLooping()) {
                    repeat.setImageResource(R.drawable.ic_baseline_repeat_one_24);
                    Toast.makeText(MainActivity.this, "Repeat Mode is ON", Toast.LENGTH_SHORT).show();
                    mediaPlayer.setLooping(true);
                }
            }
        });


        //What to do when Media Player is Completed---------------------->
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!mp.isLooping()) {
                    pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    seekBar.setCircleColor(getResources().getColor(R.color.grey));
                }
            }
        });


    }


    //Changing Seekbar progress after every 1 Second---------------->
    private void changeSeekbar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();
                }
            };

            handler.postDelayed(runnable, 1000);
        }
    }




    //Creating Option Menus---------------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }
}