package com.lailem.app.tpl;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ActivityTypeWrapper;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.ActivityType;
import com.lailem.app.ui.create_old.CreateTypeActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;

public class CreateTypeActiveTpl extends BaseTpl<ActivityTypeWrapper> {
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.icon)
    ImageView icon_tv;
    @Bind(R.id.hasDraft)
    TextView hasDraft_tv;

    private ActivityType bean;

    public CreateTypeActiveTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_create_type_active;
    }

    @Override
    public void setBean(ActivityTypeWrapper wrapper, int position) {
        this.bean = wrapper.getActivityType();

        name_tv.setText(bean.getName());
        if (Func.checkImageTag(bean.getIconName(), icon_tv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getIconName())).into(icon_tv);
        }

        if (wrapper.isHasDraft()) {
            hasDraft_tv.setVisibility(VISIBLE);
        } else {
            hasDraft_tv.setVisibility(GONE);
        }
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        String parentId = "";
        Bundle bundle = _activity.getIntent().getExtras();
        if (bundle != null) {
            parentId = bundle.getString(Const.BUNDLE_KEY_GROUP_ID);
        }
        String typeName = bean.getName();
        UIHelper.showCreateNameActiveOrGroup(_activity, CreateTypeActivity.GROUP_TYPE_ACTIVE, bean.getId(), typeName, parentId);
    }
}
