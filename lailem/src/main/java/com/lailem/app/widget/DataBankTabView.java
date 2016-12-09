package com.lailem.app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.utils.TDevice;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

public class DataBankTabView extends FrameLayout {
    private AppContext ac;
    private Activity activity;

    private ViewPager pager;
    private ArrayList<String> titles;
    private LinearLayout tabLayout;
    private ImageView line_iv;

    private int currentTabIndex;
    private ImageView bottomLine_iv;

    public DataBankTabView(Context context) {
        super(context);
        init(context);
    }

    public DataBankTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        activity = (Activity) context;
        ac = (AppContext) context.getApplicationContext();
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
        init();
        for (int i = 0; i < titles.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.weight = 1;
            TextView tv = (TextView) View.inflate(getContext(), R.layout.view_pager_tab, null);
            tv.setText(titles.get(i));
            tabLayout.addView(tv, params);
            final int index = i;
            tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(index);
                }
            });
        }

        setCurrentTab(currentTabIndex);
    }

    private void init() {
        tabLayout = new LinearLayout(getContext());
        tabLayout.setOrientation(LinearLayout.HORIZONTAL);
        FrameLayout.LayoutParams tabLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(tabLayout, tabLayoutParams);

        line_iv = new ImageView(getContext());
        ColorDrawable lineBg = new ColorDrawable(getResources().getColor(R.color.orange));
        line_iv.setImageDrawable(lineBg);
        FrameLayout.LayoutParams lineParams = new FrameLayout.LayoutParams((int) (TDevice.getScreenWidth() / titles.size()), (int) TDevice.dpToPixel(3));
        lineParams.gravity = Gravity.BOTTOM;
        line_iv.setPadding((int) TDevice.dpToPixel(6.7f), 0, (int) TDevice.dpToPixel(6.7f), 0);
        lineParams.leftMargin = (int) TDevice.dpToPixel(0);
        addView(line_iv, lineParams);
    }

    public void setCurrentTab(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            TextView child = (TextView) tabLayout.getChildAt(i);
            if (i == currentTabIndex) {
                child.setTextColor(getResources().getColor(R.color.orange));
                continue;
            }
            child.setTextColor(getResources().getColor(R.color.text_medium_2));
        }
        if (titles != null) {
            ViewHelper.setTranslationX(line_iv, TDevice.getScreenWidth() / titles.size() * currentTabIndex);
        }
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
        ViewHelper.setTranslationX(line_iv, TDevice.getScreenWidth() / titles.size() * (arg0 + arg1));
    }

}
