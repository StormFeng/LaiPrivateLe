package com.lailem.app.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.baidu.mobstat.StatService;
import com.lailem.app.AppContext;

/**
 * Fragment基类
 *
 * @author XuYang
 */
public class BaseFragment extends Fragment {

    protected AppContext ac;
    protected BaseActivity _activity;
    protected Fragment _fragment;
    protected FragmentManager fm;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _activity = (BaseActivity) activity;
        ac = (AppContext) _activity.getApplication();
        fm = getChildFragmentManager();
        _fragment = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(_activity, getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(_activity, getClass().getName());
    }
}
