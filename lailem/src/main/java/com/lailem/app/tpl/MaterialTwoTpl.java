package com.lailem.app.tpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.dynamic.PicMaterialBean;
import com.lailem.app.ui.create_old.MaterialListTwoActivity;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MaterialTwoTpl extends BaseTpl<PicMaterialBean> {
    @Bind(R.id.image)
    ImageView image_iv;
    @Bind(R.id.check)
    ImageButton check_ib;

    private PicMaterialBean bean;
    private int position;

    public MaterialTwoTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_material_2;
    }

    @Override
    public void setBean(PicMaterialBean bean, int position) {
        this.bean = bean;
        this.position = position;

        if (Func.checkImageTag(bean.getSquareSPicName(), image_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getSquareSPicName())).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(image_iv);
        }
        if (adapter.getCheckedPosition() == position) {
            check_ib.setVisibility(VISIBLE);
        } else {
            check_ib.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        adapter.setCheckedPosition(position);
        adapter.notifyDataSetChanged();

        Bundle bundle = new Bundle();
        bundle.putString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_ID, bean.getId());
        bundle.putString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_SNAME, bean.getsPicName());
        bundle.putString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_BNAME, bean.getbPicName());
        bundle.putString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_SQUARE_SNAME, bean.getSquareSPicName());
        Intent intent = new Intent();
        intent.putExtras(bundle);
        _activity.setResult(Activity.RESULT_OK, intent);
        _activity.finish();

    }

}
