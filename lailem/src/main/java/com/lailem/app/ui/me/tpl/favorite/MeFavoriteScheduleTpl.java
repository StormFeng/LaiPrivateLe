package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicScheduleView;

import java.util.ArrayList;

import butterknife.Bind;

public class MeFavoriteScheduleTpl extends BaseTpl<PublishInfo> {

    @Bind(R.id.dynamicSchedule)
    DynamicScheduleView dynamicScheduleView;

    public MeFavoriteScheduleTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_schedule;
    }

    @Override
    public void setBean(PublishInfo bean, int position) {
        ArrayList<Contact> contactList = bean.getContact();
        StringBuilder contactSB = new StringBuilder();
        if (contactList != null) {
            for (int i = 0; i < contactList.size(); i++) {
                Contact contact = contactList.get(i);
                contactSB.append(contact.getName() + " " + contact.getPhone());
            }
        }
        dynamicScheduleView.render(bean.getStartTime(), bean.getAddress(), bean.getContact(), bean.getTopic(), bean.getContent());
    }

}
