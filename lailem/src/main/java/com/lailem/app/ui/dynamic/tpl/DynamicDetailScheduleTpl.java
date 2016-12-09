package com.lailem.app.ui.dynamic.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicScheduleView;

import butterknife.Bind;

public class DynamicDetailScheduleTpl extends BaseTpl<PublishInfo> {

    @Bind(R.id.dynamicSchedule)
    DynamicScheduleView dynamicScheduleView;

    public DynamicDetailScheduleTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_detail_schedule;
    }

    @Override
    public void setBean(PublishInfo bean, int position) {
        dynamicScheduleView.render(bean.getStartTime(), bean.getAddress(), bean.getContact(), bean.getTopic(), bean.getContent());
    }

}
