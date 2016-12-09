package com.lailem.app.ui.create_old;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.CreateActiveCache;
import com.lailem.app.bean.CreateActiveIntroEditBean;
import com.lailem.app.bean.CreateActiveIntroImageBean;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.GroupIdBean;
import com.lailem.app.jsonbean.activegroup.UploadFileInfo;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.tpl.CreateActiveEditTpl;
import com.lailem.app.tpl.CreateActiveImageTpl;
import com.lailem.app.ui.group.SelectLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.togglebutton.ToggleButton;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateActiveActivity extends BaseActivity implements OnPhotoListener {

    public static final int TPL_EDIT = 0;
    public static final int TPL_IMAGE = 1;

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.permissionRg)
    RadioGroup permissionRg;
    @Bind(R.id.permissionTip)
    TextView permissionTip_tv;
    @Bind(R.id.needContactToggle)
    ToggleButton needContactToggle;
    @Bind(R.id.needVerifyToggle)
    ToggleButton needVerifyToggle;
    @Bind(R.id.topic)
    EditText topic_et;
    @Bind(R.id.dateTime)
    TextView dateTime_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.contactArea)
    View contactArea;
    @Bind(R.id.contactAreaToggle)
    ToggleButton contactAreaToggle;
    @Bind(R.id.contactName)
    EditText contactName_et;
    @Bind(R.id.contactPhone)
    EditText contactPhone_et;
    @Bind(R.id.intro_ll)
    LinearLayout intro_ll;
    @Bind(R.id.publicType)
    RadioButton publicType_rb;
    @Bind(R.id.arrow_line_left_part)
    ImageView arrowLineLeftPart;
    @Bind(R.id.editActionBar)
    public View editActionBar;

    private String permission;

    private String date;

    private ArrayList<Object> models = new ArrayList<Object>();

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            final View rootView = getWindow().getDecorView();
            final int softKeyboardHeight = 100;
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
            int heightDiff = rootView.getBottom() - r.bottom;
            boolean isKeyboardShown = heightDiff > softKeyboardHeight * dm.density;
            if (isKeyboardShown && getCurrentFocus() != contactName_et && getCurrentFocus() != contactPhone_et) {
                editActionBar.setVisibility(View.VISIBLE);
            } else {
                editActionBar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.getInstance().onCreate(this, this);
        setContentView(R.layout.activity_create_active);
        ButterKnife.bind(this);
        initView();
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private void initView() {
        topbar.setTitle("活动名称").setBack("上一步", R.drawable.ic_topbar_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDraft();
                finish();
            }
        }).setRightText("完成");
        topic_et.setText(_Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_NAME));
        topic_et.setSelection(topic_et.length());

        intro_ll.setTag(0);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 200);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return new GestureDetector(_activity, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                }).onTouchEvent(event);
            }
        });

        //非自定义主题不可编辑
        if (!Const.TYPE_ID_CUSTOM.equals(_Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID))) {
            topic_et.setEnabled(false);
        }

        //使用当前用户数据设置用户名和手机号
        contactName_et.setText(ac.getProperty(Const.USER_NICKNAME));
        contactName_et.setSelection(contactName_et.length());
        contactPhone_et.setText(ac.getProperty(Const.USER_PHONE));

        contactName_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editActionBar.setVisibility(View.GONE);
                return false;
            }
        });
        contactPhone_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editActionBar.setVisibility(View.GONE);
                return false;
            }
        });

        contactAreaToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(ToggleButton toggleButton, boolean on) {
                if (on) {
                    contactArea.setVisibility(View.VISIBLE);
                } else {
                    contactArea.setVisibility(View.GONE);
                }
            }
        });


        permissionRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.privateType) {
                    permissionTip_tv.setText("活动将不公开，只能通过活动成员邀请才能加入该活动。");
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) arrowLineLeftPart.getLayoutParams();
                    params.width = (int) (publicType_rb.getWidth() + TDevice.dpToPixel(13f));
                    arrowLineLeftPart.setLayoutParams(params);
                } else if (checkedId == R.id.publicType) {
                    permissionTip_tv.setText("公开在附近活动中，附近的人能看到该活动信息及其动态。");
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) arrowLineLeftPart.getLayoutParams();
                    params.width = (int) TDevice.dpToPixel(13);
                    arrowLineLeftPart.setLayoutParams(params);
                }
            }
        });
        needContactToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(ToggleButton toggleButton, boolean on) {

            }
        });

        //处理已存在的草稿
        if (ac.isLogin()) {
            String cacheName = Func.buildCreateTypeCacheName(ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID));
            if (ac.isExistDataCache(cacheName)) {
                Object object = ac.readObject(cacheName);
                if (object instanceof CreateActiveCache) {
                    final CreateActiveCache cache = (CreateActiveCache) object;
                    //权限类型
                    permissionRg.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Const.PERMISSION_PUBLIC.equals(cache.getPermission())) {
                                permissionRg.check(R.id.publicType);
                            } else {
                                permissionRg.check(R.id.privateType);
                            }
                        }
                    });
                    //主题
                    if (!TextUtils.isEmpty(cache.getTopic())) {
                        topic_et.setText(cache.getTopic());
                        topic_et.setSelection(topic_et.length());
                    }

                    //日期
                    if (!TextUtils.isEmpty(cache.getStartDateTime())) {
                        dateTime_tv.setText(cache.getStartDateTime());
                    }

                    //地点
                    AddressInfo addressInfo = cache.getAddressInfo();
                    if (addressInfo != null && addressInfo.isValid()) {
                        address_tv.setTag(addressInfo);
                        address_tv.setText(addressInfo.getAddress());
                    }

                    //简介
                    if (cache.getIntro() != null && cache.getIntro().size() > 0) {
                        models.addAll(cache.getIntro());
                        for (int i = 0; i < models.size(); i++) {
                            Base base = (Base) models.get(i);
                            if (base.getItemViewType() == TPL_EDIT) {
                                CreateActiveEditTpl editTpl = new CreateActiveEditTpl(_activity, models);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                intro_ll.addView(editTpl, params);
                                editTpl.setBean();
                            } else if (base.getItemViewType() == TPL_IMAGE) {
                                CreateActiveImageTpl imageTpl = new CreateActiveImageTpl(_activity, models);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                intro_ll.addView(imageTpl, params);
                                imageTpl.setBean();
                            }
                        }
                    } else {
                        CreateActiveIntroEditBean initEditBean = new CreateActiveIntroEditBean();
                        models.add(initEditBean);
                        CreateActiveEditTpl editTpl = new CreateActiveEditTpl(_activity, models);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        intro_ll.addView(editTpl, params);
                        editTpl.setBean();
                    }

                    //是否需要联系方式
                    if (!TextUtils.isEmpty(cache.getJoinNeedContact())
                            && Const.ACTIVE_JOIN_NEED_CONTACT.equals(cache.getJoinNeedContact())) {
                        needContactToggle.toggleOn();
                    }

                    //联系人
                    if (!TextUtils.isEmpty(cache.getContactName())) {
                        contactName_et.setText(cache.getContactName());
                    }

                    //联系方式
                    if (!TextUtils.isEmpty(cache.getContactPhone())) {
                        contactPhone_et.setText(cache.getContactPhone());
                    }

                    //是否需要验证
                    if (!TextUtils.isEmpty(cache.getJoinNeedVerify())
                            && Const.ACTIVE_JOIN_NEED_VERIFY.equals(cache.getJoinNeedVerify())) {
                        needVerifyToggle.toggleOn();
                    }
                }
            }
        }
        if (models.size() == 0) {
            CreateActiveIntroEditBean initEditBean = new CreateActiveIntroEditBean();
            models.add(initEditBean);
            CreateActiveEditTpl editTpl = new CreateActiveEditTpl(_activity, models);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            intro_ll.addView(editTpl, params);
            editTpl.setBean();
        }
    }

    @OnClick({R.id.right_tv, R.id.dateTime_ll, R.id.address_ll, R.id.insertImage})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.right_tv:
                submit();
                break;
            case R.id.dateTime_ll:
                chooseDateTime();
                break;
            case R.id.address_ll:
                UIHelper.showSelectLoc(this);
                break;
            case R.id.insertImage:
                insertImage();
                break;
        }
    }

    /**
     * 活动介绍插入图片
     */
    private void insertImage() {
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            new ActionDialog(this).init(ActionDialog.DialogActionData.build(null, null, new ActionDialog.DialogActionData.ActionData("立即拍照", R.drawable.ic_by_camera_selector), new ActionDialog.DialogActionData.ActionData("图库上传", R.drawable.ic_by_gallery_selector)))
                    .setOnActionClickListener(new ActionDialog.OnActionClickListener() {

                        @Override
                        public void onActionClick(ActionDialog dialog, View View, int position) {
                            if (position == 0) {
                                PhotoManager.getInstance().photo(System.currentTimeMillis() + "");
                            } else if (position == 1) {
                                PhotoManager.getInstance().album(System.currentTimeMillis() + "");
                            }
                        }
                    }).show();
        } else {
            UIHelper.showLogin(this, false);
        }
    }

    /**
     * 选择结束时间
     */
    protected void chooseDateTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timeDialog = new TimePickerDialog(this, new OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                dateTime_tv.setText(date + " " + sdf.format(calendar.getTime()));
            }
        }, hourOfDay, minute, true);

        DatePickerDialog dateDialog = new DatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date = sdf.format(calendar.getTime());
                timeDialog.show();
            }
        }, year, monthOfYear, dayOfMonth);

        dateDialog.show();

    }

    public void submit() {
        String groupType = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_GROUP_TYPE);
        String typeId = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID);
        File pic = null;
        String picPath = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_LOCAL);
        if (!TextUtils.isEmpty(picPath)) {
            pic = new File(picPath);
        }
        String picMaterialId = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_MATERIALID);
        if (TextUtils.isEmpty(picMaterialId)) {
            picMaterialId = null;
        }
        String name = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_NAME);

        //父id
        String parentId = _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID);
        if (TextUtils.isEmpty(parentId)) {
            parentId = null;
        }

        //公开私有
        permission = null;
        if (permissionRg.getCheckedRadioButtonId() == R.id.privateType) {
            if (TextUtils.isEmpty(parentId)) {
                permission = Const.PERMISSION_PRIVATE;
            } else {
                permission = Const.PERMISSION_PRIVATE_IN_GROUP;
            }
        } else {
            permission = Const.PERMISSION_PUBLIC;
        }

        //主题
        String typeName = topic_et.getText().toString().trim();
        if (TextUtils.isEmpty(typeName)) {
            AppContext.showToast("请填写主题");
            return;
        }

        //开始日期和时间
        String dateTime = dateTime_tv.getText().toString().trim();
        if (TextUtils.isEmpty(dateTime)) {
            AppContext.showToast("请选择日期");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sdf.parse(dateTime);
            if (d.getTime() <= System.currentTimeMillis()) {
                AppContext.showToast("活动时间须晚于当前时间");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            AppContext.showToast("日期解析出错");
            return;
        }

        //地点
        if (address_tv.getTag() == null || !((AddressInfo) address_tv.getTag()).isValid()) {
            AppContext.showToast("请选择地点");
            return;
        }
        AddressInfo addressInfo = (AddressInfo) address_tv.getTag();


        //活动介绍
        String intro = buildHtml();
        if (TextUtils.isEmpty(intro)) {
            AppContext.showToast("请详细介绍一下您的活动内容及安排");
            return;
        }
        if (intro.length() < 10) {
            AppContext.showToast("活动介绍至少10个字或添加一张图片");
            return;
        }


        //是否需要填写联系人
        String joinNeedContact = needContactToggle.isToggleOn() ? Const.ACTIVE_JOIN_NEED_CONTACT : Const.ACTIVE_JOIN_NOT_NEED_CONTACT;

        //联系人
        String contactStr = null;
        if (contactAreaToggle.isToggleOn()) {
            String contactName = contactName_et.getText().toString().trim();
            String contactPhone = contactPhone_et.getText().toString().trim();
            if (!TextUtils.isEmpty(contactName) && TextUtils.isEmpty(contactPhone)) {
                //联系人不为空，联系方式为空
                AppContext.showToast("请填写联系方式");
                return;
            }
            if (TextUtils.isEmpty(contactName) && !TextUtils.isEmpty(contactPhone)) {
                //联系人为空，联系方式不为空
                AppContext.showToast("请填写联系人");
                return;
            }
            ArrayList<Contact> contacts = new ArrayList<Contact>();

            if (!TextUtils.isEmpty(contactName) && !TextUtils.isEmpty(contactPhone)) {

                if (!Func.isPhoneNumber(contactPhone)) {
                    AppContext.showToast("联系方式格式不正确");
                    return;
                }
                //联系人和联系方式都不为空
                contacts.add(new Contact(contactName, contactPhone));
                contactStr = new Gson().toJson(contacts);
            } else {
                AppContext.showToast("请填写联系人和联系方式");
                return;
            }
        }

        //是否需要验证
        String joinVerify = needVerifyToggle.isToggleOn() ? Const.ACTIVE_JOIN_NEED_VERIFY : Const.ACTIVE_JOIN_NOT_NEED_VERIFY;

        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            ApiClient.getApi().createGroup(this, ac.getLoginUid(), addressInfo.getAddress(), contactStr, null, groupType, intro, addressInfo.getLat(), addressInfo.getLng(), name, parentId, permission, pic, picMaterialId, dateTime, typeId, typeName, joinNeedContact, joinVerify);
        } else {
            UIHelper.showLogin(this, false);
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
        if (res.isOK()) {
            AppContext.showToast("创建成功");
            GroupIdBean bean = (GroupIdBean) res;
            GroupIdBean.GroupInfo groupInfo = bean.getGroupInfo();
            String typeId = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID);
            GroupUtils.joinActivity(this, groupInfo.getId(), groupInfo.getName(), buildText(), groupInfo.getSquareSPicName(), permission, typeId);
            UIHelper.showInvite(this, groupInfo.getName(), groupInfo.getSquareSPicName(), Const.INVITE_TYPE_ACTIVE, groupInfo.getId());

            if (TextUtils.isEmpty(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID))) {
                ac.setProperty(Const.USER_DYNAMIC_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_DYNAMIC_COUNT)) + 1) + "");
            }

            //删除草稿
            String cacheName = Func.buildCreateTypeCacheName(ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), typeId);
            if (ac.isExistDataCache(cacheName)) {
                ac.deleteFile(cacheName);
            }
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
        hideWaitDialog();
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SelectLocActivity.REQUEST_SELECT_LOC:
                    AddressInfo addressInfo = (AddressInfo) data.getExtras().getSerializable(SelectLocActivity.BUNDLE_KEY_ADDRESSINFO);
                    if (addressInfo.isValid()) {
                        address_tv.setText(addressInfo.getAddress());
                        address_tv.setTag(addressInfo);
                    } else {
                        AppContext.showToast("您选择位置信息无效");
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
    public void onBackPressed() {
        super.onBackPressed();
        saveDraft();
    }

    private void saveDraft() {
        if (ac.isLogin()) {
            //判断以及保存缓存
            CreateActiveCache cache = new CreateActiveCache();
            cache.setUserId(ac.getLoginUid());
            cache.setParentId(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
            cache.setTypeId(_Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID));
            //权限类型
            if (permissionRg.getCheckedRadioButtonId() == R.id.publicType) {
                cache.setPermission(Const.PERMISSION_PUBLIC);
            }

            //主题
            cache.setTopic(topic_et.getText().toString().trim());

            //日期
            cache.setStartDateTime(dateTime_tv.getText().toString().trim());

            //地点
            if (address_tv.getTag() != null) {
                AddressInfo addressInfo = (AddressInfo) address_tv.getTag();
                cache.setAddressInfo(addressInfo);
            }

            //简介
            cache.setIntro(models);

            //是否需要联系方式
            if (needContactToggle.isToggleOn()) {
                cache.setJoinNeedContact(Const.ACTIVE_JOIN_NEED_CONTACT);
            } else {
                cache.setJoinNeedContact(Const.ACTIVE_JOIN_NOT_NEED_CONTACT);
            }

            //联系人
            cache.setContactName(contactName_et.getText().toString().trim());
            //联系方式
            cache.setContactPhone(contactPhone_et.getText().toString().trim());

            //是否需要验证
            if (needVerifyToggle.isToggleOn()) {
                cache.setJoinNeedVerify(Const.ACTIVE_JOIN_NEED_VERIFY);
            } else {
                cache.setJoinNeedVerify(Const.ACTIVE_JOIN_NOT_NEED_VERIFY);
            }

            String cacheName = Func.buildCreateTypeCacheName(ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID));
            if (cache.isEmpty()) {
                //删除已经存在的草稿
                if (ac.isExistDataCache(cacheName)) {
                    Object object = ac.readObject(cacheName);
                    if (object instanceof CreateActiveCache) {
                        ac.deleteFile(cacheName);
                    }
                }
            } else {
                //保存草稿
                ac.saveObject(cache, cacheName);
            }
        }
    }

    private String buildHtml() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < models.size(); i++) {
            Object object = models.get(i);
            if (object instanceof CreateActiveIntroEditBean) {
                CreateActiveIntroEditBean bean = (CreateActiveIntroEditBean) object;
                sb.append("<p>" + bean.getText().replace("\n", "<br/>") + "</p>");
            } else if (object instanceof CreateActiveIntroImageBean) {
                CreateActiveIntroImageBean bean = (CreateActiveIntroImageBean) object;
                sb.append("<p><img src=\"" + bean.getRemotePath() + "\"/></p>");
            }
        }
        return sb.toString();
    }

    private String buildText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < models.size(); i++) {
            Object object = models.get(i);
            if (object instanceof CreateActiveIntroEditBean) {
                CreateActiveIntroEditBean bean = (CreateActiveIntroEditBean) object;
                sb.append(bean.getText());
            } else if (object instanceof CreateActiveIntroImageBean) {
                CreateActiveIntroImageBean bean = (CreateActiveIntroImageBean) object;
                sb.append("[图片]");
            }
        }
        return sb.toString();
    }

    @Override
    public void onPhoto(String tag, final ArrayList<PhotoBean> photos) {
        showWaitDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<UploadFileInfo> uploadFileInfos = new ArrayList<UploadFileInfo>();
                for (int i = 0; i < photos.size(); i++) {
                    PhotoBean photoBean = photos.get(i);
                    try {
                        UploadFileInfo res = (UploadFileInfo) ApiClient.getApi().uploadPicFile(AppContext.getInstance().getLoginUid(), new File(photoBean.getImagePath()));
                        if (res.isOK()) {
                            res.setLocalPath(photoBean.getImagePath());
                            uploadFileInfos.add(res);
                        } else {
                            AppContext.getInstance().handleErrorCode(_activity, res.errorCode, res.errorInfo);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (uploadFileInfos.size() > 0) {
                            final int curEditIndex = (int) intro_ll.getTag();
                            CreateActiveEditTpl curEditTpl = (CreateActiveEditTpl) intro_ll.getChildAt(curEditIndex);
                            EditText curEdit = (EditText) curEditTpl.findViewById(R.id.content);
                            int selectionStart = curEdit.getSelectionStart();

                            final CreateActiveIntroEditBean oldEditBean = (CreateActiveIntroEditBean) models.get(curEditIndex);
                            String text = oldEditBean.getText();
                            final CreateActiveIntroEditBean newEditBean = new CreateActiveIntroEditBean();
                            oldEditBean.setText(text.substring(0, selectionStart));
                            newEditBean.setText(text.substring(selectionStart));
                            models.add(curEditIndex + 1, newEditBean);

                            curEditTpl.setBean();

                            CreateActiveEditTpl newEditTpl = new CreateActiveEditTpl(_activity, models);
                            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            intro_ll.addView(newEditTpl, curEditIndex + 1, params2);
                            newEditTpl.setBean();

                            CreateActiveEditTpl tempTpl = null;
                            for (int i = 0; i < uploadFileInfos.size(); i++) {
                                final CreateActiveIntroImageBean imageBean = new CreateActiveIntroImageBean();
                                imageBean.setLocPath(uploadFileInfos.get(i).getLocalPath());
                                imageBean.setRemotePath(ApiClient.getFileUrl(uploadFileInfos.get(i).getFileName()));
                                BitmapFactory.Options o = new BitmapFactory.Options();
                                o.inJustDecodeBounds = true;
                                BitmapFactory.decodeFile(imageBean.getLocPath(), o);
                                imageBean.setWidth(o.outWidth);
                                imageBean.setHeight(o.outHeight);
                                models.add(i + i + curEditIndex + 1, imageBean);
                                final int tempIndex = i + i + curEditIndex + 1;

                                CreateActiveImageTpl imageTpl = new CreateActiveImageTpl(_activity, models);
                                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                intro_ll.addView(imageTpl, tempIndex, params3);
                                imageTpl.setBean();

                                if (i != uploadFileInfos.size() - 1) {
                                    final CreateActiveIntroEditBean tempBean = new CreateActiveIntroEditBean();
                                    models.add(i + i + 1 + curEditIndex + 1, tempBean);
                                    tempTpl = new CreateActiveEditTpl(_activity, models);
                                    LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    intro_ll.addView(tempTpl, tempIndex + 1, params4);
                                    tempTpl.setBean();
                                }
                            }

                            if (uploadFileInfos.size() > 1) {
                                if (tempTpl != null) {
                                    EditText editText = (EditText) tempTpl.findViewById(R.id.content);
                                    editText.requestFocus();
                                    editText.setSelection(0);
                                }
                            } else {
                                EditText editText = (EditText) newEditTpl.findViewById(R.id.content);
                                editText.requestFocus();
                                editText.setSelection(0);
                            }
                        }
                        hideWaitDialog();
                    }
                });
            }
        }).start();
    }
}
