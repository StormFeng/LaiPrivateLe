package com.lailem.app.tpl;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.bean.Result;

import butterknife.OnClick;

public class CreateTypeActiveDynamic extends BaseTpl<Result> {

	public CreateTypeActiveDynamic(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_create_type_active_dynamic;
	}

	@OnClick(R.id.camera)
	public void clickCamera() {
		AppContext.showToast("拍照");
	}

	@OnClick(R.id.photos)
	public void clickPhotos() {
		AppContext.showToast("相册");
	}

	@OnClick(R.id.video)
	public void clickVideo() {
		AppContext.showToast("视频");
	}

	@OnClick(R.id.vioce)
	public void clickVioce() {
		AppContext.showToast("录音");
	}

	@OnClick(R.id.text)
	public void clickText() {
		AppContext.showToast("文本");
	}

	@OnClick(R.id.loc)
	public void clickLoc() {
		AppContext.showToast("位置");
	}

	@OnClick(R.id.vote)
	public void clickVote() {
		AppContext.showToast("投票");
	}

	@OnClick(R.id.schedule)
	public void clickSchedule() {
		AppContext.showToast("日程");
	}

	@OnClick(R.id.message)
	public void clickMessage() {
		AppContext.showToast("群通知");
	}

	@Override
	public void setBean(Result bean, int position) {

	}

}
