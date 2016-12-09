package com.lailem.app.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.datasource.GroupMemberListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListActivity;
import com.lailem.app.bean.Base;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean;
import com.lailem.app.tpl.GroupMemberSectionTpl;
import com.lailem.app.tpl.GroupMemberTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupMemberListActivity extends BaseSectionListActivity<Object> {

    public static final String BUNDLE_KEY_ROLETYPE = "roletype";

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

        topbar.setTitle("群成员列表").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_member_list;
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new GroupMemberListDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, GroupMemberSectionTpl.class);
        tpls.add(TPL_MEMBER, GroupMemberTpl.class);
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
