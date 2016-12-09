package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 活动与群组-取Group申请验证方式
 *
 * @author MD-12
 */
public class GroupVerifyWayBean extends Result {

    private String verifyWay;// 验证方式 string 1（文本）、2（语音）
    private String applyFlay;
    private String joinNeedContact;
    private String joinVerify;

    public String getVerifyWay() {
        return verifyWay;
    }

    public void setVerifyWay(String verifyWay) {
        this.verifyWay = verifyWay;
    }

    public String getApplyFlay() {
        return applyFlay;
    }

    public void setApplyFlay(String applyFlay) {
        this.applyFlay = applyFlay;
    }

    public String getJoinNeedContact() {
        return joinNeedContact;
    }

    public void setJoinNeedContact(String joinNeedContact) {
        this.joinNeedContact = joinNeedContact;
    }

    public String getJoinVerify() {
        return joinVerify;
    }

    public void setJoinVerify(String joinVerify) {
        this.joinVerify = joinVerify;
    }

    public static GroupVerifyWayBean parse(String json) throws AppException {
        GroupVerifyWayBean res = new GroupVerifyWayBean();
        try {
            res = gson.fromJson(json, GroupVerifyWayBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }
}
