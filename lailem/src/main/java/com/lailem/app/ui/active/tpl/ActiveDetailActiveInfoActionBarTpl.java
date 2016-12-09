package com.lailem.app.ui.active.tpl;

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
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean.ActiveInfo;
import com.lailem.app.jsonbean.dynamic.AddCommentBean;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.active.ActiveDetailActivity;
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

public class ActiveDetailActiveInfoActionBarTpl extends BaseTpl<Object> implements OnSendClickListener {

    // 1（已赞）、2（未赞）
    public static final String HAS_ZAN = "1";
    public static final String NOT_ZAN = "2";
    public static final String HAS_FAVOR = "1";
    public static final String NOT_FAVOR = "2";

    @Bind(R.id.addfavor)
    TextView addFavor_tv;
    @Bind(R.id.addcomment)
    TextView addComment_tv;
    @Bind(R.id.addZan)
    TextView addZan_tv;
    private ObjectWrapper bean;
    private int position;

    private View addZanBig;
    private View addFavorBig;
    private ActiveInfo activeInfo;

    private AddCommentBarDialog addCommentBarDialog;
    private String tempContent;

    public ActiveDetailActiveInfoActionBarTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        addZanBig = View.inflate(_activity, R.layout.view_add_zan_big, null);
        addFavorBig = View.inflate(_activity, R.layout.view_add_favor_big, null);
        FrameLayout decorView = (FrameLayout) _activity.getWindow().getDecorView();
        FrameLayout.LayoutParams zanParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        zanParams.gravity = Gravity.CENTER;
        decorView.addView(addZanBig, zanParams);
        FrameLayout.LayoutParams favorParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        favorParams.gravity = Gravity.CENTER;
        decorView.addView(addFavorBig, favorParams);

        addZanBig.setVisibility(INVISIBLE);
        addFavorBig.setVisibility(INVISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_detail_actionbar;
    }

    @Override
    public void setBean(Object obj, int position) {
        this.bean = (ObjectWrapper) obj;
        this.position = position;
        this.activeInfo = (ActiveInfo) bean.getObject();
        addFavor_tv.setText(activeInfo.getCollectCount());
        addComment_tv.setText(activeInfo.getComCount());
        addZan_tv.setText(activeInfo.getLikeCount());

        if (HAS_ZAN.equals(activeInfo.getIsLiked())) {
            addZan_tv.setTextColor(getResources().getColor(R.color.orange));
            addZan_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addzan_orange), null, null, null);
        } else if (NOT_ZAN.equals(activeInfo.getIsLiked())) {
            addZan_tv.setTextColor(Color.parseColor("#999999"));
            addZan_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addzan), null, null, null);
        }
        if (HAS_FAVOR.equals(activeInfo.getIsCollected())) {
            addFavor_tv.setTextColor(getResources().getColor(R.color.orange));
            addFavor_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addfavor_orange), null, null, null);
        } else if (NOT_FAVOR.equals(activeInfo.getIsCollected())) {
            addFavor_tv.setTextColor(Color.parseColor("#999999"));
            addFavor_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_addfavor), null, null, null);
        }

    }

    @OnClick({R.id.addfavor_ll, R.id.addcomment_ll, R.id.addZan_ll, R.id.more_ll})
    public void clickAction(View v) {
        switch (v.getId()) {
            case R.id.addfavor_ll:
                clickFavor();
                break;
            case R.id.addcomment_ll:
                clickComment();
                break;
            case R.id.addZan_ll:
                clickZan();
                break;
            case R.id.more_ll:
                clickMore();
                break;
        }
    }

    private void clickMore() {
        //旧版
//        String title = activeInfo.getName() + "来了";
//        String content = activeInfo.getIntro();
//        String dataUrl = Const.ACTIVE_PATTERN + activeInfo.getId();
//        String imageUrl = ApiClient.getFileUrl(activeInfo.getbPicName());
//        String smsContent = activeInfo.getName() + "来了，" + activeInfo.getIntro() + "，活动详情： " + dataUrl;
//        String emailContent = activeInfo.getName() + "来了，" + activeInfo.getIntro() + "，活动详情： " + dataUrl;
//        String emailTopic = activeInfo.getName() + "来了";
//        UIHelper.showShare(_activity, title, content, dataUrl, imageUrl, smsContent, emailContent, emailTopic);

        String title = activeInfo.getName();
        String content = activeInfo.getName() + "，时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress();
        String dataUrl = Const.ACTIVE_PATTERN + activeInfo.getId();
        String imageUrl = ApiClient.getFileUrl(activeInfo.getbPicName());
        String smsContent = "我在【来了】上创建了活动：" + activeInfo.getName() + ",时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + "，活动详情：" + Const.ACTIVE_PATTERN + activeInfo.getId() + "，快给我点个赞吧！有兴趣也可以报名参加哦！";
        String emailContent = "我在【来了】上创建了活动：" + activeInfo.getName() + ",时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + "，活动详情：" + Const.ACTIVE_PATTERN + activeInfo.getId() + "，快给我点个赞吧！有兴趣也可以报名参加哦！";
        String emailTopic = activeInfo.getName();
        String weixinMomentTitle = activeInfo.getName();
        String weiboContent = activeInfo.getName() + "，时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + Const.ACTIVE_PATTERN + activeInfo.getId();
        UIHelper.showShare(_activity, title, content, dataUrl, imageUrl, smsContent, emailContent, emailTopic, weixinMomentTitle, weiboContent);
    }

    public void refreshListView() {
        if (listViewHelper != null) {
            listViewHelper.refresh();
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
        final int favorCount = Integer.parseInt(activeInfo.getCollectCount());
        if (HAS_FAVOR.equals(activeInfo.getIsCollected())) {
            if (favorCount > 0) {
                activeInfo.setCollectCount(favorCount - 1 + "");
                activeInfo.setIsCollected(NOT_FAVOR);
                ApiClient.getApi().collect(new ApiCallbackAdapter() {
                    @Override
                    protected void onApiError(String tag) {
                        activeInfo.setCollectCount(favorCount + 1 + "");
                        activeInfo.setIsCollected(HAS_FAVOR);
                        setBean(bean, position);
                    }

                    ;

                    public void onApiSuccess(Result res, String tag) {
                        if (res.isOK()) {
                            ac.setProperty(Const.USER_COLLECT_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_COLLECT_COUNT)) - 1) + "");
                        }
                    }

                    ;
                }, ac.getLoginUid(), activeInfo.getId(), "2");// （1（动态）、2（活动）、3（群组）
            }
        } else if (NOT_FAVOR.equals(activeInfo.getIsCollected())) {
            animIn(addFavorBig);
            activeInfo.setCollectCount(favorCount + 1 + "");
            activeInfo.setIsCollected(HAS_FAVOR);
            ApiClient.getApi().collect(new ApiCallbackAdapter() {
                @Override
                protected void onApiError(String tag) {
                    activeInfo.setCollectCount(favorCount - 1 + "");
                    activeInfo.setIsCollected(NOT_FAVOR);
                    setBean(bean, position);
                }

                ;

                public void onApiSuccess(Result res, String tag) {
                    if (res.isOK()) {
                        ac.setProperty(Const.USER_COLLECT_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_COLLECT_COUNT)) + 1) + "");
                    }
                }

                ;
            }, ac.getLoginUid(), activeInfo.getId(), "2");// （1（动态）、2（活动）、3（群组）
        }

        setBean(bean, position);
    }

    /**
     * 点赞/取消赞
     */
    private void toggleZan() {
        final int zanCount = Integer.parseInt(activeInfo.getLikeCount());
        if (HAS_ZAN.equals(activeInfo.getIsLiked())) {
            if (zanCount > 0) {
                activeInfo.setLikeCount(zanCount - 1 + "");
                activeInfo.setIsLiked(NOT_ZAN);
                ApiClient.getApi().like(new ApiCallbackAdapter() {
                    @Override
                    protected void onApiError(String tag) {
                        activeInfo.setLikeCount(zanCount + 1 + "");
                        activeInfo.setIsLiked(HAS_ZAN);
                        setBean(bean, position);
                    }

                    ;
                }, ac.getLoginUid(), activeInfo.getId(), "1");// 1(活动)、2（动态）
            }
        } else if (NOT_ZAN.equals(activeInfo.getIsLiked())) {
            animIn(addZanBig);
            activeInfo.setLikeCount(zanCount + 1 + "");
            activeInfo.setIsLiked(HAS_ZAN);
            ApiClient.getApi().like(new ApiCallbackAdapter() {
                @Override
                protected void onApiError(String tag) {
                    activeInfo.setLikeCount(zanCount - 1 + "");
                    activeInfo.setIsLiked(NOT_ZAN);
                    setBean(bean, position);
                }

                ;
            }, ac.getLoginUid(), activeInfo.getId(), "1");// 1(活动)、2（动态）
        }

        setBean(bean, position);
    }

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
        ApiClient.getApi().comment(this, ac.getLoginUid(), activeInfo.getId(), null, "1", content);
    }

    /**
     * 添加新评论到页面
     *
     * @param commentId
     */
    private void addNewComment(String commentId) {
//        // 组装新条目
//        Comment newComment = new Comment();
//        newComment.setNickname(ac.getProperty(Const.USER_NICKNAME));
//        newComment.setRemark(newComment.getNickname());
//        newComment.setComment(tempContent);
//        newComment.setCreateTime(Func.getNow());
//        newComment.setHeadSPicName(ac.getProperty(Const.USER_SHEAD));
//        newComment.setId(commentId);
//        newComment.setUserId(ac.getLoginUid());
//        newComment.setItemViewType(ActiveDetailActivity.TPL_COMMENT);
//
//        for (int i = 0; i < data.size(); i++) {
//            Base obj = (Base) data.get(i);
//            // 如果为“没有评论”条目
//            if (obj.getItemViewType() == ActiveDetailActivity.TPL_COMMENT_EMPTY) {
//                // 移除“没有评论条目”
//                data.remove(obj);
//                // 添加新条目
//                data.add(i, newComment);
//                break;
//            }
//            // 如果为“评论”条目
//            if (obj.getItemViewType() == ActiveDetailActivity.TPL_COMMENT) {
//                // 添加新条目
//                data.add(i, newComment);
//                break;
//            }
//        }
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

            // 更新评论数
            activeInfo.setComCount((Integer.parseInt(activeInfo.getComCount()) + 1) + "");
            // 如果当前为显示评论列表
            AddCommentBean addCommentBean = (AddCommentBean) res;
            if (activeInfo.getRoleType().equals(Const.ROLE_TYPE_STRANGER)) {
                addNewComment(addCommentBean.getCommentId());
            }
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
