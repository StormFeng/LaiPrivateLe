package com.lailem.app.ui.me;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.AppManager;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.dynamic.VersionCheckBean;
import com.lailem.app.ui.webview.WebViewActivity;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ConfirmDialog;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.checkUpdate)
    TextView checkUpdate_tv;
    @Bind(R.id.versionName)
    TextView versionName_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();
        checkAppVersion();
    }

    private void checkAppVersion() {
        ApiClient.getApi().versionCheck(this, TDevice.getVersionCode() + "");
    }

    private void initView() {
        topbar.setTitle("关于我们").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        versionName_tv.setText(getString(R.string.app_name) + " V" + TDevice.getVersionName());
    }

    @OnClick(R.id.addComment_ll)
    public void clickAddCommont() {
        TDevice.gotoMarket(this, getPackageName());
    }

    @OnClick(R.id.protocol_ll)
    public void clickProtocol() {
        UIHelper.showWebView(_activity, WebViewActivity.TYPE_PRIVACY_POLICY);
    }

    @OnClick(R.id.about_ll)
    public void clickAbout() {
        UIHelper.showWebView(_activity, WebViewActivity.TYPE_ABOUT_LAILE);
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        if (res.isOK()) {
            VersionCheckBean bean = (VersionCheckBean) res;
            try {
                if (!TextUtils.isEmpty(bean.getvCode()) && Integer.parseInt(bean.getvCode()) > TDevice.getVersionCode()) {
                    checkUpdate_tv.setText("发现新版本");
                    showUpdateDialog(bean);
                } else {
                    checkUpdate_tv.setText("已是最新版本");
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
        ConfirmDialog dialog = new ConfirmDialog(_activity, R.style.confirm_dialog).config("更新提示", "发现新版本", "更新", new OnClickListener() {

            @Override
            public void onClick(View v) {
                UIHelper.startUpdateSevice(_activity, ApiClient.getFileUrl(bean.getApkFileName()));
            }
        });
        if ("1".equals(bean.getIsForce())) {
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    AppContext.showToast("很抱歉，软件升级后才可以继续使用");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            _activity.finish();
                            AppManager.getAppManager().AppExit(_activity);
                        }
                    }, 1000);
                }
            });
        }
        dialog.show();
    }
}
