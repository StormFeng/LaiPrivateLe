package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lailem.app.R;
import com.lailem.app.utils.Const;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupHomeActionDialog extends Dialog implements View.OnClickListener {
    private static final int DEFAULT_THEME = R.style.bottom_dialog;


    private Context context;
    private OnActionClickListener onActionClickListener;

    public interface OnActionClickListener {
        void onClickReport();

        void onClickShare();

        void onClickQuit();
    }

    public GroupHomeActionDialog(Context context, String roleType) {
        super(context, DEFAULT_THEME);
        init(context, roleType);
    }

    private void init(Context context, String roleType) {
        this.context = context;

        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);

        View contentView = null;
        if (Const.ROLE_TYPE_CREATOR.equals(roleType)
                || Const.ROLE_TYPE_MANAGER.equals(roleType)) {
            contentView = View.inflate(context, R.layout.dialog_group_home_action_for_creator, null);
        } else if (Const.ROLE_TYPE_NORMAL.equals(roleType)) {
            contentView = View.inflate(context, R.layout.dialog_group_home_action_for_member, null);
        } else if (Const.ROLE_TYPE_STRANGER.equals(roleType)) {
            contentView = View.inflate(context, R.layout.dialog_group_home_action_for_public, null);
        }
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);


    }

    @OnClick({R.id.report, R.id.share, R.id.quit, R.id.cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report:
                if (onActionClickListener != null) {
                    onActionClickListener.onClickReport();
                }
                dismiss();
                break;
            case R.id.share:
                if (onActionClickListener != null) {
                    onActionClickListener.onClickShare();
                }
                dismiss();
                break;
            case R.id.quit:
                if (onActionClickListener != null) {
                    onActionClickListener.onClickQuit();
                }
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
    }
}
