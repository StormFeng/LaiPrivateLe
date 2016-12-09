package com.lailem.app.ui.group;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

public class ModifyGroupNameActivity extends BaseActivity implements TextWatcher {
    public static final String BUNDLE_KEY_NAME = "name";
    public static final int REQUEST_MODIFY_NAME = 4000;
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.right_tv)
    TextView submit_tv;
    @Bind(R.id.input)
    EditText input_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group_name);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("群名称").setLeftText("取消", UIHelper.finish(this)).setRightText("保存");
        if (_Bundle != null) {
            input_et.setText(_Bundle.getString(BUNDLE_KEY_NAME));
            input_et.setSelection(input_et.length());
        }
        submit_tv.setEnabled(input_et.length() > 0);
        input_et.addTextChangedListener(this);
    }

    @OnClick(R.id.right_tv)
    public void submit() {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_NAME, input_et.getText().toString().trim());
        setResult(RESULT_OK, bundle);
        finish();
    }

    @OnClick(R.id.clear)
    public void clear() {
        input_et.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        submit_tv.setEnabled(input_et.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
