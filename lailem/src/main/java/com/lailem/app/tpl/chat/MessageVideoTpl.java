package com.lailem.app.tpl.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.model.msg.MsgVideo;
import com.lailem.app.chat.util.MessageFileManger;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.User;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.chat.ChatActivity;
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

public class MessageVideoTpl extends BaseTpl<Message> {
    @Bind(R.id.video)
    ImageView videoIv;
    @Bind(R.id.nicknameTv)
    TextView nicknameTv;
    @Bind(R.id.headIv)
    ImageView headIv;
    Message bean;

    public MessageVideoTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        if (ChatActivity.TPL_LEFT_VIDEO == itemViewType) {
            return R.layout.item_chat_left_video;
        } else {
            return R.layout.item_chat_right_video;
        }
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        MsgVideo msgVideo = bean.getMsgObj();
        User user = UserCache.getInstance(_activity).get(bean.getFId());
        if (user != null) {
            if (Func.checkImageTag(user.getHead(), headIv)) {
                Glide.with(_activity).load(StringUtils.getUri(user.getHead())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(headIv);
            }
            nicknameTv.setText(Func.formatNickName(_activity, bean.getFId(), user.getNickname()));
        }
        if (TextUtils.isEmpty(msgVideo.getLocalPicPath())) {
            if (Func.checkImageTag(msgVideo.getPic(), videoIv)) {
                Glide.with(_activity).load(StringUtils.getUri(msgVideo.getPic())).into(videoIv);
            }
        } else {
            if (Func.checkImageTag(msgVideo.getLocalPicPath(), videoIv)) {
                Glide.with(_activity).load(msgVideo.getLocalPicPath()).into(videoIv);
            }
        }

    }

    @OnClick(R.id.video_fl)
    public void clickVideo() {
        MsgVideo msgVideo = bean.getMsgObj();
        if (TextUtils.isEmpty(msgVideo.getLocalPath())) {
            UIHelper.showPlayVideo(_activity, StringUtils.getUri(msgVideo.getVideo()), StringUtils.getUri(msgVideo.getPic()));
        } else {
            UIHelper.showPlayVideo(_activity, msgVideo.getLocalPath(), msgVideo.getLocalPicPath());
        }
    }


    @OnLongClick(R.id.video_fl)
    public boolean longVideoFL() {
        showDialog();
        return false;
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
                        MessageFileManger.getInstance().delete(bean);
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
