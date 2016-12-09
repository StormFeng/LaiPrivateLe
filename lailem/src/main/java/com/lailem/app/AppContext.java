package com.lailem.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mobstat.StatService;
import com.lailem.app.base.BaseApplication;
import com.lailem.app.broadcast.BaiduLocReceiver;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.cache.UserCache;
import com.lailem.app.dao.SysProperty;
import com.lailem.app.dao.User;
import com.lailem.app.jni.JniSharedLibraryWrapper;
import com.lailem.app.jsonbean.personal.UserBean;
import com.lailem.app.service.LLService;
import com.lailem.app.share_ex.ShareBlock;
import com.lailem.app.ui.chat.expression.ExpressionUtil;
import com.lailem.app.ui.qupai.VideoRecordActivity;
import com.lailem.app.utils.ConfigManager;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.FileUtils;
import com.lailem.app.utils.NetStateManager;
import com.lailem.app.utils.ResourceUtil;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static com.lailem.app.AppConfig.KEY_FRITST_START;
import static com.lailem.app.AppConfig.KEY_LOAD_IMAGE;

/**
 * 全局应用程序类
 */
public class AppContext extends BaseApplication {
    public static boolean DEBUG = true;

    public static final int PAGE_SIZE = 20;// 默认分页大小
    private static AppContext instance;

    public ImageLoader imageLoader;// 图片加载
    private ResourceUtil resourceUtil;// 资源工具类
    private LocationClient mLocationClient;// 定位
    private MyLocationListener mMyLocationListener;

    public String lon = "";
    public String lat = "";
    public String provinceName = "";
    public String cityName = "";
    public String provinceId = "";
    public String cityId = "";


    private String loginUid;
    private String token;
    private boolean isLogin;


    private HashMap<String, Object> shareObjects = new HashMap<String, Object>();

    private Object loginCallbackObject;//登录成功后回调使用
    private String loginCallbackMethodName;//登录成功回调使用
    public boolean isInitShareReady;//分享是否已经初始化
    public boolean isInitVideoReady;//视频是否初始化
    private boolean isReady;//基本的初始化
    private boolean isInitFileReady;//文件相关初始化
    private boolean isInitLoginReady;//用户信息初始化

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        String processName = getProcessName(this, android.os.Process.myPid());
        if (processName != null) {
            KLog.e("processName=" + processName);
            if (processName.equals(getPackageName())) {
                //必要的初始化资源操作
                init();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void init() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                KLog.e("init", "AppContext init");
                long start = System.currentTimeMillis();
                //日志工具
                KLog.init(DEBUG);
                //百度统计日志
                StatService.setDebugOn(DEBUG);
                KLog.e("init", System.currentTimeMillis() - start + "百度统计日志");
                start = System.currentTimeMillis();
                //初始化Service
                LLService.start(getApplicationContext(), LLService.FLAG_DEFAULT);
                KLog.e("init", System.currentTimeMillis() - start + "初始化Service");
                start = System.currentTimeMillis();
                // 初始化网络状态
                NetStateManager.getInstance().initNetworkState(getApplicationContext());
                KLog.e("init", System.currentTimeMillis() - start + "初始化网络状态");
                start = System.currentTimeMillis();
                // 初始化定位
                initLocationClient();
                KLog.e("init", System.currentTimeMillis() - start + "初始化定位");
                start = System.currentTimeMillis();
                // 初始化资源工具
                resourceUtil = new ResourceUtil(getApplicationContext());
                KLog.e("init", System.currentTimeMillis() - start + "初始化资源工具");
                start = System.currentTimeMillis();
                // 初始化图片加载
                initUImageLoader();
                KLog.e("init", System.currentTimeMillis() - start + "初始化图片加载");
                start = System.currentTimeMillis();
                // 初始化登录信息
                initLogin();
                KLog.e("init", System.currentTimeMillis() - start + "初始化登录信息");
                start = System.currentTimeMillis();
                //初始化表情
                ExpressionUtil.getInstace().getFileText(getApplicationContext());
                KLog.e("init", System.currentTimeMillis() - start + "初始化表情");
                initFile();
                isReady = true;
            }
        });
    }

    /**
     * 文件相关初始化
     */
    public void initFile() {
        KLog.e("初始化文件");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //保存logo到本地
                initLogoImage();
                //检查并清理文件
                FileUtils.checkFile(getApplicationContext());
                isInitFileReady = true;
            }
        }).start();
    }

    /**
     * 初始化分享
     */
    public void initShare() {
        KLog.e("初始化分享");
        ShareBlock.getInstance().initShare(JniSharedLibraryWrapper.weixinAppId(), JniSharedLibraryWrapper.weiboApiKey(), JniSharedLibraryWrapper.qqAppId(), JniSharedLibraryWrapper.weixinAppSecret());
        isInitShareReady = true;
    }

    /**
     * 初始化视频
     */
    public void initVideo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                KLog.e("初始化视频");
                VideoRecordActivity.init(getApplicationContext());
                isInitVideoReady = true;
            }
        }).start();
    }

    private void initLocationClient() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        int span = 1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.start();
    }

    private void initUImageLoader() {
        int maxWidth = (int) TDevice.getScreenWidth();
        int maxHeight = (int) TDevice.getScreenHeight();
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.empty).showImageForEmptyUri(R.drawable.empty).showImageOnFail(R.drawable.empty)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(1000)
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().memoryCacheExtraOptions(maxWidth, maxHeight).diskCacheExtraOptions(maxWidth, maxHeight, null).threadPoolSize(10)
                .build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        L.disableLogging();
    }

    /**
     * 保存logo到本地
     */
    private void initLogoImage() {
        String fileToPath = FileUtils.getImageDir(this) + "/logo.png";
        File file = new File(fileToPath);
        try {
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.logo_square);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppContext.showToast("创建文件失败,请检查sd卡是否可用!");
        }
    }

    public void initLogin() {
        this.token = getProperty(Const.USER_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            isLogin = true;
            loginUid = getProperty(Const.USER_ID);
        } else {
            token = null;
            loginUid = null;
        }
        isInitLoginReady = true;
    }

    private void initTestData() {
        if (AppContext.DEBUG) {

        }
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);

    }

    public List<SysProperty> getProperties() {
        return ConfigManager.getConfigManager().getProperties();
    }

    public void setProperty(String key, String value) {
        ConfigManager.getConfigManager().setProperty(key, value);
    }

    public String getProperty(String key) {
        return ConfigManager.getConfigManager().getProperty(key);
    }

    public void removeProperties(String... key) {
        ConfigManager.getConfigManager().removeProperties(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;

    }

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    public void saveUserInfo(final UserBean user) {
        this.loginUid = user.getUserInfo().getId();
        this.isLogin = true;
        this.token = user.getUserInfo().getToken();
        setProperty(Const.USER_BIRTHDAY, user.getUserInfo().getBirthday());
        setProperty(Const.USER_SEX, user.getUserInfo().getSex());
        setProperty(Const.USER_ID, user.getUserInfo().getId());
        setProperty(Const.USER_PROVINCE, user.getUserInfo().getProvince());
        setProperty(Const.USER_CITY, user.getUserInfo().getCity());
        setProperty(Const.USER_AREA, user.getUserInfo().getArea());
        setProperty(Const.USER_PHONE, user.getUserInfo().getPhone());
        setProperty(Const.USER_EMAIL, user.getUserInfo().getEmail());
        setProperty(Const.USER_NAME, user.getUserInfo().getName());
        setProperty(Const.USER_NICKNAME, user.getUserInfo().getNickname());
        setProperty(Const.USER_USERNAME, user.getUserInfo().getUsername());
        setProperty(Const.USER_BHEAD, user.getUserInfo().getHeadBPicName());
        setProperty(Const.USER_SHEAD, user.getUserInfo().getHeadSPicName());
        setProperty(Const.USER_SIGN, user.getUserInfo().getPersonalizedSignature());
        setProperty(Const.USER_REGISTER_TYPE, user.getUserInfo().getRegisterType());
        setProperty(Const.USER_TYPE, user.getUserInfo().getUserType());
        setProperty(Const.USER_TOKEN, user.getUserInfo().getToken());
        setProperty(Const.USER_COLLECT_COUNT, user.getUserInfo().getCollectCount());
        setProperty(Const.USER_DYNAMIC_COUNT, user.getUserInfo().getDynamicCount());
        User user1 = new User();
        user1.setHead(user.getUserInfo().getHeadSPicName());
        user1.setNickname(user.getUserInfo().getNickname());
        user1.setUserId(user.getUserInfo().getId());
        UserCache.getInstance(_context).put(user1);

    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = null;
        this.isLogin = false;
        this.token = null;
        removeProperties(Const.USER_ID, Const.USER_PROVINCE, Const.USER_CITY, Const.USER_BIRTHDAY, Const.USER_SEX, Const.USER_AREA, Const.USER_EMAIL, Const.USER_NAME,
                Const.USER_NICKNAME, Const.USER_USERNAME, Const.USER_BHEAD, Const.USER_SHEAD, Const.USER_SIGN, Const.USER_REGISTER_TYPE, Const.USER_TYPE, Const.USER_TOKEN, Const.USER_COLLECT_COUNT);

    }

    public String getLoginUid() {
        return loginUid;
    }

    public String getToken() {
        return token;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public boolean isLogin(Object object, String methodName) {
        this.loginCallbackObject = object;
        this.loginCallbackMethodName = methodName;
        return isLogin;
    }

    /**
     * 执行登录成功回调
     */
    public void excuteLoginCallback() {
        if (isNeedLoginCallback()) {
            try {
                Method method = loginCallbackObject.getClass().getMethod(loginCallbackMethodName);
                method.invoke(loginCallbackObject);
            } catch (Exception e) {
                AppContext.showToast("loginCallbackMethod not exist");
                e.printStackTrace();
            }
        }
    }

    public boolean isNeedLoginCallback() {
        return loginCallbackObject != null && !TextUtils.isEmpty(loginCallbackMethodName);
    }

    /**
     * 用户注销
     */
    public void logout() {
        this.isLogin = false;
        this.loginCallbackMethodName = "";
        this.loginCallbackObject = "";
        cleanLoginInfo();
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {

    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    public boolean isExistDataCache(String cachefile) {
        boolean exist = false;
//        File data = getFileStreamPath(cachefile);
        File data = new File(getFilesDir() + File.separator + cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Serializable readObject(String file) {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     * @throws IOException
     */
    public boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 设置是否显示图片
     *
     * @param flag
     */
    public static void setLoadImage(boolean flag) {
        set(KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 判断程序首次启动
     *
     * @return
     */
    public static boolean isFristStart() {
        return getPreferences().getBoolean(KEY_FRITST_START, true);
    }

    /**
     * 修改程序是否首次启动
     *
     * @param frist
     */
    public static void setFristStart(boolean frist) {
        set(KEY_FRITST_START, frist);
    }

    /**
     * 处理后台返回的错误码
     *
     * @param context
     * @param errorCode
     */
    public void handleErrorCode(final Context context, final String errorCode, final String errorInfo) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (TextUtils.isEmpty(errorCode)) {
                    return;
                }
                // 登录过期
                if ("token_error".equals(errorCode) || "tokenTimeLimit".equals(errorCode) || "tokenError".equals(errorCode)) {
                    AppContext.showToast("您的账号已经在其他设备登陆，请重新登录");
                    logout();
                    BroadcastManager.sendLogoutBroadcast(_context);

                    UIHelper.showLogin(context, true);
                } else {
                    AppContext.showToast(errorInfo);
                }
            }
        };
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(runnable);
        } else if (context instanceof AppContext) {
            new Handler().post(runnable);
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (!"4.9E-324".equals(location.getLatitude())) {
                lat = location.getLatitude() + "";
            }
            if (!"4.9E-324".equals(location.getLongitude())) {
                lon = location.getLongitude() + "";
            }
            if (!TextUtils.isEmpty(location.getProvince()) && !TextUtils.isEmpty(location.getCity())) {
                if (location.getProvince().equals(location.getCity())) {
                    //直辖市
                    provinceName = location.getCity().replace("市", "");
                    cityName = location.getCity().replace("市", "");
                } else {
                    //非直辖市
                    provinceName = location.getProvince().replace("省", "");
                    cityName = location.getCity().replace("市", "");
                }

            }

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            sb.append("\ncity : ");
            sb.append(location.getCity());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            String address = location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet();
            String action = "";
            if (isRequestLocError()) {
                action = BaiduLocReceiver.ACTION_BAIDU_LOC_FAILURE;
            } else {
                action = BaiduLocReceiver.ACTION_BAIDU_LOC_SUCCESS;
            }
            BroadcastManager.sendBaiduLocBroadcast(AppContext.this, action, lat, lon, provinceName, cityName, address);
        }

    }

    public void putShareObject(String key, Object object) {
        shareObjects.put(key, object);
    }

    public Object getShareObject(String key) {
        return shareObjects.get(key);
    }


    public void setLoginCallbackMethodName(String loginCallbackMethodName) {
        this.loginCallbackMethodName = loginCallbackMethodName;
    }

    public void setLoginCallbackObject(Object loginCallbackObject) {
        this.loginCallbackObject = loginCallbackObject;
    }

    private boolean isRequestLocError() {
        return "4.9E-324".equals(this.lat) || "4.9E-324".equals(this.lon) || TextUtils.isEmpty(this.lat) || TextUtils.isEmpty(this.lon);
    }

    /**
     * 请求定位
     */
    public void requestLoc() {
        this.mLocationClient.requestLocation();
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}
