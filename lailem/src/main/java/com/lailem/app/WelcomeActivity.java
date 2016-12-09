package com.lailem.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;

import com.lailem.app.chat.util.FloatWindowManager;
import com.lailem.app.ui.main.MainActivity;
import com.lailem.app.utils.ConfigManager;
import com.lailem.app.widget.ConfirmDialog;

public class WelcomeActivity extends Activity {
    private ConfigManager.OnConfigUpdateCallback callback;
    private ConfirmDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppManager.getActivity(MainActivity.class) != null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, AppStart.class);
                startActivity(intent);
                finish();
            }
        }, 2000);


//        callback = new ConfigManager.OnConfigUpdateCallback() {
//
//            @Override
//            public void onStart() {
//            }
//
//            @Override
//            public void onSuccess() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(WelcomeActivity.this, AppStart.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }, 2000);
//            }
//
//
//            @Override
//            public void onError() {
//                if (dialog == null) {
//                    dialog = new ConfirmDialog(WelcomeActivity.this, R.style.confirm_dialog).config("错误提示", "初始化失败，请检查网络设置", "重试", "检查设置", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ConfigManager.getConfigManager().checkConfigVersion(callback);
//                        }
//                    }, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    });
//
//                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            ConfigManager.getConfigManager().checkConfigVersion(callback);
//                        }
//                    });
//                }
//                dialog.show();
//            }
//        };
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        ConfigManager.getConfigManager().checkConfigVersion(callback);
//
//    }
}
