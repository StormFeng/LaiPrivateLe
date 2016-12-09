package com.lailem.app.jni;

public class JniSharedLibrary {

    static {
        System.loadLibrary("Lailem");
    }

	/*
     * ======================================个人中心================================
	 * ====
	 */

    /**
     * 登陆 Post
     */
    public native static String login();

    /**
     * 注册 Post
     */
    public native static String register();

    /**
     * 第三方登录 微信登录 Post
     */
    public native static String loginThird();

	/*
     * ======================================验证码==================================
	 * ===
	 */

    /**
     * 取验证码 Post
     */
    public native static String authCode();

    /**
     * 取变更手机号时的验证码 Post
     */
    public native static String changePhone();

    /**
     * 验证验证码 Post
     */
    public native static String verifyAuthCode();

    /**
     * 绑定手机号 Post
     */
    public native static String bindPhone();

	/*
     * ======================================其它==================================
	 * ====
	 */

    /**
     * 修改个人信息 Post
     */
    public native static String changePersonInfo();

    /**
     * 忘记密码 Post
     */
    public native static String forgetPassword();

    /**
     * 变更手机号码 Post
     */
    public native static String changPhone();

    /**
     * 设置用户名 Post
     */
    public native static String setUsername();

    /**
     * 我的日程 Get
     */
    public native static String schedule();

    /**
     * 修改个人头像 Post
     */
    public native static String changeHead();

    /**
     * 加入黑名单 Post
     */
    public native static String addBlack();

    /**
     * 权限控制接口 Post
     */
    public native static String setPermission();

    /**
     * 提交个人位置的接口 Post
     */
    public native static String submitPosition();

    /**
     * 取黑名单接口 Get
     */
    public native static String blacklist();

    /**
     * 取黑名单id接口 Get
     */
    public native static String blacklistIds();

    /**
     * 移除黑名单 Post
     */
    public native static String removeBlack();

    /**
     * 设置备注名 Post
     */
    public native static String setRemark();

	/*
     * =====================================收藏====================================
	 * ==
	 */

    /**
     * 我的收藏列表 Get
     */
    public native static String collectList();

    /**
     * 删除我的收藏接口 Post
     */
    public native static String collectDel();

    /**
     * 收藏 Post
     */
    public native static String collect();

    /**
     * 我的动态列表 Get
     */
    public native static String getMyDynamicList();

    /**
     * 删除我的动态接口 Post
     */
    public native static String deleteMyDynamic();

	/*
     * =====================================活动与群组================================
	 * ======
	 */

    /**
     * 主页信息 Get
     */
    public native static String homePage();

    /**
     * 附近群组列表-按地址折叠 Get
     */
    public native static String fold();

    /**
     * 附近活动列表 Get
     */
    public native static String near();

    /**
     * 群组的活动列表 Get
     */
    public native static String getGroupActiveList();

    /**
     * 活动详情 Get
     */
    public native static String activity();

    /**
     * 投票活动详情 Get
     */
    public native static String getVoteActiveDetail();

    /**
     * 推荐或热门活动列表 Get
     */
    public native static String activity_recommond();


    /**
     * 申请入Group Get
     */
    public native static String apply();

    /**
     * 取group成员接口 Get
     */
    public native static String membe();

    /**
     * 获取Group成员个信息 Get
     */
    public native static String memberInfo();

    /**
     * 推荐或热门群组列表 Get
     */
    public native static String group_recommond();

    /**
     * 附近群组列表-不按地址折叠 Get
     */
    public native static String nofold();

    /**
     * 群组详情 Get
     */
    public native static String group();

    /**
     * 取Group申请验证方式 Get
     */
    public native static String verifyWay();

    /**
     * 获取轮播图片广告 Get
     */
    public native static String adsList();

    /**
     * 获取group邀请码 Post
     */
    public native static String groupInviteCode();

    /**
     * 接受邀请 Post
     */
    public native static String acceptInvite();

    /**
     * 是否加入group Get
     */
    public native static String isJoinedGroup();

    /**
     * 同步group Get
     */
    public native static String syncGroup();

    /**
     * 获取group简要信息 Get
     */
    public native static String groupBrief();

	/* =====================活动与群组-（称谓）============================= */


    /**
     * 取称谓模版分类列表 Get
     */
    public native static String type();

    /**
     * 取称谓模版详情 Get
     */
    public native static String cwtemplate();

    /**
     * 取称谓模板列表Get
     */
    public native static String group_cwtemplate_type();

    /**
     * 取group称谓接口 Get
     */
    public native static String group_cw();

    /**
     * 设置group称谓 Get
     */
    public native static String setGroupCw();

	/* =====================活动与群组-（我的活动与群组）============================= */

    /**
     * 退群或取消报名 Post
     */
    public native static String quit();

    /**
     * 解散群组 get
     */
    public native static String disbandGroup();

    /**
     * 根据邀请码获取邀请信息 get
     */
    public native static String getInviteInfo();

    /**
     * 我的活动列表 Get
     */
    public native static String activityList();

    /**
     * 申请验证接口 Post
     */
    public native static String applyVerify();

    /**
     * 踢人接口 Post
     */
    public native static String getout();

    /**
     * 取消管理员 Post
     */
    public native static String cancelManager();

    /**
     * 设置管理员 Post
     */
    public native static String setManager();

    /**
     * 设置group图片 Post
     */
    public native static String setPic();

    /**
     * 新建group Post
     */
    public native static String createGroup();

    /**
     * 新建投票活动 Post
     */
    public native static String createVoteActive();

    /**
     * 设置group昵称 Post
     */
    public native static String setNickname();

    /**
     * 我的群组列表 Get
     */
    public native static String onGroup();

    /**
     * 取group日程 Get
     */
    public native static String groupSchedule();

    /** 取资料库接口 Get */

    /**
     * 设置群标签 Post
     */
    public native static String setTag();

    /**
     * 修改group资料 Post
     */
    public native static String changGroupInfo();

    /**
     * 修改group设置 Post
     */
    public native static String changProperty();
    /**
     * 修改active设置 Post
     */
    public native static String changActiveProperty();

    /**
     * 设置group验证方式 Post
     */
    public native static String setVerifyWay();

    /**
     * 获取资料库 get
     */
    public native static String getGroupDatabase();

	/*
     * =====================================动态====================================
	 * ==
	 */
    /* =====================================动态(日程)============================= */

	/* =====================================动态(发表)============================= */

    /**
     * 发表文字、语音等 Post
     */
    public native static String dynamic();

    /**
     * 发表通知 Post
     */
    public native static String notice();

    /**
     * 取group动态列表 Get
     */
    public native static String groupDynamic();

    /**
     * 转载 Post
     */
    public native static String reprint();

    /**
     * 取动态详情 get
     */
    public native static String onDynamic();

    /**
     * 取Group通知列表 get
     */
    public native static String getGroupNoticeList();

	/* ====================================动态(评论与赞)=========================== */

    /**
     * 评论接口 Post
     */
    public native static String comment();

    /**
     * 取点赞列表 Post
     */
    public native static String likeList();

    /**
     * 取评论列表 Get
     */
    public native static String commentList();

    /**
     * 点赞或取消赞接口 Post
     */
    public native static String like();

    /**
     * 删除评论 Post
     */
    public native static String deleteComment();

	/* ===================================动态(投票)============================== */

    /**
     * 投票接口 Post
     */
    public native static String vote();

    /**
     * 投票人列表接口 Post
     */
    public native static String voterList();

	/* ===================================其它==================================== */
    /* ===================================其它(海报)================== */
    /* ===================================其它(图片素材)============== */

    /**
     * 随机获取一张图片 Get
     */
    public native static String randomOne();

    /**
     * 获取图片素材类型 Get
     */
    public native static String picMaterialType();

    /**
     * 取某一分类下的图片列表 Get
     */
    public native static String picMaterialList();

	/* ===================================其它(系统配置)================== */

    /**
     * 系统公共配置 Get
     */
    public native static String commonConfig();

    /**
     * 系统公共配置版本 Get
     */
    public native static String configVersion();

	/* ===================================其它(版本更新)================== */

    /**
     * 检测新版本-安卓 Get
     */
    public native static String versionCheck();

	/* ===================================其它(其它)================== */

    /**
     * 取备注名列表
     */
    public native static String getRemarks();

    /**
     * 根据用户id取用户头像与昵称
     */
    public native static String getUserBrief();

    /**
     * 获取文件Url
     */
    public native static String getFileUrl();

	/*
     * =====================================即时通讯=================================
	 */
    /* =====================================即时通讯(消息列表)============== */

    /**
     * 新增会话接口 post
     */
    public native static String conversationAdd();

    /**
     * 取会话列表接口 Get
     */
    public native static String conversationList();

    /**
     * 删除会话接口 post
     */
    public native static String deleteConversation();

    /**
     * 上传即时聊天文件 post
     */
    public native static String uploadChatFile();

    /**
     * 编辑器上传图片
     *
     * @return
     */
    public native static String uploadPicFile();

    public native static String updatePassword();

	/* =====================================公共配置================================ */

    /**
     * 数据库名字
     */
    public native static String dbName();

    /**
     * 聊天ip
     */
    public native static String hostForChat();

    /**
     * 聊天端口
     */
    public native static String portForChat();

    /**
     * 聊天域名
     */
    public native static String domainForChat();

    /**
     * 聊天子域名
     */
    public native static String subDomainForChat();

    /**
     * 域名
     */
    public native static String domain();

    /**
     * web项目域名
     */
    public native static String domainWeb();

    /**
     * ck的固定字符串
     */
    public native static String ckValue();

    /**
     * 通知发布者的id
     */
    public native static String notificationPublisherId();

    /**
     * 回调接收方的id
     */
    public native static String callbackReceiverId();

    /* =====================================常量================================ */
    public native static String ckKey();

    /**
     * tk
     */
    public native static String tKey();

    /**
     * 反馈意见
     */
    public native static String feedback();

    /**
     * 反馈类型
     */
    public native static String feedbackType();

    /**
     * 反馈类型详细
     */
    public native static String feedbackTypeSpecial();

    /**
     * 举报
     */
    public native static String addComplain();

    /**
     * 获取举报类型
     */
    public native static String getComplainType();

    /**
     * cia app_id
     */
    public native static String ciaAppId();

    /**
     * cia auth_code
     */
    public native static String ciaAuthCode();

    public native static String weiboApiKey();

    public native static String weiboRedirectUrl();

    public native static String weiboScope();

    public native static String qqAppId();

    public native static String weixinAppId();

    public native static String weixinAppSecret();

    /**
     * 友盟 appKey
     */
    public native static String umengAppkey();

    /**
     * 友盟 通道
     */
    public native static String umengChannel();

}
