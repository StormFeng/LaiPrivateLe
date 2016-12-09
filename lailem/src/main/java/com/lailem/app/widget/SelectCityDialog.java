package com.lailem.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.broadcast.BaiduLocReceiver;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.dao.Region;
import com.lailem.app.tpl.DialogCityTpl;
import com.lailem.app.utils.ConfigManager;
import com.lailem.app.utils.TDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCityDialog extends Dialog implements OnItemClickListener {

    public static final String TAG_CHECKED_ID = "checkedId";
    public static final String TAG_CHECKED_NAME = "checkedName";
    public static final String LOC_PID = Integer.MAX_VALUE + "";
    public static final String LOC_CID = (Integer.MAX_VALUE - 1) + "";
    public static final String DIRECT_PID = (Integer.MAX_VALUE - 2) + "";
    private final String initProvinceName;
    private final String initCityName;
    private final String initProvinceId;
    private final String initCityId;

    private Context context;
    private Activity _activity;

    @Bind(R.id.contentArea)
    View contentArea;
    @Bind(R.id.province)
    ListView pListView;
    @Bind(R.id.city)
    ListView cListView;
    @Bind(R.id.address)
    TextView address_tv;

    private BaseListAdapter<Region> pAdapter;
    private BaseListAdapter<Region> cAdapter;

    private static final int DEFAULT_THEME = R.style.confirm_dialog;
    private OnChooseListener OnChooseListener;

    private HashMap<Region, List<Region>> data = new HashMap<Region, List<Region>>();
    private ArrayList<Region> pData = new ArrayList<Region>();
    private ArrayList<Region> cData = new ArrayList<Region>();
    private ConfigManager configManager;
    private Region directP;
    private Region locP;
    private Region locC;


    private boolean isChanged;

    private BaiduLocReceiver baiduLocReceiver = new BaiduLocReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BaiduLocReceiver.ACTION_BAIDU_LOC_SUCCESS.equals(intent.getAction())) {
                String provinceName = intent.getStringExtra(BaiduLocReceiver.BUNDLE_KEY_PROVINCE);
                String cityName = intent.getStringExtra(BaiduLocReceiver.BUNDLE_KEY_CITY);
                if (!TextUtils.isEmpty(provinceName) && !TextUtils.isEmpty(cityName)) {
                    ConfigManager configManager = ConfigManager.getConfigManager();
                    if (provinceName.equals(cityName)) {
                        //直辖市
                        String cId = configManager.queryRId(cityName);
                        if (!TextUtils.isEmpty(cId)) {
                            locC.setName(cityName);
                            locC.setRId(cId);
                        }
                    } else {
                        //非直辖市
                        String pId = configManager.queryRId(provinceName);
                        String cId = configManager.queryRId(cityName);
                        if (!TextUtils.isEmpty(cId) && !TextUtils.isEmpty(pId)) {
                            locC.setName(provinceName + " " + cityName);
                            locC.setRId(pId + " " + cId);
                        }
                    }
                    cAdapter.notifyDataSetChanged();
                } else {

                }
            }
        }
    };


    public OnChooseListener getOnChooseListener() {
        return OnChooseListener;
    }

    public void setOnChooseListener(OnChooseListener onChooseListener) {
        OnChooseListener = onChooseListener;
    }

    public interface OnChooseListener {
        void OnChoose(String provinceName, String provinceId, String cityName, String cityId);
    }

    public SelectCityDialog(Context context, String provinceName, String cityName, String provinceId, String cityId) {
        super(context, DEFAULT_THEME);
        this.initProvinceName = provinceName;
        this.initCityName = cityName;
        this.initProvinceId = provinceId;
        this.initCityId = cityId;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this._activity = (Activity) context;
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        View contentView = View.inflate(context, R.layout.dialog_select_city, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (TDevice.getScreenHeight() / 5 * 3));
        contentArea.setLayoutParams(params);

        pAdapter = new BaseListAdapter<Region>(pListView, context, pData, DialogCityTpl.class, null);
        cAdapter = new BaseListAdapter<Region>(cListView, context, cData, DialogCityTpl.class, null);

        pListView.setAdapter(pAdapter);
        cListView.setAdapter(cAdapter);

        pAdapter.setOnItemClickListener(this);
        cAdapter.setOnItemClickListener(this);

        initData();
    }

    private void initData() {
        configManager = ConfigManager.getConfigManager();
        List<Region> provinces = configManager.queryProvince();//所有省
        List<Region> directCity = configManager.queryDirectCity();//所有直辖市
        ArrayList<Region> curRegions = new ArrayList<Region>();//定位城市
        locC = new Region();
        locC.setName("定位中");
        locC.setRId(LOC_CID);
        locC.setPId(LOC_PID);
        locC.setRType(ConfigManager.CITY);
        curRegions.add(locC);

        //初始化省列表数据
        //当前城市
        locP = new Region();
        locP.setName("当前城市");
        locP.setRId(LOC_PID);
        locP.setRType(ConfigManager.PROVINCE);
        pData.add(locP);
        // 直辖市
        directP = new Region();
        directP.setName("直辖市");
        directP.setRId(DIRECT_PID);
        directP.setRType(ConfigManager.PROVINCE);
        pData.add(directP);
        pData.addAll(provinces);

        //初始化城市列表数据
        cData.add(locC);//默认为定位城市


        //填充所有关联数据
        //添加当前城市
        data.put(locP, curRegions);
        // 添加直辖市
        for (int i = 0; i < directCity.size(); i++) {
            directCity.get(i).setPId(DIRECT_PID);
        }
        data.put(directP, (ArrayList<Region>) directCity);
        //添加一般城市
        for (Region region : provinces) {
            data.put(region, new ArrayList<Region>());
        }

        //处理已经选中
        if (!TextUtils.isEmpty(initProvinceName) && !TextUtils.isEmpty(initCityName)) {
            address_tv.setText("(" + initProvinceName + " " + initCityName + ")");
        } else if (TextUtils.isEmpty(initProvinceName) && !TextUtils.isEmpty(initCityName)) {
            address_tv.setText("(" + initCityName + ")");
        } else {

        }

        pAdapter.setCheckedPosition(0);

        pAdapter.notifyDataSetChanged();
        cAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ok)
    public void clickOk() {
        if (OnChooseListener != null) {
            if (isChanged) {
                String pName = (String) pAdapter.getTag(TAG_CHECKED_NAME);
                String pId = (String) pAdapter.getTag(TAG_CHECKED_ID);
                String cName = (String) cAdapter.getTag(TAG_CHECKED_NAME);
                String cId = (String) cAdapter.getTag(TAG_CHECKED_ID);

                if (LOC_PID.equals(pId)) {
                    //定位城市
                    if (cName.contains(" ")) {
                        String[] nameArr = cName.split(" ");
                        String[] idArr = cId.split(" ");
                        //有省有城市
                        OnChooseListener.OnChoose(nameArr[0], idArr[0], nameArr[1], idArr[1]);
                    } else {
                        //只有城市
                        OnChooseListener.OnChoose("", "", cName, cId);
                    }
                } else if (DIRECT_PID.equals(pId)) {
                    //直辖市
                    OnChooseListener.OnChoose("", "", cName, cId);
                } else {
                    //其他省
                    OnChooseListener.OnChoose(pName, pId, cName, cId);
                }
            } else {
                OnChooseListener.OnChoose(initProvinceName, initProvinceId, initCityName, initCityId);
            }
        }
        dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == pListView) {
            pAdapter.setCheckedPosition(position);
            Region region = pData.get(position);
            List<Region> regions = data.get(region);
            if (regions.size() == 0) {
                if (position == 0) {
                    regions = configManager.queryDirectCity();
                } else {
                    regions = configManager.queryCity(region.getRId());
                }
                data.put(region, regions);
            }
            cData.clear();
            cData.addAll(regions);
        } else if (parent == cListView) {
            Region c = cData.get(position);
            String pId = c.getPId();
            String cId = c.getRId();
            String cName = c.getName();

            if (LOC_PID.equals(pId)) {
                if (!cId.equals(LOC_CID)) {
                    //定位城市
                    if (cName.contains(" ")) {
                        String[] nameArr = cName.split(" ");
                        String[] idArr = cId.split(" ");
                        //有省有城市
                        pAdapter.putTag(TAG_CHECKED_ID, idArr[0]);
                        pAdapter.putTag(TAG_CHECKED_NAME, nameArr[0]);
                        cAdapter.putTag(TAG_CHECKED_ID, idArr[1]);
                        cAdapter.putTag(TAG_CHECKED_NAME, nameArr[1]);
                        address_tv.setText("(" + nameArr[0] + " " + nameArr[1] + ")");
                    } else {
                        //只有城市
                        pAdapter.putTag(TAG_CHECKED_ID, locP.getRId());
                        pAdapter.putTag(TAG_CHECKED_NAME, locP.getName());
                        cAdapter.putTag(TAG_CHECKED_ID, cId);
                        cAdapter.putTag(TAG_CHECKED_NAME, cName);
                        address_tv.setText("(" + cName + ")");
                    }
                    isChanged = true;
                } else {
                    AppContext.showToast("正在定位，请稍后...");
                    AppContext.getInstance().requestLoc();
                }
            } else if (DIRECT_PID.equals(pId)) {
                //直辖市
                pAdapter.putTag(TAG_CHECKED_ID, directP.getRId());
                pAdapter.putTag(TAG_CHECKED_NAME, directP.getName());
                cAdapter.putTag(TAG_CHECKED_ID, cId);
                cAdapter.putTag(TAG_CHECKED_NAME, cName);
                address_tv.setText("(" + cName + ")");
                isChanged = true;
            } else {
                //其他省
                String pName = pData.get(pAdapter.getCheckedPosition()).getName();
                pAdapter.putTag(TAG_CHECKED_ID, pId);
                pAdapter.putTag(TAG_CHECKED_NAME, pName);
                cAdapter.putTag(TAG_CHECKED_ID, cId);
                cAdapter.putTag(TAG_CHECKED_NAME, cName);
                address_tv.setText("(" + pName + " " + cName + ")");
                isChanged = true;
            }
        }
        pAdapter.notifyDataSetChanged();
        cAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStart() {
        super.onStart();
        BroadcastManager.registerBaiduLocReceiver(context, baiduLocReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BroadcastManager.unRegisterBaiduLocReceiver(context, baiduLocReceiver);
    }
}
