package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.fragment.MainActiveFragment;
import com.lailem.app.jsonbean.activegroup.ActiveNearBean;
import com.lailem.app.jsonbean.activegroup.ActiveNearBean.ActiveNear;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActiveListDataSource extends BaseListDataSource<Object> {

    private HashMap<String, String> typeColorMap;

    private ObjectWrapper section = new ObjectWrapper("附近的活动", BaseMultiTypeListAdapter.TPL_SECTION);

    private String lat;
    private String lon;

    public MainActiveListDataSource(Context context) {
        super(context);
        init();
    }

    private void init() {
        //初始化颜色
        typeColorMap = new HashMap<String, String>();
        ArrayList<CommonConfigBean.ActivityType> list = (ArrayList<CommonConfigBean.ActivityType>) ac.readObject(Const.ACTIVITY_TYPE_LIST);
        for (CommonConfigBean.ActivityType activityType : list) {
            typeColorMap.put(activityType.getId(), activityType.getColor());
        }

        this.lat = ac.lat;
        this.lon = ac.lon;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();

        if (Func.istLocValid(lat, lon)) {
            ac.requestLoc();
            Thread.sleep(3000);
            this.lat = ac.lat;
            this.lon = ac.lon;
            if (Func.istLocValid(lat, lon)) {
                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppContext.showToast("定位失败，请检查定位设置或稍后重试");
                    }
                });
                hasMore = false;
                return models;
            }
        }

        ActiveNearBean list = (ActiveNearBean) ApiClient.getApi().near(this.lat, this.lon, AppContext.PAGE_SIZE + "", page + "", null, null, null);

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.getActivityList() != null) {
            for (ActiveNear activeNear : list.getActivityList()) {
                activeNear.setItemViewType(MainActiveFragment.TPL_ACTIVE_NEAR);
                activeNear.setTypeColor(typeColorMap.get(activeNear.getTypeId()));
                //设置备注名
                activeNear.getCreatorInfo().setRemark(Func.formatNickName(_activity, activeNear.getCreatorInfo().getId(), activeNear.getCreatorInfo().getNickname()));
            }
            models.addAll(list.getActivityList());
            if (page == FIRST_PAGE_NO && list.getActivityList().size() > 0) {
                models.add(0, section);
            }
        }
        hasMore = list.getActivityList() != null && list.getActivityList().size() == AppContext.PAGE_SIZE;
        this.page = page;

        if (page == FIRST_PAGE_NO) {
            //保存第一页缓存
            ac.saveObject(models, Const.KEY_MAIN_ACTIVE_CACHE);
        }
        return models;
    }
}
