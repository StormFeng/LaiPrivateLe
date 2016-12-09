package com.lailem.app.tpl.notification;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.model.inmsg.VInfo;
import com.lailem.app.chat.model.msg.MsgAGApply;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.User;
import com.lailem.app.jni.JniSharedLibraryWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TimeUtil;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class NotificationAgreeGroupApplyTpl extends BaseTpl<Message> implements OnClickListener {
    @Bind(R.id.state)
    TextView stateTV;
    @Bind(R.id.title)
    TextView titleTV;
    @Bind(R.id.content)
    TextView contentTV;
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

    public NotificationAgreeGroupApplyTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {

        return R.layout.item_notification_agree_group_apply;
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        //读状态
        if (Constant.value_no.equals(bean.getIsReadOne())) {
            stateTV.setVisibility(View.VISIBLE);
        } else {
            stateTV.setVisibility(View.GONE);
        }
        MsgAGApply msgObj = bean.getMsgObj();
        if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
            titleTV.setText("您已加入新群组啦！");
            contentTV.setText("恭喜您加入新群组啦，现在可以跟新的小伙伴们一起玩耍了！");
            rightBottomTV.setText("开始群聊");
            rightBottomTV.setOnClickListener(this);
        } else {
            titleTV.setText("您已参加了新活动！");
            contentTV.setText("恭喜您参加了新的活动，现在可以跟新的小伙伴们一起玩耍了！");
            rightBottomTV.setText("");
            rightBottomTV.setOnClickListener(null);
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
        User user = UserCache.getInstance(_activity).get(JniSharedLibraryWrapper.notificationPublisherId());
        if (user != null) {
            if (Func.checkImageTag(user.getHead(), headIV)) {
                Glide.with(_activity).load(StringUtils.getUri(user.getHead())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(headIV);
            }
            nameTV.setText(user.getNickname());
        }
        dateTV.setText(TimeUtil.getInstance().getTime(bean.getSTime()));

    }

    @OnClick(R.id.group_ll)
    public void clickGroupInfo() {
        MsgAGApply msgObj = bean.getMsgObj();
        if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
            UIHelper.showActiveDetail(_activity, msgObj.getgInfo().getId());
        } else if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
            UIHelper.showGroupHome(_activity, msgObj.getgInfo().getId());
        }
        updateIsReadOne();
    }


    @Override
    public void onClick(View view) {
        MsgAGApply msgObj = bean.getMsgObj();
        switch (view.getId()) {
            case R.id.right_bottom://开始群聊按钮
                UIHelper.showChat(_activity, msgObj.getgInfo().getId(), null, Constant.cType_gc);
                break;
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
        MsgAGApply msgObj = bean.getMsgObj();
        VInfo vInfo = msgObj.getvInfo();
        UIHelper.showMemberInfoAlone(_activity, vInfo.getId());
        updateIsReadOne();
    }

    private void updateIsReadOne() {
        if (Constant.value_no.equals(bean.getIsReadOne())) {
            bean.setIsReadOne(Constant.value_yes);
            DaoOperate.getInstance(_activity).update(bean);
            adapter.notifyDataSetChanged();
        }
    }
}
