package com.lailem.app.adapter.datasource;

import android.content.Context;
import android.os.Bundle;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.ActivityTypeWrapper;
import com.lailem.app.bean.CreateActiveCache;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.ActivityType;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class CreateTypeActiveDataSource extends BaseListDataSource<Object> {

    private boolean isGroupActive = false;
    private String parentId;

    public CreateTypeActiveDataSource(Context context) {
        super(context);
    }

    public CreateTypeActiveDataSource(Context context, boolean isGroupActive) {
        super(context);
        this.isGroupActive = isGroupActive;
        Bundle bundle = _activity.getIntent().getExtras();
        if (bundle != null) {
            this.parentId = bundle.getString(Const.BUNDLE_KEY_GROUP_ID);
        }
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        ArrayList<ActivityType> list = (ArrayList<ActivityType>) ac.readObject(Const.ACTIVITY_TYPE_LIST);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (isGroupActive && Const.TYPE_ID_VOTE.equals(list.get(i).getId())) {
                    continue;
                }
                if (ac.isLogin()) {
                    Object object = ac.readObject(Func.buildCreateTypeCacheName(ac.getLoginUid(), parentId, list.get(i).getId()));
                    if (object != null && object instanceof CreateActiveCache) {
                        models.add(new ActivityTypeWrapper(list.get(i), true, (CreateActiveCache) object));
                    } else {
                        models.add(new ActivityTypeWrapper(list.get(i), false));
                    }

                } else {
                    models.add(new ActivityTypeWrapper(list.get(i), false));
                }

            }
        }
        hasMore = false;
        return models;
    }
}
