package com.lailem.app.ui.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.BitmapUtil;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.FileUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QrCodeActivity extends BaseActivity {

    public static final String BUNDLE_KEY_TYPE = "type";
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_CONTENT = "content";
    public static final String BUNDLE_KEY_IMAGEURL = "imageUrl";
    public static final String BUNDLE_INVITE_URL = "inviteUrl";

    private String logoPath = FileUtils.getImageDir(this) + "/logo.png";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.qrCode)
    ImageView qrCode_iv;

    private Bitmap qrCodeBitmap;
    private String url = "";
    private String title = "";
    private String content = "";
    private String imageUrl = "";

    private String pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_qrcode);
        ButterKnife.bind(this);
        String groupId = _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID);


        title = _Bundle.getString(BUNDLE_KEY_TITLE);
        content = _Bundle.getString(BUNDLE_KEY_CONTENT);


        switch (_Bundle.getInt(BUNDLE_KEY_TYPE)) {
            case Const.QRCODE_TYPE_ACTIVE:
                url = Const.ACTIVE_PATTERN + groupId;
                pageTitle = "活动二维码";
                imageUrl = logoPath;
                break;
            case Const.QRCODE_TYPE_GROUP:
                url = Const.GROUP_PATTERN + groupId;
                pageTitle = "群组二维码";
                imageUrl = logoPath;
                break;
            case Const.QRCODE_TYPE_VOTE_ACTIVE:
                url = Const.VOTE_ACTIVE_PATTERN + groupId;
                pageTitle = "活动二维码";
                imageUrl = logoPath;
                break;
            case Const.QRCODE_TYPE_INVITE:
                url = _Bundle.getString(BUNDLE_INVITE_URL);
                pageTitle = "邀请二维码";
                break;
            default:
                pageTitle = "二维码";
                break;
        }

        initView();
    }

    private void initView() {
        topbar.setTitle(pageTitle).setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
//        switch (_Bundle.getInt(BUNDLE_KEY_TYPE)) {
//            case Const.QRCODE_TYPE_INVITE:
//                topbar.setRightImageButton(R.drawable.ic_topbar_share, new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        String imagePath = "";
//                        if (_Bundle.getInt(BUNDLE_KEY_TYPE) == Const.QRCODE_TYPE_INVITE) {
//                            imagePath = FileUtils.getImageDir(_activity) + "/inviteQrCode.png";
//                            File file = new File(imagePath);
//                            try {
//                                if (!file.exists()) {
//                                    file.createNewFile();
//                                    FileOutputStream fos = new FileOutputStream(file);
//                                    qrCodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                                    fos.flush();
//                                    fos.close();
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                AppContext.showToast("创建文件失败,请检查sd卡是否可用!");
//                                return;
//                            }
//                        } else {
//                            imagePath = imageUrl;
//                        }
//
//                        UIHelper.showShare(_activity, title, content + url, url, imagePath);
//                    }
//                });
//                break;
//        }


        try {
            qrCodeBitmap = BitmapUtil.create2DCoderBitmap(url, 300, 300);
            qrCode_iv.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            AppContext.showToast("二维码生成失败");
        }
    }
}
