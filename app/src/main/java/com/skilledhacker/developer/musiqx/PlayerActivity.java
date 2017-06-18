package com.skilledhacker.developer.musiqx;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Player.MusicService;
import com.skilledhacker.developer.musiqx.Player.MusicService.MusicBinder;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;
import com.skilledhacker.developer.musiqx.Utilities.Verification;

/**
 * Created by Guy on 4/17/2017.
 */

public class PlayerActivity extends AppCompatActivity{

    private DatabaseHandler database;
    private int SongId;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.skilledhacker.developer.musiqx.PlayNewAudio";

    private ImageButton Play;

    private ImageButton Shuffle;

    private TextView SongInfo;
    private TextView TimeElapsed;
    private TextView TimeRemaining;
    private SeekBar SongProgressBar;
    private Handler handler = new Handler();
    private ImageButton ThumbsUp;
    private ImageButton ThumbsDown;
    private ImageButton Repeat;
    private ImageButton MoreSong;

    private MusicService musicSrv;
    private Intent playIntent=null;
    private boolean musicBound=false;
    private ImageButton Next;
    private ImageButton Previous;

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
        SongProgressBar=(SeekBar) findViewById(R.id.SongProgressBar);
        BroadcastReceiver player_status_receiver;

        database=new DatabaseHandler(PlayerActivity.this);
        Bundle b = getIntent().getExtras();
        SongId=-1;
        if(b != null) {
            SongId = b.getInt("data");
        }

        IntentFilter broadcast_filter = new IntentFilter();
        broadcast_filter.addAction(musicSrv.player_status_change_broadcast);
        player_status_receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (musicSrv.isPng()){
                    updateProgressBar();
                    musicSrv.SetPaused(false);
                    Play.setImageResource(R.drawable.pause_focused);
                    SongInfo.setText(musicSrv.GetPlayingInfo());
                }else {
                    handler.removeCallbacks(mUpdateTimeTask);
                    musicSrv.SetPaused(true);
                    Play.setImageResource(R.drawable.play_default);
                    SongInfo.setText(musicSrv.GetPlayingInfo());

                    if(musicSrv.IsStopped()){
                        TimeElapsed.setText(R.string.null_time);
                        TimeRemaining.setText(R.string.null_time);
                        SongProgressBar.setProgress(0);
                    }
                }
            }
        };
        registerReceiver(player_status_receiver,broadcast_filter);

        SongProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //handler.removeCallbacks(mUpdateTimeTask);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(mUpdateTimeTask);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = musicSrv.getDur();
                int currentPostion = Utilities.progressToTimer(seekBar.getProgress(),totalDuration);
                musicSrv.seek(currentPostion);
                updateProgressBar();
            }
        });

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicSrv.isPng()){
                    musicSrv.pausePlayer();
                }else {
                    if (musicSrv.IsPaused() && !musicSrv.IsStopped()){
                        musicSrv.resumePlayer();
                    }else if (musicSrv.IsPaused() && musicSrv.IsStopped()) {
                        musicSrv.playSong();
                    }else {
                        songPicked();
                    }
                }

            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playNext(true);
            }
        });

        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrev();

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

        Repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicSrv.repeatState()==0){
                    musicSrv.setRepeat(1);
                    Repeat.setImageResource(R.drawable.repeat_one);
                }else if (musicSrv.repeatState()==1){
                    musicSrv.setRepeat(2);
                    Repeat.setImageResource(R.drawable.repeat_on);
                }else if (musicSrv.repeatState()==2){
                    musicSrv.setRepeat(0);
                    Repeat.setImageResource(R.drawable.repeat_off);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(mUpdateTimeTask);
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
                updateProgressBar();
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
    }

    public void updateProgressBar(){

        handler.postDelayed(mUpdateTimeTask,100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            int currentDuration = musicSrv.getPosn();
            int totalDuration = musicSrv.getDur();
            int remaininglDuration = totalDuration - currentDuration;

            if (currentDuration>totalDuration || currentDuration<0) currentDuration=0; //Check if it can be safely deleted
            if (totalDuration<0) totalDuration=0; //Check if it can be safely deleted

            TimeRemaining.setText(Utilities.milliSecondsToTimer(remaininglDuration));
            TimeElapsed.setText(Utilities.milliSecondsToTimer(currentDuration));
            int progress =(Utilities.getProgressPercentage(currentDuration,totalDuration));
            SongProgressBar.setProgress(progress);
            handler.postDelayed(this,100);
        }
    };




}
