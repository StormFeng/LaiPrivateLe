package com.lailem.app.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.mobstat.StatService;
import com.lailem.app.AppContext;
import com.lailem.app.AppManager;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallback;
import com.lailem.app.bean.Result;
import com.lailem.app.broadcast.AccountBroadcastReceiver;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.chat.util.MNotificationManager;
import com.lailem.app.utils.MyLoadViewHelper;
import com.lailem.app.utils.SystemBarTintManager;
import com.lailem.app.widget.WaitDialog;

/**
 * activity基类
 *
 * @author XuYang
 */
public class BaseActivity extends FragmentActivity implements ApiCallback {

    protected AppContext ac;
    protected FragmentManager fm;

    protected Intent _Intent;
    protected Bundle _Bundle;
    protected BaseActivity _activity;

    protected WaitDialog mWaitDialog;

    protected MyLoadViewHelper loadViewHelper = new MyLoadViewHelper();

    protected Runnable loginCallback;
    protected Runnable logoutCallback;

    protected AccountBroadcastReceiver accountBroadcastReceiver = new AccountBroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (AccountBroadcastReceiver.ACTION_LOGIN.equals(action)) {
                if (loginCallback != null) {
                    loginCallback.run();
                }
            } else if (AccountBroadcastReceiver.ACTION_REGISTER.equals(action)) {
                if (loginCallback != null) {
                    loginCallback.run();
                }
            } else if (AccountBroadcastReceiver.ACTION_LOGOUT.equals(action)) {
                if (logoutCallback != null) {
                    logoutCallback.run();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        //设置沉浸式状态栏
        setTranslucentStatus(isNeedTranslucentStatus());

        _activity = this;
        ac = (AppContext) getApplication();
        fm = getSupportFragmentManager();

        _Intent = getIntent();
        if (_Intent != null) {
            _Bundle = _Intent.getExtras();
        }

        BroadcastManager.registerAccountBroadcastReceiver(this, accountBroadcastReceiver);
    }

    protected boolean isNeedTranslucentStatus() {
        return true;
    }

    public void showWaitDialog(String msg, final boolean isNotBackFinish) {
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            return;
        }
        if (mWaitDialog == null) {
            mWaitDialog = new WaitDialog(this);
        }
        mWaitDialog.setCanceledOnTouchOutside(isNotBackFinish);
        mWaitDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (!isNotBackFinish) {
                    finish();
                }
            }
        });
        mWaitDialog.showMessage(msg);
    }

    public void showWaitDialog() {
        showWaitDialog("", false);
    }

    public void hideWaitDialog() {
        if (mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
        ac.setLoginCallbackMethodName("");
        ac.setLoginCallbackObject("");

        BroadcastManager.unRegisterAccountBroadcastReceiver(this, accountBroadcastReceiver);
    }

    protected void initLoginCallback(Runnable logoutCallback) {
        this.logoutCallback = logoutCallback;
    }


    /**
     * 返回结果到上一个activity
     *
     * @param resultCode
     * @param bundle
     */
    protected void setResult(int resultCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(resultCode, intent);
    }

//	@Override
//	protected void attachBaseContext(Context newBase) {
//		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//	}

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onApiStart(String tag) {
    }

    @Override
    public void onApiLoading(long count, long current, String tag) {
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        t.printStackTrace();
        onApiError(tag);
    }

    @Override
    public void onParseError(String tag) {
        onApiError(tag);
    }

    protected void onApiError(String tag) {
        AppContext.showToast("网络出错啦，请重试");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //应用恢复前端时取消即时聊天的通知
        MNotificationManager.getInstance(_activity).cancelNotification();
        StatService.onPageStart(this, getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPageEnd(this, getClass().getName());
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param on 使用topbar颜色显示状态栏
     */
    protected void setTranslucentStatus(boolean on, int colorResId) {
        if (on) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window win = getWindow();
                WindowManager.LayoutParams winParams = win.getAttributes();
                final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                if (on) {
                    winParams.flags |= bits;
                } else {
                    winParams.flags &= ~bits;
                }
                win.setAttributes(winParams);
            }
            if (on) {
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(colorResId);
            } else {
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(false);
            }
        }
    }

    /**
     * 使用默认颜色设置沉浸式状态栏
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        setTranslucentStatus(on, R.color.topbar_bg);
    }


}
