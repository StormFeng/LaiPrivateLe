package com.lailem.app.ui.active.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.widget.dynamic.DynamicVoteView;
import com.lailem.app.widget.dynamic.DynamicVoteView.OnVotedCallback;

import butterknife.Bind;

public class ActiveDetailVoteTpl extends BaseTpl<PublishInfo> implements OnVotedCallback {
	@Bind(R.id.dynamicVote)
	DynamicVoteView dynamicVoteView;
	private PublishInfo bean;

	public ActiveDetailVoteTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_active_detail_vote;
	}

	@Override
	public void setBean(PublishInfo bean, int position) {
		this.bean = bean;
		boolean isHasVote = bean.getIsVoted().equals(Const.HAS_VOTE);
		boolean isVoteOver = Func.isExpired(bean.getEndTime());
		boolean isShowCheckbox = false;
		boolean isShowColorPercent = isHasVote;
		boolean isShowItemVoteCount = isHasVote;
		boolean isShowActionBar = false;

		dynamicVoteView.render(bean, isShowCheckbox, isShowColorPercent, isShowItemVoteCount, isShowActionBar, isHasVote, isVoteOver, this);
	}

	@Override
	public void onVotedCallback() {
		this.bean.setIsVoted(Const.HAS_VOTE);
	}

}
