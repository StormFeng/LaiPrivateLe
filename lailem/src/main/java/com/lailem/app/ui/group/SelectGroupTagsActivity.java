package com.lailem.app.ui.group;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.AppContext;
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

public class SelectGroupTagsActivity extends BaseActivity {

    public static final String BUNDLE_KEY_TAG_IDS = "tag_ids";
    public static final String BUNDLE_KEY_TAG_NAMES = "tag_names";
    public static final int REQUEST_SELECT_TAGS = 2000;

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.tags)
    LinearLayout tagsLayout;

    private ArrayList<GroupTag> list;
    private ArrayList<String> beforeSelectedTagNames;

    private ArrayList<CheckBox> tagCheckBoxs = new ArrayList<CheckBox>();
    private ArrayList<Tag> tags = new ArrayList<Tag>();
    private int checkedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_option);
        ButterKnife.bind(this);

        list = (ArrayList<GroupTag>) ac.readObject(Const.GROUP_TAG_LIST);
        beforeSelectedTagNames = (ArrayList<String>) _Bundle.getSerializable(BUNDLE_KEY_TAG_NAMES);
        if (beforeSelectedTagNames == null) {
            beforeSelectedTagNames = new ArrayList<String>();
        }

        initView();
    }

    private void initView() {
        topbar.setTitle("设置群标签").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).setRightText("保存", new OnClickListener() {

            @Override
            public void onClick(View v) {
                save();
            }
        });
        initTags();
    }

    protected void save() {
        StringBuilder tagIds = new StringBuilder();
        StringBuilder tagNames = new StringBuilder();
        for (int i = 0; i < tagCheckBoxs.size(); i++) {
            CheckBox checkBox = tagCheckBoxs.get(i);
            Tag tag = tags.get(i);
            if (checkBox.isChecked()) {
                tagIds.append("," + tag.getId());
                tagNames.append("、" + tag.getName());
            }
        }
        if (tagIds.length() < 1) {
            AppContext.showToast("请选择群标签");
            return;
        }
        tagIds = tagIds.deleteCharAt(0);
        tagNames = tagNames.deleteCharAt(0);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TAG_IDS, tagIds.toString());
        bundle.putString(BUNDLE_KEY_TAG_NAMES, tagNames.toString());
        setResult(RESULT_OK, bundle);
        finish();
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
                final CheckBox checkBox = (CheckBox) View.inflate(this, R.layout.view_selecte_tag_item, null);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkedCount++;
                        } else {
                            checkedCount--;
                        }
                    }
                });
                checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkedCount == 4) {
                            checkBox.setChecked(false);
                            AppContext.showToast("群标签个数最多3个");
                            return;
                        }
                    }
                });
                checkBox.setText(tag.getName());
                FlowLayout.LayoutParams flParams = new FlowLayout.LayoutParams((int) (TDevice.getScreenWidth() / 5.5f), FlowLayout.LayoutParams.WRAP_CONTENT);
                flParams.horizontalSpacing = (int) TDevice.dpToPixel(20f);
                flParams.verticalSpacing = (int) TDevice.dpToPixel(15.4f);
                fl.addView(checkBox, flParams);
                if (beforeSelectedTagNames.contains(tag.getName())) {
                    checkBox.setChecked(true);
                }
                tagCheckBoxs.add(checkBox);
                tags.add(tag);
            }
        }
    }
}
