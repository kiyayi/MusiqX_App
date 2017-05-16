package com.skilledhacker.developer.musiqx;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.MediaController.MediaPlayerControl;

import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Player.MusicService;
import com.skilledhacker.developer.musiqx.Player.MusicService.MusicBinder;

/**
 * Created by Guy on 4/17/2017.
 */

public class PlayerActivity extends AppCompatActivity{

    private DatabaseHandler database;
    private int SongId;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.skilledhacker.developer.musiqx.PlayNewAudio";

    private ImageButton Play;
    private ImageButton Next;
    private ImageButton Previous;
    private ImageButton ThumbsUp;
    private ImageButton ThumbsDown;
    private ImageButton Shuffle;
    private ImageButton Repeat;
    private ImageButton MoreSong;
    private TextView SongInfo;
    private TextView TimeElapsed;
    private TextView TimeRemaining;
    private ProgressBar SongProgressBar;

    private MusicService musicSrv;
    private Intent playIntent=null;
    private boolean musicBound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        startService();
        Play=(ImageButton)findViewById(R.id.Play);
        Next=(ImageButton)findViewById(R.id.Next);
        Previous=(ImageButton)findViewById(R.id.Previous);
        ThumbsUp=(ImageButton)findViewById(R.id.ThumbsUp);
        ThumbsDown=(ImageButton)findViewById(R.id.ThumbsDown);
        Shuffle=(ImageButton)findViewById(R.id.Shuffle);
        Repeat=(ImageButton)findViewById(R.id.Repeat);
        MoreSong=(ImageButton)findViewById(R.id.MoreSong);
        TimeElapsed=(TextView)findViewById(R.id.TimeElapsed);
        TimeRemaining=(TextView)findViewById(R.id.TimeRemaining);
        SongInfo=(TextView)findViewById(R.id.SongInfo);
        SongProgressBar=(ProgressBar) findViewById(R.id.SongProgressBar);

        database=new DatabaseHandler(PlayerActivity.this);
        Bundle b = getIntent().getExtras();
        SongId=-1;
        if(b != null) {
            SongId = b.getInt("data");
        }

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicSrv.isPng()){
                    musicSrv.pausePlayer();
                    musicSrv.SetPaused(true);
                    Play.setImageResource(R.drawable.play_default);
                }else {
                    if (musicSrv.IsPaused()){
                        musicSrv.resumePlayer();
                        musicSrv.SetPaused(false);
                        Play.setImageResource(R.drawable.pause_focused);
                    }else {
                        songPicked();
                        SongInfo.setText(musicSrv.GetPlayingInfo());
                    }
                }

            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playNext();
                SongInfo.setText(musicSrv.GetPlayingInfo());
            }
        });

        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrev();
                SongInfo.setText(musicSrv.GetPlayingInfo());
            }
        });

        Shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.setShuffle();
                if (musicSrv.ShuffleOn()){
                    Shuffle.setImageResource(R.drawable.shuffle_on);
                }else {
                    Shuffle.setImageResource(R.drawable.shuffle_off);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_add:
                break;
            case R.id.action_download:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicBound = true;

            boolean skip=false;
            if (SongId != -1) {
                songPicked();
                skip=true;
            } else {
                SongId = database.retrieve_playing()+1;
            }

            SongInfo.setText(musicSrv.GetPlayingInfo());
            if (musicSrv.isPng()) {
                Play.setImageResource(R.drawable.pause_focused);
            } else if (!skip){
                Play.setImageResource(R.drawable.play_default);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void startService(){
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void songPicked(){
        database.update_playing(SongId-1);
        musicSrv.playSong();
        Play.setImageResource(R.drawable.pause_focused);
    }
}
