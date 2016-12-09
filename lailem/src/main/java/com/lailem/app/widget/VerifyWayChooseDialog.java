package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lailem.app.R;
import com.lailem.app.utils.Const;

public class VerifyWayChooseDialog extends Dialog implements android.view.View.OnClickListener {
    private Context context;

    private static final int DEFAULT_THEME = R.style.bottom_dialog;
    private OnVerifyWayChooseListener onVerifyWayChooseListener;

    public interface OnVerifyWayChooseListener {
        void onVerifyWayChoose(String name, String value);
    }

    public VerifyWayChooseDialog(Context context) {
        super(context, DEFAULT_THEME);
        init(context);
    }

    public VerifyWayChooseDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);

        View contentView = View.inflate(context, R.layout.dialog_verify_way, null);
        this.setContentView(contentView);
        contentView.findViewById(R.id.text).setOnClickListener(this);
        contentView.findViewById(R.id.voice).setOnClickListener(this);
        contentView.findViewById(R.id.cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text:
                if (onVerifyWayChooseListener != null) {
                    onVerifyWayChooseListener.onVerifyWayChoose("文字验证", Const.VERIFY_WAY_TEXT);
                }
                dismiss();
                break;
            case R.id.voice:
                if (onVerifyWayChooseListener != null) {
                    onVerifyWayChooseListener.onVerifyWayChoose("语音验证", Const.VERIFY_WAY_VOICE);
                }
                dismiss();
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    public void setOnVerifyWayChooseListener(OnVerifyWayChooseListener onVerifyWayChooseListener) {
        this.onVerifyWayChooseListener = onVerifyWayChooseListener;
    }
}
