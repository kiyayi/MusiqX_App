package com.skilledhacker.developer.musiqx.Database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.Models.Metric;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by Guy on 7/16/2017.
 */

public class DatabaseUpdater {
    private DatabaseHandler database;
    private Context ctx;
    private int update_error_library =0;
    private int update_error_metric =0;

    private String library_sync_url="";
    private String metric_sync_url="";

    public static final String SYNC_LIBRARY_BROADCAST="com.skilledhacker.developer.musiqx.database.sync.library";
    public static final String SYNC_LIBRARY_ERROR="com.skilledhacker.developer.musiqx.database.sync.library.error";
    public static final String SYNC_LIBRARY_EMPTY="com.skilledhacker.developer.musiqx.database.sync.library.empty";

    public static final String SYNC_METRIC_BROADCAST="com.skilledhacker.developer.musiqx.database.sync.metric";
    public static final String SYNC_METRIC_ERROR="com.skilledhacker.developer.musiqx.database.sync.metric.error";
    public static final String SYNC_METRIC_EMPTY="com.skilledhacker.developer.musiqx.database.sync.metric.empty";

    public DatabaseUpdater(Context context) {
        database = new DatabaseHandler(context);
        library_sync_url=context.getString(R.string.library_update_url);
        metric_sync_url=context.getString(R.string.metric_update_url);;
        ctx=context;
    }

    public void SyncLibrary(){
        ArrayList<Audio> library_list=database.retrieve_library_change();
        String token=database.retrieve_account_token();
        JSONArray jsonArray=new JSONArray();

        try{
            jsonArray=library_list_to_json(library_list);
        }catch (JSONException e){
            e.printStackTrace();
            update_error_library =1;
        }

        LibraryRequest libraryRequest=new LibraryRequest();
        libraryRequest.execute(String.valueOf(jsonArray),"POST",token);

    }

    public void SyncMetric(){
        ArrayList<Metric> metric_list=database.retrieve_metric_change();
        String token=database.retrieve_account_token();
        JSONArray jsonArray=new JSONArray();

        try{
            jsonArray=metric_list_to_json(metric_list);
        }catch (JSONException e){
            e.printStackTrace();
            update_error_metric =1;
        }

        MetricRequest metricRequest=new MetricRequest();
        metricRequest.execute(String.valueOf(jsonArray),"POST",token);

    }

    private JSONArray library_list_to_json(ArrayList<Audio> list) throws JSONException{
        JSONArray array=new JSONArray();
        int i=0;
        int size=list.size();

        for (i=0;i<size;i++){
            JSONObject object=new JSONObject();
            object.put(DatabaseHandler.KEY_LIBRARY_SONG,list.get(i).getSong());
            object.put(DatabaseHandler.KEY_LIBRARY_UPDATED_AT,list.get(i).getUpdated_at());
            object.put(DatabaseHandler.KEY_STATUS,list.get(i).getStatus());

            array.put(object);
        }

        return array;
    }

    private JSONArray metric_list_to_json(ArrayList<Metric> list) throws JSONException{
        JSONArray array=new JSONArray();
        int i=0;
        int size=list.size();

        for (i=0;i<size;i++){
            JSONObject object=new JSONObject();
            object.put(DatabaseHandler.KEY_METRIC_SONG,list.get(i).getSong());
            object.put(DatabaseHandler.KEY_METRIC_PLAY,list.get(i).getSong());
            object.put(DatabaseHandler.KEY_METRIC_SKIP,list.get(i).getSong());
            object.put(DatabaseHandler.KEY_METRIC_RATING,list.get(i).getSong());
            object.put(DatabaseHandler.KEY_METRIC_UPDATED_AT,list.get(i).getUpdated_at());
            object.put(DatabaseHandler.KEY_STATUS,list.get(i).getStatus());

            array.put(object);
        }

        return array;
    }

    private class LibraryRequest extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String response= Utilities.put_post_request(library_sync_url,params[0],params[1],params[2]);
                return response;
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                update_error_library =2;
                return null;
            } catch (IOException e) {
                //e.printStackTrace();
                update_error_library =3;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if(response==null || response.isEmpty()){
                Intent intent=new Intent();
                if (update_error_library !=0) {
                    intent.setAction(SYNC_LIBRARY_ERROR);
                    ctx.sendBroadcast(intent);
                }else {
                    intent.setAction(SYNC_LIBRARY_EMPTY);
                    ctx.sendBroadcast(intent);
                }
            }else{
                try {
                    JSONArray array=new JSONArray(response);
                    long size=array.length();
                    for(int i=0;i<size;i++) {
                        JSONObject row=array.getJSONObject(i);

                        database.update_library(row.getInt(DatabaseHandler.KEY_LIBRARY_SONG), row.getString(DatabaseHandler.KEY_LIBRARY_SONG_TITLE),
                                row.getInt(DatabaseHandler.KEY_LIBRARY_ARTIST), row.getString(DatabaseHandler.KEY_LIBRARY_ARTIST_NAME),
                                row.getInt(DatabaseHandler.KEY_LIBRARY_ALBUM), row.getString(DatabaseHandler.KEY_LIBRARY_ALBUM_NAME),
                                row.getInt(DatabaseHandler.KEY_LIBRARY_GENRE), row.getString(DatabaseHandler.KEY_LIBRARY_GENRE_NAME),
                                row.getInt(DatabaseHandler.KEY_LIBRARY_YEAR), row.getInt(DatabaseHandler.KEY_LIBRARY_LICENSE),
                                row.getString(DatabaseHandler.KEY_LIBRARY_LICENSE_NAME), row.getString(DatabaseHandler.KEY_LIBRARY_LYRICS),
                                row.getString(DatabaseHandler.KEY_LIBRARY_CREATED_AT), row.getString(DatabaseHandler.KEY_LIBRARY_UPDATED_AT),false);
                    }

                    Intent intent=new Intent();
                    intent.setAction(SYNC_LIBRARY_BROADCAST);
                    ctx.sendBroadcast(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent intent=new Intent();
                    intent.setAction(SYNC_LIBRARY_ERROR);
                    ctx.sendBroadcast(intent);
                }
            }
        }
    }

    private class MetricRequest extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String response= Utilities.put_post_request(metric_sync_url,params[0],params[1],params[2]);
                return response;
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                update_error_metric =2;
                return null;
            } catch (IOException e) {
                //e.printStackTrace();
                update_error_metric =3;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if(response==null || response.isEmpty()){
                Intent intent=new Intent();
                if (update_error_metric !=0) {
                    intent.setAction(SYNC_METRIC_ERROR);
                    ctx.sendBroadcast(intent);
                }else {
                    intent.setAction(SYNC_METRIC_EMPTY);
                    ctx.sendBroadcast(intent);
                }
            }else{
                try {
                    JSONArray array=new JSONArray(response);
                    long size=array.length();
                    for(int i=0;i<size;i++) {
                        JSONObject row=array.getJSONObject(i);

                        database.update_metric(row.getInt(DatabaseHandler.KEY_METRIC_SONG), row.getInt(DatabaseHandler.KEY_METRIC_PLAY),
                                row.getInt(DatabaseHandler.KEY_METRIC_SKIP), row.getInt(DatabaseHandler.KEY_METRIC_RATING),
                                row.getString(DatabaseHandler.KEY_METRIC_CREATED_AT), row.getString(DatabaseHandler.KEY_METRIC_UPDATED_AT),false);
                    }

                    Intent intent=new Intent();
                    intent.setAction(SYNC_METRIC_BROADCAST);
                    ctx.sendBroadcast(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent intent=new Intent();
                    intent.setAction(SYNC_METRIC_ERROR);
                    ctx.sendBroadcast(intent);
                }
            }
        }
    }
}
