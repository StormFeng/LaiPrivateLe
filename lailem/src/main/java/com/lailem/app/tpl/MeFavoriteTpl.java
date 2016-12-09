package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;

import com.lailem.app.R;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.util.ArrayList;

public class MeFavoriteTpl extends BaseTpl<Object> implements OnActionClickListener {

	private ActionDialog dialog;

	public MeFavoriteTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
//		return R.layout.item_me_favorite;
		return 0;
	}

	@Override
	public void setBean(Object bean, int position) {

	}

	public void todo() {
		if (dialog == null) {
			dialog = new ActionDialog(_activity);
			ActionData actionData1 = new ActionData("取消", R.drawable.ic_dialog_favorite_cancel_selector);
			ActionData actionData2 = new ActionData("删除", R.drawable.ic_dialog_ok_selector);
			ArrayList<ActionData> actionDatas = new ArrayList<ActionData>();
			actionDatas.add(actionData1);
			actionDatas.add(actionData2);
			DialogActionData dialogActionData = new DialogActionData(null, null, actionDatas);
			dialog.init(dialogActionData);
			dialog.setOnActionClickListener(this);
		}
		dialog.show();
	}

	@Override
	public void onActionClick(ActionDialog dialog, View View, int position) {
		switch (position) {
		case 0:

			break;
		case 1:

			break;
		}
	}

}
