package com.lailem.app.utils;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.MGroup;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupUtils {
    /**
     * 加入或新建群成功后调用
     *
     * @param context
     * @param groupId
     * @param groupName
     * @param squareSPic
     */
    public static void joinGroup(final Context context, final String groupId, final String groupName, final String groupIntro, final String squareSPic, final String permission) {
        KLog.i("groupId:::" + groupId + ",groupName:::" + groupName + ",squareSPic:::" + squareSPic + ",permission:::" + permission);
        new Thread(new Runnable() {

            @Override
            public void run() {
                DaoOperate.getInstance(context).insert(groupId, Constant.gType_group);
                addGroup(context, groupId, groupName, groupIntro, squareSPic, permission, Constant.gType_group, null);
                BroadcastManager.sendJoinGroupBroadcast(context);
            }
        }).start();
    }

    /**
     * 退出或解散群成功后调用
     *
     * @param context
     * @param groupId
     */
    public static void exitGroup(final Context context, final String groupId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                DaoOperate.getInstance(context).delete(groupId);
                BroadcastManager.sendExitGroupBroadcast(context, groupId);
            }
        }).start();
    }

    /**
     * 更新群资料
     *
     * @param context
     * @param groupId
     * @param groupName
     * @param groupIntro
     * @param squareSPic
     */
    public static void updateGroup(final Context context, final String groupId, final String groupName, final String groupIntro, final String squareSPic, final String permission) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                updateGroupCommon(context, groupId, groupName, groupIntro, squareSPic, permission, Constant.gType_group);
                BroadcastManager.sendUpdateGroupBroadcast(context, groupId);
            }
        }).start();
    }


    /**
     * 加入或新建活动成功后调用
     *
     * @param context
     * @param groupId
     * @param groupName
     * @param squareSPic
     */
    public static void joinActivity(final Context context, final String groupId, final String groupName, final String groupIntro, final String squareSPic, final String permission) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                DaoOperate.getInstance(context).insert(groupId, Constant.gType_activity);
                addGroup(context, groupId, groupName, groupIntro, squareSPic, permission, Constant.gType_activity, null);
                BroadcastManager.sendJoinActivityBroadcast(context);
            }
        }).start();
    }

    /**
     * 加入或新建活动成功后调用
     *
     * @param context
     * @param groupId
     * @param groupName
     * @param squareSPic
     * @param typeId     活动类型id
     */
    public static void joinActivity(final Context context, final String groupId, final String groupName, final String groupIntro, final String squareSPic, final String permission, final String typeId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                DaoOperate.getInstance(context).insert(groupId, Constant.gType_activity);
                addGroup(context, groupId, groupName, groupIntro, squareSPic, permission, Constant.gType_activity, typeId);
                BroadcastManager.sendJoinActivityBroadcast(context);
            }
        }).start();
    }

    /**
     * 退出或解散活动成功后调用
     *
     * @param context
     * @param groupId
     */
    public static void exitActivity(final Context context, final String groupId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                DaoOperate.getInstance(context).delete(groupId);
                BroadcastManager.sendExitActivityBroadcast(context, groupId);
            }
        }).start();
    }


    /**
     * 更新活动资料
     *
     * @param context
     * @param groupId
     * @param groupName
     * @param groupIntro
     * @param squareSPic
     */
    public static void updateActivity(final Context context, final String groupId, final String groupName, final String groupIntro, final String squareSPic, final String permission) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                updateGroupCommon(context, groupId, groupName, groupIntro, squareSPic, permission, Constant.gType_activity);
                BroadcastManager.sendUpdateActivityBroadcast(context, groupId);
            }
        }).start();
    }

    private static void addGroup(Context context, String groupId, String groupName, String groupIntro, String squareSPic, String permission, String gType, String typeId) {
        Group group = DaoOperate.getInstance(context).queryGroup(groupId);
        if (group == null) {
            group = new Group();
            group.setCreateTime(System.currentTimeMillis());
            group.setUpdateTime(System.currentTimeMillis());
            group.setGroupId(groupId);
            group.setName(groupName);
            group.setSquareSPic(squareSPic);
            group.setGroupType(gType);
            group.setIntro(groupIntro);
            group.setTypeId(typeId);
            if(!TextUtils.isEmpty(permission)) {
                group.setPermission(Integer.parseInt(permission));
            }
            DaoOperate.getInstance(context).insert(group);
        }
    }

    private static void updateGroupCommon(Context context, String groupId, String groupName, String groupIntro, String squareSPic, String permission, String gType) {
        Group group = DaoOperate.getInstance(context).queryGroup(groupId);
        if (group != null) {
            if (!TextUtils.isEmpty(groupName)) {
                group.setName(groupName);
            }
            if (!TextUtils.isEmpty(groupIntro)) {
                group.setIntro(groupIntro);
            }
            if (!TextUtils.isEmpty(squareSPic)) {
                group.setSquareSPic(squareSPic);
            }
            if (!TextUtils.isEmpty(permission)) {
                group.setPermission(Integer.parseInt(permission));
            }
            DaoOperate.getInstance(context).update(group);
        }
    }

    /**
     * 根据置顶时间和更新时间排序
     *
     * @param groups
     */
    public static void sortGroups(List<MGroup> groups) {
        Collections.sort(groups, new Comparator<MGroup>() {
            @Override
            public int compare(MGroup lhs, MGroup rhs) {
                if (lhs.getTopTime() != null && rhs.getTopTime() != null) {//都置顶
                    return rhs.getTopTime().compareTo(lhs.getTopTime());
                } else if (lhs.getTopTime() != null && rhs.getTopTime() == null) {
                    return -1;
                } else if (lhs.getTopTime() == null && rhs.getTopTime() != null) {
                    return 1;
                } else {
                    return rhs.getUpdateTime().compareTo(lhs.getUpdateTime());
                }
            }
        });
    }

}
