package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 *  邀请信息
 *
 */
public class InviteInfoBean extends Result {

	private InviteInfo inviteInfo;

	public static InviteInfoBean parse(String json) throws AppException {
		InviteInfoBean res = new InviteInfoBean();
		try {
			res = gson.fromJson(json, InviteInfoBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

    public InviteInfo getInviteInfo() {
        return inviteInfo;
    }

    public void setInviteInfo(InviteInfo inviteInfo) {
        this.inviteInfo = inviteInfo;
    }

    public static class InviteInfo{
        private GroupInfo groupInfo;//		object
        private String inviteCode;//	邀请码	string
        private UserInfo userInfo;//	用户信息	object

        public void setGroupInfo(GroupInfo groupInfo) {
            this.groupInfo = groupInfo;
        }

        public GroupInfo getGroupInfo() {
            return groupInfo;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }
    }


    public static class GroupInfo{
        private String groupType;//类型	string	类型：1（活动）、2（普通群）、3（群联）
        private String id;//		string
        private String name;//	名称	string
        private String squareSPicName;//	正方形小图文件名	string
        private String permission;

        public void setPermission(String permission) {
            this.permission = permission;
        }

        public String getPermission() {
            return permission;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
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

        public String getSquareSPicName() {
            return squareSPicName;
        }

        public void setSquareSPicName(String squareSPicName) {
            this.squareSPicName = squareSPicName;
        }
    }

    public static class UserInfo{
        private String headSPicName;//	小头像文件名	string
        private String id;//	用户id	string
        private String nickname;//	用户昵称	string

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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }


}
