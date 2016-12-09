package com.lailem.app.tpl.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter.ImageBean;
import com.lailem.app.api.ApiClient;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.model.msg.MsgPic;
import com.lailem.app.chat.util.Constant;
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

public class MessagePicTpl extends BaseTpl<Message> {
    @Bind(R.id.contentIv)
    ImageView contentIv;
    @Bind(R.id.nicknameTv)
    TextView nicknameTv;
    @Bind(R.id.headIv)
    ImageView headIv;
    @Bind(R.id.send_fail)
    ImageView sendFailIv;
    Message bean;

    public MessagePicTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        if (ChatActivity.TPL_LEFT_PIC == itemViewType) {
            return R.layout.item_chat_left_pic;
        } else {
            return R.layout.item_chat_right_pic;
        }
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        MsgPic msgObj = bean.getMsgObj();
        if (TextUtils.isEmpty(msgObj.getLocalPath())) {
            if (Func.checkImageTag(msgObj.gettPic(), contentIv)) {
                AppContext.getInstance().imageLoader.displayImage(ApiClient.getFileUrl(msgObj.gettPic()), contentIv);
            }
        } else {
            if (Func.checkImageTag(msgObj.getLocalPath(), contentIv)) {
                AppContext.getInstance().imageLoader.displayImage(StringUtils.getUri(msgObj.getLocalPath()), contentIv);
            }
        }

        User user = UserCache.getInstance(_activity).get(bean.getFId());
        if (user != null) {
            if (Func.checkImageTag(user.getHead(), headIv)) {
                Glide.with(_activity).load(StringUtils.getUri(user.getHead())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(headIv);
            }
            nicknameTv.setText(Func.formatNickName(_activity, bean.getFId(), user.getNickname()));
        }

        if (itemViewType == ChatActivity.TPL_RIGHT_PIC) {
            if (Constant.status_sendFail.equals(bean.getStatus()) || Constant.status_uploadFail.equals(bean.getStatus())) {
                sendFailIv.setVisibility(View.VISIBLE);
            } else {
                sendFailIv.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onItemClick() {
        super.onItemClick();

    }

    @OnClick(R.id.contentIv)
    public void clickPic() {
        int startIndex = 0;
        ArrayList<ImageBean> beans = new ArrayList<ImageBean>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            Message message = data.get(i);
            if (Constant.sType_pic.equals(message.getSType())) {

                MsgPic msgPic = message.getMsgObj();
                String url = null;
                String thumb = null;
                if (TextUtils.isEmpty(msgPic.getLocalPath())) {
                    url = StringUtils.getUri(msgPic.getPic());
                    thumb = StringUtils.getUri(msgPic.gettPic());
                } else {
                    url = StringUtils.getUri(msgPic.getLocalPath());
                }
                ImageBean imageBean = new ImageBean(url, thumb);
                beans.add(imageBean);
                if (message.getId() == bean.getId()) {
                    startIndex = beans.size() - 1;
                }
            }
        }
        UIHelper.showImages(_activity, beans, startIndex);
    }

    @OnClick(R.id.send_fail)
    public void clickSendFail() {
        sendFailIv.setVisibility(View.GONE);
        bean.reSend();
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

    @OnLongClick(R.id.contentIv)
    public boolean longClickPic() {
        showDialog();
        return false;
    }

    @OnClick({R.id.nicknameTv, R.id.headIv})
    public void clickAvatarOrName() {
        UIHelper.showMemberInfoAlone(_activity, bean.getFId());
    }
}
