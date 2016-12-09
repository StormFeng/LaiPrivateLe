package com.lailem.app.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.MeDynamicsDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListActivity;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.loadfactory.MeDynamicLoadViewFactory;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicActionBarTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicAddressTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicAvatarTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicCreateTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicGapTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicImageTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicLineTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicLinkTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicNewAddressTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicNewVoiceTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicNoticeTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicScheduleTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicSectionTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicSingleImageTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicSingleVideoTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicTextTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicVideoImageTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicVideoTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicVoiceTpl;
import com.lailem.app.ui.me.tpl.dynamic.MeDynamicVoteTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MeDynamicActivity extends BaseSectionListActivity<Object> {

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
    public static final int TPL_CREATE = 13;
    public static final int TPL_NEW_ADDRESS = 14;//新版地址类型
    public static final int TPL_NEW_VOICE = 15;//新版语音播放
    public static final int TPL_VIDEOIMAGE = 16;//新版视频图片九宫格类型
    public static final int TPL_GAP = 17;//分割
    public static final int TPL_SINGLE_IMAGE = 18;//单个图片
    public static final int TPL_SINGLE_VIDEO = 19;//单个视频

    @Bind(R.id.topbar)
    TopBarView topbar;

    public MeDynamicActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
    }

    private void initView() {
        topbar.setTitle("我的动态").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        listView.setDividerHeight(0);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new MeDynamicsDataSource(this);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, MeDynamicSectionTpl.class);
        tpls.add(TPL_AVATAR, MeDynamicAvatarTpl.class);
        tpls.add(TPL_TEXT, MeDynamicTextTpl.class);
        tpls.add(TPL_IMAGE, MeDynamicImageTpl.class);
        tpls.add(TPL_VOICE, MeDynamicVoiceTpl.class);
        tpls.add(TPL_VIDEO, MeDynamicVideoTpl.class);
        tpls.add(TPL_ADDRESS, MeDynamicAddressTpl.class);
        tpls.add(TPL_SCHEDULE, MeDynamicScheduleTpl.class);
        tpls.add(TPL_VOTE, MeDynamicVoteTpl.class);
        tpls.add(TPL_LINK, MeDynamicLinkTpl.class);
        tpls.add(TPL_LINE, MeDynamicLineTpl.class);
        tpls.add(TPL_ACTIONBAR, MeDynamicActionBarTpl.class);
        tpls.add(TPL_NOTICE, MeDynamicNoticeTpl.class);
        tpls.add(TPL_CREATE, MeDynamicCreateTpl.class);
        tpls.add(TPL_NEW_ADDRESS, MeDynamicNewAddressTpl.class);
        tpls.add(TPL_NEW_VOICE, MeDynamicNewVoiceTpl.class);
        tpls.add(TPL_VIDEOIMAGE, MeDynamicVideoImageTpl.class);
        tpls.add(TPL_GAP, MeDynamicGapTpl.class);
        tpls.add(TPL_SINGLE_IMAGE, MeDynamicSingleImageTpl.class);
        tpls.add(TPL_SINGLE_VIDEO, MeDynamicSingleVideoTpl.class);
        return tpls;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Base item = (Base) resultList.get((int) id);
        switch (item.getItemViewType()) {
            case TPL_AVATAR:
            case TPL_TEXT:
            case TPL_IMAGE:
            case TPL_VOICE:
            case TPL_VIDEO:
            case TPL_VOTE:
            case TPL_ADDRESS:
            case TPL_SCHEDULE:
            case TPL_LINE:
            case TPL_NOTICE:
            case TPL_ACTIONBAR:
                for (int i = (int) id; i < resultList.size(); i++) {
                    if (id > 0 && ((Base) resultList.get((int) (id - 1))).getItemViewType() == TPL_CREATE) {
                        Object obj = resultList.get((int) (id - 1));
                        if (obj instanceof ObjectWrapper) {
                            DynamicBean bean = (DynamicBean) ((ObjectWrapper) obj).getObject();
                            UIHelper.showActiveDetail(this, bean.getId());
                            break;
                        }
                    } else {
                        Object obj = resultList.get(i);
                        if (obj instanceof DynamicBean) {
                            UIHelper.showDynamicDetail(this, ((DynamicBean) obj).getId(), position);
                            break;
                        }
                    }

                }
                break;
            case TPL_CREATE:
                Object obj = resultList.get(position);
                if (obj instanceof ObjectWrapper) {
                    DynamicBean bean = (DynamicBean) ((ObjectWrapper) obj).getObject();
                    UIHelper.showActiveDetail(this, bean.getId());
                }
                break;
        }
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new MeDynamicLoadViewFactory();
    }
}
