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

import java.util.Collections;
import java.util.Random;

import android.app.Notification;
import android.app.PendingIntent;

import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
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
    private boolean shuffle=false;
    private int repeat=0;// 0 for off, 1 for repeat one, 2 for repeat all
    private Random rand;
    private ArrayList<Integer> shuffleExclude;

    private ArrayList<Audio> songs;
    private DatabaseHandler database;
    private boolean paused=false;
    private boolean stopped=false;

    public static final String player_status_change_broadcast="com.skilledhacker.developer.musiqx.player.status";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        stopped=true;
        player_status_broadcast();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            player.reset();
            playNext(false);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        player.reset();
        stopped=true;
        player_status_broadcast();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
        stopped=false;
        player_status_broadcast();

        Intent notIntent = new Intent(this, PlayerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(), 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
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
        songs=database.retrieve_music();
        shuffleExclude=new ArrayList<>();
        database.insert_playing(0);
        initMusicPlayer();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
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
        //player_status_broadcast();
        Audio playSong = songs.get(database.retrieve_playing());
        int currSong = playSong.getData();
        try{
            if(StorageHandler.SongOnStorage(currSong)){
                player.setDataSource(StorageHandler.PathBuilder(currSong));
            }else{
                player.setDataSource(StorageHandler.URLBuilder(currSong));
            }
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public int getPosn(){
        if (player.isPlaying()) return player.getCurrentPosition();
        return 0;
    }

    public int getDur(){
        if (player.isPlaying()) return player.getDuration();
        return 0;
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        if (player.isPlaying()) {
            player.pause();
            player_status_broadcast();
        }
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void resumePlayer(){
        if (!player.isPlaying()) {
            player.start();
            stopped=false;
            player_status_broadcast();
        }
    }

    public void stopPlayer() {
        if (player == null) return;
        if (player.isPlaying()) {
            player.stop();
        }
        stopped=true;
        player_status_broadcast();
    }

    public void playPrev(){
        int pos=database.retrieve_playing();
        pos--;
        if(pos<0) pos=songs.size()-1;
        database.update_playing(pos);
        playSong();
    }

    public void playNext(boolean fromActivity){
        if (repeat==1 && fromActivity==false){
            playSong();
            return;
        }

        int pos=database.retrieve_playing();
        if (shuffle){
            int uniqueRandom=RandomWithExclusion(rand,0,songs.size()-1,shuffleExclude);
            pos=uniqueRandom;
            shuffleExclude.add(uniqueRandom);
            if (shuffleExclude.size()==songs.size() || shuffleExclude.size()>50000){
                shuffleExclude.clear();
            }else {
                Collections.sort(shuffleExclude);
            }
        }else {
            pos++;
            if(pos>=songs.size()) pos=0;
        }
        database.update_playing(pos);

        if (repeat==0 && pos==0 && fromActivity==false){
            stopPlayer();
            return;
        }
        playSong();
    }

    private int RandomWithExclusion(Random rnd, int start, int end, ArrayList<Integer> exclude) {
        int random = start + rnd.nextInt(end - start + 1 - exclude.size());
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }
    public void setShuffle(){
        shuffle = !shuffle;
        if (shuffle && shuffleExclude.size()>0){
            shuffleExclude.clear();
        }
    }
    public boolean ShuffleOn(){
        return shuffle;
    }

    public void  setRepeat(int state){
        repeat=state;
    }

    public int repeatState(){
        return repeat;
    }

    public void SetPaused(boolean state){
        paused=state;
    }

    public boolean IsPaused(){
        return paused;
    }

    public boolean IsStopped(){
        return stopped;
    }

    public String GetPlayingInfo(){
        String result=songs.get(database.retrieve_playing()).getTitle()+" - "+songs.get(database.retrieve_playing()).getArtist();
        return result;
    }

    private void player_status_broadcast(){
        Intent intent=new Intent();
        intent.setAction(player_status_change_broadcast);
        sendBroadcast(intent);
    }
}
