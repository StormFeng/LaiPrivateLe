package com.lailem.app.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.dynamic.PicMaterialTypeBean.PicMaterialType;
import com.lailem.app.ui.create_old.MaterialListTwoActivity;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MaterialTpl extends BaseTpl<PicMaterialType> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.title)
    TextView title_tv;
    @Bind(R.id.content)
    TextView content_tv;

    private PicMaterialType bean;

    public MaterialTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_material;
    }

    @Override
    public void setBean(PicMaterialType bean, int position) {
        this.bean = bean;
        if (Func.checkImageTag(bean.getsPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getsPicName())).placeholder(R.drawable.ic_empty_circle).error(R.drawable.ic_empty_circle).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        title_tv.setText(bean.getName());
        content_tv.setText(bean.getDescription());
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        UIHelper.showMaterialListTwo(_activity, bean.getId(), _activity.getIntent().getExtras().getString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_ID));
    }

}
