package com.lailem.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.utils.BitmapUtil;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.ImageUtils;
import com.lailem.app.utils.TDevice;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActiveQrCodeDialog extends Dialog {

    private Context context;
    private View contentView;
    private Activity _activity;

    private View.OnClickListener onClickListener;

    private static final int DEFAULT_THEME = R.style.confirm_dialog;

    @Bind(R.id.activeName)
    TextView activeName_tv;
    @Bind(R.id.date)
    TextView date_tv;
    @Bind(R.id.qrCode)
    ImageView qrCode_iv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.contentArea)
    View contentArea;

    private String activeName, date, groupId, address;
    private Bitmap qrCodeBitmap;

    public ActiveQrCodeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public ActiveQrCodeDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public ActiveQrCodeDialog(Context context) {
        super(context, DEFAULT_THEME);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this._activity = (Activity) context;
        Window w = this.getWindow();
        LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = LayoutParams.MATCH_PARENT;
        lp.width = LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);

        contentView = View.inflate(context, R.layout.dialog_active_qrcode, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (TDevice.getScreenWidth() - TDevice.dpToPixel(50f)), LinearLayout.LayoutParams.WRAP_CONTENT);
        contentArea.setLayoutParams(params);


    }

    @OnClick(R.id.close)
    public void close() {
        dismiss();
    }

    @OnClick(R.id.saveQrCode)
    public void saveQrCode() {
        if (qrCodeBitmap != null) {
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            AppContext.showToast("二维码已经保存到系统相册");
                            break;
                        case 1:
                            AppContext.showToast("二维码保存失败，请检查SD是否可用");
                            break;
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Environment.DIRECTORY_DCIM + File.separator + System.currentTimeMillis() + ".jpg";
                        ImageUtils.saveImageToSD(_activity, filePath, qrCodeBitmap, 100);
                        handler.sendEmptyMessage(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(1);
                    }
                }
            }).start();
        }
        dismiss();
    }

    public void setGroupInfo(String activeName, String date, String groupId, String address) {
        this.activeName = activeName;
        this.date = date;
        this.groupId = groupId;
        this.address = address;

        activeName_tv.setText(activeName);
        date_tv.setText(Func.formatTime(date));
        address_tv.setText(address);

        String url = Const.ACTIVE_PATTERN + groupId;
        try {
            qrCodeBitmap = BitmapUtil.create2DCoderBitmap(url, 300, 300);
            qrCode_iv.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            AppContext.showToast("二维码生成失败");
        }
    }


}
