package com.lailem.app.ui.common;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.personal.ComplainTypeBean;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XuYang on 15/10/19.
 */
public class ComplainActivity extends BaseActivity{
    public static final String API_TAG_COMPLAIN_TYPE="getComplainType";
    public static final String API_TAG_ADD_COMPLAIN="addComplain";

    public static final String BUNDLE_KEY_REPORT_ID ="reason_id";//举报对象id
    public static final String BUNDLE_KEY_REPORT_TYPE ="reason_type";//举报对象类型


    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.reasonRg)
    RadioGroup radioGroup;
    @Bind(R.id.content)
    EditText content_et;
    @Bind(R.id.submit)
    TextView submit_btn;

    private RadioGroup.LayoutParams params;
    private RadioGroup.LayoutParams lineParams;

    private ArrayList<ComplainTypeBean.ComplainType> complainTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void loadData() {
        ApiClient.getApi().getComplainType(this);
    }

    private void initView() {
        topbar.setTitle("举报").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        submit_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                report();
            }
        });

        params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, (int) TDevice.dpToPixel(48));
        lineParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, 1);
        loadViewHelper.init(findViewById(R.id.scrollView), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadData();
            }
        });

    }

    public void report() {
        if(ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            String reasonId = _Bundle.getString(BUNDLE_KEY_REPORT_ID);
            String reasonType = _Bundle.getString(BUNDLE_KEY_REPORT_TYPE);

            RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            String typeId = (String) radioButton.getTag();

            String detail = content_et.getText().toString().trim();
            if (TextUtils.isEmpty(detail)) {
                detail = null;
            }

            ApiClient.getApi().addComplain(this, detail, "2", reasonId, reasonType, typeId, ac.getLoginUid());
        }else{
            UIHelper.showLogin(_activity,false);
        }
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        topbar.showProgressBar();
        if (tag.equals(API_TAG_COMPLAIN_TYPE)) {
            loadViewHelper.showLoading();
        }
        if (tag.equals(API_TAG_ADD_COMPLAIN)) {
            showWaitDialog();
        }
        submit_btn.setEnabled(false);
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        topbar.hideProgressBar();
        submit_btn.setEnabled(true);
        hideWaitDialog();

            if (tag.equals(API_TAG_COMPLAIN_TYPE)) {
                if(res.isOK()){
                    loadViewHelper.restore();
                    this.complainTypeList = ((ComplainTypeBean) res).getCompplainTypeList();
                    if(complainTypeList==null){
                        complainTypeList  = new ArrayList<ComplainTypeBean.ComplainType>();
                    }
                    renderReportReasons();
                }else{
                    loadViewHelper.showFail();
                }

            } else if (tag.equals(API_TAG_ADD_COMPLAIN)) {
                AppContext.showToast("提交成功");
                finish();
            }
    }

    @Override
    public void onApiError(String tag) {
        super.onParseError(tag);
        topbar.hideProgressBar();
        submit_btn.setEnabled(true);
        hideWaitDialog();
        if (tag.equals(API_TAG_COMPLAIN_TYPE)) {
            loadViewHelper.showFail();
        }
    }

    @SuppressLint("NewApi")
    private void renderReportReasons() {
        for (int i = 0; i < complainTypeList.size(); i++) {
            ComplainTypeBean.ComplainType reason = complainTypeList.get(i);
            ImageView line = (ImageView) View.inflate(_activity, R.layout.view_divider_line, null);
            RadioButton rb = (RadioButton) View.inflate(_activity, R.layout.view_report_reason, null);
            rb.setText(reason.getName());
            rb.setId(i);
            // 如果版本高使用系统自带生成id的方法
            if (Build.VERSION.SDK_INT > 16) {
                rb.setId(View.generateViewId());
            }
            rb.setTag(complainTypeList.get(i).getId());

            if (i == 0) {
                rb.setChecked(true);
            }
            reason.setResourceId(rb.getId());

            radioGroup.addView(line,lineParams);
            radioGroup.addView(rb, params);
        }
    }
}
