package com.lailem.app.widget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ZanListDataSource;
import com.lailem.app.base.BaseListDialog;
import com.lailem.app.jsonbean.dynamic.LikeListBean.Like;
import com.lailem.app.tpl.ZanTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZanListDialog extends BaseListDialog<Object> {

	private String id;
	private String likeType;
	private String totalCount;

	public ZanListDialog(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_zan_list;
	}

	@Override
	protected IDataSource<Object> getDataSource() {
		return new ZanListDataSource(_activity, id, likeType);
	}

	@Override
	protected ArrayList<Class> getTemplateClasses() {
		ArrayList<Class> tpls = new ArrayList<Class>();
		tpls.add(ZanTpl.class);
		return tpls;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Object obj = resultList.get(position);
		if (obj instanceof Like) {
			Like like = (Like) obj;
			UIHelper.showMemberInfoAlone(getContext(),like.getId());
		}
		dismiss();
	}

	public void setParams(String id, String likeType, String totalCount) {
		this.id = id;
		this.likeType = likeType;
		this.totalCount = totalCount;
	}

	@Override
	public void show() {
		super.show();
		ButterKnife.bind(this);
		TextView title_tv = (TextView) findViewById(R.id.title);
		title_tv.setText(totalCount + "个人觉得很赞");
		listViewHelper.refresh();
	}

	@OnClick(R.id.close)
	public void close() {
		dismiss();
	}

}
