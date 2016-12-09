package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupDatabaseBean;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import java.util.ArrayList;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DataBankVideoTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.videoLayout)
    LinearLayout videoLayout;

    public DataBankVideoTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_data_bank_video;
    }

    @Override
    protected void initView() {
        super.initView();
        int width = (int) ((TDevice.getScreenWidth() - TDevice.dpToPixel(40)) / 3);
        int height = width;

        for (int i = 0; i < videoLayout.getChildCount(); i++) {
            FrameLayout fl = (FrameLayout) videoLayout.getChildAt(i);
            ImageView iv = (ImageView) fl.getChildAt(0);
            ImageView mask = (ImageView) fl.getChildAt(1);
            FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) iv.getLayoutParams();
            params.width = width;
            params.height = height;
            iv.setLayoutParams(params);
            mask.setLayoutParams(params);
        }
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        ArrayList<GroupDatabaseBean.Data> bean = (ArrayList<GroupDatabaseBean.Data>) wrapper.getObject();
        for (int i = 0; i < videoLayout.getChildCount(); i++) {
            FrameLayout fl = (FrameLayout) videoLayout.getChildAt(i);
            if (i < bean.size()) {
                final GroupDatabaseBean.Data itemData = bean.get(i);
                fl.setVisibility(VISIBLE);
                final ImageView iv = (ImageView) fl.getChildAt(0);
                TextView tv = (TextView) fl.getChildAt(2);
                Glide.with(_activity).load(ApiClient.getFileUrl(itemData.gettFilename())).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(iv);
                tv.setText(itemData.getNickname());
                fl.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIHelper.showVideoPlay(_activity, ApiClient.getFileUrl(itemData.getFilename()), ApiClient.getFileUrl(itemData.gettFilename()));
                    }
                });
            } else {
                fl.setVisibility(INVISIBLE);
            }
        }
    }

}
