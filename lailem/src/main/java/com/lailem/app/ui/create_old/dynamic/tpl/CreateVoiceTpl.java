package com.lailem.app.ui.create_old.dynamic.tpl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VoiceModel;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.TDevice;

import butterknife.Bind;

public class CreateVoiceTpl extends BaseTpl<ObjectWrapper> implements OnClickListener {
    @Bind(R.id.voice)
	ImageView voiceImageView;
    @Bind(R.id.voiceText)
	TextView voiceTextView;
	VoiceModel bean;

	public CreateVoiceTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_create_voice;
	}

	@Override
	public void setBean(ObjectWrapper wrapper, int position) {
		setItemFillParent(getChildAt(0));
		this.bean = (VoiceModel) wrapper.getObject();
	    voiceImageView.setOnClickListener(this);
		
	}
	
	private void setItemFillParent(View item) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item.getLayoutParams();
		params.width = (int) TDevice.getScreenWidth();
		setLayoutParams(params);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.voice:
			AudioPlayUtil.getInstance().start(bean.getContent().getFilename());
			break;

		}
		
	}

}
