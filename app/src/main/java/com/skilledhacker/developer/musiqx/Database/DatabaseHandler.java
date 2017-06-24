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

    public static final String TABLE_USER="user";
    public static final String TABLE_LIBRARY="library";
    public static final String TABLE_MUSIC="music";
    public static final String TABLE_RATING="rating";
    public static final String TABLE_PLAYING="playing";

    //PLAYING KEY
    public static final String KEY_PLAYING_ID="id";

    //USER KEYS
    public static final String KEY_USER_ID="id";
    public static final String KEY_USER_EMAIL="email";
    public static final String KEY_USER_FNAME="f_name";
    public static final String KEY_USER_LNAME="l_name";
    public static final String KEY_USER_DATE="date_joined";
    public static final String KEY_USER_COUNTRY="country";
    public static final String KEY_USER_ADDRESS="address";
    public static final String KEY_USER_LICENCE="licence";
    public static final String KEY_USER_AGE="age";
    public static final String KEY_USER_GENDER="gender";

    //LIBRARY KEYS
    public static final String KEY_LIBRARY_ID="id";
    public static final String KEY_LIBRARY_USER_ID="user";
    public static final String KEY_LIBRARY_MUSIC_ID="music";
    public static final String KEY_LIBRARY_DATE_ADDED="date";
    public static final String KEY_LIBRARY_PLAY_COUNT="play";
    public static final String KEY_LIBRARY_SKIP_COUNT="skip";

    //MUSIC KEYS
    public static final String KEY_MUSIC_ID="id";
    public static final String KEY_MUSIC_LICENCE="license";
    public static final String KEY_MUSIC_TITLE="title";
    public static final String KEY_MUSIC_ARTIST="artist";
    public static final String KEY_MUSIC_ALBUM="album";
    public static final String KEY_MUSIC_GENRE="genre";
    public static final String KEY_MUSIC_YEAR="year";

    //RATING KEYS
    public static final String KEY_RATING_ID="id";
    public static final String KEY_RATING_USER_ID="user";
    public static final String KEY_RATING_MUSIC_ID="music";
    public static final String KEY_RATING_RATING="rating";

    private static final DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
    //TABLES
    public static final String TABLE_ACCOUNT="account";

    //ACCOUNT KEYS
    public static final String KEY_ACCOUNT_ID="id";
    public static final String KEY_ACCOUNT_TOKEN="token";
    public static final String KEY_ACCOUNT_DATE="date";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database=db;

        String CREATE_TABLE_USER = "CREATE TABLE " +TABLE_USER+ " ("
                +KEY_USER_ID + " INTEGER PRIMARY KEY,"
                +KEY_USER_EMAIL+ " VARCHAR(50), "
                +KEY_USER_FNAME+ " VARCHAR(50), "
                +KEY_USER_LNAME+ " VARCHAR(50), "
                +KEY_USER_AGE+ " INTEGER, "
                +KEY_USER_GENDER+ " INTEGER, "
                +KEY_USER_ADDRESS+ " VARCHAR(100), "
                +KEY_USER_COUNTRY+ " VARCHAR(50), "
                +KEY_USER_DATE+ " VARCHAR(100), "
                +KEY_USER_LICENCE + " INTEGER );";

        String CREATE_TABLE_MUSIC = "CREATE TABLE " +TABLE_MUSIC+ "("
                +KEY_MUSIC_ID + " INTEGER PRIMARY KEY,"
                +KEY_MUSIC_LICENCE+ " INTEGER, "
                +KEY_MUSIC_TITLE+ " VARCHAR(100), "
                +KEY_MUSIC_ARTIST+ " VARCHAR(100), "
                +KEY_MUSIC_ALBUM+ " VARCHAR(100), "
                +KEY_MUSIC_GENRE+ " VARCHAR(100), "
                +KEY_MUSIC_YEAR + " INTEGER );";

        String CREATE_LIBRARY_USER = "CREATE TABLE " +TABLE_LIBRARY+ "("
                +KEY_LIBRARY_ID + " INTEGER PRIMARY KEY,"
                +KEY_LIBRARY_USER_ID+ " INTEGER, "
                +KEY_LIBRARY_MUSIC_ID+ " INTEGER, "
                +KEY_LIBRARY_DATE_ADDED+ " VARCHAR(100), "
                +KEY_LIBRARY_PLAY_COUNT+ " INTEGER, "
                +KEY_LIBRARY_SKIP_COUNT + " INTEGER, "
                + "FOREIGN KEY(" + KEY_LIBRARY_USER_ID + ") REFERENCES "
                + TABLE_USER + "("+KEY_USER_ID+"), "
                + "FOREIGN KEY(" + KEY_LIBRARY_MUSIC_ID + ") REFERENCES "
                + TABLE_MUSIC + "("+KEY_MUSIC_ID+") " + ");";

        String CREATE_TABLE_RATING = "CREATE TABLE " +TABLE_RATING+ "("
                +KEY_RATING_ID + " INTEGER PRIMARY KEY,"
                +KEY_RATING_USER_ID+ " INTEGER, "
                +KEY_RATING_MUSIC_ID+ " INTEGER, "
                +KEY_RATING_RATING + " INTEGER, "
                + "FOREIGN KEY(" + KEY_RATING_USER_ID + ") REFERENCES "
                + TABLE_USER + "("+KEY_USER_ID+"), "
                + "FOREIGN KEY(" + KEY_RATING_MUSIC_ID + ") REFERENCES "
                + TABLE_MUSIC + "("+KEY_MUSIC_ID+") " + ");";

        String CREATE_TABLE_PLAYING = "CREATE TABLE " +TABLE_PLAYING+ "("
                +KEY_PLAYING_ID + " INTEGER );";

        String CREATE_TABLE_ACCOUNT = "CREATE TABLE " +TABLE_ACCOUNT+ "("
                +KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY,"
                +KEY_ACCOUNT_TOKEN+ " VARCHAR(255), "
                +KEY_ACCOUNT_DATE + " VARCHAR(100) );";

        database.execSQL(CREATE_TABLE_ACCOUNT);

        database.execSQL(CREATE_TABLE_USER);
        database.execSQL(CREATE_TABLE_MUSIC);
        database.execSQL(CREATE_LIBRARY_USER);
        database.execSQL(CREATE_TABLE_RATING);
        database.execSQL(CREATE_TABLE_PLAYING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        database=db;

        // Drop older table if existed, all data will be gone!!!
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYING);

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);

        // Create tables again
        onCreate(database);
    }

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

    private long getNumberOfRows(String table) {
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








    public void insert_playing(int id){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_PLAYING_ID, id);
        database.insert(TABLE_PLAYING, null, values);
        database.close();
    }

    public void insert_rating(int id,int userId,int musicId,int rating){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_RATING_ID, id);
        values.put(KEY_RATING_USER_ID, userId);
        values.put(KEY_RATING_MUSIC_ID, musicId);
        values.put(KEY_RATING_RATING, rating);
        database.insert(TABLE_RATING, null, values);
        database.close();
    }

    public void insert_library(int id,int userId,int musicId,String date,int play,int skip){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_LIBRARY_ID, id);
        values.put(KEY_LIBRARY_USER_ID, userId);
        values.put(KEY_LIBRARY_MUSIC_ID, musicId);
        values.put(KEY_LIBRARY_DATE_ADDED, date);
        values.put(KEY_LIBRARY_PLAY_COUNT, play);
        values.put(KEY_LIBRARY_SKIP_COUNT, skip);
        database.insert(TABLE_LIBRARY, null, values);
        database.close();
    }

    public void insert_music(int id,int licence,String title,String artist,String album,String genre,int year){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_MUSIC_ID, id);
        values.put(KEY_MUSIC_LICENCE, licence);
        values.put(KEY_MUSIC_TITLE, title);
        values.put(KEY_MUSIC_ARTIST, artist);
        values.put(KEY_MUSIC_ALBUM, album);
        values.put(KEY_MUSIC_GENRE, genre);
        values.put(KEY_MUSIC_YEAR, year);
        database.insert(TABLE_MUSIC, null, values);
        database.close();
    }

    public void insert_user(int id,String email,String fname,String lname,String date,String country,String address,int licence,int age,String gender){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_MUSIC_ID, id);
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_FNAME, fname);
        values.put(KEY_USER_LNAME, lname);
        values.put(KEY_USER_AGE, age);
        values.put(KEY_USER_GENDER, gender);
        values.put(KEY_USER_ADDRESS, address);
        values.put(KEY_USER_COUNTRY, country);
        values.put(KEY_USER_DATE, date);
        values.put(KEY_USER_LICENCE, licence);
        database.insert(TABLE_USER, null, values);
        database.close();
    }

    public void delete_playing(int id){
        database=getWritableDatabase();
        database.delete(TABLE_PLAYING, KEY_PLAYING_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void delete_user(int id){
        database=getWritableDatabase();
        database.delete(TABLE_USER, KEY_USER_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void delete_music(int id){
        database=getWritableDatabase();
        database.delete(TABLE_MUSIC, KEY_MUSIC_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void delete_library(int id){
        database=getWritableDatabase();
        database.delete(TABLE_LIBRARY, KEY_LIBRARY_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void delete_rating(int id){
        database=getWritableDatabase();
        database.delete(TABLE_RATING, KEY_RATING_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void update_playing(int id){
        long oldId=retrieve_playing();
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_PLAYING_ID, id);
        database.update(TABLE_PLAYING, values, KEY_PLAYING_ID + "=?", new String[]{String.valueOf(oldId)});
        database.close();
    }

    public void update_user(int id,String email,String fname,String lname,String date,String country,String address,int licence,int age,String gender){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_FNAME, fname);
        values.put(KEY_USER_LNAME, lname);
        values.put(KEY_USER_AGE, age);
        values.put(KEY_USER_GENDER, gender);
        values.put(KEY_USER_ADDRESS, address);
        values.put(KEY_USER_COUNTRY, country);
        values.put(KEY_USER_DATE, date);
        values.put(KEY_USER_LICENCE, licence);
        database.update(TABLE_USER, values, KEY_USER_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void update_music(int id,int licence,String title,String artist,String album,String genre,int year){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_MUSIC_LICENCE, licence);
        values.put(KEY_MUSIC_TITLE, title);
        values.put(KEY_MUSIC_ARTIST, artist);
        values.put(KEY_MUSIC_ALBUM, album);
        values.put(KEY_MUSIC_GENRE, genre);
        values.put(KEY_MUSIC_YEAR, year);
        database.update(TABLE_MUSIC, values, KEY_MUSIC_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void update_library(int id,int userId,int musicId,String date,int play,int skip){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_LIBRARY_USER_ID, userId);
        values.put(KEY_LIBRARY_MUSIC_ID, musicId);
        values.put(KEY_LIBRARY_DATE_ADDED, date);
        values.put(KEY_LIBRARY_PLAY_COUNT, play);
        values.put(KEY_LIBRARY_SKIP_COUNT, skip);
        database.update(TABLE_LIBRARY, values, KEY_LIBRARY_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void update_rating(int id,int userId,int musicId,int rating){
        database=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_RATING_USER_ID, userId);
        values.put(KEY_RATING_MUSIC_ID, musicId);
        values.put(KEY_RATING_RATING, rating);
        database.update(TABLE_RATING, values, KEY_RATING_ID + "=?", new String[]{String.valueOf(id)});
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

    public String[] retrieve_user(){
        String[] result=new String[10];
        String query = "SELECT * FROM "+TABLE_USER;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){

            Log.d("VALUEid", "" + cursor.getInt(0));
            Log.d("VALUEemail",""+cursor.getString(1));

            result[0]=String.valueOf(cursor.getInt(0));
            result[1]=cursor.getString(1);
            result[2]=cursor.getString(2);
            result[3]=cursor.getString(3);
            result[4]=String.valueOf(cursor.getInt(4));
            result[5]=String.valueOf(cursor.getInt(5));
            result[6]=cursor.getString(6);
            result[7]=cursor.getString(7);
            result[8]=cursor.getString(8);
            result[9]=String.valueOf(cursor.getInt(9));

        }

        cursor.close();
        database.close();
        return result;
    }

    public ArrayList<Audio> retrieve_music(){
        ArrayList<Audio> audioList=new ArrayList<>();
        String query = "SELECT "+KEY_LIBRARY_MUSIC_ID+","+KEY_MUSIC_LICENCE+","+KEY_MUSIC_TITLE+","+KEY_MUSIC_ARTIST+","+KEY_MUSIC_ALBUM+","
                +KEY_MUSIC_GENRE+","+KEY_MUSIC_YEAR+","+KEY_LIBRARY_PLAY_COUNT+","+KEY_LIBRARY_SKIP_COUNT+" FROM " + TABLE_MUSIC + " INNER JOIN " + TABLE_LIBRARY
                + " ON "+TABLE_MUSIC+"." + KEY_MUSIC_ID + " = "+TABLE_LIBRARY+"." + KEY_LIBRARY_MUSIC_ID;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){

            Log.d("VALUEid", "" + cursor.getInt(0));
            Log.d("VALUElicense",""+cursor.getInt(1));
            Log.d("VALUEtitle",""+cursor.getString(2));
            Log.d("VALUEartist",""+cursor.getString(3));
            Log.d("VALUEalbum",""+cursor.getString(4));
            Log.d("VALUEgenre",""+cursor.getString(5));
            Log.d("VALUEyear",""+cursor.getInt(6));
            Log.d("VALUEplay",""+cursor.getInt(7));
            Log.d("VALUEskip",""+cursor.getInt(8));

            int data = cursor.getInt(0);
            String title = cursor.getString(2);
            String album = cursor.getString(4);
            String artist = cursor.getString(3);

            // Save to audioList
            audioList.add(new Audio(data, title, album, artist));
        }

        cursor.close();
        database.close();
        return audioList;

    }

    public int GetMusicId(String title){
        int result=-2;
        String query = "SELECT "+KEY_MUSIC_ID+" FROM "+TABLE_MUSIC+" WHERE "+KEY_MUSIC_TITLE+"="+title;
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
           result=cursor.getInt(0);
        }

        cursor.close();
        database.close();
        return result;
    }

    public long getNumberOfRows_music() {
        database = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(database, TABLE_MUSIC);
        database.close();
        return cnt;
    }

    public boolean CheckIsDataAlreadyInDBorNot(int fieldValue,String table) {
        String id="";
        if (table==TABLE_MUSIC){
            id=KEY_MUSIC_ID;
        }else if (table==TABLE_LIBRARY){
            id=KEY_LIBRARY_ID;
        }else if (table==TABLE_USER){
            id=KEY_USER_ID;
        }else if (table==TABLE_RATING){
            id=KEY_RATING_ID;
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
