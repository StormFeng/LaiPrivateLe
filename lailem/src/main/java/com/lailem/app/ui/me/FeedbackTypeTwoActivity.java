package com.lailem.app.ui.me;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.personal.FeedbackTypeBean;
import com.lailem.app.jsonbean.personal.FeedbackTypeBean.FeedbackType;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedbackTypeTwoActivity extends BaseActivity implements OnCheckedChangeListener {
    public static final String BUNDLE_KEY_ID = "id";
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.reasonRg)
    RadioGroup radioGroup;

    private RadioGroup.LayoutParams params;

    private ArrayList<FeedbackType> types = new ArrayList<FeedbackType>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_type2);
        ButterKnife.bind(this);
        initView();
        ApiClient.getApi().feedbackTypeSpecial(this, _Bundle.getString(BUNDLE_KEY_ID));
    }

    private void initView() {
        topbar.setTitle("闪退、卡顿").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, (int) TDevice.dpToPixel(48));
    }

    private void renderReasons() {
        for (int i = 0; i < types.size(); i++) {
            FeedbackType type = types.get(i);
            ImageView line = (ImageView) View.inflate(_activity, R.layout.view_divider_line, null);
            RadioButton rb = (RadioButton) View.inflate(_activity, R.layout.view_report_reason, null);
            rb.setText(type.getItem());
            rb.setId(i);
            // 如果版本高使用系统自带生成id的方法
            if (VERSION.SDK_INT > 16) {
                rb.setId(View.generateViewId());
            }
            rb.setTag("" + i);
            radioGroup.addView(line);
            radioGroup.addView(rb, params);
        }
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String tag = (String) findViewById(checkedId).getTag();
        int checkedIndex = Integer.parseInt(tag);
        Bundle bundle = new Bundle();
        bundle.putString(FeedbackActivity.BUNDLE_KEY_TEXT, types.get(checkedIndex).getItem());
        bundle.putString(FeedbackActivity.BUNDLE_KEY_VALUE, types.get(checkedIndex).getId());
        setResult(Activity.RESULT_OK, bundle);
        finish();
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        if (res.isOK()) {
            FeedbackTypeBean bean = (FeedbackTypeBean) res;
            types.addAll(bean.getFeedbackTypeList());
            renderReasons();
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }

}
