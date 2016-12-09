package com.lailem.app.ui.active.tpl;

import android.content.Context;
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
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean;
import com.lailem.app.jsonbean.dynamic.AddCommentBean;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.active.ActiveInfoDetailActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.AddCommentBarDialog;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by XuYang on 15/12/4.
 */
public class ActiveInfoDetailCommentHeaderTpl extends BaseTpl<Object> implements AddCommentBarDialog.OnSendClickListener {

    @Bind(R.id.commentCount)
    TextView commentCount_tv;
    @Bind(R.id.commentAvatar)
    ImageView commentAvatar_iv;
    private ActiveInfoBean.ActiveInfo bean;

    private AddCommentBarDialog addCommentBarDialog;
    private String tempContent;

    public ActiveInfoDetailCommentHeaderTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_info_detail_comment_header;
    }

    @Override
    public void setBean(Object wrapper, int position) {
        this.bean = (ActiveInfoBean.ActiveInfo) ((ObjectWrapper) wrapper).getObject();
        commentCount_tv.setText("(" + bean.getComCount() + ")");
        if (ac.isLogin()) {
            String headUrl = ApiClient.getFileUrl(ac.getProperty(Const.USER_SHEAD));
            if (Func.checkImageTag(headUrl, commentAvatar_iv)) {
                Glide.with(_activity).load(headUrl).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(commentAvatar_iv);
            }
        }
    }


    @OnClick({R.id.addcomment, R.id.addFace})
    public void clickAction(View v) {
        switch (v.getId()) {
            case R.id.addcomment:
                addComment(AddCommentBarDialog.STATE_NORMAL);
                break;
            case R.id.addFace:
                addComment(AddCommentBarDialog.STATE_FACE);
                break;
        }
    }

    public void refreshListView() {
        if (listViewHelper != null) {
            listViewHelper.refresh();
        }
    }

    private void addComment(final int type) {
        if (ac.isLogin(this, "refreshListView")) {
            if (addCommentBarDialog == null) {
                addCommentBarDialog = new AddCommentBarDialog(_activity);
                addCommentBarDialog.setOnSendClickListener(this);
            }
            addCommentBarDialog.setState(type);
            addCommentBarDialog.show();
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
        ApiClient.getApi().comment(this, ac.getLoginUid(), bean.getId(), null, "1", content);
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
        newComment.setItemViewType(ActiveInfoDetailActivity.TPL_COMMENT);

        for (int i = 0; i < data.size(); i++) {
            Base obj = (Base) data.get(i);
            // 如果为“没有评论”条目
            if (obj.getItemViewType() == ActiveInfoDetailActivity.TPL_COMMENT_EMPTY) {
                // 移除“没有评论条目”
                data.remove(obj);
                // 添加新条目
                data.add(i, newComment);
                break;
            }
            // 如果为“评论”条目
            if (obj.getItemViewType() == ActiveInfoDetailActivity.TPL_COMMENT) {
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
            // 在页面中上添加一条新评论
            addNewComment(addCommentBean.getCommentId());
            // 更新评论数
            bean.setComCount((Integer.parseInt(bean.getComCount()) + 1) + "");
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
