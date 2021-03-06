package com.lailem.app.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GROUPS".
*/
public class GroupDao extends AbstractDao<Group, Long> {

    public static final String TABLENAME = "GROUPS";

    /**
     * Properties of entity Group.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CreateTime = new Property(1, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property GroupId = new Property(2, String.class, "groupId", false, "GROUP_ID");
        public final static Property GroupType = new Property(3, String.class, "groupType", false, "GROUP_TYPE");
        public final static Property Name = new Property(4, String.class, "name", false, "NAME");
        public final static Property SquareSPic = new Property(5, String.class, "squareSPic", false, "SQUARE_SPIC");
        public final static Property SPic = new Property(6, String.class, "sPic", false, "S_PIC");
        public final static Property UpdateTime = new Property(7, Long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property Intro = new Property(8, String.class, "intro", false, "INTRO");
        public final static Property Pic = new Property(9, String.class, "pic", false, "PIC");
        public final static Property GroupNum = new Property(10, String.class, "groupNum", false, "GROUP_NUM");
        public final static Property StartTime = new Property(11, Long.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(12, Long.class, "endTime", false, "END_TIME");
        public final static Property TypeId = new Property(13, String.class, "typeId", false, "TYPE_ID");
        public final static Property TypeName = new Property(14, String.class, "typeName", false, "TYPE_NAME");
        public final static Property AreaId = new Property(15, String.class, "areaId", false, "AREA_ID");
        public final static Property Address = new Property(16, String.class, "address", false, "ADDRESS");
        public final static Property Lon = new Property(17, String.class, "lon", false, "LON");
        public final static Property Lat = new Property(18, String.class, "lat", false, "LAT");
        public final static Property Permission = new Property(19, Integer.class, "permission", false, "PERMISSION");
    };


    public GroupDao(DaoConfig config) {
        super(config);
    }
    
    public GroupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GROUPS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CREATE_TIME\" INTEGER," + // 1: createTime
                "\"GROUP_ID\" TEXT," + // 2: groupId
                "\"GROUP_TYPE\" TEXT," + // 3: groupType
                "\"NAME\" TEXT," + // 4: name
                "\"SQUARE_SPIC\" TEXT," + // 5: squareSPic
                "\"S_PIC\" TEXT," + // 6: sPic
                "\"UPDATE_TIME\" INTEGER," + // 7: updateTime
                "\"INTRO\" TEXT," + // 8: intro
                "\"PIC\" TEXT," + // 9: pic
                "\"GROUP_NUM\" TEXT," + // 10: groupNum
                "\"START_TIME\" INTEGER," + // 11: startTime
                "\"END_TIME\" INTEGER," + // 12: endTime
                "\"TYPE_ID\" TEXT," + // 13: typeId
                "\"TYPE_NAME\" TEXT," + // 14: typeName
                "\"AREA_ID\" TEXT," + // 15: areaId
                "\"ADDRESS\" TEXT," + // 16: address
                "\"LON\" TEXT," + // 17: lon
                "\"LAT\" TEXT," + // 18: lat
                "\"PERMISSION\" INTEGER);"); // 19: permission
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GROUPS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Group entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(2, createTime);
        }
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(3, groupId);
        }
 
        String groupType = entity.getGroupType();
        if (groupType != null) {
            stmt.bindString(4, groupType);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String squareSPic = entity.getSquareSPic();
        if (squareSPic != null) {
            stmt.bindString(6, squareSPic);
        }
 
        String sPic = entity.getSPic();
        if (sPic != null) {
            stmt.bindString(7, sPic);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(8, updateTime);
        }
 
        String intro = entity.getIntro();
        if (intro != null) {
            stmt.bindString(9, intro);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(10, pic);
        }
 
        String groupNum = entity.getGroupNum();
        if (groupNum != null) {
            stmt.bindString(11, groupNum);
        }
 
        Long startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(12, startTime);
        }
 
        Long endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(13, endTime);
        }
 
        String typeId = entity.getTypeId();
        if (typeId != null) {
            stmt.bindString(14, typeId);
        }
 
        String typeName = entity.getTypeName();
        if (typeName != null) {
            stmt.bindString(15, typeName);
        }
 
        String areaId = entity.getAreaId();
        if (areaId != null) {
            stmt.bindString(16, areaId);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(17, address);
        }
 
        String lon = entity.getLon();
        if (lon != null) {
            stmt.bindString(18, lon);
        }
 
        String lat = entity.getLat();
        if (lat != null) {
            stmt.bindString(19, lat);
        }
 
        Integer permission = entity.getPermission();
        if (permission != null) {
            stmt.bindLong(20, permission);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Group readEntity(Cursor cursor, int offset) {
        Group entity = new Group( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // createTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // groupId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // groupType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // squareSPic
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // sPic
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // updateTime
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // intro
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // pic
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // groupNum
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11), // startTime
            cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12), // endTime
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // typeId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // typeName
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // areaId
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // address
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // lon
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // lat
            cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19) // permission
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Group entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreateTime(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setGroupId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGroupType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSquareSPic(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSPic(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUpdateTime(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setIntro(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPic(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setGroupNum(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setStartTime(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
        entity.setEndTime(cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12));
        entity.setTypeId(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setTypeName(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setAreaId(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setAddress(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setLon(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setLat(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setPermission(cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Group entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Group entity) {
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
