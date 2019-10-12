package com.example.amanullah.myapplication50;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by AMANULLAH on 21-Aug-18.
 */

public class Polygone {
    private PolygonOptions polygonOptions;
    private LatLng center;
    private String name;
    private static int counter = 0;

    public Polygone (PolygonOptions polygonOptions, LatLng position, String name) {
        this.polygonOptions = polygonOptions;
        center = position;
        this.name = name;
        counter++;
    }

    public Polygone() {

    }

    public static int getCounter() {
        return counter;
    }

    public PolygonOptions getPolygonOptions() {
        return polygonOptions;
    }

    public void setPolygonOptions(PolygonOptions polygonOptions) {
        this.polygonOptions = polygonOptions;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
