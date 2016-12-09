package com.lailem.app.utils;

import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.MGroup;

import java.util.List;

/**
 * 内存置顶
 *
 * @author 94505
 */
public class SetTopUtil {
    private static SetTopUtil instance;

    private SetTopUtil() {

    }

    public static SetTopUtil getInstance() {
        if (instance == null)
            instance = new SetTopUtil();
        return instance;
    }

    public void top(List<Conversation> conversations, Conversation conversation) {
        conversation.setIsTop(Constant.value_yes);
        conversation.setTopTime(System.currentTimeMillis());
        conversations.remove(conversation);
        conversations.add(0, conversation);
    }

    public void top(List<Conversation> conversations, String conversationId) {
        Conversation c = null;
        for (Conversation conversation : conversations) {
            if (conversation.getConversationId().equals(conversationId)) {
                c = conversation;
                break;
            }
        }
        c.setIsTop(Constant.value_yes);
        c.setTopTime(System.currentTimeMillis());
        conversations.remove(c);
        conversations.add(0, c);
    }

    public void cancelTop(List<Conversation> conversations, Conversation conversation) {
        int size = conversations.size();
        int index = 0;
        for (int i = 0; i < size; i++) {
            Conversation c = conversations.get(i);
            // 找到第一个非置顶并且更新时间小于或等于该会话的会话
            if (Constant.value_no.equals(c.getIsTop()) && c.getUpdateTime() < conversation.getUpdateTime()) {
                index = i;
                break;
            }
            // 如果该消息的更新时间是最小的，则加入最后
            if (i == size - 1) {
                index = i;
            }
        }
        conversation.setIsTop(Constant.value_no);
        conversation.setTopTime(null);// 置空利于查询排序
        conversations.remove(conversation);
        if (index > 0) {
            conversations.add(index, conversation);
        } else {
            conversations.add(0, conversation);
        }
    }

    public void cancelTop(List<Conversation> conversations, String conversationId) {
        int size = conversations.size();
        int index = 0;
        Conversation temp = null;
        for (int i = 0; i < size; i++) {
            Conversation c = conversations.get(i);
            if (temp == null) {
                if (c.getConversationId().equals(conversationId)) {
                    temp = c;
                }
                //如果取消置顶的记录刚好是最后一条
                if (i == size - 1) {
                    index = i;
                }
                continue;
            }
            // 找到第一个非置顶并且更新时间小于或等于该会话的会话
            if (Constant.value_no.equals(c.getIsTop()) && c.getUpdateTime() < temp.getUpdateTime()) {
                index = i;
                break;
            }
            // 如果该消息的更新时间是最小的，则加入最后
            if (i == size - 1) {
                index = i;
            }
        }
        temp.setIsTop(Constant.value_no);
        temp.setTopTime(null);// 置空利于查询排序
        conversations.remove(temp);
        if (index > 0) {
            conversations.add(index, temp);
        } else {
            conversations.add(0, temp);
        }
    }

    public void top(List<MGroup> mGroups, MGroup mGroup) {
        mGroup.setIsTop(Constant.value_yes);
        mGroup.setTopTime(System.currentTimeMillis());
        mGroups.remove(mGroup);
        mGroups.add(0, mGroup);
    }

    public void cancelTop(List<MGroup> mGroups, MGroup mGroup) {
//		int size = mGroups.size();
//		int index = 0;
//		for (int i = 0; i < size; i++) {
//			MGroup c = mGroups.get(i);
//			// 找到第一个非置顶并且更新时间小于或等于该会话的会话
//			if (Constant.value_no.equals(c.getIsTop()) && c.getUpdateTime() < mGroup.getUpdateTime()) {
//				index = i;
//				break;
//			}
//			// 如果该记录的更新时间是最小的，则加入最后
//			if (i == size - 1) {
//				index = i;
//			}
//		}
//		mGroup.setIsTop(Constant.value_no);
//		mGroup.setTopTime(null);// 置空利于查询排序
//		mGroups.remove(mGroup);
//		if (index > 0) {
//			mGroups.add(index, mGroup);
//		} else {
//			mGroups.add(0, mGroup);
//		}

        mGroup.setIsTop(Constant.value_no);
        mGroup.setTopTime(null);// 置空利于查询排序
        GroupUtils.sortGroups(mGroups);
    }
}
