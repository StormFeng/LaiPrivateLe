package com.lailem.app.ui.active;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.GroupIdBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.tpl.CreateVoteActiveItemTpl;
import com.lailem.app.ui.create_old.CreateNameActiveOrGroupActivity;
import com.lailem.app.ui.create_old.dynamic.ChooseVoteSelectCountActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.dragsortlistview.DragSortListView;
import com.lailem.app.widget.dragsortlistview.DragSortListView.DropListener;
import com.lailem.app.widget.dragsortlistview.DragSortListView.RemoveListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建投票活动
 *
 * @author XuYang
 */
public class CreateVoteActiveActivity extends BaseActivity implements DropListener, RemoveListener {
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

    private BaseListAdapter<CreateVoteActiveItem> adapter;

    private ArrayList<CreateVoteActiveItem> voteItems = new ArrayList<CreateVoteActiveItem>();

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
                voteItems.add(new CreateVoteActiveItem());
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
        voteItems.add(new CreateVoteActiveItem());
        voteItems.add(new CreateVoteActiveItem());
        topbar.setTitle("创建投票活动").setLeftImageButton(R.drawable.ic_topbar_close, UIHelper.finish(this)).setRightText("完成");
        adapter = new BaseListAdapter<CreateVoteActiveItem>(mListView, this, voteItems, CreateVoteActiveItemTpl.class, null) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                try {
                    BaseTpl baseTpl = (BaseTpl) CreateVoteActiveItemTpl.class.getConstructor(Context.class).newInstance(context);
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
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                EditText editOne = (EditText) view.findViewById(R.id.topic);
                EditText editTwo = (EditText) view.findViewById(R.id.description);
                EditText editThree = (EditText) view.findViewById(R.id.name);
                if (editOne != null) {
                    editOne.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editOne, 0);
                    return;
                }
                if (editTwo != null) {
                    editTwo.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editTwo, 0);
                    return;
                }
                if (editThree != null) {
                    editThree.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editThree, 0);
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mListView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            }
        });
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

    public void submit() {
        // 投票信息
        CreateVoteActiveInfo voteInfo = new CreateVoteActiveInfo();


        String topic = topic_et.getText().toString().trim();

        //主题
        if (TextUtils.isEmpty(topic)) {
            AppContext.showToast("请填写主题");
            return;
        }

        //选项
        if (voteItems.size() < 2) {
            AppContext.showToast("至少需要两个选项");
            return;
        }
        //空选项
        for (int i = 0; i < voteItems.size(); i++) {
            CreateVoteActiveItem item = (CreateVoteActiveItem) voteItems.get(i);
            if (TextUtils.isEmpty(item.getName())) {
                AppContext.showToast("请输入选项");
                return;
            }
        }

        //结束时间
        if (TextUtils.isEmpty(endDateTime)) {
            AppContext.showToast("请选择结束时间");
            return;
        }

        //备注(选填)
        String description = decription_et.getText().toString().trim();


        voteInfo.setTopic(topic);
        voteInfo.setDescription(description);
        voteInfo.setStartTime(Func.getNow());
        voteInfo.setEndTime(endDateTime);
        voteInfo.setSelectCount(String.valueOf(selectCount));
        voteInfo.setVoteItems(voteItems);

        String name = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_NAME);
        String picPath = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_LOCAL);
        File pic = null;
        if (!TextUtils.isEmpty(picPath)) {
            pic = new File(picPath);
        }
        String picMaterialId = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_MATERIALID);
        if (TextUtils.isEmpty(picMaterialId)) {
            picMaterialId = null;
        }

        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            ApiClient.getApi().createVoteActive(this, ac.getLoginUid(), endDateTime, name, pic, picMaterialId, new Gson().toJson(voteInfo));
        } else {
            UIHelper.showLogin(this, false);
        }
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        if (res.isOK()) {
            GroupIdBean bean = (GroupIdBean) res;
            GroupIdBean.GroupInfo groupInfo = bean.getGroupInfo();
            GroupUtils.joinActivity(this, groupInfo.getId(), groupInfo.getName(), decription_et.getText().toString().trim(), groupInfo.getSquareSPicName(), Const.PERMISSION_PUBLIC, Const.TYPE_ID_VOTE);
            UIHelper.showMainWithClearTop(this);
        } else {
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
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
        CreateVoteActiveItem item = (CreateVoteActiveItem) voteItems.remove(from);
        voteItems.add(to, item);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void remove(int which) {
        voteItems.remove(which);
        adapter.notifyDataSetChanged();
    }

    public static final class CreateVoteActiveInfo {
        private String topic = "";
        private String description = "";
        private String startTime = "";
        private String endTime = "";
        private String selectCount = "";
        private ArrayList<CreateVoteActiveItem> voteItems;

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getSelectCount() {
            return selectCount;
        }

        public void setSelectCount(String selectCount) {
            this.selectCount = selectCount;
        }

        public ArrayList<CreateVoteActiveItem> getVoteItems() {
            return voteItems;
        }

        public void setVoteItems(ArrayList<CreateVoteActiveItem> voteItems) {
            this.voteItems = voteItems;
        }

    }

    public static final class CreateVoteActiveItem {
        private String name = "";
        private String description = "";

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}
