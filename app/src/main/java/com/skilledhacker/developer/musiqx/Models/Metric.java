package com.skilledhacker.developer.musiqx.Models;

import java.io.Serializable;

/**
 * Created by Guy on 7/17/2017.
 */

public class Metric implements Serializable {
    private int song;
    private int play;
    private int skip;
    private int rating;
    private String created_at;
    private String updated_at;
    private String status;

    public Metric(int song, String updated_at, String status) {
        this.song = song;
        this.updated_at = updated_at;
        this.status = status;
    }

    public Metric(int song, int play, int skip, int rating, String created_at, String updated_at) {
        this.song = song;
        this.play = play;
        this.skip = skip;
        this.rating = rating;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getSong() {
        return song;
    }

    public void setSong(int song) {
        this.song = song;
    }

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
