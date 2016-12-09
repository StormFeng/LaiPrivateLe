package com.lailem.app.widget.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel.VoteItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateVoteView extends LinearLayout {
	public static final int[] drawables = { R.drawable.bg_vote_item_color_1, R.drawable.bg_vote_item_color_2, R.drawable.bg_vote_item_color_3, R.drawable.bg_vote_item_color_4 };
	private AppContext ac;
	private BaseActivity activity;

	@Bind(R.id.title)
	TextView title_tv;
	@Bind(R.id.remark)
	TextView remark_tv;
	@Bind(R.id.choiceLayout)
	LinearLayout choiceLayout;

	// 数据源
	private VoteModel bean;

	public CreateVoteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CreateVoteView(Context context) {
		super(context);
		init(context);
	}

	void init(Context context) {
		this.activity = (BaseActivity) context;
		this.ac = (AppContext) context.getApplicationContext();

		View.inflate(context, R.layout.view_create_vote, this);
		ButterKnife.bind(this, this);
	}

	public void render(VoteModel bean) {
		this.bean = bean;
		ArrayList<VoteItem> items = bean.getContent().getVoteItems();
		if (items == null) {
			items = new ArrayList<VoteItem>();
		}
		if (choiceLayout.getChildCount() <= items.size()) {
			for (int i = 0; i < items.size(); i++) {
				if (i < choiceLayout.getChildCount()) {
					choiceLayout.getChildAt(i).setVisibility(VISIBLE);
				} else {
					View view = View.inflate(activity, R.layout.item_view_create_vote, null);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					choiceLayout.addView(view, i, params);
				}
			}
		} else if (choiceLayout.getChildCount() > items.size()) {
			for (int i = 0; i < choiceLayout.getChildCount(); i++) {
				if (i < items.size()) {
					choiceLayout.getChildAt(i).setVisibility(VISIBLE);
				} else {
					choiceLayout.getChildAt(i).setVisibility(GONE);
				}
			}
		}

		renderName();

		title_tv.setText("Q：" + bean.getContent().getTopic());
		remark_tv.setText(bean.getContent().getDescription());
	}

	private void renderName() {
		for (int i = 0; i < bean.getContent().getVoteItems().size(); i++) {
			FrameLayout fl = (FrameLayout) ((LinearLayout) choiceLayout.getChildAt(i)).getChildAt(0);
			TextView name_tv = (TextView) fl.getChildAt(0);
			name_tv.setText(bean.getContent().getVoteItems().get(i).getName());
		}
	}
}
