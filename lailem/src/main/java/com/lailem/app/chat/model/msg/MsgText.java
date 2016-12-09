package com.lailem.app.chat.model.msg;

import android.text.SpannableString;
import android.text.TextUtils;

import com.lailem.app.ui.chat.expression.ExpressionUtil;
import com.socks.library.KLog;

public class MsgText extends Msg {
	private String text;
	private SpannableString expressionText;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public SpannableString getExpressionText() {
        KLog.i("text:::" + text);
		if(expressionText==null&&!TextUtils.isEmpty(text))
			expressionText = ExpressionUtil.getInstace().getExpressionString(text);
		return expressionText;
	}

	public void setExpressionText(SpannableString expressionText) {
		this.expressionText = expressionText;
	}

}
