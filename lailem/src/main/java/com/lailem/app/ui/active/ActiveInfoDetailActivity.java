package com.lailem.app.ui.active;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ActiveInfoDetailDataSource;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.bean.Base;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean.ActiveInfo;
import com.lailem.app.tpl.ActiveInfoDetailCommentTpl;
import com.lailem.app.tpl.ActiveInfoDetailTpl;
import com.lailem.app.tpl.ActiveInfoDetailZanListTpl;
import com.lailem.app.tpl.CommentEmptyTpl;
import com.lailem.app.ui.active.tpl.ActiveInfoDetailCommentHeaderTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.ZanListDialog;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActiveInfoDetailActivity extends BaseListActivity<Object> {

    public static final int TPL_DETAIL = 0;
    public static final int TPL_ZANLIST = 1;
    public static final int TPL_COMMENT = 2;
    public static final int TPL_COMMENT_EMPTY = 3;
    public static final int TPL_COMMENT_HEADER = 4;

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
        topbar.setTitle("活动介绍").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).setRightImageButton(R.drawable.ic_topbar_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveInfoDetailDataSource dataSource = (ActiveInfoDetailDataSource) listViewHelper.getDataSource();
                ActiveInfo activeInfo = dataSource.getActiveInfo();

                String title = activeInfo.getName();
                String content = activeInfo.getName() + "，时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress();
                String dataUrl = Const.ACTIVE_PATTERN + activeInfo.getId();
                String imageUrl = ApiClient.getFileUrl(activeInfo.getbPicName());
                String smsContent = "我在【来了】上创建了活动：" + activeInfo.getName() + ",时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + "，活动详情：" + Const.ACTIVE_PATTERN + activeInfo.getId() + "，快给我点个赞吧！有兴趣也可以报名参加哦！";
                String emailContent = "我在【来了】上创建了活动：" + activeInfo.getName() + ",时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + "，活动详情：" + Const.ACTIVE_PATTERN + activeInfo.getId() + "，快给我点个赞吧！有兴趣也可以报名参加哦！";
                String emailTopic = activeInfo.getName();
                String weixinMomentTitle = activeInfo.getName();
                String weiboContent = activeInfo.getName() + "，时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + Const.ACTIVE_PATTERN + activeInfo.getId();
                UIHelper.showShare(_activity, title, content, dataUrl, imageUrl, smsContent, emailContent, emailTopic, weixinMomentTitle, weiboContent);
            }
        }).hideRight_ib();
        listView.setDividerHeight(0);
        listView.setVerticalScrollBarEnabled(false);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new ActiveInfoDetailDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_DETAIL, ActiveInfoDetailTpl.class);
        tpls.add(TPL_ZANLIST, ActiveInfoDetailZanListTpl.class);
        tpls.add(TPL_COMMENT, ActiveInfoDetailCommentTpl.class);
        tpls.add(TPL_COMMENT_EMPTY, CommentEmptyTpl.class);
        tpls.add(TPL_COMMENT_HEADER, ActiveInfoDetailCommentHeaderTpl.class);
        return tpls;
    }

    @Override
    public void onEndRefresh(IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
        super.onEndRefresh(adapter, result);
        if (resultList.size() == 0) {
            return;
        }
        topbar.showRight_ib();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Base base = (Base) resultList.get(position);
        if (base.getItemViewType() == TPL_ZANLIST) {
            ZanListDialog dialog = new ZanListDialog(this);
            ActiveInfoDetailDataSource ds = (ActiveInfoDetailDataSource) dataSource;
            ActiveInfo activeInfo = ds.getActiveInfo();
            dialog.setParams(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), "1", activeInfo.getLikeCount());
            dialog.show();
        }
    }

}
