package com.lailem.app.ui.group;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.GroupTag;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.Tag;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.FlowLayout;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupTagsActivity extends BaseActivity {

    public static final String BUNDLE_KEY_TAG_ID = "tag_id";
    public static final String BUNDLE_KEY_TAG_NAME = "tag_name";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.tags)
    LinearLayout tagsLayout;

    private ArrayList<GroupTag> list;

    private ArrayList<RadioButton> tagRadioButtons = new ArrayList<RadioButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_option);
        ButterKnife.bind(this);
        list = (ArrayList<GroupTag>) ac.readObject(Const.GROUP_TAG_LIST);
        initView();
    }

    private void initView() {
        topbar.setTitle("附近群组").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        initTags();
    }

    private void initTags() {
        for (int i = 0; i < list.size(); i++) {
            GroupTag groupTag = list.get(i);
            ArrayList<Tag> tagList = groupTag.getTagList();

            TextView label = (TextView) View.inflate(this, R.layout.view_group_label, null);
            label.setText(groupTag.getName());
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            labelParams.leftMargin = (int) TDevice.dpToPixel(15.4f);
            labelParams.topMargin = (int) TDevice.dpToPixel(23f);
            labelParams.bottomMargin = (int) TDevice.dpToPixel(23f);
            tagsLayout.addView(label, labelParams);
            FlowLayout fl = new FlowLayout(this);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llParams.leftMargin = (int) TDevice.dpToPixel(15.4f);
            llParams.rightMargin = (int) TDevice.dpToPixel(15.4f);
            if (i == list.size() - 1) {
                fl.setPadding(0, 0, 0, (int) TDevice.dpToPixel(15.4f));
            }
            tagsLayout.addView(fl, llParams);
            for (int j = 0; j < tagList.size(); j++) {
                final Tag tag = tagList.get(j);
                final RadioButton radioButton = (RadioButton) View.inflate(this, R.layout.view_group_tag_item, null);
                radioButton.setText(tag.getName());
                FlowLayout.LayoutParams flParams = new FlowLayout.LayoutParams((int) (TDevice.getScreenWidth() / 5.5f), FlowLayout.LayoutParams.WRAP_CONTENT);
                flParams.horizontalSpacing = (int) TDevice.dpToPixel(20f);
                flParams.verticalSpacing = (int) TDevice.dpToPixel(15.4f);
                fl.addView(radioButton, flParams);
                tagRadioButtons.add(radioButton);
                radioButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        for (int k = 0; k < tagRadioButtons.size(); k++) {
                            tagRadioButtons.get(k).setChecked(false);
                        }
                        radioButton.setChecked(true);
                        UIHelper.showAroundGroupList(_activity, tag.getId(), tag.getName());
                    }
                });
            }
        }
    }
}
