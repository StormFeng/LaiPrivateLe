package com.lailem.app.ui.active;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ActiveMemberListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListActivity;
import com.lailem.app.bean.Base;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean;
import com.lailem.app.tpl.ActiveMemberSectionTpl;
import com.lailem.app.tpl.ActiveMemberTpl;
import com.lailem.app.ui.group.MemberInfoActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActiveMemberListActivity extends BaseSectionListActivity<Object> {

    public static final String BUNDLE_KEY_ROLETYPE = "roletype";
    public static final String BUNDLE_KEY_JOIN_NEED_CONTACT = "join_need_contact";

    public static final int TPL_MEMBER = 1;

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.searchBar)
    TextView search_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
    }

    private void initView() {
        topbar.setTitle("活动成员列表").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_active_member_list;
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new ActiveMemberListDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, ActiveMemberSectionTpl.class);
        tpls.add(TPL_MEMBER, ActiveMemberTpl.class);
        return tpls;
    }

    @OnClick(R.id.searchBar)
    public void clickSearchBar() {
        AppContext.showToast("searchbar");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String removeId = data.getExtras().getString(MemberInfoActivity.BUNDLE_KEY_ID);
            for (int i = 0; i < resultList.size(); i++) {
                Base base = (Base) resultList.get(i);
                if (base.getItemViewType() == TPL_MEMBER) {
                    GroupMemberBean.GroupMember member = (GroupMemberBean.GroupMember) base;
                    if (removeId.equals(member.getMemberId())) {
                        resultList.remove(i);
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
