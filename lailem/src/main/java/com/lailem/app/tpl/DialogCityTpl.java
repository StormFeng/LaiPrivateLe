package com.lailem.app.tpl;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.dao.Region;
import com.lailem.app.utils.ConfigManager;
import com.lailem.app.utils.TDevice;
import com.lailem.app.widget.SelectCityDialog;

import butterknife.Bind;

public class DialogCityTpl extends BaseTpl<Region> {

    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.item)
    RelativeLayout itemView;

    public DialogCityTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dialog_city;
    }


    @Override
    public void setBean(Region bean, int position) {
        if (ConfigManager.PROVINCE.equals(bean.getRType())) {
            if (adapter.getCheckedPosition() == position) {
                name_tv.setTextColor(getResources().getColor(R.color.orange));
                itemView.setBackgroundColor(Color.parseColor("#EFEFEF"));
            } else {
                name_tv.setTextColor(getResources().getColor(R.color.text_dark_2));
                itemView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
        } else {
            String checkedId = (String) adapter.getTag(SelectCityDialog.TAG_CHECKED_ID);
            if (!TextUtils.isEmpty(checkedId) && checkedId.equals(bean.getRId())) {
                name_tv.setTextColor(getResources().getColor(R.color.orange));
                itemView.setBackgroundColor(Color.parseColor("#EFEFEF"));
            } else {
                name_tv.setTextColor(getResources().getColor(R.color.text_dark_2));
                itemView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
        }

        if (SelectCityDialog.LOC_PID.equals(bean.getRId())) {
            name_tv.setCompoundDrawablePadding((int) TDevice.dpToPixel(11.5f));
            name_tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_location), null, null, null);
        } else {
            name_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if (SelectCityDialog.LOC_CID.equals(bean.getRId())) {
            //定位城市
            if (!TextUtils.isEmpty(ac.provinceName) && !TextUtils.isEmpty(ac.cityName)) {
                name_tv.setText(ac.provinceName + " " + ac.cityName);
            } else if (!TextUtils.isEmpty(ac.provinceName) && !TextUtils.isEmpty(ac.cityName)) {
                name_tv.setText(ac.cityName);
            } else {
                name_tv.setText("定位中");
            }
        } else {
            name_tv.setText(bean.getName());
        }
    }
}
