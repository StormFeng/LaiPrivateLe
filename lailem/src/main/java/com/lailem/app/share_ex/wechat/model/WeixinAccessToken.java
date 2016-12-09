package com.lailem.app.share_ex.wechat.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lailem.app.bean.Base;

/**
 * Created by XuYang on 15/9/25.
 * 用户解析微信返回的token
 */
public class WeixinAccessToken extends Base {
    private String errcode;
    private String errmsg;
    private String access_token;//ACCESS_TOKEN
    private int expires_in;//7200,
    private String refresh_token;//REFRESH_TOKEN
    private String openid;//OPENID
    private String scope;//SCOPE
    private String unionid;//o6_bmasdasdsad6_2sgVt7hMZOPfL

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

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public static WeixinAccessToken parse(String json) throws JsonSyntaxException {
        WeixinAccessToken res = new WeixinAccessToken();
        try {
            res = new Gson().fromJson(json, WeixinAccessToken.class);
        } catch (JsonSyntaxException e) {
            throw e;
        }
        return res;
    }
}
