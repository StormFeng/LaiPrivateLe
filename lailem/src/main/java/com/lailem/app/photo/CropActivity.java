package com.lailem.app.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.edmodo.cropper.CropImageView;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.BitmapUtil;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XuYang on 15/10/20.
 */
public class CropActivity extends BaseActivity {
    public static final String BUNDLE_KEY_PATH = "data";
    public static final String BUNDLE_KEY_ASPECT_X = "aspectX";
    public static final String BUNDLE_KEY_ASPECT_Y = "aspectY";
    public int aspectX = 200;
    public int aspectY = 200;
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.cropImageView)
    CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crop);
        ButterKnife.bind(this);

        int x = _Bundle.getInt(BUNDLE_KEY_ASPECT_X);
        int y = _Bundle.getInt(BUNDLE_KEY_ASPECT_Y);
        if (x != 0) {
            aspectX = x;
        }
        if (y != 0) {
            aspectY = y;
        }

        cropImageView.setAspectRatio(aspectX, aspectY);
        cropImageView.setFixedAspectRatio(true);

        topbar.setTitle("裁剪").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).setRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap croppedImage = cropImageView.getCroppedImage();
                File file = BitmapUtil.saveBitmap(croppedImage, _activity);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_KEY_PATH, file.getAbsolutePath());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        Bitmap bitmap = BitmapUtil.decodeFile(_Bundle.getString(BUNDLE_KEY_PATH), (int) TDevice.getScreenWidth());
        cropImageView.setImageBitmap(bitmap);
    }
}
