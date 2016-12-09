package com.lailem.app.utils;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiCallback;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.dao.DaoFactory;
import com.lailem.app.dao.Region;
import com.lailem.app.dao.RegionDao;
import com.lailem.app.dao.SysProperty;
import com.lailem.app.dao.SysPropertyDao;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.RegionBean;
import com.lailem.app.jsonbean.dynamic.VCodeBean;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 公共配置管理
 *
 * @author XuYang
 */
public class ConfigManager implements ApiCallback {
    public static final String COUNTRY = "0";// （国家）
    public static final String PROVINCE = "1";// （省）
    public static final String DIRECT_CITY = "2";// （直辖市）
    public static final String CITY = "3";// （直辖市）
    public static final String DISTANCE = "4"; // （区）
    private static ConfigManager configManager;
    private final String API_TAG_COMMON_CONFIG = "getCommonConfig";// 系统配置
    private final String API_TAG_CONFIG_VERSION = "getConfigVersion";// 系统配置版本检查
    private AppContext ac;
    private Context context;
    private SysPropertyDao sysPropertyDao;
    private RegionDao regionDao;

    private OnConfigUpdateCallback onConfigUpdateCallback;

    public static ConfigManager getConfigManager() {
        if (configManager == null) {
            synchronized (ConfigManager.class) {
                if (configManager == null) {
                    configManager = new ConfigManager();
                }
            }
            configManager = new ConfigManager();
        }
        return configManager;
    }

    public ConfigManager() {
        this.context = AppContext.getInstance();
        this.ac = AppContext.getInstance();
        this.sysPropertyDao = DaoFactory.getInstance(context).getSysPropertyDao();
        this.regionDao = DaoFactory.getInstance(context).getRegionDao();
    }

    /**
     * 获取最新配置
     */
    public void getNewConfig() {
        ApiClient.getApi().getCommonConfig(this, ac.getProperty(Const.CONFIG_V_CODE));
    }

    /**
     * 获取最新配置版本号
     */
    public void getNewConfigVersion() {
        ApiClient.getApi().getConfigVersion(this);
    }

    /**
     * 系统配置版本检查
     */
    public void checkConfigVersion(OnConfigUpdateCallback onConfigUpdateCallback) {
        this.onConfigUpdateCallback = onConfigUpdateCallback;
        String code = getProperty(Const.CONFIG_V_CODE);
        if (TextUtils.isEmpty(code)) {
            getNewConfig();
        } else {
            getNewConfigVersion();
        }
    }

    // ---------------------region的增删改查---------------------------------
    public long insert(Region region) {
        return regionDao.insert(region);
    }

    public void delete(Region region) {
        regionDao.delete(region);
    }

    public void update(Region region) {
        regionDao.update(region);
    }

    /**
     * 查询省（不包含直辖市）
     *
     * @return
     */
    public List<Region> queryProvince() {
        QueryBuilder<Region> qb = regionDao.queryBuilder();
        qb.where(RegionDao.Properties.RType.eq(PROVINCE));
        return qb.list();
    }

    /**
     * 查询直辖市
     *
     * @return
     */
    public List<Region> queryDirectCity() {
        QueryBuilder<Region> qb = regionDao.queryBuilder();
        qb.where(RegionDao.Properties.RType.eq(DIRECT_CITY));
        return qb.list();
    }

    /**
     * 查询市
     *
     * @return
     */
    public List<Region> queryCity(String pId) {
        QueryBuilder<Region> qb = regionDao.queryBuilder();
        qb.where(RegionDao.Properties.PId.eq(pId), RegionDao.Properties.RType.eq(CITY));
        return qb.list();
    }

    /**
     * 查询城市名对应的RId
     *
     * @param name
     * @return
     */
    public String queryRId(String name) {
        QueryBuilder<Region> qb = regionDao.queryBuilder();
        qb.where(RegionDao.Properties.Name.eq(name));
        List<Region> list = qb.list();
        if (list != null && list.size() > 0) {
            return list.get(0).getRId();
        } else {
            return null;
        }
    }

    /**
     * 查询系统配置
     *
     * @param name
     * @return
     */
    public SysProperty querySysProperty(String name) {
        QueryBuilder<SysProperty> qb = sysPropertyDao.queryBuilder();
        qb.where(SysPropertyDao.Properties.Name.eq(name));
        List<SysProperty> list = qb.list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 判断系统配置是否需要更新
     *
     * @param newCode
     */
    private boolean isConfigNeedUpdate(String newCode) {
        String oldCode = getProperty(Const.CONFIG_V_CODE);
        try {
            if (Integer.parseInt(newCode) > Integer.parseInt(oldCode)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToast("系统配置版本号转换错误");
            return false;
        }
    }

    /**
     * 更新地区列表
     *
     * @param regionList
     */
    private synchronized void updateRegion(final ArrayList<RegionBean> regionList) {
        regionDao.deleteAll();
        ArrayList<Region> regions = new ArrayList<Region>();
        for (int i = 0; i < regionList.size(); i++) {
            RegionBean bean = regionList.get(i);
            Region r = new Region();
            r.setCreateTime(System.currentTimeMillis());
            r.setPId(bean.getpId());
            r.setRId(bean.getrId());
            r.setName(bean.getName());
            r.setRType(bean.getrType());
            regions.add(r);
        }
        regionDao.insertInTx(regions);

    }

    /**
     * 设置系统配置
     *
     * @param name
     * @param value
     */
    public void setProperty(String name, String value) {
        SysProperty property = querySysProperty(name);
        if (property == null) {
            SysProperty p = new SysProperty();
            p.setCreateTime(System.currentTimeMillis());
            p.setName(name);
            p.setPropValue(value);
            sysPropertyDao.insert(p);
        } else {
            property.setPropValue(value);
            sysPropertyDao.update(property);
        }
    }

    /**
     * 获取系统配置
     *
     * @param name
     * @return
     */
    public String getProperty(String name) {
        SysProperty property = querySysProperty(name);
        if (property != null) {
            return property.getPropValue();
        }
        return "";
    }

    /**
     * 移除系统配置
     *
     * @param key
     */
    public void removeProperty(String key) {
        QueryBuilder<SysProperty> qb = sysPropertyDao.queryBuilder();
        qb.where(SysPropertyDao.Properties.Name.eq(key));
        sysPropertyDao.deleteInTx(qb.list());
    }

    /**
     * 移除多条系统配置
     *
     * @param key
     */
    public void removeProperties(String... key) {
        QueryBuilder<SysProperty> qb = sysPropertyDao.queryBuilder();
        qb.where(SysPropertyDao.Properties.Name.in(key));
        sysPropertyDao.deleteInTx(qb.list());
    }

    /**
     * 获取所有系统配置
     *
     * @return
     */
    public List<SysProperty> getProperties() {
        QueryBuilder<SysProperty> qb = sysPropertyDao.queryBuilder();
        return qb.list();
    }

    @Override
    public void onApiStart(String tag) {
        if (onConfigUpdateCallback != null) {
            onConfigUpdateCallback.onStart();
        }
    }

    @Override
    public void onApiLoading(long count, long current, String tag) {

    }

    @Override
    public void onApiSuccess(final Result res, String tag) {
        if (API_TAG_COMMON_CONFIG.equals(tag)) {
            if (res.isOK()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CommonConfigBean config = (CommonConfigBean) res;
                        // 更新地区列表
                        if (config.getRegionList() != null && config.getRegionList().size() > 0) {
                            updateRegion(config.getRegionList());
                        }
                        // 群标签列表
                        if (config.getGroupTypeList() != null && config.getGroupTypeList().size() > 0) {
                            ac.saveObject(config.getGroupTypeList(), Const.GROUP_TYPE_LIST);
                        }
                        // 群类型列表
                        if (config.getActivityTypeList() != null && config.getActivityTypeList().size() > 0) {
                            ac.saveObject(config.getActivityTypeList(), Const.ACTIVITY_TYPE_LIST);
                        }
                        // 群标签
                        if (config.getGroupTagList() != null && config.getGroupTagList().size() > 0) {
                            ac.saveObject(config.getGroupTagList(), Const.GROUP_TAG_LIST);
                        }
                        // 更新手机号验证方式
                        if (!TextUtils.isEmpty(config.getMsgSwitch())) {
                            setProperty(Const.PHONE_VERIFY_TYPE, config.getMsgSwitch());
                        }
                        // 更新活动上线人数
                        if (!TextUtils.isEmpty(config.getActivityPLimit())) {
                            setProperty(Const.ACTIVITY_PLIMIT, config.getActivityPLimit());
                        }
                        // 更新群人数上限
                        if (!TextUtils.isEmpty(config.getGroupPLimit())) {
                            setProperty(Const.GROUP_PLIMIT, config.getGroupPLimit());
                        }
                        // 更新用户参与群组数上限
                        if (!TextUtils.isEmpty(config.getUserJGLimit())) {
                            setProperty(Const.USER_JG_LIMIT, config.getUserJGLimit());
                        }
                        // 更新系统配置版本号
                        setProperty(Const.CONFIG_V_CODE, config.getvCode());
                    }
                }).start();
                if (onConfigUpdateCallback != null) {
                    onConfigUpdateCallback.onSuccess();
                }
            } else {
                if (onConfigUpdateCallback != null) {
                    onConfigUpdateCallback.onError();
                }
            }
        } else if (API_TAG_CONFIG_VERSION.equals(tag)) {
            if (res.isOK()) {
                VCodeBean vcodeBean = (VCodeBean) res;
                if (isConfigNeedUpdate(vcodeBean.getvCode())) {
                    getNewConfig();
                } else {
                    if (onConfigUpdateCallback != null) {
                        onConfigUpdateCallback.onSuccess();
                    }
                }
            } else {
                ac.handleErrorCode(context, res.errorCode, res.errorInfo);
            }
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        if (API_TAG_COMMON_CONFIG.equals(tag)) {
            if (onConfigUpdateCallback != null) {
                onConfigUpdateCallback.onError();
            }
        } else if (API_TAG_CONFIG_VERSION.equals(tag)) {
            if (onConfigUpdateCallback != null) {
                onConfigUpdateCallback.onSuccess();
            }
        }
    }

    @Override
    public void onParseError(String tag) {
        if (API_TAG_COMMON_CONFIG.equals(tag)) {
            if (onConfigUpdateCallback != null) {
                onConfigUpdateCallback.onError();
            }
        } else if (API_TAG_CONFIG_VERSION.equals(tag)) {
            if (onConfigUpdateCallback != null) {
                onConfigUpdateCallback.onSuccess();
            }
        }
    }

    public interface OnConfigUpdateCallback {
        void onStart();

        void onSuccess();

        void onError();
    }


}
