package com.lailem.app.tpl.notification;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallback;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.model.inmsg.UInfo;
import com.lailem.app.chat.model.msg.MsgGApply;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.listener.OnAudioPlayListener;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TimeUtil;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.socks.library.KLog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class NotificationApplyJoinGroupVoiceTpl extends BaseTpl<Message> {
    public static final String PLAYING_VIEW = "playingView";

    @Bind(R.id.state)
    TextView stateTV;
    @Bind(R.id.title)
    TextView titleTV;
    @Bind(R.id.voice)
    ImageView voiceIV;
    @Bind(R.id.duration)
    TextView durationTV;
    @Bind(R.id.reddot)
    ImageView reddotIV;
    @Bind(R.id.ok)
    TextView okTV;
    @Bind(R.id.no)
    TextView noTV;
    @Bind(R.id.group_icon)
    ImageView groupIconIV;
    @Bind(R.id.group_name)
    TextView groupNameTV;
    @Bind(R.id.head)
    ImageView headIV;
    @Bind(R.id.name)
    TextView nameTV;
    @Bind(R.id.date)
    TextView dateTV;
    @Bind(R.id.right_bottom)
    TextView rightBottomTV;
    Message bean;
    private int position;

    public NotificationApplyJoinGroupVoiceTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_notification_apply_join_group_voice;
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        this.position = position;
        // 读状态
        if (Constant.value_no.equals(bean.getIsReadOne())) {
            stateTV.setVisibility(View.VISIBLE);
            reddotIV.setVisibility(View.VISIBLE);
        } else {
            stateTV.setVisibility(View.GONE);
            reddotIV.setVisibility(View.INVISIBLE);
        }

        MsgGApply msgObj = bean.getMsgObj();
        if (!TextUtils.isEmpty(msgObj.getDur())) {
            durationTV.setText(msgObj.getDur() + "'");
        } else {
            durationTV.setText("");
        }
        if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
            titleTV.setText(msgObj.getuInfo().getNick() + "申请加入群组");
        } else {
            titleTV.setText(msgObj.getuInfo().getNick() + "申请加入活动");
        }
        Group group = GroupCache.getInstance(_activity).get(msgObj.getgInfo().getId());
        if (group != null) {
            if (Func.checkImageTag(group.getSquareSPic(), groupIconIV)) {
                Glide.with(_activity).load(StringUtils.getUri(group.getSquareSPic())).placeholder(R.drawable.empty).error(R.drawable.empty).into(groupIconIV);
            }
            groupNameTV.setText(group.getName());
        } else {
            groupIconIV.setImageResource(R.drawable.empty);
            groupNameTV.setText("");
        }
        if (Func.checkImageTag(msgObj.getuInfo().getsHead(), headIV)) {
            Glide.with(_activity).load(StringUtils.getUri(msgObj.getuInfo().getsHead())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(headIV);
        }
        nameTV.setText(msgObj.getuInfo().getNick());
        dateTV.setText(TimeUtil.getInstance().getTime(bean.getSTime()));
        rightBottomTV.setText("");
        String status = bean.getStatus();
        if (Const.groupApplyVerifyStatus_agree.equals(status)) {
            okTV.setText("已同意");
            okTV.setEnabled(false);
            noTV.setEnabled(false);
        } else if (Const.groupApplyVerifyStatus_igone.equals(status)) {
            noTV.setText("已忽略");
            okTV.setEnabled(false);
            noTV.setEnabled(false);
        } else {
            okTV.setText("同意");
            noTV.setText("忽略");
            okTV.setEnabled(true);
            noTV.setEnabled(true);
        }

        //判断执行播放动画

        if (adapter.getCheckedPosition() == this.position) {
            startPalyAnim(voiceIV);
        } else {
            stopPalyAnim(voiceIV);
        }
    }

    private void startPalyAnim(ImageView voiceIV) {
        AnimationDrawable drawable = (AnimationDrawable) voiceIV.getDrawable();
        if (!drawable.isRunning()) {
            drawable.start();
        }
    }

    private void stopPalyAnim(ImageView voiceIV) {
        AnimationDrawable drawable = (AnimationDrawable) voiceIV.getDrawable();
        if (drawable.isRunning()) {
            drawable.stop();
        }
        drawable.selectDrawable(0);
    }


    /**
     * 点击同意
     */
    @OnClick(R.id.ok)
    public void clickOk() {
        okTV.setText("已同意");
        okTV.setEnabled(false);
        noTV.setEnabled(false);
        bean.setStatus(Const.groupApplyVerifyStatus_agree);
        DaoOperate.getInstance(_activity).update(bean);
        verify(Const.groupApplyVerifyStatus_agree);
        updateIsReadOne();

    }

    /**
     * 点击忽略
     */
    @OnClick(R.id.no)
    public void clickNo() {
        noTV.setText("已忽略");
        okTV.setEnabled(false);
        noTV.setEnabled(false);
        bean.setStatus(Const.groupApplyVerifyStatus_igone);
        DaoOperate.getInstance(_activity).update(bean);
        verify(Const.groupApplyVerifyStatus_igone);
        updateIsReadOne();
    }

    private void verify(String verifyStatus) {
        MsgGApply msgGApply = (MsgGApply) bean.getMsgObj();
        ApiClient.getApi().applyVerify(new ApiCallback() {
            @Override
            public void onParseError(String tag) {
                KLog.i("onParseError:::" + tag);
            }

            @Override
            public void onApiSuccess(Result res, String tag) {
                KLog.i("验证结果:::ret:::" + res.ret + ",errorCode:::" + res.errorCode + ",errorInfo:::" + res.errorInfo);
            }

            @Override
            public void onApiStart(String tag) {
            }

            @Override
            public void onApiLoading(long count, long current, String tag) {
            }

            @Override
            public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
                KLog.i("onApiFailure:::t::" + t.getMessage() + ",errorNo:::" + errorNo + ",strMsg:::" + strMsg + ",tag:::" + tag);
            }
        }, AppContext.getInstance().getLoginUid(), msgGApply.getId(), verifyStatus);

    }

    /**
     * 点击播放语音
     */
    @OnClick(R.id.voiceLL)
    public void clickVoice() {
        MsgGApply msgGApply = (MsgGApply) bean.getMsgObj();
        if (adapter.getCheckedPosition() == position && voiceIV == adapter.getTag(PLAYING_VIEW)) {
            AudioPlayUtil.getInstance().stop();
            stopPalyAnim(voiceIV);
        } else {
            if (adapter.getTag(PLAYING_VIEW) != null) {
                stopPalyAnim((ImageView) adapter.getTag(PLAYING_VIEW));
            }
            adapter.putTag(PLAYING_VIEW, voiceIV);
            adapter.setCheckedPosition(this.position);
            startPalyAnim(voiceIV);
            AudioPlayUtil.getInstance().start(ApiClient.getFileUrl(msgGApply.getCon()), PLAYING_VIEW, new OnAudioPlayListener() {
                @Override
                public void onAudioPlayEnd(String tag, String path) {
                    adapter.setCheckedPosition(-1);
                    adapter.removeTag(PLAYING_VIEW);
                    stopPalyAnim(voiceIV);
                }

                @Override
                public void onAudioException() {
                    adapter.setCheckedPosition(-1);
                    adapter.removeTag(PLAYING_VIEW);
                    stopPalyAnim(voiceIV);
                    AppContext.showToast("播放失败");
                }
            });
        }

        if (Constant.value_no.equals(bean.getIsReadOne())) {
            bean.setIsReadOne(Constant.value_yes);
            stateTV.setVisibility(View.GONE);
            reddotIV.setVisibility(View.INVISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DaoOperate.getInstance(_activity).update(bean);
                }
            });
        }

    }

    @OnClick(R.id.group_ll)
    public void clickGroupInfo() {
        MsgGApply msgObj = (MsgGApply) bean.getMsgObj();
        if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
            UIHelper.showActiveDetail(_activity, msgObj.getgInfo().getId());
        } else if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
            UIHelper.showGroupHome(_activity, msgObj.getgInfo().getId());
        }

        updateIsReadOne();
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        updateIsReadOne();
    }

    @Override
    protected void onItemLongClick() {
        super.onItemLongClick();
        showDialog();
        updateIsReadOne();
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

    @OnClick(R.id.user_ll)
    public void clickUserInfo() {
        MsgGApply msgObj = bean.getMsgObj();
        UInfo uInfo = msgObj.getuInfo();
        UIHelper.showMemberInfoAlone(_activity, uInfo.getId());
        updateIsReadOne();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (AudioPlayUtil.getInstance().isPlaying()) {
            AudioPlayUtil.getInstance().stop();
        }
    }

    private void updateIsReadOne() {
        if (Constant.value_no.equals(bean.getIsReadOne())) {
            bean.setIsReadOne(Constant.value_yes);
            DaoOperate.getInstance(_activity).update(bean);
            adapter.notifyDataSetChanged();
        }
    }
}
