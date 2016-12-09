package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-取group成员接口
 *
 * @author MD-12
 */
public class GroupMemberBean extends Result {

    private ArrayList<GroupMember> memberList;// 成员列表 array<object>

    public ArrayList<GroupMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<GroupMember> memberList) {
        this.memberList = memberList;
    }

    public static GroupMemberBean parse(String json) throws AppException {
        GroupMemberBean res = new GroupMemberBean();
        try {
            res = gson.fromJson(json, GroupMemberBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    public static class GroupMember extends Result {
        private String age;// 年龄 string
        private String groupNickname;// 该用户在群里的昵称昵称 string
        private String headSPicName;// 小头像文件名 string
        private String id;// string
        private String memberId;// group成员用户id string
        private String nickname;// 用户昵称
        private String personalizedSignature;// 个性签名 string
        private String roleType;// 角色类型 string 角色类型：1（创建者）、2（管理员）、3（普通成员）
        private String sex;// 性别 string 1（男）、0（女）
        private String remark;
        private String cPhone;
        private String cName;

        public String getcPhone() {
            return cPhone;
        }

        public void setcPhone(String cPhone) {
            this.cPhone = cPhone;
        }

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public GroupMember(int itemViewType) {
            this.itemViewType = itemViewType;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGroupNickname() {
            return groupNickname;
        }

        public void setGroupNickname(String groupNickname) {
            this.groupNickname = groupNickname;
        }

        public String getHeadSPicName() {
            return headSPicName;
        }

        public void setHeadSPicName(String headSPicName) {
            this.headSPicName = headSPicName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPersonalizedSignature() {
            return personalizedSignature;
        }

        public void setPersonalizedSignature(String personalizedSignature) {
            this.personalizedSignature = personalizedSignature;
        }

        public String getRoleType() {
            return roleType;
        }

        public void setRoleType(String roleType) {
            this.roleType = roleType;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

    }
}
