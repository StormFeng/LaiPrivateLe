package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.fragment.MainGroupFragment;
import com.lailem.app.jsonbean.activegroup.GroupNear;
import com.lailem.app.jsonbean.activegroup.GroupNearNofoldBean;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class MainGroupListDataSource extends BaseListDataSource<Object> {

    private ObjectWrapper section = new ObjectWrapper("附近的群组", BaseMultiTypeListAdapter.TPL_SECTION);

    private String lat;
    private String lon;

    public MainGroupListDataSource(Context context) {
        super(context);
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

        GroupNearNofoldBean list = (GroupNearNofoldBean) ApiClient.getApi().nofold(this.lat, this.lon, AppContext.PAGE_SIZE + "", page + "", null, null);

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.getGroupList() != null) {
            for (GroupNear groupNear : list.getGroupList()) {
                groupNear.setItemViewType(MainGroupFragment.TPL_GROUP_NEAR);
            }
            models.addAll(list.getGroupList());
            if (page == FIRST_PAGE_NO && list.getGroupList().size() > 0) {
                models.add(0, section);
            }
        }

        hasMore = list.getGroupList() != null && list.getGroupList().size() == AppContext.PAGE_SIZE;

        this.page = page;

        if (page == FIRST_PAGE_NO) {
            //保存第一页缓存
            ac.saveObject(models, Const.KEY_MAIN_GROUP_CACHE);
        }
        return models;
    }
}
