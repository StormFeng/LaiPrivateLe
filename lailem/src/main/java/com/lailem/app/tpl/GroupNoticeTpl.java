package com.lailem.app.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.bean.PublisherInfo;
import com.lailem.app.jsonbean.activegroup.GroupNoticeListBean.GroupNotice;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class GroupNoticeTpl extends BaseTpl<GroupNotice> {
    @Bind(R.id.state)
    TextView state_tv;
    @Bind(R.id.title)
    TextView title_tv;
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.head)
    ImageView head_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.date)
    TextView date_tv;
    @Bind(R.id.readCount)
    TextView readCount_tv;
    private GroupNotice bean;

    public GroupNoticeTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_notice;
    }

    @Override
    public void setBean(GroupNotice bean, int position) {
        this.bean = bean;
        state_tv.setVisibility(GONE);

        title_tv.setText(bean.getTopic());
        content_tv.setText(bean.getDetail());
        date_tv.setText(bean.getCreateTime());

        PublisherInfo publisherInfo = bean.getPublisherInfo();
        if (publisherInfo != null) {
            if (Func.checkImageTag(publisherInfo.getHeadSPicName(), head_iv)) {
                Glide.with(_activity).load(StringUtils.getUri(publisherInfo.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(head_iv);
            }
            name_tv.setText(publisherInfo.getRemark());
        }
        readCount_tv.setText("");
    }

    @OnClick(R.id.user_ll)
    public void clickUser() {
        UIHelper.showMemberInfoAlone(_activity, bean.getPublisherInfo().getId());
    }

}
