package com.lailem.app.zxing;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CaptureRusultActivity extends BaseActivity {
    public static final String BUNDLE_KEY_CONTENT = "content";
	@Bind(R.id.topbar)
	TopBarView topbar;
	@Bind(R.id.webview)
	WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_result);
		ButterKnife.bind(this);
		initView();
	}

	private void initView() {
		topbar.setTitle("扫描结果").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
		String content = getIntent().getStringExtra(BUNDLE_KEY_CONTENT);
		if (TextUtils.isEmpty(content)) {
			finish();
		} else {
			webview.getSettings().setJavaScriptEnabled(true);
			if (content.startsWith("http://") || content.startsWith("https://")) {
				webview.loadUrl(content);
			} else {
				webview.getSettings().setDefaultTextEncodingName("UTF-8");
				webview.loadData(content, "text/html", "UTF-8");
			}
		}
	}
}
