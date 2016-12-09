package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;
import com.lailem.app.utils.ResourceUtil;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddDynamicChoiceDialog extends Dialog {
    public static final int TYPE_PHOTO = 0;
    public static final int TYPE_GALLERY = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_VOICE = 4;
    public static final int TYPE_ADDRESS = 5;
    public static final int TYPE_VOTE = 6;

    private Context context;
    private ResourceUtil resourceUtil;

    private String groupId;

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.indicator)
    DotIndicator indicator;

    String[] titles;
    String[] icons;

    protected ArrayList<GridView> mGridViews;
    protected int index;

    private static final int DEFAULT_THEME = R.style.confirm_dialog;
    private View contentView;

    public AddDynamicChoiceDialog(Context context) {
        super(context, DEFAULT_THEME);
        init(context);
    }

    public AddDynamicChoiceDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        contentView = View.inflate(context, R.layout.view_add_dynamic_choice, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);


        resourceUtil = new ResourceUtil(context);
        titles = context.getResources().getStringArray(R.array.add_dynamic_choice_title);
        icons = context.getResources().getStringArray(R.array.add_dynamic_choice_icon);

        FrameLayout.LayoutParams pagerParams = new FrameLayout.LayoutParams((int) TDevice.getScreenWidth(), (int) (TDevice.getScreenHeight() * 1.9f / 3.0f));
        pager.setLayoutParams(pagerParams);

        mGridViews = new ArrayList<GridView>();

        final int pageCount = titles.length / 9 + (titles.length % 9 == 0 ? 0 : 1);
        for (int i = 0; i < pageCount; i++) {
            final int page = i;
            GridView gridView = (GridView) View.inflate(getContext(), R.layout.view_add_dynamic_choice_gridview, null);
            gridView.setAdapter(new BaseAdapter() {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView tv = (TextView) View.inflate(getContext(), R.layout.item_add_dynamic_choice_gridview, null);
                    tv.setText(getItem(position));
                    tv.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable(resourceUtil.getDrawable(icons[9 * page + position])), null, null);
                    return tv;
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public String getItem(int position) {
                    return titles[9 * page + position];
                }

                @Override
                public int getCount() {
                    int size = titles.length / 9;
                    if (page == size)
                        return titles.length - 9 * page;
                    else
                        return 9;
                }
            });
            gridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    index = page * 9 + position;
                    switch (index) {
                        case TYPE_PHOTO:
                            UIHelper.showCreateDyanmic(context, groupId, CreateDynamicActivity.TYPE_PHOTO);
                            break;
                        case TYPE_GALLERY:
                            UIHelper.showCreateDyanmic(context, groupId, CreateDynamicActivity.TYPE_GALLERY);
                            break;
                        case TYPE_TEXT:
                            UIHelper.showCreateDyanmic(context, groupId, CreateDynamicActivity.TYPE_TEXT);
                            break;
                        case TYPE_VIDEO:
                            UIHelper.showCreateDyanmic(context, groupId, CreateDynamicActivity.TYPE_VIDEO);
                            break;
                        case TYPE_VOICE:
                            UIHelper.showCreateDyanmic(context, groupId, CreateDynamicActivity.TYPE_VOICE);
                            break;
                        case TYPE_ADDRESS:
                            UIHelper.showCreateDyanmic(context, groupId, CreateDynamicActivity.TYPE_ADDRESS);
                            break;
                        case TYPE_VOTE:
                            UIHelper.showCreateDyanmic(context, groupId, CreateDynamicActivity.TYPE_VOTE);
                            break;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hide();
                        }
                    }, 200);
                }
            });
            mGridViews.add(gridView);

        }

        pager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mGridViews.get(position));
                return mGridViews.get(position);
            }

            @Override
            public int getCount() {
                return pageCount;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        indicator.initIndicator(context, pager);
        indicator.setDotRes(R.drawable.dot_orange, R.drawable.dot_gray);
    }

    public void setGroupData(String groupId) {
        this.groupId = groupId;
    }

}
