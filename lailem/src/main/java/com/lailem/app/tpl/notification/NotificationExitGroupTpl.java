package com.lailem.app.tpl.notification;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.model.inmsg.UInfo;
import com.lailem.app.chat.model.msg.MsgExitG;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
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

public class NotificationExitGroupTpl extends BaseTpl<Message> {
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

    public NotificationExitGroupTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {

        return R.layout.item_notification_exit_group;
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;
        // 读状态
        if (Constant.value_no.equals(bean.getIsReadOne())) {
            stateTV.setVisibility(View.VISIBLE);
        } else {
            stateTV.setVisibility(View.GONE);
        }
        MsgExitG msgObj = bean.getMsgObj();

        if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
            titleTV.setText(msgObj.getuInfo().getNick() + "退出群组");
        } else {
            titleTV.setText(msgObj.getuInfo().getNick() + "退出活动");
        }
        contentTV.setText("有聚有散，我再也不能跟你们一起玩耍了，你们要想我!");
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
    }

    @OnClick(R.id.group_ll)
    public void clickGroupInfo() {
        MsgExitG msgObj = bean.getMsgObj();
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
        MsgExitG msgObj = bean.getMsgObj();
        UInfo uInfo = msgObj.getuInfo();
        UIHelper.showMemberInfoAlone(_activity, uInfo.getId());
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
