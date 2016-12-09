package com.lailem.app.ui.common;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.adapter.ImageViewerAdapter.ImageBean;
import com.lailem.app.adapter.OnPageChangeAdapter;
import com.lailem.app.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.HackyViewPager;

/**
 * 动态-查看大图
 *
 * @author XuYang
 */
public class ImageViewerActivity extends BaseActivity implements OnCheckedChangeListener {
    public static final int REQUEST_CODE_IMAGE_VIEWER = 10000;
    public static final String BUNDLE_KEY_IMAGE_BEANS = "image_beans";
    public static final String BUNDLE_KEY_IS_CHECK_MODE = "is_check_mode";
    public static final String BUNDLE_KEY_START_INDEX = "start_index";

    @Bind(R.id.pager)
    HackyViewPager pager;
    @Bind(R.id.indicator)
    TextView indicator_tv;
    @Bind(R.id.checkbox)
    CheckBox checkbox;

    private ArrayList<ImageBean> imageBeans = new ArrayList<ImageViewerAdapter.ImageBean>();
    private int startIndex;

    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();

        setContentView(R.layout.c_imageviewer);
        setTranslucentStatus(true, R.color.black);
        ButterKnife.bind(this);
        startIndex = _Bundle != null ? _Bundle.getInt(BUNDLE_KEY_START_INDEX, 0) : 0;
        initView();

    }

    private void initView() {
        ArrayList<ImageBean> beans = (ArrayList<ImageBean>) _Bundle.getSerializable(BUNDLE_KEY_IMAGE_BEANS);
        if (beans != null) {
            imageBeans.addAll(beans);
        }

        pager.setAdapter(new ImageViewerAdapter(this, imageBeans));
        pager.setCurrentItem(startIndex);
        indicator_tv.setText((startIndex + 1) + "/" + imageBeans.size());
        if (imageBeans.size() == 1) {
            indicator_tv.setVisibility(View.INVISIBLE);
        }
        pager.setOnPageChangeListener(new OnPageChangeAdapter() {
            @Override
            public void onPageSelected(int position) {
                indicator_tv.setText(position + 1 + "/" + imageBeans.size());
                checkbox.setChecked(imageBeans.get(position).isChecked);
            }
        });

        if (_Bundle != null && _Bundle.getBoolean(BUNDLE_KEY_IS_CHECK_MODE)) {
            checkbox.setVisibility(View.VISIBLE);
        } else {
            checkbox.setVisibility(View.GONE);
        }
        checkbox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        imageBeans.get(pager.getCurrentItem()).isChecked = isChecked;
    }

    @Override
    public void finish() {
        if (_Bundle.getBoolean(BUNDLE_KEY_IS_CHECK_MODE)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("imageBeans", imageBeans);
            setResult(RESULT_OK, bundle);
        }
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

}