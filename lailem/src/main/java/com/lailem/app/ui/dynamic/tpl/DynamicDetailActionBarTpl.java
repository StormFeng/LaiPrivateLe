package com.lailem.app.ui.dynamic.tpl;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.AnimatorAdapter;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.dynamic.AddCommentBean;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.dynamic.DynamicDetailActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.AddCommentBarDialog;
import com.lailem.app.widget.AddCommentBarDialog.OnSendClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.OnClick;

public class DynamicDetailActionBarTpl extends BaseTpl<Object> implements OnSendClickListener {
    // 1（已赞）、2（未赞）
    public static final String HAS_ZAN = "1";
    public static final String NOT_ZAN = "2";
    public static final String HAS_FAVOR = "1";
    public static final String NOT_FAVOR = "2";

    @Bind(R.id.addfavor)
    TextView addFavor_tv;
    @Bind(R.id.addcomment)
    TextView addcomment_tv;
    @Bind(R.id.addZan)
    TextView addZan_tv;

    private View addZanBig;
    private View addFavorBig;
    private int position;
    private DynamicBean bean;
    private AddCommentBarDialog addCommentBarDialog;
    private String tempContent;

    public DynamicDetailActionBarTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        FrameLayout decorView = (FrameLayout) _activity.getWindow().getDecorView();
        // 向decorView中添加点赞和收藏需要执行动画的view
        addZanBig = View.inflate(_activity, R.layout.view_add_zan_big, null);
        FrameLayout.LayoutParams zanParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        zanParams.gravity = Gravity.CENTER;
        decorView.addView(addZanBig, zanParams);
        addZanBig.setVisibility(INVISIBLE);

        addFavorBig = View.inflate(_activity, R.layout.view_add_favor_big, null);
        FrameLayout.LayoutParams favorParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        favorParams.gravity = Gravity.CENTER;
        decorView.addView(addFavorBig, favorParams);
        addFavorBig.setVisibility(INVISIBLE);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_detail_actionbar;
    }

    @Override
    public void setBean(Object obj, int position) {
        this.bean = (DynamicBean) obj;
        this.position = position;

        addFavor_tv.setText(bean.getCollectCount());
        addcomment_tv.setText(bean.getComCount());
        addZan_tv.setText(bean.getLikeCount());

        if (HAS_ZAN.equals(bean.getIsLiked())) {
            addZan_tv.setTextColor(getResources().getColor(R.color.orange));
            addZan_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addzan_orange), null, null, null);
        } else if (NOT_ZAN.equals(bean.getIsLiked())) {
            addZan_tv.setTextColor(Color.parseColor("#999999"));
            addZan_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addzan), null, null, null);
        }
        if (HAS_FAVOR.equals(bean.getIsCollected())) {
            addFavor_tv.setTextColor(getResources().getColor(R.color.orange));
            addFavor_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addfavor_orange), null, null, null);
        } else if (NOT_FAVOR.equals(bean.getIsCollected())) {
            addFavor_tv.setTextColor(Color.parseColor("#999999"));
            addFavor_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addfavor), null, null, null);
        }
    }

    /**
     * 处理点赞，评论，收藏
     *
     * @param v
     */
    @OnClick({R.id.addfavor, R.id.addcomment, R.id.addZan})
    public void clickAction(View v) {
        switch (v.getId()) {
            case R.id.addfavor:
                clickFavor();
                break;
            case R.id.addcomment:
                clickComment();
                break;
            case R.id.addZan:
                clickZan();
                break;
        }
    }

    /**
     * 点击收藏
     */
    public void clickFavor() {
        if (ac.isLogin(this, "refreshListView")) {
            toggleFavor();
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    public void refreshListView() {
        if (listViewHelper != null) {
            listViewHelper.refresh();
        }
    }

    /**
     * 点击评论
     */
    public void clickComment() {
        if (ac.isLogin(this, "refreshListView")) {
            if (addCommentBarDialog == null) {
                addCommentBarDialog = new AddCommentBarDialog(_activity);
                addCommentBarDialog.setOnSendClickListener(this);
            }
            addCommentBarDialog.show();
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    /**
     * 点击赞
     */
    public void clickZan() {
        if (ac.isLogin(this, "refreshListView")) {
            toggleZan();
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    /**
     * 收藏/取消收藏
     */
    private void toggleFavor() {
        final int favorCount = Integer.parseInt(bean.getCollectCount());
        if (HAS_FAVOR.equals(bean.getIsCollected())) {
            if (favorCount > 0) {
                bean.setCollectCount(favorCount - 1 + "");
                bean.setIsCollected(NOT_FAVOR);
                ApiClient.getApi().collect(new ApiCallbackAdapter() {
                    @Override
                    protected void onApiError(String tag) {
                        bean.setCollectCount(favorCount + 1 + "");
                        bean.setIsCollected(HAS_FAVOR);
                        setBean(bean, position);
                    }

                    ;

                    public void onApiSuccess(Result res, String tag) {
                        if (res.isOK()) {
                            ac.setProperty(Const.USER_COLLECT_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_COLLECT_COUNT)) - 1) + "");
                        }
                    }

                    ;
                }, ac.getLoginUid(), bean.getId(), "1");// （1（动态）、2（活动）、3（群组）
            }
        } else if (NOT_FAVOR.equals(bean.getIsCollected())) {
            animIn(addFavorBig);
            bean.setCollectCount(favorCount + 1 + "");
            bean.setIsCollected(HAS_FAVOR);
            ApiClient.getApi().collect(new ApiCallbackAdapter() {
                @Override
                protected void onApiError(String tag) {
                    bean.setCollectCount(favorCount - 1 + "");
                    bean.setIsCollected(NOT_FAVOR);
                    setBean(bean, position);
                }

                ;

                public void onApiSuccess(Result res, String tag) {
                    if (res.isOK()) {
                        ac.setProperty(Const.USER_COLLECT_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_COLLECT_COUNT)) + 1) + "");
                    }
                }

                ;
            }, ac.getLoginUid(), bean.getId(), "1");// （1（动态）、2（活动）、3（群组）
        }

        setBean(bean, position);
    }

    /**
     * 点赞/取消赞
     */
    private void toggleZan() {
        final int zanCount = Integer.parseInt(bean.getLikeCount());
        if (HAS_ZAN.equals(bean.getIsLiked())) {
            if (zanCount > 0) {
                bean.setLikeCount(zanCount - 1 + "");
                bean.setIsLiked(NOT_ZAN);
                ApiClient.getApi().like(new ApiCallbackAdapter() {
                    @Override
                    protected void onApiError(String tag) {
                        bean.setLikeCount(zanCount + 1 + "");
                        bean.setIsLiked(HAS_ZAN);
                        setBean(bean, position);
                    }

                    ;
                }, ac.getLoginUid(), bean.getId(), "2");// 1(活动)、2（动态）
            }
        } else if (NOT_ZAN.equals(bean.getIsLiked())) {
            animIn(addZanBig);
            bean.setLikeCount(zanCount + 1 + "");
            bean.setIsLiked(HAS_ZAN);
            ApiClient.getApi().like(new ApiCallbackAdapter() {
                @Override
                protected void onApiError(String tag) {
                    bean.setLikeCount(zanCount - 1 + "");
                    bean.setIsLiked(NOT_ZAN);
                    setBean(bean, position);
                }

                ;
            }, ac.getLoginUid(), bean.getId(), "2");// 1(活动)、2（动态）
        }

        setBean(bean, position);
    }

    /**
     * 动画进入
     *
     * @param v
     */
    private void animIn(final View v) {
        v.setVisibility(VISIBLE);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "scaleX", 0.8f, 1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, "scaleY", 0.8f, 1.0f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(v, "alpha", 0.7f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animatorX, animatorY, animatorAlpha);
        set.setDuration(300).start();
        set.playSequentially(set);
        set.addListener(new AnimatorAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                super.onAnimationEnd(arg0);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        v.setVisibility(INVISIBLE);
                    }
                }, 300);
            }
        });
    }

    @Override
    public void onSendClick(String content) {
        this.tempContent = content;
        if (TextUtils.isEmpty(content)) {
            AppContext.showToast("请输入评论内容");
            return;
        }
        ApiClient.getApi().comment(this, ac.getLoginUid(), bean.getId(), null, "2", content);
    }

    /**
     * 添加新评论到页面
     *
     * @param commentId
     */
    private void addNewComment(String commentId) {
        // 组装新条目
        Comment newComment = new Comment();
        newComment.setNickname(ac.getProperty(Const.USER_NICKNAME));
        newComment.setRemark(newComment.getNickname());
        newComment.setComment(tempContent);
        newComment.setCreateTime(Func.getNow());
        newComment.setHeadSPicName(ac.getProperty(Const.USER_SHEAD));
        newComment.setId(commentId);
        newComment.setUserId(ac.getLoginUid());
        newComment.setItemViewType(DynamicDetailActivity.TPL_COMMENT);

        for (int i = 0; i < data.size(); i++) {
            Base obj = (Base) data.get(i);
            // 如果为“没有评论”条目
            if (obj.getItemViewType() == DynamicDetailActivity.TPL_COMMENT_EMPTY) {
                // 移除“没有评论条目”
                data.remove(obj);
                // 添加新条目
                data.add(i, newComment);
                break;
            }
            // 如果为“评论”条目
            if (obj.getItemViewType() == DynamicDetailActivity.TPL_COMMENT) {
                // 添加新条目
                data.add(i, newComment);
                break;
            }
        }
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
            AppContext.showToast("评论成功");
            // 隐藏评论条
            addCommentBarDialog.restore();
            addCommentBarDialog.dismiss();

            AddCommentBean addCommentBean = (AddCommentBean) res;
            // 更新评论数
            bean.setComCount((Integer.parseInt(bean.getComCount()) + 1) + "");
            // 在页面中上添加一条新评论
            addNewComment(addCommentBean.getCommentId());
            // 刷新界面
            adapter.notifyDataSetChanged();
        } else {
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        _activity.hideWaitDialog();
    }

}
