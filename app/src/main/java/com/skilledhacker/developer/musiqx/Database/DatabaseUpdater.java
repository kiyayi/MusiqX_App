package com.skilledhacker.developer.musiqx.Database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Guy on 4/18/2017.
 */

public class DatabaseUpdater {
    private DatabaseHandler database;
    private Context ctx;
    private int update_error=0;

    public DatabaseUpdater(Context context) {
        database = new DatabaseHandler(context);
        ctx=context;
    }

    public int getUpdate_error() {
        return update_error;
    }

    public String Update(String theURL,String table) {
        String result;
        update_error=0;
        try {
            result=download(theURL);
            try {
                JSONArray json=new JSONArray(result);
                long size=json.length();
                for(int i=0;i<size;i++){
                    JSONObject row=json.getJSONObject(i);
                    if(table==DatabaseHandler.TABLE_LIBRARY){
                        if (database.CheckIsDataAlreadyInDBorNot(row.getInt(DatabaseHandler.KEY_LIBRARY_SONG),table) == false && row.getInt(DatabaseHandler.KEY_LIBRARY_SONG)!=0) {
                            database.insert_library(row.getInt(DatabaseHandler.KEY_LIBRARY_SONG), row.getString(DatabaseHandler.KEY_LIBRARY_SONG_TITLE),
                                    row.getInt(DatabaseHandler.KEY_LIBRARY_ARTIST), row.getString(DatabaseHandler.KEY_LIBRARY_ARTIST_NAME),
                                    row.getInt(DatabaseHandler.KEY_LIBRARY_ALBUM), row.getString(DatabaseHandler.KEY_LIBRARY_ALBUM_NAME),
                                    row.getInt(DatabaseHandler.KEY_LIBRARY_GENRE), row.getString(DatabaseHandler.KEY_LIBRARY_GENRE_NAME),
                                    row.getInt(DatabaseHandler.KEY_LIBRARY_YEAR), row.getInt(DatabaseHandler.KEY_LIBRARY_LICENSE),
                                    row.getString(DatabaseHandler.KEY_LIBRARY_LICENSE_NAME), row.getString(DatabaseHandler.KEY_LIBRARY_LYRICS),
                                    row.getString(DatabaseHandler.KEY_LIBRARY_CREATED_AT), row.getString(DatabaseHandler.KEY_LIBRARY_UPDATED_AT));
                        }
                    }
                }

            } catch (JSONException e) {
                update_error=1;
            }

        }catch (IOException e){
            update_error=2;
        }
        return "";
    }

    private String download(String theURL) throws IOException{
        int BUFFER_SIZE=2000;
        InputStream is=null;
        String Contents="";
        try {
            URL url=new URL(theURL);
            String token=database.retrieve_account_token();
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Token "+token);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            int response=con.getResponseCode();

            Log.d("DownloadMusic","connection response is"+response);
            is=con.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            int charRead;
            char[] inputBuffer=new char[BUFFER_SIZE];
            try{
                while((charRead=isr.read(inputBuffer))>0){
                    String readString=String.copyValueOf(inputBuffer,0,charRead);
                    Contents+=readString;
                    inputBuffer=new char[BUFFER_SIZE];
                }

                return Contents;

            }catch (IOException e){
                update_error=3;
                return null;
            }

        }finally {
            if(is!=null){
                is.close();
            }
        }

    }
}
