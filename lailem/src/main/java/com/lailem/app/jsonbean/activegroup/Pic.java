package com.lailem.app.jsonbean.activegroup;

import java.io.Serializable;

public class Pic implements Serializable {
    private String filename;
    private String tFilename;
    private String gFilename;
    //下面四个字段用于发表时上传图片
    private String w;
    private String h;
    private String tw;
    private String th;
    //----------------------
    public String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getgFilename() {
        return gFilename;
    }

    public void setgFilename(String gFilename) {
        this.gFilename = gFilename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String gettFilename() {
        return tFilename;
    }

    public void settFilename(String tFilename) {
        this.tFilename = tFilename;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getTw() {
        return tw;
    }

    public void setTw(String tw) {
        this.tw = tw;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }


}