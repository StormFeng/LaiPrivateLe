package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupNear;
import com.lailem.app.jsonbean.activegroup.GroupNearNofoldBean;
import com.lailem.app.ui.group.GroupNearListActivity;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class GroupNearListDataSource extends BaseListDataSource<Object> {

    public static final String SORT_TIME = "1";
    public static final String SORT_HOT = "2";
    public static final String SORT_DISTANCE = "3";

    private String sort = SORT_DISTANCE;
    private String tagId;

    private ObjectWrapper section = new ObjectWrapper("排序", BaseMultiTypeListAdapter.TPL_SECTION);

    private String lat;
    private String lon;

    public GroupNearListDataSource(Context context, String tagId) {
        super(context);
        this.tagId = tagId;

        this.lat = ac.lat;
        this.lon = ac.lon;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        if (page == FIRST_PAGE_NO) {
            models.add(section);
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

        GroupNearNofoldBean list = (GroupNearNofoldBean) ApiClient.getApi().nofold(this.lat, this.lon, AppContext.PAGE_SIZE + "", page + "", sort, tagId);

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.getGroupList() != null) {
            for (GroupNear groupNear : list.getGroupList()) {
                groupNear.setItemViewType(GroupNearListActivity.TPL_GROUP_NEAR);
            }
            models.addAll(list.getGroupList());
        }

        hasMore = list.getGroupList() != null && list.getGroupList().size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

}
