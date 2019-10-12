package com.example.amanullah.myapplication50;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AMANULLAH on 24-Sep-18.
 */

public class PolygoneLatLng2 {
    private double latitude;
    private double longitude;

    public PolygoneLatLng2(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PolygoneLatLng2(){}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
