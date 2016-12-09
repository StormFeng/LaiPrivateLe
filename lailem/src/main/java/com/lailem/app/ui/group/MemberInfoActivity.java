package com.lailem.app.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.adapter.datasource.GroupMemberListDataSource;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.cache.BlackListIdsCache;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.BlacklistIds;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.jsonbean.activegroup.MemberInfoBean;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ConfirmDialog;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.togglebutton.ToggleButton;
import com.lailem.app.widget.togglebutton.ToggleButton.OnToggleChanged;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MemberInfoActivity extends BaseActivity implements OnToggleChanged {
    public static final String API_TAG_MEMBERINFO = "memberInfo";
    public static final String API_TAG_GETOUT = "getout";

    public static final String BUNDLE_KEY_ID = "id";
    public static final String BUNDLE_KEY_ROLE_TYPE = "role_type";
    public static final String BUNDLE_KEY_MY_ROLE_TYPE = "my_role_type";
    public static final int REQUEST_CODE = 3000;

    @Bind(R.id.topbar)
    TopBarView topbar;

    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.sexAgeArea)
    View sexAgeArea;
    @Bind(R.id.sex)
    ImageView sex_iv;
    @Bind(R.id.age)
    TextView age_tv;
    @Bind(R.id.sign)
    TextView sign_tv;
    @Bind(R.id.remarkArea)
    View remarkArea;
    @Bind(R.id.remark)
    TextView remark_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.isBlackArea)
    View isBlackArea;
    @Bind(R.id.isBlack)
    ToggleButton isBlackToggle;
    @Bind(R.id.report_ll)
    View report_ll;

    @Bind(R.id.chat)
    TextView chat_tv;
    @Bind(R.id.delete)
    TextView delete_tv;

    private MemberInfoBean memberInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
        ButterKnife.bind(this);
        initView();
        initLoginCallback(new Runnable() {
            @Override
            public void run() {
                loadMemberInfo();
            }
        });
        loadMemberInfo();
    }

    public void loadMemberInfo() {
        if (ac.isLogin()) {
            ApiClient.getApi().memberInfo(this, _Bundle.getString(BUNDLE_KEY_ID), ac.getLoginUid());
        } else {
            ApiClient.getApi().memberInfo(this, _Bundle.getString(BUNDLE_KEY_ID), null);
        }
    }

    private void initView() {
        topbar.setTitle(_Bundle.getString(Const.BUNDLE_KEY_GROUP_NAME)).setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).showTag_iv();
        isBlackToggle.setOnToggleChanged(this);

        loadViewHelper.init(findViewById(R.id.scrollView), new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadMemberInfo();
            }
        });
    }

    private void render() {
        String roleType = _Bundle.getString(BUNDLE_KEY_ROLE_TYPE);
        // 处理标题上的用户类型
        if (!TextUtils.isEmpty(roleType)) {
            if (roleType.equals(GroupMemberListDataSource.CREATOR)) {
                topbar.getTag_tv().setText("群主");
            } else if (roleType.equals(GroupMemberListDataSource.MANAGER)) {
                topbar.getTag_tv().setText("管理员");
            } else if (roleType.equals(GroupMemberListDataSource.NORMAL)) {
                topbar.getTag_tv().setText("成员");
            }
            if (_Bundle.getString(BUNDLE_KEY_MY_ROLE_TYPE).equals(Const.ROLE_TYPE_CREATOR)
                    && (roleType.equals(GroupMemberListDataSource.MANAGER) || roleType.equals(GroupMemberListDataSource.NORMAL))) {
                delete_tv.setVisibility(View.VISIBLE);
            } else {
                delete_tv.setVisibility(View.GONE);
            }
        } else {
            delete_tv.setVisibility(View.GONE);
            topbar.getTag_tv().setVisibility(View.GONE);
        }

        // 如果是自己的主页
        if (memberInfo.getUserInfo().getId().equals(ac.getProperty(Const.USER_ID))) {
            remarkArea.setVisibility(View.GONE);
            delete_tv.setVisibility(View.GONE);
            isBlackArea.setVisibility(View.GONE);
        }

        if (Func.checkImageTag(memberInfo.getUserInfo().getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(memberInfo.getUserInfo().getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(memberInfo.getUserInfo().getNickname());
        String sex = memberInfo.getUserInfo().getSex();
        if (Const.MALE.equals(sex)) {
            sex_iv.setImageResource(R.drawable.ic_male);
            sexAgeArea.setBackgroundResource(R.drawable.bg_male);
        } else if (Const.FEMALE.equals(sex)) {
            sex_iv.setImageResource(R.drawable.ic_female);
            sexAgeArea.setBackgroundResource(R.drawable.bg_female);
        }
        age_tv.setText(memberInfo.getUserInfo().getAge());
        if (TextUtils.isEmpty(memberInfo.getUserInfo().getPersonalizedSignature())) {
            sign_tv.setText(Const.EMPTY_PERSONAL_SIGN);
        } else {
            sign_tv.setText(memberInfo.getUserInfo().getPersonalizedSignature());
        }
        remark_tv.setText(Func.formatNickName(_activity, memberInfo.getUserInfo().getId(), memberInfo.getUserInfo().getNickname()));
        address_tv.setText(memberInfo.getUserInfo().getProvince() + " " + memberInfo.getUserInfo().getCity());
        String isBlack = memberInfo.getUserInfo().getIsBlacklist();
        if ("1".equals(isBlack)) {
            isBlackToggle.setToggleOn();
        } else if ("2".equals(isBlack)) {
            isBlackToggle.setToggleOff();
        }
    }

    @OnClick(R.id.avatar)
    public void clickAvatar() {
        ArrayList<ImageViewerAdapter.ImageBean> beans = new ArrayList<ImageViewerAdapter.ImageBean>();
        ImageViewerAdapter.ImageBean bean = new ImageViewerAdapter.ImageBean(ApiClient.getFileUrl(memberInfo.getUserInfo().getHeadBPicName()), ApiClient.getFileUrl(memberInfo.getUserInfo().getHeadSPicName()));
        beans.add(bean);
        UIHelper.showImages(this, beans, 0);
    }

    @OnClick(R.id.remark_ll)
    public void clickRemark() {
        if (ac.isLogin(this, "loadMemberInfo")) {
            UIHelper.showModifyMemberRemark(this, _Bundle.getString(BUNDLE_KEY_ID), remark_tv.getText().toString().trim());
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }


    @OnClick(R.id.report_ll)
    public void clickReport() {
        UIHelper.showComplain(this, memberInfo.getUserInfo().getId(), Const.COMPLAIN_TYPE_MEMBER);
    }

    @Override
    public void onToggle(ToggleButton toggleButton, boolean on) {
        if (on) {
            addBlack();
        } else {
            removeBlack();
        }
    }

    /**
     * 加入黑名单
     */
    public void addBlack() {
        if (ac.isLogin(this, "loadMemberInfo")) {
            ApiClient.getApi().addBlack(new ApiCallbackAdapter() {
                @Override
                public void onApiSuccess(Result res, String tag) {
                    super.onApiSuccess(res, tag);
                    if (res.isOK()) {
                        BlacklistIds blacklistIds = new BlacklistIds();
                        blacklistIds.setUserId(ac.getLoginUid());
                        blacklistIds.setCreateTime(System.currentTimeMillis());
                        blacklistIds.setBlackUserId(memberInfo.getUserInfo().getId());
                        BlackListIdsCache.getInstance(_activity).put(blacklistIds);
                    } else {
                        ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
                    }
                }
            }, ac.getLoginUid(), memberInfo.getUserInfo().getId());
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    /**
     * 移除黑名单
     */
    public void removeBlack() {
        if (ac.isLogin(this, "loadMemberInfo")) {
            ApiClient.getApi().removeBlack(new ApiCallbackAdapter() {
                @Override
                public void onApiSuccess(Result res, String tag) {
                    super.onApiSuccess(res, tag);
                    if (res.isOK()) {
                        String blackUserId = memberInfo.getUserInfo().getId();
                        BlackListIdsCache.getInstance(_activity).removeByKey(blackUserId);
                        DaoOperate.getInstance(_activity).deleteBlacklistIds(blackUserId);
                    } else {
                        ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
                    }
                }
            }, ac.getLoginUid(), memberInfo.getUserInfo().getId());
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @OnClick(R.id.chat)
    public void clickChat() {
        if (ac.isLogin(this, "loadMemberInfo")) {
            UIHelper.showChat(_activity, memberInfo.getUserInfo().getId(), memberInfo.getUserInfo().getNickname(), memberInfo.getUserInfo().getHeadSPicName(), Constant.cType_sc);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @OnClick(R.id.delete)
    public void clickDelete() {
        ConfirmDialog dialog = new ConfirmDialog(_activity, R.style.confirm_dialog).config("提示", "确定移除该成员吗", "确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                ApiClient.getApi().getout(_activity, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), memberInfo.getUserInfo().getId());
            }
        });
        dialog.show();
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        if (API_TAG_MEMBERINFO.equals(tag)) {
            loadViewHelper.showLoading();
        } else if (API_TAG_GETOUT.equals(tag)) {
            showWaitDialog();
        }
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        if (res.isOK()) {
            if (API_TAG_MEMBERINFO.equals(tag)) {
                memberInfo = (MemberInfoBean) res;
                render();
                loadViewHelper.restore();
            } else if (API_TAG_GETOUT.equals(tag)) {
                hideWaitDialog();
                setResult(RESULT_OK, _Bundle);
                finish();
            }
        } else {
            hideWaitDialog();
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
        if (API_TAG_MEMBERINFO.equals(tag)) {
            loadViewHelper.restore();
        } else if (API_TAG_GETOUT.equals(tag)) {
            hideWaitDialog();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ModifyMemberRemarkActivity.REQUEST_MODIFY_NAME:
                    String name = data.getExtras().getString(ModifyMemberRemarkActivity.BUNDLE_KEY_NAME);
                    remark_tv.setText(name);
                    break;
            }
        }
    }
}
