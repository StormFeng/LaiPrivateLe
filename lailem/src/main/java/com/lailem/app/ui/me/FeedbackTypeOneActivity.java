package com.lailem.app.ui.me;

import android.content.Intent;
import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.FeedBackOneDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.tpl.FeedBackAdviceTpl;
import com.lailem.app.tpl.FeedBackBugTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedbackTypeOneActivity extends BaseListActivity<Object> {
    public static final int TPL_ADVICE = 0;
    public static final int TPL_BUG = 1;

    public static final int REQUEST_SPECIAL = 1000;

    @Bind(R.id.topbar)
    TopBarView topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
    }

    private void initView() {
        topbar.setTitle("反馈类型").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new FeedBackOneDataSource(this);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_ADVICE, FeedBackAdviceTpl.class);
        tpls.add(TPL_BUG, FeedBackBugTpl.class);
        return tpls;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == REQUEST_SPECIAL) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

}
