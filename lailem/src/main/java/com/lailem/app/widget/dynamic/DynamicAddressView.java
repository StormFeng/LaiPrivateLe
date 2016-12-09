package com.lailem.app.widget.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DynamicAddressView extends LinearLayout {
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.mapImage)
    ImageView mapImage_iv;
    private String lat;
    private String lon;

    public DynamicAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicAddressView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_dynamic_address, this);
        ButterKnife.bind(this, this);
    }

    public void render(String address, String lon, String lat) {
        this.lat = lat;
        this.lon = lon;
        address_tv.setText(address);
        String url = Const.BAIDU_STATIC_IMAGE.replaceAll("@@", lon + "," + lat);
        if (Func.checkImageTag(url, mapImage_iv)) {
            Glide.with(getContext()).load(url).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(mapImage_iv);
        }
    }

    @OnClick(R.id.mapImage)
    public void clickMapImage() {
        UIHelper.showActiveLoc(getContext(), lat, lon, address_tv.getText().toString().trim(), ActiveLocActivity.TYPE_OTHER);
    }
}
