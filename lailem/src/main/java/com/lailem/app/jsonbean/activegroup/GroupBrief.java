package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

public class GroupBrief extends Result {
    private GroupInfo groupInfo;

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public static GroupBrief parse(String json) throws AppException {
        GroupBrief res = new GroupBrief();
        try {
            res = gson.fromJson(json, GroupBrief.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }


    public class GroupInfo {
        private String id;
        private String name;
        private String intro;
        private String squareSPicName;
        private String typeId;
        private String address;
        private String startTime;
        private String permission;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }

        public String getPermission() {
            return permission;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getSquareSPicName() {
            return squareSPicName;
        }

        public void setSquareSPicName(String squareSPicName) {
            this.squareSPicName = squareSPicName;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }


    }

}
