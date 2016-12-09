package com.lailem.app.ui.me;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ViewFlipper;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.personal.BlacklistBean;
import com.lailem.app.tpl.BlackTpl;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BlackListActivity extends BaseActivity {
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.gridView)
    GridView mGridView;
    @Bind(R.id.vp)
    ViewFlipper vp;

    private BaseListAdapter<Object> adapter;
    private ArrayList<Object> data = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void loadData() {
        ApiClient.getApi().blacklist(this, ac.getLoginUid());
    }

    private void initView() {
        adapter = new BaseListAdapter<Object>(mGridView, this, data, BlackTpl.class, null);
        mGridView.setAdapter(adapter);
        adapter.setRunnable(new Runnable() {

            @Override
            public void run() {
                if (data.size() == 0) {
                    topbar.hideRight_tv();
                }
            }
        });
        showNormal();
    }

    private void showNormal() {
        topbar.recovery().setTitle("黑名单").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).setRightText("编辑", getEditClickListener());
        topbar.hideRight_tv();
    }

    private void showEdit() {
        topbar.recovery().setTitle("黑名单").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).setRightText("保存", getSaveClickListener());
    }

    private OnClickListener getEditClickListener() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                showEdit();
                adapter.setMode(BaseListAdapter.MODE_EDIT);
                adapter.notifyDataSetChanged();
            }
        };
    }

    private OnClickListener getSaveClickListener() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                showNormal();
                adapter.setMode(BaseListAdapter.MODE_NORMAL);
                adapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        if (res.isOK()) {
            BlacklistBean bean = (BlacklistBean) res;
            if (bean.getBlacklistList() != null) {
                for (int i = 0; i < bean.getBlacklistList().size(); i++) {
                    BlacklistBean.BlackBean blackBean = bean.getBlacklistList().get(i);
                    blackBean.setRemark(Func.formatNickName(_activity, blackBean.getBlackUserId(), blackBean.getNickname()));
                    data.add(blackBean);
                }
            }

            if (bean.getBlacklistList() == null || bean.getBlacklistList().size() == 0) {
                vp.setDisplayedChild(1);
            } else {
                topbar.showRight_tv();
                vp.setDisplayedChild(0);
            }
            adapter.notifyDataSetChanged();
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }

}
