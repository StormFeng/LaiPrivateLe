package com.lailem.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.lailem.app.adapter.datasource.MainActiveListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListFragment;
import com.lailem.app.loadfactory.MainActiveLoadViewFactory;
import com.lailem.app.tpl.ActiveNearTpl;
import com.lailem.app.tpl.MainActiveSectionTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

public class MainActiveFragment extends BaseSectionListFragment<Object> {
    public static final int TPL_ACTIVE_NEAR = 1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setDividerHeight(0);

        ArrayList<Object> cache = (ArrayList<Object>) ac.readObject(Const.KEY_MAIN_ACTIVE_CACHE);
        if (cache != null && cache.size() > 0) {
            resultList.addAll(cache);
            adapter.notifyDataSetChanged();
            listViewHelper.getLoadView().restore();
            listViewHelper.doPullRefreshing(true, 1000);
        } else {
            listViewHelper.refresh();
        }

    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new MainActiveListDataSource(_activity);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, MainActiveSectionTpl.class);
        tpls.add(TPL_ACTIVE_NEAR, ActiveNearTpl.class);
        return tpls;
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new MainActiveLoadViewFactory();
    }

}
