package com.lailem.app.tpl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.model.inmsg.DInfo;
import com.lailem.app.chat.model.inmsg.UInfo;
import com.lailem.app.chat.model.msg.MsgACom;
import com.lailem.app.chat.model.msg.MsgALike;
import com.lailem.app.chat.model.msg.MsgDCom;
import com.lailem.app.chat.model.msg.MsgDLike;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.TimeUtil;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MeMessageListTpl extends BaseTpl<Message> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.date)
    TextView date_tv;
    @Bind(R.id.title)
    TextView title_tv;
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.image)
    ImageView image_iv;
    @Bind(R.id.imagePlay)
    ImageView imagePlay_iv;
    Message bean;

    private UInfo userInfo;

    public MeMessageListTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_message;
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        String sType = bean.getSType();
        if (Constant.sType_aCom.equals(sType)) {
            aCom(bean);
        } else if (Constant.sType_aLike.equals(sType)) {
            aLike(bean);
        } else if (Constant.sType_dCom.equals(sType)) {
            dCom(bean);
        } else if (Constant.sType_dLike.equals(sType)) {
            dLike(bean);
        }
    }

    private void aCom(Message bean) {
        MsgACom msgObj = bean.getMsgObj();
        setUserInfo(msgObj.getuInfo());
        setCommentInfo(msgObj.gettUInfo() == null ? null : msgObj.gettUInfo().getNick(), msgObj.getCom(), msgObj.gettCom());
        setActivityInfo(msgObj.getaId());
        setTime();
    }

    private void aLike(Message bean) {
        MsgALike msgObj = bean.getMsgObj();
        setUserInfo(msgObj.getuInfo());
        setLikeInfo();
        setActivityInfo(msgObj.getaId());
        setTime();

    }

    private void dCom(Message bean) {
        MsgDCom msgObj = bean.getMsgObj();
        setUserInfo(msgObj.getuInfo());
        setCommentInfo(msgObj.gettUInfo() == null ? null : msgObj.gettUInfo().getNick(), msgObj.getCom(), msgObj.gettCom());
        setDynamicInfo(msgObj.getdInfo());
        setTime();

    }

    private void dLike(Message bean) {
        MsgDLike msgObj = bean.getMsgObj();
        setUserInfo(msgObj.getuInfo());
        setLikeInfo();
        setDynamicInfo(msgObj.getdInfo());
        setTime();
    }

    /**
     * 设置用户信息
     *
     * @param userInfo
     */
    private void setUserInfo(UInfo userInfo) {
        this.userInfo = userInfo;
        if (Func.checkImageTag(userInfo.getsHead(), avatar_iv)) {
            Glide.with(_activity).load(StringUtils.getUri(userInfo.getsHead())).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(avatar_iv);
        }
        name_tv.setText(userInfo.getNick());
    }

    private void setCommentInfo(String toNickname, String comment, String toComment) {
        if (TextUtils.isEmpty(toComment)) {// 评论
            title_tv.setText(comment);
        } else {// 评论的评论
            title_tv.setText("回复了" + toNickname + ":" + comment);
        }
        title_tv.setCompoundDrawables(null, null, null, null);
    }

    private void setLikeInfo() {
        title_tv.setText("给你点了一个赞哟！");
        title_tv.setRight(R.drawable.ic_addzan);

    }

    private void setActivityInfo(String aId) {
        image_iv.setVisibility(View.VISIBLE);
        imagePlay_iv.setVisibility(View.GONE);
        Group group = GroupCache.getInstance(_activity).get(aId);
        if (group != null) {
            content_tv.setText("活动：" + group.getName());
            if (Func.checkImageTag(group.getSquareSPic(), image_iv)) {
                Glide.with(_activity).load(StringUtils.getUri(group.getSquareSPic())).placeholder(R.drawable.empty).error(R.drawable.empty).into(image_iv);
            }
        }
    }

    private void setDynamicInfo(DInfo dInfo) {
        // text（文本）、pic（图片）、voice（语音）、video（视频）、notice（通知）、vote（投票）、sche（日程）、add(地址)、cActi(创建活动)
        String dType = dInfo.getdType();
        if ("text".equals(dType)) {
            image_iv.setVisibility(View.GONE);
            imagePlay_iv.setVisibility(View.GONE);
            content_tv.setText(dInfo.getCon());
        } else if ("pic".equals(dType)) {
            image_iv.setVisibility(View.VISIBLE);
            imagePlay_iv.setVisibility(View.GONE);
            if (Func.checkImageTag(dInfo.getCon(), image_iv)) {
                Glide.with(_activity).load(StringUtils.getUri(dInfo.getCon())).placeholder(R.drawable.empty).error(R.drawable.empty).into(image_iv);
            }
            content_tv.setVisibility(View.GONE);
        } else if ("voice".equals(dType)) {
            image_iv.setVisibility(View.VISIBLE);
            imagePlay_iv.setVisibility(View.GONE);
            image_iv.setImageResource(R.drawable.bg_me_dynamic_voice);
            content_tv.setVisibility(View.GONE);
        } else if ("video".equals(dType)) {
            image_iv.setVisibility(View.VISIBLE);
            imagePlay_iv.setVisibility(View.VISIBLE);
            if (Func.checkImageTag(dInfo.getCon(), image_iv)) {
                Glide.with(_activity).load(StringUtils.getUri(dInfo.getCon())).placeholder(R.drawable.empty).error(R.drawable.empty).into(image_iv);
            }
            content_tv.setVisibility(View.GONE);
        } else if ("notice".equals(dType)) {
            image_iv.setVisibility(View.GONE);
            imagePlay_iv.setVisibility(View.GONE);
            content_tv.setText(dInfo.getCon());
        } else if ("vote".equals(dType)) {
            image_iv.setVisibility(View.VISIBLE);
            imagePlay_iv.setVisibility(View.GONE);
            content_tv.setText(dInfo.getCon());
            image_iv.setImageResource(R.drawable.bg_me_dynamic_vote);
        } else if ("sche".equals(dType)) {
            image_iv.setVisibility(View.GONE);
            imagePlay_iv.setVisibility(View.GONE);
            content_tv.setText(dInfo.getCon());
        } else if ("add".equals(dType)) {
            image_iv.setVisibility(View.VISIBLE);
            imagePlay_iv.setVisibility(View.GONE);
            image_iv.setImageResource(R.drawable.bg_me_dynamic_location);
            content_tv.setText(dInfo.getCon());
        } else if ("cActi".equals(dType)) {
            image_iv.setVisibility(View.GONE);
            imagePlay_iv.setVisibility(View.GONE);
            content_tv.setText(dInfo.getCon());
        }

    }

    private void setTime() {
        if (bean.getSTime() != null)
            date_tv.setText(TimeUtil.getInstance().getTime(bean.getSTime()));
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        String sType = bean.getSType();
        if (Constant.sType_aCom.equals(sType)) {
            MsgACom msgObj = bean.getMsgObj();
            Comment comment = new Comment();
            comment.setNickname(this.userInfo.getNick());
            comment.setUserId(this.userInfo.getId());
            comment.setHeadSPicName(this.userInfo.getsHead());
            comment.setComment(msgObj.getCom());
            comment.setId(msgObj.getId());
            UIHelper.showActiveDetailWithComment(_activity, msgObj.getaId(), comment);
        } else if (Constant.sType_aLike.equals(sType)) {
            MsgALike msgObj = bean.getMsgObj();
            UIHelper.showActiveDetail(_activity, msgObj.getaId());
        } else if (Constant.sType_dCom.equals(sType)) {
            MsgDCom msgObj = bean.getMsgObj();
            Comment comment = new Comment();
            comment.setNickname(this.userInfo.getNick());
            comment.setUserId(this.userInfo.getId());
            comment.setHeadSPicName(this.userInfo.getsHead());
            comment.setComment(msgObj.getCom());
            comment.setId(msgObj.getId());
            UIHelper.showDynamicDetailWithComment(_activity, msgObj.getdInfo().getId(), -1, comment);
        } else if (Constant.sType_dLike.equals(sType)) {
            MsgDLike msgObj = bean.getMsgObj();
            UIHelper.showDynamicDetail(_activity, msgObj.getdInfo().getId(), -1);
        }
    }

    @Override
    protected void onItemLongClick() {
        super.onItemLongClick();
        showDialog();
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

    @OnClick({R.id.name, R.id.avatar})
    public void clickAvatarOrName() {
        UIHelper.showMemberInfoAlone(_activity, userInfo.getId());
    }

}
