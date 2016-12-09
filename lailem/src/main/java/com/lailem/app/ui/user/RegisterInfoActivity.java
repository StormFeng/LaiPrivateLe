package com.lailem.app.ui.user;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.SelectCityDialog;
import com.lailem.app.widget.SelectCityDialog.OnChooseListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterInfoActivity extends BaseActivity implements TextWatcher, OnChooseListener {
    @Bind(R.id.name)
    TextView name_et;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        intView();
    }

    private void intView() {
        name_et.addTextChangedListener(this);
        birthday_tv.addTextChangedListener(this);
        address_tv.addTextChangedListener(this);
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.birthday_ll)
    public void clickBirthday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {

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
            dialog = new SelectCityDialog(this, "", "", "", "");
        }
        dialog.setOnChooseListener(this);
        dialog.show();
    }

    @OnClick(R.id.submit)
    public void submit() {
        String name = name_et.getText().toString().trim();
        String birthday = birthday_tv.getText().toString().trim();
        if (sexRg.getCheckedRadioButtonId() == R.id.male) {
            sex = Const.MALE;
        } else {
            sex = Const.FEMALE;
        }
        UIHelper.showRegisterAvatar(this, _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_PHONE), _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_PASSWORD),
                _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_VALIDCODE), _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_TRANSID), name, birthday, sex, provinceId, cityId);

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
            this.provinceId = "";
            this.cityId = cityId;
        } else {
            address_tv.setText(provinceName + " " + cityName);
            this.provinceId = provinceId;
            this.cityId = cityId;
        }
    }

}
