package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicVoiceView;

import butterknife.Bind;

public class MeFavoriteVoiceTpl extends BaseTpl<PublishInfo> {

    @Bind(R.id.dynamicVoice)
    DynamicVoiceView dynamicVoiceView;

    public MeFavoriteVoiceTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_voice;
    }

    @Override
    public void setBean(PublishInfo bean, int position) {
        dynamicVoiceView.render(adapter, bean.getDuration(), bean.getText(), ApiClient.getFileUrl(bean.getFilename()), position);
    }

}
