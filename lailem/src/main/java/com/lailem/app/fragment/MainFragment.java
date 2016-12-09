package com.lailem.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lailem.app.R;
import com.lailem.app.adapter.PagerTabAdapter;
import com.lailem.app.base.BaseFragment;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.MainPagerTabView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends BaseFragment implements OnPageChangeListener {
    @Bind(R.id.tabs)
    MainPagerTabView tabs;
    @Bind(R.id.pager)
    ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_main_main, null);
        ButterKnife.bind(this, root);
        intView();
        return root;
    }

    private void intView() {
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("活动");
        titles.add("群组");
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new MainActiveFragment());
        fragments.add(new MainGroupFragment());
        pager.setAdapter(new PagerTabAdapter(fm, titles, fragments));
        pager.setOnPageChangeListener(this);

        tabs.setViewPager(pager);
        tabs.setTitles(titles);

    }

    @OnClick(R.id.scan)
    public void clickScan() {
        UIHelper.showCapture(_activity);
    }

    @OnClick(R.id.add)
    public void clickAdd() {
        UIHelper.showCreateType(_activity, pager.getCurrentItem());
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
