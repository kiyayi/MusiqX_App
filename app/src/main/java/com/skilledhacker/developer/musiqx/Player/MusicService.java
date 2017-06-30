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
    private int repeat=0;// 0 for off, 1 for repeat one, 2 for repeat all
    private Random rand;
    private ArrayList<Integer> shuffledSongs;

    private ArrayList<Audio> songs;
    private DatabaseHandler database;
    private boolean paused=false;
    private boolean stopped=false;
    private long playingNumber=1;
    private int saved_pos;

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
        if (saved_pos>0) seek(saved_pos);
        player.start();
        stopped=false;
        player_status_broadcast();

        Intent notIntent = new Intent(this, PlayerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(), 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.mini_play)
                .setTicker(songs.get(database.retrieve_playing()).getSong_title())
                .setOngoing(true)
                .setContentTitle(songs.get(database.retrieve_playing()).getSong_title())
        .setContentText(songs.get(database.retrieve_playing()).getArtist_name());
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        rand=new Random();
        player = new MediaPlayer();
        database=new DatabaseHandler(this);
        if (database.getNumberOfRows(DatabaseHandler.TABLE_PLAYING)<1) database.insert_playing(0);
        saved_pos=database.retrieve_playing_pos();
        shuffledSongs=new ArrayList<>();
        songs=new ArrayList<>();
        initBroadcasts();
        initSongs();
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
                if (songs.size()>0){
                    songs.clear();
                    shuffledSongs.clear();
                }
                songs=database.retrieve_library();
                initShuffle(songs.size());
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
        //player_status_broadcast();
        Audio playSong = songs.get(database.retrieve_playing());
        int currSong = playSong.getSong();
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

    public void updatePos(){
        int curr_pos=getPosn();
        int len=getDur();
        if (curr_pos>0) database.update_playing_pos(curr_pos,len);
    }

    public void playing_number_increase(){
        playingNumber++;
        if (playingNumber>songs.size()){
            playingNumber=1;
        }
    }
    public void playing_number_decrease(){
        playingNumber--;
        if (playingNumber<1){
            playingNumber=songs.size();
        }
    }

    public void init_playing_number(){
        playingNumber=1;
    }

    public void playPrev(){
        if (getPosn()<=2000){
            playSong();
            return;
        }

        int pos=database.retrieve_playing();
        if (shuffle){
            int size=shuffledSongs.size();
            int last_pos=pos;
            int new_pos=pos-1;
            if (new_pos<0){
                pos=shuffledSongs.get(size-1);
                if (pos==last_pos && size>1) pos=shuffledSongs.get(RandomWithExclusion(rand,0,size-1,size-1));
            }
            else{
                pos=shuffledSongs.get(new_pos);
                if (pos==last_pos && size>1) pos=shuffledSongs.get(RandomWithExclusion(rand,0,size-1,new_pos));
            }
        }else {
            pos--;
            if(pos<0) pos=songs.size()-1;
        }

        database.update_playing(pos);
        playing_number_decrease();
        playSong();
    }

    public void playNext(boolean fromActivity){
        if (repeat==1 && fromActivity==false){
            playSong();
            return;
        }

        int pos=database.retrieve_playing();
        if (shuffle){
            int size=shuffledSongs.size();
            int last_pos=pos;
            int new_pos=pos+1;
            if (new_pos>=size){
                pos=shuffledSongs.get(0);
                if (pos==last_pos && size>1) pos=shuffledSongs.get(RandomWithExclusion(rand,0,size-1,0));
            }
            else{
                pos=shuffledSongs.get(new_pos);
                if (pos==last_pos && size>1) pos=shuffledSongs.get(RandomWithExclusion(rand,0,size-1,new_pos));
            }
        }else {
            pos++;
            if(pos>=songs.size()) pos=0;
        }
        database.update_playing(pos);
        playing_number_increase();

        if (repeat==0 && fromActivity==false && playingNumber==1){
            stopPlayer();
            return;
        }
        playSong();
    }

    private int RandomWithExclusion(Random rnd, int start, int end, int... exclude) {
        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
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
        init_playing_number();
        if (shuffle){
            shuffle_songs();
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
        String result=songs.get(database.retrieve_playing()).getSong_title()+" - "+songs.get(database.retrieve_playing()).getArtist_name();
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
          
    private void player_status_broadcast(){
        Intent intent=new Intent();
        intent.setAction(player_status_change_broadcast);
        sendBroadcast(intent);
    }

    private void initShuffle(final int size){
        int i;
        for(i=0;i<size;i++) shuffledSongs.add(i);
    }

    private void shuffle_songs(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Collections.shuffle(shuffledSongs);
            }
        });
    }
}
