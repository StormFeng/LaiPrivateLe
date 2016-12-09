package com.lailem.app.ui.group;

import android.os.Bundle;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.jsonbean.activegroup.GroupInfoBean.GroupInfo;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.GroupQrCodeDialog;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群资料
 *
 * @author XuYang
 */
public class GroupInfoActivity extends BaseActivity {

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.groupCode)
    TextView groupCode_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.tags)
    TextView tags_tv;
    @Bind(R.id.permission)
    TextView permission_tv;
    @Bind(R.id.intro)
    TextView intro_tv;

    private GroupInfo groupInfo;
    private GroupQrCodeDialog qrCodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        ButterKnife.bind(this);
        groupInfo = (GroupInfo) ac.getShareObject(Const.SHAREOBJ_KEY_GROUP_INFO);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        topbar.setTitle("群组资料").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        name_tv.setText(groupInfo.getName());
        groupCode_tv.setText(groupInfo.getGroupNum());
        address_tv.setText(groupInfo.getAddress());
        tags_tv.setText(groupInfo.getTagNames());
        if (Const.PERMISSION_PUBLIC.equals(groupInfo.getPermission())) {
            permission_tv.setText("公有群");
        } else {
            permission_tv.setText("私有群");
        }
        intro_tv.setText(groupInfo.getIntro());
    }

    @OnClick(R.id.qrCode_ll)
    public void clickQrCode() {
        if (qrCodeDialog == null) {
            qrCodeDialog = new GroupQrCodeDialog(_activity);
            qrCodeDialog.setGroupInfo(groupInfo.getName(), groupInfo.getGroupNum(), groupInfo.getId(), groupInfo.getAddress());
        }
        qrCodeDialog.show();
    }

    @OnClick(R.id.address_ll)
    public void clickAddress() {
        UIHelper.showActiveLoc(_activity, groupInfo.getLat(), groupInfo.getLon(), groupInfo.getAddress(), ActiveLocActivity.TYPE_GROUP);
    }
}
