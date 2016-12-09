package com.lailem.app.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.AppManager;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.jsonbean.personal.UserBean;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.ui.main.MainActivity;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RegisterAvatarActivity extends BaseActivity implements OnPhotoListener {

    @Bind(R.id.avatar)
    ImageView avatar_iv;

    private String avatarPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(this, this);
        setContentView(R.layout.activity_register_avatar);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.avatar)
    public void clickAvatar() {
        PhotoManager.getInstance().aspectX = 1;
        PhotoManager.getInstance().aspectY = 1;
        new ActionDialog(this).init(DialogActionData.build(null, null, new ActionData("立即拍照", R.drawable.ic_by_camera_selector), new ActionData("图库上传", R.drawable.ic_by_gallery_selector)))
                .setOnActionClickListener(new OnActionClickListener() {

                    @Override
                    public void onActionClick(ActionDialog dialog, View View, int position) {
                        if (position == 0) {
                            String tag = System.currentTimeMillis() + "";
                            PhotoManager.getInstance().setLimit(tag, 1);
                            PhotoManager.getInstance().photo(tag, 1, 1, 1000, 1000);
                        } else if (position == 1) {
                            String tag = System.currentTimeMillis() + "";
                            PhotoManager.getInstance().setLimit(tag, 1);
                            PhotoManager.getInstance().album(tag, 1, 1, 1000, 1000);
                        }
                    }
                }).show();
    }

    @OnClick(R.id.submit)
    public void submit() {
        File head = null;
        if (!TextUtils.isEmpty(avatarPath)) {
            head = new File(avatarPath);
        }
        ApiClient.getApi().register(this, _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_VALIDCODE), _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_BIRTHDAY),
                _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_CITYID), head, _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_NICKNAME), _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_PASSWORD),
                _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_PHONE), _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_PROVINCEID), _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_SEX),
                _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_TRANSID));
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
            ac.saveUserInfo((UserBean) res);
            //如果还没有进入主界面
            if (AppManager.getActivity(MainActivity.class) == null) {
                UIHelper.showMain(this, true);
            } else {
                if (ac.isNeedLoginCallback()) {
                    AppManager.getActivity(RegisterAvatarActivity.class).finish();
                    AppManager.getActivity(RegisterInfoActivity.class).finish();
                    AppManager.getActivity(RegisterVerifyActivity.class).finish();
                    AppManager.getActivity(LoginActivity.class).finish();
                    ac.excuteLoginCallback();
                } else {
                    UIHelper.showMain(_activity, true);
                }
            }
            BroadcastManager.sendRegisterBroadcast(_activity);
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
        if (photos != null && photos.size() > 0) {
            this.avatarPath = photos.get(0).getImagePath();
            if (Func.checkImageTag(avatarPath, avatar_iv)) {
                Glide.with(_activity).load(avatarPath).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(avatar_iv);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
    }

}
