package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.adapter.AnimatorAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.bean.Result;
import com.lailem.app.cache.BlackListIdsCache;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.jsonbean.personal.BlacklistBean.BlackBean;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class BlackTpl extends BaseTpl<BlackBean> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.delete)
    ImageView delete_iv;

    private int position;
    private BlackBean bean;

    public BlackTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_black;
    }

    @Override
    public void setBean(BlackBean bean, int position) {
        this.position = position;
        this.bean = bean;
        if (Func.checkImageTag(bean.getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(bean.getRemark());
        if (adapter.getMode() == BaseListAdapter.MODE_NORMAL) {
            zoomOutDelete();
        } else if (adapter.getMode() == BaseListAdapter.MODE_EDIT) {
            zoomInDelete();
        }
    }

    @OnClick(R.id.delete)
    public void delete() {
        ApiClient.getApi().removeBlack(this, ac.getLoginUid(), bean.getBlackUserId());
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        _activity.showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        _activity.hideWaitDialog();
        if (res.isOK()) {
            data.remove(this.position);
            adapter.notifyDataSetChanged();
            BlackListIdsCache.getInstance(_activity).removeByKey(bean.getBlackUserId());
            DaoOperate.getInstance(_activity).deleteBlacklistIds(bean.getBlackUserId());
        } else {
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        _activity.hideWaitDialog();
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        UIHelper.showMemberInfoAlone(_activity, ((BlackBean) bean).getBlackUserId());
    }

    private void zoomOutDelete() {
        if (delete_iv.getVisibility() == VISIBLE) {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(delete_iv, "scaleX", 1.0f, 0f);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(delete_iv, "scaleY", 1.0f, 0f);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animatorX, animatorY);
            set.addListener(new AnimatorAdapter() {
                @Override
                public void onAnimationEnd(Animator arg0) {
                    delete_iv.setVisibility(GONE);
                }
            });
            set.setDuration(200).start();
        }
    }

    public void zoomInDelete() {
        if (delete_iv.getVisibility() != VISIBLE) {
            delete_iv.setVisibility(View.VISIBLE);
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(delete_iv, "scaleX", 0.0f, 1f);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(delete_iv, "scaleY", 0.0f, 1f);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animatorX, animatorY);
            set.setDuration(200).start();
        }
    }

}
