package com.lailem.app.ui.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.jsonbean.personal.ChangeHeadBean;
import com.lailem.app.jsonbean.personal.UserBean;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.utils.ConfigManager;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.SelectCityDialog;
import com.lailem.app.widget.SelectCityDialog.OnChooseListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 补全用户资料
 * Created by XuYang on 15/10/13.
 */
public class EditProfileActivity extends BaseActivity implements OnPhotoListener, TextWatcher, OnChooseListener {
    private static final String API_TAG_CHANGE_HEAD = "changeHead";
    private static final String API_TAG_CHANGE_PERSON_INFO = "changePersonInfo";
    public static final String BUNDLE_KEY_USERINFO = "user_info";

    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    EditText name_et;
    @Bind(R.id.birthday)
    TextView birthday_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.sexRg)
    RadioGroup sexRg;
    @Bind(R.id.submit)
    Button submit_btn;

    private String sex;
    private String provinceId;
    private String cityId;
    private SelectCityDialog dialog;

    private UserBean userBean;
    private String cityName;
    private String provinceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(this, this);
        setContentView(R.layout.activity_edit_profile);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        userBean = (UserBean) _Bundle.getSerializable(BUNDLE_KEY_USERINFO);
        intView();
    }

    private void intView() {
        name_et.addTextChangedListener(this);
        birthday_tv.addTextChangedListener(this);
        address_tv.addTextChangedListener(this);

        //头像
        String head = userBean.getUserInfo().getHeadSPicName();
        if (!TextUtils.isEmpty(head)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(head)).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(avatar_iv);
        }
        //姓名
        name_et.setText(userBean.getUserInfo().getNickname());
        name_et.setSelection(name_et.length());
        //出生日期
        birthday_tv.setText(userBean.getUserInfo().getBirthday());
        //性别
        if (Const.FEMALE.equals(userBean.getUserInfo().getSex())) {
            sexRg.check(R.id.female);
        } else {
            sexRg.check(R.id.male);
        }
        //居住城市
        String provinceName = userBean.getUserInfo().getProvince();
        String cityName = userBean.getUserInfo().getCity();
        if (!TextUtils.isEmpty(provinceName) && !TextUtils.isEmpty(cityName)) {
            ConfigManager configManager = ConfigManager.getConfigManager();
            String provinceId = configManager.queryRId(provinceName);
            String cityId = configManager.queryRId(cityName);
            if (!TextUtils.isEmpty(cityId) && !TextUtils.isEmpty(provinceId)) {
                this.provinceId = provinceId;
                this.cityId = cityId;
                address_tv.setText(provinceName + " " + cityName);
            }
        }
    }

    @OnClick(R.id.avatar)
    public void clickAvatar() {
        new ActionDialog(this).init(ActionDialog.DialogActionData.build(null, null, new ActionDialog.DialogActionData.ActionData("立即拍照", R.drawable.ic_by_camera_selector), new ActionDialog.DialogActionData.ActionData("图库上传", R.drawable.ic_by_gallery_selector)))
                .setOnActionClickListener(new ActionDialog.OnActionClickListener() {

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


    @OnClick(R.id.birthday_ll)
    public void clickBirthday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                birthday_tv.setText(sdf.format(calendar.getTime()));
            }
        }, year, monthOfYear, dayOfMonth);
        dialog.show();
    }

    @OnClick(R.id.address_ll)
    public void clickAddress() {
        if (dialog == null) {
            String provinceName = userBean.getUserInfo().getProvince();
            String cityName = userBean.getUserInfo().getCity();
            ConfigManager configManager = ConfigManager.getConfigManager();
            String provinceId = configManager.queryRId(provinceName);
            String cityId = configManager.queryRId(cityName);
            dialog = new SelectCityDialog(this, provinceName, cityName, provinceId, cityId);
        }
        dialog.setOnChooseListener(this);
        dialog.show();
    }

    @OnClick(R.id.submit)
    public void submit() {
        //姓名
        String nickName = name_et.getText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            AppContext.showToast("请填写姓名");
            return;
        }
        //生日
        String birthday = birthday_tv.getText().toString().trim();
        if (TextUtils.isEmpty(birthday)) {
            AppContext.showToast("请选择生日");
            return;
        }
        //性别
        if (sexRg.getCheckedRadioButtonId() == R.id.male) {
            sex = Const.MALE;
        } else {
            sex = Const.FEMALE;
        }
        //城市
        if (TextUtils.isEmpty(provinceId) && TextUtils.isEmpty(cityId)) {
            AppContext.showToast("选择居住城市");
            return;
        }

        ApiClient.getApi().changePersonInfo(this, userBean.getUserInfo().getToken(), userBean.getUserInfo().getId(), birthday, cityId, null, null, nickName, null, provinceId,
                sex);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        submit_btn.setEnabled(name_et.length() > 0 && birthday_tv.length() > 0 && address_tv.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void OnChoose(String provinceName, String provinceId, String cityName, String cityId) {
        if (provinceName.equals(cityName)) {
            address_tv.setText(cityName);
            this.cityName = cityName;
            this.provinceName = "";
            this.provinceId = "";
            this.cityId = cityId;
        } else {
            this.provinceName = provinceName;
            this.cityName = cityName;
            address_tv.setText(provinceName + " " + cityName);
            this.provinceId = provinceId;
            this.cityId = cityId;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPhoto(String tag, ArrayList<PhotoBean> photos) {
        if (photos != null && photos.size() > 0) {
            String avatarPath = photos.get(0).getImagePath();
            File head = new File(avatarPath);
            ApiClient.getApi().changeHead(this, userBean.getUserInfo().getToken(), userBean.getUserInfo().getId(), head);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
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
            if (API_TAG_CHANGE_HEAD.equals(tag)) {
                ChangeHeadBean changeHeadBean = (ChangeHeadBean) res;
                userBean.getUserInfo().setHeadSPicName(changeHeadBean.getHeadSPicName());
                userBean.getUserInfo().setHeadBPicName(changeHeadBean.getHeadBPicName());
                Glide.with(_activity).load(ApiClient.getFileUrl((userBean.getUserInfo().getHeadSPicName()))).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(avatar_iv);
            } else if (API_TAG_CHANGE_PERSON_INFO.equals(tag)) {
                userBean.getUserInfo().setNickname(name_et.getText().toString().trim());
                userBean.getUserInfo().setBirthday(birthday_tv.getText().toString().trim());
                userBean.getUserInfo().setAge(Func.getAge(birthday_tv.getText().toString().trim()));
                userBean.getUserInfo().setSex(sex);
                userBean.getUserInfo().setProvince(provinceName);
                userBean.getUserInfo().setCity(cityName);

                ac.saveUserInfo(userBean);
                UIHelper.showMain(_activity, true);
                BroadcastManager.sendLoginBroadcast(_activity);
                finish();
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
    public void onBackPressed() {
        super.onBackPressed();
        ac.logout();
        finish();
        UIHelper.showLogin(this, true);
    }
}
