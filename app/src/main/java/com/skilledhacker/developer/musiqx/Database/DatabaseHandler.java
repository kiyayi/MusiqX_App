package com.skilledhacker.developer.musiqx.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.Models.Metric;

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
    private Context context;
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="database.db";
    private static final DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");

    //STATUS
    public static final String KEY_STATUS ="status";
    public static final String STATUS_OK="ok";
    public static final String STATUS_CREATED="created";
    public static final String STATUS_UPDATED="updated";
    public static final String STATUS_DELETED="deleted";

    //TABLES
    public static final String TABLE_ACCOUNT="account";
    public static final String TABLE_LIBRARY="library";
    public static final String TABLE_PLAYING="playing";
    public static final String TABLE_METRIC="metric";

    //PLAYING KEY
    public static final String KEY_PLAYING_ID="id";
    public static final String KEY_PLAYING_POS="current_position";
    public static final String KEY_PLAYING_LENGTH="length";

    //ACCOUNT KEYS
    public static final String KEY_ACCOUNT_ID="id";
    public static final String KEY_ACCOUNT_TOKEN="token";
    public static final String KEY_ACCOUNT_USER_ID="user_id";
    public static final String KEY_ACCOUNT_USERNAME="username";
    public static final String KEY_ACCOUNT_EMAIL="email";
    public static final String KEY_ACCOUNT_FIRST_NAME="first_name";
    public static final String KEY_ACCOUNT_LAST_NAME="last_name";
    public static final String KEY_ACCOUNT_AGE="age";
    public static final String KEY_ACCOUNT_GENDER="gender";
    public static final String KEY_ACCOUNT_ADDRESS="address";
    public static final String KEY_ACCOUNT_COUNTRY="country";
    public static final String KEY_ACCOUNT_LANGUAGE="language";
    public static final String KEY_ACCOUNT_LICENSE="license";
    public static final String KEY_ACCOUNT_LICENSE_NAME="license_name";
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

    //METRIC KEYS
    public static final String KEY_METRIC_SONG="song";
    public static final String KEY_METRIC_PLAY="play";
    public static final String KEY_METRIC_SKIP="skip";
    public static final String KEY_METRIC_RATING="rating";
    public static final String KEY_METRIC_CREATED_AT="created_at";
    public static final String KEY_METRIC_UPDATED_AT="updated_at";

    //BROADCASTS LIBRARY
    public static final String BROADCAST_LIBRARY_CREATED="com.skilledhacker.developer.musiqx.database.library.created";
    public static final String BROADCAST_LIBRARY_UPDATED="com.skilledhacker.developer.musiqx.database.library.updated";
    public static final String BROADCAST_LIBRARY_DELETED="com.skilledhacker.developer.musiqx.database.library.deleted";

    //BROADCASTS METRIC
    public static final String BROADCAST_METRIC_CREATED="com.skilledhacker.developer.musiqx.database.metric.created";
    public static final String BROADCAST_METRIC_UPDATED="com.skilledhacker.developer.musiqx.database.metric.updated";
    public static final String BROADCAST_METRIC_DELETED="com.skilledhacker.developer.musiqx.database.metric.deleted";

    //BROADCASTS ACCOUNT
    public static final String BROADCAST_ACCOUNT_CREATED="com.skilledhacker.developer.musiqx.database.account.created";
    public static final String BROADCAST_ACCOUNT_UPDATED="com.skilledhacker.developer.musiqx.database.account.updated";
    public static final String BROADCAST_ACCOUNT_DELETED="com.skilledhacker.developer.musiqx.database.account.deleted";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
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
                +KEY_LIBRARY_UPDATED_AT+ " VARCHAR(100), "
                +KEY_STATUS + " VARCHAR(100) );";

        String CREATE_TABLE_METRIC = "CREATE TABLE " +TABLE_METRIC+ "("
                +KEY_METRIC_SONG + " INTEGER,"
                +KEY_METRIC_PLAY + " INTEGER,"
                +KEY_METRIC_SKIP + " INTEGER,"
                +KEY_METRIC_RATING + " INTEGER,"
                +KEY_METRIC_CREATED_AT+ " VARCHAR(100), "
                +KEY_METRIC_UPDATED_AT+ " VARCHAR(100), "
                + KEY_STATUS + " VARCHAR(100) );";

        String CREATE_TABLE_PLAYING = "CREATE TABLE " +TABLE_PLAYING+ "("
                +KEY_PLAYING_ID + " INTEGER,"
                +KEY_PLAYING_POS + " INTEGER,"
                +KEY_PLAYING_LENGTH + " INTEGER );";

        String CREATE_TABLE_ACCOUNT = "CREATE TABLE " +TABLE_ACCOUNT+ "("
                +KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY,"
                +KEY_ACCOUNT_TOKEN+ " VARCHAR(255), "
                +KEY_ACCOUNT_USER_ID+ " INTEGER, "
                +KEY_ACCOUNT_USERNAME+ " VARCHAR(150), "
                +KEY_ACCOUNT_EMAIL+ " VARCHAR(150), "
                +KEY_ACCOUNT_FIRST_NAME+ " VARCHAR(150), "
                +KEY_ACCOUNT_LAST_NAME+ " VARCHAR(150), "
                +KEY_ACCOUNT_AGE+ " INTEGER, "
                +KEY_ACCOUNT_GENDER+ " VARCHAR(50), "
                +KEY_ACCOUNT_ADDRESS+ " VARCHAR(255), "
                +KEY_ACCOUNT_COUNTRY+ " VARCHAR(100), "
                +KEY_ACCOUNT_LANGUAGE+ " VARCHAR(100), "
                +KEY_ACCOUNT_LICENSE+ " INTEGER, "
                +KEY_ACCOUNT_LICENSE_NAME+ " VARCHAR(100), "
                +KEY_ACCOUNT_DATE + " VARCHAR(100) );";

        database.execSQL(CREATE_TABLE_ACCOUNT);
        database.execSQL(CREATE_TABLE_METRIC);
        database.execSQL(CREATE_TABLE_LIBRARY);
        database.execSQL(CREATE_TABLE_PLAYING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        database=db;

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYING);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_METRIC);

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
        send_broadcast(BROADCAST_ACCOUNT_CREATED);
        database.close();
    }

    public void update_account(int id,int user_id,String username,String email,String fname,String lname,int age,String gender
            ,String address,String country,String language,int license,String license_name){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_ACCOUNT_USER_ID, user_id);
        values.put(KEY_ACCOUNT_USERNAME, username);
        values.put(KEY_ACCOUNT_EMAIL, email);
        values.put(KEY_ACCOUNT_FIRST_NAME, fname);
        values.put(KEY_ACCOUNT_LAST_NAME, lname);
        values.put(KEY_ACCOUNT_AGE, age);
        values.put(KEY_ACCOUNT_GENDER, gender);
        values.put(KEY_ACCOUNT_ADDRESS, address);
        values.put(KEY_ACCOUNT_COUNTRY, country);
        values.put(KEY_ACCOUNT_LANGUAGE, language);
        values.put(KEY_ACCOUNT_LICENSE, license);
        values.put(KEY_ACCOUNT_LICENSE_NAME, license_name);
        database.update(TABLE_ACCOUNT, values, KEY_ACCOUNT_ID + "=?", new String[]{String.valueOf(1)});
        send_broadcast(BROADCAST_ACCOUNT_UPDATED);
        database.close();
    }

    public void delete_account(int id){
        database=getWritableDatabase();
        database.delete(TABLE_ACCOUNT, KEY_ACCOUNT_ID + "=?", new String[]{String.valueOf(id)});
        send_broadcast(BROADCAST_ACCOUNT_DELETED);
        database.close();
    }

    public String retrieve_account_token(){
        String result="";
        String query = "SELECT * FROM "+TABLE_ACCOUNT;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        result = cursor.getString(1);
        cursor.close();
        database.close();
        return result;
    }

    public String retrieve_account_date(){
        String result="";
        String query = "SELECT * FROM "+TABLE_ACCOUNT;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        result = cursor.getString(14);
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
                               String lyrics,String created_at,String updated_at,boolean is_user){

        if (CheckIsDataAlreadyInDBorNot(song,TABLE_LIBRARY)){
            return;
        }
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
        if (is_user) values.put(KEY_STATUS, STATUS_CREATED);
        else values.put(KEY_STATUS, STATUS_OK);
        database.insert(TABLE_LIBRARY, null, values);
        send_broadcast(BROADCAST_LIBRARY_CREATED);
        database.close();
    }

    public void update_library(int song,String song_title,int artist,String artist_name,int album,String album_name,
                               int genre,String genre_name,int year,int license,String license_name,
                               String lyrics,String created_at,String updated_at,boolean is_user){
        if (!CheckIsDataAlreadyInDBorNot(song,TABLE_LIBRARY)){
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
            if (is_user) values.put(KEY_STATUS, STATUS_CREATED);
            else values.put(KEY_STATUS, STATUS_OK);
            database.insert(TABLE_LIBRARY, null, values);
            send_broadcast(BROADCAST_LIBRARY_CREATED);
            database.close();
        }else {
            database=getWritableDatabase();
            ContentValues values=new ContentValues();
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
            values.put(KEY_LIBRARY_UPDATED_AT, updated_at);
            if (is_user) values.put(KEY_STATUS, STATUS_CREATED);
            else values.put(KEY_STATUS, STATUS_OK);
            database.update(TABLE_LIBRARY, values, KEY_LIBRARY_SONG + "=?", new String[]{String.valueOf(song)});
            send_broadcast(BROADCAST_LIBRARY_UPDATED);
            database.close();
        }
    }

    public void delete_library(int song,boolean is_user){
        database=getWritableDatabase();
        if (is_user){
            ContentValues values=new ContentValues();
            values.put(KEY_STATUS, STATUS_DELETED);
            database.update(TABLE_LIBRARY, values, KEY_LIBRARY_SONG + "=?", new String[]{String.valueOf(song)});
        }else {
            database.delete(TABLE_LIBRARY, KEY_LIBRARY_SONG + "=?", new String[]{String.valueOf(song)});
        }
        send_broadcast(BROADCAST_LIBRARY_DELETED);
        database.close();
    }

    public ArrayList<Audio> retrieve_library(){
        ArrayList<Audio> audioList=new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_LIBRARY+" WHERE "+KEY_STATUS+" = '"+STATUS_OK+"' OR "+KEY_STATUS+" = '"+STATUS_CREATED+"'";
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            int song = cursor.getInt(0);
            String song_title = cursor.getString(1);
            int artist = cursor.getInt(2);
            String artist_name = cursor.getString(3);
            int album = cursor.getInt(4);
            String album_name = cursor.getString(5);
            int genre = cursor.getInt(6);
            String genre_name = cursor.getString(7);
            int year = cursor.getInt(8);
            String lyrics = cursor.getString(11);
            int license = cursor.getInt(9);
            String created_at = cursor.getString(12);
            String updated_at = cursor.getString(13);

            audioList.add(new Audio(song, song_title, artist, artist_name, album, album_name,
                    genre, genre_name, year, lyrics, license,created_at,updated_at));
        }

        cursor.close();
        database.close();
        return audioList;

    }

    public ArrayList<Audio> retrieve_library_change() {
        ArrayList<Audio> audioList=new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_LIBRARY+" WHERE "+KEY_STATUS+" = '"+STATUS_DELETED+"' OR "+KEY_STATUS+" = '"+STATUS_CREATED+"'";
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            int song = cursor.getInt(0);
            String updated_at = cursor.getString(13);
            String status = cursor.getString(14);

            audioList.add(new Audio(song,updated_at,status));
        }

        cursor.close();
        database.close();
        return audioList;
    }

    //CRUD FOR METRIC
    public void insert_metric(int song,int play,int skip, int rating,String created_at,String updated_at,boolean is_user){
        if (CheckIsDataAlreadyInDBorNot(song,TABLE_METRIC)){
            return;
        }

        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_METRIC_SONG, song);
        values.put(KEY_METRIC_PLAY, play);
        values.put(KEY_METRIC_SKIP, skip);
        values.put(KEY_METRIC_RATING, rating);
        values.put(KEY_METRIC_CREATED_AT, created_at);
        values.put(KEY_METRIC_UPDATED_AT, updated_at);
        if (is_user) values.put(KEY_STATUS, STATUS_CREATED);
        else values.put(KEY_STATUS, STATUS_OK);
        database.insert(TABLE_METRIC, null, values);
        send_broadcast(BROADCAST_METRIC_CREATED);
        database.close();
    }

    public void update_metric(int song,int play,int skip, int rating,String created_at,String updated_at,boolean is_user){
        if (!CheckIsDataAlreadyInDBorNot(song,TABLE_METRIC)){
            database=getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(KEY_METRIC_SONG, song);
            values.put(KEY_METRIC_PLAY, play);
            values.put(KEY_METRIC_SKIP, skip);
            values.put(KEY_METRIC_RATING, rating);
            values.put(KEY_METRIC_CREATED_AT, created_at);
            values.put(KEY_METRIC_UPDATED_AT, updated_at);
            if (is_user) values.put(KEY_STATUS, STATUS_CREATED);
            else values.put(KEY_STATUS, STATUS_OK);
            database.insert(TABLE_METRIC, null, values);
            send_broadcast(BROADCAST_METRIC_CREATED);
            database.close();
        }else {
            database=getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(KEY_METRIC_PLAY, play);
            values.put(KEY_METRIC_SKIP, skip);
            values.put(KEY_METRIC_RATING, rating);
            values.put(KEY_METRIC_UPDATED_AT, updated_at);
            if (is_user) values.put(KEY_STATUS, STATUS_UPDATED);
            else values.put(KEY_STATUS, STATUS_OK);
            database.update(TABLE_METRIC, values, KEY_METRIC_SONG + "=?", new String[]{String.valueOf(song)});
            send_broadcast(BROADCAST_METRIC_UPDATED);
            database.close();
        }
    }

    public void delete_metric(int song){
        database=getWritableDatabase();
        database.delete(TABLE_METRIC, KEY_METRIC_SONG + "=?", new String[]{String.valueOf(song)});
        send_broadcast(BROADCAST_METRIC_DELETED);
        database.close();
    }

    public ArrayList<Metric> retrieve_metric(){
        ArrayList<Metric> list=new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_METRIC+" WHERE "+KEY_STATUS+" = '"+STATUS_OK+"' OR "+KEY_STATUS+" = '"+STATUS_CREATED+"' OR "+KEY_STATUS+" = '"+STATUS_UPDATED+"'";
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            int song = cursor.getInt(0);
            int play = cursor.getInt(1);
            int skip = cursor.getInt(2);
            int rating = cursor.getInt(3);
            String created_at = cursor.getString(4);
            String updated_at = cursor.getString(5);

            list.add(new Metric(song, play, skip, rating,created_at,updated_at));
        }

        cursor.close();
        database.close();
        return list;

    }

    public ArrayList<Metric> retrieve_metric_change() {
        ArrayList<Metric> list=new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_METRIC+" WHERE "+KEY_STATUS+" = '"+STATUS_DELETED+"' OR "+KEY_STATUS+" = '"+STATUS_CREATED+"' OR "+KEY_STATUS+" = '"+STATUS_UPDATED+"'";
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            int song = cursor.getInt(0);
            String updated_at = cursor.getString(5);
            String status = cursor.getString(6);

            list.add(new Metric(song,updated_at,status));
        }

        cursor.close();
        database.close();
        return list;
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
        }else if (table==TABLE_METRIC){
            id=KEY_METRIC_SONG;
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

    private void send_broadcast(String broadcast){
        Intent intent=new Intent();
        intent.setAction(broadcast);
        context.sendBroadcast(intent);
    }
}
