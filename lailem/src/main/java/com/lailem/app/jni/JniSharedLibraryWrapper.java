package com.lailem.app.jni;

public class JniSharedLibraryWrapper {
    public static final String MASK = "****************************************";

	/*
     * ======================================个人中心================================
	 * ====
	 */

    /**
     * 登陆 Post
     */
    public static String login() {
        return JniSharedLibrary.login().replace(MASK, "");
    }

    /**
     * 注册 Post
     */
    public static String register() {
        return JniSharedLibrary.register().replace(MASK, "");
    }

    /**
     * 第三方登录 微信登录 Post
     */
    public static String loginThird() {
        return JniSharedLibrary.loginThird().replace(MASK, "");
    }

	/*
     * ======================================验证码==================================
	 * ===
	 */

    /**
     * 取验证码 Post
     */
    public static String authCode() {
        return JniSharedLibrary.authCode().replace(MASK, "");
    }

    /**
     * 取变更手机号时的验证码 Post
     */
    public static String changePhone() {
        return JniSharedLibrary.changePhone().replace(MASK, "");
    }

    /**
     * 验证验证码 Post
     */
    public static String verifyAuthCode() {
        return JniSharedLibrary.verifyAuthCode().replace(MASK, "");
    }

    /**
     * 绑定手机号 Post
     */
    public static String bindPhone() {
        return JniSharedLibrary.bindPhone().replace(MASK, "");
    }
    
	/*
     * ======================================其它==================================
	 * ====
	 */

    /**
     * 修改个人信息 Post
     */
    public static String changePersonInfo() {
        return JniSharedLibrary.changePersonInfo().replace(MASK, "");
    }

    /**
     * 忘记密码 Post
     */
    public static String forgetPassword() {
        return JniSharedLibrary.forgetPassword().replace(MASK, "");
    }

    /**
     * 变更手机号码 Post
     */
    public static String changPhone() {
        return JniSharedLibrary.changePhone().replace(MASK, "");
    }

    /**
     * 设置用户名 Post
     */
    public static String setUsername() {
        return JniSharedLibrary.setUsername().replace(MASK, "");
    }

    /**
     * 我的日程 Get
     */
    public static String schedule() {
        return JniSharedLibrary.schedule().replace(MASK, "");
    }

    /**
     * 修改个人头像 Post
     */
    public static String changeHead() {
        return JniSharedLibrary.changeHead().replace(MASK, "");
    }

    /**
     * 加入黑名单 Post
     */
    public static String addBlack() {
        return JniSharedLibrary.addBlack().replace(MASK, "");
    }

    /**
     * 权限控制接口 Post
     */
    public static String setPermission() {
        return JniSharedLibrary.setPermission().replace(MASK, "");
    }

    /**
     * 提交个人位置的接口 Post
     */
    public static String submitPosition() {
        return JniSharedLibrary.submitPosition().replace(MASK, "");
    }

    /**
     * 取黑名单接口 Get
     */
    public static String blacklist() {
        return JniSharedLibrary.blacklist().replace(MASK, "");
    }

    /**
     * 取黑名单id接口 Get
     */
    public static String blacklistIds() {
        return JniSharedLibrary.blacklistIds().replace(MASK, "");
    }

    /**
     * 移除黑名单 Post
     */
    public static String removeBlack() {
        return JniSharedLibrary.removeBlack().replace(MASK, "");
    }

    /**
     * 设置备注名 Post
     */
    public static String setRemark() {
        return JniSharedLibrary.setRemark().replace(MASK, "");
    }

	/*
     * =====================================收藏====================================
	 * ==
	 */

    /**
     * 我的收藏列表 Get
     */
    public static String collectList() {
        return JniSharedLibrary.collectList().replace(MASK, "");
    }

    /**
     * 删除我的收藏接口 Post
     */
    public static String collectDel() {
        return JniSharedLibrary.collectDel().replace(MASK, "");
    }

    /**
     * 收藏 Post
     */
    public static String collect() {
        return JniSharedLibrary.collect().replace(MASK, "");
    }

    /**
     * 我的动态列表 Get
     */
    public static String getMyDynamicList() {
        return JniSharedLibrary.getMyDynamicList().replace(MASK, "");
    }

    /**
     * 删除我的动态接口 Post
     */
    public static String deleteMyDynamic() {
        return JniSharedLibrary.deleteMyDynamic().replace(MASK, "");
    }
    /*
     * =====================================活动与群组================================
	 * ======
	 */

    /**
     * 主页信息 Get
     */
    public static String homePage() {
        return JniSharedLibrary.homePage().replace(MASK, "");
    }

    /**
     * 附近群组列表-按地址折叠 Get
     */
    public static String fold() {
        return JniSharedLibrary.fold().replace(MASK, "");
    }

    /**
     * 附近活动列表 Get
     */
    public static String near() {
        return JniSharedLibrary.near().replace(MASK, "");
    }

    /**
     * 群组的活动列表 Get
     */
    public static String getGroupActiveList() {
        return JniSharedLibrary.getGroupActiveList().replace(MASK, "");
    }

    /**
     * 活动详情 Get
     */
    public static String activity() {
        return JniSharedLibrary.activity().replace(MASK, "");
    }

    /**
     * 投票活动详情 Get
     */
    public static String getVoteActiveDetail() {
        return JniSharedLibrary.getVoteActiveDetail().replace(MASK, "");
    }

    /**
     * 推荐或热门活动列表 Get
     */
    public static String activity_recommond() {
        return JniSharedLibrary.activity_recommond().replace(MASK, "");
    }

    /**
     * 申请入Group Get
     */
    public static String apply() {
        return JniSharedLibrary.apply().replace(MASK, "");
    }

    /**
     * 取group成员接口 Get
     */
    public static String membe() {
        return JniSharedLibrary.membe().replace(MASK, "");
    }

    /**
     * 获取Group成员个信息 Get
     */
    public static String memberInfo() {
        return JniSharedLibrary.memberInfo().replace(MASK, "");
    }

    /**
     * 推荐或热门群组列表 Get
     */
    public static String group_recommond() {
        return JniSharedLibrary.group_recommond().replace(MASK, "");
    }

    /**
     * 附近群组列表-不按地址折叠 Get
     */
    public static String nofold() {
        return JniSharedLibrary.nofold().replace(MASK, "");
    }

    /**
     * 群组详情 Get
     */
    public static String group() {
        return JniSharedLibrary.group().replace(MASK, "");
    }

    /**
     * 取Group申请验证方式 Get
     */
    public static String verifyWay() {
        return JniSharedLibrary.verifyWay().replace(MASK, "");
    }

    /**
     * 获取轮播图片广告 Get
     */
    public static String adsList() {
        return JniSharedLibrary.adsList().replace(MASK, "");
    }

    /**
     * 获取group邀请码 Post
     */
    public static String groupInviteCode() {
        return JniSharedLibrary.groupInviteCode().replace(MASK, "");
    }

    /**
     * 接受邀请 Post
     */
    public static String acceptInvite() {
        return JniSharedLibrary.acceptInvite().replace(MASK, "");
    }

    /**
     * 是否加入group Get
     */
    public static String isJoinedGroup() {
        return JniSharedLibrary.isJoinedGroup().replace(MASK, "");
    }

    /**
     * 同步group Get
     */
    public static String syncGroup() {
        return JniSharedLibrary.syncGroup().replace(MASK, "");
    }

    /**
     * 获取group简要信息 Get
     */
    public static String groupBrief() {
        return JniSharedLibrary.groupBrief().replace(MASK, "");
    }

	/* =====================活动与群组-（称谓）============================= */

    /**
     * 取称谓模版分类列表 Get
     */
    public static String type() {
        return JniSharedLibrary.type().replace(MASK, "");
    }

    /**
     * 取称谓模版详情 Get
     */
    public static String cwtemplate() {
        return JniSharedLibrary.cwtemplate().replace(MASK, "");
    }

    /**
     * 取称谓模板列表Get
     */
    public static String group_cwtemplate_type() {
        return JniSharedLibrary.group_cwtemplate_type().replace(MASK, "");
    }

    /**
     * 取group称谓接口 Get
     */
    public static String group_cw() {
        return JniSharedLibrary.group_cw().replace(MASK, "");
    }

    /**
     * 设置group称谓 Get
     */
    public static String setGroupCw() {
        return JniSharedLibrary.setGroupCw().replace(MASK, "");
    }

	/* =====================活动与群组-（我的活动与群组）============================= */

    /**
     * 退群或取消报名 Post
     */
    public static String quit() {
        return JniSharedLibrary.quit().replace(MASK, "");
    }

    /**
     * 解散群组 get
     */
    public static String disbandGroup() {
        return JniSharedLibrary.disbandGroup().replace(MASK, "");
    }

    /**
     * 根据邀请码获取邀请信息 get
     */
    public static String getInviteInfo() {
        return JniSharedLibrary.getInviteInfo().replace(MASK, "");
    }

    /**
     * 我的活动列表 Get
     */
    public static String activityList() {
        return JniSharedLibrary.activityList().replace(MASK, "");
    }

    /**
     * 申请验证接口 Post
     */
    public static String applyVerify() {
        return JniSharedLibrary.applyVerify().replace(MASK, "");
    }

    /**
     * 踢人接口 Post
     */
    public static String getout() {
        return JniSharedLibrary.getout().replace(MASK, "");
    }

    /**
     * 取消管理员 Post
     */
    public static String cancelManager() {
        return JniSharedLibrary.cancelManager().replace(MASK, "");
    }

    /**
     * 设置管理员 Post
     */
    public static String setManager() {
        return JniSharedLibrary.setManager().replace(MASK, "");
    }

    /**
     * 设置group图片 Post
     */
    public static String setPic() {
        return JniSharedLibrary.setPic().replace(MASK, "");
    }

    /**
     * 新建group Post
     */
    public static String createGroup() {
        return JniSharedLibrary.createGroup().replace(MASK, "");
    }

    /**
     * 新建投票活动 Post
     */
    public static String createVoteActive() {
        return JniSharedLibrary.createVoteActive().replace(MASK, "");
    }

    /**
     * 设置group昵称 Post
     */
    public static String setNickname() {
        return JniSharedLibrary.setNickname().replace(MASK, "");
    }

    /**
     * 我的群组列表 Get
     */
    public static String onGroup() {
        return JniSharedLibrary.onGroup().replace(MASK, "");
    }

    /**
     * 取group日程 Get
     */
    public static String groupSchedule() {
        return JniSharedLibrary.groupSchedule().replace(MASK, "");
    }

    /** 取资料库接口 Get */

    /**
     * 设置群标签 Post
     */
    public static String setTag() {
        return JniSharedLibrary.setTag().replace(MASK, "");
    }

    /**
     * 修改group资料 Post
     */
    public static String changGroupInfo() {
        return JniSharedLibrary.changGroupInfo().replace(MASK, "");
    }

    /**
     * 修改group设置 Post
     */
    public static String changProperty() {
        return JniSharedLibrary.changProperty().replace(MASK, "");
    }

    /**
     * 修改active设置 Post
     */
    public static String changActiveProperty() {
        return JniSharedLibrary.changActiveProperty().replace(MASK, "");
    }

    /**
     * 设置group验证方式 Post
     */
    public static String setVerifyWay() {
        return JniSharedLibrary.setVerifyWay().replace(MASK, "");
    }

    /**
     * 获取资料库 get
     */
    public static String getGroupDatabase() {
        return JniSharedLibrary.getGroupDatabase().replace(MASK, "");
    }

    
	/*
     * =====================================动态====================================
	 * ==
	 */
    /* =====================================动态(日程)============================= */

	/* =====================================动态(发表)============================= */

    /**
     * 发表文字、语音等 Post
     */
    public static String dynamic() {
        return JniSharedLibrary.dynamic().replace(MASK, "");
    }

    /**
     * 发表通知 Post
     */
    public static String notice() {
        return JniSharedLibrary.notice().replace(MASK, "");
    }

    /**
     * 取group动态列表 Get
     */
    public static String groupDynamic() {
        return JniSharedLibrary.groupDynamic().replace(MASK, "");
    }

    /**
     * 转载 Post
     */
    public static String reprint() {
        return JniSharedLibrary.reprint().replace(MASK, "");
    }

    /**
     * 取动态详情 get
     */
    public static String onDynamic() {
        return JniSharedLibrary.onDynamic().replace(MASK, "");
    }

    /**
     * 取Group通知列表 get
     */
    public static String getGroupNoticeList() {
        return JniSharedLibrary.getGroupNoticeList().replace(MASK, "");
    }

	/* ====================================动态(评论与赞)=========================== */

    /**
     * 评论接口 Post
     */
    public static String comment() {
        return JniSharedLibrary.comment().replace(MASK, "");
    }

    /**
     * 取点赞列表 Post
     */
    public static String likeList() {
        return JniSharedLibrary.likeList().replace(MASK, "");
    }

    /**
     * 取评论列表 Get
     */
    public static String commentList() {
        return JniSharedLibrary.commentList().replace(MASK, "");
    }

    /**
     * 点赞或取消赞接口 Post
     */
    public static String like() {
        return JniSharedLibrary.like().replace(MASK, "");
    }


    /**
     * 删除评论 Post
     */
    public static String deleteComment() {
        return JniSharedLibrary.deleteComment().replace(MASK, "");
    }

	/* ===================================动态(投票)============================== */

    /**
     * 投票接口 Post
     */
    public static String vote() {
        return JniSharedLibrary.vote().replace(MASK, "");
    }

    /**
     * 投票人列表接口 Post
     */
    public static String voterList() {
        return JniSharedLibrary.voterList().replace(MASK, "");
    }

	/* ===================================其它==================================== */
    /* ===================================其它(海报)================== */
    /* ===================================其它(图片素材)============== */

    /**
     * 随机获取一张图片 Get
     */
    public static String randomOne() {
        return JniSharedLibrary.randomOne().replace(MASK, "");
    }

    /**
     * 获取图片素材类型 Get
     */
    public static String picMaterialType() {
        return JniSharedLibrary.picMaterialType().replace(MASK, "");
    }

    /**
     * 取某一分类下的图片列表 Get
     */
    public static String picMaterialList() {
        return JniSharedLibrary.picMaterialList().replace(MASK, "");
    }
    
	/* ===================================其它(系统配置)================== */

    /**
     * 系统公共配置 Get
     */
    public static String commonConfig() {
        return JniSharedLibrary.commonConfig().replace(MASK, "");
    }

    /**
     * 系统公共配置版本 Get
     */
    public static String configVersion() {
        return JniSharedLibrary.configVersion().replace(MASK, "");
    }

	/* ===================================其它(版本更新)================== */

    /**
     * 检测新版本-安卓 Get
     */
    public static String versionCheck() {
        return JniSharedLibrary.versionCheck().replace(MASK, "");
    }


	/* ===================================其它(其它)================== */

    /**
     * 取备注名列表
     */
    public static String getRemarks() {
        return JniSharedLibrary.getRemarks().replace(MASK, "");
    }

    /**
     * 根据用户id取用户头像与昵称
     */
    public static String getUserBrief() {
        return JniSharedLibrary.getUserBrief().replace(MASK, "");
    }

    /**
     * 获取文件Url
     */
    public static String getFileUrl() {
        return JniSharedLibrary.getFileUrl().replace(MASK, "");
    }

	/*
     * =====================================即时通讯=================================
	 */
    /* =====================================即时通讯(消息列表)============== */

    /**
     * 新增会话接口 post
     */
    public static String conversationAdd() {
        return JniSharedLibrary.conversationAdd().replace(MASK, "");
    }

    /**
     * 取会话列表接口 Get
     */
    public static String conversationList() {
        return JniSharedLibrary.conversationList().replace(MASK, "");
    }

    /**
     * 删除会话接口 post
     */
    public static String deleteConversation() {
        return JniSharedLibrary.deleteConversation().replace(MASK, "");
    }

    /**
     * 上传即时聊天文件 post
     */
    public static String uploadChatFile() {
        return JniSharedLibrary.uploadChatFile().replace(MASK, "");
    }

    /**
     * 编辑器上传图片
     *
     * @return
     */
    public static String uploadPicFile() {
        return JniSharedLibrary.uploadPicFile().replace(MASK, "");
    }

    /**
     * 更新密码
     *
     * @return
     */
    public static String updatePassword() {
        return JniSharedLibrary.updatePassword().replace(MASK, "");
    }


	/* =====================================公共配置================================ */

    /**
     * 数据库名字
     */
    public static String dbName() {
        return JniSharedLibrary.dbName().replace(MASK, "");
    }

    /**
     * 聊天ip
     */
    public static String hostForChat() {
        return JniSharedLibrary.hostForChat().replace(MASK, "");
    }

    /**
     * 聊天端口
     */
    public static String portForChat() {
        return JniSharedLibrary.portForChat().replace(MASK, "");
    }

    /**
     * 聊天域名
     */
    public static String domainForChat() {
        return JniSharedLibrary.domainForChat().replace(MASK, "");
    }

    /**
     * 聊天子域名
     */
    public static String subDomainForChat() {
        return JniSharedLibrary.subDomainForChat().replace(MASK, "");
    }

    /**
     * 域名
     */
    public static String domain() {
        return JniSharedLibrary.domain().replace(MASK, "");
    }

    /**
     * web项目域名
     */
    public static String domainWeb() {
        return JniSharedLibrary.domainWeb().replace(MASK, "");
    }

    /**
     * ck的固定字符串
     */
    public static String ckValue() {
        return JniSharedLibrary.ckValue().replace(MASK, "");
    }


    /**
     * 通知发布者的id
     */
    public static String notificationPublisherId() {
        return JniSharedLibrary.notificationPublisherId().replace(MASK, "");
    }

    /**
     * 回调接收方的id
     */
    public static String callbackReceiverId() {
        return JniSharedLibrary.callbackReceiverId().replace(MASK, "");
    }

    /* =====================================常量================================ */
    public static String ckKey() {
        return JniSharedLibrary.ckKey().replace(MASK, "");
    }


    /**
     * tk
     */
    public static String tKey() {
        return JniSharedLibrary.tKey().replace(MASK, "");
    }

    /**
     * 反馈意见
     */
    public static String feedback() {
        return JniSharedLibrary.feedback().replace(MASK, "");
    }

    /**
     * 反馈类型
     */
    public static String feedbackType() {
        return JniSharedLibrary.feedbackType().replace(MASK, "");
    }

    /**
     * 反馈类型详细
     */
    public static String feedbackTypeSpecial() {
        return JniSharedLibrary.feedbackTypeSpecial().replace(MASK, "");
    }

    /**
     * 举报
     */
    public static String addComplain() {
        return JniSharedLibrary.addComplain().replace(MASK, "");
    }

    /**
     * 获取举报类型
     */
    public static String getComplainType() {
        return JniSharedLibrary.getComplainType().replace(MASK, "");
    }

    /**
     * cia app_id
     */
    public static String ciaAppId() {
        return JniSharedLibrary.ciaAppId().replace(MASK, "");
    }

    /**
     * cia auth_code
     */
    public static String ciaAuthCode() {
        return JniSharedLibrary.ciaAuthCode().replace(MASK, "");
    }

    public static String weiboApiKey() {
        return JniSharedLibrary.weiboApiKey().replace(MASK, "");
    }

    public static String weiboRedirectUrl() {
        return JniSharedLibrary.weiboRedirectUrl().replace(MASK, "");
    }

    public static String weiboScope() {
        return JniSharedLibrary.weiboScope().replace(MASK, "");
    }

    public static String qqAppId() {
        return JniSharedLibrary.qqAppId().replace(MASK, "");
    }

    public static String weixinAppId() {
        return JniSharedLibrary.weixinAppId().replace(MASK, "");
    }

    public static String weixinAppSecret() {
        return JniSharedLibrary.weixinAppSecret().replace(MASK, "");
    }

    /**
     * 友盟 appKey
     */
    public static String umengAppkey() {
        return JniSharedLibrary.umengAppkey().replace(MASK, "");
    }

    /**
     * 友盟 通道
     */
    public static String umengChannel() {
        return JniSharedLibrary.umengChannel().replace(MASK, "");
    }
}
