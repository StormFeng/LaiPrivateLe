package com.lailem.app.ui.create_old.dynamic;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.ui.create_old.dynamic.model.ScheduleModel;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ScheduleActivity extends BaseActivity {

    public static final String BUNDLE_KEY_SCHEDULE = "schedule";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.date)
    TextView date_tv;
    @Bind(R.id.scheduleDate)
    TextView scheduleDate_tv;
    @Bind(R.id.address_ll)
    View address_ll;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.contact_ll)
    View contact_ll;
    @Bind(R.id.contact)
    TextView contact_tv;
    @Bind(R.id.theme)
    TextView theme_tv;
    @Bind(R.id.content)
    TextView content_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("活动日程").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        ScheduleModel model = (ScheduleModel) _Bundle.getSerializable(BUNDLE_KEY_SCHEDULE);
        if (ac.isLogin()) {
            if (Func.checkImageTag(ac.getProperty(Const.USER_SHEAD), avatar_iv)) {
                Glide.with(_activity).load(ApiClient.getFileUrl(ac.getProperty(Const.USER_SHEAD))).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
            }
            name_tv.setText(ac.getProperty(Const.USER_NICKNAME));
        } else {
            avatar_iv.setImageResource(R.drawable.default_avatar);
            name_tv.setText("");
        }
        date_tv.setText(Func.formatTime(Func.getNow()));
        scheduleDate_tv.setText(model.getContent().getStartTime());

        //地点
        String address = model.getContent().getAddress();
        if (TextUtils.isEmpty(address)) {
            address_ll.setVisibility(View.GONE);
        } else {
            address_ll.setVisibility(View.VISIBLE);
            address_tv.setText(address);
        }

        theme_tv.setText(model.getContent().getTopic());
        content_tv.setText(model.getContent().getContent());

        ArrayList<Contact> contactList = model.getContent().getContact();
        StringBuilder contactSB = new StringBuilder();
        if (contactList != null) {
            for (int i = 0; i < contactList.size(); i++) {
                Contact contact = contactList.get(i);
                contactSB.append(contact.getName() + " " + contact.getPhone());
            }
        }

        //联系人
        if (contactSB.length() == 0) {
            contact_ll.setVisibility(View.GONE);
        } else {
            contact_ll.setVisibility(View.VISIBLE);
            contact_tv.setText(contactSB.toString());
        }
    }

}
