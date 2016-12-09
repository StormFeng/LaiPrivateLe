package com.lailem.app.bean;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by XuYang on 15/11/29.
 */
public class CreateActiveCache extends Base {
    private String userId;
    private String parentId;
    private String typeId;

    private String name;
    private String picPath;
    private String picMaterial;
    private String picName;

    private String startDateTime;
    private String permission;
    private String topic;
    private AddressInfo addressInfo;
    private ArrayList<Object> intro;
    private String contactName;
    private String contactPhone;
    private String joinNeedContact;
    private String joinNeedVerify;

    public ArrayList<Object> getIntro() {
        return intro;
    }

    public void setIntro(ArrayList<Object> intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPicMaterial() {
        return picMaterial;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public void setPicMaterial(String picMaterial) {
        this.picMaterial = picMaterial;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getJoinNeedContact() {
        return joinNeedContact;
    }

    public void setJoinNeedContact(String joinNeedContact) {
        this.joinNeedContact = joinNeedContact;
    }

    public String getJoinNeedVerify() {
        return joinNeedVerify;
    }

    public void setJoinNeedVerify(String joinNeedVerify) {
        this.joinNeedVerify = joinNeedVerify;
    }


    public boolean isEmpty() {
        if (intro == null) {
            return false;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < intro.size(); i++) {
                Object object = intro.get(i);
                if (object instanceof CreateActiveIntroEditBean) {
                    CreateActiveIntroEditBean bean = (CreateActiveIntroEditBean) object;
                    sb.append(bean.getText());
                } else if (object instanceof CreateActiveIntroImageBean) {
                    CreateActiveIntroImageBean bean = (CreateActiveIntroImageBean) object;
                    sb.append("[图片]");
                }
            }
            String str = sb.toString().trim();
            if (TextUtils.isEmpty(str)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
