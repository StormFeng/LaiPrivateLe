package com.lailem.app.ui.databank;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.lailem.app.R;
import com.lailem.app.adapter.PagerTabAdapter;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.DataBankTabView;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DataBankActivity extends BaseActivity implements OnPageChangeListener {
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.tabs)
    DataBankTabView tabs;
    @Bind(R.id.pager)
    ViewPager pager;

    private Fragment picFrag;
    private Fragment videoFrag;
    private Fragment voiceFrag;
    private Fragment mapFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_bank);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("资料库").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        String groupId = _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID);
        picFrag = new DataBankPicFragment(groupId);
        videoFrag = new DataBankVideoFragment(groupId);
        voiceFrag = new DataBankVoiceFragment(groupId);
        mapFrag = new DataBankMapFragment(groupId);
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("相片");
        titles.add("视频");
        titles.add("录音");
        titles.add("地点");
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(picFrag);
        fragments.add(videoFrag);
        fragments.add(voiceFrag);
        fragments.add(mapFrag);
        pager.setAdapter(new PagerTabAdapter(fm, titles, fragments));
        pager.setOnPageChangeListener(this);

        tabs.setViewPager(pager);
        tabs.setTitles(titles);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        tabs.onPageScrolled(arg0, arg1, arg2);
    }

    @Override
    public void onPageSelected(int arg0) {
        tabs.setCurrentTab(arg0);
    }
}
