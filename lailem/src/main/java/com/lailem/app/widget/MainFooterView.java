package com.lailem.app.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.chat.listener.OnMessageCountListener;
import com.lailem.app.chat.util.MessageCountManager;
import com.lailem.app.utils.DynamicTaskUtil;
import com.lailem.app.utils.TDevice;

import java.util.ArrayList;

/**
 * 主界面底部导航栏 Created by XuYang on 15/4/12.
 */
public class MainFooterView extends LinearLayout implements OnMessageCountListener {
    public static final int[][] iconsArr = {{R.drawable.main_tab_1_n, R.drawable.main_tab_1_c}, {R.drawable.main_tab_2_n, R.drawable.main_tab_2_c},
            {R.drawable.main_tab_3_n, R.drawable.main_tab_3_c}, {R.drawable.main_tab_4_n, R.drawable.main_tab_4_c}};

    private LinearLayout tab_1;
    private LinearLayout tab_2;
    private LinearLayout tab_3;
    private LinearLayout tab_4;

    private AppContext ac;
    private Context context;

    private ArrayList<LinearLayout> tabViews = new ArrayList<LinearLayout>();
    private onTabChangeListener onTabChangeListener;

    private int curIndex = 0;
    public TextView tab1RedDot, tab2RedDot, tab3RedDot, tab4RedDot;
    private DynamicTaskReceiver dynamicTaskReceiver = new DynamicTaskReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int dynamicTaskCount = DynamicTaskUtil.getDynamicTaskCount(context);
            if (dynamicTaskCount > 0) {
                tab4RedDot.setVisibility(VISIBLE);
                setSmallRedDot(tab4RedDot);
            } else {
                tab4RedDot.setVisibility(GONE);
            }
        }
    };

    public interface onTabChangeListener {
        void onTabChange(int index);
    }

    public MainFooterView(Context context) {
        super(context);
        init(context);
    }

    public MainFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.ac = (AppContext) context.getApplicationContext();
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_main_footer, this);

        tab_1 = (LinearLayout) findViewById(R.id.tab_1);
        tab1RedDot = (TextView) tab_1.findViewById(R.id.tab_1_red_dot);
        tab_2 = (LinearLayout) findViewById(R.id.tab_2);
        tab2RedDot = (TextView) tab_2.findViewById(R.id.tab_2_red_dot);
        tab_3 = (LinearLayout) findViewById(R.id.tab_3);
        tab3RedDot = (TextView) tab_3.findViewById(R.id.tab_3_red_dot);
        tab_4 = (LinearLayout) findViewById(R.id.tab_4);
        tab4RedDot = (TextView) tab_4.findViewById(R.id.tab_4_red_dot);

        tabViews.add(tab_1);
        tabViews.add(tab_2);
        tabViews.add(tab_3);
        tabViews.add(tab_4);

        for (int i = 0; i < tabViews.size(); i++) {
            final int index = i;
            final View tabView = tabViews.get(i);
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (index != curIndex) {
                        setCurIndex(index);
                    }
                }
            });
        }

        setCurIndex(curIndex);
        MessageCountManager.getInstance().registerOnMessageCountListener(this);
        MessageCountManager.getInstance().initCount(context);

        int dynamicTaskCount = DynamicTaskUtil.getDynamicTaskCount(context);
        if (dynamicTaskCount > 0) {
            tab4RedDot.setVisibility(VISIBLE);
            setSmallRedDot(tab4RedDot);
        } else {
            tab4RedDot.setVisibility(GONE);
        }

        BroadcastManager.registerDynamicTaskReceiver(context, dynamicTaskReceiver);


    }

    public MainFooterView.onTabChangeListener getOnTabChangeListener() {
        return onTabChangeListener;
    }

    public void setOnTabChangeListener(MainFooterView.onTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public void setCurIndex(int i) {
        ((ImageView) ((RelativeLayout) (tabViews.get(curIndex).getChildAt(0))).getChildAt(0)).setImageResource(iconsArr[this.curIndex][0]);
        ((TextView) tabViews.get(curIndex).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_tab_n));
        ((ImageView) ((RelativeLayout) (tabViews.get(i).getChildAt(0))).getChildAt(0)).setImageResource(iconsArr[i][1]);
        ((TextView) tabViews.get(i).getChildAt(1)).setTextColor(getResources().getColor(R.color.text_orange));
        if (onTabChangeListener != null) {
            onTabChangeListener.onTabChange(i);
        }
        this.curIndex = i;
    }

    int comCount, nGPubCount;

    @Override
    public void onMessageCount(String key, int count) {
        // 聊天
        if (MessageCountManager.KEY_NO_READ_COUNT_FOR_CHAT.equals(key)) {
            if (count == 0) {
                tab3RedDot.setText("");
                tab3RedDot.setVisibility(View.GONE);
            } else {
                tab3RedDot.setVisibility(View.VISIBLE);
                tab3RedDot.setText(count + "");
                setBigRedDot(tab3RedDot, count);
            }
            // 评论与点赞
        } else if (MessageCountManager.KEY_NO_READ_COUNT_FOR_COM.equals(key)) {
            if (count == 0) {
                tab2RedDot.setText("");
                if (nGPubCount == 0) {
                    tab2RedDot.setVisibility(View.GONE);
                } else {
                    setSmallRedDot(tab2RedDot);
                }
            } else {
                tab2RedDot.setText(count + "");
                tab2RedDot.setVisibility(View.VISIBLE);
                setBigRedDot(tab2RedDot, count);
            }
            comCount = count;
            // 群或活动新发表
        } else if (MessageCountManager.KEY_NO_READ_COUNT_FOR_NGPUB.equals(key)) {
            if (count == 0) {
                if (comCount == 0) {
                    tab2RedDot.setVisibility(View.GONE);
                }
            } else {
                if (comCount == 0) {
                    tab2RedDot.setVisibility(View.VISIBLE);
                    setSmallRedDot(tab2RedDot);
                }
            }
            nGPubCount = count;
        }

    }

    int smallTopMargin = (int) TDevice.dpToPixel(1);
    int bigTopMargin = (int) TDevice.dpToPixel(5);
    int smallHeight = (int) TDevice.dpToPixel(10);
    int bigHeight = (int) TDevice.dpToPixel(20);
    int bigWidth = (int) TDevice.dpToPixel(30);

    private void setBigRedDot(TextView textview, int count) {
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) textview.getLayoutParams();
        if (count < 10) {
            params.width = bigHeight;
            params.height = bigHeight;
        } else if (count < 100) {
            params.width = bigWidth;
            params.height = bigHeight;
        } else {
            params.width = bigWidth;
            params.height = bigHeight;
            textview.setText("•••");
        }
        params.topMargin = smallTopMargin;
        textview.setLayoutParams(params);
        textview.setBackgroundResource(R.drawable.ic_main_foot_view_red_dot);
    }

    private void setSmallRedDot(TextView textview) {
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) textview.getLayoutParams();
        params.width = smallHeight;
        params.height = smallHeight;
        params.topMargin = bigTopMargin;
        textview.setLayoutParams(params);
        textview.setBackgroundResource(R.drawable.ic_main_foot_view_red_dot_for_small);
    }

    public void destory() {
        MessageCountManager.getInstance().unRegisterOnMessageCountListener(this);
        BroadcastManager.unRegisterDynamicTaskReceiver(context, dynamicTaskReceiver);
    }
}
