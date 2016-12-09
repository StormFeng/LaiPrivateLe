package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.dynamic.VoterListBean;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class VoterListDataSource extends BaseListDataSource<Object> {

    private String id;

    public VoterListDataSource(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        VoterListBean list = (VoterListBean) ApiClient.getApi().voterList(id);

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.isOK()) {
            if (list.getVoterList() != null && list.getVoterList().size() > 0) {
                for (int i = 0; i < list.getVoterList().size(); i++) {
                    VoterListBean.Voter voter = list.getVoterList().get(i);
                    voter.setRemark(Func.formatNickName(_activity, voter.getId(), voter.getNickname()));
                    models.add(voter);
                }
            }
        } else {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
        }

        hasMore = list.getVoterList() != null && list.getVoterList().size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }
}
