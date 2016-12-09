package com.lailem.app.ui.active.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicVoiceView;

import butterknife.Bind;

public class ActiveDetailVoiceTpl extends BaseTpl<PublishInfo> {

    @Bind(R.id.dynamicVoice)
    DynamicVoiceView dynamicVoiceView;

    public ActiveDetailVoiceTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_detail_voice;
    }

    @Override
    public void setBean(PublishInfo bean, int position) {
        dynamicVoiceView.render(adapter, bean.getDuration(), bean.getText(), ApiClient.getFileUrl(bean.getFilename()), position);
    }
}
