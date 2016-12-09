package com.lailem.app.ui.create_old.dynamic;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.CreateDynamicPreviewDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicAddressTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicAvatarTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicImageTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicLineTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicScheduleTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicTextTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicVideoTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicVoiceTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicVoteTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateDynamicPreviewActivity extends BaseListActivity<Object> {

	public static final int TPL_TEXT = 0;
	public static final int TPL_IMAGE = 1;
	public static final int TPL_VOICE = 2;
	public static final int TPL_VIDEO = 3;
	public static final int TPL_VOTE = 4;
	public static final int TPL_ADDRESS = 5;
	public static final int TPL_SCHEDULE = 6;
	public static final int TPL_LINE = 7;
	public static final int TPL_AVATAR = 8;

	@Bind(R.id.topbar)
	TopBarView topbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.bind(this);
		initView();
		listViewHelper.refresh();
	}

	private void initView() {
		topbar.setTitle("动态预览").setLeftText("关闭", UIHelper.finish(this));
		refreshListView.setPullRefreshEnabled(false);
		listView.setDividerHeight(0);
        listView.setBackgroundResource(R.color.white);
	}

	@Override
	protected IDataSource<Object> getDataSource() {
		ArrayList<Object> models = (ArrayList<Object>) _Bundle.getSerializable(CreateDynamicActivity.BUNDLE_KEY_DYNAMICS);
		return new CreateDynamicPreviewDataSource(this, models);
	}

	@Override
	protected ArrayList<Class> getTemplateClasses() {
		ArrayList<Class> tpls = new ArrayList<Class>();
		tpls.add(TPL_TEXT, LocalDynamicTextTpl.class);
		tpls.add(TPL_IMAGE, LocalDynamicImageTpl.class);
		tpls.add(TPL_VOICE, LocalDynamicVoiceTpl.class);
		tpls.add(TPL_VIDEO, LocalDynamicVideoTpl.class);
		tpls.add(TPL_VOTE, LocalDynamicVoteTpl.class);
		tpls.add(TPL_ADDRESS, LocalDynamicAddressTpl.class);
		tpls.add(TPL_SCHEDULE, LocalDynamicScheduleTpl.class);
		tpls.add(TPL_LINE, LocalDynamicLineTpl.class);
		tpls.add(TPL_AVATAR, LocalDynamicAvatarTpl.class);
		return tpls;
	}
}
