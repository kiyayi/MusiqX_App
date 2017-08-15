package com.skilledhacker.developer.musiqx;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Adapters.Menu_adapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Player.MediaPlayerService;
import com.skilledhacker.developer.musiqx.Player.MusicService;
import com.skilledhacker.developer.musiqx.Player.MusicService.MusicBinder;
import com.skilledhacker.developer.musiqx.Utilities.Gestions_menu_bar;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;

import com.skilledhacker.developer.musiqx.Utilities.Verification;
import com.skilledhacker.developer.musiqx.Utilities.Volume_Controller;

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
    private boolean is_more_menu_open=false;
    private boolean is_player_tools_open = true;
    private SeekBar volume_control;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String []menu_item;

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

        listView = (ListView)findViewById(R.id.list_view);
        menu_item = getResources().getStringArray(R.array.menu_item_player_activity);
        linearLayout_menu_bar = (LinearLayout)findViewById(R.id.my_drawer);
        moreSong_bar = (ImageButton)findViewById(R.id.MoreSong_bar);
        linearLayout_player_tools = (LinearLayout)findViewById(R.id.navigation_tool);
        volume_control = (SeekBar)findViewById(R.id.volume_manage);

        Menu_adapter adapter = new Menu_adapter(PlayerActivity.this,menu_item);
        listView.setAdapter(adapter);
        linearLayout_menu_bar.setVisibility(View.INVISIBLE);

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

        MoreSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_player_tools_open = true;
                linearLayout_menu_bar.setLayoutAnimation(null);
                linearLayout_player_tools.setLayoutAnimation(null);
                linearLayout_player_tools.setLayoutAnimation(Gestions_menu_bar.clearPlayertools(500,PlayerActivity.this));

                linearLayout_player_tools.setLayoutAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(is_player_tools_open) {
                            is_player_tools_open = false;
                            linearLayout_menu_bar.setLayoutAnimation(Gestions_menu_bar.openMoreSongBar(500, PlayerActivity.this, linearLayout_menu_bar));
                            linearLayout_player_tools.setVisibility(View.INVISIBLE);
                            linearLayout_menu_bar.setVisibility(View.VISIBLE);
                            linearLayout_menu_bar.startLayoutAnimation();
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                if(is_player_tools_open) {
                    linearLayout_player_tools.startLayoutAnimation();
                }


            }
        });

        moreSong_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_more_menu_open = true;
                linearLayout_menu_bar.setLayoutAnimation(null);
                linearLayout_player_tools.setLayoutAnimation(null);
                linearLayout_menu_bar.setLayoutAnimation(Gestions_menu_bar.closeMoreSongBar(500,PlayerActivity.this,linearLayout_menu_bar));
                linearLayout_menu_bar.setLayoutAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (is_more_menu_open==true) {
                            is_more_menu_open = false;
                            linearLayout_player_tools.setLayoutAnimation(Gestions_menu_bar.showPlayerTools(500, PlayerActivity.this));
                            linearLayout_menu_bar.setVisibility(View.INVISIBLE);
                            linearLayout_player_tools.setVisibility(View.VISIBLE);
                            linearLayout_player_tools.startLayoutAnimation();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                if(is_more_menu_open) {
                    linearLayout_menu_bar.startLayoutAnimation();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        Volume_Controller.volume_Controller(volume_control,PlayerActivity.this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PlayerActivity.this,menu_item[position]+" is cheched",Toast.LENGTH_SHORT).show();
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
            musicSrv = binder.getService();
            musicBound = true;

            boolean skip=false;
            if (SongId != -1) {
                musicSrv.init_playing_number();
                songPicked();
                skip=true;
            } else {
                SongId = database.retrieve_playing()+1;
            }

            if (!musicSrv.isPng()){
                int curr_pos=database.retrieve_playing_pos();
                if (curr_pos>0){
                    int len=database.retrieve_playing_len();
                    int remaininglDuration = len - curr_pos;
                    TimeRemaining.setText(Utilities.milliSecondsToTimer(remaininglDuration));
                    TimeElapsed.setText(Utilities.milliSecondsToTimer(curr_pos));
                    int progress =(Utilities.getProgressPercentage(curr_pos,len));
                    SongProgressBar.setProgress(progress);
                }
            }

            SongInfo.setText(musicSrv.GetPlayingInfo());
            if (musicSrv.isPng()) {
                Play.setImageResource(R.drawable.pause_focused);
                updateProgressBar();
            } else if (!skip){
                Play.setImageResource(R.drawable.play_default);
            }

            if (musicSrv.ShuffleOn())Shuffle.setImageResource(R.drawable.shuffle_on);
            else Shuffle.setImageResource(R.drawable.shuffle_off);

            if (musicSrv.repeatState()==0)Repeat.setImageResource(R.drawable.repeat_off);
            else if (musicSrv.repeatState()==1) Repeat.setImageResource(R.drawable.repeat_one);
            else Repeat.setImageResource(R.drawable.repeat_on);
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