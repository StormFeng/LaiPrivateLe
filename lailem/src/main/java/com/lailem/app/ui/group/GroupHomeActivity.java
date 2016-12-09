package com.lailem.app.ui.group;

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
import com.lailem.app.adapter.datasource.GroupHomeDataSouce;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseZoomListActivity;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.bean.Result;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.activegroup.GroupInfoBean.GroupInfo;
import com.lailem.app.jsonbean.activegroup.GroupVerifyWayBean;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.ui.create_old.MaterialListActivity;
import com.lailem.app.ui.create_old.MaterialListTwoActivity;
import com.lailem.app.ui.dynamic.DynamicDetailActivity;
import com.lailem.app.ui.group.tpl.GroupHomeActionBarTpl;
import com.lailem.app.ui.group.tpl.GroupHomeAddressTpl;
import com.lailem.app.ui.group.tpl.GroupHomeAvatarTpl;
import com.lailem.app.ui.group.tpl.GroupHomeCreateTpl;
import com.lailem.app.ui.group.tpl.GroupHomeDynamicHeader;
import com.lailem.app.ui.group.tpl.GroupHomeForCreatorTpl;
import com.lailem.app.ui.group.tpl.GroupHomeForMemberTpl;
import com.lailem.app.ui.group.tpl.GroupHomeForPublicTpl;
import com.lailem.app.ui.group.tpl.GroupHomeGapTpl;
import com.lailem.app.ui.group.tpl.GroupHomeImageTpl;
import com.lailem.app.ui.group.tpl.GroupHomeLineTpl;
import com.lailem.app.ui.group.tpl.GroupHomeNewAddressTpl;
import com.lailem.app.ui.group.tpl.GroupHomeNewVoiceTpl;
import com.lailem.app.ui.group.tpl.GroupHomeNoticeTpl;
import com.lailem.app.ui.group.tpl.GroupHomeScheduleTpl;
import com.lailem.app.ui.group.tpl.GroupHomeSingleImageTpl;
import com.lailem.app.ui.group.tpl.GroupHomeSingleVideoTpl;
import com.lailem.app.ui.group.tpl.GroupHomeTextTpl;
import com.lailem.app.ui.group.tpl.GroupHomeVideoImageTpl;
import com.lailem.app.ui.group.tpl.GroupHomeVideoTpl;
import com.lailem.app.ui.group.tpl.GroupHomeVoiceTpl;
import com.lailem.app.ui.group.tpl.GroupHomeVoteTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.AddDynamicDialog;
import com.lailem.app.widget.BindPhoneDialog;
import com.lailem.app.widget.ConfirmDialog;
import com.lailem.app.widget.GroupHomeActionDialog;
import com.lailem.app.widget.GroupManagerDialog;
import com.lailem.app.widget.MaterialChooseDialog;
import com.lailem.app.widget.MaterialChooseDialog.onMaterialChooseListener;
import com.lailem.app.widget.PullToZoomListView;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupHomeActivity extends BaseZoomListActivity<Object> implements OnPhotoListener {
    public static final String API_TAG_VERIFY_WAY = "verifyWay";
    public static final String API_TAG_SET_PIC = "setPic";
    public static final String API_TAG_QUIT = "quit";
    public static final String API_TAG_APPLY = "apply";
    public static final String API_TAG_DISBANDGROUP = "disbandGroup";

    public static final int TPL_DETAIL_FOR_MEMBER = 0;
    public static final int TPL_DETAIL_FOR_CREATOR = 1;
    public static final int TPL_DETAIL_FOR_PUBLIC = 2;
    public static final int TPL_DYNAMIS_HEADER = 3;
    public static final int TPL_AVATAR = 4;
    public static final int TPL_LINE = 5;
    public static final int TPL_ACTIONBAR = 6;
    public static final int TPL_TEXT = 7;
    public static final int TPL_IMAGE = 8;
    public static final int TPL_VOICE = 9;
    public static final int TPL_VIDEO = 10;
    public static final int TPL_VOTE = 11;
    public static final int TPL_ADDRESS = 12;
    public static final int TPL_SCHEDULE = 13;
    public static final int TPL_NOTICE = 14;
    public static final int TPL_CREATE = 15;
    public static final int TPL_NEW_ADDRESS = 16;
    public static final int TPL_NEW_VOICE = 17;
    public static final int TPL_VIDEOIMAGE = 18;
    public static final int TPL_GAP = 19;
    public static final int TPL_SINGLE_IMAGE = 20;
    public static final int TPL_SINGLE_VIDEO = 21;

    public static final int REQUEST_GROUP_INFO = 1000;
    public static final int REQUEST_CODE = 2000;

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
    @Bind(R.id.enterGroup)
    View enterGroup;
    @Bind(R.id.chat)
    View chat;
    @Bind(R.id.add)
    View add;
    @Bind((R.id.ic_add))
    View icAdd;
    @Bind(R.id.bg_add)
    View bgAdd;
    @Bind(R.id.toolbar)
    View toolbar;

    private GroupManagerDialog groupManagerDialog;

    private ImageView privateMark_iv;

    private GroupInfo groupInfo;

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
        GroupHomeActionDialog dialog = new GroupHomeActionDialog(_activity, groupInfo.getRoleType());
        dialog.setOnActionClickListener(new GroupHomeActionDialog.OnActionClickListener() {
            @Override
            public void onClickReport() {
                UIHelper.showComplain(_activity, groupInfo.getId(), Const.COMPLAIN_TYPE_GROUP);
            }

            @Override
            public void onClickShare() {
                String title = groupInfo.getName();
                String content = groupInfo.getIntro();
                String dataUrl = Const.GROUP_PATTERN + groupInfo.getId();
                String imageUrl = ApiClient.getFileUrl(groupInfo.getbPicName());
                String smsContent = "我在【来了】上创建了群组：" + groupInfo.getName() + "," + groupInfo.getIntro() + "，群组详情：" + Const.GROUP_PATTERN + groupInfo.getId() + ",快来加入吧！";
                String emailContent = "我在【来了】上创建了群组：" + groupInfo.getName() + "," + groupInfo.getIntro() + "，群组详情：" + Const.GROUP_PATTERN + groupInfo.getId() + ",快来加入吧！";
                String emailTopic = groupInfo.getName();
                String weixinMomentTitle = groupInfo.getName();
                String weiboContent = groupInfo.getName() + "，" + groupInfo.getIntro() + Const.GROUP_PATTERN + groupInfo.getId();
                UIHelper.showShare(_activity, title, content, dataUrl, imageUrl, smsContent, emailContent, emailTopic, weixinMomentTitle, weiboContent);
            }

            @Override
            public void onClickQuit() {
                if (groupInfo.getRoleType().equals(Const.ROLE_TYPE_CREATOR)) {
                    String phone = ac.getProperty(Const.USER_PHONE);
                    if (TextUtils.isEmpty(phone)) {
                        showBindPhoneConfirmDialog();
                    } else {
                        showGroupManagerDialog();
                    }
                } else {
                    ApiClient.getApi().quit(_activity, ac.getLoginUid(), groupInfo.getId());
                }
            }
        });
        dialog.show();
    }

    @OnClick(R.id.chat)
    public void clickChat() {
        UIHelper.showChat(_activity, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), groupInfo.getName(), groupInfo.getbPicName(), Constant.cType_gc);
    }

    @OnClick(R.id.add)
    public void addDynamic() {
        AddDynamicDialog addDynamicDialog = new AddDynamicDialog(_activity, groupInfo.getId(), Const.GROUPTYPE_GROUP, groupInfo.getRoleType(), groupInfo.getCreateActivityFlay());
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


    @OnClick(R.id.enterGroup)
    public void clickEnterGroup() {
        if (ac.isLogin(this, "refreshListView")) {
            ApiClient.getApi().verifyWay(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
        } else {
            UIHelper.showLogin(_activity, false);
        }
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
    protected int getLayoutId() {
        return R.layout.activity_group_home;
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new GroupHomeDataSouce(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_DETAIL_FOR_MEMBER, GroupHomeForMemberTpl.class);
        tpls.add(TPL_DETAIL_FOR_CREATOR, GroupHomeForCreatorTpl.class);
        tpls.add(TPL_DETAIL_FOR_PUBLIC, GroupHomeForPublicTpl.class);
        tpls.add(TPL_DYNAMIS_HEADER, GroupHomeDynamicHeader.class);
        tpls.add(TPL_AVATAR, GroupHomeAvatarTpl.class);
        tpls.add(TPL_LINE, GroupHomeLineTpl.class);
        tpls.add(TPL_ACTIONBAR, GroupHomeActionBarTpl.class);
        tpls.add(TPL_TEXT, GroupHomeTextTpl.class);
        tpls.add(TPL_IMAGE, GroupHomeImageTpl.class);
        tpls.add(TPL_VOICE, GroupHomeVoiceTpl.class);
        tpls.add(TPL_VIDEO, GroupHomeVideoTpl.class);
        tpls.add(TPL_VOTE, GroupHomeVoteTpl.class);
        tpls.add(TPL_ADDRESS, GroupHomeAddressTpl.class);
        tpls.add(TPL_SCHEDULE, GroupHomeScheduleTpl.class);
        tpls.add(TPL_NOTICE, GroupHomeNoticeTpl.class);
        tpls.add(TPL_CREATE, GroupHomeCreateTpl.class);
        tpls.add(TPL_NEW_ADDRESS, GroupHomeNewAddressTpl.class);
        tpls.add(TPL_NEW_VOICE, GroupHomeNewVoiceTpl.class);
        tpls.add(TPL_VIDEOIMAGE, GroupHomeVideoImageTpl.class);
        tpls.add(TPL_GAP, GroupHomeGapTpl.class);
        tpls.add(TPL_SINGLE_IMAGE, GroupHomeSingleImageTpl.class);
        tpls.add(TPL_SINGLE_VIDEO, GroupHomeSingleVideoTpl.class);
        return tpls;
    }

    public void refreshListView() {
        if (listViewHelper != null) {
            listViewHelper.refresh();
        }
    }

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
            if (API_TAG_VERIFY_WAY.equals(tag)) {
                GroupVerifyWayBean bean = (GroupVerifyWayBean) res;
                if (Const.VERIFY_WAY_TEXT.equals(bean.getVerifyWay())) {
                    UIHelper.showApplyGroupText(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
                } else if (Const.VERIFY_WAY_VOICE.equals(bean.getVerifyWay())) {
                    UIHelper.showApplyGroupVoice(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
                } else {
                    ApiClient.getApi().apply(_activity, ac.getLoginUid(), groupInfo.getId(), null, null, null, null, null);
                }
            } else if (API_TAG_SET_PIC.equals(tag)) {
                AppContext.showToast("修改成功");
            } else if (API_TAG_QUIT.equals(tag)) {

                GroupUtils.exitGroup(this, groupInfo.getId());
                AppContext.showToast("退出群组成功");
                handQuitOrDisbandGroup();
            } else if (API_TAG_DISBANDGROUP.equals(tag)) {
                GroupUtils.exitGroup(this, groupInfo.getId());
                AppContext.showToast("解散群组成功");
                showTipDialog();
            } else if (API_TAG_APPLY.equals(tag)) {
                AppContext.showToast("加入群组成功");
                groupInfo.setRoleType(Const.ROLE_TYPE_NORMAL);
                adapter.notifyDataSetChanged();
                enterGroup.setVisibility(View.GONE);
                listViewHelper.refresh();
                GroupUtils.joinGroup(_activity, groupInfo.getId(), groupInfo.getName(), groupInfo.getIntro(), groupInfo.getbPicName(), groupInfo.getPermission());
            }
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
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
            case TPL_CREATE:
                ObjectWrapper wrapper = (ObjectWrapper) item;
                DynamicBean dynamicBean = (DynamicBean) wrapper.getObject();
                UIHelper.showActiveDetail(_activity, dynamicBean.getCreateActivityInfo().getId());
                break;
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
                    if (nextItem.getItemViewType() == TPL_CREATE) {
                        ObjectWrapper w = (ObjectWrapper) nextItem;
                        DynamicBean db = (DynamicBean) w.getObject();
                        UIHelper.showActiveDetail(_activity, db.getCreateActivityInfo().getId());
                    } else {
                        for (int i = (int) id; i < resultList.size(); i++) {
                            Object object = resultList.get(i);
                            if (object instanceof DynamicBean) {
                                UIHelper.showDynamicDetail(this, ((DynamicBean) object).getId(), position);
                                break;
                            }
                        }
                    }
                }
                break;
            case TPL_ACTIONBAR:
                if (lastItem != null) {
                    if (lastItem.getItemViewType() == TPL_CREATE) {
                        ObjectWrapper w = (ObjectWrapper) lastItem;
                        DynamicBean db = (DynamicBean) w.getObject();
                        UIHelper.showActiveDetail(_activity, db.getCreateActivityInfo().getId());
                    } else {
                        UIHelper.showDynamicDetail(this, ((DynamicBean) item).getId(), position);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
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
                case GroupInfoForCreatorActivity.REQUEST_INFO_EDIT:
                    if (data.getExtras().getBoolean(Const.BUNDLE_KEY_GROUP_INFO_CHANGED)) {
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
    public void onEndRefresh(IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
        super.onEndRefresh(adapter, result);
        topbarProgressBar.setVisibility(View.INVISIBLE);
        if (resultList.size() == 0) {

            return;
        }
        topbarMore_iv.setVisibility(View.VISIBLE);
        topbarMore2_iv.setVisibility(View.VISIBLE);

        GroupHomeDataSouce ds = (GroupHomeDataSouce) this.dataSource;
        this.groupInfo = ds.getGroupInfo();
        if (Const.ROLE_TYPE_STRANGER.equals(groupInfo.getRoleType())) {
            enterGroup.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
        } else {
            enterGroup.setVisibility(View.GONE);
            chat.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            listView.setToolBar(toolbar);
        }

        if (Const.PERMISSION_PRIVATE.equals(this.groupInfo.getPermission())) {
            privateMark_iv.setVisibility(View.VISIBLE);
        } else {
            privateMark_iv.setVisibility(View.GONE);
        }


        if (Const.ROLE_TYPE_CREATOR.equals(groupInfo.getRoleType())) {
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

        String bannerUrl = ApiClient.getFileUrl(groupInfo.getbPicName());
        if (Func.checkImageTag(bannerUrl, listView.getHeaderView())) {
            ac.imageLoader.loadImage(bannerUrl, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    listView.getHeaderView().setImageBitmap(loadedImage);
                }
            });
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
        ac.putShareObject(Const.SHAREOBJ_KEY_GROUP_INFO, null);
    }


    private void showBindPhoneConfirmDialog() {
        ConfirmDialog dialog = new ConfirmDialog(_activity, R.style.confirm_dialog).config("提示", "解散群组需要先绑定手机号,是否绑定?", "绑定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                showBindPhoneDialog();
            }
        });
        dialog.show();
    }


    private void showBindPhoneDialog() {
        BindPhoneDialog dialog = new BindPhoneDialog(this);
        dialog.setOnBindPhoneSuccessListener(new BindPhoneDialog.OnBindPhoneSuccessListener() {

            @Override
            public void onBindSuccess(String password) {
                showQuitGroupConfirmDialog(password);
            }

        });
        dialog.show();
    }


    private void showQuitGroupConfirmDialog(final String password) {
        ConfirmDialog dialog = new ConfirmDialog(_activity, R.style.confirm_dialog).config("提示", "是否确认解散群组?", "解散", new OnClickListener() {

            @Override
            public void onClick(View v) {
                ApiClient.getApi().disbandGroup(_activity, ac.getLoginUid(), groupInfo.getId(), null, null, password);
            }
        });
        dialog.show();
    }


    private void showGroupManagerDialog() {
        groupManagerDialog = new GroupManagerDialog(_activity, groupInfo.getId(), groupInfo.getName());
        groupManagerDialog.show();
    }


    private void showTipDialog() {
        ActionDialog dialog = new ActionDialog(_activity);
        ActionDialog.DialogActionData.ActionData actionData = new ActionDialog.DialogActionData.ActionData("知道了", R.drawable.ic_dialog_ok_selector);
        ArrayList<ActionDialog.DialogActionData.ActionData> actionDatas = new ArrayList<ActionDialog.DialogActionData.ActionData>();
        actionDatas.add(actionData);
        ActionDialog.DialogActionData dialogActionData = new ActionDialog.DialogActionData(groupInfo.getName() + "已经解散", "您所创建的群组\"" + groupInfo.getName() + "\"已解散", actionDatas);
        dialog.init(dialogActionData);
        dialog.setOnActionClickListener(new ActionDialog.OnActionClickListener() {

            @Override
            public void onActionClick(ActionDialog dialog, View View, int position) {
                if (position == 0) {
                    handQuitOrDisbandGroup();
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                handQuitOrDisbandGroup();
            }
        });
        dialog.show();
    }

    private void handQuitOrDisbandGroup() {
        GroupUtils.exitGroup(_activity, groupInfo.getId());
        UIHelper.showMainWithClearTop(this);
    }

}
