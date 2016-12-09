package com.lailem.app.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.GroupScheduleBean.GroupSchedule;
import com.lailem.app.utils.Func;
import com.lailem.app.widget.dynamic.DynamicScheduleView;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class GroupScheduleTpl extends BaseTpl<GroupSchedule> {

    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.date)
    TextView date_tv;
    @Bind(R.id.dynamicSchedule)
    DynamicScheduleView dynamicScheduleView;

    public GroupScheduleTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_schedule;
    }

    @Override
    public void setBean(GroupSchedule bean, int position) {
        if (Func.checkImageTag(bean.getCreatorInfo().getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getCreatorInfo().getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(bean.getCreatorInfo().getRemark());
        date_tv.setText(bean.getAddTime());
        dynamicScheduleView.render(bean.getStartTime(), bean.getAddress(), bean.getContact(), bean.getTopic(), bean.getContent());
    }

}
