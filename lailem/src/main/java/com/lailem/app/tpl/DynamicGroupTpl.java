package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.MessageCountManager;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.MGroup;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.lailem.app.widget.InviteDialog;

import java.util.ArrayList;

import butterknife.Bind;

public class DynamicGroupTpl extends BaseTpl<MGroup> {
    @Bind(R.id.image)
    ImageView image_iv;
    @Bind(R.id.reddot)
    ImageView reddot_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.privateMark)
    ImageView privateMark_iv;
    @Bind(R.id.contentArea)
    View contentArea;

    private MGroup bean;
    private Group group;

    public DynamicGroupTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_group;
    }

    @Override
    public void setBean(MGroup bean, int position) {
        this.bean = bean;
        group = GroupCache.getInstance(_activity).get(bean.getGroupId());
        if (group != null) {
            name_tv.setText(group.getName());
            if (Func.checkImageTag(group.getSquareSPic(), image_iv)) {
                Glide.with(_activity).load(ApiClient.getFileUrl(group.getSquareSPic())).placeholder(R.drawable.empty).error(R.drawable.empty).into(image_iv);
            }
        } else {
            name_tv.setText("");
            image_iv.setImageResource(R.drawable.empty);
        }

        if (Constant.value_yes.equals(bean.getIsTop())) {
            contentArea.setBackgroundResource(R.drawable.bg_c_card_light_blue);
        } else {
            contentArea.setBackgroundResource(R.drawable.bg_c_card);
        }

        if (bean.getNewPublishCount() > 0) {
            reddot_iv.setVisibility(View.VISIBLE);
        } else {
            reddot_iv.setVisibility(View.GONE);
        }

        if (Constant.value_permission_public.equals(String.valueOf(group.getPermission()))) {
            privateMark_iv.setVisibility(GONE);
        } else {
            privateMark_iv.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        UIHelper.showGroupHome(_activity, bean.getGroupId());
        if (bean.getNewPublishCount() > 0) {
            MessageCountManager.getInstance().reduce(MessageCountManager.KEY_NO_READ_COUNT_FOR_NGPUB, bean.getNewPublishCount());
            bean.setNewPublishCount(0);
            reddot_iv.setVisibility(View.GONE);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    DaoOperate.getInstance(_activity).update(bean);
                }
            }).start();
        }
    }

    @Override
    protected void onItemLongClick() {
        super.onItemLongClick();
        showDialog();
    }

    private void showDialog() {
        ActionDialog dialog = new ActionDialog(_activity);
        ActionData actionData1 = new ActionData("邀请成员", R.drawable.ic_invite_member_selector);
        ActionData actionData2 = new ActionData("群资料", R.drawable.ic_look_material_selector);
        ActionData actionData3 = null;
        if (Constant.value_yes.equals(bean.getIsTop())) {
            actionData3 = new ActionData("取消置顶", R.drawable.ic_set_top_selector);
        } else {
            actionData3 = new ActionData("置顶", R.drawable.ic_set_top_selector);
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
                    case 0:// 邀请成员
                        InviteDialog inviteDialog = new InviteDialog(_activity, group.getGroupId(), group.getName(), group.getSquareSPic(), group.getStartTime() + "", group.getAddress(), group.getIntro(), Const.INVITE_TYPE_GROUP);
                        inviteDialog.show();
                        break;
                    case 1:// 活动资料
                        UIHelper.showGroupHome(_activity, bean.getGroupId());
                        break;
                    case 2:// 置顶
                        if (Constant.value_yes.equals(bean.getIsTop())) {
                            // 取消置顶
                            int size = data.size();
                            int index = 0;
                            for (int i = 0; i < size; i++) {
                                MGroup mGroup = data.get(i);
                                // 找到第一个非置顶并且更新时间小于或等于该会话的会话
                                if (Constant.value_no.equals(mGroup.getIsTop()) && mGroup.getUpdateTime() < bean.getUpdateTime()) {
                                    index = i;
                                    break;
                                }
                            }
                            bean.setIsTop(Constant.value_no);
                            bean.setTopTime(null);// 置空利于查询排序
                            data.remove(bean);
                            if (index > 0) {
                                data.add(index - 1, bean);
                            } else {
                                data.add(0, bean);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // 置顶
                            bean.setIsTop(Constant.value_yes);
                            bean.setTopTime(System.currentTimeMillis());
                            data.remove(bean);
                            data.add(0, bean);
                            adapter.notifyDataSetChanged();
                        }
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                DaoOperate.getInstance(_activity).update(bean);
                            }
                        }).start();
                        break;

                }

            }
        });
        dialog.show();
    }

}
