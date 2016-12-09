package com.lailem.app.dao;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.chat.util.Constant;
import com.socks.library.KLog;

import java.util.Collection;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class DaoOperate {

    MessageDao messageDao;
    ConversationDao conversationDao;
    MGroupDao mGroupDao;
    GroupDao groupDao;
    UserDao userDao;
    RemarkDao remarkDao;
    BlacklistIdsDao blacklistIdsDao;
    private static DaoOperate instance;

    public static DaoOperate getInstance(Context context) {
        if (instance == null) {
            instance = new DaoOperate(context);
        }
        return instance;
    }

    private DaoOperate(Context context) {
        messageDao = DaoFactory.getInstance(context).getMessageDao();
        conversationDao = DaoFactory.getInstance(context).getConversationDao();
        mGroupDao = DaoFactory.getInstance(context).getMGroupDao();
        groupDao = DaoFactory.getInstance(context).getGroupDao();
        userDao = DaoFactory.getInstance(context).getUserDao();
        remarkDao = DaoFactory.getInstance(context).getRemarkDao();
        blacklistIdsDao = DaoFactory.getInstance(context).getBlacklistIdsDao();
    }

    // ---------------------message的增删改查---------------------------------
    public synchronized long insert(Message message) {
        return messageDao.insert(message);
    }

    public synchronized void delete(Message message) {
        messageDao.delete(message);
    }

    public synchronized void deleteInTxMessage(Iterable<Message> entities) {
        messageDao.deleteInTx(entities);
    }

    /**
     * 删除所有消息
     */
    public synchronized void deleteMessages() {
        String sql = "delete from " + messageDao.getTablename() + " where " + MessageDao.Properties.UserId.columnName + "=\"" + AppContext.getInstance().getLoginUid() + "\"";
        messageDao.getDatabase().execSQL(sql);
    }

    /**
     * 删除所有会话消息
     */
    public synchronized void deleteMessagesForConversation() {
        String sql = "delete from " + messageDao.getTablename() + " where " + MessageDao.Properties.ConversationId.columnName + "!=null" + " and " + MessageDao.Properties.UserId.columnName + "=\"" + AppContext.getInstance().getLoginUid() + "\"";
        messageDao.getDatabase().execSQL(sql);
    }

    public synchronized void deleteMessages(String conversationId) {
        String sql = "delete from " + messageDao.getTablename() + " where " + MessageDao.Properties.ConversationId.columnName + "=\"" + conversationId + "\" and " + MessageDao.Properties.UserId.columnName + "=\"" + AppContext.getInstance().getLoginUid() + "\"";
        messageDao.getDatabase().execSQL(sql);
    }

    public synchronized void update(Message message) {
        messageDao.update(message);
    }

    public synchronized void updateInTx(Iterable<Message> entities) {
        messageDao.updateInTx(entities);
    }

    public List<Message> queryMessagesForChat(String conversationId, long firstId, int countPerPage) {
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MessageDao.Properties.ConversationId.eq(conversationId), MessageDao.Properties.Id.lt(firstId));
        int totalCount = (int) qb.count();
        KLog.i("totalCount:::" + totalCount);
        int residue = totalCount % countPerPage;
        int pageCount = totalCount / countPerPage;
        if (residue > 0) {
            pageCount++;
        }
        if (pageCount == 1) {// 只有一页
            return qb.list();
        } else if (pageCount > 1) {// 至少有两页的情况
            if (residue > 0) {// 存在小于countPerPage数量的页
                qb.limit(countPerPage).offset(residue + (pageCount - 2) * countPerPage);
            } else {// 余数等于0时
                qb.limit(countPerPage).offset((pageCount - 1) * countPerPage);
            }
            return qb.list();
        } else {// pageCount = 0
            return null;
        }

    }

    public List<Message> queryMessagesForNotification(String conversationId, long lastId, int countPerPage) {
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MessageDao.Properties.ConversationId.eq(conversationId), MessageDao.Properties.Id.lt(lastId)).orderDesc(
                MessageDao.Properties.Id);
        qb.limit(countPerPage);
        return qb.list();
    }

    public List<Message> queryMessagesNoRead(String conversationId) {
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MessageDao.Properties.ConversationId.eq(conversationId), MessageDao.Properties.IsRead.eq(Constant.value_no));
        return qb.list();
    }

    public List<Message> queryMessagesNoRead(String... sTypes) {
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MessageDao.Properties.SType.in(sTypes), MessageDao.Properties.IsRead.eq(Constant.value_no));
        return qb.list();
    }

    public List<Message> queryMessages(String conversationId) {
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MessageDao.Properties.ConversationId.eq(conversationId));
        return qb.list();
    }

    public List<Message> queryMessagesForMeMessageList(long lastId, int countPerPage) {
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.Id.lt(lastId), MessageDao.Properties.SType.in(Constant.sType_aCom, Constant.sType_aLike, Constant.sType_dCom, Constant.sType_dLike),
                MessageDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()));
        qb.orderDesc(MessageDao.Properties.Id);
        qb.limit(countPerPage).offset(0);
        return qb.list();
    }

    public Long queryLastSTime(String conversationId) {
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MessageDao.Properties.ConversationId.eq(conversationId)).orderDesc(MessageDao.Properties.STime).limit(1);
        Message message = qb.unique();
        if (message != null)
            return message.getSTime() == null ? null : message.getSTime();
        return null;
    }

    // ---------------------conversation的增删改查---------------------------------
    public synchronized long insert(Conversation conversation) {
        return conversationDao.insert(conversation);
    }

    public synchronized void delete(Conversation conversation) {
        conversationDao.delete(conversation);
    }

    /**
     * 删除所有会话
     */
    public synchronized void deleteConversations() {
        String sql = "delete from " + conversationDao.getTablename() + " where " + ConversationDao.Properties.UserId.columnName + "=\"" + AppContext.getInstance().getLoginUid() + "\"";
        conversationDao.getDatabase().execSQL(sql);
    }

    public synchronized void update(Conversation conversation) {
        conversationDao.update(conversation);
    }

    public synchronized void updateConversationInTx(Iterable<Conversation> entities) {
        conversationDao.updateInTx(entities);
    }

    public List<Conversation> queryConversations() {
        QueryBuilder<Conversation> qb = conversationDao.queryBuilder();
        qb.where(ConversationDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid())).orderDesc(ConversationDao.Properties.TopTime).orderDesc(ConversationDao.Properties.UpdateTime);
        return qb.list();
    }

    public Conversation queryConversation(String tId) {
        QueryBuilder<Conversation> qb = conversationDao.queryBuilder();
        qb.where(ConversationDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), ConversationDao.Properties.TId.eq(tId));
        return qb.unique();
    }

    public Conversation queryConversationByConversationId(String conversationId) {
        QueryBuilder<Conversation> qb = conversationDao.queryBuilder();
        qb.where(ConversationDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), ConversationDao.Properties.ConversationId.eq(conversationId));
        return qb.unique();
    }

    // ---------------------mgroup的增删改查---------------------------------
    public synchronized long insert(MGroup mGroup) {
        return mGroupDao.insert(mGroup);
    }

    public synchronized long insert(String groupId, String groupType) {
        MGroup mGroup = new MGroup();
        mGroup.setCreateTime(System.currentTimeMillis());
        mGroup.setGroupId(groupId);
        mGroup.setGroupType(groupType);
        mGroup.setIsTop(Constant.value_no);
        mGroup.setNewApplyCount(0);
        mGroup.setNewNoticeCount(0);
        mGroup.setNewPublishCount(0);
        mGroup.setTotalCount(0);
        mGroup.setUpdateTime(System.currentTimeMillis());
        mGroup.setUserId(AppContext.getInstance().getLoginUid());
        return mGroupDao.insert(mGroup);
    }

    public synchronized void insertOrReplaceInTx(Iterable<MGroup> entities) {
        mGroupDao.insertOrReplaceInTx(entities);
    }

    public synchronized void delete(MGroup mGroup) {
        mGroupDao.delete(mGroup);
    }

    public synchronized void delete(String groupId) {
        MGroup mGroup = mGroupDao.queryBuilder().where(MGroupDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MGroupDao.Properties.GroupId.eq(groupId)).unique();
        if (mGroup != null) {
            mGroupDao.delete(mGroup);
        }
    }

    public synchronized void deleteInTxGroup(Iterable<MGroup> entities) {
        mGroupDao.deleteInTx(entities);
    }

    public synchronized void update(MGroup group) {
        mGroupDao.update(group);
    }

    public List<MGroup> queryMGroups(String gType) {
        QueryBuilder<MGroup> qb = mGroupDao.queryBuilder();
        qb.where(MGroupDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MGroupDao.Properties.GroupType.eq(gType)).orderDesc(MGroupDao.Properties.TopTime)
                .orderDesc(MGroupDao.Properties.UpdateTime);
        return qb.list();
    }

    public List<MGroup> queryMGroups() {
        QueryBuilder<MGroup> qb = mGroupDao.queryBuilder();
        qb.where(MGroupDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()));
        return qb.list();
    }

    public MGroup queryMGroup(String groupId) {
        QueryBuilder<MGroup> qb = mGroupDao.queryBuilder();
        qb.where(MGroupDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MGroupDao.Properties.GroupId.eq(groupId));
        return qb.unique();
    }

    public MGroup queryLastMGroup(String groupType) {
        QueryBuilder<MGroup> qb = mGroupDao.queryBuilder();
        qb.where(MGroupDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MGroupDao.Properties.GroupType.eq(groupType)).orderDesc(MGroupDao.Properties.Id).limit(1);
        return qb.unique();
    }

    // ---------------------group的增删改查---------------------------------
    public synchronized long insert(Group group) {
        return groupDao.insert(group);
    }

    public synchronized void update(Group group) {
        groupDao.update(group);
    }

    public Group queryGroup(String groupId) {
        QueryBuilder<Group> qb = groupDao.queryBuilder();
        qb.where(GroupDao.Properties.GroupId.eq(groupId));
//		Group group = qb.unique();
//		return group;
        List<Group> list = qb.list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * ---------------------------------user的增删改查------------------------------
     * ---------------------
     */
    public synchronized long insert(User user) {
        return userDao.insert(user);
    }

    public synchronized void update(User user) {
        userDao.update(user);
    }

    public User query(String userId) {
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.UserId.eq(userId));
        return qb.unique();
    }

    public List<User> query(int limit) {
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.limit(limit);
        return qb.list();
    }

    public List<User> query(Collection<String> userIds) {
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.UserId.in(userIds));
        return qb.list();
    }

    /**
     * ---------------------------------remark的增删改查----------------------------
     * -----------------------
     */
    public synchronized long insert(Remark remark) {
        return remarkDao.insert(remark);
    }

    public synchronized void insertOrReplaceInTxRemarks(Iterable<Remark> entities) {
        remarkDao.insertOrReplaceInTx(entities);
    }

    public synchronized void delete(Iterable<Remark> entities) {
        remarkDao.deleteInTx(entities);
    }

    public synchronized void update(Remark remark) {
        remarkDao.update(remark);
    }

    public Remark queryRemark(String userId) {
        QueryBuilder<Remark> qb = remarkDao.queryBuilder();
        qb.where(RemarkDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), RemarkDao.Properties.ToUserId.eq(userId));
        List<Remark> list = qb.list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<Remark> queryRemarks() {
        QueryBuilder<Remark> qb = remarkDao.queryBuilder();
        qb.where(RemarkDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()));
        return qb.list();
    }

    public List<Remark> queryRemarks(int limit) {
        QueryBuilder<Remark> qb = remarkDao.queryBuilder();
        qb.where(RemarkDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()));
        qb.limit(limit);
        return qb.list();
    }

    public List<Remark> queryRemarks(Collection<String> userIds) {
        QueryBuilder<Remark> qb = remarkDao.queryBuilder();
        qb.where(RemarkDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), RemarkDao.Properties.ToUserId.in(userIds));
        return qb.list();
    }

    // --------------------------------------blacklistids的增删改查----------------------------------------

    public List<BlacklistIds> queryBlacklistIds() {
        QueryBuilder<BlacklistIds> qb = blacklistIdsDao.queryBuilder();
        qb.where(BlacklistIdsDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()));
        return qb.list();
    }

    public synchronized void insertOrReplaceInTxBlacklistIds(Iterable<BlacklistIds> entities) {
        blacklistIdsDao.insertOrReplaceInTx(entities);
    }

    public synchronized void deleteBlacklistIds(Iterable<BlacklistIds> entities) {
        blacklistIdsDao.deleteInTx(entities);
    }

    public synchronized void deleteBlacklistIds(String userId) {
        QueryBuilder<BlacklistIds> qb = blacklistIdsDao.queryBuilder();
        qb.where(BlacklistIdsDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), BlacklistIdsDao.Properties.BlackUserId.eq(userId));
        blacklistIdsDao.deleteInTx(qb.list());
    }

    public BlacklistIds queryBlacklistIds(String userId) {
        QueryBuilder<BlacklistIds> qb = blacklistIdsDao.queryBuilder();
        qb.where(BlacklistIdsDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), BlacklistIdsDao.Properties.BlackUserId.eq(userId));
        return qb.unique();
    }

    public synchronized long insert(BlacklistIds blacklistIds) {
        return blacklistIdsDao.insert(blacklistIds);
    }

    public synchronized void update(BlacklistIds blacklistIds) {
        blacklistIdsDao.update(blacklistIds);
    }

    public List<BlacklistIds> queryBlacklistIds(int limit) {
        QueryBuilder<BlacklistIds> qb = blacklistIdsDao.queryBuilder();
        qb.where(BlacklistIdsDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()));
        qb.limit(limit);
        return qb.list();
    }

    public List<BlacklistIds> queryBlacklistIds(Collection<String> userIds) {
        QueryBuilder<BlacklistIds> qb = blacklistIdsDao.queryBuilder();
        qb.where(BlacklistIdsDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), BlacklistIdsDao.Properties.BlackUserId.in(userIds));
        return qb.list();
    }
}
