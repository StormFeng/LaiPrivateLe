package com.lailem.app.ui.active.tpl;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean.ActiveInfo;
import com.lailem.app.jsonbean.dynamic.AddCommentBean;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.jsonbean.dynamic.Comment.ToCommentInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.active.ActiveDetailActivity;
import com.lailem.app.ui.chat.expression.ExpressionUtil;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.lailem.app.widget.AddCommentBarDialog;
import com.lailem.app.widget.AddCommentBarDialog.OnSendClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ActiveDetailCommentTpl extends BaseTpl<Object> implements OnSendClickListener {
    public static final String API_TAG_DELETECOMMENT = "deleteComment";
    public static final String API_TAG_COMMENT = "comment";

    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.date)
    TextView date_tv;

    private Comment bean;
    private ActionDialog dialog;
    private AddCommentBarDialog addCommentBarDialog;
    private String tempContent;

    public ActiveDetailCommentTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_detail_comment;
    }

    @Override
    public void setBean(Object obj, int position) {
        this.bean = (Comment) obj;
        if (Func.checkImageTag(bean.getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(bean.getRemark());
        ToCommentInfo toCommentInfo = bean.getToCommentInfo();
        SpannableString comment = ExpressionUtil.getInstace().getExpressionString(bean.getComment());
        content_tv.setText("");
        if (toCommentInfo != null && !TextUtils.isEmpty(toCommentInfo.getNickname())) {
            Spanned toInfo = Html.fromHtml("回复 <font color='#909090'>" + toCommentInfo.getRemark() + "</font> ：");
            content_tv.append(toInfo);
        }
        content_tv.append(comment);
        date_tv.setText(Func.formatTime(bean.getCreateTime()));
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        clickComment();
    }

    public void refreshListView() {
        if (listViewHelper != null) {
            listViewHelper.refresh();
        }
    }

    /**
     * 点击评论条目
     */
    public void clickComment() {
        if (ac.isLogin(this, "refreshListView")) {
            // 如果为本人评论
            if (bean.getUserId().equals(ac.getLoginUid())) {
                if (dialog == null) {
                    dialog = new ActionDialog(_activity);
                    ActionData actionData = new ActionData("删除", R.drawable.ic_dialog_ok_selector);
                    ArrayList<ActionData> actionDatas = new ArrayList<ActionData>();
                    actionDatas.add(actionData);
                    DialogActionData dialogActionData = new DialogActionData(null, null, actionDatas);
                    dialog.init(dialogActionData);
                    dialog.setOnActionClickListener(new OnActionClickListener() {

                        @Override
                        public void onActionClick(ActionDialog dialog, View View, int position) {
                            ApiClient.getApi().deleteComment(ActiveDetailCommentTpl.this, ac.getLoginUid(), bean.getId());
                        }
                    });
                }
                dialog.show();
            } else {
                if (addCommentBarDialog == null) {
                    addCommentBarDialog = new AddCommentBarDialog(_activity);
                    addCommentBarDialog.setOnSendClickListener(this);
                }
                addCommentBarDialog.setHint("回复" + bean.getRemark() + "：");
                addCommentBarDialog.show();
            }
        } else {
            UIHelper.showLogin(_activity, false);
        }
    }

    @Override
    public void onSendClick(String content) {
        this.tempContent = content;
        if (TextUtils.isEmpty(content)) {
            AppContext.showToast("请输入评论内容");
            return;
        }
        ApiClient.getApi().comment(this, ac.getLoginUid(), _activity.getIntent().getExtras().getString(Const.BUNDLE_KEY_GROUP_ID), bean.getId(), "1", content);
    }

    /**
     * 添加新评论到页面,并更新评论数
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
        newComment.setItemViewType(ActiveDetailActivity.TPL_COMMENT);
        ToCommentInfo toCommentInfo = new ToCommentInfo();
        toCommentInfo.setComment(bean.getComment());
        toCommentInfo.setHeadSPicName(bean.getHeadSPicName());
        toCommentInfo.setId(bean.getId());
        toCommentInfo.setNickname(bean.getRemark());
        toCommentInfo.setRemark(toCommentInfo.getNickname());
        toCommentInfo.setUserId(bean.getUserId());
        newComment.setToCommentInfo(toCommentInfo);

        for (int i = 0; i < data.size(); i++) {
            Base obj = (Base) data.get(i);
            if (obj.getItemViewType() == ActiveDetailActivity.TPL_COMMENT_HEADER) {
                ActiveInfo activeInfo = (ActiveInfo) ((ObjectWrapper) obj).getObject();
                activeInfo.setComCount((Integer.parseInt(activeInfo.getComCount()) + 1) + "");
                data.add(i + 1, newComment);
                break;
            }
        }
    }

    @OnClick({R.id.avatar, R.id.name})
    public void clickAvatarOrName() {
        UIHelper.showMemberInfoAlone(_activity, bean.getUserId());
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
            if (API_TAG_COMMENT.equals(tag)) {// 添加评论
                AppContext.showToast("评论成功");
                // 隐藏评论条
                addCommentBarDialog.restore();
                addCommentBarDialog.dismiss();

                AddCommentBean addCommentBean = (AddCommentBean) res;
                // 在页面中上添加一条新评论，同时更新评论数
                addNewComment(addCommentBean.getCommentId());
                // 刷新界面
                adapter.notifyDataSetChanged();
            } else if (API_TAG_DELETECOMMENT.equals(tag)) {// 删除评论
                // 删除当前评论
                data.remove(bean);
                // 更新评论数,同时判断评论是否为空
                for (int i = 0; i < data.size(); i++) {
                    Base obj = (Base) data.get(i);
                    // 如果为点赞，评论，收藏条目
                    if (obj.getItemViewType() == ActiveDetailActivity.TPL_COMMENT_HEADER) {
                        ActiveInfo activeInfo = (ActiveInfo) ((ObjectWrapper) obj).getObject();
                        int newComCount = (Integer.parseInt(activeInfo.getComCount()) - 1);
                        activeInfo.setComCount(newComCount + "");
                        break;
                    }
                }
                // 刷新界面
                adapter.notifyDataSetInvalidated();
            }
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
