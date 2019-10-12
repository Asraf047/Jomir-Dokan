package com.example.amanullah.myapplication50;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by AMANULLAH on 23-Sep-18.
 */

public class Polygone2 {
    private String name;
    private String price;
    private String area;
    private String address;
    private String details;
    private ArrayList<LatLng> allLatLng = new ArrayList<LatLng>();

    public Polygone2(String name, String price, String area, String address, ArrayList<LatLng> allLatLng) {
        this.name = name;
        this.price = price;
        this.area = area;
        this.address = address;
        this.allLatLng = allLatLng;
    }

    public Polygone2(String name, String price, String area, String address, String details, ArrayList<LatLng> allLatLng) {
        this.name = name;
        this.price = price;
        this.area = area;
        this.address = address;
        this.details = details;
        this.allLatLng = allLatLng;
    }

    public Polygone2(String name, String price, ArrayList<LatLng> allLatLng) {
        this.name = name;
        this.price = price;
        this.allLatLng = allLatLng;
    }

    public Polygone2(){}

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
