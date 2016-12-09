package com.lailem.app.ui.create_old.dynamic.tpl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.ScheduleModel;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;

import java.util.ArrayList;

import butterknife.Bind;

public class CreateSchduleTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.date)
    TextView date_tv;
    @Bind(R.id.address_ll)
    View address_ll;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.contact_ll)
    View contact_ll;
    @Bind(R.id.contact)
    TextView contact_tv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.detail)
    TextView detail_tv;
    ScheduleModel bean;

    public CreateSchduleTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_create_schedule;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        setItemFillParent(getChildAt(0));
        this.bean = (ScheduleModel) wrapper.getObject();
        date_tv.setText(Func.formatTime2(bean.getContent().getStartTime()));

        //地点
        String address = bean.getContent().getAddress();
        if (TextUtils.isEmpty(address)) {
            address_ll.setVisibility(GONE);
        } else {
            address_ll.setVisibility(VISIBLE);
            address_tv.setText(address);
        }

        name_tv.setText(bean.getContent().getTopic());
        detail_tv.setText(bean.getContent().getContent());

        ArrayList<Contact> contactList = bean.getContent().getContact();
        StringBuilder contactSB = new StringBuilder();
        if (contactList != null) {
            for (int i = 0; i < contactList.size(); i++) {
                Contact contact = contactList.get(i);
                contactSB.append(contact.getName() + " " + contact.getPhone());
            }
        }

        //联系人
        if (contactSB.length() == 0) {
            contact_ll.setVisibility(GONE);
        } else {
            contact_ll.setVisibility(VISIBLE);
            contact_tv.setText(contactSB.toString());
        }


    }

    private void setItemFillParent(View item) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item.getLayoutParams();
        params.width = (int) TDevice.getScreenWidth();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        setLayoutParams(params);
    }

}
