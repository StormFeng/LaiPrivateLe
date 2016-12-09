package com.lailem.app.ui.group;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.InviteDialog;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.togglebutton.ToggleButton;
import com.lailem.app.widget.togglebutton.ToggleButton.OnToggleChanged;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 聊天页面进入的群资料
 *
 * @author XuYang
 */
public class GroupInfoForChatActivity extends BaseActivity implements OnToggleChanged {
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.groupImage)
    ImageView groupImage_iv;
    @Bind(R.id.groupName)
    TextView groupName_tv;
    @Bind(R.id.setTop)
    ToggleButton setTopToggle;
    @Bind(R.id.setNoDisturb)
    ToggleButton setNoDisturbToggle;
    Conversation conversation;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_for_chat);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("群资料").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        group = GroupCache.getInstance(_activity).get(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
        if (Func.checkImageTag(group.getSquareSPic(), groupImage_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(group.getSquareSPic())).into(groupImage_iv);
        }
        groupName_tv.setText(group.getName());


        String conversationId = getIntent().getStringExtra("conversationId");
        conversation = DaoOperate.getInstance(_activity).queryConversationByConversationId(conversationId);

        if (conversation != null && Constant.value_yes.equals(conversation.getIsTop())) {
            setTopToggle.toggleOn();
        } else {
            setTopToggle.toggleOff();
        }


        if (conversation != null && Constant.value_yes.equals(conversation.getIsNoTip())) {
            setNoDisturbToggle.toggleOn();
        } else {
            setNoDisturbToggle.toggleOff();
        }

        setTopToggle.setOnToggleChanged(this);
        setNoDisturbToggle.setOnToggleChanged(this);
    }

    @OnClick(R.id.groupHome)
    public void clickGroupHome() {
        UIHelper.showGroupHome(_activity, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @OnClick(R.id.clearHistory)
    public void clearHistory() {
        if (conversation != null) {
            showWaitDialog();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    DaoOperate.getInstance(_activity).deleteMessages(conversation.getConversationId());
                    BroadcastManager.sendClearMsgRecordBroadcast(_activity, conversation.getConversationId());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            hideWaitDialog();
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onToggle(ToggleButton toggleButton, boolean on) {
        switch (toggleButton.getId()) {
            case R.id.setTop:
                // 置顶
                if (conversation != null) {
                    if (on) {
                        conversation.setIsTop(Constant.value_yes);
                        conversation.setTopTime(System.currentTimeMillis());
                        BroadcastManager.sendSetTopBroadcast(_activity, conversation.getConversationId(), Constant.value_yes);
                    } else {
                        conversation.setIsTop(Constant.value_no);
                        conversation.setTopTime(null);
                        BroadcastManager.sendSetTopBroadcast(_activity, conversation.getConversationId(), Constant.value_no);
                    }
                    DaoOperate.getInstance(_activity).update(conversation);
                }
                break;
            case R.id.setNoDisturb:
                // 消息免打扰
                if (conversation != null) {
                    if (on) {
                        conversation.setIsNoTip(Constant.value_yes);
                        BroadcastManager.sendNoTipBroadcast(_activity, conversation.getConversationId(), Constant.value_yes);
                    } else {
                        conversation.setIsNoTip(Constant.value_no);
                        BroadcastManager.sendNoTipBroadcast(_activity, conversation.getConversationId(), Constant.value_no);
                    }
                    DaoOperate.getInstance(_activity).update(conversation);
                }
                break;
        }
    }

    @OnClick(R.id.inviteFriend)
    public void clickInviteFriend() {
        InviteDialog dialog = new InviteDialog(_activity, group.getGroupId(), group.getName(), group.getSquareSPic(), group.getStartTime() + "", group.getAddress(), group.getIntro(), Const.INVITE_TYPE_GROUP);
        dialog.show();
    }

}
