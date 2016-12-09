package com.lailem.app.runnable.sync;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.MGroup;
import com.lailem.app.jsonbean.activegroup.SyncGroupListBean;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.GroupBriefUtil;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步我的group
 *
 * @author leeib
 */
public class SyncGroupRunnable implements Runnable {
    Context context;

    public SyncGroupRunnable(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            SyncGroupListBean list = (SyncGroupListBean) ApiClient.getApi().syncGroup(AppContext.getInstance().getLoginUid(), null);
            KLog.i("SyncGroupListBean:::" + list);
            if (list != null && Const.value_success.equals(list.ret)) {
                ArrayList<com.lailem.app.jsonbean.activegroup.SyncGroupListBean.Group> gruops = list.getGroupList();

                if (gruops != null && gruops.size() > 0) {
                    List<MGroup> dbGroups = DaoOperate.getInstance(context).queryMGroups();
                    // 新增的或需要更新的
                    ArrayList<MGroup> addOrUpdateGroups = new ArrayList<MGroup>();

                    for (com.lailem.app.jsonbean.activegroup.SyncGroupListBean.Group gup : gruops) {
                        MGroup g = null;
                        for (MGroup group : dbGroups) {
                            if (gup.getId().equals(group.getGroupId())) {
                                g = group;
                                break;
                            }
                        }

                        if (g != null) {// 数据库有
                            // 从dbGroups集合中移除已经匹配到的，最后剩下没有匹配的就是需要删除的
                            dbGroups.remove(g);
                        } else {// 本地数据库没有
                            g = new MGroup();
                            g.setCreateTime(System.currentTimeMillis());
                            g.setGroupId(gup.getId());
                            g.setGroupType(gup.getgType());
                            g.setIsTop(Constant.value_no);
                            g.setNewApplyCount(0);
                            g.setNewNoticeCount(0);
                            g.setNewPublishCount(0);
                            g.setTotalCount(0);
                            g.setUpdateTime(System.currentTimeMillis());
                            g.setUserId(AppContext.getInstance().getLoginUid());
                            addOrUpdateGroups.add(g);
                            //处理group信息
                            GroupBriefUtil.getGroupBriefFromNetBySync(gup.getId(), gup.getgType(), context);

                        }
                    }

                    if (addOrUpdateGroups.size() > 0) {
                        DaoOperate.getInstance(context).insertOrReplaceInTx(addOrUpdateGroups);
                    }
                    // 删除没有匹配的
                    if (dbGroups.size() > 0) {
                        DaoOperate.getInstance(context).deleteInTxGroup(dbGroups);
                    }
                    // 发送广播通知相关地方刷新数据
                    BroadcastManager.sendGroupSnycOverBroadcast(context);
                    BroadcastManager.sendActivitySnycOverBroadcast(context);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
