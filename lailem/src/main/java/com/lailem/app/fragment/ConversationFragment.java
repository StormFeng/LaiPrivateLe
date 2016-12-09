package com.lailem.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ConversationListDataSource;
import com.lailem.app.base.BaseListFragment;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.ConversationSetBroadcastReceiver;
import com.lailem.app.chat.listener.OnConversationListener;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Conversation;
import com.lailem.app.tpl.ConversationTpl;
import com.lailem.app.loadfactory.ConversationLoadViewFactory;
import com.lailem.app.ui.me.SettingActivity;
import com.lailem.app.utils.SetTopUtil;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.Bind;

public class ConversationFragment extends BaseListFragment<Conversation> implements OnConversationListener {
	@Bind(R.id.topbar)
	TopBarView topbar;
	ConversationListDataSource dataSource;
    ConversationSetBroadcastReceiver conversationSetBroadcastReceiver = new ConversationSetBroadcastReceiver(){
    	public void onReceive(android.content.Context context, android.content.Intent intent) {
    		if(ConversationSetBroadcastReceiver.ACTION_SET_TOP.equals(intent.getAction())){
    			String conversationId = intent.getStringExtra("conversationId");
    			String isSetTop = intent.getStringExtra("isSetTop");
                if(Constant.value_yes.equals(isSetTop)){//置顶
                	SetTopUtil.getInstance().top(resultList, conversationId);
                }else{//取消置顶
                	SetTopUtil.getInstance().cancelTop(resultList, conversationId);
                } 
                adapter.notifyDataSetChanged();
    		}else if(ConversationSetBroadcastReceiver.ACTION_NO_TIP.equals(intent.getAction())){
    			String conversationId = intent.getStringExtra("conversationId");
    			String isNoTip = intent.getStringExtra("isNoTip");
    			for(Conversation conversation:resultList){
    				if(conversation.getConversationId().equals(conversationId)){
    					conversation.setIsNoTip(isNoTip);
    					adapter.notifyDataSetChanged();
    					break;
    				}
    			}
    		}else if(ConversationSetBroadcastReceiver.ACTION_CLEAR_MSG_RECORD.equals(intent.getAction())){
    			String conversationId = intent.getStringExtra("conversationId");
    			if(SettingActivity.CLEAR_ALL_CHAT_RECORD.equals(conversationId)){//清除所有聊天记录
    				listViewHelper.refresh();
    			}
    		}
    	}
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ChatListenerManager.getInstance().registerOnConversationListener(this);
		BroadcastManager.registerConversationSetBroadcastReceiver(_activity, conversationSetBroadcastReceiver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ChatListenerManager.getInstance().unRegisterOnConversationListener();
		BroadcastManager.unRegisterConversationSetBroadcastReceiver(_activity, conversationSetBroadcastReceiver);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		topbar.setTitle("消息");
	}

	@Override
	protected int getLayoutId() {
		return R.layout.c_list_activity;
	}

	@Override
	protected IDataSource<Conversation> getDataSource() {
		dataSource = new ConversationListDataSource(_activity);
		return dataSource;
	}

	@Override
	protected ILoadViewFactory getLoadViewFactory() {
		return new ConversationLoadViewFactory();
	}
	
	@Override
	protected ArrayList<Class> getTemplateClasses() {
		ArrayList<Class> tpls = new ArrayList<Class>();
		tpls.add(ConversationTpl.class);
		return tpls;
	}

	@Override
	public void onConversation(Conversation conversation) {
//		boolean isAdd = true;
//		for (Conversation c : resultList) {
//			if (c.getId() == conversation.getId()) {
//				c = conversation;
//				isAdd = false;
//				break;
//			}
//		}
//		if (isAdd) {
//			resultList.add(conversation);
//		}
//		adapter.notifyDataSetChanged();
        listViewHelper.refresh();
	}

}
