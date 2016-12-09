package com.lailem.app.ui.me;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.DraftBoxListDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.tpl.DraftBoxActionBarTpl;
import com.lailem.app.tpl.DraftBoxDateTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicAddressTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicGapTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicImageTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicLineTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicNewAddressTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicNewVoiceTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicScheduleTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicSingleImageTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicSingleVideoTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicTextTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicVideoImageTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicVideoTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicVoiceTpl;
import com.lailem.app.ui.create_old.preview.tpl.LocalDynamicVoteTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XuYang on 15/11/10.
 */
public class DraftBoxListActivity extends BaseListActivity<Object> {

    public static final int TPL_DATE = 0;
    public static final int TPL_TEXT = 1;
    public static final int TPL_IMAGE = 2;
    public static final int TPL_VOICE = 3;
    public static final int TPL_VIDEO = 4;
    public static final int TPL_VOTE = 5;
    public static final int TPL_ADDRESS = 6;
    public static final int TPL_SCHEDULE = 7;
    public static final int TPL_LINE = 8;
    public static final int TPL_ACTIONBAR = 9;
    public static final int TPL_NEW_ADDRESS = 10;//新版地址类型
    public static final int TPL_NEW_VOICE = 11;//新版语音播放
    public static final int TPL_VIDEOIMAGE = 12;//新版视频图片九宫格类型
    public static final int TPL_GAP = 13;//分割
    public static final int TPL_SINGLE_IMAGE = 14;//单个图片
    public static final int TPL_SINGLE_VIDEO = 15;//单个视频

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
        topbar.setTitle("待处理").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        listView.setDividerHeight(0);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new DraftBoxListDataSource(this);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_DATE, DraftBoxDateTpl.class);
        tpls.add(TPL_TEXT, LocalDynamicTextTpl.class);
        tpls.add(TPL_IMAGE, LocalDynamicImageTpl.class);
        tpls.add(TPL_VOICE, LocalDynamicVoiceTpl.class);
        tpls.add(TPL_VIDEO, LocalDynamicVideoTpl.class);
        tpls.add(TPL_VOTE, LocalDynamicVoteTpl.class);
        tpls.add(TPL_ADDRESS, LocalDynamicAddressTpl.class);
        tpls.add(TPL_SCHEDULE, LocalDynamicScheduleTpl.class);
        tpls.add(TPL_LINE, LocalDynamicLineTpl.class);
        tpls.add(TPL_ACTIONBAR, DraftBoxActionBarTpl.class);
        tpls.add(TPL_NEW_ADDRESS, LocalDynamicNewAddressTpl.class);
        tpls.add(TPL_NEW_VOICE, LocalDynamicNewVoiceTpl.class);
        tpls.add(TPL_VIDEOIMAGE, LocalDynamicVideoImageTpl.class);
        tpls.add(TPL_GAP, LocalDynamicGapTpl.class);
        tpls.add(TPL_SINGLE_IMAGE, LocalDynamicSingleImageTpl.class);
        tpls.add(TPL_SINGLE_VIDEO, LocalDynamicSingleVideoTpl.class);
        return tpls;
    }
}
