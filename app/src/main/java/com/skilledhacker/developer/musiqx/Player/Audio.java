package com.skilledhacker.developer.musiqx.Player;

import java.io.Serializable;

/**
 * Created by Guy on 4/17/2017.
 */

public class Audio implements Serializable {

    private int song;
    private String song_title;
    private int artist;
    private String artist_name;
    private int album;
    private String album_name;
    private int genre;
    private String genre_name;
    private int year;
    private String lyrics;
    private int license;

    public Audio(int song, String song_title, int artist, String artist_name, int album, String album_name, int genre,
                 String genre_name, int year, String lyrics, int license) {
        this.song = song;
        this.song_title = song_title;
        this.artist = artist;
        this.artist_name = artist_name;
        this.album = album;
        this.album_name = album_name;
        this.genre = genre;
        this.genre_name = genre_name;
        this.year = year;
        this.lyrics = lyrics;
        this.license = license;
    }

    public int getSong() {
        return song;
    }

    public void setSong(int song) {
        this.song = song;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public int getArtist() {
        return artist;
    }

    public void setArtist(int artist) {
        this.artist = artist;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public int getAlbum() {
        return album;
    }

    public void setAlbum(int album) {
        this.album = album;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public int getLicense() {
        return license;
    }

    public void setLicense(int license) {
        this.license = license;
    }
}
