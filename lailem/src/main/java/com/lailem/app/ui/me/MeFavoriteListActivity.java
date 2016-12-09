package com.lailem.app.ui.me;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.MeFavoriteListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListActivity;
import com.lailem.app.loadfactory.MeFavoriteLoadViewFactory;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteActionBarTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteActiveInfoTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteAddressTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteAvatarTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteCreateTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteGapTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteImageTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteLineTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteLinkTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteNewAddressTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteNewVoiceTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteNoticeTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteScheduleTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteSectionTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteSingleImageTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteSingleVideoTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteTextTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteVideoImageTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteVideoTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteVoiceTpl;
import com.lailem.app.ui.me.tpl.favorite.MeFavoriteVoteTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MeFavoriteListActivity extends BaseSectionListActivity<Object> {
    public static final int TPL_AVATAR = 1;
    public static final int TPL_TEXT = 2;
    public static final int TPL_IMAGE = 3;
    public static final int TPL_VOICE = 4;
    public static final int TPL_VIDEO = 5;
    public static final int TPL_ADDRESS = 6;
    public static final int TPL_SCHEDULE = 7;
    public static final int TPL_VOTE = 8;
    public static final int TPL_LINK = 9;
    public static final int TPL_LINE = 10;
    public static final int TPL_ACTIONBAR = 11;
    public static final int TPL_NOTICE = 12;
    public static final int TPL_ACTIVEINFO = 13;
    public static final int TPL_CREATE = 14;
    public static final int TPL_NEW_ADDRESS = 15;//新版地址类型
    public static final int TPL_NEW_VOICE = 16;//新版语音播放
    public static final int TPL_VIDEOIMAGE = 17;//新版视频图片九宫格类型
    public static final int TPL_GAP = 18;//分割
    public static final int TPL_SINGLE_IMAGE = 19;//单个图片
    public static final int TPL_SINGLE_VIDEO = 20;//单个视频
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
        topbar.setTitle("我的收藏").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        listView.setDivider(getResources().getDrawable(android.R.color.transparent));
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new MeFavoriteListDataSource(this);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, MeFavoriteSectionTpl.class);
        tpls.add(TPL_AVATAR, MeFavoriteAvatarTpl.class);
        tpls.add(TPL_TEXT, MeFavoriteTextTpl.class);
        tpls.add(TPL_IMAGE, MeFavoriteImageTpl.class);
        tpls.add(TPL_VOICE, MeFavoriteVoiceTpl.class);
        tpls.add(TPL_VIDEO, MeFavoriteVideoTpl.class);
        tpls.add(TPL_ADDRESS, MeFavoriteAddressTpl.class);
        tpls.add(TPL_SCHEDULE, MeFavoriteScheduleTpl.class);
        tpls.add(TPL_VOTE, MeFavoriteVoteTpl.class);
        tpls.add(TPL_LINK, MeFavoriteLinkTpl.class);
        tpls.add(TPL_LINE, MeFavoriteLineTpl.class);
        tpls.add(TPL_ACTIONBAR, MeFavoriteActionBarTpl.class);
        tpls.add(TPL_NOTICE, MeFavoriteNoticeTpl.class);
        tpls.add(TPL_ACTIVEINFO, MeFavoriteActiveInfoTpl.class);
        tpls.add(TPL_CREATE, MeFavoriteCreateTpl.class);
        tpls.add(TPL_NEW_ADDRESS, MeFavoriteNewAddressTpl.class);
        tpls.add(TPL_NEW_VOICE, MeFavoriteNewVoiceTpl.class);
        tpls.add(TPL_VIDEOIMAGE, MeFavoriteVideoImageTpl.class);
        tpls.add(TPL_GAP, MeFavoriteGapTpl.class);
        tpls.add(TPL_SINGLE_IMAGE, MeFavoriteSingleImageTpl.class);
        tpls.add(TPL_SINGLE_VIDEO, MeFavoriteSingleVideoTpl.class);
        return tpls;
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new MeFavoriteLoadViewFactory();
    }

}
