package com.lailem.app.tpl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.GroupOrActiveNoExistReceiver;
import com.lailem.app.jsonbean.activegroup.ActiveNearBean.ActiveNear;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ActiveNearTpl extends BaseTpl<ActiveNear> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.activeJoinCount)
    TextView activeJoinCount_tv;
    @Bind(R.id.activeImage)
    ImageView activeImage_iv;
    @Bind(R.id.activeName)
    TextView activeName_tv;
    @Bind(R.id.activeAddress)
    TextView activeAddress_tv;
    @Bind(R.id.activeDate)
    TextView activeDate_tv;
    @Bind(R.id.distance)
    TextView distance_tv;
    @Bind(R.id.divider)
    ImageView divider;

    private ActiveNear bean;
    private AnimatorSet set;
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

    public ActiveNearTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_near;
    }

    @Override
    protected void initView() {
        super.initView();
        activeName_tv.setShadowLayer(TDevice.dpToPixel(4), 0, TDevice.dpToPixel(2), Color.parseColor("#7F000000"));

        BroadcastManager.registerGroupOrActiveNotExistReceiver(_activity, groupOrActiveNoExistReceiver);

    }

    @Override
    public void setBean(ActiveNear bean, int position) {
        this.bean = bean;
        this.position = position;
        if (Func.checkImageTag(bean.getCreatorInfo().getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getCreatorInfo().getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CenterCrop(_activity), new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(bean.getCreatorInfo().getRemark());
        activeJoinCount_tv.setText(bean.getCurrCount() + "人参加");
        if (Func.checkImageTag(bean.getbPicName(), activeImage_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getbPicName())).placeholder(R.drawable.empty).error(R.drawable.empty).centerCrop().into(activeImage_iv);
        }
        activeName_tv.setText(bean.getName());
        activeAddress_tv.setText(bean.getAddress());
        activeDate_tv.setText(Func.formatTime4(bean.getStartTime()));
        distance_tv.setText(bean.getDistance());

        divider.setVisibility(position == 1 ? GONE : VISIBLE);

        zoomImage();
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        UIHelper.showActiveDetail(_activity, bean.getId());
    }

    private void zoomImage() {
        if (set == null) {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(activeImage_iv, "scaleX", 0.2f, 1f);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(activeImage_iv, "scaleY", 0.2f, 1f);
            set = new AnimatorSet();
            set.playTogether(animatorX, animatorY);
            set.setDuration(400);
        }
        if (!set.isRunning()) {
            set.start();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BroadcastManager.unRegisterGroupOrActiveNoExistReceiver(_activity, groupOrActiveNoExistReceiver);
    }
}
