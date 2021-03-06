package com.lailem.app.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig remarkDaoConfig;
    private final DaoConfig groupDaoConfig;
    private final DaoConfig mGroupDaoConfig;
    private final DaoConfig conversationDaoConfig;
    private final DaoConfig messageDaoConfig;
    private final DaoConfig regionDaoConfig;
    private final DaoConfig sysPropertyDaoConfig;
    private final DaoConfig blacklistIdsDaoConfig;

    private final UserDao userDao;
    private final RemarkDao remarkDao;
    private final GroupDao groupDao;
    private final MGroupDao mGroupDao;
    private final ConversationDao conversationDao;
    private final MessageDao messageDao;
    private final RegionDao regionDao;
    private final SysPropertyDao sysPropertyDao;
    private final BlacklistIdsDao blacklistIdsDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        remarkDaoConfig = daoConfigMap.get(RemarkDao.class).clone();
        remarkDaoConfig.initIdentityScope(type);

        groupDaoConfig = daoConfigMap.get(GroupDao.class).clone();
        groupDaoConfig.initIdentityScope(type);

        mGroupDaoConfig = daoConfigMap.get(MGroupDao.class).clone();
        mGroupDaoConfig.initIdentityScope(type);

        conversationDaoConfig = daoConfigMap.get(ConversationDao.class).clone();
        conversationDaoConfig.initIdentityScope(type);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        regionDaoConfig = daoConfigMap.get(RegionDao.class).clone();
        regionDaoConfig.initIdentityScope(type);

        sysPropertyDaoConfig = daoConfigMap.get(SysPropertyDao.class).clone();
        sysPropertyDaoConfig.initIdentityScope(type);

        blacklistIdsDaoConfig = daoConfigMap.get(BlacklistIdsDao.class).clone();
        blacklistIdsDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        remarkDao = new RemarkDao(remarkDaoConfig, this);
        groupDao = new GroupDao(groupDaoConfig, this);
        mGroupDao = new MGroupDao(mGroupDaoConfig, this);
        conversationDao = new ConversationDao(conversationDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);
        regionDao = new RegionDao(regionDaoConfig, this);
        sysPropertyDao = new SysPropertyDao(sysPropertyDaoConfig, this);
        blacklistIdsDao = new BlacklistIdsDao(blacklistIdsDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Remark.class, remarkDao);
        registerDao(Group.class, groupDao);
        registerDao(MGroup.class, mGroupDao);
        registerDao(Conversation.class, conversationDao);
        registerDao(Message.class, messageDao);
        registerDao(Region.class, regionDao);
        registerDao(SysProperty.class, sysPropertyDao);
        registerDao(BlacklistIds.class, blacklistIdsDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        remarkDaoConfig.getIdentityScope().clear();
        groupDaoConfig.getIdentityScope().clear();
        mGroupDaoConfig.getIdentityScope().clear();
        conversationDaoConfig.getIdentityScope().clear();
        messageDaoConfig.getIdentityScope().clear();
        regionDaoConfig.getIdentityScope().clear();
        sysPropertyDaoConfig.getIdentityScope().clear();
        blacklistIdsDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public RemarkDao getRemarkDao() {
        return remarkDao;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public MGroupDao getMGroupDao() {
        return mGroupDao;
    }

    public ConversationDao getConversationDao() {
        return conversationDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public RegionDao getRegionDao() {
        return regionDao;
    }

    public SysPropertyDao getSysPropertyDao() {
        return sysPropertyDao;
    }

    public BlacklistIdsDao getBlacklistIdsDao() {
        return blacklistIdsDao;
    }

}
