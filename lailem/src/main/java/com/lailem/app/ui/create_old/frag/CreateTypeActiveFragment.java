package com.lailem.app.ui.create_old.frag;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.CreateTypeActiveDataSource;
import com.lailem.app.base.BaseGridListFragment;
import com.lailem.app.tpl.CreateTypeActiveTpl;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

public class CreateTypeActiveFragment extends BaseGridListFragment<Object> {

    private boolean isGroupActive;

    private boolean isInited = false;

    public CreateTypeActiveFragment() {

    }

    public CreateTypeActiveFragment(boolean isGroupActive) {
        this.isGroupActive = isGroupActive;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_create_type_active;
    }

    private void initView() {
        gridView.setNumColumns(3);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        refreshGridView.setPullRefreshEnabled(false);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new CreateTypeActiveDataSource(_activity, isGroupActive);
    }

    @Override
    protected Class getTemplateClass() {
        return CreateTypeActiveTpl.class;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInited) {
            listViewHelper.refresh();
        } else {
            isInited = true;
        }

    }
}
