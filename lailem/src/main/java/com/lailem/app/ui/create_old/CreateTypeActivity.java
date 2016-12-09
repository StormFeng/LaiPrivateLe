package com.lailem.app.ui.create_old;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.lailem.app.R;
import com.lailem.app.adapter.PagerTabAdapter;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.ui.create_old.frag.CreateTypeActiveFragment;
import com.lailem.app.ui.create_old.frag.CreateTypeGroupFragment;
import com.lailem.app.utils.TDevice;
import com.lailem.app.widget.DotIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTypeActivity extends BaseActivity implements OnPageChangeListener {
    public static final String BUNDLE_INDEX = "index";
    public static final String BUNDLE_KEY_GROUP_TYPE = "groupType";// 类型：1（活动）、2（普通群）、3（群联）
    public static final String BUNDLE_KEY_TYPE_ID = "groupTypeId";// group分类类型id
    public static final String BUNDLE_KEY_TYPE_NAME = "groupTypeName";// group分类类型名称

    public static final String GROUP_TYPE_ACTIVE = "1";
    public static final String GROUP_TYPE_GROUP = "2";
    public static final String GROUP_TYPE_GROUPS = "3";

    public static final int REQUEST_NEXT = 2000;
    public static final int REQUEST_CREATE_TYPE = 3000;

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.indicator)
    DotIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_type);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("创建活动");
        titles.add("创建群组");
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new CreateTypeActiveFragment());
        fragments.add(new CreateTypeGroupFragment());
        pager.setAdapter(new PagerTabAdapter(fm, titles, fragments));
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(_Bundle.getInt(BUNDLE_INDEX, 0));
        indicator.configIndicator(R.drawable.ic_indicator_create_type_c, R.drawable.ic_indicator_create_type_n);
        indicator.setDivideWidth((int) TDevice.dpToPixel(4f));
        indicator.initIndicator(this, titles.size(), pager.getCurrentItem());
    }

    @OnClick(R.id.close)
    public void close() {
        finish();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        indicator.changeIndiccator(arg0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_NEXT:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_close);
    }

}
