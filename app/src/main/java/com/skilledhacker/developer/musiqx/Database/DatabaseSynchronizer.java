package com.skilledhacker.developer.musiqx.Database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;

/**
 * Created by Guy on 4/19/2017.
 */

public class DatabaseSynchronizer extends AsyncTask<String,Void,String> {

    private DatabaseUpdater updater;
    private final String Music_Url="https://musiqx.herokuapp.com/app/music/song";
    private final String Library_Url="https://musiqx.herokuapp.com/app/library/song";
    private final String Rating_Url="";
    private final String User_Url="";
    public static final String SyncBroadcast="com.skilledhacker.developer.musiqx.sync";
    private Context ctx;

    public DatabaseSynchronizer(Context context){
        updater=new DatabaseUpdater(context);
        ctx=context;
    }

    @Override
    protected String doInBackground(String... params) {
        //updater.Update(User_Url,DatabaseHandler.TABLE_USER);
        updater.Update(Music_Url,DatabaseHandler.TABLE_MUSIC);
        updater.Update(Library_Url,DatabaseHandler.TABLE_LIBRARY);
        //updater.Update(Rating_Url,DatabaseHandler.TABLE_RATING);
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        Intent intent=new Intent();
        intent.setAction(SyncBroadcast);
        ctx.sendBroadcast(intent);
    }
}
