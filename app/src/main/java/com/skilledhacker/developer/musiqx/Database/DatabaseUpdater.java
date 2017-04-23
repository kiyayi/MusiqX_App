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

    public DatabaseUpdater(Context context) {
        database = new DatabaseHandler(context);
        ctx=context;
    }

    public String Update(String theURL,String table) {
        String result;
        try {
            result=download(theURL);
            try {
                JSONArray json=new JSONArray(result);
                long size=json.length();
                for(int i=0;i<size;i++){
                    JSONObject row=json.getJSONObject(i);
                    Log.d("good3", "good3"+size);
                    if(table==DatabaseHandler.TABLE_MUSIC) {
                        if (database.CheckIsDataAlreadyInDBorNot(row.getInt(DatabaseHandler.KEY_MUSIC_ID),table) == false && row.getInt(DatabaseHandler.KEY_MUSIC_ID)!=0) {
                            Log.d("good5", "good5");
                            database.insert_music(row.getInt(DatabaseHandler.KEY_MUSIC_ID), row.getInt(DatabaseHandler.KEY_MUSIC_LICENCE),
                                    row.getString(DatabaseHandler.KEY_MUSIC_TITLE), row.getString(DatabaseHandler.KEY_MUSIC_ARTIST),
                                    row.getString(DatabaseHandler.KEY_MUSIC_ALBUM), row.getString(DatabaseHandler.KEY_MUSIC_GENRE),
                                    row.getInt(DatabaseHandler.KEY_MUSIC_YEAR));
                            Log.d("Success Music", "Values added in the database");
                        }
                    }else if(table==DatabaseHandler.TABLE_LIBRARY){
                        if (database.CheckIsDataAlreadyInDBorNot(row.getInt(DatabaseHandler.KEY_LIBRARY_ID),table) == false && row.getInt(DatabaseHandler.KEY_LIBRARY_ID)!=0) {
                            Log.d("good5", "good5");
                            database.insert_library(row.getInt(DatabaseHandler.KEY_LIBRARY_ID), row.getInt(DatabaseHandler.KEY_LIBRARY_USER_ID),
                                    row.getInt(DatabaseHandler.KEY_LIBRARY_MUSIC_ID), row.getString(DatabaseHandler.KEY_LIBRARY_DATE_ADDED),
                                    row.getInt(DatabaseHandler.KEY_LIBRARY_PLAY_COUNT), row.getInt(DatabaseHandler.KEY_LIBRARY_SKIP_COUNT));
                            Log.d("Success Library", "Values added in the database");
                        }
                    }else if(table==DatabaseHandler.TABLE_RATING){
                        if (database.CheckIsDataAlreadyInDBorNot(row.getInt(DatabaseHandler.KEY_RATING_ID),table) == false && row.getInt(DatabaseHandler.KEY_RATING_ID) != 0) {
                            Log.d("good5", "good5");
                            database.insert_rating(row.getInt(DatabaseHandler.KEY_RATING_ID), row.getInt(DatabaseHandler.KEY_RATING_USER_ID),
                                    row.getInt(DatabaseHandler.KEY_RATING_MUSIC_ID),row.getInt(DatabaseHandler.KEY_RATING_RATING));
                            Log.d("Success", "Values added in the database");
                        }
                    }else if(table==DatabaseHandler.TABLE_USER){
                    if (database.CheckIsDataAlreadyInDBorNot(row.getInt(DatabaseHandler.KEY_USER_ID),table) == false && row.getInt(DatabaseHandler.KEY_USER_ID) != 0) {
                        Log.d("good5", "good5");
                        database.insert_user(row.getInt(DatabaseHandler.KEY_USER_ID), row.getString(DatabaseHandler.KEY_USER_EMAIL),
                                row.getString(DatabaseHandler.KEY_USER_FNAME),row.getString(DatabaseHandler.KEY_USER_LNAME),
                                row.getString(DatabaseHandler.KEY_USER_DATE),row.getString(DatabaseHandler.KEY_USER_COUNTRY),
                                row.getString(DatabaseHandler.KEY_USER_ADDRESS),row.getInt(DatabaseHandler.KEY_USER_LICENCE),
                                row.getInt(DatabaseHandler.KEY_USER_AGE),row.getString(DatabaseHandler.KEY_USER_GENDER));
                        Log.d("Success", "Values added in the database");
                        }
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(ctx, R.string.update_library_fail,Toast.LENGTH_LONG);
            }

        }catch (IOException e){
            Toast.makeText(ctx, R.string.update_library_fail,Toast.LENGTH_LONG);
        }
        return "";
    }

    private String download(String theURL) throws IOException{
        int BUFFER_SIZE=2000;
        InputStream is=null;
        String Contents="";
        try {
            URL url=new URL(theURL);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
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
                e.printStackTrace();
                return null;
            }

        }finally {
            if(is!=null){
                is.close();
            }
        }

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
