package com.cookandroid.smartmirror;

public class wifiData {
    private int imgSrc;
    private String wifiName;

    public wifiData(int imgSrc, String wifiName){
        this.imgSrc = imgSrc;
        this.wifiName = wifiName;
    }
    public int getImgSrc(){
        return this.imgSrc;
//        return (this.isLock)? true : false;
    }
    public String getWifiName(){
        return this.wifiName;
    }
}
