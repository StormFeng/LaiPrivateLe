package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallback;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.utils.AcceptInviteManager.AcceptInviteInfo;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class AcceptInviteDialog extends Dialog implements ApiCallback {

    BaseActivity _activity;
    AcceptInviteInfo acceptInviteInfo;
    @Bind(R.id.title)
    TextView title_tv;
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.head)
    ImageView head;

    private static final int DEFAULT_THEME = R.style.confirm_dialog;

    public AcceptInviteDialog(Context context, AcceptInviteInfo acceptInviteInfo) {
        super(context, DEFAULT_THEME);
        init(context, acceptInviteInfo);
    }

    public AcceptInviteDialog(Context context, int theme, AcceptInviteInfo acceptInviteInfo) {
        super(context, theme);
        init(context, acceptInviteInfo);
    }

    private void init(final Context context, AcceptInviteInfo acceptInviteInfo) {
        this.acceptInviteInfo = acceptInviteInfo;
        this._activity = (BaseActivity) context;
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        View contentView = View.inflate(context, R.layout.dialog_accept_invite, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);

        Glide.with(_activity).load(StringUtils.getUri(acceptInviteInfo.uHead)).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(head);
        title_tv.setText(acceptInviteInfo.gName);
        if (Constant.gType_activity.equals(acceptInviteInfo.gType)) {
            content_tv.setText(acceptInviteInfo.uNick + "邀请您加入活动");
        } else {
            content_tv.setText(acceptInviteInfo.uNick + "邀请您加入群组");
        }
    }

    @OnClick({R.id.accept, R.id.ignore})
    public void clickShare(View v) {
        switch (v.getId()) {
            case R.id.accept:
                dismiss();
                _activity.showWaitDialog();
                if (AppContext.getInstance().isLogin()) {
                    ApiClient.getApi().acceptInvite(this, AppContext.getInstance().getLoginUid(), acceptInviteInfo.gId, acceptInviteInfo.inviteCode, acceptInviteInfo.uId);
                } else {
                    UIHelper.showLogin(_activity, false);
                }
                break;
            case R.id.ignore:
                dismiss();
                break;
        }
    }

    @Override
    public void onApiStart(String tag) {
    }

    @Override
    public void onApiLoading(long count, long current, String tag) {
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        if (res.isOK()) {
            _activity.hideWaitDialog();
            AppContext.showToast("接受邀请成功！");
            if (Constant.gType_activity.equals(acceptInviteInfo.gType)) {
                GroupUtils.joinActivity(_activity, acceptInviteInfo.gId, acceptInviteInfo.gName, acceptInviteInfo.gIntro, acceptInviteInfo.gSquareSPic, acceptInviteInfo.perm);
            } else {
                GroupUtils.joinGroup(_activity, acceptInviteInfo.gId, acceptInviteInfo.gName, acceptInviteInfo.gIntro, acceptInviteInfo.gSquareSPic, acceptInviteInfo.perm);
            }
        } else {
            _activity.hideWaitDialog();
            AppContext.showToast("接受邀请失败！");
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        _activity.hideWaitDialog();
        AppContext.showToast("接受邀请失败！");
    }

    @Override
    public void onParseError(String tag) {
        _activity.hideWaitDialog();
        AppContext.showToast("接受邀请失败！");
    }

}
