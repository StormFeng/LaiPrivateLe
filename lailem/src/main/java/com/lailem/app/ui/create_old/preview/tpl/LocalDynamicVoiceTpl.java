package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VoiceModel;
import com.lailem.app.widget.dynamic.DynamicVoiceView;

import butterknife.Bind;

public class LocalDynamicVoiceTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.dynamicVoice)
    DynamicVoiceView dynamicVoiceView;
    private VoiceModel bean;

    public LocalDynamicVoiceTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_preview_voice;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (VoiceModel) wrapper.getObject();
        dynamicVoiceView.render(adapter, bean.getContent().getDuration(), bean.getContent().getText(), ApiClient.getFileUrl(bean.getContent().getFilename()), position);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dynamicVoiceView.stop();
    }

}
