package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.datasource.MeFavoriteListDataSource;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.personal.CollectBean.Collect;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.me.MeDynamicActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.util.ArrayList;

import butterknife.OnClick;

public class MeFavoriteActionBarTpl extends BaseTpl<Collect> implements OnActionClickListener {

    private ActionDialog dialog;
    private Collect bean;
    private int position;

    public MeFavoriteActionBarTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_actionbar;
    }

    @Override
    public void setBean(Collect bean, int position) {
        this.bean = bean;
        this.position = position;
    }

    @OnClick(R.id.cancelFavor)
    public void cancelFavor() {
        if (dialog == null) {
            dialog = new ActionDialog(_activity);
            ActionData actionData1 = new ActionData("取消", R.drawable.ic_dialog_favorite_cancel_selector);
            ActionData actionData2 = new ActionData("删除", R.drawable.ic_dialog_ok_selector);
            ArrayList<ActionData> actionDatas = new ArrayList<ActionData>();
            actionDatas.add(actionData1);
            actionDatas.add(actionData2);
            DialogActionData dialogActionData = new DialogActionData("取消收藏？", null, actionDatas);
            dialog.init(dialogActionData);
            dialog.setOnActionClickListener(this);
        }
        dialog.show();
    }

    @OnClick(R.id.showMore)
    public void showMore() {
        if (MeFavoriteListDataSource.COLLECT_TYPE_ACTIVE.equals(bean.getCollectType())) {
            if (Const.TYPE_ID_VOTE.equals(bean.getActivityInfo().getTypeId())) {
                // 投票活动
                UIHelper.showVoteActiveDetail(_activity, bean.getActivityInfo().getId());
            } else {
                // 普通活动
                UIHelper.showActiveDetail(_activity, bean.getActivityInfo().getId());
            }
        } else if (MeFavoriteListDataSource.COLLECT_TYPE_DYNAMIC.equals(bean.getCollectType())) {
            if (Const.DYNA_TYPE_CREATE_ACTIVE.equals(bean.getDynamic().getDynaType())) {
                UIHelper.showActiveDetail(_activity, bean.getDynamic().getCreateActivityInfo().getId());
            } else {
                UIHelper.showDynamicDetail(_activity, bean.getDynamic().getId(), -1);
            }
        }
    }

    @Override
    public void onActionClick(ActionDialog dialog, View View, int position) {
        switch (position) {
            case 0:
                dialog.dismiss();
                break;
            case 1:
                ApiClient.getApi().collectDel(this, ac.getLoginUid(), bean.getId());
                break;
        }
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        _activity.showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        _activity.hideWaitDialog();
        if (res.isOK()) {
            AppContext.showToast("删除成功");
            for (int i = position; i >= 0; i--) {
                int itemViewType = ((Base) data.get(i)).getItemViewType();
                // 遇到上一个条目的actionbar停止
                if (i != position && itemViewType == MeDynamicActivity.TPL_ACTIONBAR) {
                    break;
                }
                // 遇到时间分隔条目
                if (itemViewType == BaseMultiTypeListAdapter.TPL_SECTION) {
                    boolean flag = false;
                    // 是最后一条
                    if (data.size() == 1) {
                        flag = true;
                    }
                    // 当前时间段已没有动态
                    if (data.size() > i + 1 && ((Base) data.get(i + 1)).getItemViewType() == BaseMultiTypeListAdapter.TPL_SECTION) {
                        flag = true;
                    }
                    // 该时间分隔条目是列表中的最后一条
                    if (i == data.size() - 1) {
                        flag = true;
                    }
                    if (flag) {
                        data.remove(i);
                    }
                    break;
                }
                data.remove(i);
            }
            adapter.notifyDataSetChanged();
            ac.setProperty(Const.USER_COLLECT_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_COLLECT_COUNT)) - 1) + "");
        } else {
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        _activity.hideWaitDialog();
    }

}
