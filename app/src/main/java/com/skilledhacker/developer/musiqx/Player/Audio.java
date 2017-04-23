package com.skilledhacker.developer.musiqx.Player;

import java.io.Serializable;

/**
 * Created by Guy on 4/17/2017.
 */

public class Audio implements Serializable {

    private int data;
    private String title;
    private String album;
    private String artist;

    public Audio(int data, String title, String album, String artist) {
        this.data = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
