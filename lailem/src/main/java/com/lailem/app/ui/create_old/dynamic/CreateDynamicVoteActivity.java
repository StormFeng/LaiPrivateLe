package com.lailem.app.ui.create_old.dynamic;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel;
import com.lailem.app.ui.create_old.dynamic.model.VoteModel.VoteItem;
import com.lailem.app.ui.create_old.dynamic.tpl.VoteItemTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.DynamicTaskUtil;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.dragsortlistview.DragSortListView;
import com.lailem.app.widget.dragsortlistview.DragSortListView.DropListener;
import com.lailem.app.widget.dragsortlistview.DragSortListView.RemoveListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateDynamicVoteActivity extends BaseActivity implements DropListener, RemoveListener {
    public static final String BUNDLE_KEY_DIRECT_PUBLISH = "direct_publish";

    public static final int REQUEST_SELECT_COUNT = 1000;

    public static final String BUNDLE_KEY_COUNT = "count";

    public static final String BUNDLE_KEY_SELECTCOUNT = "select_count";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.list)
    DragSortListView mListView;

    EditText topic_et;
    TextView selectCount_tv;
    TextView endTime_tv;
    EditText decription_et;

    private BaseListAdapter<VoteItem> adapter;

    private ArrayList<VoteItem> voteItems = new ArrayList<VoteItem>();

    private int selectCount = 1;

    private String endDateTime;// 结束时间
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dynamic_vote);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        View header = View.inflate(this, R.layout.view_create_dyanmic_vote_header, null);
        View footer = View.inflate(this, R.layout.view_create_dyanmic_vote_footer, null);
        topic_et = (EditText) header.findViewById(R.id.topic);
        selectCount_tv = (TextView) footer.findViewById(R.id.selectCount);
        endTime_tv = (TextView) footer.findViewById(R.id.endTime);
        decription_et = (EditText) footer.findViewById(R.id.description);
        footer.findViewById(R.id.selectCount_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showChooseVoteSelectCount(_activity, voteItems.size(), selectCount);
            }
        });
        footer.findViewById(R.id.addNewItem).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                voteItems.add(new VoteModel.VoteItem());
                adapter.notifyDataSetChanged();
                // 恢复多选数为单选
                if (selectCount != 1) {
                    selectCount = 1;
                    selectCount_tv.setText("单选");
                    AppContext.showToast("请重新选择多选数");
                }
            }
        });
        footer.findViewById(R.id.endTime_ll).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                chooseEndTime();
            }
        });
        mListView.setDivider(getResources().getDrawable(R.drawable.c_divider_line));
        mListView.addHeaderView(header);
        mListView.addFooterView(footer);
        voteItems.add(new VoteModel.VoteItem());
        voteItems.add(new VoteModel.VoteItem());
        topbar.setTitle("投票").setLeftImageButton(R.drawable.ic_topbar_close, UIHelper.finish(this)).setRightText("完成");
        adapter = new BaseListAdapter<VoteItem>(mListView, this, voteItems, VoteItemTpl.class, null) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                try {
                    BaseTpl baseTpl = (BaseTpl) VoteItemTpl.class.getConstructor(Context.class).newInstance(context);
                    baseTpl.init(context, getItemViewType(position));
                    baseTpl.config(this, voteItems, null, mListView, null);
                    baseTpl.setBean(voteItems.get(position), position);
                    return baseTpl;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return convertView;
            }
        };
        mListView.setAdapter(adapter);
        mListView.setDropListener(this);
        mListView.setRemoveListener(this);
        mListView.requestFocus();
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                EditText editOne = (EditText) view.findViewById(R.id.topic);
                EditText editTwo = (EditText) view.findViewById(R.id.description);
                EditText editThree = (EditText) view.findViewById(R.id.name);
                if (editOne != null) {
                    editOne.requestFocus();
                    return;
                }
                if (editTwo != null) {
                    editTwo.requestFocus();
                    return;
                }
                if (editThree != null) {
                    editThree.requestFocus();
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mListView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            }
        });

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    /**
     * 选择结束时间
     */
    protected void chooseEndTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timeDialog = new TimePickerDialog(this, new OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                endDateTime = endDate + " " + sdf.format(calendar.getTime());
                endTime_tv.setText(endDate + " " + sdf.format(calendar.getTime()));
            }
        }, hourOfDay, minute, true);

        DatePickerDialog dateDialog = new DatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDate = sdf.format(calendar.getTime());
                timeDialog.show();
            }
        }, year, monthOfYear, dayOfMonth);

        dateDialog.show();

    }

    @OnClick({R.id.right_tv})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.right_tv:
                submit();
                break;
        }
    }

    private void submit() {
        String topic = topic_et.getText().toString().trim();
        String description = decription_et.getText().toString().trim();

        if (TextUtils.isEmpty(topic)) {
            AppContext.showToast("请填写主题");
            return;
        }

        if (voteItems.size() < 2) {
            AppContext.showToast("至少需要两个选项");
            return;
        }
        for (int i = 0; i < voteItems.size(); i++) {
            VoteItem item = (VoteItem) voteItems.get(i);
            if (TextUtils.isEmpty(item.getName())) {
                AppContext.showToast("请输入选项");
                return;
            }
        }
        if (TextUtils.isEmpty(endDateTime)) {
            AppContext.showToast("请选择结束时间");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            AppContext.showToast("请填写备注");
            return;
        }

        VoteModel model = new VoteModel();
        model.getContent().setTopic(topic_et.getText().toString());
        model.getContent().setDescription(decription_et.getText().toString());
        model.getContent().setSelectCount(selectCount + "");
        model.getContent().setStartTime(Func.getNow());
        model.getContent().setEndTime(endDateTime);
        model.getContent().setVoteItems(voteItems);
        boolean isDirectPublish = _Bundle.getBoolean(BUNDLE_KEY_DIRECT_PUBLISH);
        if (isDirectPublish) {
            if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
                String key = DynamicTaskUtil.buildTaskKey(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), Const.DYNAMIC_STATE_SENDING, Const.DYNA_FROM_DISABLE_SORT);
                ArrayList<Object> models = new ArrayList<Object>();
                models.add(model);
                ac.saveObject(models, key);
                BroadcastManager.sendDynamicTaskBroadcast(_activity, DynamicTaskReceiver.ACTION_ADD_TASK, key);
                finish();
            } else {
                UIHelper.showLogin(this, false);
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(CreateDynamicActivity.BUNDLE_KEY_VOTE, model);
            setResult(RESULT_OK, bundle);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_COUNT:
                    String name = data.getExtras().getString(ChooseVoteSelectCountActivity.BUNDLE_KEY_NAME);
                    String value = data.getExtras().getString(ChooseVoteSelectCountActivity.BUNDLE_KEY_VALUE);
                    selectCount_tv.setText(name);
                    selectCount = Integer.parseInt(value);
                    break;
            }
        }
    }

    @Override
    public void drop(int from, int to) {
        VoteItem item = (VoteItem) voteItems.remove(from);
        voteItems.add(to, item);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void remove(int which) {
        voteItems.remove(which);
        adapter.notifyDataSetChanged();
    }

}
