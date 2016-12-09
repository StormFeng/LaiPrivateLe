package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ActiveNearListDataSource;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.ActivityType;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.TDevice;
import com.lailem.app.widget.FlowLayout;
import com.lailem.app.widget.pulltorefresh.helper.ListViewHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class ActiveConditionsTpl extends BaseTpl<Object> {

    @Bind(R.id.noTimeFilter)
    RadioButton noTimeFilter_cb;
    @Bind(R.id.noTypeFilter)
    RadioButton noTypeFilter;

    @Bind(R.id.types)
    FlowLayout typesFilterLayout;
    @Bind(R.id.timeFilters)
    FlowLayout timeFiltersLayout;

    private ArrayList<ActivityType> typeFilters;
    private String[] timeFilterNames;
    private String[] timeFilterValues;

    public ActiveConditionsTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();

        initTimeFilter();
        initTypeFilter();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_conditions;
    }

    private void initTypeFilter() {
        typeFilters = (ArrayList<ActivityType>) ac.readObject(Const.ACTIVITY_TYPE_LIST);
        for (int i = 0; i < typeFilters.size(); i++) {
            final ActivityType type = typeFilters.get(i);
            //去除投票类型
            if (Const.TYPE_ID_VOTE.equals(type.getId())) {
                continue;
            }
            final RadioButton item = (RadioButton) View.inflate(_activity, R.layout.view_active_condition_item, null);
            item.setText(type.getName());
            final int index = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < typesFilterLayout.getChildCount(); j++) {
                        RadioButton radioButton = (RadioButton) typesFilterLayout.getChildAt(j);
                        if (index != j) {
                            radioButton.setChecked(false);
                        }
                        item.setChecked(true);
                    }
                    noTypeFilter.setChecked(false);
                    ActiveNearListDataSource ds = (ActiveNearListDataSource) dataSource;
                    ds.setTypeId(type.getId());
                    refresh();
                }
            });
            LayoutParams params = new LayoutParams((int) (TDevice.getScreenWidth() / 5.5f), LayoutParams.WRAP_CONTENT);
            typesFilterLayout.addView(item, params);
        }
    }

    private void initTimeFilter() {
        timeFilterNames = _activity.getResources().getStringArray(R.array.active_near_time_filter_name);
        timeFilterValues = _activity.getResources().getStringArray(R.array.active_near_time_filter_value);
        for (int i = 0; i < timeFilterNames.length; i++) {
            String name = timeFilterNames[i];
            RadioButton item = (RadioButton) View.inflate(_activity, R.layout.view_active_condition_item, null);
            item.setText(name);
            final int index = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < timeFiltersLayout.getChildCount(); j++) {
                        if (index != j) {
                            RadioButton radioButton = (RadioButton) timeFiltersLayout.getChildAt(j);
                            radioButton.setChecked(false);
                        }
                    }
                    noTimeFilter_cb.setChecked(false);
                    ActiveNearListDataSource ds = (ActiveNearListDataSource) dataSource;
                    ds.setTimeFilter(timeFilterValues[index]);
                    refresh();
                }
            });
            LayoutParams params = new LayoutParams((int) (TDevice.getScreenWidth() / 5.5f), LayoutParams.WRAP_CONTENT);
            timeFiltersLayout.addView(item, params);
        }
    }

    @Override
    public void setBean(Object bean, int position) {

    }

    @OnClick({R.id.noTimeFilter, R.id.noTypeFilter})
    public void clickNoFilter(View v) {
        ActiveNearListDataSource ds = (ActiveNearListDataSource) dataSource;
        switch (v.getId()) {
            case R.id.noTimeFilter:
                for (int j = 0; j < timeFiltersLayout.getChildCount(); j++) {
                    RadioButton radioButton = (RadioButton) timeFiltersLayout.getChildAt(j);
                    radioButton.setChecked(false);
                }
                ds.setTimeFilter(null);
                break;
            case R.id.noTypeFilter:
                for (int j = 0; j < typesFilterLayout.getChildCount(); j++) {
                    RadioButton radioButton = (RadioButton) typesFilterLayout.getChildAt(j);
                    radioButton.setChecked(false);
                }
                ds.setTypeId(null);
                break;
        }
        refresh();
    }

    private void refresh() {
        ListViewHelper lvh = (ListViewHelper) this.listViewHelper;
        lvh.doPullRefreshing(true, 0);
    }

}
