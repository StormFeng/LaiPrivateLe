package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * Created by XuYang on 15/10/30.
 */
public class GroupDatabaseBean extends Result {
    private ArrayList<DataBase> database;

    public static GroupDatabaseBean parse(String json) throws AppException {
        GroupDatabaseBean res = new GroupDatabaseBean();
        try {
            res = gson.fromJson(json, GroupDatabaseBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    public ArrayList<DataBase> getDatabase() {
        return database;
    }

    public void setDatabase(ArrayList<DataBase> database) {
        this.database = database;
    }

    public static class DataBase extends Base {
        private String createTime;//	发布时间	string
        private ArrayList<Data> dataList;//		array<object>

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public ArrayList<Data> getDataList() {
            return dataList;
        }

        public void setDataList(ArrayList<Data> dataList) {
            this.dataList = dataList;
        }
    }

    public static class Data extends Base {
        private String address;//	地址	string	类型为地址时，才有值
        private String duration;//	语音或视频持续的时长（单位秒）	string	当文件为语音和视频时，必填
        private String filename;//	文件名	string	地址时为截图
        private String lat;//	纬度	string	类型为地址时，才有值
        private String lon;//	经度	string	类型为地址时，才有值
        private String nickname;//	发布者昵称	string
        private String tFilename;//	文件为图片时，为缩略图	string	类型非地址时，才有值

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String gettFilename() {
            return tFilename;
        }

        public void settFilename(String tFilename) {
            this.tFilename = tFilename;
        }
    }
}
