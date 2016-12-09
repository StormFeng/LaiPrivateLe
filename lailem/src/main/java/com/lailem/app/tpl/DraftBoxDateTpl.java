package com.lailem.app.tpl;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;

import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;

/**
 * Created by XuYang on 15/11/10.
 */
public class DraftBoxDateTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.date)
    TextView date_tv;

    public DraftBoxDateTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_draft_box_date;
    }

    @Override
    public void setBean(ObjectWrapper bean, int position) {
        Date dat = new Date(Long.parseLong(bean.getObject().toString()));
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date_tv.setText("发表于：" + format.format(gc.getTime()));
    }
}
