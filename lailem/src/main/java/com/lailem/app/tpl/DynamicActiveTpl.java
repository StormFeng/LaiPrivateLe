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
import com.lailem.app.utils.SetTopUtil;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.lailem.app.widget.InviteDialog;

import java.util.ArrayList;

import butterknife.Bind;

public class DynamicActiveTpl extends BaseTpl<MGroup> {
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

    public DynamicActiveTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_active;
    }

    @Override
    public void setBean(MGroup bean, int position) {
        this.bean = bean;
        group = GroupCache.getInstance(_activity).get(bean.getGroupId());
        if (group != null) {
            name_tv.setText(group.getName());
            //图片不变不重新渲染
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
        Group group = DaoOperate.getInstance(_activity).queryGroup(bean.getGroupId());
        if (Const.TYPE_ID_VOTE.equals(group.getTypeId())) {
            // 投票活动
            UIHelper.showVoteActiveDetail(_activity, group.getGroupId());
        } else {
            // 普通活动
            UIHelper.showActiveDetail(_activity, bean.getGroupId());
        }
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
        ActionData actionData2 = new ActionData("活动资料", R.drawable.ic_look_material_selector);
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
                        InviteDialog inviteDialog = new InviteDialog(_activity, group.getGroupId(), group.getName(), group.getSquareSPic(), group.getStartTime() + "", group.getAddress(), group.getIntro(), Const.INVITE_TYPE_ACTIVE);
                        inviteDialog.show();
                        break;
                    case 1:// 活动资料
                        UIHelper.showActiveDetail(_activity, group.getGroupId());
                        break;
                    case 2:// 置顶
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

                }

            }
        });
        dialog.show();
    }

}
