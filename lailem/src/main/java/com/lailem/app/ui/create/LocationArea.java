package com.lailem.app.ui.create;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.ui.create_old.dynamic.model.AddressModel;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by XuYang on 15/12/17.
 */
public class LocationArea extends FrameLayout {
    @Bind(R.id.autoFitView)
    View autoFitView;
    @Bind(R.id.viewFlipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.add)
    ImageView add_iv;
    @Bind(R.id.image)
    ImageView image_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.address)
    TextView address_tv;

    private AddressModel model;

    private OnChangeListener onChangeListener;

    public LocationArea(Context context) {
        super(context);
        init(context);
    }

    public LocationArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocationArea(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LocationArea(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_create_dynamic_action_bar_location, this);
        ButterKnife.bind(this);

        FrameLayout.LayoutParams autoFileViewParams = (LayoutParams) autoFitView.getLayoutParams();
        autoFileViewParams.height = (int) (TDevice.getScreenWidth() * 0.45f);
        autoFileViewParams.width = (int) (autoFileViewParams.height * 1.75f);
        autoFitView.setLayoutParams(autoFileViewParams);


        FrameLayout.LayoutParams addParams = (LayoutParams) add_iv.getLayoutParams();
        addParams.height = (int) (TDevice.getScreenWidth() * 0.45f);
        addParams.width = addParams.height;
        add_iv.setLayoutParams(addParams);

    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    @OnClick({R.id.add, R.id.image, R.id.delete})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.add:
            case R.id.image:
                UIHelper.showSelectLoc(getContext());
                break;
            case R.id.delete:
                viewFlipper.setDisplayedChild(0);
                this.model = null;
                if (onChangeListener != null) {
                    onChangeListener.onChangeListener(this, false, 0);
                }
                break;
        }
    }

    public void render(AddressModel model) {
        this.model = model;
        name_tv.setText(model.getContent().getAddress());
        address_tv.setText(model.getContent().getAddress());
        String url = Const.BAIDU_STATIC_IMAGE.replaceAll("@@", model.getContent().getLon() + "," + model.getContent().getLat());
        if (Func.checkImageTag(url, image_iv)) {
            Glide.with(getContext()).load(url).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(image_iv);
        }
        viewFlipper.setDisplayedChild(1);
        if (onChangeListener != null) {
            onChangeListener.onChangeListener(this, true, 1);
        }
    }


    public AddressModel getModel() {
        return model;
    }
}
