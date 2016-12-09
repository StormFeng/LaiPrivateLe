package com.lailem.app.chat.handler.other;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgGUpdate;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.GroupUtils;
import com.socks.library.KLog;

public class GUpdateMessageHandler extends MessageHandler {

	public GUpdateMessageHandler(Message message, Context context) {
		super(message, context);
        KLog.i("111111111111111111111111");
		MsgGUpdate msgObj = message.getMsgObj();
		if(msgObj!=null){//更新群资料
			KLog.i("2222");
			Group group = DaoOperate.getInstance(context).queryGroup(msgObj.getgId());
			if(group!=null){
				KLog.i("333");
				if(Constant.gType_activity.equals(group.getGroupType())){
					GroupUtils.updateActivity(context, msgObj.getgId(), msgObj.getgName(), msgObj.getgIntro(), msgObj.getgSSPic(),null);
				}else if(Constant.gType_group.equals(group.getGroupType())){
					GroupUtils.updateGroup(context, msgObj.getgId(), msgObj.getgName(), msgObj.getgIntro(), msgObj.getgSSPic(),null);
				}
			}
		}
	}

}
