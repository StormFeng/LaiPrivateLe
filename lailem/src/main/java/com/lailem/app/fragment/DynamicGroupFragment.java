package com.lailem.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.lailem.app.adapter.datasource.DynamicGroupListDataSource;
import com.lailem.app.base.BaseGridListFragment;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.GroupBroadcastReceiver;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.listener.OnGroupListener;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.MGroup;
import com.lailem.app.tpl.DynamicGroupTpl;
import com.lailem.app.loadfactory.DynamicGroupLoadViewFactory;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import butterknife.ButterKnife;

public class DynamicGroupFragment extends BaseGridListFragment<MGroup> implements OnGroupListener {
    GroupBroadcastReceiver groupBroadcastReceiver = new GroupBroadcastReceiver() {
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            // 群同步完成
            if (GroupBroadcastReceiver.ACTION_GROUP_SNYC_OVER.equals(intent.getAction())) {
                listViewHelper.refresh();
                //加入或新建群
            } else if (GroupBroadcastReceiver.ACTION_JOIN_GROUP.equals(intent.getAction())) {
                MGroup mGroup = DaoOperate.getInstance(_activity).queryLastMGroup(Constant.gType_group);
                if (mGroup != null) {
                    if (resultList.size() > 0) {
                        for (int i = 0; i < resultList.size(); i++) {
                            if (Constant.value_yes.equals(resultList.get(i).getIsTop())) {
                                continue;
                            } else {
                                resultList.add(i, mGroup);
                                break;
                            }
                        }
                    } else {
                        resultList.add(0, mGroup);
                    }
                    adapter.notifyDataSetChanged();
                }
                //退出或解散群
            } else if (GroupBroadcastReceiver.ACTION_EXIT_GROUP.equals(intent.getAction())) {
                String groupId = intent.getStringExtra("groupId");
                for (MGroup mGroup : resultList) {
                    if (mGroup.getGroupId().equals(groupId)) {
                        resultList.remove(mGroup);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            } else if (GroupBroadcastReceiver.ACTION_UPDATE_GROUP.equals(intent.getAction())) {//更新资料
                String groupId = intent.getStringExtra("groupId");
                for (MGroup mGroup : resultList) {
                    if (mGroup.getGroupId().equals(groupId)) {
                        GroupCache.getInstance(_activity).removeByKey(groupId);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

        }

        ;
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.registerGroupBroadcastReceiver(_activity, groupBroadcastReceiver);
        ChatListenerManager.getInstance().registerOnGroupListener(this);
    }

    ;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();

    }

    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.unRegisterGroupBroadcastReceiver(_activity, groupBroadcastReceiver);
        ChatListenerManager.getInstance().unRegisterOnGroupListener(this);
    }

    ;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView() {
        gridView.setPadding((int) TDevice.dpToPixel(23f), 0, (int) TDevice.dpToPixel(23f), (int) TDevice.dpToPixel(23f));
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing((int) TDevice.dpToPixel(23f));
    }

    @Override
    protected IDataSource<MGroup> getDataSource() {
        return new DynamicGroupListDataSource(_activity);
    }

    @Override
    protected Class getTemplateClass() {
        return DynamicGroupTpl.class;
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new DynamicGroupLoadViewFactory();
    }

    @Override
    public void onGroup(MGroup mGroup) {
        if (Constant.gType_group.equals(mGroup.getGroupType())) {
//			for(MGroup g:resultList){
//				if(g.getGroupId().equals(mGroup.getGroupId())){
//					int newPublishCount = g.getNewPublishCount();
//					if(newPublishCount==1){
//						adapter.notifyDataSetChanged();
//					}
//					break;
//				}
//			}
            GroupUtils.sortGroups(resultList);
            adapter.notifyDataSetChanged();
        }
    }
}
