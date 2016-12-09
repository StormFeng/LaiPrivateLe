package com.lailem.app.ui.webview;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {
    public static final String TYPE_ABOUT_LAILE = "type_about_laile";
    public static final String TYPE_HELP = "type_help";
    public static final String TYPE_PRIVACY_POLICY = "type_privacy_policy";
    public static final String TYPE_GROUP_LAILE = "type_group_laile";
    public static final String TYPE_ACTI_LAILE = "type_acti_laile";
    public static final String TYPE_INVITE_LEETER = "type_invite_letter";
    public static final String TYPE = "type";
    public static final String BUNDLE_KEY_URL = "url";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    String title;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        topbar.setTitle(title).setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        webview.loadUrl(url);
    }

    private void initData() {
        String type = getIntent().getStringExtra(TYPE);
        if (TYPE_ABOUT_LAILE.equals(type)) {
            title = "关于来了";
            url = Const.URL_APP_ABOUT;
        } else if (TYPE_HELP.equals(type)) {
            title = "使用帮助";
            url = Const.URL_APP_HELP;
        } else if (TYPE_PRIVACY_POLICY.equals(type)) {
            title = "服务声明";
            url = Const.URL_APP_SERVICE_DECLARE;
        } else if (TYPE_GROUP_LAILE.equals(type)) {
            title = "群组来了";
            url = Const.URL_GROUP_LAILE;
        } else if (TYPE_ACTI_LAILE.equals(type)) {
            title = "活动来了";
            url = Const.URL_ACTIVE_LAILE;
        } else if (TYPE_INVITE_LEETER.equals(type)) {
            title = "邀请函预览";
            url = _Bundle.getString(BUNDLE_KEY_URL);
        }
    }

}
