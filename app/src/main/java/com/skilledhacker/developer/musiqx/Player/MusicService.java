package com.skilledhacker.developer.musiqx.Player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.View;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Database.DatabaseSynchronizer;
import com.skilledhacker.developer.musiqx.PlayerActivity;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.StorageHandler;

/**
 * Created by Guy on 4/24/2017.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private static final int NOTIFY_ID=1;
    private final IBinder musicBind = new MusicBinder();
    private BroadcastReceiver SyncReceiver;
    private boolean shuffle=false;
    private Random rand;

    private ArrayList<Audio> songs;
    private DatabaseHandler database;
    private boolean paused=false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            player.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        player.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();

        Intent notIntent = new Intent(this, PlayerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(), 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.mini_play)
                .setTicker(songs.get(database.retrieve_playing()).getTitle())
                .setOngoing(true)
                .setContentTitle(songs.get(database.retrieve_playing()).getTitle())
        .setContentText(songs.get(database.retrieve_playing()).getArtist());
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        rand=new Random();
        player = new MediaPlayer();
        database=new DatabaseHandler(this);
        initSongs();
        database.insert_playing(0);
        initBroadcasts();
        initMusicPlayer();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void initSongs(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                songs=database.retrieve_music();
            }
        });
    }
    public void initMusicPlayer(){
        player.setWakeMode(this,PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong(){
        player.reset();
        Audio playSong = songs.get(database.retrieve_playing());
        int currSong = playSong.getData();
        try{
            if(StorageHandler.SongOnStorage(currSong,this)){
                player.setDataSource(StorageHandler.PathBuilder(currSong,0,this));
            }else{
                player.setDataSource(StorageHandler.URLBuilder(currSong,0));
            }
        }
        catch(Exception e){
            Toast.makeText(this,R.string.player_source_error,Toast.LENGTH_LONG).show();
        }
        player.prepareAsync();
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        if (player.isPlaying()) {
            player.pause();
        }
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void resumePlayer(){
        if (!player.isPlaying()) {
            player.start();
        }
    }

    public void stopPlayer() {
        if (player == null) return;
        if (player.isPlaying()) {
            player.stop();
        }
    }

    public void playPrev(){
        int pos=database.retrieve_playing();
        pos--;
        if(pos<0) pos=songs.size()-1;
        database.update_playing(pos);
        playSong();
    }

    public void playNext(){
        int pos=database.retrieve_playing();
        if(shuffle){
            int newSong = pos;
            while(newSong==pos){
                newSong=rand.nextInt(songs.size());
            }
            pos=newSong;
        }
        else{
            pos++;
            if(pos>=songs.size()) pos=0;
        }
        database.update_playing(pos);
        playSong();
    }

    public void setShuffle(){
        shuffle = !shuffle;

    }
    public boolean ShuffleOn(){
        return shuffle;
    }

    public void SetPaused(boolean state){
        paused=state;
    }

    public boolean IsPaused(){
        return paused;
    }

    public String GetPlayingInfo(){
        String result=songs.get(database.retrieve_playing()).getTitle()+" - "+songs.get(database.retrieve_playing()).getArtist();
        return result;
    }

    private void initBroadcasts(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(DatabaseSynchronizer.SyncBroadcast);
        SyncReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initSongs();
            }
        };

        registerReceiver(SyncReceiver,filter);
    }
}
