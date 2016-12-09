package com.lailem.app.ui.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.chat.util.MNotificationManager;
import com.lailem.app.chat.util.MessageCountManager;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.FileUtils;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.togglebutton.ToggleButton;
import com.lailem.app.widget.togglebutton.ToggleButton.OnToggleChanged;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements OnToggleChanged {
    public final static String CLEAR_ALL_CHAT_RECORD = "clear_all_chat_record";
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.logout_ll)
    View logout_ll;
    @Bind(R.id.newNoticeToggle)
    ToggleButton newNoticeToggle;
    @Bind(R.id.displayNoticeDetailToggle)
    ToggleButton displayNoticeDetailToggle;
    @Bind(R.id.chatEnableToggle)
    ToggleButton chatEnableToggle;
    @Bind(R.id.blackListCount)
    TextView blackListCount_tv;

    @Bind(R.id.managePwd)
    TextView managePwd_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("设置").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        newNoticeToggle.setOnToggleChanged(this);
        if (MNotificationManager.getInstance(_activity).getIsRemind()) {
            newNoticeToggle.setToggleOn();
        } else {
            newNoticeToggle.setToggleOff();
        }

        String phone = ac.getProperty(Const.USER_PHONE);
        if (TextUtils.isEmpty(phone)) {
            managePwd_tv.setText("绑定手机号");
        } else {
            managePwd_tv.setText("修改密码");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果未登录
        if (ac.isLogin()) {
            logout_ll.setVisibility(View.VISIBLE);
        } else {
            logout_ll.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.blackList_ll)
    public void clickBlackList() {
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            UIHelper.showBlackList(_activity);
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @OnClick(R.id.managePwd)
    public void clickManagePwd() {
        String phone = ac.getProperty(Const.USER_PHONE);
        if (TextUtils.isEmpty(phone)) {
            if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
                UIHelper.showBindPhone(_activity);
            } else {
                UIHelper.showLogin(_activity, false);
            }
        } else {
            if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
                UIHelper.showModifyPwd(_activity);
            } else {
                UIHelper.showLogin(_activity, false);
            }
        }
    }

    @OnClick(R.id.clearCache)
    public void clearCache() {
        showWaitDialog();
        new Thread(new Runnable() {

            @Override
            public void run() {
                FileUtils.delete(FileUtils.getRootFileOfCache(_activity));
                FileUtils.delete(FileUtils.getRootDirFileOfSDForApp(_activity));
                runOnUiThread(new Runnable() {
                    public void run() {
                        AppContext.showToast("清除缓存成功");
                        hideWaitDialog();
                    }
                });

            }
        }).start();
    }

    @OnClick(R.id.clearChatCache)
    public void clearChatCache() {
        showWaitDialog();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // 删除会话
                DaoOperate.getInstance(_activity).deleteConversations();
                // 删除消息
                DaoOperate.getInstance(_activity).deleteMessagesForConversation();
                BroadcastManager.sendClearMsgRecordBroadcast(_activity, CLEAR_ALL_CHAT_RECORD);
                // 未读消息数量置为0
                MessageCountManager.getInstance().setCount(MessageCountManager.KEY_NO_READ_COUNT_FOR_CHAT, 0);
                runOnUiThread(new Runnable() {
                    public void run() {
                        AppContext.showToast("清除聊天记录成功");
                        hideWaitDialog();
                    }
                });

            }
        }).start();
    }

    @OnClick(R.id.logout)
    public void logout() {
        ac.logout();
        UIHelper.showAppStart(this, true);
        BroadcastManager.sendLogoutBroadcast(_activity);
    }

    @Override
    public void onToggle(ToggleButton toggleButton, boolean on) {
        switch (toggleButton.getId()) {
            case R.id.newNoticeToggle:
                MNotificationManager.getInstance(_activity).setIsRemind(on);
                break;
            case R.id.displayNoticeDetailToggle:

                break;
            case R.id.chatEnableToggle:

                break;
        }
    }

}
