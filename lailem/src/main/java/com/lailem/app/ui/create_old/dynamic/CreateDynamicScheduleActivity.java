package com.lailem.app.ui.create_old.dynamic;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.ui.create_old.dynamic.model.ScheduleModel;
import com.lailem.app.ui.group.SelectLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.DynamicTaskUtil;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.togglebutton.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateDynamicScheduleActivity extends BaseActivity {
    public static final String BUNDLE_KEY_DIRECT_PUBLISH = "direct_publish";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.dateTime)
    TextView dateTime_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.needContactToggle)
    ToggleButton needContactToggle;
    @Bind(R.id.contactArea)
    View contactArea;
    @Bind(R.id.contactName)
    EditText contactName_et;
    @Bind(R.id.contactPhone)
    EditText contactPhone_et;
    @Bind(R.id.topic)
    EditText topic_et;
    @Bind(R.id.brief)
    EditText brief_et;

    private String date;

    private ScheduleModel model = new ScheduleModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dynamic_schedule);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("日程").setLeftImageButton(R.drawable.ic_topbar_close, UIHelper.finish(this)).setRightText("完成");
        //使用当前用户数据设置用户名和手机号
        contactName_et.setText(ac.getProperty(Const.USER_NICKNAME));
        contactName_et.setSelection(contactName_et.length());
        contactPhone_et.setText(ac.getProperty(Const.USER_PHONE));

        needContactToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(ToggleButton toggleButton, boolean on) {
                if (on) {
                    contactArea.setVisibility(View.VISIBLE);
                } else {
                    contactArea.setVisibility(View.GONE);
                }
            }
        });

    }

    @OnClick({R.id.right_tv, R.id.showSchedule, R.id.dateTime_ll, R.id.address_ll})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.right_tv:
                submit();
                break;
            case R.id.showSchedule:
                if (buildMode()) {
                    UIHelper.showSchedule(this, model);
                }
                break;
            case R.id.dateTime_ll:
                chooseDateTime();
                break;
            case R.id.address_ll:
                UIHelper.showSelectLoc(this);
                break;
        }
    }

    /**
     * 提交
     */
    private void submit() {
        if (buildMode()) {
            boolean isDirectPublish = _Bundle.getBoolean(BUNDLE_KEY_DIRECT_PUBLISH);
            if (isDirectPublish) {
                if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
                    String key = DynamicTaskUtil.buildTaskKey(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), Const.DYNAMIC_STATE_SENDING, Const.DYNA_FROM_DISABLE_SORT);
                    ArrayList<Object> models = new ArrayList<Object>();
                    models.add(model);
                    ac.saveObject(models, key);
                    BroadcastManager.sendDynamicTaskBroadcast(_activity, DynamicTaskReceiver.ACTION_ADD_TASK, key);
                    finish();
                } else {
                    UIHelper.showLogin(this, false);
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable(CreateDynamicActivity.BUNDLE_KEY_SCHEDULE, model);
                setResult(RESULT_OK, bundle);
                finish();
            }
        }
    }

    /**
     * 组装日程对象
     *
     * @return
     */
    private boolean buildMode() {

        //开始日期和时间
        String dateTime = dateTime_tv.getText().toString().trim();
        if (TextUtils.isEmpty(dateTime)) {
            AppContext.showToast("请选择日期");
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sdf.parse(dateTime);
            if (d.getTime() <= System.currentTimeMillis()) {
                AppContext.showToast("活动时间须晚于当前时间");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            AppContext.showToast("日期解析出错");
            return false;
        }
        model.getContent().setStartTime(dateTime);

        //地点
        if (address_tv.getTag() != null) {
            AddressInfo addressInfo = (AddressInfo) address_tv.getTag();
            if (addressInfo.isValid()) {
                model.getContent().setAddress(addressInfo.getAddress());
                model.getContent().setLon(addressInfo.getLng());
                model.getContent().setLat(addressInfo.getLat());
            }
        }

        //联系人
        if (needContactToggle.isToggleOn()) {
            String contactName = contactName_et.getText().toString().trim();
            String contactPhone = contactPhone_et.getText().toString().trim();
            if (!TextUtils.isEmpty(contactName) && TextUtils.isEmpty(contactPhone)) {
                //联系人不为空，联系方式为空
                AppContext.showToast("请填写联系方式");
                return false;
            }
            if (TextUtils.isEmpty(contactName) && !TextUtils.isEmpty(contactPhone)) {
                //联系人为空，联系方式不为空
                AppContext.showToast("请填写联系人");
                return false;
            }
            ArrayList<Contact> contacts = new ArrayList<Contact>();
            model.getContent().getContact().clear();
            if (!TextUtils.isEmpty(contactName) && !TextUtils.isEmpty(contactPhone)) {

                if (!Func.isPhoneNumber(contactPhone)) {
                    AppContext.showToast("联系方式格式不正确");
                    return false;
                }
                //联系人和联系方式都不为空
                contacts.add(new Contact(contactName, contactPhone));
                model.getContent().getContact().addAll(contacts);
            } else {
                AppContext.showToast("请填写联系人和联系方式");
                return false;
            }
        }

        //主题
        String topic = topic_et.getText().toString().trim();
        if (TextUtils.isEmpty(topic)) {
            AppContext.showToast("请填写主题");
            return false;
        }
        model.getContent().setTopic(topic);

        //活动介绍
        String brief = brief_et.getText().toString().trim();
        if (TextUtils.isEmpty(brief)) {
            AppContext.showToast("请填写活动介绍");
            return false;
        }

        if (brief.length() < 10 || brief.length() > 1024) {//10-1024
            AppContext.showToast("活动介绍要求10-1024个字");
            return false;
        }
        model.getContent().setContent(brief);

        return true;
    }

    /**
     * 选择日期
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            }
        }
    }

}
