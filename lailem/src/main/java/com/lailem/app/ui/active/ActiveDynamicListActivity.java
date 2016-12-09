package com.lailem.app.ui.active;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ActiveDynamicListDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.bean.Base;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.loadfactory.ActiveDynamicLoadViewFactory;
import com.lailem.app.ui.active.tpl.ActiveDetailActionBarTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailAddressTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailAvatarTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailGapTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailImageTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailLineTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailNewAddressTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailNewVoiceTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailNoticeTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailScheduleTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailSingleImageTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailSingleVideoTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailTextTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailVideoImageTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailVideoTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailVoiceTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailVoteTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XuYang on 15/11/10.
 */
public class ActiveDynamicListActivity extends BaseListActivity<Object> {
    public static final int TPL_AVATAR = 0;
    public static final int TPL_TEXT = 1;
    public static final int TPL_IMAGE = 2;
    public static final int TPL_ACTIONBAR = 3;
    public static final int TPL_VOICE = 4;
    public static final int TPL_VIDEO = 5;
    public static final int TPL_VOTE = 6;
    public static final int TPL_ADDRESS = 7;
    public static final int TPL_SCHEDULE = 8;
    public static final int TPL_LINE = 9;
    public static final int TPL_NOTICE = 10;
    public static final int TPL_NEW_ADDRESS = 11;//新版地址类型
    public static final int TPL_NEW_VOICE = 12;//新版语音播放
    public static final int TPL_VIDEOIMAGE = 13;//新版视频图片九宫格类型
    public static final int TPL_GAP = 14;//分割
    public static final int TPL_SINGLE_IMAGE = 15;//单个图片
    public static final int TPL_SINGLE_VIDEO = 16;//单个视频


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
        topbar.setTitle("活动动态").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        listView.setDividerHeight(0);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new ActiveDynamicListDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_AVATAR, ActiveDetailAvatarTpl.class);
        tpls.add(TPL_TEXT, ActiveDetailTextTpl.class);
        tpls.add(TPL_IMAGE, ActiveDetailImageTpl.class);
        tpls.add(TPL_ACTIONBAR, ActiveDetailActionBarTpl.class);
        tpls.add(TPL_VOICE, ActiveDetailVoiceTpl.class);
        tpls.add(TPL_VIDEO, ActiveDetailVideoTpl.class);
        tpls.add(TPL_VOTE, ActiveDetailVoteTpl.class);
        tpls.add(TPL_ADDRESS, ActiveDetailAddressTpl.class);
        tpls.add(TPL_SCHEDULE, ActiveDetailScheduleTpl.class);
        tpls.add(TPL_LINE, ActiveDetailLineTpl.class);
        tpls.add(TPL_NOTICE, ActiveDetailNoticeTpl.class);
        tpls.add(TPL_NEW_ADDRESS, ActiveDetailNewAddressTpl.class);
        tpls.add(TPL_NEW_VOICE, ActiveDetailNewVoiceTpl.class);
        tpls.add(TPL_VIDEOIMAGE, ActiveDetailVideoImageTpl.class);
        tpls.add(TPL_GAP, ActiveDetailGapTpl.class);
        tpls.add(TPL_SINGLE_IMAGE, ActiveDetailSingleImageTpl.class);
        tpls.add(TPL_SINGLE_VIDEO, ActiveDetailSingleVideoTpl.class);
        return tpls;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        if (id == -1) {
            return;
        }
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
                    Object obj = resultList.get(i);
                    if (obj instanceof DynamicBean) {
                        UIHelper.showDynamicDetail(this, ((DynamicBean) obj).getId(), position);
                        break;
                    }
                }
                break;
        }
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new ActiveDynamicLoadViewFactory();
    }
}
