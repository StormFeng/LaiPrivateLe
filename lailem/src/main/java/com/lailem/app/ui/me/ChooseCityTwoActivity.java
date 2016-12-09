package com.lailem.app.ui.me;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.CityListDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.dao.Region;
import com.lailem.app.tpl.CityTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseCityTwoActivity extends BaseListActivity<Object> {
    public static final String BUNDLE_KEY_PROVINCENAME = "provinceName";
    public static final String BUNDLE_KEY_CITYNAME = "cityName";
    public static final String BUNDLE_KEY_CITYID = "cityId";
    public static final String BUNDLE_KEY_PROVINCEID = "provinceId";
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
        topbar.setTitle(_Bundle.getString(BUNDLE_KEY_PROVINCENAME)).setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new CityListDataSource(this, _Bundle.getString(BUNDLE_KEY_PROVINCEID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(CityTpl.class);
        return tpls;
    }

    @Override
    public void onEndRefresh(IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
        super.onEndRefresh(adapter, result);
        if (resultList.size() == 0) {
            return;
        }
        String cityName = _Bundle.getString(BUNDLE_KEY_CITYNAME);
        for (int i = 0; i < resultList.size(); i++) {
            Region region = (Region) resultList.get(i);
            if (cityName.equals(region.getName())) {
                BaseListAdapter<Object> baseListAdapter = (BaseListAdapter<Object>) adapter;
                baseListAdapter.setCheckedPosition(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();

    }
}
