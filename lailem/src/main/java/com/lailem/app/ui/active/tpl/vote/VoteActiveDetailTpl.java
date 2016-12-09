package com.lailem.app.ui.active.tpl.vote;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.VoteActiveDetailBean.ActiveInfo;
import com.lailem.app.jsonbean.activegroup.VoteInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.dynamic.DynamicVoteView;
import com.lailem.app.widget.dynamic.DynamicVoteView.OnVotedCallback;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.Bind;
import butterknife.OnClick;

public class VoteActiveDetailTpl extends BaseTpl<ActiveInfo> implements OnVotedCallback {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.sexAgeArea)
    View sexAgeArea;
    @Bind(R.id.sex)
    ImageView sex_iv;
    @Bind(R.id.age)
    TextView age_tv;
    @Bind(R.id.sign)
    TextView sign_tv;
    @Bind(R.id.chooseType)
    TextView chooseType_tv;
    @Bind(R.id.dynamicVote)
    DynamicVoteView dynamicVoteView;
    private ActiveInfo bean;

    public VoteActiveDetailTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_vote_active_detail;
    }

    @Override
    public void setBean(ActiveInfo bean, int position) {
        this.bean = bean;

        ac.imageLoader.loadImage(ApiClient.getFileUrl(bean.getCreatorInfo().getHeadSPicName()), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                avatar_iv.setImageBitmap(loadedImage);
            }
        });
        name_tv.setText(bean.getRemark());
        String sex = bean.getCreatorInfo().getSex();
        if (Const.MALE.equals(sex)) {
            sex_iv.setImageResource(R.drawable.ic_male);
            sexAgeArea.setBackgroundResource(R.drawable.bg_male);
        } else if (Const.FEMALE.equals(sex)) {
            sex_iv.setImageResource(R.drawable.ic_female);
            sexAgeArea.setBackgroundResource(R.drawable.bg_female);
        }
        age_tv.setText(bean.getCreatorInfo().getAge());
        if (TextUtils.isEmpty(bean.getCreatorInfo().getPersonalizedSignature())) {
            sign_tv.setText(Const.EMPTY_PERSONAL_SIGN);
        } else {
            sign_tv.setText(bean.getCreatorInfo().getPersonalizedSignature());
        }

        VoteInfo voteInfo = bean.getVoteInfo();

        if ("1".equals(voteInfo.getSelectCount())) {
            chooseType_tv.setText("单选");
        } else {
            chooseType_tv.setText("最多选择" + voteInfo.getSelectCount() + "个");
        }

        renderDynamicVoteView(voteInfo);
    }

    private void renderDynamicVoteView(VoteInfo bean) {
        boolean isHasVote = bean.getIsVoted().equals(Const.HAS_VOTE);
        boolean isVoteOver = Func.isExpired(bean.getEndTime());
        boolean isShowCheckbox = (!isVoteOver && !isHasVote);
        boolean isShowColorPercent = isHasVote;
        boolean isShowItemVoteCount = isHasVote;
        boolean isShowActionBar = true;
        dynamicVoteView.render(bean, isShowCheckbox, isShowColorPercent, isShowItemVoteCount, isShowActionBar, isHasVote, isVoteOver, this);
    }

    @Override
    public void onVotedCallback() {
        this.bean.getVoteInfo().setIsVoted(Const.HAS_VOTE);
    }

    @OnClick({R.id.avatar, R.id.name})
    public void clickAvatarOrName() {
        UIHelper.showMemberInfoAlone(_activity, bean.getCreatorInfo().getId());
    }

}
