package com.lailem.app.tpl.chat;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.chat.model.msg.MsgTip;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Message;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.TimeUtil;

public class MessageTipTpl extends BaseTpl<Message> {

	public MessageTipTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_chat_tip;
	}

	@Override
	public void setBean(Message bean, int position) {
		TextView contentTv = (TextView) findViewById(R.id.contentTv);
		if (Constant.sType_time.equals(bean.getSType())) {// 时间
			contentTv.setText(TimeUtil.getInstance().getTime(bean.getSTime()));
		}else if(Constant.sType_tip.equals(bean.getSType())){//提示消息
			MsgTip msgObj = bean.getMsgObj();
			contentTv.setText(msgObj.getTip());
		}
	}

}
