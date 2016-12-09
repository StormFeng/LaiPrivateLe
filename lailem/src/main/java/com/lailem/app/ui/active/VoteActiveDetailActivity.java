package com.lailem.app.ui.active;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.datasource.VoteActiveDetailDataSource;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseZoomListActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.VoteActiveDetailBean.ActiveInfo;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.tpl.CommentEmptyTpl;
import com.lailem.app.ui.active.tpl.vote.VoteActiveDetailActionBarTpl;
import com.lailem.app.ui.active.tpl.vote.VoteActiveDetailCommentTpl;
import com.lailem.app.ui.active.tpl.vote.VoteActiveDetailTpl;
import com.lailem.app.ui.create_old.MaterialListActivity;
import com.lailem.app.ui.create_old.MaterialListTwoActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.MaterialChooseDialog;
import com.lailem.app.widget.MaterialChooseDialog.onMaterialChooseListener;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.VoteActiveDetailActionDialog;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 投票活动详情
 *
 * @author XuYang
 */
public class VoteActiveDetailActivity extends BaseZoomListActivity<Object> implements OnPhotoListener {
    public static final String API_TAG_SET_PIC = "setPic";
    public static final String API_TAG_DISBANDGROUP = "disbandGroup";// 解散群组

    public static final int TPL_DETAIL = 0;
    public static final int TPL_ACTIONBAR = 1;
    public static final int TPL_COMMENT = 2;
    public static final int TPL_COMMENT_EMPTY = 3;

    public static final int REQUEST_CODE = 2000;

    @Bind(R.id.topbar)
    TopBarView topbar;

    private TextView activeTitle_tv;
    private TextView activeType_tv;
    private ActiveInfo activeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(this, this);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
    }

    private void initView() {
        topbar.setTitle("投票活动").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).setRightImageButton(R.drawable.ic_topbar_more, new OnClickListener() {

            @Override
            public void onClick(View v) {
                VoteActiveDetailActionDialog dialog = new VoteActiveDetailActionDialog(_activity);
                dialog.setOnActionClickListener(new VoteActiveDetailActionDialog.OnActionClickListener() {
                    @Override
                    public void onClickQrCode() {
                        UIHelper.showQrCode(_activity, activeInfo.getName(), activeInfo.getIntro(), ApiClient.getFileUrl(activeInfo.getbPicName()), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), Const.QRCODE_TYPE_VOTE_ACTIVE);
                    }

                    @Override
                    public void onClickDelete() {
                        ApiClient.getApi().disbandGroup(_activity, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), null, null, null);
                    }

                    @Override
                    public void onClickShare() {
                        String title = activeInfo.getName();
                        String content = activeInfo.getName() + "，时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress();
                        String dataUrl = Const.VOTE_ACTIVE_PATTERN + activeInfo.getId();
                        String imageUrl = ApiClient.getFileUrl(activeInfo.getbPicName());
                        String smsContent = "我在【来了】上创建了活动：" + activeInfo.getName() + ",时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + "，活动详情：" + Const.VOTE_ACTIVE_PATTERN + activeInfo.getId() + "，快给我点个赞吧！有兴趣也可以报名参加哦！";
                        String emailContent = "我在【来了】上创建了活动：" + activeInfo.getName() + ",时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + "，活动详情：" + Const.VOTE_ACTIVE_PATTERN + activeInfo.getId() + "，快给我点个赞吧！有兴趣也可以报名参加哦！";
                        String emailTopic = activeInfo.getName();
                        String weixinMomentTitle = activeInfo.getName();
                        String weiboContent = activeInfo.getName() + "，时间：" + activeInfo.getStartTime() + "，地点：" + activeInfo.getAddress() + Const.VOTE_ACTIVE_PATTERN + activeInfo.getId();
                        UIHelper.showShare(_activity, title, content, dataUrl, imageUrl, smsContent, emailContent, emailTopic, weixinMomentTitle, weiboContent);
                    }
                });
                dialog.show();
            }
        });
        refreshListView.setPullRefreshEnabled(false);
        listView.setDividerHeight(0);
        View header = View.inflate(this, R.layout.view_vote_active_detail_header, null);
        listView.getHeaderContainer().addView(header);
        activeTitle_tv = (TextView) header.findViewById(R.id.activeTitle);
        activeType_tv = (TextView) header.findViewById(R.id.activeType);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new VoteActiveDetailDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_DETAIL, VoteActiveDetailTpl.class);
        tpls.add(TPL_ACTIONBAR, VoteActiveDetailActionBarTpl.class);
        tpls.add(TPL_COMMENT, VoteActiveDetailCommentTpl.class);
        tpls.add(TPL_COMMENT_EMPTY, CommentEmptyTpl.class);
        return tpls;
    }

    @Override
    public void onStartRefresh(IDataAdapter<ArrayList<Object>> adapter) {
        super.onStartRefresh(adapter);
        topbar.hideRight_ib();
    }

    @Override
    public void onEndRefresh(IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
        super.onEndRefresh(adapter, result);
        topbar.hideProgressBar();
        if (resultList.size() == 0) {
            //加载失败的情况
            return;
        }
        topbar.showRight_ib();
        VoteActiveDetailDataSource ds = (VoteActiveDetailDataSource) this.dataSource;
        this.activeInfo = ds.getActiveInfo();

        // 自己创建的投票活动可以修改群图片
        if (activeInfo.getCreatorInfo().getId().equals(ac.getLoginUid())) {
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
        activeType_tv.setText(activeInfo.getTypeName());
        activeTitle_tv.setText(activeInfo.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
        // 选择素材s
        if (requestCode == MaterialListActivity.REQUEST_CODE_MATERIAL_TYPE) {
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
        }
    }

    @Override
    public void handZoom(float mScale) {
        super.handZoom(mScale);
        if (mScale > 1.4f) {
            topbar.showProgressBar();
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
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        if (res.isOK()) {
            if (API_TAG_DISBANDGROUP.equals(tag)) {
                GroupUtils.exitActivity(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
                finish();
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
        hideWaitDialog();
    }

    @Override
    public void onDestroy() {
        PhotoManager.getInstance().onDestory();
        super.onDestroy();
    }

}
