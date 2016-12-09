package com.lailem.app.widget;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.StringUtils;

public class ValidCodeButton extends RadioButton {

    public static int MAX = 60;
    private AppContext ac;
    private Context context;
    private int num = MAX;
    private Handler handler;
    private Runnable runnable;

    private String normalTipText = "验证码";

    private OnTapStartListener onTapStartListener;

    public interface OnActionListener {
        void onStart();

        /**
         * 是否需要进一步验证验证码
         *
         * @param isNeedVerifyCode
         */
        void onSuccess(boolean isNeedVerifyCode, String transId);

        void onFail(String msg);
    }

    public static class SimpleOnActionListener implements OnActionListener {

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(boolean isNeedVerifyCode, String transId) {

        }

        @Override
        public void onFail(String msg) {

        }

    }

    public interface OnTapStartListener {
        void onTapStart();
    }

    public ValidCodeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ValidCodeButton(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        ac = (AppContext) context.getApplicationContext();
        setButtonDrawable(null);

        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isChecked()) {
                    if (onTapStartListener != null) {
                        onTapStartListener.onTapStart();
                    }
                }
            }
        });

        stopCounting();

    }

    public void setNormalTipText(String normalTipText) {
        this.normalTipText = normalTipText;
    }


    /**
     * 通过短信获取验证码
     *
     * @param phone
     */
    public void startByAuthCode(String phone, String authType, final OnActionListener onActionListener) {
        startCounting();
        if (!checkPhone(phone)) {
            return;
        }
        ApiClient.getApi().authCode(new ApiCallbackAdapter() {
            @Override
            public void onApiStart(String tag) {
                if (onActionListener != null) {
                    onActionListener.onStart();
                }
            }

            @Override
            public void onApiSuccess(Result res, String tag) {
                if (res.isOK()) {
                    if (onActionListener != null) {
                        onActionListener.onSuccess(true, "");
                    }
                } else {
                    if (onActionListener != null) {
                        onActionListener.onFail(res.errorInfo);
                    }
                    stopCounting();
                }
            }

            @Override
            protected void onApiError(String tag) {
                stopCounting();
                if (onActionListener != null) {
                    onActionListener.onFail("网络异常");
                }
            }
        }, authType, phone);
    }

    /**
     * 开始倒计时
     */
    public void startCounting() {
        stopCounting();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (num > 1) {
                    num--;
                    setText(String.format("%s秒", num));
                    handler.postDelayed(this, 1000);
                } else {
                    stopCounting();
                }
            }
        };
        handler.postDelayed(runnable, 0);// 打开定时器，执行操作
        setChecked(true);
    }

    /**
     * 停止计时
     */
    public void stopCounting() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        setChecked(false);
        setText(normalTipText);
        num = MAX;
    }

    /**
     * 校检手机号
     *
     * @param phone
     * @return
     */
    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            AppContext.showToast("请输入手机号");
            if (isChecked()) {
                setChecked(false);
            }
            return false;
        }
        if (!StringUtils.isMobileNO(phone)) {
            AppContext.showToast("请输入正确格式的手机号");
            if (isChecked()) {
                setChecked(false);
            }
            return false;
        }
        return true;
    }

    public void setOnTapStartListener(OnTapStartListener onTapStartListener) {
        this.onTapStartListener = onTapStartListener;
    }
}
