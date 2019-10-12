package com.example.amanullah.myapplication50;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by AMANULLAH on 23-Sep-18.
 */

public class PolyLatLng {
    private String name;
    private ArrayList<LatLng> allLatLng = new ArrayList<LatLng>();

    public PolyLatLng(String name, ArrayList<LatLng> allLatLng) {
        this.name = name;
        this.allLatLng = allLatLng;
    }

    public PolyLatLng(){}

    public String getName() {
        return name;
    }

    public ArrayList<LatLng> getAllLatLng() {
        return allLatLng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAllLatLng(ArrayList<LatLng> allLatLng) {
        this.allLatLng = allLatLng;
    }

}
