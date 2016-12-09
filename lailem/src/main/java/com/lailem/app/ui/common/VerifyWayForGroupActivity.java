package com.lailem.app.ui.common;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XuYang on 16/1/13.
 */
public class VerifyWayForGroupActivity extends BaseActivity {
    public static final String BUNDLE_KEY_VERIFYWAY = "verify_way";
    public static final int REQUEST_CODE = 6000;
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.verifyWayRg)
    RadioGroup verifyWayRg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyway_for_group);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("验证方式").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        String verifyWay = _Bundle.getString(BUNDLE_KEY_VERIFYWAY);
        if (Const.VERIFY_WAY_TEXT.equals(verifyWay)) {
            verifyWayRg.check(R.id.text);
        } else if (Const.VERIFY_WAY_VOICE.equals(verifyWay)) {
            verifyWayRg.check(R.id.voice);
        } else if (Const.VERIFY_WAY_NONE.equals(verifyWay)) {
            verifyWayRg.check(R.id.none);
        }

        verifyWayRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Bundle bundle = new Bundle();
                switch (checkedId) {
                    case R.id.text:
                        bundle.putString(BUNDLE_KEY_VERIFYWAY, Const.VERIFY_WAY_TEXT);
                        setResult(RESULT_OK, bundle);
                        break;
                    case R.id.voice:
                        bundle.putString(BUNDLE_KEY_VERIFYWAY, Const.VERIFY_WAY_VOICE);
                        setResult(RESULT_OK, bundle);
                        break;
                    case R.id.none:
                        bundle.putString(BUNDLE_KEY_VERIFYWAY, Const.VERIFY_WAY_NONE);
                        setResult(RESULT_OK, bundle);
                        break;
                }
                finish();
            }
        });
    }
}
