package com.lailem.app.ui.main;

//import io.vov.vitamio.LibsChecker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.AppManager;
import com.lailem.app.AppStart;
import com.lailem.app.R;
import com.lailem.app.adapter.PagerTabAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.GroupBroadcastReceiver;
import com.lailem.app.fragment.ActiveFragment;
import com.lailem.app.fragment.ConversationFragment;
import com.lailem.app.fragment.MainFragment;
import com.lailem.app.fragment.MeFragment;
import com.lailem.app.jsonbean.dynamic.VersionCheckBean;
import com.lailem.app.service.LLService;
import com.lailem.app.utils.AcceptInviteManager;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ConfirmDialog;
import com.lailem.app.widget.MainFooterView;
import com.lailem.app.widget.MainFooterView.onTabChangeListener;
import com.lailem.app.widget.XViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements onTabChangeListener {
    public static final String BUNDLE_KEY_FROM_APPSTART = "from_appstart";
    @Bind(R.id.pager)
    XViewPager pager;
    @Bind(R.id.footer)
    MainFooterView footer;

    private long exitTime;

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> titles = new ArrayList<String>();
    private GroupBroadcastReceiver groupBroadcastReceiver = new GroupBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GroupBroadcastReceiver.ACTION_JOIN_ACTIVITY.equals(action)
                    || GroupBroadcastReceiver.ACTION_JOIN_GROUP.equals(action)) {
                footer.setCurIndex(1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPager();
        initFooter();
        if (!getIntent().getBooleanExtra(BUNDLE_KEY_FROM_APPSTART, false)) {
            AcceptInviteManager.getInstance().init(_activity, getIntent());
        }
        checkAppVersion();
        //初始化即时聊天
        LLService.start(_activity, LLService.FLAG_LOGIN_IM);
        BroadcastManager.registerGroupBroadcastReceiver(this, groupBroadcastReceiver);

        //初始化视频
        if (!ac.isInitVideoReady) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ac.initVideo();
                }
            });
        }

        if (AppManager.getActivity(AppStart.class) != null) {
            AppManager.getActivity(AppStart.class).finish();
        }

    }


    private void checkAppVersion() {
        ApiClient.getApi().versionCheck(this, TDevice.getVersionCode() + "");
    }

    private void initPager() {
        for (int i = 0; i < 4; i++) {
            titles.add(i + "");
        }
        Fragment mainFragment = new MainFragment();
        Fragment activeFragment = new ActiveFragment();
        Fragment messageFragment = new ConversationFragment();
        Fragment meFragment = new MeFragment();
        fragments.add(mainFragment);
        fragments.add(activeFragment);
        fragments.add(messageFragment);
        fragments.add(meFragment);
        pager.setOffscreenPageLimit(titles.size() - 1);
        pager.setPagingEnabled(false);
        pager.setAdapter(new PagerTabAdapter(fm, titles, fragments));
    }

    private void initFooter() {
        footer.setOnTabChangeListener(this);
    }

    @Override
    public void onTabChange(int index) {
        if (index != pager.getCurrentItem()) {
            pager.setCurrentItem(index, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        footer.destory();
        BroadcastManager.unRegisterGroupBroadcastReceiver(this, groupBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (AppManager.getActivity(AppStart.class) != null) {
            AppManager.getActivity(AppStart.class).finish();
        }
        AcceptInviteManager.getInstance().init(_activity, intent);
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        if (res.isOK()) {
            VersionCheckBean bean = (VersionCheckBean) res;
            try {
                if (!TextUtils.isEmpty(bean.getvCode()) && Integer.parseInt(bean.getvCode()) > TDevice.getVersionCode()) {
                    showUpdateDialog(bean);
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
                AppContext.showToast("App版本号转换错误");
            }
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    private void showUpdateDialog(final VersionCheckBean bean) {
        ConfirmDialog dialog = new ConfirmDialog(_activity, R.style.confirm_dialog).config("更新提示", "发现新版本", "更新", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UIHelper.startUpdateSevice(_activity, ApiClient.getFileUrl(bean.getApkFileName()));
            }
        });
        if ("1".equals(bean.getIsForce())) {
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppContext.showToast("很抱歉，软件升级后才可以继续使用");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            _activity.finish();
                        }
                    }, 1000);
                }
            });
        }
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                AppContext.showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                AppManager.getAppManager().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
