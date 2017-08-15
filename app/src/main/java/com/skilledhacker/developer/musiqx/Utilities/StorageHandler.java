package com.skilledhacker.developer.musiqx.Utilities;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Guy on 4/24/2017.
 */

public class StorageHandler {
    private static String MusicExtension=".mp3";
    private static String MusicURL="https://musiqx.herokuapp.com/player/audio";

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean SongOnStorage(int id,Context context){
        boolean is_ok=CreateMusiqXDir(context);
        if (is_ok){
            File file =GetMusicPath(id, context);
            return file.exists();
        }else return false;
    }

    public static String PathBuilder(int id,int file_type,Context context){
        if (file_type==0) {
            File file = GetMusicPath(id, context);
            return file.toString();
        }else return null;
    }

    public static String URLBuilder(int id,int file_type){
        if (file_type==0){
            String URL=MusicURL+"/"+id+""+MusicExtension;
            return URL;
        }else return null;
    }

    private static boolean CreateMusiqXDir(Context context) {
        if (isExternalStorageReadable() && isExternalStorageWritable()){
            File file = new File(context.getExternalFilesDir(null), Environment.DIRECTORY_MUSIC);
            if (!file.exists()) {
                file.mkdirs();
                file.setExecutable(true);
                file.setReadable(true);
                file.setWritable(true);
            }
            return file.exists();
        }else return false;
    }

    private static File GetMusicPath(int id,Context context){
        String filename=id+""+MusicExtension;
        File filePath = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), filename);
        return filePath;
    }

    public static void initDirs(Context context){
        CreateMusiqXDir(context);
    }
}
