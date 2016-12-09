package com.lailem.app.chat.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.chat.listener.OnMessageCountListener;
import com.lailem.app.dao.ConversationDao;
import com.lailem.app.dao.DaoFactory;
import com.lailem.app.dao.MGroupDao;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.MessageDao;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.dao.query.QueryBuilder;

public class MessageCountManager {

	public static final String KEY_NO_READ_COUNT_FOR_COM = "1";
	public static final String KEY_NO_READ_COUNT_FOR_CHAT = "2";
	public static final String KEY_NO_READ_COUNT_FOR_NGPUB = "3";
	Activity activity = new Activity();

	HashMap<String, Integer> map;
	ArrayList<OnMessageCountListener> onMessageCountListeners;

	private static MessageCountManager instance;

	private MessageCountManager() {
		map = new HashMap<String, Integer>();
	}

	public static MessageCountManager getInstance() {
		if (instance == null)
			instance = new MessageCountManager();
		return instance;
	}

	public void initCount(final Context context) {

		if (TextUtils.isEmpty(AppContext.getInstance().getLoginUid())) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 未读会话消息数量
				SQLiteDatabase db = DaoFactory.getInstance(context).getDb();
				Cursor cursor = db.rawQuery("select sum(" + ConversationDao.Properties.NoReadCount.columnName + ")" + " from " + ConversationDao.TABLENAME + " where "
						+ ConversationDao.Properties.UserId.columnName + "=?", new String[] { AppContext.getInstance().getLoginUid() });
				while (cursor.moveToNext()) {
					setCount(KEY_NO_READ_COUNT_FOR_CHAT, cursor.getInt(0));
				}
				// 未读讨论与点赞消息数量
				MessageDao messageDao = DaoFactory.getInstance(context).getMessageDao();
				QueryBuilder<Message> messageQb = messageDao.queryBuilder();
				messageQb.where(MessageDao.Properties.UserId.eq(AppContext.getInstance().getLoginUid()), MessageDao.Properties.IsRead.eq(Constant.value_no),
						MessageDao.Properties.SType.in(Constant.sType_aCom, Constant.sType_aLike, Constant.sType_dCom, Constant.sType_dLike));
				setCount(KEY_NO_READ_COUNT_FOR_COM, (int) messageQb.count());
				// 群或活动新发表的数量
				cursor = db.rawQuery("select sum(" + MGroupDao.Properties.NewPublishCount.columnName + ")" + " from " + MGroupDao.TABLENAME + " where " + MGroupDao.Properties.UserId.columnName + "=?",
						new String[] { AppContext.getInstance().getLoginUid() });
				while (cursor.moveToNext()) {
					setCount(KEY_NO_READ_COUNT_FOR_NGPUB, cursor.getInt(0));
				}

			}
		}).start();

	}

	public void setCount(String key, int count) {
		map.put(key, count);
		noticeListener(key,count);
		
	}

	public int getCount(String key) {
		Integer count = map.get(key);
		if (count == null)
			return 0;
		return count;
	}

	public synchronized void addOne(String key) {
		Integer count = getCount(key);
		if(count==null){
			count = 0;
		}
		count++;
		map.put(key, count);
		noticeListener(key, count);
	}
	
	public synchronized void reduce(String key,int count){
		Integer count1 = getCount(key);
		if(count1==null)
			count1=0;
		count1-=count;
		if(count1<0){
			count1=0;
		}
        map.put(key, count1);
		noticeListener(key, count1);
	}

	public void registerOnMessageCountListener(OnMessageCountListener onMessageCountListener) {
		if (onMessageCountListeners == null)
			onMessageCountListeners = new ArrayList<OnMessageCountListener>();
		onMessageCountListeners.add(onMessageCountListener);
	}

	public void unRegisterOnMessageCountListener(OnMessageCountListener onMessageCountListener) {
        if(onMessageCountListeners!=null) {
            onMessageCountListeners.remove(onMessageCountListener);
        }
	}

	public void destory(){
		if(onMessageCountListeners!=null&&onMessageCountListeners.size()>0)
			onMessageCountListeners.clear();
		onMessageCountListeners = null;
		map.clear();
		map = null;
		instance = null;
	}
	
	private void noticeListener(final String key,final int count){
		if(onMessageCountListeners!=null){
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					for(OnMessageCountListener onMessageCountListener:onMessageCountListeners){
						onMessageCountListener.onMessageCount(key, count);
					}
				}
			});
			
		}
	}
	
}
