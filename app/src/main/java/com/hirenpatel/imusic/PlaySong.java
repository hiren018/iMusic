package com.hirenpatel.imusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    ImageView previous, next, play,imageView;
    TextView textView;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textcontent;
    int position;
    SeekBar seekBar;

    Thread updateseek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        play = findViewById(R.id.play);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
        textcontent = intent.getStringExtra("currentsong");
        textView.setText(textcontent);
        textView.setSelected(true);
        position = intent.getIntExtra("position" , 0);
        Uri uri= Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

       updateseek = new Thread(){
           @Override
           public void run() {
               int currentposition = 0;
               try {
                   while(currentposition<mediaPlayer.getDuration()){
                       currentposition= mediaPlayer.getCurrentPosition();
                       seekBar.setProgress(currentposition);
                       sleep(800);
                   }
               }
               catch (Exception e){
                   e.printStackTrace();
               }
           }
       };
       updateseek.start();

       play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mediaPlayer.isPlaying()){
                   play.setImageResource(R.drawable.play);
                   mediaPlayer.pause();
               }
               else{
                   play.setImageResource(R.drawable.pause);
                   mediaPlayer.start();
               }

           }
       });

       previous.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mediaPlayer.stop();
               mediaPlayer.release();
               if (position != 0){
                   position = position-1;
               }
               else{
                   position = songs.size()-1;
               }
               Uri uri= Uri.parse(songs.get(position).toString());
               mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
               mediaPlayer.start();
               int currentposition = mediaPlayer.getCurrentPosition();
               seekBar.setProgress(currentposition);
               play.setImageResource(R.drawable.pause);
               textcontent = songs.get(position).getName().toString();
               textView.setText(textcontent);
           }
       });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size()-1){
                    position = position+1;
                }
                else{
                    position = 0;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                int currentposition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentposition);
                play.setImageResource(R.drawable.pause);
                textcontent = songs.get(position).getName().toString();
                textView.setText(textcontent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlaySong.this, "Created by : Hiren Patel", Toast.LENGTH_SHORT).show();
            }
        });


    }
}