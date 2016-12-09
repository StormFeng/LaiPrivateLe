package com.lailem.app.ui.create_old;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.ui.create_old.frag.CreateTypeActiveFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateActiveTypeActivity extends BaseActivity {

    CreateTypeActiveFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_active_type);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        fragment = new CreateTypeActiveFragment(true);
        fm.beginTransaction().replace(R.id.contentArea, fragment).commit();
    }

    @OnClick(R.id.close)
    public void close() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_close);
    }

}
