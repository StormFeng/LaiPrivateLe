package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.personal.FeedbackTypeBean;
import com.lailem.app.jsonbean.personal.FeedbackTypeBean.FeedbackType;
import com.lailem.app.ui.me.FeedbackTypeOneActivity;

import java.util.ArrayList;

public class FeedBackOneDataSource extends BaseListDataSource<Object> {

    public FeedBackOneDataSource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        FeedbackTypeBean list = (FeedbackTypeBean) ApiClient.getApi().feedbackType();

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        for (int i = 0; i < list.getFeedbackTypeList().size(); i++) {
            FeedbackType feedbackType = list.getFeedbackTypeList().get(i);
            if (i == 0) {
                feedbackType.setItemViewType(FeedbackTypeOneActivity.TPL_ADVICE);
                continue;
            }
            feedbackType.setItemViewType(FeedbackTypeOneActivity.TPL_BUG);
        }
        models.addAll(list.getFeedbackTypeList());
        hasMore = false;
        return models;
    }

}
