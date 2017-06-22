package com.skilledhacker.developer.musiqx;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.Adapters.Menu_adapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Player.MusicService;
import com.skilledhacker.developer.musiqx.Player.MusicService.MusicBinder;
import com.skilledhacker.developer.musiqx.Utilities.Gestions_menu_bar;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;

/**
 * Created by Guy on 4/17/2017.
 */

public class PlayerActivity extends AppCompatActivity {

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
    private ListView listView;
    private LinearLayout linearLayout_menu_bar;
    private ImageButton moreSong_bar;
    private LinearLayout linearLayout_player_tools;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String [] menu_item;

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
        listView = (ListView)findViewById(R.id.list_view);
        menu_item = getResources().getStringArray(R.array.menu_item_player_activity);
        linearLayout_menu_bar = (LinearLayout)findViewById(R.id.my_drawer);
        moreSong_bar = (ImageButton)findViewById(R.id.MoreSong_bar);
        linearLayout_player_tools = (LinearLayout)findViewById(R.id.navigation_tool);

        Menu_adapter adapter = new Menu_adapter(this,menu_item);
        listView.setAdapter(adapter);
        linearLayout_menu_bar.setVisibility(View.INVISIBLE);





        MoreSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_menu_bar.setVisibility(View.VISIBLE);
                linearLayout_player_tools.setLayoutAnimation(Gestions_menu_bar.clearPlayertools(2000,PlayerActivity.this));
                linearLayout_player_tools.setVisibility(View.INVISIBLE);
                linearLayout_menu_bar.setLayoutAnimation(Gestions_menu_bar.openMoreSongBar(1000,PlayerActivity.this,linearLayout_menu_bar));
            }
        });

        moreSong_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_menu_bar.setLayoutAnimation(Gestions_menu_bar.closeMoreSongBar(1000,PlayerActivity.this,linearLayout_menu_bar));
            }
        });





        float screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float layoutHeight = linearLayout_menu_bar.getHeight();



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
                    handler.removeCallbacks(mUpdateTimeTask);
                    musicSrv.SetPaused(true);
                    Play.setImageResource(R.drawable.play_default);
                }else {
                    if (musicSrv.IsPaused()){
                        musicSrv.resumePlayer();
                        updateProgressBar();
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
                handler.removeCallbacks(mUpdateTimeTask);
                updateProgressBar();
                SongInfo.setText(musicSrv.GetPlayingInfo());
            }
        });

        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrev();
                handler.removeCallbacks(mUpdateTimeTask);
                updateProgressBar();
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
        updateProgressBar();
        Play.setImageResource(R.drawable.pause_focused);
    }

    public void updateProgressBar(){

        handler.postDelayed(mUpdateTimeTask,100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            long currentDuration = musicSrv.getPosn();
            long totalDuration = musicSrv.getDur() - currentDuration;
            TimeRemaining.setText(Utilities.milliSecondsToTimer(totalDuration));
            TimeElapsed.setText(Utilities.milliSecondsToTimer(currentDuration));
            int progress = (int)(Utilities.getProgressPercentage(currentDuration,musicSrv.getDur()));
            SongProgressBar.setProgress(progress);
            handler.postDelayed(this,100);
        }
    };




}
