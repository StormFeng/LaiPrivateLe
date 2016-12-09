package com.lailem.app.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.adapter.ImageViewerAdapter.ImageBean;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.User;
import com.lailem.app.jsonbean.personal.ChangeHeadBean;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;
import com.lailem.app.widget.TopBarView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UserInfoActivity extends BaseActivity implements OnPhotoListener {
    public static final int REQUEST_NAME = 1000;
    public static final int REQUEST_ACTIVEID = 1001;
    public static final int REQUEST_SEX = 1002;
    public static final int REQUEST_ADDRESS = 1003;
    public static final int REQUEST_SIGN = 1004;

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.activeId_ll)
    View activeId_ll;
    @Bind(R.id.activeId)
    TextView activeId_tv;
    @Bind(R.id.activeIdArrow)
    ImageView activeIdArrow_iv;
    @Bind(R.id.age)
    TextView age_tv;
    @Bind(R.id.sex)
    TextView sex_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.sign)
    TextView sign_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(this, this);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("个人信息").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));

        if (Func.checkImageTag(ac.getProperty(Const.USER_SHEAD), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(ac.getProperty(Const.USER_SHEAD))).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }

        name_tv.setText(ac.getProperty(Const.USER_NICKNAME));
        String birthday = ac.getProperty(Const.USER_BIRTHDAY);
        if (!TextUtils.isEmpty(birthday)) {
            try {
                age_tv.setText(Func.getAge(birthday) + "岁");
            } catch (Exception e) {
                age_tv.setText("");
            }
        } else {
            age_tv.setText("");
        }


        String sex = ac.getProperty(Const.USER_SEX);
        if (Const.MALE.equals(sex)) {
            sex_tv.setText("男");
        } else if (Const.FEMALE.equals(sex)) {
            sex_tv.setText("女");
        }

        activeId_tv.setText(ac.getProperty(Const.USER_USERNAME));

        if (activeId_tv.length() > 0) {
            activeIdArrow_iv.setVisibility(View.GONE);
            activeId_ll.setBackgroundResource(R.color.white);
        }
        String provinceName = ac.getProperty(Const.USER_PROVINCE);
        String cityName = ac.getProperty(Const.USER_CITY);
        if (TextUtils.isEmpty(provinceName)) {
            address_tv.setText(cityName);
        } else {
            address_tv.setText(provinceName + " " + cityName);
        }

        sign_tv.setText(ac.getProperty(Const.USER_SIGN));
    }

    @OnClick(R.id.avatar_ll)
    public void clickAvatarLL() {

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

    @OnClick(R.id.avatar)
    public void clickAvatar() {
        ArrayList<ImageViewerAdapter.ImageBean> beans = new ArrayList<ImageViewerAdapter.ImageBean>();
        String url = "";
        String user_bhead = ac.getProperty(Const.USER_BHEAD);

        if (!TextUtils.isEmpty(user_bhead)) {
            url = ApiClient.getFileUrl(user_bhead);
        }

        ImageBean bean = new ImageBean(url);
        beans.add(bean);
        UIHelper.showImages(this, beans, 0);
    }

    @OnClick(R.id.name_ll)
    public void clickName() {
        UIHelper.showModifyName(_activity, name_tv.getText().toString().trim());
    }

    @OnClick(R.id.activeId_ll)
    public void clickActiveId() {
        if (activeId_tv.length() == 0) {
            UIHelper.showModifyActiveId(_activity, activeId_tv.getText().toString().trim());
        }
    }

    @OnClick(R.id.age_ll)
    public void clickAge() {
        UIHelper.showModifyAge(_activity, ac.getProperty(Const.USER_BIRTHDAY));
    }

    @OnClick(R.id.sex_ll)
    public void clickSex() {
        UIHelper.showModifySex(_activity, sex_tv.getText().toString().trim());
    }

    @OnClick(R.id.address_ll)
    public void clickAddress() {
        String address = address_tv.getText().toString().trim();
        String[] strArr = address.split(" ");
        String cityName = "";
        if (strArr.length > 1) {
            cityName = strArr[1];
        } else {
            cityName = strArr[0];
        }
        UIHelper.showChooseCityOne(_activity, cityName);
    }

    @OnClick(R.id.sign_ll)
    public void clickSign() {
        UIHelper.showModifySign(_activity, sign_tv.getText().toString().trim());
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
            ChangeHeadBean changeHeadBean = (ChangeHeadBean) res;
            ac.setProperty(Const.USER_SHEAD, changeHeadBean.getHeadSPicName());
            ac.setProperty(Const.USER_BHEAD, changeHeadBean.getHeadBPicName());
            if (Func.checkImageTag(ac.getProperty(Const.USER_SHEAD), avatar_iv)) {
                Glide.with(_activity).load(ApiClient.getFileUrl(ac.getProperty(Const.USER_SHEAD))).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
            }
            AppContext.showToast("头像修改成功");
            updateDBHead(changeHeadBean.getHeadSPicName(), changeHeadBean.getHeadBPicName());
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }


    /**
     * 更新会话状态为需要发头像
     */
    private void updateDBHead(final String sPicName, final String bPicName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Conversation> conversations = DaoOperate.getInstance(_activity).queryConversations();
                if (conversations != null) {
                    for (int i = 0; i < conversations.size(); i++) {
                        Conversation conversation = conversations.get(i);
                        conversation.setNeedSendHead(Constant.value_yes);
                    }
                }
                DaoOperate.getInstance(_activity).updateConversationInTx(conversations);

                User user = UserCache.getInstance(_activity).get(ac.getProperty(Const.USER_ID));
                user.setHead(sPicName);
                user.setHeadBig(bPicName);
                UserCache.getInstance(_activity).put(user);
            }
        }).start();
    }

    /**
     * 更新会话状态为需要发昵称
     */
    private void updateDBName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Conversation> conversations = DaoOperate.getInstance(_activity).queryConversations();
                if (conversations != null) {
                    for (int i = 0; i < conversations.size(); i++) {
                        Conversation conversation = conversations.get(i);
                        conversation.setNeedSendNick(Constant.value_yes);
                    }
                }
                DaoOperate.getInstance(_activity).updateConversationInTx(conversations);

                User user = UserCache.getInstance(_activity).get(ac.getProperty(Const.USER_ID));
                user.setNickname(ac.getProperty(Const.USER_NICKNAME));
                UserCache.getInstance(_activity).put(user);

            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            initView();
            if (requestCode == REQUEST_NAME) {
                updateDBName();
            }
        }
    }

    @Override
    public void onPhoto(String tag, ArrayList<PhotoBean> photos) {
        if (photos != null && photos.size() > 0) {
            String avatarPath = photos.get(0).getImagePath();
            File head = new File(avatarPath);
            ApiClient.getApi().changeHead(this, ac.getLoginUid(), head);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
    }
}
