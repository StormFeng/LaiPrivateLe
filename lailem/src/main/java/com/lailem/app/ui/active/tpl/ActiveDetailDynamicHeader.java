package com.lailem.app.ui.active.tpl;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;

import butterknife.Bind;

/**
 * Created by XuYang on 15/12/5.
 */
public class ActiveDetailDynamicHeader extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.dynamicCount)
    TextView dynamicCount_tv;
    @Bind(R.id.emptyTip)
    TextView emptyTip_tv;

    public ActiveDetailDynamicHeader(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_detail_dynamic_header;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        int dynamicCount = (int) wrapper.getObject();
        dynamicCount_tv.setText("(" + dynamicCount + ")");
        if (dynamicCount == 0) {
            emptyTip_tv.setVisibility(VISIBLE);
        } else {
            emptyTip_tv.setVisibility(GONE);
        }
    }
}
