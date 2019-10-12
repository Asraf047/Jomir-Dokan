package com.example.amanullah.myapplication50;

/**
 * Created by AMANULLAH on 05-Nov-18.
 */

public class User {
    private String key;
    private int polyCount;
    private int markerCount;

    public User(){

    }

    public User(String key, int polyCount, int markerCount) {
        this.key = key;
        this.polyCount = polyCount;
        this.markerCount = markerCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPolyCount() {
        return polyCount;
    }

    public void setPolyCount(int polyCount) {
        this.polyCount = polyCount;
    }

    public int getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(int markerCount) {
        this.markerCount = markerCount;
    }
}
