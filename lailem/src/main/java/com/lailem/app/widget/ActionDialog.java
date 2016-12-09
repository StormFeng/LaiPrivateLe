package com.lailem.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableRow;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.utils.DialogTipUtils;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionDialog extends Dialog {

	public interface OnActionClickListener {
		void onActionClick(ActionDialog dialog, View View, int position);
	}

	private Context context;
	private Activity _activity;

	private OnActionClickListener onActionClickListener;

	private DialogActionData data;

	@Bind(R.id.title)
	TextView title_tv;
	@Bind(R.id.content)
	TextView content_tv;
	@Bind(R.id.actions)
	TableRow actions;

	private static final int DEFAULT_THEME = R.style.confirm_dialog;

	public ActionDialog(Context context) {
		super(context, DEFAULT_THEME);
		init(context);
	}

	public ActionDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		this._activity = (Activity) context;
		Window w = this.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.setCanceledOnTouchOutside(true);
		View contentView = View.inflate(context, R.layout.dialog_action, null);
		this.setContentView(contentView);
		ButterKnife.bind(this, contentView);
	}

	public ActionDialog init(DialogActionData data) {
		if (data == null) {
			return this;
		}
		if (!TextUtils.isEmpty(data.getTitle())) {
			title_tv.setText(data.getTitle());
		}else{
			title_tv.setText("选项");
		}
		if (!TextUtils.isEmpty(data.getContent())) {
			content_tv.setText(data.getContent());
		}else{
			content_tv.setText(DialogTipUtils.getInstance(_activity).getContent());
		}
		if (data.getActions() == null && data.getActions().size() == 0) {
			return this;
		}
		for (int i = 0; i < data.getActions().size(); i++) {
			ActionData actionData = data.getActions().get(i);
			final TextView child = (TextView) View.inflate(context, R.layout.item_dialog_action, null);
			final int position = i;
			child.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (onActionClickListener != null) {
						onActionClickListener.onActionClick(ActionDialog.this, child, position);
					}
					dismiss();
				}
			});
			child.setText(actionData.getName());
			child.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable(actionData.getIcon()), null, null);
			actions.addView(child);
		}

		return this;

	}

	@OnClick(R.id.close)
	public void close() {
		dismiss();
	}

	public ActionDialog setOnActionClickListener(OnActionClickListener onActionClickListener) {
		this.onActionClickListener = onActionClickListener;
		return this;
	}

	public static class DialogActionData {
		private String title;
		private String content;
		private ArrayList<ActionData> actions;

		public DialogActionData(String title, String content, ArrayList<ActionData> actions) {
			super();
			this.title = title;
			this.content = content;
			this.actions = actions;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getContent() {
			return content;
		}

		public void setActions(ArrayList<ActionData> actions) {
			this.actions = actions;
		}

		public ArrayList<ActionData> getActions() {
			return actions;
		}

		public static class ActionData {
			private String name;
			private int icon;

			public ActionData(String name, int icon) {
				super();
				this.name = name;
				this.icon = icon;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getName() {
				return name;
			}

			public void setIcon(int icon) {
				this.icon = icon;
			}

			public int getIcon() {
				return icon;
			}
		}

		public static DialogActionData build(String title, String content, ActionData... actionDatas) {
			ArrayList<ActionData> actions = new ArrayList<ActionData>();
			for (int i = 0; i < actionDatas.length; i++) {
				actions.add(actionDatas[i]);
			}
			DialogActionData dialogActionData = new DialogActionData(title, content, actions);
			return dialogActionData;
		}
	}

}
