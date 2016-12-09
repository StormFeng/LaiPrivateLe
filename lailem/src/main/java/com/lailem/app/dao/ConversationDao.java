package com.lailem.app.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONVERSATION".
*/
public class ConversationDao extends AbstractDao<Conversation, Long> {

    public static final String TABLENAME = "CONVERSATION";

    /**
     * Properties of entity Conversation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CreateTime = new Property(1, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property UserId = new Property(2, String.class, "userId", false, "USER_ID");
        public final static Property NeedSendNick = new Property(3, String.class, "needSendNick", false, "NEED_SEND_NICK");
        public final static Property NeedSendHead = new Property(4, String.class, "needSendHead", false, "NEED_SEND_HEAD");
        public final static Property TId = new Property(5, String.class, "tId", false, "T_ID");
        public final static Property CType = new Property(6, String.class, "cType", false, "C_TYPE");
        public final static Property IsTop = new Property(7, String.class, "isTop", false, "IS_TOP");
        public final static Property TopTime = new Property(8, Long.class, "topTime", false, "TOP_TIME");
        public final static Property UpdateTime = new Property(9, Long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property IsNoTip = new Property(10, String.class, "isNoTip", false, "IS_NO_TIP");
        public final static Property TipMsg = new Property(11, String.class, "tipMsg", false, "TIP_MSG");
        public final static Property NoReadCount = new Property(12, Integer.class, "noReadCount", false, "NO_READ_COUNT");
        public final static Property Status = new Property(13, String.class, "status", false, "STATUS");
        public final static Property ConversationId = new Property(14, String.class, "conversationId", false, "CONVERSATION_ID");
        public final static Property ChatBg = new Property(15, String.class, "chatBg", false, "CHAT_BG");
    };


    public ConversationDao(DaoConfig config) {
        super(config);
    }
    
    public ConversationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONVERSATION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CREATE_TIME\" INTEGER," + // 1: createTime
                "\"USER_ID\" TEXT," + // 2: userId
                "\"NEED_SEND_NICK\" TEXT," + // 3: needSendNick
                "\"NEED_SEND_HEAD\" TEXT," + // 4: needSendHead
                "\"T_ID\" TEXT," + // 5: tId
                "\"C_TYPE\" TEXT," + // 6: cType
                "\"IS_TOP\" TEXT," + // 7: isTop
                "\"TOP_TIME\" INTEGER," + // 8: topTime
                "\"UPDATE_TIME\" INTEGER," + // 9: updateTime
                "\"IS_NO_TIP\" TEXT," + // 10: isNoTip
                "\"TIP_MSG\" TEXT," + // 11: tipMsg
                "\"NO_READ_COUNT\" INTEGER," + // 12: noReadCount
                "\"STATUS\" TEXT," + // 13: status
                "\"CONVERSATION_ID\" TEXT," + // 14: conversationId
                "\"CHAT_BG\" TEXT);"); // 15: chatBg
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONVERSATION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Conversation entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(2, createTime);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String needSendNick = entity.getNeedSendNick();
        if (needSendNick != null) {
            stmt.bindString(4, needSendNick);
        }
 
        String needSendHead = entity.getNeedSendHead();
        if (needSendHead != null) {
            stmt.bindString(5, needSendHead);
        }
 
        String tId = entity.getTId();
        if (tId != null) {
            stmt.bindString(6, tId);
        }
 
        String cType = entity.getCType();
        if (cType != null) {
            stmt.bindString(7, cType);
        }
 
        String isTop = entity.getIsTop();
        if (isTop != null) {
            stmt.bindString(8, isTop);
        }
 
        Long topTime = entity.getTopTime();
        if (topTime != null) {
            stmt.bindLong(9, topTime);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(10, updateTime);
        }
 
        String isNoTip = entity.getIsNoTip();
        if (isNoTip != null) {
            stmt.bindString(11, isNoTip);
        }
 
        String tipMsg = entity.getTipMsg();
        if (tipMsg != null) {
            stmt.bindString(12, tipMsg);
        }
 
        Integer noReadCount = entity.getNoReadCount();
        if (noReadCount != null) {
            stmt.bindLong(13, noReadCount);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(14, status);
        }
 
        String conversationId = entity.getConversationId();
        if (conversationId != null) {
            stmt.bindString(15, conversationId);
        }
 
        String chatBg = entity.getChatBg();
        if (chatBg != null) {
            stmt.bindString(16, chatBg);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Conversation readEntity(Cursor cursor, int offset) {
        Conversation entity = new Conversation( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // createTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // needSendNick
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // needSendHead
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // tId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // cType
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // isTop
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // topTime
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // updateTime
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // isNoTip
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // tipMsg
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // noReadCount
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // status
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // conversationId
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15) // chatBg
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Conversation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreateTime(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setUserId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNeedSendNick(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNeedSendHead(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCType(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsTop(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTopTime(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setUpdateTime(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setIsNoTip(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTipMsg(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setNoReadCount(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setStatus(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setConversationId(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setChatBg(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Conversation entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Conversation entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
