package com.lailem.app.dao;

import android.text.SpannableString;

import com.lailem.app.bean.Base;
import com.lailem.app.ui.chat.expression.ExpressionUtil;

public class ConversationBase extends Base{
	private SpannableString expressionTipMsg;

	public SpannableString getExpressionTipMsg() {
		if(expressionTipMsg==null){
			expressionTipMsg = ExpressionUtil.getInstace().getExpressionString(((Conversation)this).getTipMsg());
		}
		return expressionTipMsg;
	}

	public void setExpressionTipMsg(SpannableString expressionTipMsg) {
		this.expressionTipMsg = expressionTipMsg;
	}

}
