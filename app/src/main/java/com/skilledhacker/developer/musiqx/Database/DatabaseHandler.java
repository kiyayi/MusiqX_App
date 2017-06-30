package com.skilledhacker.developer.musiqx.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ParseException;
import android.util.Log;

import com.skilledhacker.developer.musiqx.Player.Audio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Guy on 4/18/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private SQLiteDatabase database;
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="database.db";
    private static final DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");

    //TABLES
    public static final String TABLE_ACCOUNT="account";
    public static final String TABLE_LIBRARY="library";
    public static final String TABLE_PLAYING="playing";

    //PLAYING KEY
    public static final String KEY_PLAYING_ID="id";
    public static final String KEY_PLAYING_POS="current_position";
    public static final String KEY_PLAYING_LENGTH="length";

    //ACCOUNT KEYS
    public static final String KEY_ACCOUNT_ID="id";
    public static final String KEY_ACCOUNT_TOKEN="token";
    public static final String KEY_ACCOUNT_DATE="date";

    //LIBRARY KEYS
    public static final String KEY_LIBRARY_SONG="song";
    public static final String KEY_LIBRARY_SONG_TITLE="song_title";
    public static final String KEY_LIBRARY_ARTIST="artist";
    public static final String KEY_LIBRARY_ARTIST_NAME="artist_name";
    public static final String KEY_LIBRARY_ALBUM="album";
    public static final String KEY_LIBRARY_ALBUM_NAME="album_name";
    public static final String KEY_LIBRARY_GENRE="genre";
    public static final String KEY_LIBRARY_GENRE_NAME="genre_name";
    public static final String KEY_LIBRARY_YEAR="year";
    public static final String KEY_LIBRARY_LICENSE="license";
    public static final String KEY_LIBRARY_LICENSE_NAME="license_name";
    public static final String KEY_LIBRARY_LYRICS="lyrics";
    public static final String KEY_LIBRARY_CREATED_AT="created_at";
    public static final String KEY_LIBRARY_UPDATED_AT="updated_at";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database=db;

        String CREATE_TABLE_LIBRARY = "CREATE TABLE " +TABLE_LIBRARY+ "("
                +KEY_LIBRARY_SONG + " INTEGER,"
                +KEY_LIBRARY_SONG_TITLE+ " VARCHAR(255), "
                +KEY_LIBRARY_ARTIST + " INTEGER,"
                +KEY_LIBRARY_ARTIST_NAME+ " VARCHAR(255), "
                +KEY_LIBRARY_ALBUM + " INTEGER,"
                +KEY_LIBRARY_ALBUM_NAME+ " VARCHAR(255), "
                +KEY_LIBRARY_GENRE + " INTEGER,"
                +KEY_LIBRARY_GENRE_NAME+ " VARCHAR(255), "
                +KEY_LIBRARY_YEAR + " INTEGER,"
                +KEY_LIBRARY_LICENSE + " INTEGER,"
                +KEY_LIBRARY_LICENSE_NAME+ " VARCHAR(255), "
                +KEY_LIBRARY_LYRICS+ " TEXT, "
                +KEY_LIBRARY_CREATED_AT+ " VARCHAR(100), "
                +KEY_LIBRARY_UPDATED_AT + " VARCHAR(100) );";

        String CREATE_TABLE_PLAYING = "CREATE TABLE " +TABLE_PLAYING+ "("
                +KEY_PLAYING_ID + " INTEGER,"
                +KEY_PLAYING_POS + " INTEGER,"
                +KEY_PLAYING_LENGTH + " INTEGER );";

        String CREATE_TABLE_ACCOUNT = "CREATE TABLE " +TABLE_ACCOUNT+ "("
                +KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY,"
                +KEY_ACCOUNT_TOKEN+ " VARCHAR(255), "
                +KEY_ACCOUNT_DATE + " VARCHAR(100) );";

        database.execSQL(CREATE_TABLE_ACCOUNT);
        database.execSQL(CREATE_TABLE_LIBRARY);
        database.execSQL(CREATE_TABLE_PLAYING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        database=db;

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYING);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);

        // Create tables again
        onCreate(database);
    }

    //CRUD FOR ACCOUNT
    public void insert_account(int id,String token){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_ACCOUNT_ID, id);
        values.put(KEY_ACCOUNT_TOKEN, token);
        values.put(KEY_ACCOUNT_DATE, get_date());
        database.insert(TABLE_ACCOUNT, null, values);
        database.close();
    }

    public void delete_account(int id){
        database=getWritableDatabase();
        database.delete(TABLE_ACCOUNT, KEY_ACCOUNT_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public String retrieve_account_token(){
        String result="";
        String query = "SELECT * FROM "+TABLE_ACCOUNT;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            result = cursor.getString(1);
        }

        cursor.close();
        database.close();
        return result;
    }

    public String retrieve_account_date(){
        String result="";
        String query = "SELECT * FROM "+TABLE_ACCOUNT;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            result = cursor.getString(2);
        }

        cursor.close();
        database.close();
        return result;
    }

    //CRUD FOR PLAYING
    public void insert_playing(int id){
        database=getWritableDatabase();
        int pos=0;
        int len=0;
        ContentValues values=new ContentValues();
        values.put(KEY_PLAYING_ID, id);
        values.put(KEY_PLAYING_POS, pos);
        values.put(KEY_PLAYING_LENGTH, len);
        database.insert(TABLE_PLAYING, null, values);
        database.close();
    }

    public void delete_playing(int id){
        database=getWritableDatabase();
        database.delete(TABLE_PLAYING, KEY_PLAYING_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void update_playing(int id){
        long oldId=retrieve_playing();
        database=getWritableDatabase();
        int pos=0;
        int len=0;
        ContentValues values=new ContentValues();
        values.put(KEY_PLAYING_ID, id);
        values.put(KEY_PLAYING_POS, pos);
        values.put(KEY_PLAYING_LENGTH, len);
        database.update(TABLE_PLAYING, values, KEY_PLAYING_ID + "=?", new String[]{String.valueOf(oldId)});
        database.close();
    }

    public int retrieve_playing(){
        int index=-1;
        String query = "SELECT * FROM "+TABLE_PLAYING;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            index=cursor.getInt(0);
        }

        cursor.close();
        database.close();
        return index;
    }

    public void update_playing_pos(int pos,int len){
        long oldId=retrieve_playing();
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_PLAYING_POS, pos);
        values.put(KEY_PLAYING_LENGTH, len);
        database.update(TABLE_PLAYING, values, KEY_PLAYING_ID + "=?", new String[]{String.valueOf(oldId)});
        database.close();
    }

    public int retrieve_playing_pos(){
        int index=-1;
        String query = "SELECT * FROM "+TABLE_PLAYING;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            index=cursor.getInt(1);
        }

        cursor.close();
        database.close();
        return index;
    }

    public int retrieve_playing_len(){
        int index=-1;
        String query = "SELECT * FROM "+TABLE_PLAYING;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            index=cursor.getInt(2);
        }

        cursor.close();
        database.close();
        return index;
    }

    //CRUD FOR LIBRARY
    public void insert_library(int song,String song_title,int artist,String artist_name,int album,String album_name,
                               int genre,String genre_name,int year,int license,String license_name,
                               String lyrics,String created_at,String updated_at){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_LIBRARY_SONG, song);
        values.put(KEY_LIBRARY_SONG_TITLE, song_title);
        values.put(KEY_LIBRARY_ARTIST, artist);
        values.put(KEY_LIBRARY_ARTIST_NAME, artist_name);
        values.put(KEY_LIBRARY_ALBUM, album);
        values.put(KEY_LIBRARY_ALBUM_NAME, album_name);
        values.put(KEY_LIBRARY_GENRE, genre);
        values.put(KEY_LIBRARY_GENRE_NAME, genre_name);
        values.put(KEY_LIBRARY_YEAR, year);
        values.put(KEY_LIBRARY_LICENSE, license);
        values.put(KEY_LIBRARY_LICENSE_NAME, license_name);
        values.put(KEY_LIBRARY_LYRICS, lyrics);
        values.put(KEY_LIBRARY_CREATED_AT, created_at);
        values.put(KEY_LIBRARY_UPDATED_AT, updated_at);
        database.insert(TABLE_LIBRARY, null, values);
        database.close();
    }

    public void delete_library(int song){
        database=getWritableDatabase();
        database.delete(TABLE_LIBRARY, KEY_LIBRARY_SONG + "=?", new String[]{String.valueOf(song)});
        database.close();
    }

    public ArrayList<Audio> retrieve_library(){
        ArrayList<Audio> audioList=new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_LIBRARY;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            int song = cursor.getInt(0);
            String song_title = cursor.getString(1);
            int artist = cursor.getInt(2);;
            String artist_name = cursor.getString(3);
            int album = cursor.getInt(4);;
            String album_name = cursor.getString(5);
            int genre = cursor.getInt(6);;
            String genre_name = cursor.getString(7);
            int year = cursor.getInt(8);;
            String lyrics = cursor.getString(9);
            int license = cursor.getInt(10);;

            audioList.add(new Audio(song, song_title, artist, artist_name, album, album_name,
                    genre, genre_name, year, lyrics, license));
        }

        cursor.close();
        database.close();
        return audioList;

    }

    public boolean is_login() throws java.text.ParseException {
        if (getNumberOfRows(TABLE_ACCOUNT)>0){
            if (get_date_difference(retrieve_account_date(),get_date())>30){
                delete_account(1);
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    public long getNumberOfRows(String table) {
        database = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(database, table);
        database.close();
        return cnt;
    }

    private String get_date(){
        Date date = new Date();
        String result=date_format.format(date);
        return result;
    }

    private long get_date_difference(String old_date,String new_date) throws java.text.ParseException {
        Date date1 = date_format.parse(old_date);
        Date date2 = date_format.parse(new_date);
        long diff = date2.getTime() - date1.getTime();
        long result=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return result;
    }

    public boolean CheckIsDataAlreadyInDBorNot(int fieldValue,String table) {
        String id="";
        if (table==TABLE_ACCOUNT){
            id=KEY_ACCOUNT_ID;
        }else if (table==TABLE_LIBRARY){
            id=KEY_LIBRARY_SONG;
        }else if (table==TABLE_PLAYING){
            id=KEY_PLAYING_ID;
        }

        database=this.getWritableDatabase();
        String Query = "Select * from " +table+ " where " + id+ " =" + fieldValue;
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            database.close();
            return false;
        } else {
            cursor.close();
            database.close();
            return true;
        }
    }
}
