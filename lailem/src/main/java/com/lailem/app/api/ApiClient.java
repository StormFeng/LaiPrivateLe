package com.lailem.app.api;

import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.bean.Result;
import com.lailem.app.jni.JniSharedLibraryWrapper;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean;
import com.lailem.app.jsonbean.activegroup.ActiveListBean;
import com.lailem.app.jsonbean.activegroup.ActiveNearBean;
import com.lailem.app.jsonbean.activegroup.ActiveRecommondBean;
import com.lailem.app.jsonbean.activegroup.AdsListBean;
import com.lailem.app.jsonbean.activegroup.CwDetailBean;
import com.lailem.app.jsonbean.activegroup.CwInfoBean;
import com.lailem.app.jsonbean.activegroup.CwListBean;
import com.lailem.app.jsonbean.activegroup.CwTypeBean;
import com.lailem.app.jsonbean.activegroup.GroupActiveListBean;
import com.lailem.app.jsonbean.activegroup.GroupApplyBean;
import com.lailem.app.jsonbean.activegroup.GroupBrief;
import com.lailem.app.jsonbean.activegroup.GroupDatabaseBean;
import com.lailem.app.jsonbean.activegroup.GroupIdBean;
import com.lailem.app.jsonbean.activegroup.GroupInfoBean;
import com.lailem.app.jsonbean.activegroup.GroupListBean;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean;
import com.lailem.app.jsonbean.activegroup.GroupNearFoldBean;
import com.lailem.app.jsonbean.activegroup.GroupNearNofoldBean;
import com.lailem.app.jsonbean.activegroup.GroupNoticeListBean;
import com.lailem.app.jsonbean.activegroup.GroupRecommondBean;
import com.lailem.app.jsonbean.activegroup.GroupScheduleBean;
import com.lailem.app.jsonbean.activegroup.GroupVerifyWayBean;
import com.lailem.app.jsonbean.activegroup.HomePageBean;
import com.lailem.app.jsonbean.activegroup.InviteCode;
import com.lailem.app.jsonbean.activegroup.IsJoinedGroup;
import com.lailem.app.jsonbean.activegroup.MemberInfoBean;
import com.lailem.app.jsonbean.activegroup.SyncGroupListBean;
import com.lailem.app.jsonbean.activegroup.UploadFileInfo;
import com.lailem.app.jsonbean.activegroup.VoteActiveDetailBean;
import com.lailem.app.jsonbean.chat.UploadChatFileInfo;
import com.lailem.app.jsonbean.dynamic.AddCommentBean;
import com.lailem.app.jsonbean.dynamic.CommentListBean;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean;
import com.lailem.app.jsonbean.dynamic.DynamicDetailBean;
import com.lailem.app.jsonbean.dynamic.GetUserBriefBean;
import com.lailem.app.jsonbean.dynamic.GroupDynamicsBean;
import com.lailem.app.jsonbean.dynamic.LikeListBean;
import com.lailem.app.jsonbean.dynamic.PicMaterialListBean;
import com.lailem.app.jsonbean.dynamic.PicMaterialTypeBean;
import com.lailem.app.jsonbean.dynamic.RandomMaterialBean;
import com.lailem.app.jsonbean.dynamic.UserRemarksBean;
import com.lailem.app.jsonbean.dynamic.VCodeBean;
import com.lailem.app.jsonbean.dynamic.VersionCheckBean;
import com.lailem.app.jsonbean.dynamic.VoterListBean;
import com.lailem.app.jsonbean.personal.BlacklistBean;
import com.lailem.app.jsonbean.personal.BlacklistIdsBean;
import com.lailem.app.jsonbean.personal.ChangeHeadBean;
import com.lailem.app.jsonbean.personal.CollectBean;
import com.lailem.app.jsonbean.personal.ComplainTypeBean;
import com.lailem.app.jsonbean.personal.FeedbackTypeBean;
import com.lailem.app.jsonbean.personal.InviteInfoBean;
import com.lailem.app.jsonbean.personal.MyDynamicListBean;
import com.lailem.app.jsonbean.personal.ScheduleBean;
import com.lailem.app.jsonbean.personal.UserBean;
import com.lailem.app.utils.EncryptionUtil;
import com.midian.fastdevelop.afinal.FinalHttp;
import com.midian.fastdevelop.afinal.http.AjaxCallBack;
import com.midian.fastdevelop.afinal.http.AjaxParams;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ApiClient {
    public static final String TAG = "ApiClient";
    private static ApiClient instance;
    private FinalHttp httpUtils = new FinalHttp();
    public static String DOMAIN = JniSharedLibraryWrapper.domain();
    public static String fileBaseUrl;

    public ApiClient() {

    }

    public static ApiClient getApi() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                }
            }
        }
        return instance;
    }

    private Result getSync(String url, AjaxParams params, final Class clazz) throws Exception {
        addCK(params);
        String t = (String) httpUtils.getSync(url, params);
        KLog.i(TAG, url + "\n" + params.getParamString().replace("&", "\n").replace("=", " = ").replace("&", "\n").replace("=", " = "));
        KLog.json(TAG, t);
        Result res = (Result) clazz.getMethod("parse", String.class).invoke(clazz, t);
        return res;
    }

    private void get(final ApiCallback callback, final String url, final AjaxParams params, final Class clazz, final String tag) {
        addCK(params);
        KLog.i(TAG, url + "\n" + params.getParamString().replace("&", "\n").replace("=", " = ") + "\n----params end-----\n");
        httpUtils.get(url, params, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                callback.onApiStart(tag);
            }

            @Override
            public void onLoading(long count, long current) {
                callback.onApiLoading(count, current, tag);
            }

            @Override
            public void onSuccess(String t, String requestTag) {
                KLog.json(TAG, t);
                Result res = null;
                try {
                    res = (Result) clazz.getMethod("parse", String.class).invoke(clazz, t);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onParseError(tag);
                }
                callback.onApiSuccess(res, tag);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg, String requestTag) {
                callback.onApiFailure(t, errorNo, strMsg, tag);
            }

        });
    }

    private Result postSync(String url, AjaxParams params, final Class clazz) throws Exception {
        addCK(params);
        String t = (String) httpUtils.postSync(url, params);
        KLog.i(TAG, url + "\n" + params.getParamString().replace("&", "\n").replace("=", " = ") + "\n----params end-----\n");
        KLog.json(TAG, t);
        Result res = (Result) clazz.getMethod("parse", String.class).invoke(clazz, t);
        return res;
    }

    private void post(final ApiCallback callback, final String url, final AjaxParams params, final Class clazz, final String tag) {
        addCK(params);
        KLog.i(TAG, url + "\n" + params.getParamString().replace("&", "\n").replace("=", " = ") + "\n----params end-----\n");
        httpUtils.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                callback.onApiStart(tag);
            }

            @Override
            public void onLoading(long count, long current) {
                callback.onApiLoading(count, current, tag);
            }

            @Override
            public void onSuccess(String t, String requestTag) {
                KLog.json(TAG, t);
                Result res = null;
                try {
                    res = (Result) clazz.getMethod("parse", String.class).invoke(clazz, t);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onParseError(tag);
                }
                callback.onApiSuccess(res, tag);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg, String requestTag) {
                callback.onApiFailure(t, errorNo, strMsg, requestTag);
            }

        }, "");
    }

    public FinalHttp getHttpUtils() {
        return httpUtils;
    }

    public static String getMethodName(StackTraceElement ste[]) {
        String methodName = "";
        boolean flag = false;

        for (StackTraceElement s : ste) {

            if (flag) {

                methodName = s.getMethodName();
                break;
            }
            flag = s.getMethodName().equals("getStackTrace");
        }
        return methodName;
    }

    private static void addCK(AjaxParams params) {
        String ck = EncryptionUtil.getEncryptionStr();
        params.put(JniSharedLibraryWrapper.ckKey(), ck);
    }

    private static void addT(AjaxParams params) {
        String token = AppContext.getInstance().getToken();
        if (!TextUtils.isEmpty(token)) {
            params.put(JniSharedLibraryWrapper.tKey(), token);
        }
    }

	/*
     * ======================================个人中心================================
	 */

    /**
     * 登陆 Post
     */
    public void login(ApiCallback callback, String account, String password) {
        String[] metaArr = JniSharedLibraryWrapper.login().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], account);
        params.put(metaArr[2], password);
        post(callback, DOMAIN + metaArr[0], params, UserBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 注册 Post
     */
    public void register(ApiCallback callback, String authCode, String birthday, String cityId, File head, String nickname, String password, String phone, String provinceId, String sex, String transId) {
        String[] metaArr = JniSharedLibraryWrapper.register().split(",");
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put(metaArr[1], authCode);
        params.put(metaArr[2], birthday);
        params.put(metaArr[3], cityId);
        if (head != null) {
            try {
                params.put(metaArr[4], head);
            } catch (FileNotFoundException e) {
                AppContext.showToast("您选择的图片不存在，请检查该文件是否被删除");
                e.printStackTrace();
                return;
            }
        }
        params.put(metaArr[5], nickname);
        params.put(metaArr[6], password);
        params.put(metaArr[7], phone);
        params.put(metaArr[8], provinceId);
        params.put(metaArr[9], sex);
        params.put(metaArr[10], transId);
        post(callback, DOMAIN + metaArr[0], params, UserBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 第三方登录 微信登录 Post
     */
    public void loginThird(ApiCallback callback, String headBUrl, String nickname, String openId, String type, String unionId
            , String birthday, String cityId, String provinceId, String sex) {
        String[] metaArr = JniSharedLibraryWrapper.loginThird().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], headBUrl);
        params.put(metaArr[2], nickname);
        params.put(metaArr[3], openId);
        params.put(metaArr[4], type);
        params.put(metaArr[5], unionId);
        params.put(metaArr[6], birthday);
        params.put(metaArr[7], cityId);
        params.put(metaArr[8], provinceId);
        params.put(metaArr[9], sex);
        post(callback, DOMAIN + metaArr[0], params, UserBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/*
     * ======================================验证码==================================
	 * ===
	 */

    /**
     * 取验证码 Post
     */
    public void authCode(ApiCallback callback, String authType, String phone) {
        String[] metaArr = JniSharedLibraryWrapper.authCode().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], authType);
        params.put(metaArr[2], phone);
        post(callback, DOMAIN + metaArr[0], params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取变更手机号时的验证码 Post
     */
    public void changePhone(ApiCallback callback, String id, String phone, String phoneType) {
        String[] metaArr = JniSharedLibraryWrapper.changePhone().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], phone);
        params.put(metaArr[2], phoneType);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 验证验证码 Post
     */
    public void verifyAuthCode(ApiCallback callback, String authCode, String phone, String transId) {
        String[] metaArr = JniSharedLibraryWrapper.verifyAuthCode().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], authCode);
        params.put(metaArr[2], phone);
        params.put(metaArr[3], transId);
        post(callback, DOMAIN + metaArr[0], params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 绑定手机号 Post
     */
    public void bindPhone(ApiCallback callback, String id, String authCode, String password, String phone, String transId) {
        String[] metaArr = JniSharedLibraryWrapper.bindPhone().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], authCode);
        params.put(metaArr[2], password);
        params.put(metaArr[3], phone);
        params.put(metaArr[4], transId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/*
     * ======================================其它==================================
	 * ====
	 */

    /**
     * 修改个人信息 Post
     */
    public void changePersonInfo(ApiCallback callback, String id, String birthday, String cityId, String email, String name, String nickname, String personalizedSignature, String provinceId,
                                 String sex) {
        String[] metaArr = JniSharedLibraryWrapper.changePersonInfo().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], birthday);
        params.put(metaArr[2], cityId);
        params.put(metaArr[3], email);
        params.put(metaArr[4], name);
        params.put(metaArr[5], nickname);
        params.put(metaArr[6], personalizedSignature);
        params.put(metaArr[7], provinceId);
        params.put(metaArr[8], sex);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 修改个人信息 Post(登录后完善资料使用 token需要单独传入)
     */
    public void changePersonInfo(ApiCallback callback, String token, String id, String birthday, String cityId, String email, String name, String nickname, String personalizedSignature, String provinceId,
                                 String sex) {
        String[] metaArr = JniSharedLibraryWrapper.changePersonInfo().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], birthday);
        params.put(metaArr[2], cityId);
        params.put(metaArr[3], email);
        params.put(metaArr[4], name);
        params.put(metaArr[5], nickname);
        params.put(metaArr[6], personalizedSignature);
        params.put(metaArr[7], provinceId);
        params.put(metaArr[8], sex);
        params.put(JniSharedLibraryWrapper.tKey(), token);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 忘记密码 Post
     */
    public void forgetPassword(ApiCallback callback, String authCode, String password, String phone, String transId) {
        String[] metaArr = JniSharedLibraryWrapper.forgetPassword().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], authCode);
        params.put(metaArr[2], password);
        params.put(metaArr[3], phone);
        params.put(metaArr[4], transId);
        post(callback, DOMAIN + metaArr[0], params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 变更手机号码 Post
     */
    public void changPhone(ApiCallback callback, String id, String newAuthCode, String newPhone, String oldAuthCode, String oldPhone) {
        String[] metaArr = JniSharedLibraryWrapper.changPhone().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], newAuthCode);
        params.put(metaArr[2], newPhone);
        params.put(metaArr[3], oldAuthCode);
        params.put(metaArr[4], oldPhone);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 设置用户名 Post
     */
    public void setUsername(ApiCallback callback, String id, String username) {
        String[] metaArr = JniSharedLibraryWrapper.setUsername().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], username);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 我的日程 Get
     */
    public void schedule(ApiCallback callback, String id) {
        String[] metaArr = JniSharedLibraryWrapper.schedule().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, ScheduleBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 修改个人头像 Post
     */
    public void changeHead(ApiCallback callback, String id, File head) {
        String[] metaArr = JniSharedLibraryWrapper.changeHead().split(",");
        AjaxParams params = new AjaxParams();
        if (head != null) {
            try {
                params.put(metaArr[1], head);
            } catch (FileNotFoundException e) {
                AppContext.showToast("您选择的图片不存在，请检查该文件是否被删除");
                e.printStackTrace();
                return;
            }
        }
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, ChangeHeadBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 修改个人头像（完善资料使用 token需要单独传入） Post
     */
    public void changeHead(ApiCallback callback, String token, String id, File head) {
        String[] metaArr = JniSharedLibraryWrapper.changeHead().split(",");
        AjaxParams params = new AjaxParams();
        if (head != null) {
            try {
                params.put(metaArr[1], head);
            } catch (FileNotFoundException e) {
                AppContext.showToast("您选择的图片不存在，请检查该文件是否被删除");
                e.printStackTrace();
                return;
            }
        }
        params.put(JniSharedLibraryWrapper.tKey(), token);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, ChangeHeadBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 加入黑名单 Post
     */
    public void addBlack(ApiCallback callback, String id, String blackUserId) {
        String[] metaArr = JniSharedLibraryWrapper.addBlack().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], blackUserId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 权限控制接口 Post
     */
    public void setPermission(ApiCallback callback, String id) {
        String[] metaArr = JniSharedLibraryWrapper.setPermission().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 提交个人位置的接口 Post
     */
    public void submitPosition(ApiCallback callback, String id, String address, String lat, String lon) {
        String[] metaArr = JniSharedLibraryWrapper.submitPosition().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], address);
        params.put(metaArr[2], lat);
        params.put(metaArr[3], lon);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取黑名单接口 Get
     */
    public void blacklist(ApiCallback callback, String id) {
        String[] metaArr = JniSharedLibraryWrapper.blacklist().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, BlacklistBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取黑名单接口 Get
     *
     * @throws Exception
     */
    public Result blacklist(String id) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.blacklist().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, BlacklistBean.class);
    }

    /**
     * 取黑名单用户id接口 Get
     *
     * @throws Exception
     */
    public Result blacklistIds(String id) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.blacklistIds().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, BlacklistIdsBean.class);
    }

    /**
     * 移除黑名单 Post
     */
    public void removeBlack(ApiCallback callback, String id, String blackUserId) {
        String[] metaArr = JniSharedLibraryWrapper.removeBlack().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], blackUserId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 设置备注名 Post
     */
    public void setRemark(ApiCallback callback, String id, String rem, String remUserId) {
        String[] metaArr = JniSharedLibraryWrapper.setRemark().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], rem);
        params.put(metaArr[2], remUserId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/*
     * =====================================收藏====================================
	 */

    /**
     * 我的收藏列表 Get
     *
     * @throws Exception
     */
    public Result collect_List(String id, String pageCount, String pageNo) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.collectList().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, CollectBean.class);
    }

    /**
     * 我的收藏列表 Get
     *
     * @throws Exception
     */
    public Result getMyDynamicList(String id, String pageCount, String pageNo) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.getMyDynamicList().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, MyDynamicListBean.class);
    }

    /**
     * 删除我的收藏接口 Post
     */
    public void collectDel(ApiCallback callback, String id, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.collectDel().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 删除我的动态接口 Post
     */
    public void deleteMyDynamic(ApiCallback callback, String id, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.deleteMyDynamic().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 收藏 Post
     */
    public void collect(ApiCallback callback, String id, String id2, String cType) {
        String[] metaArr = JniSharedLibraryWrapper.collect().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], cType);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/*
     * =====================================活动与群组================================
	 * ======
	 */

    /**
     * 主页信息 Get
     */
    public void homePage(ApiCallback callback, String homePage) {
        String[] metaArr = JniSharedLibraryWrapper.homePage().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], homePage);
        get(callback, DOMAIN + metaArr[0], params, HomePageBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 附近群组列表-按地址折叠 Get
     */
    public void fold(ApiCallback callback, String lat, String lon, String pageCount, String pageNo, String tagId, String timeFilter) {
        String[] metaArr = JniSharedLibraryWrapper.fold().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], lat);
        params.put(metaArr[2], lon);
        params.put(metaArr[3], pageCount);
        params.put(metaArr[4], pageNo);
        params.put(metaArr[5], tagId);
        params.put(metaArr[6], timeFilter);
        get(callback, DOMAIN + metaArr[0], params, GroupNearFoldBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 附近活动列表 Get
     *
     * @throws Exception
     */
    public Result near(String lat, String lon, String pageCount, String pageNo, String sort, String timeFilter, String typeId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.near().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], lat);
        params.put(metaArr[2], lon);
        params.put(metaArr[3], pageCount);
        params.put(metaArr[4], pageNo);
        params.put(metaArr[5], sort);
        params.put(metaArr[6], timeFilter);
        params.put(metaArr[7], typeId);
        return getSync(DOMAIN + metaArr[0], params, ActiveNearBean.class);
    }

    /**
     * 群组的活动列表 Get
     *
     * @throws Exception
     */
    public Result getGroupActiveList(String id, String pageCount, String pageNo, String userId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.getGroupActiveList().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        params.put(metaArr[3], userId);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replaceFirst(":id", id), params, GroupActiveListBean.class);
    }

    /**
     * 活动详情 Get
     *
     * @throws Exception
     */
    public Result activity(String id, String userId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.activity().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], userId);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, ActiveInfoBean.class);
    }

    /**
     * 投票活动详情 Get
     *
     * @throws Exception
     */
    public Result getVoteActiveDetail(String id, String userId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.getVoteActiveDetail().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], userId);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, VoteActiveDetailBean.class);
    }

    /**
     * 推荐或热门活动列表 Get
     */
    public void activity_recommond(ApiCallback callback, String askType, String lat, String lon, String pageCount, String pageNo) {
        String[] metaArr = JniSharedLibraryWrapper.activity_recommond().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], askType);
        params.put(metaArr[2], lat);
        params.put(metaArr[3], lon);
        params.put(metaArr[4], pageCount);
        params.put(metaArr[5], pageNo);
        get(callback, DOMAIN + metaArr[0], params, ActiveRecommondBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 申请入Group post
     */
    public void apply(ApiCallback callback, String id, String id2, String applyText, File applyVoice, String duration, String name, String phone) {
        String[] metaArr = JniSharedLibraryWrapper.apply().split(",");
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put(metaArr[1], applyText);
        if (applyVoice != null) {
            try {
                params.put(metaArr[2], applyVoice);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                AppContext.showToast("您语音文件不存在，请检查该文件是否被删除");
                return;
            }
        }
        params.put(metaArr[3], duration);
        params.put(metaArr[4], name);
        params.put(metaArr[5], phone);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, GroupApplyBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取group成员接口(同步) Get
     *
     * @throws Exception
     */
    public Result membeSync(String id, String pageCount, String pageNo, String userId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.membe().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        params.put(metaArr[3], userId);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, GroupMemberBean.class);
    }

    /**
     * 取group成员接口(异步) Get
     *
     * @throws Exception
     */
    public void membe(ApiCallback callback, String id, String pageCount, String pageNo, String userId) {
        String[] metaArr = JniSharedLibraryWrapper.membe().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        params.put(metaArr[3], userId);
        addT(params);
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, GroupMemberBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 获取Group成员个信息 Get
     *
     * @throws Exception
     */
    public void memberInfo(ApiCallback calllback, String id, String userId) {
        String[] metaArr = JniSharedLibraryWrapper.memberInfo().split(",");
        AjaxParams params = new AjaxParams();
        if (userId != null) {
            params.put(metaArr[1], userId);
            addT(params);
        }
        get(calllback, DOMAIN + metaArr[0].replaceFirst(":id", id), params, MemberInfoBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 推荐或热门群组列表 Get
     */
    public void group_recommond(ApiCallback callback, String askType, String lat, String lon, String pageCount, String pageNo) {
        String[] metaArr = JniSharedLibraryWrapper.group_recommond().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], askType);
        params.put(metaArr[2], lat);
        params.put(metaArr[3], lon);
        params.put(metaArr[4], pageCount);
        params.put(metaArr[5], pageNo);
        get(callback, DOMAIN + metaArr[0], params, GroupRecommondBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 附近群组列表-不按地址折叠 Get
     *
     * @throws Exception
     */
    public Result nofold(String lat, String lon, String pageCount, String pageNo, String sort, String tagId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.nofold().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], lat);
        params.put(metaArr[2], lon);
        params.put(metaArr[3], pageCount);
        params.put(metaArr[4], pageNo);
        params.put(metaArr[5], sort);
        params.put(metaArr[6], tagId);
        return getSync(DOMAIN + metaArr[0], params, GroupNearNofoldBean.class);
    }

    /**
     * 群组详情 Get
     *
     * @return
     * @throws Exception
     */
    public Result group(String id, String userId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.group().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], userId);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, GroupInfoBean.class);
    }

    /**
     * 取Group申请验证方式 Get
     */
    public void verifyWay(ApiCallback callback, String id, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.verifyWay().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        get(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, GroupVerifyWayBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 获取轮播图片广告 Get
     */
    public void adsList(ApiCallback callback) {
        String[] metaArr = JniSharedLibraryWrapper.adsList().split(",");
        AjaxParams params = new AjaxParams();
        get(callback, DOMAIN + metaArr[0], params, AdsListBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 获取Group邀请码 Post
     *
     * @throws Exception
     */
    public Result groupInviteCode(String id1, String id2) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.groupInviteCode().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        return postSync(DOMAIN + metaArr[0].replaceFirst(":id", id1).replaceFirst(":id", id2), params, InviteCode.class);
    }

    /**
     * 获取Group邀请码 Post
     *
     * @throws Exception
     */
    public void groupInviteCode(ApiCallback callback, String id1, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.groupInviteCode().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id1).replaceFirst(":id", id2), params, InviteCode.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 接受邀请 Post
     *
     * @throws Exception
     */
    public void acceptInvite(ApiCallback callback, String id1, String id2, String inviteCode, String inviterId) {
        String[] metaArr = JniSharedLibraryWrapper.acceptInvite().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], inviteCode);
        params.put(metaArr[2], inviterId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id1).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 是否加入group Get
     *
     * @throws Exception
     */
    public void isJoinedGroup(ApiCallback callback, String id1, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.isJoinedGroup().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        get(callback, DOMAIN + metaArr[0].replaceFirst(":id", id1).replaceFirst(":id", id2), params, IsJoinedGroup.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 同步group Get
     *
     * @throws Exception
     */
    public Result syncGroup(String id, String groupIds) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.syncGroup().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], groupIds);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, SyncGroupListBean.class);
    }

    /**
     * 获取Group简要信息 Get
     *
     * @throws Exception
     */
    public Result groupBrief(String id1, String id2) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.groupBrief().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        return getSync(DOMAIN + metaArr[0].replaceFirst(":id", id1).replaceFirst(":id", id2), params, GroupBrief.class);
    }

    /**
     * 获取Group简要信息 Get
     *
     * @throws Exception
     */
    public void groupBrief(ApiCallback callback, String id1, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.groupBrief().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        get(callback, DOMAIN + metaArr[0].replaceFirst(":id", id1).replaceFirst(":id", id2), params, GroupBrief.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/* =====================活动与群组-（称谓）============================= */

    /**
     * 取称谓模版分类列表 Get
     */
    public void type(ApiCallback callback) {
        String[] metaArr = JniSharedLibraryWrapper.type().split(",");
        AjaxParams params = new AjaxParams();
        get(callback, DOMAIN + metaArr[0], params, CwTypeBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取称谓模版详情 Get
     */
    public void cwtemplate(ApiCallback callback, String id) {
        String[] metaArr = JniSharedLibraryWrapper.cwtemplate().split(",");
        AjaxParams params = new AjaxParams();
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, CwDetailBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取称谓模板列表Get
     */
    public void group_cwtemplate_type(ApiCallback callback, String id, String pageCount, String pageNo) {
        String[] metaArr = JniSharedLibraryWrapper.group_cwtemplate_type().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, CwListBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取group称谓接口 Get
     */
    public void group_cw(ApiCallback callback, String id, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.group_cw().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        get(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, CwInfoBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/* =====================活动与群组-（我的活动与群组）============================= */

    /**
     * 退群或取消报名 post
     */
    public void quit(ApiCallback callback, String id, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.quit().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 解散群组 post
     */
    public void disbandGroup(ApiCallback callback, String id, String id2, String authCode, String transId, String password) {
        String[] metaArr = JniSharedLibraryWrapper.disbandGroup().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], authCode);
        params.put(metaArr[2], transId);
        params.put(metaArr[3], password);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 我的活动列表 Get
     *
     * @throws Exception
     */
    public Result activity_list(String id) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.activityList().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, ActiveListBean.class);
    }

    /**
     * 申请验证接口 Post
     */
    public void applyVerify(ApiCallback callback, String id, String applyId, String verifyStatus) {
        String[] metaArr = JniSharedLibraryWrapper.applyVerify().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], applyId);
        params.put(metaArr[2], verifyStatus);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 踢人接口 Post
     */
    public void getout(ApiCallback callback, String id, String id2, String memberId) {
        String[] metaArr = JniSharedLibraryWrapper.getout().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], memberId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取消管理员 Post
     */
    public void cancelManager(ApiCallback callback, String id, String id2, String cancelManagerId) {
        String[] metaArr = JniSharedLibraryWrapper.cancelManager().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], cancelManagerId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 设置管理员 Post
     */
    public void setManager(ApiCallback callback, String id, String id2, String toManagerId) {
        String[] metaArr = JniSharedLibraryWrapper.setManager().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], toManagerId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 设置group图片 Post
     */
    public void setPic(ApiCallback callback, String id, String id2, File pic, String picMaterialId) {
        String[] metaArr = JniSharedLibraryWrapper.setPic().split(",");
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        if (pic != null) {
            try {
                params.put(metaArr[1], pic);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                AppContext.showToast("您选择的图片不存在，请检查该文件是否被删除");
                return;
            }
        }
        params.put(metaArr[2], picMaterialId);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 新建group Post
     */
    public void createGroup(ApiCallback callback, String id, String address, String contact, String endTime, String groupType, String intro, String lat, String lon, String name, String parentId,
                            String permission, File pic, String picMaterialId, String startTime, String typeId, String typeName, String joinNeedContact, String joinVerify) {
        String[] metaArr = JniSharedLibraryWrapper.createGroup().split(",");
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put(metaArr[1], address);
        params.put(metaArr[2], contact);
        params.put(metaArr[3], endTime);
        params.put(metaArr[4], groupType);
        params.put(metaArr[5], intro);
        params.put(metaArr[6], lat);
        params.put(metaArr[7], lon);
        params.put(metaArr[8], name);
        params.put(metaArr[9], parentId);
        params.put(metaArr[10], permission);
        if (pic != null) {
            try {
                params.put(metaArr[11], pic);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                AppContext.showToast("您选择的图片不存在，请检查该文件是否被删除");
                return;
            }
        }
        params.put(metaArr[12], picMaterialId);
        params.put(metaArr[13], startTime);
        params.put(metaArr[14], typeId);
        params.put(metaArr[15], typeName);
        params.put(metaArr[16], joinNeedContact);
        params.put(metaArr[17], joinVerify);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, GroupIdBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 新建投票活动 Post
     */
    public void createVoteActive(ApiCallback callback, String id, String endTime, String name, File pic, String picMaterialId, String voteInfo) {
        String[] metaArr = JniSharedLibraryWrapper.createVoteActive().split(",");
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put(metaArr[1], endTime);
        params.put(metaArr[2], name);
        if (pic != null) {
            try {
                params.put(metaArr[3], pic);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                AppContext.showToast("您选择的图片不存在，请检查该文件是否被删除");
                return;
            }
        }
        params.put(metaArr[4], picMaterialId);
        params.put(metaArr[5], voteInfo);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, GroupIdBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 设置group昵称 Post
     */
    public void setNickname(ApiCallback callback, String id, String id2, String nickname) {
        String[] metaArr = JniSharedLibraryWrapper.setNickname().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], nickname);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 我的群组列表 Get
     *
     * @throws Exception
     */
    public Result onGroup(String id) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.onGroup().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, GroupListBean.class);
    }

    /**
     * 取group日程 Get
     *
     * @throws Exception
     */
    public Result groupSchedule(String id, String id2, String pageCount, String pageNo) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.groupSchedule().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, GroupScheduleBean.class);
    }

    /**
     * 设置群标签 Post
     */
    public void setTag(ApiCallback callback, String id, String id2, String tagIds) {
        String[] metaArr = JniSharedLibraryWrapper.setTag().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], tagIds);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 修改group资料 Post
     */
    public void changGroupInfo(ApiCallback callback, String id, String id2, String intro, String name, String address, String lat, String lon) {
        String[] metaArr = JniSharedLibraryWrapper.changGroupInfo().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], intro);
        params.put(metaArr[2], name);
        params.put(metaArr[3], address);
        params.put(metaArr[4], lat);
        params.put(metaArr[5], lon);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 修改group设置 Post
     */
    public void changProperty(ApiCallback callback, String id, String id2, String createActivityFlay) {
        String[] metaArr = JniSharedLibraryWrapper.changProperty().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], createActivityFlay);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 修改active设置 Post
     */
    public void changActiveProperty(ApiCallback callback, String id, String id2, String applyFlay, String joinNeedContact, String name, String phone) {
        String[] metaArr = JniSharedLibraryWrapper.changActiveProperty().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], applyFlay);
        params.put(metaArr[2], joinNeedContact);
        params.put(metaArr[3], name);
        params.put(metaArr[4], phone);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }


    /**
     * 设置group验证方式 Post
     */
    public void setVerifyWay(ApiCallback callback, String id, String id2, String verifyWay) {
        String[] metaArr = JniSharedLibraryWrapper.setVerifyWay().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], verifyWay);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/* =====================================动态(发表)============================= */

    /**
     * 发表文字、语音等 Post
     */
    public void dynamic(ApiCallback callback, String id, String contentList, ArrayList<File> fileKey, String groupId, String dynaForm) {
        String[] metaArr = JniSharedLibraryWrapper.dynamic().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], contentList);
        try {
            if (fileKey != null) {
                for (int i = 0; i < fileKey.size(); i++) {
                    params.put(metaArr[2], fileKey.get(i));
                }
            }
        } catch (FileNotFoundException e) {
            AppContext.showToast("您选择的图片不存在，请检查该文件是否被删除");
            e.printStackTrace();
            return;
        }
        params.put(metaArr[3], groupId);
        params.put(metaArr[4], dynaForm);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 发表通知 Post
     */
    public void notice(ApiCallback callback, String id, String deail, String groupId, String topic) {
        String[] metaArr = JniSharedLibraryWrapper.notice().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], deail);
        params.put(metaArr[2], groupId);
        params.put(metaArr[3], topic);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取group动态列表 Get
     *
     * @throws Exception
     */
    public Result groupDynamic(String id, String contentType, String pageCount, String pageNo, String userId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.groupDynamic().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], contentType);
        params.put(metaArr[2], pageCount);
        params.put(metaArr[3], pageNo);
        params.put(metaArr[4], userId);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, GroupDynamicsBean.class);
    }

    /**
     * 转载 Post
     */
    public void reprint(ApiCallback callback, String id, String groupId, String userId, String whither) {
        String[] metaArr = JniSharedLibraryWrapper.reprint().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], groupId);
        params.put(metaArr[2], userId);
        params.put(metaArr[3], whither);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取动态详情 get
     *
     * @throws Exception
     */
    public Result onDynamic(String id, String id2) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.onDynamic().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], id2);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, DynamicDetailBean.class);
    }

    /**
     * 取group通知列表 get
     *
     * @throws Exception
     */
    public Result getGroupNoticeList(String id, String pageCount, String pageNo, String userId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.getGroupNoticeList().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        params.put(metaArr[3], userId);
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, GroupNoticeListBean.class);
    }

	/* ====================================动态(评论与赞)=========================== */

    /**
     * 评论接口 Post
     */
    public void comment(ApiCallback callback, String id, String id2, String comId, String comType, String comment) {
        String[] metaArr = JniSharedLibraryWrapper.comment().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], comId);
        params.put(metaArr[2], comType);
        params.put(metaArr[3], comment);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, AddCommentBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取点赞列表 Post
     *
     * @throws Exception
     */
    public Result likeList(String id, String likeType, String pageCount, String pageNo) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.likeList().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], likeType);
        params.put(metaArr[2], pageCount);
        params.put(metaArr[3], pageNo);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, LikeListBean.class);
    }

    /**
     * 取评论列表 Get
     *
     * @throws Exception
     */
    public Result commentList(String id, String comType, String pageCount, String pageNo) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.commentList().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], comType);
        params.put(metaArr[2], pageCount);
        params.put(metaArr[3], pageNo);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, CommentListBean.class);
    }

    /**
     * 点赞或取消赞接口 Post
     */
    public void like(ApiCallback callback, String id, String id2, String likeType) {
        String[] metaArr = JniSharedLibraryWrapper.like().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], likeType);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 删除评论 Post
     */
    public void deleteComment(ApiCallback callback, String id, String id2) {
        String[] metaArr = JniSharedLibraryWrapper.deleteComment().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/* ===================================动态(投票)============================== */

    /**
     * 投票接口 Post
     */
    public void vote(ApiCallback callback, String id, String id2, String voteItemIds) {
        String[] metaArr = JniSharedLibraryWrapper.vote().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], voteItemIds);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replaceFirst(":id", id).replaceFirst(":id", id2), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 投票人列表接口 Post
     *
     * @throws Exception
     */
    public Result voterList(String id) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.voterList().split(",");
        AjaxParams params = new AjaxParams();
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, VoterListBean.class);
    }

	/* ===================================其它(图片素材)============== */

    /**
     * 随机获取一张图片 Get
     */
    public void randomOne(ApiCallback callback) {
        String[] metaArr = JniSharedLibraryWrapper.randomOne().split(",");
        AjaxParams params = new AjaxParams();
        get(callback, DOMAIN + metaArr[0], params, RandomMaterialBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 获取图片素材类型 Get
     *
     * @throws Exception
     */
    public Result picMaterialType() throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.picMaterialType().split(",");
        AjaxParams params = new AjaxParams();
        return getSync(DOMAIN + metaArr[0], params, PicMaterialTypeBean.class);
    }

    /**
     * 取某一分类下的图片列表 Get
     *
     * @throws Exception
     */
    public Result picMaterial_Type(String id) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.picMaterialList().split(",");
        AjaxParams params = new AjaxParams();
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, PicMaterialListBean.class);
    }

	/* ===================================其它(系统配置)================== */

    /**
     * 系统公共配置 Get
     */
    public void getCommonConfig(ApiCallback callback, String version) {
        String[] metaArr = JniSharedLibraryWrapper.commonConfig().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], version);
        get(callback, DOMAIN + metaArr[0], params, CommonConfigBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 系统公共配置版本 Get
     */
    public void getConfigVersion(ApiCallback callback) {
        String[] metaArr = JniSharedLibraryWrapper.configVersion().split(",");
        AjaxParams params = new AjaxParams();
        get(callback, DOMAIN + metaArr[0], params, VCodeBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/* ===================================其它(版本更新)================== */

    /**
     * 检测新版本-安卓 Get
     */
    public void versionCheck(ApiCallback callback, String vCode) {
        String[] metaArr = JniSharedLibraryWrapper.versionCheck().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], vCode);
        get(callback, DOMAIN + metaArr[0], params, VersionCheckBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

	/* ===================================其它(其它)================== */

    /**
     * 根据用户id取用户头像与昵称 Get
     */
    public void getUserBrief(ApiCallback callback, String id, String userId) {
        String[] metaArr = JniSharedLibraryWrapper.getUserBrief().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], userId);
        addT(params);
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, GetUserBriefBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 根据用户id取用户头像与昵称 Get
     */
    public String getUserBrief(String id, String userId) {
        String[] metaArr = JniSharedLibraryWrapper.getUserBrief().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], userId);
        addT(params);
        addCK(params);
        String t = (String) httpUtils.getSync(DOMAIN + metaArr[0].replace(":id", id), params);
        return t;
    }

    /**
     * 取备注名列表 Get
     */
    public void getRemarks(ApiCallback callback, String id) {
        String[] metaArr = JniSharedLibraryWrapper.getRemarks().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, UserRemarksBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取备注名列表 Get
     *
     * @throws Exception
     */
    public Result getRemarks(String id) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.getRemarks().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, UserRemarksBean.class);
    }

    /**
     * 获取文件url
     */
    public static String getFileUrl(String fileName) {
        if (fileBaseUrl == null) {
            // String[] metaArr = JniSharedLibraryWrapper.getFileUrl().split(",");
            // fileBaseUrl = DOMAIN + metaArr[0];
            fileBaseUrl = JniSharedLibraryWrapper.getFileUrl();
        }
        return fileBaseUrl + fileName;
    }

	/* =====================================即时通讯(消息列表)============== */

    /**
     * 新增会话接口 post
     */
    public void conversationAdd(ApiCallback callback, String id, String objId, String objType) {
        String[] metaArr = JniSharedLibraryWrapper.conversationAdd().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], objId);
        params.put(metaArr[2], objType);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 取会话列表接口 Get
     */
    public void conversationList(ApiCallback callback, String id) {
        String[] metaArr = JniSharedLibraryWrapper.conversationList().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 删除会话接口 post
     */
    public void deleteConversation(ApiCallback callback, String id) {
        String[] metaArr = JniSharedLibraryWrapper.deleteConversation().split(",");
        AjaxParams params = new AjaxParams();
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 上传聊天文件接口 post
     *
     * @throws Exception
     */
    public Result uploadeChatFile(ArrayList<File> files, String fileType, String duration, String userId) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.uploadChatFile().split(",");
        AjaxParams params = new AjaxParams();
        try {
            for (File file : files) {
                params.put(metaArr[1], file);
            }
        } catch (FileNotFoundException e) {
            throw e;
        }
        params.put(metaArr[2], fileType);
        params.put(metaArr[3], duration);
        params.put(metaArr[4], userId);
        addT(params);
        return postSync(DOMAIN + metaArr[0], params, UploadChatFileInfo.class);
    }


    /**
     * 上传聊天文件接口 post
     *
     * @throws Exception
     */
    public Result uploadPicFile(String id, File file) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.uploadPicFile().split(",");
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        if (file != null) {
            try {
                params.put(metaArr[1], file);
            } catch (FileNotFoundException e) {
                throw e;
            }
        }
        addT(params);
        return postSync(DOMAIN + metaArr[0].replace(":id", id), params, UploadFileInfo.class);
    }

    public void updatePassword(ApiCallback callback, String id, String oldPwd, String newPwd) {
        String[] metaArr = JniSharedLibraryWrapper.updatePassword().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], oldPwd);
        params.put(metaArr[2], newPwd);
        addT(params);
        post(callback, DOMAIN + metaArr[0].replace(":id", id), params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 反馈意见 post
     */
    public void feedback(ApiCallback callback, String content, String fbItemId, ArrayList<File> pics, String userId) {
        String[] metaArr = JniSharedLibraryWrapper.feedback().split(",");
        AjaxParams params = new AjaxParams();
        params.setHasFile(true);
        params.put(metaArr[1], content);
        params.put(metaArr[2], fbItemId);
        try {
            for (File f : pics) {
                params.put(metaArr[3], f);
            }
        } catch (FileNotFoundException e) {
            AppContext.showToast("您选择的图片不存在，请检查该文件是否被删除");
            e.printStackTrace();
            return;
        }
        params.put(metaArr[4], userId);
        addT(params);
        post(callback, DOMAIN + metaArr[0], params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 反馈类型 get
     *
     * @throws Exception
     */
    public Result feedbackType() throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.feedbackType().split(",");
        AjaxParams params = new AjaxParams();
        return getSync(DOMAIN + metaArr[0], params, FeedbackTypeBean.class);
    }

    /**
     * 反馈类型详细 get
     */
    public void feedbackTypeSpecial(ApiCallback callback, String id) {
        String[] metaArr = JniSharedLibraryWrapper.feedbackTypeSpecial().split(",");
        AjaxParams params = new AjaxParams();
        get(callback, DOMAIN + metaArr[0].replace(":id", id), params, FeedbackTypeBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }


    /**
     * 举报
     *
     * @param callback
     * @param content
     * @param isAnonymous
     * @param objId
     * @param objType
     * @param typeId
     * @param userId
     */
    public void addComplain(ApiCallback callback, String content, String isAnonymous, String objId, String objType, String typeId, String userId) {
        String[] metaArr = JniSharedLibraryWrapper.addComplain().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], content);
        params.put(metaArr[2], isAnonymous);
        params.put(metaArr[3], objId);
        params.put(metaArr[4], objType);
        params.put(metaArr[5], typeId);
        params.put(metaArr[6], userId);
        addT(params);
        post(callback, DOMAIN + metaArr[0], params, Result.class, getMethodName(Thread.currentThread().getStackTrace()));

    }

    /**
     * 获取举报类型
     */
    public void getComplainType(ApiCallback callback) {
        String[] metaArr = JniSharedLibraryWrapper.getComplainType().split(",");
        AjaxParams params = new AjaxParams();
        get(callback, DOMAIN + metaArr[0], params, ComplainTypeBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }

    /**
     * 根据邀请码获取邀请信息get
     */
    public void getInviteInfo(ApiCallback callback, String inviteCode) {
        String[] metaArr = JniSharedLibraryWrapper.getInviteInfo().split(",");
        AjaxParams params = new AjaxParams();
        get(callback, DOMAIN + metaArr[0].replace(":inviteCode", inviteCode), params, InviteInfoBean.class, getMethodName(Thread.currentThread().getStackTrace()));
    }


    /**
     * 获取资料库get
     */
    public Result getGroupDatabase(String id, String pageCount, String pageNo, String type) throws Exception {
        String[] metaArr = JniSharedLibraryWrapper.getGroupDatabase().split(",");
        AjaxParams params = new AjaxParams();
        params.put(metaArr[1], pageCount);
        params.put(metaArr[2], pageNo);
        params.put(metaArr[3], type);
        return getSync(DOMAIN + metaArr[0].replace(":id", id), params, GroupDatabaseBean.class);
    }

}
