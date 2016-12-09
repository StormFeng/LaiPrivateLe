package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.Md5Utils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ActionDialog.DialogActionData;
import com.lailem.app.widget.ActionDialog.DialogActionData.ActionData;
import com.lailem.app.widget.ActionDialog.OnActionClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupManagerDialog extends Dialog {

    private Context context;
    private BaseActivity _activity;
    private AppContext ac;

    @Bind(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;
    @Bind(R.id.errorInfo)
    TextView errorInfo_tv;
    @Bind(R.id.validCode)
    EditText validCode_et;
    @Bind(R.id.password)
    TextView password_et;
    @Bind(R.id.getValid)
    ValidCodeButton getValid_tv;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.close)
    ImageView close_iv;
    @Bind(R.id.submit)
    TextView submit_tv;
    @Bind(R.id.inputViewFlipper)
    ViewFlipper inputViewFlipper;
    @Bind(R.id.toggleVerifyType)
    TextView toggleVerifyType_tv;

    private static final int DEFAULT_THEME = R.style.confirm_dialog;

    private OnSuccessListener onSuccessListener;
    private String groupId;
    private String groupName;

    public interface OnSuccessListener {
        void onSuccess();
    }

    public GroupManagerDialog(Context context, String groupId, String groupName) {
        super(context, DEFAULT_THEME);
        init(context);
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public GroupManagerDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this._activity = (BaseActivity) context;
        this.ac = (AppContext) context.getApplicationContext();
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        View contentView = View.inflate(context, R.layout.dialog_group_manage, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public OnSuccessListener getOnSuccessListener() {
        return onSuccessListener;
    }

    @OnClick(R.id.toggleVerifyType)
    public void toggleVerifyType() {
        if (inputViewFlipper.getDisplayedChild() == 0) {
            // 手机号验证
            toggleVerifyType_tv.setText("手机号验证");
            inputViewFlipper.setDisplayedChild(1);
        } else {
            // 密码验证
            toggleVerifyType_tv.setText("密码验证");
            inputViewFlipper.setDisplayedChild(0);
        }
    }

    @OnClick(R.id.close)
    public void close() {
        dismiss();
    }

    @OnClick(R.id.getValid)
    public void clickGetValid() {
        // 验证码验证
        getValid_tv.startByAuthCode(ac.getProperty(Const.USER_PHONE), Const.AUTH_TYPE_DISBAND_GROUP, new ValidCodeButton.OnActionListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(boolean isNeedVerifyCode, String transId) {

            }

            @Override
            public void onFail(String msg) {
                errorInfo_tv.setText(msg);
            }
        });
    }

    @OnClick(R.id.submit)
    public void submit() {
        if (inputViewFlipper.getDisplayedChild() == 0) {
            final String validCode = validCode_et.getText().toString().trim();
            if (TextUtils.isEmpty(validCode)) {
                AppContext.showToast("请输入验证码");
                return;
            }
            // 验证码验证
            ApiClient.getApi().verifyAuthCode(new ApiCallbackAdapter() {
                @Override
                public void onApiStart(String tag) {
                    viewSwitcher.setDisplayedChild(0);
                    progressBar.setVisibility(View.VISIBLE);
                    close_iv.setVisibility(View.INVISIBLE);
                    validCode_et.setEnabled(false);
                    password_et.setEnabled(false);
                    submit_tv.setEnabled(false);
                }

                @Override
                public void onApiSuccess(Result res, String tag) {
                    progressBar.setVisibility(View.INVISIBLE);
                    close_iv.setVisibility(View.VISIBLE);
                    validCode_et.setEnabled(true);
                    password_et.setEnabled(true);
                    submit_tv.setEnabled(true);
                    if (res.isOK()) {
                        disbandGroup(validCode, null, null);
                    } else {
                        errorInfo_tv.setText(res.errorInfo);
                        viewSwitcher.setDisplayedChild(1);
                    }
                }

                @Override
                protected void onApiError(String tag) {
                    progressBar.setVisibility(View.INVISIBLE);
                    close_iv.setVisibility(View.VISIBLE);
                    validCode_et.setEnabled(true);
                    password_et.setEnabled(true);
                    submit_tv.setEnabled(true);
                    errorInfo_tv.setText("网络出错啦，请重试");
                    viewSwitcher.setDisplayedChild(1);
                }
            }, validCode, ac.getProperty(Const.USER_PHONE), null);

        } else {
            // 使用密码验证
            String password = password_et.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                AppContext.showToast("请输入密码");
                return;
            }
            ApiClient.getApi().disbandGroup(new ApiCallbackAdapter() {
                @Override
                public void onApiStart(String tag) {
                    viewSwitcher.setDisplayedChild(0);
                    progressBar.setVisibility(View.VISIBLE);
                    close_iv.setVisibility(View.INVISIBLE);
                    validCode_et.setEnabled(false);
                    password_et.setEnabled(false);
                    submit_tv.setEnabled(false);
                }

                @Override
                public void onApiSuccess(Result res, String tag) {
                    progressBar.setVisibility(View.INVISIBLE);
                    close_iv.setVisibility(View.VISIBLE);
                    validCode_et.setEnabled(true);
                    password_et.setEnabled(true);
                    submit_tv.setEnabled(true);
                    if (res.isOK()) {
                        AppContext.showToast("解散群组成功");
                        dismiss();
                        showTipDialog();
                    } else {
                        errorInfo_tv.setText(res.errorInfo);
                        viewSwitcher.setDisplayedChild(1);
                    }
                }

                @Override
                protected void onApiError(String tag) {
                    progressBar.setVisibility(View.INVISIBLE);
                    close_iv.setVisibility(View.VISIBLE);
                    validCode_et.setEnabled(true);
                    password_et.setEnabled(true);
                    submit_tv.setEnabled(true);
                    errorInfo_tv.setText("网络出错啦，请重试");
                    viewSwitcher.setDisplayedChild(1);
                }
            }, ac.getLoginUid(), this.groupId, null, null, Md5Utils.md5(password));
        }
    }

    private void disbandGroup(final String authCode, final String transId, final String password) {
        ApiClient.getApi().disbandGroup(new ApiCallbackAdapter() {
            @Override
            public void onApiStart(String tag) {
                viewSwitcher.setDisplayedChild(0);
                progressBar.setVisibility(View.VISIBLE);
                close_iv.setVisibility(View.INVISIBLE);
                validCode_et.setEnabled(false);
                password_et.setEnabled(false);
                submit_tv.setEnabled(false);
            }

            @Override
            public void onApiSuccess(Result res, String tag) {
                progressBar.setVisibility(View.INVISIBLE);
                close_iv.setVisibility(View.VISIBLE);
                validCode_et.setEnabled(true);
                password_et.setEnabled(true);
                submit_tv.setEnabled(true);
                if (res.isOK()) {
                    AppContext.showToast("解散群组成功");
                    dismiss();
                    showTipDialog();
                } else {
                    errorInfo_tv.setText(res.errorInfo);
                    viewSwitcher.setDisplayedChild(1);
                }
            }

            @Override
            protected void onApiError(String tag) {
                progressBar.setVisibility(View.INVISIBLE);
                close_iv.setVisibility(View.VISIBLE);
                validCode_et.setEnabled(true);
                password_et.setEnabled(true);
                submit_tv.setEnabled(true);
                errorInfo_tv.setText("网络出错啦，请重试");
                viewSwitcher.setDisplayedChild(1);
            }
        }, ac.getLoginUid(), this.groupId, authCode, transId, password);
    }

    /**
     * 解散群组成功提示对话框
     */
    private void showTipDialog() {
        ActionDialog dialog = new ActionDialog(_activity);
        ActionData actionData = new ActionData("知道了", R.drawable.ic_dialog_ok_selector);
        ArrayList<ActionData> actionDatas = new ArrayList<ActionData>();
        actionDatas.add(actionData);
        DialogActionData dialogActionData = new DialogActionData(groupName + "已经解散", "您所创建的群组\"" + groupName + "\"已解散", actionDatas);
        dialog.init(dialogActionData);
        dialog.setOnActionClickListener(new OnActionClickListener() {

            @Override
            public void onActionClick(ActionDialog dialog, View View, int position) {
                if (position == 0) {
                    handQuitOrDisbandGroup();
                }
            }
        });
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                handQuitOrDisbandGroup();
            }
        });
        dialog.show();
    }

    private void handQuitOrDisbandGroup() {
        GroupUtils.exitGroup(_activity, groupId);
        UIHelper.showMainWithClearTop(_activity);
    }
}
