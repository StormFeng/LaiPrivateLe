package com.lailem.app.utils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.InviteCode;

public class InviteInfoManager {
	BaseActivity activity;
	String groupId;

	public InviteInfoManager(BaseActivity activity) {
		this.activity = activity;
	}

	OnInviteInfoListener onInviteInfoListener;

	public void setOnInviteInfoListener(OnInviteInfoListener onInviteInfoListener, String groupId) {
		this.onInviteInfoListener = onInviteInfoListener;
		this.groupId = groupId;
		getCode();
	}

	private void getCode() {
		activity.showWaitDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Result result = ApiClient.getApi().groupInviteCode(AppContext.getInstance().getLoginUid(), groupId);
					if (result.isOK()) {
						InviteCode inviteCode = (InviteCode) result;
						success(Const.URL_INVITE_LETTER+inviteCode.getInviteCode());
					} else {
						hideWaitDialog();
						fail();
					}
				} catch (Exception e) {
					hideWaitDialog();
					fail();
					e.printStackTrace();
				}
			}
		}).start();

	}

	public interface OnInviteInfoListener {
		public void onInviteInfoSuccess(String inviteInfo);

		public void onInviteInfoFail();
	}

	private void hideWaitDialog() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				activity.hideWaitDialog();
			}
		});
	}
	
	private void success(final String inviteInfo){
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				onInviteInfoListener.onInviteInfoSuccess(inviteInfo);
			}
		});
	}

	private void fail(){
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				onInviteInfoListener.onInviteInfoFail();
			}
		});
	}
}
