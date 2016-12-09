package com.lailem.app.tpl.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.model.msg.MsgVoice;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.MessageFileManger;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.User;
import com.lailem.app.listener.OnAudioPlayListener;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.chat.ChatActivity;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.socks.library.KLog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnLongClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MessageVoiceTpl extends BaseTpl<Message> implements OnAudioPlayListener {
    Message bean;
    MsgVoice msg;
    int position;
    Timer timer;
    @Bind(R.id.voiceIv)
    ImageView voiceIv;
    @Bind(R.id.timeTv)
    TextView timeTv;
    @Bind(R.id.redDotIv)
    ImageView redDotIv;
    @Bind(R.id.nicknameTv)
    TextView nicknameTv;
    @Bind(R.id.headIv)
    ImageView headIv;
    ChatActivity activity;
    @Bind(R.id.send_fail)
    ImageView sendFailIv;
    @Bind(R.id.voiceLL)
    LinearLayout voiceLL;

    public MessageVoiceTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        if (ChatActivity.TPL_LEFT_VOICE == itemViewType) {
            return R.layout.item_chat_left_voice;
        } else {
            return R.layout.item_chat_right_voice;
        }
    }

    @Override
    public void init(Context context, int itemViewType) {
        super.init(context, itemViewType);
        activity = (ChatActivity) _activity;
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        this.position = position;
        msg = bean.getMsgObj();
        timeTv.setText(msg.getD() + "'");
        if (Constant.value_yes.equals(bean.getIsReadOne())) {
            redDotIv.setVisibility(View.INVISIBLE);
        } else {
            redDotIv.setVisibility(View.VISIBLE);
        }

        User user = UserCache.getInstance(_activity).get(bean.getFId());
        if (user != null) {
            if (Func.checkImageTag(user.getHead(), headIv)) {
                Glide.with(_activity).load(StringUtils.getUri(user.getHead())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(headIv);
            }
            nicknameTv.setText(Func.formatNickName(_activity, bean.getFId(), user.getNickname()));
        }

        if (itemViewType == ChatActivity.TPL_RIGHT_VOICE) {
            if (Constant.status_sendFail.equals(bean.getStatus()) || Constant.status_uploadFail.equals(bean.getStatus())) {
                sendFailIv.setVisibility(View.VISIBLE);
            } else {
                sendFailIv.setVisibility(View.GONE);
            }
        }

    }

    boolean isPlaying;

    @OnClick(R.id.voiceLL)
    protected void clickVoiceLL() {
        if (isPlaying) {
            stopPlay(true);
        } else {
            startPlay();
        }

    }

    @OnLongClick(R.id.voiceLL)
    public boolean longClickVoiceLL() {
        showDialog();
        return false;
    }

    public void startPlay() {
        // 声音未读
        if (Constant.value_no.equals(this.bean.getIsReadOne())) {
            updateIsReadOne();
            needAutoPlayNext = true;
        } else {
            needAutoPlayNext = false;
        }
        dealOtherPlayer();
        isPlaying = true;
        MsgVoice msgVoice = this.bean.getMsgObj();
        if (TextUtils.isEmpty(msgVoice.getLocalPath())) {
            KLog.i("net path:::" + ApiClient.getFileUrl(msgVoice.getVoice()));
            AudioPlayUtil.getInstance().start(ApiClient.getFileUrl(msgVoice.getVoice()), position + "", this);
        } else {
            AudioPlayUtil.getInstance().start(msgVoice.getLocalPath(), position + "", this);
        }
        timer = new Timer();
        if (ChatActivity.TPL_LEFT_VOICE == itemViewType) {
            timer.schedule(new TimerTask() {

                int curResId = R.drawable.ic_voice_left4;

                @Override
                public void run() {
                    _activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            voiceIv.setBackgroundResource(curResId);
                        }
                    });
                    switch (curResId) {
                        case R.drawable.ic_voice_left1:
                            curResId = R.drawable.ic_voice_left4;
                            break;
                        case R.drawable.ic_voice_left2:
                            curResId = R.drawable.ic_voice_left1;
                            break;
                        case R.drawable.ic_voice_left3:
                            curResId = R.drawable.ic_voice_left2;
                            break;
                        case R.drawable.ic_voice_left4:
                            curResId = R.drawable.ic_voice_left3;
                            break;
                    }

                }
            }, 0, 350);
        } else {
            timer.schedule(new TimerTask() {

                int curResId = R.drawable.ic_voice_right4;

                @Override
                public void run() {
                    _activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            voiceIv.setBackgroundResource(curResId);
                        }
                    });
                    switch (curResId) {
                        case R.drawable.ic_voice_right1:
                            curResId = R.drawable.ic_voice_right4;
                            break;
                        case R.drawable.ic_voice_right2:
                            curResId = R.drawable.ic_voice_right1;
                            break;
                        case R.drawable.ic_voice_right3:
                            curResId = R.drawable.ic_voice_right2;
                            break;
                        case R.drawable.ic_voice_right4:
                            curResId = R.drawable.ic_voice_right3;
                            break;
                    }

                }
            }, 0, 350);
        }

    }

    public void stopPlay(boolean needManualStop) {
        isPlaying = false;
        // 停步播放时，将正在播放置为默认值
        activity.positionForPlayingVoice = activity.DEFAULT_POSITION;
        timer.cancel();
        if (ChatActivity.TPL_LEFT_VOICE == itemViewType) {
            voiceIv.setBackgroundResource(R.drawable.ic_voice_left1);
        } else {
            voiceIv.setBackgroundResource(R.drawable.ic_voice_right1);
        }
        if (needManualStop) {
            AudioPlayUtil.getInstance().stop();
        }
        if (needAutoPlayNext) {// 需处理自动播放下一条
            dealAutoPlayNext();
        }
    }

    /**
     * 处理其它的播放
     */
    private void dealOtherPlayer() {
        // 开始播放前，检查有没有其它正在播放的
        if (activity.positionForPlayingVoice != activity.DEFAULT_POSITION) {
            // 有正在播放的将其停掉
            KLog.i("playing position:::" + activity.positionForPlayingVoice);
            View view = absListView.getChildAt(activity.positionForPlayingVoice - absListView.getFirstVisiblePosition());
            try {
                KLog.i("className:::" + MessageVoiceTpl.class.getName());
                Method method = Class.forName(MessageVoiceTpl.class.getName()).getMethod("stopPlay", boolean.class);
                KLog.i("view:::" + view);
                method.invoke(view, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        activity.positionForPlayingVoice = position;

    }

    boolean needAutoPlayNext;

    /**
     * 处理自动播放下一个
     */
    private void dealAutoPlayNext() {
        KLog.i("自动播放下一个");
        int size = data.size();
        if (position < size - 1) {// 还有下一个
            int positionX = position + 1;
            KLog.i("还有下一个");
            for (int i = positionX; i < size; i++) {
                KLog.i("i:::" + i);
                KLog.i("message.getItemViewType():::" + this.bean.getItemViewType());
                KLog.i("message.getIsReadOne()::::" + this.bean.getIsReadOne());

                if (ChatActivity.TPL_LEFT_VOICE == data.get(i).getItemViewType() && Constant.value_no.equals(data.get(i).getIsReadOne())) {
                    if (i > absListView.getLastVisiblePosition()) {//如果下一个播放的位置大于最后一个可见的位置则滚动
                        absListView.setSelection(i);
                    }
                    View view = absListView.getChildAt(i - absListView.getFirstVisiblePosition());
                    try {
                        KLog.i("找到下一个");
                        Method method = Class.forName(MessageVoiceTpl.class.getName()).getMethod("startPlay");
                        method.invoke(view);
                        KLog.i("执行完下一个");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }

        }
    }

    @Override
    public void onAudioPlayEnd(String tag, String path) {
        KLog.i("播放完成");
        stopPlay(false);
    }

    @Override
    public void onAudioException() {
        stopPlay(false);
        AppContext.showToast("播放失败");
    }

    /**
     * 更新语音已读状态
     */
    private void updateIsReadOne() {
        redDotIv.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {

            @Override
            public void run() {
                bean.setIsReadOne(Constant.value_yes);
                DaoOperate.getInstance(_activity).update(bean);
            }
        }).start();
    }

    @OnClick(R.id.send_fail)
    public void clickSendFail(View view) {
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

    @OnClick({R.id.nicknameTv, R.id.headIv})
    public void clickAvatarOrName() {
        UIHelper.showMemberInfoAlone(_activity, bean.getFId());
    }

}
