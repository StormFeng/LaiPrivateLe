package com.lailem.app.ui.create;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.listener.OnAudioRecordListener;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.ui.create_old.dynamic.model.TextModel;
import com.lailem.app.utils.AudioRecordUtils;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.DynamicTaskUtil;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XuYang on 15/12/16.
 */
public class NewCreateDynamicActivity extends BaseActivity {

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.text)
    EditText text_et;
    @Bind(R.id.actionBar)
    CreateDynamicActionBar actionBar;

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
            if (isKeyboardShown) {
                actionBar.toggleContentArea(false);
            } else {
                actionBar.toggleContentArea(true);
            }
        }
    };
    private AudioRecordUtils recordUtilsForPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dynamic);
        ButterKnife.bind(this);
        PhotoManager.getInstance().onCreate(this, actionBar.getOnPhotoListener());
        initView();
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private void initView() {
        topbar.setTitle("发表动态").setLeftImageButton(R.drawable.ic_topbar_close, UIHelper.finish(this)).setRightText("发表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        if (recordUtilsForPermission == null) {
            recordUtilsForPermission = new AudioRecordUtils();
            recordUtilsForPermission.setMaxDuration(10 * 1000);
            recordUtilsForPermission.setOnSoundRecordListener(new OnAudioRecordListener() {
                @Override
                public void onAudioRecordStart() {

                }

                @Override
                public void onAudioRecordDb(double db) {

                }

                @Override
                public void onAudioRecordEnd(String path, int time) {

                }

                @Override
                public void onAudioRecordTime(int time) {

                }

                @Override
                public void onAudioRecordCancel() {

                }
            });
        }
        recordUtilsForPermission.start(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recordUtilsForPermission.stop();
            }
        }, 1000);
    }

    private void submit() {
        ArrayList<Object> models = actionBar.buildModels();
        if (text_et.length() > 0) {
            TextModel textModel = new TextModel(text_et.getText().toString().trim());
            models.add(0, textModel);
        }

        if (models.size() == 0) {
            AppContext.showToast("请发表点什么");
            return;
        }

        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            String key = DynamicTaskUtil.buildTaskKey(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), Const.DYNAMIC_STATE_SENDING, Const.DYNA_FROM_DISABLE_SORT);
            ac.saveObject(models, key);
            BroadcastManager.sendDynamicTaskBroadcast(_activity, DynamicTaskReceiver.ACTION_ADD_TASK, key);
            finish();
        } else {
            UIHelper.showLogin(this, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoManager.getInstance().onActivityResult(requestCode, resultCode, data);
        actionBar.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().onDestory();
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

}
