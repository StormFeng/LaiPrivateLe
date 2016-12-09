package com.lailem.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.adapter.PagerTabAdapter;
import com.lailem.app.base.BaseFragment;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.GroupBroadcastReceiver;
import com.lailem.app.chat.listener.OnComListener;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.MessageCountManager;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.MainPagerTabView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActiveFragment extends BaseFragment implements OnPageChangeListener, OnComListener, OnClickListener {
    @Bind(R.id.tabs)
    MainPagerTabView tabs;
    @Bind(R.id.pager)
    ViewPager pager;

    View header;
    TextView header_tv;
    int count;

    private GroupBroadcastReceiver groupBroadcastReceiver = new GroupBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GroupBroadcastReceiver.ACTION_JOIN_ACTIVITY.equals(action)) {
                pager.setCurrentItem(0);
            } else if (GroupBroadcastReceiver.ACTION_JOIN_GROUP.equals(action)) {
                pager.setCurrentItem(1);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.registerGroupBroadcastReceiver(_activity, groupBroadcastReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_main_main, null);
        ButterKnife.bind(this, root);
        intView(root);
        ChatListenerManager.getInstance().registerOnComListener(this);
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ChatListenerManager.getInstance().unRegisterOnComListener(this);
        BroadcastManager.unRegisterGroupBroadcastReceiver(_activity, groupBroadcastReceiver);
    }

    private void intView(View root) {
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("活动");
        titles.add("群组");
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new DynamicActiveFragment());
        fragments.add(new DynamicGroupFragment());
        pager.setAdapter(new PagerTabAdapter(fm, titles, fragments));
        pager.setOnPageChangeListener(this);

        tabs.setViewPager(pager);
        tabs.setTitles(titles);

        header = root.findViewById(R.id.header);
        header_tv = (TextView) header.findViewById(R.id.header_tv);
        header_tv.setOnClickListener(this);
        count = MessageCountManager.getInstance().getCount(MessageCountManager.KEY_NO_READ_COUNT_FOR_COM);
        if (count > 0) {
            showHeader();
        }
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

    @Override
    public void onCom(Message message) {
        count++;
        showHeader();

    }

    private void showHeader() {
        header.setVisibility(View.VISIBLE);
        header_tv.setText("你有" + count + "条互动消息");
    }

    private void hideHeader() {
        MessageCountManager.getInstance().reduce(MessageCountManager.KEY_NO_READ_COUNT_FOR_COM, count);
        count = 0;
        header.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_tv:
                UIHelper.showMessageList(_activity);
                hideHeader();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        List<Message> messages = DaoOperate.getInstance(_activity).queryMessagesNoRead(Constant.sType_aCom, Constant.sType_aLike, Constant.sType_dCom, Constant.sType_dLike);
                        if (messages != null && messages.size() > 0) {
                            for (Message message : messages) {
                                message.setIsRead(Constant.value_yes);
                            }
                            DaoOperate.getInstance(_activity).updateInTx(messages);
                        }
                    }
                }).start();

                break;
        }

    }
}
