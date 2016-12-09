package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.ui.me.DraftBoxListActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.DynamicTaskUtil;
import com.lailem.app.widget.ActionDialog;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * Created by XuYang on 15/11/10.
 */
public class DraftBoxActionBarTpl extends BaseTpl<ObjectWrapper> implements ActionDialog.OnActionClickListener {
    private DynamicTaskUtil.DynamicTask task;
    private int position;
    private ActionDialog dialog;

    public DraftBoxActionBarTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_draft_box_actionbar;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.task = (DynamicTaskUtil.DynamicTask) wrapper.getObject();
        this.position = position;
    }

    @OnClick(R.id.delete)
    public void clickDelete() {
        if (dialog == null) {
            dialog = new ActionDialog(_activity);
            ActionDialog.DialogActionData.ActionData actionData1 = new ActionDialog.DialogActionData.ActionData("取消", R.drawable.ic_dialog_favorite_cancel_selector);
            ActionDialog.DialogActionData.ActionData actionData2 = new ActionDialog.DialogActionData.ActionData("删除", R.drawable.ic_dialog_ok_selector);
            ArrayList<ActionDialog.DialogActionData.ActionData> actionDatas = new ArrayList<ActionDialog.DialogActionData.ActionData>();
            actionDatas.add(actionData1);
            actionDatas.add(actionData2);
            ActionDialog.DialogActionData dialogActionData = new ActionDialog.DialogActionData("删除该草稿吗？", null, actionDatas);
            dialog.init(dialogActionData);
            dialog.setOnActionClickListener(this);
        }
        dialog.show();
    }

    @OnClick(R.id.send)
    public void clickSend() {
        DynamicTaskUtil.changeTaskState(_activity, this.task, Const.DYNAMIC_STATE_SENDING);
        String[] arr = task.key.split("_");
        String newKey = arr[0] + "_" + arr[1] + "_" + arr[2] + "_" + arr[3] + "_" + Const.DYNAMIC_STATE_SENDING + "_" + arr[5];
        BroadcastManager.sendDynamicTaskBroadcast(_activity, DynamicTaskReceiver.ACTION_ADD_TASK, newKey);

        removeItem();
        if (data.size() == 0) {
            BroadcastManager.sendDynamicTaskBroadcast(_activity, DynamicTaskReceiver.ACTION_TASK_EMPTY, newKey);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActionClick(ActionDialog dialog, View View, int position) {
        switch (position) {
            case 0:
                dialog.dismiss();
                break;
            case 1:
                AppContext.getInstance().deleteFile(this.task.key);
                removeItem();
                if (data.size() == 0) {
                    BroadcastManager.sendDynamicTaskBroadcast(_activity, DynamicTaskReceiver.ACTION_TASK_EMPTY, "");
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void removeItem() {
        for (int i = position; i >= 0; i--) {
            Base base = data.get(i);
            data.remove(base);
            if (base.getItemViewType() == DraftBoxListActivity.TPL_DATE) {
                break;
            }
        }
    }
}
