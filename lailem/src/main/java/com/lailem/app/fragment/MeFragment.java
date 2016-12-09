package com.lailem.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseFragment;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.ui.webview.WebViewActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.DynamicTaskUtil;
import com.lailem.app.utils.FileUtils;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MeFragment extends BaseFragment {
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.viewFlipper)
    ViewFlipper mViewFlipper;
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
    @Bind(R.id.activeId)
    TextView activeId_tv;
    @Bind(R.id.showDraftBox)
    View showDraftBoxView;
    @Bind(R.id.draftBox)
    TextView draftBox_tv;
    @Bind(R.id.contactsCount)
    TextView contactsCount_tv;
    @Bind(R.id.dynamicsCount)
    TextView dynamicsCount_tv;
    @Bind(R.id.favoritesCount)
    TextView favoritesCount_tv;
    private DynamicTaskReceiver dynamicTaskReceiver = new DynamicTaskReceiver() {
        @Override
        public void onReceive(Context content, Intent intent) {
            int dynamicTaskCount = DynamicTaskUtil.getDynamicTaskCount(_activity);
            if (dynamicTaskCount > 0) {
                showDraftBoxView.setVisibility(View.VISIBLE);
                draftBox_tv.setText("动态发送失败(" + dynamicTaskCount + ")");
            } else {
                showDraftBoxView.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.registerDynamicTaskReceiver(_activity, dynamicTaskReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_main_me, null);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.unRegisterDynamicTaskReceiver(_activity, dynamicTaskReceiver);
    }

    private void initView() {
        topbar.setTitle("个人中心");
        name_tv.setMaxWidth((int) (TDevice.getScreenWidth() - TDevice.dpToPixel(180)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ac.isLogin()) {
            mViewFlipper.setDisplayedChild(0);
            if (Func.checkImageTag(ApiClient.getFileUrl(ac.getProperty(Const.USER_SHEAD)), avatar_iv)) {
                Glide.with(_activity).load(ApiClient.getFileUrl(ac.getProperty(Const.USER_SHEAD))).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
            }

            name_tv.setText(ac.getProperty(Const.USER_NICKNAME));

            String sex = ac.getProperty(Const.USER_SEX);
            if (Const.MALE.equals(sex)) {
                sex_iv.setImageResource(R.drawable.ic_male);
                sexAgeArea.setBackgroundResource(R.drawable.bg_male);
            } else if (Const.FEMALE.equals(sex)) {
                sex_iv.setImageResource(R.drawable.ic_female);
                sexAgeArea.setBackgroundResource(R.drawable.bg_female);
            }

            age_tv.setText(StringUtils.getAgeNum(ac.getProperty(Const.USER_BIRTHDAY)));

            String activeId = ac.getProperty(Const.USER_USERNAME);
            if (TextUtils.isEmpty(activeId)) {
                activeId_tv.setVisibility(View.INVISIBLE);
            } else {
                activeId_tv.setText("来了号：" + activeId);
            }

            dynamicsCount_tv.setText(ac.getProperty(Const.USER_DYNAMIC_COUNT));
            favoritesCount_tv.setText(ac.getProperty(Const.USER_COLLECT_COUNT));

            //处理草稿箱及红点显示
            int dynamicTaskCount = DynamicTaskUtil.getDynamicTaskCount(_activity);
            if (dynamicTaskCount > 0) {
                showDraftBoxView.setVisibility(View.VISIBLE);
                draftBox_tv.setText("动态发送失败(" + dynamicTaskCount + ")");
            } else {
                showDraftBoxView.setVisibility(View.GONE);
            }
        } else {
            mViewFlipper.setDisplayedChild(1);
            avatar_iv.setImageResource(R.drawable.default_avatar);
        }
    }


    @OnClick(R.id.brief)
    public void clickBrief() {
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            UIHelper.showUserInfo(_activity);
        } else {
            UIHelper.showLogin(_activity, false);
        }

    }

    @OnClick(R.id.showDraftBox)
    public void clickShowDraftBox() {
        UIHelper.showDraftBoxListActivity(_activity);
    }

    @OnClick(R.id.contacts)
    public void clickContacts() {
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            UIHelper.showContactList(_activity);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @OnClick(R.id.dynamics)
    public void clickDynamics() {
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            UIHelper.showMyDynamics(_activity);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @OnClick(R.id.favorites)
    public void clickFavorites() {
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            UIHelper.showFavoriteList(_activity);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @OnClick(R.id.setting)
    public void clickSetting() {
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            UIHelper.showSetting(_activity);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @OnClick(R.id.notices)
    public void clickNotices() {
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            UIHelper.showMessageList(_activity);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @OnClick(R.id.share)
    public void clickShare() {
        String title = "来了-最专业的活动聚会平台";
        String content = "活动来了，群组来了，最专业的活动聚会平台-来了，点按连接安装app，即可寻找周边活动、快速发起活动，还可快捷创建群组，在群组里玩转活动哦！";
        String dataUrl = Const.URL_APP_DOWNLOAD;
        String imageUrl = FileUtils.getImageDir(_activity) + "/logo.png";
        String smsContent = "活动来了，群组来了，最专业的活动聚会平台-来了。点按连接(" + Const.URL_APP_DOWNLOAD + ")安装app，即可寻找周边活动、快速发起活动，还可快捷创建群组，在群组里玩转活动哦！";
        String emailContent = "活动来了，群组来了，最专业的活动聚会平台-来了。点按连接(" + Const.URL_APP_DOWNLOAD + ")安装app，即可寻找周边活动、快速发起活动，还可快捷创建群组，在群组里玩转活动哦！";
        String emailTopic = "来了-最专业的活动聚会平台";
        String weixinMomentTitle = "来了-最专业的活动聚会平台";
        String weiboContent = "活动来了，群组来了，最专业的活动聚会平台-来了，点按连接安装app，即可寻找周边活动、快速发起活动，还可快捷创建群组，在群组里玩转活动哦！" + Const.URL_APP_DOWNLOAD;
        UIHelper.showShare(_activity, title, content, dataUrl, imageUrl, smsContent, emailContent, emailTopic, weixinMomentTitle, weiboContent);
    }

    @OnClick(R.id.feedback)
    public void clickFeedback() {
        UIHelper.showFeedback(_activity);
    }

    @OnClick(R.id.help)
    public void clickHelp() {
        UIHelper.showWebView(_activity, WebViewActivity.TYPE_HELP);
    }

    @OnClick(R.id.about)
    public void clickAbout() {
        UIHelper.showAbout(_activity);
    }
}
