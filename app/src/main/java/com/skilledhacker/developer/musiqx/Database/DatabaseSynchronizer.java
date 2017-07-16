package com.skilledhacker.developer.musiqx.Database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.R;

/**
 * Created by Guy on 4/19/2017.
 */

public class DatabaseSynchronizer extends AsyncTask<String,Void,String> {

    private DatUp updater;
    private String Library_Url="";
    public static final String SyncBroadcast="com.skilledhacker.developer.musiqx.sync";
    private Context ctx;

    public DatabaseSynchronizer(Context context){
        updater=new DatUp(context);
        ctx=context;
        Library_Url=ctx.getString(R.string.library_download_url);
    }

    @Override
    protected String doInBackground(String... params) {
        updater.Update(Library_Url,DatabaseHandler.TABLE_LIBRARY);
        //updater.Update(Rating_Url,DatabaseHandler.TABLE_RATING);
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        Intent intent=new Intent();
        intent.setAction(SyncBroadcast);
        ctx.sendBroadcast(intent);
        if (updater.getUpdate_error()!=0){
            Toast.makeText(ctx, R.string.update_library_fail, Toast.LENGTH_LONG).show();
        }
    }
}
