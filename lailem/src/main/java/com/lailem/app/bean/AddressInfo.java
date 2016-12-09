package com.lailem.app.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by XuYang on 15/10/14.
 */
public class AddressInfo implements Serializable{
    private String lat;
    private String lng;
    private String address;

    public AddressInfo(String lat, String lng, String address) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 是否可用
     * @return
     */
    public boolean isValid(){
        return !TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(lng)&&!TextUtils.isEmpty(address);
    }
}
