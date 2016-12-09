package com.lailem.app.share_ex.wechat.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lailem.app.bean.Base;

/**
 * Created by XuYang on 15/9/25.
 * 用户解析微信返回的用户信息
 */
public class WeixinUser  extends Base {
    private String errcode;
    private String errmsg;
    private String openid;//OPENID
    private String nickname;//NICKNAME",
    private String sex;//1
    private String province;//PROVINCE",
    private String city;//CITY",
    private String country;//COUNTRY",
    private String headimgurl;//http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
    private String unionid;//


    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }


    public static WeixinUser parse(String json) throws JsonSyntaxException {
        WeixinUser res = new WeixinUser();
        try {
            res = new Gson().fromJson(json, WeixinUser.class);
        } catch (JsonSyntaxException e) {
            throw e;
        }
        return res;
    }

    @Override
    public String toString() {
        return "WeixinUser{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
