package com.lailem.app.ui.create_old.frag;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.CreateTypeGroupDataSource;
import com.lailem.app.base.BaseGridListFragment;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.GroupType;
import com.lailem.app.tpl.CreateTypeGroupTpl;
import com.lailem.app.ui.create_old.CreateTypeActivity;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TipDialog;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import butterknife.OnClick;

public class CreateTypeGroupFragment extends BaseGridListFragment<Object> {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        gridView.setNumColumns(3);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setVerticalSpacing((int) TDevice.dpToPixel(23));
        refreshGridView.setPullRefreshEnabled(false);
    }

    @OnClick(R.id.createGroup)
    public void clickCreateGroup() {
        if (adapter.getCheckedPosition() < 0) {
            showTipDialog();
            return;
        }
        BaseListAdapter baseListAdapter = (BaseListAdapter) listViewHelper.getAdapter();
        GroupType groupType = (GroupType) resultList.get(baseListAdapter.getCheckedPosition());
        String typeName = groupType.getName();
        UIHelper.showCreateNameActiveOrGroup(_activity, CreateTypeActivity.GROUP_TYPE_GROUP, groupType.getId(), typeName, "");
    }

    private void showTipDialog() {
        final TipDialog dialog = new TipDialog(_activity);
        dialog.config("温馨提示", "您需要为即将创建的群组选择一个类型才可以继续创建群组！", "知道了", new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_create_type_group;
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new CreateTypeGroupDataSource(_activity);
    }

    @Override
    protected Class getTemplateClass() {
        return CreateTypeGroupTpl.class;
    }
}
