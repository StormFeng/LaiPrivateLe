package com.lailem.app.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupDatabaseBean;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DataBankAddressTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.map)
    ImageView map_iv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.name)
    TextView name_tv;

    private GroupDatabaseBean.Data bean;
    private int position;


    public DataBankAddressTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_data_bank_address;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        GroupDatabaseBean.Data bean = (GroupDatabaseBean.Data) wrapper.getObject();
        this.bean = bean;
        this.position = position;

        address_tv.setText(bean.getAddress());
        name_tv.setText(bean.getNickname());
        String url = Const.BAIDU_STATIC_IMAGE.replaceAll("@@", bean.getLon() + "," + bean.getLat());
        if (Func.checkImageTag(url, map_iv)) {
            Glide.with(_activity).load(url).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.TOP)).into(map_iv);
        }

    }

    @OnClick({R.id.map, R.id.loc})
    public void clickMap() {
        UIHelper.showActiveLoc(_activity, bean.getLat(), bean.getLon(), bean.getAddress(), ActiveLocActivity.TYPE_OTHER);
    }

}
