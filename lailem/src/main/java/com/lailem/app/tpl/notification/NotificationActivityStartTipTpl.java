package com.lailem.app.tpl.notification;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class NotificationActivityStartTipTpl extends BaseTpl<Message> {
    @Bind(R.id.state)
    TextView stateTV;
    @Bind(R.id.title)
    TextView titleTV;
    @Bind(R.id.content)
    TextView contentTV;
    @Bind(R.id.loc)
    TextView locTV;
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

    public NotificationActivityStartTipTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_notification_activity_start_tip;
    }

    @Override
    public void setBean(Message bean, int position) {
        this.bean = bean;

        if (Constant.value_no.equals(bean.getIsReadOne())) {
            stateTV.setVisibility(View.VISIBLE);
        } else {
            stateTV.setVisibility(View.GONE);
        }
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
