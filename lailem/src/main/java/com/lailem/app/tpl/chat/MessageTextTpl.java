package com.lailem.app.tpl.chat;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.model.msg.MsgText;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.User;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.chat.ChatActivity;
import com.lailem.app.utils.ClipboardUtil;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MessageTextTpl extends BaseTpl<Message> implements OnClickListener, OnLongClickListener {
    @Bind(R.id.nicknameTv)
    TextView nicknameTv;
    @Bind(R.id.textTv)
    TextView textTv;
    @Bind(R.id.headIv)
    ImageView headIv;
    @Bind(R.id.send_fail)
    ImageView sendFailIv;
    Message bean;

    public MessageTextTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        if (ChatActivity.TPL_LEFT_TEXT == itemViewType) {
            return R.layout.item_chat_left_text;
        } else {
            return R.layout.item_chat_right_text;
        }
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        MsgText msgText = bean.getMsgObj();
        User user = UserCache.getInstance(_activity).get(bean.getFId());
        if (user != null) {
            if (Func.checkImageTag(user.getHead(), headIv)) {
                Glide.with(_activity).load(StringUtils.getUri(user.getHead())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(headIv);
            }
            nicknameTv.setText(Func.formatNickName(_activity, bean.getFId(), user.getNickname()));
        }

        if (itemViewType == ChatActivity.TPL_RIGHT_TEXT) {
            if (Constant.status_sendFail.equals(bean.getStatus())) {
                sendFailIv.setVisibility(View.VISIBLE);
                sendFailIv.setOnClickListener(this);
            } else {
                sendFailIv.setVisibility(View.GONE);
            }
        }
        textTv.setText(msgText.getExpressionText());
        textTv.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_fail:// 发送失败的图标
                sendFailIv.setVisibility(View.GONE);
                bean.reSend();
                break;
        }

    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.textTv:
                showDialog();
                break;

        }
        return false;
    }

    private void showDialog() {
        ActionDialog dialog = new ActionDialog(_activity);
        ActionData actionData1 = new ActionData("删除", R.drawable.ic_delete_selector);
        ActionData actionData2 = new ActionData("复制", R.drawable.ic_look_material_selector);
        ArrayList<ActionData> actionDatas = new ArrayList<ActionData>();
        actionDatas.add(actionData1);
        actionDatas.add(actionData2);
        DialogActionData dialogActionData = new DialogActionData(null, null, actionDatas);
        dialog.init(dialogActionData);
        dialog.setOnActionClickListener(new OnActionClickListener() {

            @Override
            public void onActionClick(ActionDialog dialog, View View, int position) {
                switch (position) {
                    case 0:
                        DaoOperate.getInstance(_activity).delete(bean);
                        data.remove(bean);
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        MsgText msgObj = bean.getMsgObj();
                        ClipboardUtil.copyText(_activity, msgObj.getText());
                        AppContext.showToast("复制成功");
                        break;
                }

            }
        });
        dialog.show();
    }

    @OnClick({R.id.nicknameTv, R.id.headIv})
    public void clickAvatarOrName() {
        UIHelper.showMemberInfoAlone(_activity, bean.getFId());
    }
}
