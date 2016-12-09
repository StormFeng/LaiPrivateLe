package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.ActiveNearBean;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean;
import com.lailem.app.ui.active.ActiveNearListActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;
import java.util.HashMap;

public class ActiveNearListDataSource extends BaseListDataSource<Object> {

    private HashMap<String, String> typeColorMap;

    public static final String SORT_TIME = "1";
    public static final String SORT_HOT = "2";
    public static final String SORT_DISTANCE = "3";

    private String sort = SORT_DISTANCE;
    private String timeFilter;
    private String typeId;

    private ObjectWrapper conditionItem = new ObjectWrapper("条件", ActiveNearListActivity.TPL_CONDITION);
    private ObjectWrapper sortItem = new ObjectWrapper("排序", BaseMultiTypeListAdapter.TPL_SECTION);

    private String lat;
    private String lon;

    public ActiveNearListDataSource(Context context) {
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
        if (page == FIRST_PAGE_NO) {
            models.add(conditionItem);
            models.add(sortItem);
        }

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

        ActiveNearBean list = (ActiveNearBean) ApiClient.getApi().near(this.lat, this.lon, AppContext.PAGE_SIZE + "", page + "", sort, timeFilter, typeId);

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.getActivityList() != null) {
            for (int i = 0; i < list.getActivityList().size(); i++) {
                ActiveNearBean.ActiveNear activeNear = list.getActivityList().get(i);
                activeNear.setItemViewType(ActiveNearListActivity.TPL_ACTIVE_NEAR);
                activeNear.setTypeColor(typeColorMap.get(activeNear.getTypeId()));
                //设置备注名
                activeNear.getCreatorInfo().setRemark(Func.formatNickName(_activity, activeNear.getCreatorInfo().getId(), activeNear.getCreatorInfo().getNickname()));
            }
            models.addAll(list.getActivityList());
        }

        hasMore = list.getActivityList() != null && list.getActivityList().size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTimeFilter() {
        return timeFilter;
    }

    public void setTimeFilter(String timeFilter) {
        this.timeFilter = timeFilter;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
