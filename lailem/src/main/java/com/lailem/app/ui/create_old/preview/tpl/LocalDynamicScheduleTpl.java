package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.ScheduleModel;
import com.lailem.app.widget.dynamic.DynamicScheduleView;

import butterknife.Bind;

public class LocalDynamicScheduleTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.dynamicSchedule)
    DynamicScheduleView dynamicScheduleView;
    private ScheduleModel bean;

    public LocalDynamicScheduleTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_preview_schedule;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (ScheduleModel) wrapper.getObject();
        dynamicScheduleView.render(bean.getContent().getStartTime(), bean.getContent().getAddress(), bean.getContent().getContact(), bean.getContent().getTopic(), bean.getContent().getContent());
    }

}
