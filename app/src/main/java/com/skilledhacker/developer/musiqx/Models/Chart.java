package com.skilledhacker.developer.musiqx.Models;

/**
 * Created by Guy on 11/12/2017.
 */

public class Chart {
    public static final String KEY_ID ="id";
    public static final String KEY_TITLE ="title";
    public static final String KEY_KEYWORD ="key_word";
    public static final String KEY_CREATED_AT ="created_at";
    public static final String KEY_UPDATED_AT ="updated_at";

    private int chart;
    private String chart_title;
    private String key_word;
    private String created_at;
    private String updated_at;

    public Chart(int chart, String chart_title, String key_word, String created_at, String updated_at) {
        this.chart = chart;
        this.chart_title = chart_title;
        this.key_word = key_word;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getChart() {
        return chart;
    }

    public void setChart(int chart) {
        this.chart = chart;
    }

    public String getChart_title() {
        return chart_title;
    }

    public void setChart_title(String chart_title) {
        this.chart_title = chart_title;
    }

    public String getKey_word() {
        return key_word;
    }

    public void setKey_word(String key_word) {
        this.key_word = key_word;
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
}
