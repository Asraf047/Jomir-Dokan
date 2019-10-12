package com.example.amanullah.myapplication50;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AMANULLAH on 13-Nov-18.
 */

public class SearchUser {
    private String key;
    private String name;
    private String email;
    private String area;
    private String price;
    private String type;
    private String address;
    private String distance;
    private LatLng cent;
    private int counts;

    public SearchUser(){

    }

    public SearchUser(String key, String name, String email, String area, String price, String type, String address, LatLng cent, int counts) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.area = area;
        this.price = price;
        this.type = type;
        this.address = address;
        this.cent = cent;
        this.counts = counts;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LatLng getCent() {
        return cent;
    }

    public void setCent(LatLng cent) {
        this.cent = cent;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
