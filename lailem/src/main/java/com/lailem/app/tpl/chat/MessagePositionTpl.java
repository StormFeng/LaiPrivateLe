package com.lailem.app.tpl.chat;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.model.msg.MsgPosition;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.User;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.ui.chat.ChatActivity;
import com.lailem.app.utils.Const;
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
import butterknife.OnLongClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MessagePositionTpl extends BaseTpl<Message> {
    @Bind(R.id.nicknameTv)
    TextView nicknameTv;
    @Bind(R.id.headIv)
    ImageView headIv;
    @Bind(R.id.contentIv)
    ImageView contentIv;
    @Bind(R.id.address)
    TextView addressTextView;
    Message bean;
    private MsgPosition msgObj;

    public MessagePositionTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        if (ChatActivity.TPL_LEFT_POSITION == itemViewType) {
            return R.layout.item_chat_left_position;
        } else {
            return R.layout.item_chat_right_position;
        }
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        this.msgObj = bean.getMsgObj();
        addressTextView.setText(msgObj.getAddr());

        User user = UserCache.getInstance(_activity).get(bean.getFId());
        if (user != null) {
            if (Func.checkImageTag(user.getHead(), headIv)) {
                Glide.with(_activity).load(StringUtils.getUri(user.getHead())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(headIv);
            }
            nicknameTv.setText(Func.formatNickName(_activity, bean.getFId(), user.getNickname()));
        }
        String url = Const.BAIDU_STATIC_IMAGE_2.replaceAll("@@", msgObj.getLon() + "," + msgObj.getLat());
        if (Func.checkImageTag(url, contentIv)) {
            Glide.with(_activity).load(url).placeholder(R.drawable.empty).error(R.drawable.empty).into(contentIv);
        }
    }

    @OnLongClick(R.id.contentIv)
    public boolean longClickContentIv() {
        showDialog();
        return false;
    }

    @OnClick(R.id.contentIv)
    public void clickContentIv() {
        UIHelper.showActiveLoc(_activity, msgObj.getLat(), msgObj.getLon(), msgObj.getAddr(), ActiveLocActivity.TYPE_OTHER);
    }

    private void showDialog() {
        ActionDialog dialog = new ActionDialog(_activity);
        ActionData actionData1 = new ActionData("删除", R.drawable.ic_delete_selector);
        ArrayList<ActionData> actionDatas = new ArrayList<ActionData>();
        actionDatas.add(actionData1);
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
