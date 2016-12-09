package com.lailem.app.widget.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.VoteInfo;
import com.lailem.app.jsonbean.activegroup.VoteInfo.Item;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.TDevice;
import com.lailem.app.widget.VoterListDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicVoteView extends LinearLayout {
	public static final int[] drawables = { R.drawable.bg_vote_item_color_1, R.drawable.bg_vote_item_color_2, R.drawable.bg_vote_item_color_3, R.drawable.bg_vote_item_color_4 };
	private AppContext ac;
	private BaseActivity activity;

	@Bind(R.id.title)
	TextView title_tv;
	@Bind(R.id.remark)
	TextView remark_tv;
	@Bind(R.id.choiceLayout)
	LinearLayout choiceLayout;
	@Bind(R.id.actionArea)
	View actionArea;
	@Bind(R.id.hasVoteCount)
	TextView hasVoteCount_tv;
	@Bind(R.id.addVote)
	TextView addVote_tv;

	// 数据源
	private VoteInfo voteInfo;
	// 投票成功回调
	private OnVotedCallback onVotedCallback;

	private int totalCount;// 票总数
	private int checkCount;// 当前选中数

	private boolean isShowCheckbox;// 是否显示checkbox
	private boolean isShowColorPercent;// 是否显示颜色百分比
	private boolean isShowItemVoteCount;// 是否显示每个选项投票数
	private boolean isShowActionBar;// 是否显示查看投票人按钮和投票按钮
	private boolean isHasVote;;// 当前用户是否已经投过票
	private boolean isVoteOver;// 投票是否已经结束

	// 投票成功回调接口
	public interface OnVotedCallback {
		void onVotedCallback();
	}

	public DynamicVoteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DynamicVoteView(Context context) {
		super(context);
		init(context);
	}

	void init(Context context) {
		this.activity = (BaseActivity) context;
		this.ac = (AppContext) context.getApplicationContext();
		View.inflate(context, R.layout.view_dynamic_vote, this);
		ButterKnife.bind(this, this);
	}

	public void render(VoteInfo voteInfo, boolean isShowCheckbox, boolean isShowColorPercent, boolean isShowItemVoteCount, boolean isShowActionBar, boolean isHasVote, boolean isVoteOver,
			OnVotedCallback onVotedCallback) {
		this.voteInfo = voteInfo;
		this.isShowCheckbox = isShowCheckbox;
		this.isShowColorPercent = isShowColorPercent;
		this.isShowItemVoteCount = isShowItemVoteCount;
		this.isShowActionBar = isShowActionBar;
		this.isHasVote = isHasVote;
		this.isVoteOver = isVoteOver;
		this.onVotedCallback = onVotedCallback;

		render();
	}

	private void render() {
		// 渲染选项
		renderItems();
		// 渲染名称
		renderName();
		// 渲染checkbox
		renderCheckbox();
		// 渲染颜色百分比
		renderColorPercent();
		// 渲染每条选项票数
		renderItemVoteCount();
		// 渲染actionBar
		renderActionBar();
		// 渲染其他内容
		renderOther();
	}

	/**
	 * 渲染选项
	 */
	private void renderItems() {
		ArrayList<Item> items = voteInfo.getItems();
		if (items == null) {
			items = new ArrayList<Item>();
		}
		if (choiceLayout.getChildCount() <= items.size()) {
			for (int i = 0; i < items.size(); i++) {
				if (i < choiceLayout.getChildCount()) {
					choiceLayout.getChildAt(i).setVisibility(VISIBLE);
				} else {
					View view = View.inflate(activity, R.layout.item_vote, null);
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
	}

	/**
	 * 渲染选项名称
	 */
	private void renderName() {
		for (int i = 0; i < voteInfo.getItems().size(); i++) {
			FrameLayout fl = (FrameLayout) ((LinearLayout) choiceLayout.getChildAt(i)).getChildAt(0);
			TextView name_tv = (TextView) fl.getChildAt(1);
			name_tv.setText(voteInfo.getItems().get(i).getName());
		}
	}

	/**
	 * 渲染checkbox
	 */
	private void renderCheckbox() {
		for (int i = 0; i < voteInfo.getItems().size(); i++) {
			final CheckBox checkBox = (CheckBox) ((LinearLayout) choiceLayout.getChildAt(i)).getChildAt(1);
			if (isShowCheckbox) {
				// 显示checkbox
				checkBox.setVisibility(VISIBLE);
				checkBox.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (checkBox.isChecked()) {
							checkCount++;
						} else {
							checkCount--;
						}
						int selectCount = Integer.parseInt(voteInfo.getSelectCount());
						if (checkCount > selectCount) {
							AppContext.showToast("最多选择" + selectCount + "项");
							checkBox.setChecked(false);
							checkCount--;
							return;
						}
					}
				});
			} else {
				// 隐藏checkbox
				checkBox.setVisibility(GONE);
			}
		}
	}

	/**
	 * 渲染选项中的颜色百分比
	 */
	private void renderColorPercent() {
		if (isShowColorPercent) {
			// 显示色彩背景
			post(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < voteInfo.getItems().size(); i++) {
						FrameLayout fl = (FrameLayout) ((LinearLayout) choiceLayout.getChildAt(i)).getChildAt(0);
						ImageView bg_iv = (ImageView) fl.getChildAt(0);
						bg_iv.setImageResource(drawables[i % drawables.length]);
						bg_iv.setVisibility(VISIBLE);
						int voteCount = Integer.parseInt(voteInfo.getItems().get(i).getVoteCount());
						int width = totalCount == 0 ? 0 : (int) (fl.getWidth() * (voteCount * 1.0f / totalCount));
						int height = (int) TDevice.dpToPixel(25);
						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
						bg_iv.setLayoutParams(params);
					}
				}
			});
		} else {
			// 隐藏色彩背景
			for (int i = 0; i < voteInfo.getItems().size(); i++) {
				FrameLayout fl = (FrameLayout) ((LinearLayout) choiceLayout.getChildAt(i)).getChildAt(0);
				ImageView bg_iv = (ImageView) fl.getChildAt(0);
				bg_iv.setVisibility(INVISIBLE);
			}
		}
	}

	/**
	 * 渲染每个选项投票数
	 */
	private void renderItemVoteCount() {
		totalCount = 0;
		for (int i = 0; i < voteInfo.getItems().size(); i++) {
			FrameLayout fl = (FrameLayout) ((LinearLayout) choiceLayout.getChildAt(i)).getChildAt(0);
			TextView voteCount_tv = (TextView) fl.getChildAt(2);
			String voteCount = voteInfo.getItems().get(i).getVoteCount();
			totalCount += Integer.parseInt(voteCount);
			if (isShowItemVoteCount) {
				// 显示每个选项投票数
				voteCount_tv.setVisibility(VISIBLE);
				voteCount_tv.setText(voteCount + "票");
			} else {
				// 隐藏每个选项投票数
				voteCount_tv.setVisibility(INVISIBLE);
			}
		}
	}

	/**
	 * 渲染actionBar
	 */
	private void renderActionBar() {
		if (isShowActionBar) {
			actionArea.setVisibility(VISIBLE);
			hasVoteCount_tv.setText(voteInfo.getVoteCount() + "人已投票");
			if (isVoteOver) {
				// 投票已经终止
				addVote_tv.setText("已过期");
				addVote_tv.setEnabled(false);
				addVote_tv.setTextColor(getResources().getColor(R.color.text_medium_2));
				addVote_tv.setBackgroundResource(R.drawable.bg_capsule_gray_hollow);
				return;
			}
			if (isHasVote) {
				// 已经投票
				addVote_tv.setText("已投票");
				addVote_tv.setEnabled(false);
				addVote_tv.setTextColor(getResources().getColor(R.color.text_medium_2));
				addVote_tv.setBackgroundResource(R.drawable.bg_capsule_gray_hollow);
				return;
			}
			addVote_tv.setText("投票");
			addVote_tv.setEnabled(true);
			addVote_tv.setTextColor(getResources().getColor(R.color.orange));
			addVote_tv.setBackgroundResource(R.drawable.bg_capsule_orange_hollow);
		} else {
			actionArea.setVisibility(GONE);

		}
	}

	/**
	 * 渲染其他内容
	 */
	private void renderOther() {
		title_tv.setText("Q：" + voteInfo.getTopic());
		remark_tv.setText(voteInfo.getDesc());
	}

	@OnClick(R.id.addVote)
	public void addVote() {
		if (isHasVote || isVoteOver) {
			return;
		}

		StringBuilder voteItemIds = new StringBuilder();
		for (int i = 0; i < voteInfo.getItems().size(); i++) {
			CheckBox checkBox = (CheckBox) ((LinearLayout) choiceLayout.getChildAt(i)).getChildAt(1);
			if (checkBox.isChecked()) {
				voteItemIds.append(",").append(voteInfo.getItems().get(i).getId());
			}
		}

		if (voteItemIds.length() < 1) {
			AppContext.showToast("请完成选择");
			return;
		} else {
			voteItemIds.deleteCharAt(0);
		}

		ApiClient.getApi().vote(new ApiCallbackAdapter() {
			@Override
			public void onApiStart(String tag) {
				super.onApiStart(tag);
				activity.showWaitDialog();
			}

			@Override
			public void onApiSuccess(Result res, String tag) {
				super.onApiSuccess(res, tag);
				activity.hideWaitDialog();
				if (res.isOK()) {
					AppContext.showToast("投票成功");
					updateState();
				} else {
					ac.handleErrorCode(activity, res.errorCode, res.errorInfo);
				}
			}

			@Override
			protected void onApiError(String tag) {
				super.onApiError(tag);
				activity.hideWaitDialog();
			}
		}, ac.getLoginUid(), voteInfo.getId(), voteItemIds.toString());
	}

	/**
	 * 更新状态
	 */
	private void updateState() {
		// 投票成功回调
		if (onVotedCallback != null) {
			onVotedCallback.onVotedCallback();
		}
		// 更新投票总数及每个选项的投票数
		totalCount += Integer.parseInt(voteInfo.getSelectCount());
		for (int i = 0; i < voteInfo.getItems().size(); i++) {
			CheckBox checkBox = (CheckBox) ((LinearLayout) choiceLayout.getChildAt(i)).getChildAt(1);
			if (checkBox.isChecked()) {
				int x = Integer.parseInt(voteInfo.getItems().get(i).getVoteCount());
				voteInfo.getItems().get(i).setVoteCount((x + 1) + "");
			}
		}
		// 更新投票人数
		voteInfo.setVoteCount(Integer.parseInt(voteInfo.getVoteCount()) + 1 + "");
		// 更新状态为已投票
		voteInfo.setIsVoted(Const.HAS_VOTE);
		isHasVote = true;
		isShowCheckbox = false;
		isShowColorPercent = true;
		isShowItemVoteCount = true;

		render();

	}

	@OnClick(R.id.hasVoteCount)
	public void clickHasVoteCount() {
		VoterListDialog dialog = new VoterListDialog(getContext());
		dialog.setParams(voteInfo.getId(), voteInfo.getVoteCount() + "");
		dialog.show();
	}

}
