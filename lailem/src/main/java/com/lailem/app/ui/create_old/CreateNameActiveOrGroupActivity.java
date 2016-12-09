package com.lailem.app.ui.create_old;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.CreateActiveCache;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.dynamic.RandomMaterialBean;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.MaterialChooseDialog;
import com.lailem.app.widget.MaterialChooseDialog.onMaterialChooseListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class CreateNameActiveOrGroupActivity extends BaseActivity implements onMaterialChooseListener, OnPhotoListener {

    public static final String BUNDLE_KEY_PIC_LOCAL = "pic_local";// 本地图片
    public static final String BUNDLE_KEY_PIC_MATERIALID = "pic_material_id";// 图片素材Id
    public static final String BUNDLE_KEY_NAME = "group_name";// group名称

    @Bind(R.id.headTitle)
    TextView headTitle_tv;
    @Bind(R.id.tip)
    TextView tip_tv;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.material)
    ImageView material_iv;
    @Bind(R.id.name)
    EditText name_et;

    private String picMaterialId;// 素材id
    private String picSquareSName;// 素材图片方形小图
    private String picLocal;// 本地选择的图片
    private String typeId;// group类型
    private String typeName;//group类型名
    private String groupType;// 类型：1（活动）、2（普通群）、3（群联）
    private MaterialChooseDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(this, this);
        setContentView(R.layout.activity_create_name_active_or_group);
        setTranslucentStatus(true, R.color.window_bg);
        ButterKnife.bind(this);

        typeId = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID);
        typeName = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_NAME);
        groupType = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_GROUP_TYPE);

        initView();

    }

    private void initView() {
        if (CreateTypeActivity.GROUP_TYPE_ACTIVE.equals(groupType)) {
            headTitle_tv.setText("创建活动");
            tip_tv.setText("创建活动邀请好友们一起，你是我的小伙伴");
            name_et.setHint("请输入活动名称");
            name_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(35)});//  活动名称：1-35
        } else if (CreateTypeActivity.GROUP_TYPE_GROUP.equals(groupType)) {
            headTitle_tv.setText("创建群组");
            tip_tv.setText("邀一帮好友组建群组，一齐畅快活动");
            name_et.setHint("请输入群组名称");
            name_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});// 群组名称：1-16
        }
        name_et.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                return false;
            }
        });

        //处理草稿
        if (ac.isLogin() && CreateTypeActivity.GROUP_TYPE_ACTIVE.equals(groupType)) {
            String cacheName = Func.buildCreateTypeCacheName(ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID));
            if (ac.isExistDataCache(cacheName)) {
                Object object = ac.readObject(cacheName);
                if (object instanceof CreateActiveCache) {
                    CreateActiveCache cache = (CreateActiveCache) object;
                    String picPath = cache.getPicPath();
                    String picMaterial = cache.getPicMaterial();
                    String picName = cache.getPicName();
                    String name = cache.getName();

                    //名称
                    if (!TextUtils.isEmpty(name)) {
                        name_et.setText(name);
                        name_et.setSelection(name_et.length());
                    }

                    //图片（本地）
                    if (!TextUtils.isEmpty(picPath) && new File(picPath).exists()) {
                        this.picLocal = picPath;
                        if (Func.checkImageTag(picLocal, material_iv)) {
                            Glide.with(_activity).load(StringUtils.getUri(picLocal)).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.TOP)).into(material_iv);
                        }
                    }
                    //图片（素材中心）
                    else if (!TextUtils.isEmpty(picMaterial) && !TextUtils.isEmpty(picName)) {
                        this.picMaterialId = picMaterial;
                        this.picSquareSName = picName;
                        if (Func.checkImageTag(picSquareSName, material_iv)) {
                            Glide.with(_activity).load(ApiClient.getFileUrl(picSquareSName)).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.TOP)).into(material_iv);
                        }
                    } else {
                        getRandomMaterail();
                    }
                } else {
                    getRandomMaterail();
                }
            } else {
                getRandomMaterail();
            }
        } else {
            getRandomMaterail();
        }

    }

    public void getRandomMaterail() {
        ApiClient.getApi().randomOne(this);
    }

    @OnClick(R.id.back)
    public void back() {
        saveDraft();
        finish();
    }

    @OnClick(R.id.randomMaterail)
    public void clickRandomMaterial() {
        getRandomMaterail();
    }

    @OnClick(R.id.chooseMaterial)
    public void clickChooseMaterial() {
        if (dialog == null) {
            dialog = new MaterialChooseDialog(this);
            dialog.setOnMaterialChooseListener(this);
        }
        dialog.show();
    }

    @OnClick(R.id.submit)
    public void submit() {
        String name = name_et.getText().toString().trim();
        if (CreateTypeActivity.GROUP_TYPE_GROUP.equals(groupType)) {
            // 群组
            if (TextUtils.isEmpty(name)) {
                AppContext.showToast("请输入群组名称");
                return;
            }
            UIHelper.showCreateGroup(this, groupType, typeId, typeName, picLocal, picMaterialId, name);
        } else if (CreateTypeActivity.GROUP_TYPE_ACTIVE.equals(groupType)) {
            // 活动
            if (name_et.length() < 2) {
                AppContext.showToast("活动名称要求2-16个字");
                return;
            }
            if (Const.TYPE_ID_VOTE.equals(typeId)) {
                // 创建单独的投票活动
                UIHelper.showCreateVoteActive(this, typeId, picLocal, picMaterialId, name, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
            } else {
                UIHelper.showCreateActive(this, groupType, typeId, typeName, picLocal, picMaterialId, name, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MaterialListActivity.REQUEST_CODE_MATERIAL_TYPE:
                    picLocal = "";
                    picMaterialId = data.getExtras().getString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_ID);
                    picSquareSName = data.getExtras().getString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_SQUARE_SNAME);
                    if (Func.checkImageTag(picSquareSName, material_iv)) {
                        Glide.with(_activity).load(ApiClient.getFileUrl(picSquareSName)).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(material_iv);
                    }
                    break;
                case CreateTypeActivity.REQUEST_NEXT:
                    setResult(RESULT_OK);
                    finish();
                    break;
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
            RandomMaterialBean bean = (RandomMaterialBean) res;
            picLocal = "";
            picMaterialId = bean.getPicMaterialInfo().getId();
            picSquareSName = bean.getPicMaterialInfo().getSquareSPicName();
            if (Func.checkImageTag(picSquareSName, material_iv)) {
                Glide.with(_activity).load(ApiClient.getFileUrl(picSquareSName)).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(material_iv);
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

    @Override
    public void onClickFromMaterial() {
        UIHelper.showMaterialList(this, picMaterialId);
    }

    @Override
    public void onPhoto(String tag, ArrayList<PhotoBean> photos) {
        if (photos != null && photos.size() > 0) {
            picMaterialId = "";
            picLocal = photos.get(0).getImagePath();
            if (Func.checkImageTag(picLocal, material_iv)) {
                Glide.with(_activity).load(StringUtils.getUri(picLocal)).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(material_iv);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveDraft();
    }

    private void saveDraft() {
        if (ac.isLogin()) {
            String cacheName = Func.buildCreateTypeCacheName(ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID));
            Object object = ac.readObject(cacheName);
            if (object != null && object instanceof CreateActiveCache) {
                CreateActiveCache cache = (CreateActiveCache) object;
                cache.setName(name_et.getText().toString().trim());
                cache.setPicPath(picLocal);
                cache.setPicMaterial(picMaterialId);
                cache.setPicName(picSquareSName);
                ac.saveObject(cache, cacheName);
            }
        }
    }

}
