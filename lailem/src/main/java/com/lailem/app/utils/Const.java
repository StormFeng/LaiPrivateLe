package com.lailem.app.utils;

import com.lailem.app.jni.JniSharedLibraryWrapper;

public class Const {
    /**
     * 流程日志Tag
     */
    public static final String LOG_TAG_WORKFLOW = "log_tag_workflow";

    /**
     * 用户id
     */
    public static final String USER_ID = "user_id";
    /**
     * 用户姓名
     */
    public static final String USER_NAME = "user_name";
    /**
     * 用户昵称
     */
    public static final String USER_NICKNAME = "user_nickname";

    /**
     * 用户小头像
     */
    public static final String USER_SHEAD = "user_shead";
    /**
     * 用户大头像
     */
    public static final String USER_BHEAD = "user_bhead";

    public static final String value_success = "success";
    public static final String value_error = "error";
    public static final String key_ret = "ret";
    public static final String key_userInfo = "userInfo";
    public static final String key_headSPicName = "headSPicName";
    public static final String key_nickname = "nickname";
    public static final String key_publishType = "publishType";
    public static final String key_content = "content";
    /**
     * 用户省
     */
    public final static String USER_PROVINCE = "user_province";
    /**
     * 用户城市
     */
    public final static String USER_CITY = "user_city";
    /**
     * 生日
     */
    public final static String USER_BIRTHDAY = "user_birthday";
    /**
     * 性别
     */
    public final static String USER_SEX = "user_sex";
    /**
     * 用户区域
     */
    public final static String USER_AREA = "user_area";
    /**
     * 用户手机号
     */
    public final static String USER_PHONE = "user_phone";
    /**
     * 用户邮箱
     */
    public final static String USER_EMAIL = "user_email";
    /**
     * 用户用户名
     */
    public final static String USER_USERNAME = "user_username";
    /**
     * 用户签名
     */
    public final static String USER_SIGN = "user_sign";
    /**
     * 用户注册类型
     */
    public final static String USER_REGISTER_TYPE = "user_register_type";
    /**
     * 用户类型
     */
    public final static String USER_TYPE = "user_type";
    /**
     * 收藏个数
     */
    public final static String USER_COLLECT_COUNT = "user_collect_count";
    /**
     * 动态个数
     */
    public final static String USER_DYNAMIC_COUNT = "user_dynamic_count";

    /**
     * 用户token
     */
    public final static String USER_TOKEN = "user_token";

    /**
     * 性别男
     */
    public static final String MALE = "1";
    /**
     * 性别女
     */
    public static final String FEMALE = "2";

    /**
     * 手机验证方式（手机验证码或者CIA验证）
     */
    public static final String PHONE_VERIFY_TYPE = "msgSwitch";
    /**
     * CIA验证开
     */
    public static final String PHONE_VERIFY_TYPE_CIA_OPEN = "1";
    /**
     * CIA验证关
     */
    public static final String PHONE_VERIFY_TYPE_CIA_CLOSE = "2";

    /**
     * 活动人数上限
     */
    public static final String ACTIVITY_PLIMIT = "activityPLimit";
    /**
     * 群人数上限
     */
    public static final String GROUP_PLIMIT = "groupPLimit";
    /**
     * 用户参与群组数上限
     */
    public static final String USER_JG_LIMIT = "groupPLimit";

    /**
     * 系统公共配置版本
     */
    public static final String CONFIG_V_CODE = "config_v_code";

    /**
     * 群类型列表
     */
    public static final String GROUP_TYPE_LIST = "group_type_list";
    /**
     * 活动类型列表
     */
    public static final String ACTIVITY_TYPE_LIST = "activity_type_list";

    /**
     * 群组id
     */
    public static final String BUNDLE_KEY_GROUP_ID = "group_id";
    /**
     * 群组名称
     */
    public static final String BUNDLE_KEY_GROUP_NAME = "group_name";
    /**
     * 动态id
     */
    public static final String BUNDLE_KEY_DYNAMIC_ID = "dynamic_id";
    /**
     * 群号
     */
    public static final String BUNDLE_KEY_GROUP_NUM = "group_num";
    /**
     * 群标签
     */
    public static final String GROUP_TAG_LIST = "group_tag_list";

    /**
     *
     */
    public static final String SHAREOBJ_KEY_GROUP_INFO = "group_info";
    public static final String SHAREOBJ_KEY_ACTIVE_INFO = "active_info";

    // 验证状态：2（同意）、3（拒绝）、4（忽略）
    public static final String groupApplyVerifyStatus_agree = "2";
    public static final String groupApplyVerifyStatus_refuse = "3";
    public static final String groupApplyVerifyStatus_igone = "4";

    // 角色类型：1（创建者）、2（管理员）、3（普通成员）、4（未加入）
    public static final String ROLE_TYPE_CREATOR = "1";
    public static final String ROLE_TYPE_MANAGER = "2";
    public static final String ROLE_TYPE_NORMAL = "3";
    public static final String ROLE_TYPE_STRANGER = "4";
    // 动态类型：1（发表）、2（通知）、3（创建活动）、4（日程）、5（投票）
    public static final String DYNA_TYPE_PUBLISH = "1";
    public static final String DYNA_TYPE_NOTICE = "2";
    public static final String DYNA_TYPE_CREATE_ACTIVE = "3";
    public static final String DYNA_TYPE_SCHEDULE = "4";
    public static final String DYNA_TYPE_VOTE = "5";
    // 1（文字）、2（图片）、3（语音）、4（视频）、5（地址）、6（日程）、7（投票）、8（连接））
    public static final String PUBLISH_TYPE_TEXT = "1";
    public static final String PUBLISH_TYPE_IMAGE = "2";
    public static final String PUBLISH_TYPE_VOICE = "3";
    public static final String PUBLISH_TYPE_VIDEO = "4";
    public static final String PUBLISH_TYPE_ADDRESS = "5";
    public static final String PUBLISH_TYPE_SCHEDULE = "6";
    public static final String PUBLISH_TYPE_VOTE = "7";
    public static final String PUBLISH_TYPE_LINK = "8";
    // canVote表示是否能投票（时间未开始或没有投票权限）：1（能投）、0（不能投）；
    // isVoted表示是否已经投票：1（已投）、0（未投）
    public static final String CAN_VOTE = "1";// 能投
    public static final String CAN_NOT_VOTE = "0";// 不能投
    public static final String HAS_VOTE = "1";// 已投
    public static final String HAS_NOT_VOTE = "0";// 未投

    // CIA流水号
    public static final String BUNDLE_KEY_CIA_TRANS_ID = "cia_trans_id";

    // 获取验证码类型（1（注册）、2（忘记密码）、3（变更手机号））、4（绑定手机号）、5（解散群组）
    public static final String AUTH_TYPE_REGISTER = "1";
    public static final String AUTH_TYPE_FORGETPWD = "2";
    public static final String AUTH_TYPE_CHANGE_PHONE = "3";
    public static final String AUTH_TYPE_BIND_PHONE = "4";
    public static final String AUTH_TYPE_DISBAND_GROUP = "5";

    // 活动二维码，群二维码，投票活动二维码
    public static final String ACTIVE_PATTERN = JniSharedLibraryWrapper.domainWeb() + "activityDetail/";
    public static final String GROUP_PATTERN = JniSharedLibraryWrapper.domainWeb() + "groupDetail/";
    public static final String VOTE_ACTIVE_PATTERN = JniSharedLibraryWrapper.domainWeb() + "voteDetail/";
    // 二维码类型(活动，群组，投票活动，邀请)
    public static final int QRCODE_TYPE_ACTIVE = 1;
    public static final int QRCODE_TYPE_GROUP = 2;
    public static final int QRCODE_TYPE_VOTE_ACTIVE = 3;
    public static final int QRCODE_TYPE_INVITE = 4;

    // 投票活动固定类型ID
    public static final String TYPE_ID_VOTE = "402881f34f2075c8014f20761b0b0002";
    // 自定义活动固定类型ID
    public static final String TYPE_ID_CUSTOM = "402881f34f207b08014f207e4b8d0017";

    //固定app下载页面
    public static final String URL_APP_DOWNLOAD = "http://a.app.qq.com/o/simple.jsp?pkgname=com.lailem.app";
    public static final String URL_APP_ABOUT = JniSharedLibraryWrapper.domainWeb() + "aboutUs.html";
    public static final String URL_APP_HELP = JniSharedLibraryWrapper.domainWeb() + "help.html";
    public static final String URL_APP_SERVICE_DECLARE = JniSharedLibraryWrapper.domainWeb() + "serviceDeclare.html";
    public static final String URL_GROUP_LAILE = JniSharedLibraryWrapper.domainWeb() + "groupLaile.html";
    public static final String URL_ACTIVE_LAILE = JniSharedLibraryWrapper.domainWeb() + "laile.html";

    //邀请函
    public static final String URL_INVITE_LETTER = JniSharedLibraryWrapper.domainWeb() + "invitation/";
    //动态详情
    public static final String URL_DYNAMIC_DETAIL = JniSharedLibraryWrapper.domainWeb() + "dynamicDetail/";


    public static final int INVITE_TYPE_GROUP = 1;
    public static final int INVITE_TYPE_ACTIVE = 2;

    //权限：1（私有群，需邀请加入）、2（公开群：会出现在附近群组或活动列表里）3 群内活动私有
    public static final String PERMISSION_PRIVATE = "1";
    public static final String PERMISSION_PUBLIC = "2";
    public static final String PERMISSION_PRIVATE_IN_GROUP = "3";

    //资料是否完整 1 是完整的 ，2是不完整的
    public static final String DATA_INTACT_YES = "1";
    public static final String DATA_INTACT_NO = "2";

    //举报对象类型（1：活动2群组3用户）
    public static final String COMPLAIN_TYPE_ACTIVE = "1";
    public static final String COMPLAIN_TYPE_GROUP = "2";
    public static final String COMPLAIN_TYPE_MEMBER = "3";

    //首页活动列表缓存
    public static final String KEY_MAIN_ACTIVE_CACHE = "key_main_active_cache";
    //首页群组列表缓存
    public static final String KEY_MAIN_GROUP_CACHE = "key_main_group_cache";

    // 资料库类型 2（图片）、3（语音）、4（视频）、5（地址）
    public static final String DATABASE_TYPE_PIC = "2";
    public static final String DATABASE_TYPE_VOICE = "3";
    public static final String DATABASE_TYPE_VIDEO = "4";
    public static final String DATABASE_TYPE_ADDRESS = "5";

    public static final String DYNAMIC_SUFFIX = "dynamic_";
    //本地动态文件名的状态值  结尾数字1：发送失败 2:发送中
    public static final String DYNAMIC_STATE_FAIL = "1";
    public static final String DYNAMIC_STATE_SENDING = "2";

    //百度地图 113.423762,23.179638
    public static final String BAIDU_STATIC_IMAGE = "http://api.map.baidu.com/staticimage?width=554&height=320&center=" + "@@" + "&markers=" + "@@" + "&zoom=18&markerStyles=m,,0xff0000&ak=GIvOwC7AeDlnUGwZB2cyHeQl";
    public static final String BAIDU_STATIC_IMAGE_2 = "http://api.map.baidu.com/staticimage?width=500&height=369&center=" + "@@" + "&markers=" + "@@" + "&zoom=18&markerStyles=m,,0xff0000&ak=GIvOwC7AeDlnUGwZB2cyHeQl";

    //    加入验证是否要联系方式	string	（1需要，2不需要）默认不需要;
    public static final String ACTIVE_JOIN_NEED_CONTACT = "1";
    public static final String ACTIVE_JOIN_NOT_NEED_CONTACT = "2";

    //	是否需要加入验证	string	1（需要验证）、2（不需要验证）
    public static final String ACTIVE_JOIN_NEED_VERIFY = "1";
    public static final String ACTIVE_JOIN_NOT_NEED_VERIFY = "2";

    public static final String PHOTO_TAG = "photo_tag";

    //  动态形式（1（之前的自由组合、支持排序的形式）、2（固定排版的形式））
    public static final String DYNA_FROM_ENABLE_SORT = "1";
    public static final String DYNA_FROM_DISABLE_SORT = "2";

    //grouptype
    public static final String GROUPTYPE_ACTIVITY = "1";
    public static final String GROUPTYPE_GROUP = "2";


    //当前语音播放位置
    public static final String INDEX_VOICE_PLAYING = "index_voice_playing";

    public static final String EMPTY_PERSONAL_SIGN = "这个人很懒，神马都没有写";

    //活动报名开关 可选1（可以报名）2（截止报名）
    public static final String APPLY_FLAY_START = "1";
    public static final String APPLY_FLAY_STOP = "2";

    //普通成员创建群内活动权限开关 1（允许）2（不允许）
    public static final String CREATE_ACTIVITY_FLAY_ALLOW = "1";
    public static final String CREATE_ACTIVITY_FLAY_DENY = "2";

    //验证方式
    public static final String VERIFY_WAY_TEXT = "1";
    public static final String VERIFY_WAY_VOICE = "2";
    public static final String VERIFY_WAY_NONE = "3";

    //群信息或者设置已修改
    public static final String BUNDLE_KEY_GROUP_INFO_CHANGED = "group_info_changed";
    //活动信息或者设置已修改
    public static final String BUNDLE_KEY_ACTIVE_INFO_CHANGED = "active_info_changed";

}
