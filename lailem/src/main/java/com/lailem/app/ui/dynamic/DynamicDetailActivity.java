package com.lailem.app.ui.dynamic;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.datasource.DynamicDetailDataSource;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.dynamic.AddCommentBean;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.tpl.CommentEmptyTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailActionBarTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailAddressTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailAvatarTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailCommentTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailGapTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailImageTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailLineTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailNewAddressTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailNewVoiceTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailNoticeTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailScheduleTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailSingleImageTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailSingleVideoTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailTextTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailVideoImageTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailVideoTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailVoiceTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicDetailVoteTpl;
import com.lailem.app.ui.dynamic.tpl.DynamicZanListTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.AddCommentBarDialog;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.ZanListDialog;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DynamicDetailActivity extends BaseListActivity<Object> {

    public static final int TPL_AVATAR = 0;
    public static final int TPL_TEXT = 1;
    public static final int TPL_IMAGE = 2;
    public static final int TPL_ACTIONBAR = 3;
    public static final int TPL_VOICE = 4;
    public static final int TPL_VIDEO = 5;
    public static final int TPL_VOTE = 6;
    public static final int TPL_ADDRESS = 7;
    public static final int TPL_SCHEDULE = 8;
    public static final int TPL_LINE = 9;
    public static final int TPL_ZANLIST = 10;
    public static final int TPL_COMMENT = 11;
    public static final int TPL_COMMENT_EMPTY = 12;
    public static final int TPL_NOTICE = 13;
    public static final int TPL_NEW_ADDRESS = 14;//新版地址类型
    public static final int TPL_NEW_VOICE = 15;//新版语音播放
    public static final int TPL_VIDEOIMAGE = 16;//新版视频图片九宫格类型
    public static final int TPL_GAP = 17;//分割
    public static final int TPL_SINGLE_IMAGE = 18;//单个图片
    public static final int TPL_SINGLE_VIDEO = 19;//单个视频

    public static final String BUNDLE_KEY_COLLECT_COUNT = "collect_count";
    public static final String BUNDLE_KEY_ZAN_COUNT = "zan_count";
    public static final String BUNDLE_KEY_COMMENT_COUNT = "comment_count";
    public static final String BUNDLE_KEY_IS_COLLECTED = "is_collected";
    public static final String BUNDLE_KEY_IS_LIKED = "is_liked";
    public static final String BUNDLE_KEY_POSITION = "position";

    public static final String BUNDLE_KEY_COMMENT = "comment";

    public static final int REQUEST_DYNAMIC = 3000;

    @Bind(R.id.topbar)
    TopBarView topbar;

    private ActionDialog dialog;
    private int tempPosition;
    private DynamicBean dynamicInfo;
    private AddCommentBarDialog addCommentBarDialog;
    private String tempContent;
    private Comment pendingComment;//待评论对象
    private boolean isFirstRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();

    }

    private void initView() {
        topbar.setTitle("动态详情");
        topbar.setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        topbar.setRightImageButton(R.drawable.ic_topbar_share, new OnClickListener() {


            @Override
            public void onClick(View v) {
                Func.shareDynamic(_activity, dynamicInfo);
            }
        });
        topbar.hideRight_ib();
        listView.setDividerHeight(0);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new DynamicDetailDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_DYNAMIC_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_AVATAR, DynamicDetailAvatarTpl.class);
        tpls.add(TPL_TEXT, DynamicDetailTextTpl.class);
        tpls.add(TPL_IMAGE, DynamicDetailImageTpl.class);
        tpls.add(TPL_ACTIONBAR, DynamicDetailActionBarTpl.class);
        tpls.add(TPL_VOICE, DynamicDetailVoiceTpl.class);
        tpls.add(TPL_VIDEO, DynamicDetailVideoTpl.class);
        tpls.add(TPL_VOTE, DynamicDetailVoteTpl.class);
        tpls.add(TPL_ADDRESS, DynamicDetailAddressTpl.class);
        tpls.add(TPL_SCHEDULE, DynamicDetailScheduleTpl.class);
        tpls.add(TPL_LINE, DynamicDetailLineTpl.class);
        tpls.add(TPL_ZANLIST, DynamicZanListTpl.class);
        tpls.add(TPL_COMMENT, DynamicDetailCommentTpl.class);
        tpls.add(TPL_COMMENT_EMPTY, CommentEmptyTpl.class);
        tpls.add(TPL_NOTICE, DynamicDetailNoticeTpl.class);
        tpls.add(TPL_NEW_ADDRESS, DynamicDetailNewAddressTpl.class);
        tpls.add(TPL_NEW_VOICE, DynamicDetailNewVoiceTpl.class);
        tpls.add(TPL_VIDEOIMAGE, DynamicDetailVideoImageTpl.class);
        tpls.add(TPL_GAP, DynamicDetailGapTpl.class);
        tpls.add(TPL_SINGLE_IMAGE, DynamicDetailSingleImageTpl.class);
        tpls.add(TPL_SINGLE_VIDEO, DynamicDetailSingleVideoTpl.class);
        return tpls;
    }

    @Override
    public void onEndRefresh(final IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
        super.onEndRefresh(adapter, result);
        if (resultList.size() == 0) {
            return;
        }
        topbar.showRight_ib();
        DynamicDetailDataSource ds = (DynamicDetailDataSource) this.dataSource;
        dynamicInfo = ds.getDynamicInfo();


        if (!isFirstRefresh) {
            return;
        }
        isFirstRefresh = false;

        Object object = _Bundle.getSerializable(BUNDLE_KEY_COMMENT);
        if (object == null) {
            return;
        }
        this.pendingComment = (Comment) object;
        //根据情况显示评论回复对话框
        if (addCommentBarDialog == null) {
            addCommentBarDialog = new AddCommentBarDialog(_activity);
            addCommentBarDialog.setOnSendClickListener(new AddCommentBarDialog.OnSendClickListener() {

                @Override
                public void onSendClick(String content) {
                    tempContent = content;
                    if (TextUtils.isEmpty(content)) {
                        AppContext.showToast("请输入评论内容");
                        return;
                    }
                    ApiClient.getApi().comment(new ApiCallbackAdapter() {
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
                                // 添加评论
                                AppContext.showToast("评论成功");
                                // 隐藏评论条
                                addCommentBarDialog.restore();
                                addCommentBarDialog.dismiss();

                                AddCommentBean addCommentBean = (AddCommentBean) res;
                                // 在页面中上添加一条新评论，同时更新评论数
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
                    }, ac.getLoginUid(), _activity.getIntent().getExtras().getString(Const.BUNDLE_KEY_DYNAMIC_ID), pendingComment.getId(), "2", content);
                }
            });
        }
        addCommentBarDialog.setHint("回复" + Func.formatNickName(_activity, pendingComment.getUserId(), pendingComment.getNickname()) + "：");
        addCommentBarDialog.show();

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
        newComment.setItemViewType(DynamicDetailActivity.TPL_COMMENT);
        Comment.ToCommentInfo toCommentInfo = new Comment.ToCommentInfo();
        toCommentInfo.setComment(pendingComment.getComment());
        toCommentInfo.setHeadSPicName(pendingComment.getHeadSPicName());
        toCommentInfo.setId(pendingComment.getId());
        toCommentInfo.setNickname(Func.formatNickName(_activity, pendingComment.getUserId(), pendingComment.getNickname()));
        toCommentInfo.setUserId(pendingComment.getUserId());
        newComment.setToCommentInfo(toCommentInfo);

        for (int i = 0; i < resultList.size(); i++) {
            Base obj = (Base) resultList.get(i);
            // 如果为点赞，评论，收藏条目
            if (obj.getItemViewType() == DynamicDetailActivity.TPL_ACTIONBAR) {
                DynamicBean dynamicBean = (DynamicBean) obj;
                dynamicBean.setComCount((Integer.parseInt(dynamicBean.getComCount()) + 1) + "");
            }
            // 如果为“没有评论”条目
            if (obj.getItemViewType() == DynamicDetailActivity.TPL_COMMENT_EMPTY) {
                // 移除“没有评论条目”
                resultList.remove(obj);
                // 添加新条目jc
                resultList.add(i, newComment);
                break;
            }
            // 如果为“评论”条目
            if (obj.getItemViewType() == DynamicDetailActivity.TPL_COMMENT) {
                // 添加新条目
                resultList.add(i, newComment);
                break;
            }
        }
    }

    @Override
    public void finish() {
        if (_Bundle.getInt(BUNDLE_KEY_POSITION) > 0) {
            if (resultList != null && resultList.size() > 0) {
                for (int i = 0; i < resultList.size(); i++) {
                    Object object = resultList.get(i);
                    if (object instanceof DynamicBean) {
                        DynamicBean dynamicInfo = (DynamicBean) object;
                        Bundle bundle = new Bundle();
                        bundle.putInt(BUNDLE_KEY_POSITION, _Bundle.getInt(BUNDLE_KEY_POSITION));
                        bundle.putString(BUNDLE_KEY_IS_COLLECTED, dynamicInfo.getIsCollected());
                        bundle.putString(BUNDLE_KEY_IS_LIKED, dynamicInfo.getIsLiked());
                        bundle.putString(BUNDLE_KEY_COLLECT_COUNT, dynamicInfo.getCollectCount());
                        bundle.putString(BUNDLE_KEY_COMMENT_COUNT, dynamicInfo.getComCount());
                        bundle.putString(BUNDLE_KEY_ZAN_COUNT, dynamicInfo.getLikeCount());
                        setResult(RESULT_OK, bundle);
                        break;
                    }
                }
            }
        }
        super.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Base base = (Base) resultList.get(position);
        if (base.getItemViewType() == TPL_ZANLIST) {
            ZanListDialog dialog = new ZanListDialog(this);
            DynamicDetailDataSource ds = (DynamicDetailDataSource) dataSource;
            DynamicBean dynamicBean = ds.getDynamicInfo();
            dialog.setParams(_Bundle.getString(Const.BUNDLE_KEY_DYNAMIC_ID), "2", dynamicBean.getLikeCount());
            dialog.show();
        }
    }

}
