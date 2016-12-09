package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.lailem.app.R;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by XuYang on 15/12/21.
 */
public class AddDynamicDialog extends Dialog {
    private static final int DEFAULT_THEME = R.style.confirm_dialog;

    @Bind(R.id.addDynamic_tv)
    View addDynamic_tv;
    @Bind(R.id.addDynamic_iv)
    View addDynamic_iv;
    @Bind(R.id.addVote)
    View addVote;
    @Bind(R.id.addVote_tv)
    View addVote_tv;
    @Bind(R.id.addVote_iv)
    View addVote_iv;
    @Bind(R.id.addNotice)
    View addNotice;
    @Bind(R.id.addNotice_tv)
    View addNotice_tv;
    @Bind(R.id.addNotice_iv)
    View addNotice_iv;
    @Bind(R.id.addSchedule)
    View addSchedule;
    @Bind(R.id.addSchedule_tv)
    View addSchedule_tv;
    @Bind(R.id.addSchedule_iv)
    View addSchedule_iv;
    @Bind(R.id.addActive)
    View addActive;
    @Bind(R.id.addActive_tv)
    View addActive_tv;
    @Bind(R.id.addActive_iv)
    View addActive_iv;
    @Bind(R.id.close_ic)
    ImageView close_ic;
    @Bind(R.id.close_bg)
    ImageView close_bg;

    private int duration = 200;

    private Context context;
    private AddDynamicChoiceDialog addDynamicChoiceDialog;
    private String groupId;
    private String roleType;
    private String groupType;
    private String createActivityFlay;

    public AddDynamicDialog(Context context, String groupId, String groupType, String roleType, String createActivityFlay) {
        super(context, DEFAULT_THEME);
        init(context, groupId, groupType, roleType, createActivityFlay);
    }

    private void init(Context context, String groupId, String groupType, String roleType, String createActivityFlay) {
        this.context = context;
        this.groupId = groupId;
        this.groupType = groupType;
        this.roleType = roleType;
        this.createActivityFlay = createActivityFlay;

        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);

        View contentView = View.inflate(context, R.layout.dialog_add_dynamic, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) TDevice.getScreenWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(contentView, params);
        ButterKnife.bind(this, contentView);

        getWindow().setWindowAnimations(R.style.DialogNoAnimation);

        if (Const.GROUPTYPE_ACTIVITY.equals(groupType)) {
            addActive.setVisibility(View.GONE);
        } else if (Const.GROUPTYPE_GROUP.equals(groupType)) {
            addActive.setVisibility(View.VISIBLE);
        }

        if (Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType)) {
            addNotice.setVisibility(View.VISIBLE);
            addSchedule.setVisibility(View.VISIBLE);
        } else if (Const.ROLE_TYPE_NORMAL.equals(roleType)) {
            addNotice.setVisibility(View.GONE);
            addSchedule.setVisibility(View.GONE);
        }


    }

    @OnClick({R.id.rootView, R.id.addDynamic, R.id.addVote, R.id.addNotice, R.id.addSchedule, R.id.addActive, R.id.close})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.addDynamic:
                UIHelper.showNewCreateDynamic(context, groupId);
                dismiss();
                break;
            case R.id.addVote:
                UIHelper.showCreateDynamicVote(context, groupId, true);
                dismiss();
                break;
            case R.id.addNotice:
                UIHelper.showCreateNotice(context, groupId);
                dismiss();
                break;
            case R.id.addSchedule:
                UIHelper.showCreateDynamicSchedule(context, groupId, true);
                dismiss();
                break;
            case R.id.addActive:
                UIHelper.showCreateActiveType(context, groupId);
                dismiss();
                break;
            case R.id.rootView:
            case R.id.close:
                hideSelf();
                break;
        }
    }

    @OnLongClick(R.id.addDynamic)
    public boolean longClickPic() {
        if (addDynamicChoiceDialog == null) {
            addDynamicChoiceDialog = new AddDynamicChoiceDialog(context);
            addDynamicChoiceDialog.setGroupData(groupId);
        }
        addDynamicChoiceDialog.show();
        dismiss();
        return false;
    }

    private AnimatorSet buildChoiceShowAnim(final View view, int duration) {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.0f);
        scaleXAnimator.setDuration(duration);
        ViewHelper.setPivotX(view, TDevice.dpToPixel(40) / 2);
        ViewHelper.setPivotX(view, TDevice.dpToPixel(40));
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.0f);
        scaleYAnimator.setDuration(duration);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        alphaAnimator.setDuration(duration);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        set.setInterpolator(new OvershootInterpolator() {
            @Override
            public float getInterpolation(float t) {
                float mTension = 3f;
                t -= 1.0f;
                return t * t * ((mTension + 1) * t + mTension) + 1.0f;
            }
        });
        return set;
    }

    private AnimatorSet buildChoiceHideAnim(final View view, int duration) {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.8f);
        scaleXAnimator.setDuration(duration);
        ViewHelper.setPivotX(view, TDevice.dpToPixel(40) / 2);
        ViewHelper.setPivotX(view, TDevice.dpToPixel(40));
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.8f);
        scaleYAnimator.setDuration(duration);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f);
        alphaAnimator.setDuration(duration);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        set.setInterpolator(new OvershootInterpolator());
        return set;
    }

    private AnimatorSet assembleShowAnim() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator controlAnim = ObjectAnimator.ofFloat(close_ic, "show", 0f, 1f);
        controlAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float cVal = (float) valueAnimator.getAnimatedValue();
                ViewHelper.setAlpha(close_ic, cVal);
                ViewHelper.setAlpha(close_bg, cVal);
                ViewHelper.setRotation(close_ic, (cVal - 1) * 90);
            }
        });
        controlAnim.setDuration(300);

        // 活动 日程 通知 投票 动态
        if (Const.GROUPTYPE_GROUP.equals(groupType) && (Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType))) {
            if (Const.CREATE_ACTIVITY_FLAY_DENY.equals(createActivityFlay) && Const.ROLE_TYPE_MANAGER.equals(roleType)) {
                addActive.setVisibility(View.GONE);
                //是管理员并且不能创建活动
                AnimatorSet addScheduleIVAnim = buildChoiceShowAnim(addSchedule_iv, 0);
                AnimatorSet addScheduleTVAnim = buildChoiceShowAnim(addSchedule_tv, 0);
                AnimatorSet addNoticeIVAnim = buildChoiceShowAnim(addNotice_iv, duration);
                AnimatorSet addNoticeTVAnim = buildChoiceShowAnim(addNotice_tv, duration);
                AnimatorSet addVoteIVAnim = buildChoiceShowAnim(addVote_iv, duration * 2);
                AnimatorSet addVoteTVAnim = buildChoiceShowAnim(addVote_tv, duration * 2);
                AnimatorSet addDyanmicIVAnim = buildChoiceShowAnim(addDynamic_iv, duration * 3);
                AnimatorSet addDynamicTVAnim = buildChoiceShowAnim(addDynamic_tv, duration * 3);
                set.playTogether(controlAnim, addScheduleIVAnim, addScheduleTVAnim, addNoticeIVAnim, addNoticeTVAnim
                        , addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
            } else {
                addActive.setVisibility(View.VISIBLE);
                AnimatorSet addActiveIVAnim = buildChoiceShowAnim(addActive_iv, 0);
                AnimatorSet addActiveTVAnim = buildChoiceShowAnim(addActive_tv, 0);
                AnimatorSet addScheduleIVAnim = buildChoiceShowAnim(addSchedule_iv, duration);
                AnimatorSet addScheduleTVAnim = buildChoiceShowAnim(addSchedule_tv, duration);
                AnimatorSet addNoticeIVAnim = buildChoiceShowAnim(addNotice_iv, duration * 2);
                AnimatorSet addNoticeTVAnim = buildChoiceShowAnim(addNotice_tv, duration * 2);
                AnimatorSet addVoteIVAnim = buildChoiceShowAnim(addVote_iv, duration * 3);
                AnimatorSet addVoteTVAnim = buildChoiceShowAnim(addVote_tv, duration * 3);
                AnimatorSet addDyanmicIVAnim = buildChoiceShowAnim(addDynamic_iv, duration * 4);
                AnimatorSet addDynamicTVAnim = buildChoiceShowAnim(addDynamic_tv, duration * 4);
                set.playTogether(controlAnim, addActiveIVAnim, addActiveTVAnim, addScheduleIVAnim, addScheduleTVAnim, addNoticeIVAnim, addNoticeTVAnim
                        , addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
            }
        }

        // 日程 通知 投票 动态
        if (!Const.GROUPTYPE_GROUP.equals(groupType) && (Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType))) {
            AnimatorSet addScheduleIVAnim = buildChoiceShowAnim(addSchedule_iv, 0);
            AnimatorSet addScheduleTVAnim = buildChoiceShowAnim(addSchedule_tv, 0);
            AnimatorSet addNoticeIVAnim = buildChoiceShowAnim(addNotice_iv, duration);
            AnimatorSet addNoticeTVAnim = buildChoiceShowAnim(addNotice_tv, duration);
            AnimatorSet addVoteIVAnim = buildChoiceShowAnim(addVote_iv, duration * 2);
            AnimatorSet addVoteTVAnim = buildChoiceShowAnim(addVote_tv, duration * 2);
            AnimatorSet addDyanmicIVAnim = buildChoiceShowAnim(addDynamic_iv, duration * 3);
            AnimatorSet addDynamicTVAnim = buildChoiceShowAnim(addDynamic_tv, duration * 3);
            set.playTogether(controlAnim, addScheduleIVAnim, addScheduleTVAnim, addNoticeIVAnim, addNoticeTVAnim
                    , addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
        }

        // 活动 投票 动态
        if (Const.GROUPTYPE_GROUP.equals(groupType) && !(Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType))) {
            if (Const.CREATE_ACTIVITY_FLAY_DENY.equals(createActivityFlay)) {
                addActive.setVisibility(View.GONE);
                AnimatorSet addVoteIVAnim = buildChoiceShowAnim(addVote_iv, 0);
                AnimatorSet addVoteTVAnim = buildChoiceShowAnim(addVote_tv, 0);
                AnimatorSet addDyanmicIVAnim = buildChoiceShowAnim(addDynamic_iv, duration);
                AnimatorSet addDynamicTVAnim = buildChoiceShowAnim(addDynamic_tv, duration);
                set.playTogether(controlAnim, addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
            } else {
                addActive.setVisibility(View.VISIBLE);
                AnimatorSet addActiveIVAnim = buildChoiceShowAnim(addActive_iv, 0);
                AnimatorSet addActiveTVAnim = buildChoiceShowAnim(addActive_tv, 0);
                AnimatorSet addVoteIVAnim = buildChoiceShowAnim(addVote_iv, duration);
                AnimatorSet addVoteTVAnim = buildChoiceShowAnim(addVote_tv, duration);
                AnimatorSet addDyanmicIVAnim = buildChoiceShowAnim(addDynamic_iv, duration * 2);
                AnimatorSet addDynamicTVAnim = buildChoiceShowAnim(addDynamic_tv, duration * 2);
                set.playTogether(controlAnim, addActiveIVAnim, addActiveTVAnim, addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
            }
        }

        // 投票 动态
        if (!Const.GROUPTYPE_GROUP.equals(groupType) && !(Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType))) {
            AnimatorSet addVoteIVAnim = buildChoiceShowAnim(addVote_iv, 0);
            AnimatorSet addVoteTVAnim = buildChoiceShowAnim(addVote_tv, 0);
            AnimatorSet addDyanmicIVAnim = buildChoiceShowAnim(addDynamic_iv, duration);
            AnimatorSet addDynamicTVAnim = buildChoiceShowAnim(addDynamic_tv, duration);
            set.playTogether(controlAnim, addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
        }

        return set;
    }

    private AnimatorSet assembleHideAnim() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator controlAnim = ObjectAnimator.ofFloat(close_ic, "hide", 1f, 0f);
        controlAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float cVal = (float) valueAnimator.getAnimatedValue();
                ViewHelper.setAlpha(close_ic, cVal);
                ViewHelper.setAlpha(close_bg, cVal);
                ViewHelper.setRotation(close_ic, (cVal - 1) * 90);
            }
        });
        controlAnim.setDuration(300);

        // 活动 日程 通知 投票 动态
        if (Const.GROUPTYPE_GROUP.equals(groupType) && (Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType))) {
            AnimatorSet addActiveIVAnim = buildChoiceHideAnim(addActive_iv, 0);
            AnimatorSet addActiveTVAnim = buildChoiceHideAnim(addActive_tv, 0);
            AnimatorSet addScheduleIVAnim = buildChoiceHideAnim(addSchedule_iv, duration);
            AnimatorSet addScheduleTVAnim = buildChoiceHideAnim(addSchedule_tv, duration);
            AnimatorSet addNoticeIVAnim = buildChoiceHideAnim(addNotice_iv, duration * 2);
            AnimatorSet addNoticeTVAnim = buildChoiceHideAnim(addNotice_tv, duration * 2);
            AnimatorSet addVoteIVAnim = buildChoiceHideAnim(addVote_iv, duration * 3);
            AnimatorSet addVoteTVAnim = buildChoiceHideAnim(addVote_tv, duration * 3);
            AnimatorSet addDyanmicIVAnim = buildChoiceHideAnim(addDynamic_iv, duration * 4);
            AnimatorSet addDynamicTVAnim = buildChoiceHideAnim(addDynamic_tv, duration * 4);
            set.playTogether(controlAnim, addActiveIVAnim, addActiveTVAnim, addScheduleIVAnim, addScheduleTVAnim, addNoticeIVAnim, addNoticeTVAnim
                    , addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
        }

        // 日程 通知 投票 动态
        if (!Const.GROUPTYPE_GROUP.equals(groupType) && (Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType))) {
            AnimatorSet addScheduleIVAnim = buildChoiceHideAnim(addSchedule_iv, 0);
            AnimatorSet addScheduleTVAnim = buildChoiceHideAnim(addSchedule_tv, 0);
            AnimatorSet addNoticeIVAnim = buildChoiceHideAnim(addNotice_iv, duration);
            AnimatorSet addNoticeTVAnim = buildChoiceHideAnim(addNotice_tv, duration);
            AnimatorSet addVoteIVAnim = buildChoiceHideAnim(addVote_iv, duration * 2);
            AnimatorSet addVoteTVAnim = buildChoiceHideAnim(addVote_tv, duration * 2);
            AnimatorSet addDyanmicIVAnim = buildChoiceHideAnim(addDynamic_iv, duration * 3);
            AnimatorSet addDynamicTVAnim = buildChoiceHideAnim(addDynamic_tv, duration * 3);
            set.playTogether(controlAnim, addScheduleIVAnim, addScheduleTVAnim, addNoticeIVAnim, addNoticeTVAnim
                    , addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
        }

        // 活动 投票 动态
        if (Const.GROUPTYPE_GROUP.equals(groupType) && !(Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType))) {
            AnimatorSet addActiveIVAnim = buildChoiceHideAnim(addActive_iv, 0);
            AnimatorSet addActiveTVAnim = buildChoiceHideAnim(addActive_tv, 0);
            AnimatorSet addVoteIVAnim = buildChoiceHideAnim(addVote_iv, duration);
            AnimatorSet addVoteTVAnim = buildChoiceHideAnim(addVote_tv, duration);
            AnimatorSet addDyanmicIVAnim = buildChoiceHideAnim(addDynamic_iv, duration * 2);
            AnimatorSet addDynamicTVAnim = buildChoiceHideAnim(addDynamic_tv, duration * 2);
            set.playTogether(controlAnim, addActiveIVAnim, addActiveTVAnim, addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
        }

        // 投票 动态
        if (!Const.GROUPTYPE_GROUP.equals(groupType) && !(Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType))) {
            AnimatorSet addVoteIVAnim = buildChoiceHideAnim(addVote_iv, 0);
            AnimatorSet addVoteTVAnim = buildChoiceHideAnim(addVote_tv, 0);
            AnimatorSet addDyanmicIVAnim = buildChoiceHideAnim(addDynamic_iv, duration);
            AnimatorSet addDynamicTVAnim = buildChoiceHideAnim(addDynamic_tv, duration);
            set.playTogether(controlAnim, addVoteIVAnim, addVoteTVAnim, addDyanmicIVAnim, addDynamicTVAnim);
        }

        return set;
    }

    public void showSelf() {
        show();
        AnimatorSet set = assembleShowAnim();
        set.start();
    }

    public void hideSelf() {
        dismiss();
    }
}
