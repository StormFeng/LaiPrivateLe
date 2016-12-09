package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.dynamic.LikeListBean;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class ZanListDataSource extends BaseListDataSource<Object> {

    private String id;
    private String likeType;

    public ZanListDataSource(Context context, String id, String likeType) {
        super(context);
        this.id = id;
        this.likeType = likeType;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        LikeListBean list = (LikeListBean) ApiClient.getApi().likeList(id, likeType, AppContext.PAGE_SIZE + "", page + "");

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.isOK()) {
            if (list.getLikeList() != null && list.getLikeList().size() > 0) {
                for (int i = 0; i < list.getLikeList().size(); i++) {
                    LikeListBean.Like like = list.getLikeList().get(i);
                    like.setRemark(Func.formatNickName(_activity, like.getId(), like.getNickname()));
                    models.add(like);
                }
            }
        } else {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
        }

        hasMore = list.getLikeList() != null && list.getLikeList().size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }

}
