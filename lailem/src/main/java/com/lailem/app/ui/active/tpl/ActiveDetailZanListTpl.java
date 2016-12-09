package com.lailem.app.ui.active.tpl;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.dynamic.LikeListBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;

import java.util.ArrayList;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by XuYang on 15/12/5.
 */
public class ActiveDetailZanListTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.zanListLayout)
    LinearLayout zanListLayout;

    public ActiveDetailZanListTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_detail_zan_list;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        ArrayList<LikeListBean.Like> list = (ArrayList<LikeListBean.Like>) wrapper.getObject();
        if (list != null) {
            zanListLayout.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                LikeListBean.Like like = list.get(i);
                ImageView imageView = (ImageView) View.inflate(_activity, R.layout.view_zan_item, null);
                if (Func.checkImageTag(like.getHeadSPicName(), imageView)) {
                    Glide.with(_activity).load(ApiClient.getFileUrl(like.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(imageView);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) TDevice.dpToPixel(30f), (int) TDevice.dpToPixel(30f));
                params.rightMargin = (int) TDevice.dpToPixel(10f);
                zanListLayout.addView(imageView, params);
                if ((TDevice.dpToPixel(30 * (i + 2)) + TDevice.dpToPixel(10 * (i + 1)) + TDevice.dpToPixel(40)) > (TDevice.getScreenWidth())) {
                    break;
                }
            }
        }
    }
}
