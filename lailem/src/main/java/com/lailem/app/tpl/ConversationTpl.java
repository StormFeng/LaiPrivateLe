package com.lailem.app.tpl;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.MessageCountManager;
import com.lailem.app.chat.util.MessageFileManger;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.User;
import com.lailem.app.jni.JniSharedLibraryWrapper;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.SetTopUtil;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.TimeUtil;
import com.lailem.app.utils.TimeUtilForConversation;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ConversationTpl extends BaseTpl<Conversation> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.title)
    TextView title_tv;
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.date)
    TextView date_tv;
    @Bind(R.id.count)
    TextView count_tv;
    int noTipWidth = (int) TDevice.dpToPixel(20f);

    Conversation bean;
    static final String notificationPublisherId = JniSharedLibraryWrapper.notificationPublisherId();

    public ConversationTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_main_conversation;
    }

    @Override
    public void setBean(Conversation bean, int position) {
        this.bean = bean;

        if (Constant.cType_sc.equals(bean.getCType()) || Constant.cType_p.equals(bean.getCType())) {
            title_tv.setTextColor(Color.parseColor("#FF202020"));
            User user = UserCache.getInstance(_activity).get(bean.getTId());
            KLog.i("user:::" + user);
            if (user != null) {
                if (Func.checkImageTag(StringUtils.getUri(user.getHead()), avatar_iv)) {
                    Glide.with(_activity).load(StringUtils.getUri(user.getHead())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
                }
                title_tv.setText(Func.formatNickName(_activity, bean.getTId(), user.getNickname()));
            } else {
                avatar_iv.setImageResource(R.drawable.ic_empty_circle);
                title_tv.setText("");
            }
        } else if (Constant.cType_gc.equals(bean.getCType())) {
            title_tv.setTextColor(Color.parseColor("#FF754D4D"));
            Group group = GroupCache.getInstance(_activity).get(bean.getTId());
            if (group != null) {
                if (Func.checkImageTag(StringUtils.getUri(group.getSquareSPic()), avatar_iv)) {
                    Glide.with(_activity).load(StringUtils.getUri(group.getSquareSPic())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
                }
                title_tv.setText(group.getName());
            } else {
                avatar_iv.setImageResource(R.drawable.ic_empty_circle);
                title_tv.setText("");
            }
        }
        // TODO 很奇怪，使用了表情，无法更新视图
//         content_tv.setText(bean.getExpressionTipMsg());
        content_tv.setText(bean.getTipMsg());
        if (bean.getUpdateTime() != null)
            date_tv.setText(TimeUtilForConversation.getInstance().getTime(bean.getUpdateTime()));

        int noReadCount = bean.getNoReadCount();

        if (Constant.value_yes.equals(bean.getIsNoTip())) {//消息免打扰
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) count_tv.getLayoutParams();
            params.width = noTipWidth;
            params.height = noTipWidth;
            count_tv.setLayoutParams(params);
            count_tv.setText("");
            count_tv.setVisibility(View.VISIBLE);
            if (noReadCount > 0) {
                count_tv.setBackgroundResource(R.drawable.ic_no_tip_dot);
            } else {
                count_tv.setBackgroundResource(R.drawable.ic_no_tip);
            }

        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) count_tv.getLayoutParams();
            params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            count_tv.setLayoutParams(params);
            if (0 == noReadCount) {
                count_tv.setVisibility(View.GONE);
            } else if (noReadCount <= 99 && noReadCount > 0) {
                count_tv.setText(noReadCount + "");
                count_tv.setVisibility(View.VISIBLE);
            } else if (noReadCount > 99) {
                count_tv.setText("99+");
                count_tv.setVisibility(View.VISIBLE);
            } else {
                count_tv.setVisibility(View.GONE);
            }
        }

        if (Constant.value_yes.equals(bean.getIsTop())) {
            getChildAt(0).setBackgroundResource(R.color.item_bg_for_conversation_top);
        } else {
            getChildAt(0).setBackgroundResource(R.drawable.c_item_selector);
        }

    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        if (notificationPublisherId.equals(bean.getTId())) {
            UIHelper.showNotificationList(_activity, bean.getTId(), bean.getConversationId(), bean.getCType());
        } else {
            UIHelper.showChat(_activity, bean.getTId(), bean.getConversationId(), bean.getCType());
        }

        if (bean.getNoReadCount() > 0) {
            updateNoReadCount();
        }

        // Intent intent = new Intent(_activity, MediaRecorderActivity.class);
        // _activity.startActivity(intent);

    }

    @Override
    protected void onItemLongClick() {
        super.onItemLongClick();
        showDialog();
    }

    private void showDialog() {
        ActionDialog dialog = new ActionDialog(_activity);
        ActionData actionData1 = new ActionData("删除", R.drawable.ic_delete_selector);
        ActionData actionData2 = null;
        if (Constant.value_yes.equals(bean.getIsTop())) {
            actionData2 = new ActionData("取消置顶", R.drawable.ic_set_top_selector);
        } else {
            actionData2 = new ActionData("置顶", R.drawable.ic_set_top_selector);
        }
        ActionData actionData3 = null;
        if (bean.getNoReadCount() > 0) {
            actionData3 = new ActionData("标为已读", R.drawable.ic_readed_selector);
        } else {
            actionData3 = new ActionData("标为未读", R.drawable.ic_readed_selector);
        }
        ArrayList<ActionData> actionDatas = new ArrayList<ActionData>();
        actionDatas.add(actionData1);
        actionDatas.add(actionData2);
        actionDatas.add(actionData3);
        DialogActionData dialogActionData = new DialogActionData(null, null, actionDatas);
        dialog.init(dialogActionData);
        dialog.setOnActionClickListener(new OnActionClickListener() {

            @Override
            public void onActionClick(ActionDialog dialog, View View, int position) {
                switch (position) {
                    case 0:// 删除
                        data.remove(bean);
                        adapter.notifyDataSetChanged();
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // 删除消息
                                List<Message> messages = DaoOperate.getInstance(_activity).queryMessages(bean.getConversationId());
                                DaoOperate.getInstance(_activity).deleteInTxMessage(messages);
                                // 删除会话
                                DaoOperate.getInstance(_activity).delete(bean);
                                //删除消息文件
                                MessageFileManger.getInstance().delete(messages);
                            }
                        }).start();

                        break;
                    case 1:// 置顶
                        if (Constant.value_yes.equals(bean.getIsTop())) {
                            // 取消置顶
                            SetTopUtil.getInstance().cancelTop(data, bean);
                        } else {
                            // 置顶
                            SetTopUtil.getInstance().top(data, bean);
                        }
                        adapter.notifyDataSetChanged();
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                DaoOperate.getInstance(_activity).update(bean);
                            }
                        }).start();
                        break;
                    case 2:// 标为
                        if (bean.getNoReadCount() > 0) {
                            bean.setNoReadCount(0);
                        } else {
                            bean.setNoReadCount(1);
                        }
                        adapter.notifyDataSetChanged();
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                DaoOperate.getInstance(_activity).update(bean);
                                if (bean.getNoReadCount() == 0) {
                                    List<Message> noReadMessages = DaoOperate.getInstance(_activity).queryMessagesNoRead(bean.getConversationId());
                                    if (noReadMessages.size() > 0) {
                                        for (Message message : noReadMessages) {
                                            message.setIsReadOne(Constant.value_yes);
                                        }
                                        DaoOperate.getInstance(_activity).updateInTx(noReadMessages);
                                    }
                                }

                            }
                        }).start();
                        break;

                }

            }
        });
        dialog.show();
    }

    private void updateNoReadCount() {
        count_tv.setText("");
        count_tv.setVisibility(View.GONE);
        MessageCountManager.getInstance().reduce(MessageCountManager.KEY_NO_READ_COUNT_FOR_CHAT, bean.getNoReadCount());
        new Thread(new Runnable() {

            @Override
            public void run() {
                bean.setNoReadCount(0);
                DaoOperate.getInstance(_activity).update(bean);
                List<Message> noReadMessages = DaoOperate.getInstance(_activity).queryMessagesNoRead(bean.getConversationId());
                for (Message message : noReadMessages) {
                    message.setIsRead(Constant.value_yes);
                }
                DaoOperate.getInstance(_activity).updateInTx(noReadMessages);
            }
        }).start();

    }
}
