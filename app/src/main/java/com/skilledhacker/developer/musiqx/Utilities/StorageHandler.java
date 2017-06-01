package com.skilledhacker.developer.musiqx.Utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.skilledhacker.developer.musiqx.Player.Audio;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by Guy on 4/24/2017.
 */

public class StorageHandler {

    private static Random rand;
    private static String MusicFolder="MusiqX";
    private static String MusicExtension=".mp3";
    private static String MusicURL="https://musiqx.herokuapp.com/player/audio";

    public static int RandomSong(List<Audio> AudioList) {
        rand = new Random();
        int randomInt = AudioList.get(rand.nextInt(AudioList.size())).getData();
        return randomInt;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private static void CreateMusicFolder() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+MusicFolder);
        if (!file.exists()) {
            file.mkdirs();
            file.setExecutable(true);
            file.setReadable(true);
            file.setWritable(true);
        }
    }

    public static boolean SongOnStorage(int id){
        CreateMusicFolder();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+MusicFolder+"/"+id+""+MusicExtension);
        return file.exists();
    }

    public static String PathBuilder(int id){
        String path=Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+MusicFolder+"/"+id+""+MusicExtension;
        return path;
    }

    public static String URLBuilder(int id){
        String URL=MusicURL+"/"+id+""+MusicExtension;
        return URL;
    }
}
