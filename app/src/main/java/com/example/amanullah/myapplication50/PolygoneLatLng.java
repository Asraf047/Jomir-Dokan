package com.example.amanullah.myapplication50;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AMANULLAH on 23-Sep-18.
 */

public class PolygoneLatLng {
    private LatLng allLatLng;

    public PolygoneLatLng(LatLng allLatLng) {
        this.allLatLng = allLatLng;
    }

    public PolygoneLatLng(){}

    public LatLng getAllLatLng() {
        return allLatLng;
    }

    public void setAllLatLng(LatLng allLatLng) {
        this.allLatLng = allLatLng;
    }

}
