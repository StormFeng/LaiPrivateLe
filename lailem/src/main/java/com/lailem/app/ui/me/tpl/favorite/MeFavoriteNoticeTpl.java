package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.NoticeInfo;
import com.lailem.app.tpl.BaseTpl;

import butterknife.Bind;

public class MeFavoriteNoticeTpl extends BaseTpl<NoticeInfo> {

    @Bind(R.id.topic)
    TextView topic_tv;
    @Bind(R.id.content)
    TextView content_tv;

    public MeFavoriteNoticeTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_notice;
    }

    @Override
    public void setBean(NoticeInfo bean, int position) {
        topic_tv.setText(bean.getTopic());
        content_tv.setText("　　" + bean.getDetail());
    }

}
