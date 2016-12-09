package com.lailem.app.tpl;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.GroupOrActiveNoExistReceiver;
import com.lailem.app.jsonbean.activegroup.GroupNear;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;

public class GroupNearTpl extends BaseTpl<GroupNear> {

    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.title)
    TextView title_tv;
    @Bind(R.id.distance)
    TextView distance_tv;
    @Bind(R.id.tag)
    TextView tag_tv;
    @Bind(R.id.memberCount)
    TextView memberCount_tv;
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.divider)
    ImageView divider;

    private GroupNear bean;
    private int position;
    private GroupOrActiveNoExistReceiver groupOrActiveNoExistReceiver = new GroupOrActiveNoExistReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GroupOrActiveNoExistReceiver.ACTION_ACTIVE_NO_EXIST)) {
                String groupId = intent.getStringExtra(Const.BUNDLE_KEY_GROUP_ID);
                if (groupId.equals(bean.getId())) {
                    data.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    public GroupNearTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_near;
    }

    @Override
    protected void initView() {
        super.initView();
        BroadcastManager.registerGroupOrActiveNotExistReceiver(_activity, groupOrActiveNoExistReceiver);
    }

    @Override
    public void setBean(GroupNear bean, int position) {
        this.bean = bean;
        this.position = position;
        if (Func.checkImageTag(bean.getSquareSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getSquareSPicName())).placeholder(R.drawable.empty).error(R.drawable.empty).into(avatar_iv);
        }
        title_tv.setText(bean.getName());
        distance_tv.setText(bean.getDistance());
        tag_tv.setText(bean.getTypeName());
        memberCount_tv.setText(bean.getCurrCount() + "人");
        content_tv.setText(bean.getIntro());
        distance_tv.setText(bean.getDistance());

        divider.setVisibility(position == data.size() - 1 ? GONE : VISIBLE);

        if (position % 2 == 0) {
            divider.setBackgroundResource(R.drawable.c_inset_line_7dot7);
        } else {
            divider.setBackgroundResource(R.drawable.c_inset_line_77dot7);
        }
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        UIHelper.showGroupHome(_activity, bean.getId());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BroadcastManager.unRegisterGroupOrActiveNoExistReceiver(_activity, groupOrActiveNoExistReceiver);
    }
}
