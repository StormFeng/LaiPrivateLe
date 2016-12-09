package com.lailem.app.widget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.VoterListDataSource;
import com.lailem.app.base.BaseListDialog;
import com.lailem.app.jsonbean.dynamic.VoterListBean.Voter;
import com.lailem.app.tpl.VoterTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoterListDialog extends BaseListDialog<Object> {
	private String voteId;
	private String totalCount;

	public VoterListDialog(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_voter_list;
	}

	@Override
	protected IDataSource<Object> getDataSource() {
		return new VoterListDataSource(_activity, voteId);
	}

	@Override
	protected ArrayList<Class> getTemplateClasses() {
		ArrayList<Class> tpls = new ArrayList<Class>();
		tpls.add(VoterTpl.class);
		return tpls;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Object obj = resultList.get(position);
		if (obj instanceof Voter) {
			Voter voter = (Voter) obj;
			UIHelper.showMemberInfoAlone(getContext(),voter.getId());
		}
		dismiss();
	}

	public void setParams(String voteId, String totalCount) {
		this.voteId = voteId;
		this.totalCount = totalCount;
	}

	@Override
	public void show() {
		super.show();
		ButterKnife.bind(this);
		TextView title_tv = (TextView) findViewById(R.id.title);
		title_tv.setText(totalCount + "个人已投票");
		listViewHelper.refresh();
	}

	@OnClick(R.id.close)
	public void close() {
		dismiss();
	}
}
