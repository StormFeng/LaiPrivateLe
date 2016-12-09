package com.lailem.app.ui.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.WheelView;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XuYang on 15/11/2.
 */
public class ModifyAgeActivity extends BaseActivity {

    public static final String BUNDLE_KEY_BIRTHDAY = "bithday";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.yearWheel)
    WheelView yearWheel;
    @Bind(R.id.monthWheel)
    WheelView monthWheel;
    @Bind(R.id.dayWheel)
    WheelView dayWheel;

    private String[] yearArr;
    private String[] monthArr = new String[12];
    private String[] dayArr = new String[31];

    private int curYear = 0;
    private int curMonth;
    private int curDay;

    private int yearIndex = 60;
    private int monthIndex = 0;
    private int dayIndex = 0;

    private View.OnClickListener saveClicKListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String birthday = yearArr[yearIndex] + "-" + monthArr[monthIndex] + "-" + dayArr[dayIndex];
            ApiClient.getApi().changePersonInfo(_activity, ac.getLoginUid(), birthday, null, null, null, null, null, null, null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_age);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("修改年龄").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).setRightText("保存", saveClicKListener);


        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay = calendar.get(Calendar.DAY_OF_MONTH);

        yearIndex = curYear - 1900;
        monthIndex = curMonth;
        dayIndex = curDay - 1;

        String birthday = _Bundle.getString(BUNDLE_KEY_BIRTHDAY);
        String selectYear = "";
        String selectMonth = "";
        String selectDay = "";
        if (!TextUtils.isEmpty(birthday)) {
            String[] arr = birthday.split("-");
            if (arr.length == 3) {
                selectYear = arr[0];
                selectMonth = arr[1];
                selectDay = arr[2];
            }
        }

        yearArr = new String[curYear - 1900 + 1];

        for (int i = 1900; i <= curYear; i++) {
            String year = Func.formatToTwo(i) + "";
            yearArr[i - 1900] = year;
            if (year.equals(selectYear)) {
                yearIndex = i - 1900;
            }
        }
        for (int i = 1; i <= 12; i++) {
            String month = Func.formatToTwo(i) + "";
            monthArr[i - 1] = month;
            if (month.equals(selectMonth)) {
                monthIndex = i - 1;
            }
        }
        for (int i = 1; i <= 31; i++) {
            String day = Func.formatToTwo(i) + "";
            dayArr[i - 1] = day;
            if (day.equals(selectDay)) {
                dayIndex = i - 1;
            }
        }

        yearWheel.setOffset(2);
        monthWheel.setOffset(2);
        dayWheel.setOffset(2);

        yearWheel.setItems(Arrays.asList(yearArr));
        monthWheel.setItems(Arrays.asList(monthArr));
        dayWheel.setItems(Arrays.asList(dayArr));

        yearWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                yearIndex = selectedIndex - 2;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(yearArr[yearIndex]));
                cal.set(Calendar.MONTH, Integer.parseInt(monthArr[monthIndex]) - 1);
                int maxDate = cal.getActualMaximum(Calendar.DATE);
                if (Integer.parseInt(dayArr[dayIndex]) > maxDate) {
                    dayWheel.setSeletion(maxDate - 1);
                }
                content_tv.setText(getAge());
            }
        });
        monthWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                monthIndex = selectedIndex - 2;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(yearArr[yearIndex]));
                cal.set(Calendar.MONTH, Integer.parseInt(monthArr[monthIndex]) - 1);
                int maxDate = cal.getActualMaximum(Calendar.DATE);
                if (Integer.parseInt(dayArr[dayIndex]) > maxDate) {
                    dayWheel.setSeletion(maxDate - 1);
                }
                content_tv.setText(getAge());
            }
        });
        dayWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(yearArr[yearIndex]));
                cal.set(Calendar.MONTH, Integer.parseInt(monthArr[monthIndex]) - 1);
                int maxDate = cal.getActualMaximum(Calendar.DATE);
                if (Integer.parseInt(dayArr[selectedIndex - 2]) > maxDate) {
                    dayWheel.setSeletion(maxDate - 1);
                } else {
                    dayIndex = selectedIndex - 2;
                }
                content_tv.setText(getAge());
            }
        });

        yearWheel.setSeletion(yearIndex);
        monthWheel.setSeletion(monthIndex);
        dayWheel.setSeletion(dayIndex);

        try {
            content_tv.setText(getAge());
        } catch (Exception e) {
            content_tv.setText("0岁");
        }


    }

    private String getAge() {
        String birthday = yearArr[yearIndex] + "-" + monthArr[monthIndex] + "-" + dayArr[dayIndex];
        try {
            return String.valueOf(Func.getAge(birthday)) + "岁";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
        topbar.getRight_tv().setEnabled(false);
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        topbar.getRight_tv().setEnabled(true);
        if (res.isOK()) {
            ac.setProperty(Const.USER_BIRTHDAY, yearArr[yearIndex] + "-" + monthArr[monthIndex] + "-" + dayArr[dayIndex]);
            setResult(RESULT_OK);
            finish();
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
        topbar.getRight_tv().setEnabled(true);
    }
}
