package com.lailem.app.widget.dynamic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicScheduleView extends LinearLayout {

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
    private ArrayList<Contact> contactList;

    public DynamicScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicScheduleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_dynamic_schedule, this);
        ButterKnife.bind(this, this);
    }

    public void render(String date, String address, ArrayList<Contact> contactList, String name, String detail) {
        this.contactList = contactList;
        StringBuilder contactSB = new StringBuilder();
        if (contactList != null) {
            for (int i = 0; i < contactList.size(); i++) {
                Contact contact = contactList.get(i);
                contactSB.append(contact.getName() + " " + contact.getPhone());
            }
        }

        date_tv.setText(Func.formatTime2(date));

        //地点
        if (TextUtils.isEmpty(address)) {
            address_ll.setVisibility(GONE);
        } else {
            address_ll.setVisibility(VISIBLE);
            address_tv.setText(address);
        }

        //联系人
        if (TextUtils.isEmpty(contactSB.toString())) {
            contact_ll.setVisibility(GONE);
        } else {
            contact_ll.setVisibility(VISIBLE);
            contact_tv.setText(contactSB.toString());
        }

        name_tv.setText(name);
        detail_tv.setText(detail);
    }

    @OnClick(R.id.contact_ll)
    public void clickContact() {
        if (contactList != null && contactList.size() > 0) {
            Uri uri = Uri.parse("tel:" + contactList.get(0).getPhone());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            getContext().startActivity(intent);
        }
    }

}
