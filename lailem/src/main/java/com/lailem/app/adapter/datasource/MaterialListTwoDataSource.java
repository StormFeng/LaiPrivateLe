package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.dynamic.PicMaterialListBean;

import java.util.ArrayList;

public class MaterialListTwoDataSource extends BaseListDataSource<Object> {

    private String id;

    public MaterialListTwoDataSource(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        PicMaterialListBean list = (PicMaterialListBean) ApiClient.getApi().picMaterial_Type(id);

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        models.addAll(list.getPicMaterialList());
        hasMore = false;
        return models;
    }

}
