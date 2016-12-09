package com.lailem.app.ui.me;

import android.content.Intent;
import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ProvinceListDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.dao.Region;
import com.lailem.app.tpl.ProvinceTpl;
import com.lailem.app.tpl.chat.ProvinceHeadTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseCityOneActivity extends BaseListActivity<Object> {
    public static final int REQUEST_CITY = 1000;

    public static final int TPL_PROVINCE = 0;
    public static final int TPL_HEAD = 1;

    @Bind(R.id.topbar)
    TopBarView topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        // 初始化数据
        listViewHelper.refresh();
    }

    private void initView() {
        topbar.setTitle("所在城市").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new ProvinceListDataSource(this);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_PROVINCE, ProvinceTpl.class);
        tpls.add(TPL_HEAD, ProvinceHeadTpl.class);
        return tpls;
    }

    @Override
    public void onEndRefresh(IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
        super.onEndRefresh(adapter, result);
        if (resultList.size() == 0) {
            return;
        }
        String cityName = _Bundle.getString(ChooseCityTwoActivity.BUNDLE_KEY_CITYNAME);
        for (int i = 1; i < resultList.size(); i++) {
            Region region = (Region) resultList.get(i);
            if (cityName.equals(region.getName())) {
                BaseListAdapter<Object> baseListAdapter = (BaseListAdapter<Object>) adapter;
                baseListAdapter.setCheckedPosition(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ChooseCityOneActivity.REQUEST_CITY) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
