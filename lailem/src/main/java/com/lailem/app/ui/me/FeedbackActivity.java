package com.lailem.app.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.adapter.ImageViewerAdapter.ImageBean;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.ui.common.ImageViewerActivity;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.lailem.app.widget.FlowLayout;
import com.lailem.app.widget.TopBarView;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FeedbackActivity extends BaseActivity implements OnPhotoListener {

    public static final String BUNDLE_KEY_TEXT = "text";
    public static final String BUNDLE_KEY_VALUE = "value";
    public static final int REQUEST_TYPE = 2000;

    public static final String PHOTO_TAG_IMAGE = "photo_tag_image";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.type)
    TextView type_tv;
    @Bind(R.id.content)
    EditText content_et;
    @Bind(R.id.images)
    FlowLayout images;

    private String fbItemId = "402881f34f3f9796014f3f9842690000";//默认为建议Id

    private ImageView add_iv;
    private ArrayList<PhotoBean> photos = new ArrayList<PhotoBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(this, this);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("意见反馈").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        add_iv = (ImageView) View.inflate(_activity, R.layout.view_image_add, null);
        PhotoManager.getInstance().setLimit(PHOTO_TAG_IMAGE, 9);
        add_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new ActionDialog(_activity).init(DialogActionData.build(null, null, new ActionData("立即拍照", R.drawable.ic_by_camera_selector), new ActionData("图库上传", R.drawable.ic_by_gallery_selector)))
                        .setOnActionClickListener(new OnActionClickListener() {

                            @Override
                            public void onActionClick(ActionDialog dialog, View View, int position) {
                                if (position == 0) {
                                    PhotoManager.getInstance().photo(PHOTO_TAG_IMAGE);
                                } else if (position == 1) {
                                    PhotoManager.getInstance().album(PHOTO_TAG_IMAGE);
                                }
                            }
                        }).show();
            }
        });
        images.addView(add_iv);
    }

    @OnClick(R.id.type_ll)
    public void clickType() {
        UIHelper.showFeedbackTypeOne(this);
    }

    protected void jumpToImageView(int index) {
        ArrayList<ImageBean> imageBeans = new ArrayList<ImageViewerAdapter.ImageBean>();
        for (int i = 0; i < photos.size(); i++) {
            imageBeans.add(new ImageBean("file://" + photos.get(i).getImagePath(), true));
        }
        UIHelper.showImages(this, imageBeans, index, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_TYPE:
                    fbItemId = data.getExtras().getString(BUNDLE_KEY_VALUE);
                    type_tv.setText(data.getExtras().getString(BUNDLE_KEY_TEXT));
                    break;

                case ImageViewerActivity.REQUEST_CODE_IMAGE_VIEWER:
                    ArrayList<ImageBean> imageBeans = (ArrayList<ImageBean>) data.getExtras().getSerializable("imageBeans");
                    for (int i = imageBeans.size() - 1; i >= 0; i--) {
                        ImageBean imageBean = imageBeans.get(i);
                        if (!imageBean.isChecked) {
                            photos.remove(i);
                            images.removeViewAt(i);
                        }
                    }
                    for (int i = 0; i < images.getChildCount() - 1; i++) {
                        final int index = i;
                        images.getChildAt(index).setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                jumpToImageView(index);
                            }
                        });
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.submit)
    public void submit() {
        if (TextUtils.isEmpty(fbItemId)) {
            AppContext.showToast("请选择反馈类型");
            return;
        }

        String content = content_et.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            AppContext.showToast("请填写反馈内容");
            return;
        }

        ArrayList<File> pics = new ArrayList<File>();
        for (int i = 0; i < photos.size(); i++) {
            pics.add(new File(photos.get(i).getImagePath()));
        }
        String userId = ac.getLoginUid();
        if (TextUtils.isEmpty(userId)) {
            userId = null;
        }
        ApiClient.getApi().feedback(this, content, fbItemId, pics, userId);
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        if (res.isOK()) {
            AppContext.showToast("提交成功");
            finish();
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
    public void onPhoto(String tag, ArrayList<PhotoBean> photos) {
        if (PHOTO_TAG_IMAGE.equals(tag)) {
            if (photos != null) {
                this.photos = photos;

                images.removeAllViews();

                for (int i = 0; i < photos.size(); i++) {
                    ImageView iv = (ImageView) View.inflate(_activity, R.layout.view_image_add, null);
                    final int index = i;
                    iv.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            jumpToImageView(index);
                        }
                    });

                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams((int) TDevice.dpToPixel(61.4f), (int) TDevice.dpToPixel(61.4f));
                    params.horizontalSpacing = (int) TDevice.dpToPixel(15);
                    params.verticalSpacing = (int) TDevice.dpToPixel(15);
                    images.addView(iv, params);
                    if (Func.checkImageTag(photos.get(i).getImagePath(), iv)) {
                        Glide.with(this).load(photos.get(i).getImagePath()).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(iv);
                    }
                }
                images.addView(add_iv);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
    }
}
