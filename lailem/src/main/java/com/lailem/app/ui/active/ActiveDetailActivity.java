package com.lailem.app.ui.active;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ActiveDetailDataSource;
import com.lailem.app.api.ApiCallback;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseZoomListActivity;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean.ActiveInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.activegroup.GroupVerifyWayBean;
import com.lailem.app.jsonbean.dynamic.AddCommentBean;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.share_ex.weibo.WeiboLoginManager;
import com.lailem.app.ui.active.tpl.ActiveDetailActionBarTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailAddressTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailAvatarTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailCommentHeaderTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailCommentTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailDynamicHeader;
import com.lailem.app.ui.active.tpl.ActiveDetailForCreatorTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailForMemberTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailForPublicTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailGapTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailImageTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailLineTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailNewAddressTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailNewVoiceTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailNoticeTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailScheduleTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailSingleImageTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailSingleVideoTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailTextTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailVideoImageTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailVideoTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailVoiceTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailVoteTpl;
import com.lailem.app.ui.active.tpl.ActiveDetailZanListTpl;
import com.lailem.app.ui.create_old.MaterialListActivity;
import com.lailem.app.ui.create_old.MaterialListTwoActivity;
import com.lailem.app.ui.dynamic.DynamicDetailActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActiveDetailActionDialog;
import com.lailem.app.widget.AddCommentBarDialog;
import com.lailem.app.widget.AddDynamicDialog;
import com.lailem.app.widget.ConfirmDialog;
import com.lailem.app.widget.MaterialChooseDialog;
import com.lailem.app.widget.MaterialChooseDialog.onMaterialChooseListener;
import com.lailem.app.widget.PullToZoomListView;
import com.lailem.app.widget.ZanListDialog;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ActiveDetailActivity extends BaseZoomListActivity<Object> implements OnPhotoListener {
    public static final String API_TAG_APPLY = "apply";
    public static final String API_TAG_SET_PIC = "setPic";
    public static final String API_TAG_VERIFY_WAY = "verifyWay";

    public static final String BUNDLE_KEY_COMMENT = "comment";
    public static final int REQUEST_CODE = 2000;


    public static final int TPL_DETAIL_FOR_MEMBER = 0;
    public static final int TPL_DETAIL_FOR_CREATOR = 1;
    public static final int TPL_DETAIL_FOR_PUBLIC = 2;
    public static final int TPL_DYNAMIS_HEADER = 3;
    public static final int TPL_ZAN_LIST = 4;
    public static final int TPL_COMMENT_HEADER = 5;
    public static final int TPL_COMMENT = 6;
    public static final int TPL_AVATAR = 7;
    public static final int TPL_LINE = 8;
    public static final int TPL_ACTIONBAR = 9;
    public static final int TPL_TEXT = 10;
    public static final int TPL_IMAGE = 11;
    public static final int TPL_VOICE = 12;
    public static final int TPL_VIDEO = 13;
    public static final int TPL_VOTE = 14;
    public static final int TPL_ADDRESS = 15;
    public static final int TPL_SCHEDULE = 16;
    public static final int TPL_NOTICE = 17;
    public static final int TPL_NEW_ADDRESS = 18;
    public static final int TPL_NEW_VOICE = 19;
    public static final int TPL_VIDEOIMAGE = 20;
    public static final int TPL_GAP = 21;
    public static final int TPL_SINGLE_IMAGE = 22;
    public static final int TPL_SINGLE_VIDEO = 23;


    @Bind(R.id.topbar)
    View topbar;
    @Bind(R.id.topbarMore)
    ImageView topbarMore_iv;
    @Bind(R.id.topbarMore2)
    ImageView topbarMore2_iv;
    @Bind(R.id.topbarProgressBar)
    ProgressBar topbarProgressBar;
    @Bind(R.id.topbarTitle)
    TextView topbarTitle_tv;
    @Bind(R.id.applyActive)
    View applyActive;
    @Bind(R.id.applyActiveTv)
    TextView applyActive_tv;
    @Bind(R.id.add)
    View add;
    @Bind((R.id.ic_add))
    View icAdd;
    @Bind(R.id.bg_add)
    View bgAdd;


    private ActiveInfo activeInfo;
    private Comment pendingComment;
    private AddCommentBarDialog addCommentBarDialog;
    private String tempContent;
    private boolean isFirstRefresh = true;
    private ImageView privateMark_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(this, this);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
    }

    @Override
    protected void setTranslucentStatus(boolean on) {

    }

    @OnClick({R.id.topbarBack_ib, R.id.topbarBack_tv})
    public void clickTopbarBack() {
        finish();
    }

    @OnClick({R.id.topbarMore, R.id.topbarMore2})
    public void clickTopbarMore() {
        ActiveDetailActionDialog dialog = new ActiveDetailActionDialog(this, activeInfo.getRoleType(), Func.isExpired(activeInfo.getStartTime()));
        dialog.setOnActionClickListener(new ActiveDetailActionDialog.OnActionClickListener() {
            @Override
            public void onClickReport() {
                UIHelper.showComplain(_activity, activeInfo.getId(), Const.COMPLAIN_TYPE_ACTIVE);
            }

            @Override
            public void onClickShare() {
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

            @Override
            public void onClickQuit() {
                final ApiCallback callback = new ApiCallbackAdapter() {
                    @Override
                    public void onApiStart(String tag) {
                        super.onApiStart(tag);
                        showWaitDialog();
                    }

                    @Override
                    public void onApiSuccess(Result res, String tag) {
                        super.onApiSuccess(res, tag);
                        hideWaitDialog();
                        if (res.isOK()) {
                            GroupUtils.exitActivity(_activity, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
                            UIHelper.showMainWithClearTop(_activity);
                        } else {
                            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
                        }
                    }

                    @Override
                    protected void onApiError(String tag) {
                        super.onApiError(tag);
                        hideWaitDialog();
                    }
                };
                if (Const.ROLE_TYPE_CREATOR.equals(activeInfo.getRoleType())) {
                    ConfirmDialog dialog = new ConfirmDialog(_activity, R.style.confirm_dialog).config("提示", "确定取消该活动吗", "确定", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ApiClient.getApi().disbandGroup(callback, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), null, null, null);
                        }
                    });
                    dialog.show();
                } else {
                    ConfirmDialog dialog = new ConfirmDialog(_activity, R.style.confirm_dialog).config("提示", "确定退出该活动吗", "确定", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ApiClient.getApi().quit(callback, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onClickSetting() {
                if (Const.ROLE_TYPE_CREATOR.equals(activeInfo.getRoleType())) {
                    ac.putShareObject(Const.SHAREOBJ_KEY_ACTIVE_INFO, activeInfo);
                    UIHelper.showActiveSetting(_activity, activeInfo.getId());
                }
            }
        });
        dialog.show();
    }

    @OnClick(R.id.applyActive)
    public void applyActive() {
        if (ac.isLogin(this, "refreshListView")) {
            ApiClient.getApi().verifyWay(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
        } else {
            UIHelper.showLogin(this, false);
        }
    }

    @OnClick(R.id.add)
    public void addDynamic() {
        AddDynamicDialog addDynamicDialog = new AddDynamicDialog(_activity, activeInfo.getId(), Const.GROUPTYPE_ACTIVITY, activeInfo.getRoleType(), "");
        addDynamicDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(icAdd, "hide", 1f, 0f);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float cVal = (float) valueAnimator.getAnimatedValue();
                        ViewHelper.setAlpha(icAdd, cVal);
                        ViewHelper.setAlpha(bgAdd, cVal);
                        ViewHelper.setRotation(icAdd, (1 - cVal) * 90);
                    }
                });
                anim.setDuration(300);
                anim.start();
            }
        });
        addDynamicDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(icAdd, "show", 0f, 1f);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float cVal = (float) valueAnimator.getAnimatedValue();
                        ViewHelper.setAlpha(icAdd, cVal);
                        ViewHelper.setAlpha(bgAdd, cVal);
                        ViewHelper.setRotation(icAdd, (1 - cVal) * 90);
                    }
                });
                anim.setDuration(300);
                anim.start();
            }
        });
        addDynamicDialog.showSelf();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_active_detail;
    }

    private void initView() {
        refreshListView.setPullRefreshEnabled(false);
        listView.setDividerHeight(0);
        listView.setAnimType(PullToZoomListView.ANIM_TYPE_ALPHA);
        listView.setVerticalScrollBarEnabled(false);
        final View header = View.inflate(_activity, R.layout.view_active_detail_header, null);
        privateMark_iv = (ImageView) header.findViewById(R.id.privateMark);
        listView.setOnScrollCallback(new PullToZoomListView.OnScrollCallback() {
            @Override
            public void onScrollCallback(int headerContainerBottom) {
                float rate = 1 - (headerContainerBottom - TDevice.dpToPixel(44f) - TDevice.getStatusBarHeight()) / (10.f / 16f * TDevice.getScreenWidth() - TDevice.dpToPixel(44f) - TDevice.getStatusBarHeight());
                if (rate < 0) {
                    rate = 0;
                }
                if (rate > 1) {
                    rate = 1;
                }
                int alpha = (int) (255.0f * rate);
                topbar.setBackgroundColor(Color.argb(alpha, 0x21, 0x29, 0x36));
                ViewHelper.setAlpha(topbarTitle_tv, rate);
                ViewHelper.setAlpha(topbarMore_iv, rate);
                ViewHelper.setAlpha(topbarMore2_iv, 1 - rate);
            }
        });
        listView.getHeaderContainer().addView(header, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new ActiveDetailDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_DETAIL_FOR_MEMBER, ActiveDetailForMemberTpl.class);
        tpls.add(TPL_DETAIL_FOR_CREATOR, ActiveDetailForCreatorTpl.class);
        tpls.add(TPL_DETAIL_FOR_PUBLIC, ActiveDetailForPublicTpl.class);
        tpls.add(TPL_DYNAMIS_HEADER, ActiveDetailDynamicHeader.class);
        tpls.add(TPL_ZAN_LIST, ActiveDetailZanListTpl.class);
        tpls.add(TPL_COMMENT_HEADER, ActiveDetailCommentHeaderTpl.class);
        tpls.add(TPL_COMMENT, ActiveDetailCommentTpl.class);
        tpls.add(TPL_AVATAR, ActiveDetailAvatarTpl.class);
        tpls.add(TPL_LINE, ActiveDetailLineTpl.class);
        tpls.add(TPL_ACTIONBAR, ActiveDetailActionBarTpl.class);
        tpls.add(TPL_TEXT, ActiveDetailTextTpl.class);
        tpls.add(TPL_IMAGE, ActiveDetailImageTpl.class);
        tpls.add(TPL_VOICE, ActiveDetailVoiceTpl.class);
        tpls.add(TPL_VIDEO, ActiveDetailVideoTpl.class);
        tpls.add(TPL_VOTE, ActiveDetailVoteTpl.class);
        tpls.add(TPL_ADDRESS, ActiveDetailAddressTpl.class);
        tpls.add(TPL_SCHEDULE, ActiveDetailScheduleTpl.class);
        tpls.add(TPL_NOTICE, ActiveDetailNoticeTpl.class);
        tpls.add(TPL_NEW_ADDRESS, ActiveDetailNewAddressTpl.class);
        tpls.add(TPL_NEW_VOICE, ActiveDetailNewVoiceTpl.class);
        tpls.add(TPL_VIDEOIMAGE, ActiveDetailVideoImageTpl.class);
        tpls.add(TPL_GAP, ActiveDetailGapTpl.class);
        tpls.add(TPL_SINGLE_IMAGE, ActiveDetailSingleImageTpl.class);
        tpls.add(TPL_SINGLE_VIDEO, ActiveDetailSingleVideoTpl.class);
        return tpls;
    }

    public void refreshListView() {
        if (listViewHelper != null) {
            listViewHelper.refresh();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        if (id == -1) {
            return;
        }
        Base item = (Base) resultList.get((int) id);
        Base lastItem = null;
        Base nextItem = null;
        if (id > 1) {
            lastItem = (Base) resultList.get((int) (id - 1));
        }
        if (id + 1 < resultList.size()) {
            nextItem = (Base) resultList.get((int) (id + 1));
        }
        switch (item.getItemViewType()) {
            case TPL_TEXT:
            case TPL_IMAGE:
            case TPL_VOICE:
            case TPL_VIDEO:
            case TPL_VOTE:
            case TPL_ADDRESS:
            case TPL_SCHEDULE:
            case TPL_LINE:
            case TPL_NOTICE:
            case TPL_NEW_ADDRESS:
            case TPL_NEW_VOICE:
            case TPL_SINGLE_IMAGE:
            case TPL_SINGLE_VIDEO:
            case TPL_VIDEOIMAGE:
                for (int i = (int) id; i < resultList.size(); i++) {
                    Object object = resultList.get(i);
                    if (object instanceof DynamicBean) {
                        UIHelper.showDynamicDetail(this, ((DynamicBean) object).getId(), position);
                        break;
                    }
                }
                break;
            case TPL_AVATAR:
                if (nextItem != null) {
                    for (int i = (int) id; i < resultList.size(); i++) {
                        Object object = resultList.get(i);
                        if (object instanceof DynamicBean) {
                            UIHelper.showDynamicDetail(this, ((DynamicBean) object).getId(), position);
                            break;
                        }
                    }
                }
                break;
            case TPL_ACTIONBAR:
                if (lastItem != null) {
                    UIHelper.showDynamicDetail(this, ((DynamicBean) item).getId(), position);
                }
                break;
            case TPL_ZAN_LIST:
                ZanListDialog dialog = new ZanListDialog(this);
                ActiveDetailDataSource ds = (ActiveDetailDataSource) dataSource;
                ActiveInfo activeInfo = ds.getActiveInfo();
                dialog.setParams(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), "1", activeInfo.getLikeCount());
                dialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);

        SsoHandler mSsoHandler = WeiboLoginManager.getSsoHandler();
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }


        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DynamicDetailActivity.REQUEST_DYNAMIC:
                    String collectCount = data.getExtras().getString(DynamicDetailActivity.BUNDLE_KEY_COLLECT_COUNT);
                    String comCount = data.getExtras().getString(DynamicDetailActivity.BUNDLE_KEY_COMMENT_COUNT);
                    String zanCount = data.getExtras().getString(DynamicDetailActivity.BUNDLE_KEY_ZAN_COUNT);
                    String isLiked = data.getExtras().getString(DynamicDetailActivity.BUNDLE_KEY_IS_LIKED);
                    String isCollected = data.getExtras().getString(DynamicDetailActivity.BUNDLE_KEY_IS_COLLECTED);

                    int position = data.getExtras().getInt(DynamicDetailActivity.BUNDLE_KEY_POSITION);
                    for (int i = position; i < resultList.size(); i++) {
                        Object object = resultList.get(i);
                        if (object instanceof DynamicBean) {
                            DynamicBean dynamicBean = (DynamicBean) object;
                            dynamicBean.setCollectCount(collectCount);
                            dynamicBean.setComCount(comCount);
                            dynamicBean.setLikeCount(zanCount);
                            dynamicBean.setIsLiked(isLiked);
                            dynamicBean.setIsCollected(isCollected);
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case MaterialListActivity.REQUEST_CODE_MATERIAL_TYPE:
                    Bundle bundle = data.getExtras();
                    String picId = bundle.getString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_ID);
                    String picName = bundle.getString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_SNAME);
                    String picBName = bundle.getString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_BNAME);
                    String squareSName = bundle.getString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_SQUARE_SNAME);
                    String bannerUrl = ApiClient.getFileUrl(picBName);
                    if (Func.checkImageTag(bannerUrl, listView.getHeaderView())) {
                        ac.imageLoader.loadImage(bannerUrl, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                listView.getHeaderView().setImageBitmap(loadedImage);
                            }
                        });
                    }
                    ApiClient.getApi().setPic(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), null, picId);
                    break;
                case ActiveSettingActivity.REQUEST_CODE:
                    if (data.getExtras().getBoolean(Const.BUNDLE_KEY_ACTIVE_INFO_CHANGED)) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    @Override
    public void onStartRefresh(IDataAdapter<ArrayList<Object>> adapter) {
        super.onStartRefresh(adapter);
        topbarProgressBar.setVisibility(View.VISIBLE);
        topbarMore_iv.setVisibility(View.GONE);
        topbarMore2_iv.setVisibility(View.GONE);
    }

    @Override
    public void onEndRefresh(final IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
        super.onEndRefresh(adapter, result);
        topbarProgressBar.setVisibility(View.INVISIBLE);
        if (resultList.size() == 0) {

            return;
        }
        topbarMore_iv.setVisibility(View.VISIBLE);
        topbarMore2_iv.setVisibility(View.VISIBLE);

        ActiveDetailDataSource ds = (ActiveDetailDataSource) this.dataSource;
        this.activeInfo = ds.getActiveInfo();
        if (Const.ROLE_TYPE_STRANGER.equals(this.activeInfo.getRoleType())) {
            applyActive.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
            applyActive.setBackgroundColor(Color.parseColor("#FF72CF74"));
            if (Const.APPLY_FLAY_STOP.equals(activeInfo.getApplyFlay())) {
                applyActive_tv.setText("报名已关闭");
                applyActive.setBackgroundColor(Color.parseColor("#99000000"));
            }
            if (Func.isExpired(this.activeInfo.getStartTime())) {
                applyActive_tv.setText("报名已截止");
                applyActive.setBackgroundColor(Color.parseColor("#99000000"));
            }
        } else {
            applyActive.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
            listView.setToolBar(add);
        }

        if (Const.PERMISSION_PRIVATE.equals(this.activeInfo.getPermission())) {
            privateMark_iv.setVisibility(View.VISIBLE);
        } else {
            privateMark_iv.setVisibility(View.GONE);
        }

        if (Const.ROLE_TYPE_CREATOR.equals(activeInfo.getRoleType())) {
            listView.getHeaderContainer().setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MaterialChooseDialog dialog = new MaterialChooseDialog(_activity);
                    dialog.setOnMaterialChooseListener(new onMaterialChooseListener() {

                        @Override
                        public void onClickFromMaterial() {
                            UIHelper.showMaterialList(_activity, "");
                        }

                        @Override
                        public void onClickFromGallery() {
                            String tag = System.currentTimeMillis() + "";
                            PhotoManager.getInstance().setLimit(tag, 1);
                            PhotoManager.getInstance().album(tag, 600, 600, 600, 600);
                        }

                        @Override
                        public void onClickFromCamera() {
                            String tag = System.currentTimeMillis() + "";
                            PhotoManager.getInstance().setLimit(tag, 1);
                            PhotoManager.getInstance().photo(tag, 600, 600, 600, 600);
                        }
                    });
                    dialog.show();
                }
            });
        }

        String bannerUrl = ApiClient.getFileUrl(activeInfo.getbPicName());
        if (Func.checkImageTag(bannerUrl, listView.getHeaderView())) {
            ac.imageLoader.loadImage(bannerUrl, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    listView.getHeaderView().setImageBitmap(loadedImage);
                }
            });
        }

        if (!isFirstRefresh) {
            return;
        }
        isFirstRefresh = false;
        Object object = _Bundle.getSerializable(BUNDLE_KEY_COMMENT);
        if (object == null) {
            return;
        }
        this.pendingComment = (Comment) object;

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

                                AppContext.showToast("评论成功");

                                addCommentBarDialog.restore();
                                addCommentBarDialog.dismiss();

                                AddCommentBean addCommentBean = (AddCommentBean) res;

                                addNewComment(addCommentBean.getCommentId());

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
                    }, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), pendingComment.getId(), "1", content);
                }
            });
        }
        addCommentBarDialog.setHint("回复" + Func.formatNickName(_activity, pendingComment.getUserId(), pendingComment.getNickname()) + "：");
        addCommentBarDialog.show();

    }


    private void addNewComment(String commentId) {

        Comment newComment = new Comment();
        newComment.setNickname(ac.getProperty(Const.USER_NICKNAME));
        newComment.setRemark(newComment.getNickname());
        newComment.setComment(tempContent);
        newComment.setCreateTime(Func.getNow());
        newComment.setHeadSPicName(ac.getProperty(Const.USER_SHEAD));
        newComment.setId(commentId);
        newComment.setUserId(ac.getLoginUid());
        newComment.setItemViewType(ActiveDetailActivity.TPL_COMMENT);
        Comment.ToCommentInfo toCommentInfo = new Comment.ToCommentInfo();
        toCommentInfo.setComment(pendingComment.getComment());
        toCommentInfo.setHeadSPicName(pendingComment.getHeadSPicName());
        toCommentInfo.setId(pendingComment.getId());
        toCommentInfo.setNickname(Func.formatNickName(_activity, pendingComment.getUserId(), pendingComment.getNickname()));
        toCommentInfo.setUserId(pendingComment.getUserId());
        newComment.setToCommentInfo(toCommentInfo);

        for (int i = 0; i < resultList.size(); i++) {
            Base obj = (Base) resultList.get(i);

            if (obj.getItemViewType() == ActiveDetailActivity.TPL_COMMENT_HEADER) {
                ActiveInfo activeInfo = (ActiveInfo) ((ObjectWrapper) obj).getObject();
                activeInfo.setComCount((Integer.parseInt(activeInfo.getComCount()) + 1) + "");
            }

            if (obj.getItemViewType() == ActiveDetailActivity.TPL_COMMENT) {

                resultList.add(i, newComment);
                break;
            }
        }
    }

    @Override
    public void handZoom(float mScale) {
        super.handZoom(mScale);
        if (mScale > 1.4f) {
            listViewHelper.refresh();
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
            if (API_TAG_VERIFY_WAY.equals(tag)) {
                GroupVerifyWayBean bean = (GroupVerifyWayBean) res;
                if (Const.APPLY_FLAY_STOP.equals(bean.getApplyFlay())) {
                    AppContext.showToast("报名已关闭");
                } else {
                    if (Const.VERIFY_WAY_TEXT.equals(bean.getVerifyWay())) {
                        UIHelper.showApplActiveText(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), bean.getJoinNeedContact(), bean.getJoinVerify());
                    } else {
                        if (Const.ACTIVE_JOIN_NEED_CONTACT.equals(bean.getJoinNeedContact())) {
                            UIHelper.showApplActiveText(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), bean.getJoinNeedContact(), bean.getJoinVerify());
                        } else {
                            ApiClient.getApi().apply(_activity, ac.getLoginUid(), activeInfo.getId(), null, null, null, null, null);
                        }
                    }
                }
            } else if (API_TAG_APPLY.equals(tag)) {
                AppContext.showToast("报名成功");
                activeInfo.setRoleType(Const.ROLE_TYPE_NORMAL);
                adapter.notifyDataSetChanged();
                applyActive.setVisibility(View.GONE);
                listViewHelper.refresh();
                GroupUtils.joinActivity(_activity, activeInfo.getId(), activeInfo.getName(), activeInfo.getIntro(), activeInfo.getbPicName(), activeInfo.getPermission());
            } else if (API_TAG_SET_PIC.equals(tag)) {
                AppContext.showToast("修改成功");
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

    @Override
    public void onPhoto(String tag, ArrayList<PhotoBean> photos) {
        if (photos != null && photos.size() > 0) {
            PhotoBean photoBean = photos.get(0);
            File pic = new File(photoBean.getImagePath());
            ApiClient.getApi().setPic(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), pic, null);

            String bannerUrl = StringUtils.getUri(photoBean.getImagePath());
            if (Func.checkImageTag(bannerUrl, listView.getHeaderView())) {
                ac.imageLoader.loadImage(bannerUrl, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        listView.getHeaderView().setImageBitmap(loadedImage);
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        PhotoManager.getInstance().onDestory();
        super.onDestroy();
        ac.putShareObject(Const.SHAREOBJ_KEY_ACTIVE_INFO, null);
    }
}
