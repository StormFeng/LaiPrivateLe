package com.lailem.app.ui.group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.adapter.SimpleTextWatcher;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectLocActivity extends BaseActivity implements OnMarkerClickListener, OnGetGeoCoderResultListener, OnGetSuggestionResultListener, OnGetPoiSearchResultListener {

    public static final int REQUEST_SELECT_LOC = 1000;
    public static final String BUNDLE_KEY_ADDRESSINFO = "address_info";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.centerMarker)
    ImageView centerMarker_iv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.over)
    ImageView over_iv;
    @Bind(R.id.searchBar_vp)
    ViewFlipper searchBar_vp;
    @Bind(R.id.keyword)
    EditText keyword_et;
    @Bind(R.id.searchArea)
    View searchArea;

    private GeoCoder geoCoder;
    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    private UiSettings mUiSettings;
    private LatLng locLatLng;
    private Marker locMarker;
    private BitmapDescriptor locMarkerImage;
    private InfoWindow locInfoWindow;
    private boolean isRequestLoc = true;
    private String locText;

    private BDLocationListener myLocationListener = new MyLocationListener();
    private ArrayAdapter<String> sugAdapter;
    private List<SuggestionResult.SuggestionInfo> suggestionInfos = new ArrayList<SuggestionResult.SuggestionInfo>();
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_select_loc);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("选择位置").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this)).setRightText("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                AddressInfo addressInfo = new AddressInfo(
                        mBaiduMap.getMapStatus().target.latitude + "",
                        mBaiduMap.getMapStatus().target.longitude + "",
                        address_tv.getText().toString().trim());
                bundle.putSerializable(SelectLocActivity.BUNDLE_KEY_ADDRESSINFO, addressInfo);
                data.putExtras(bundle);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        locMarkerImage = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_location);

        // 地图初始化
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMarkerClickListener(this);
        MapStatusUpdate u2 = MapStatusUpdateFactory.zoomTo(15);
        mBaiduMap.animateMapStatus(u2);
        mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent arg0) {
                if (arg0.getAction() == MotionEvent.ACTION_UP) {
                    reverseGeo(new LatLng(mBaiduMap.getMapStatus().target.latitude, mBaiduMap.getMapStatus().target.longitude));
                }
            }
        });

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();

        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        mPoiSearch = PoiSearch.newInstance();

        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        sugAdapter = new ArrayAdapter<String>(this, R.layout.item_search_suggest);
        showSuggestPopup();

        keyword_et.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (keyword_et.length() >= 2) {
                    mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().keyword(keyword_et.getText().toString().trim()).city("广州"));
                }
            }
        });

        keyword_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) keyword_et.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    clickSearch();
                    return true;
                }
                return false;
            }
        });
    }


    @OnClick(R.id.searchBarHint)
    public void clickSearchBarHint() {
        searchBar_vp.setDisplayedChild(1);
        keyword_et.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) keyword_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    @OnClick(R.id.search)
    public void clickSearch() {
        ((InputMethodManager) keyword_et.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        String keyword = keyword_et.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            AppContext.showToast("请输入关键字");
            return;
        }
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword(keyword);
        option.location(locLatLng);
        option.radius(Integer.MAX_VALUE);
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mPoiSearch.searchNearby(option);
    }


    @OnClick(R.id.requestLoc)
    public void requestLoc() {
        isRequestLoc = true;
        mLocClient.requestLocation();
    }


    private void initOverLay() {
        mBaiduMap.clear();

        OverlayOptions ooB = new MarkerOptions().position(locLatLng).icon(locMarkerImage).zIndex(5);
        locMarker = (Marker) (mBaiduMap.addOverlay(ooB));
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(locLatLng);
        mBaiduMap.animateMapStatus(u);
        topbar.showRight_tv();

        View addressView = View.inflate(_activity, R.layout.view_loc_infowindow, null);
        TextView loc_tv = (TextView) addressView.findViewById(R.id.loc);
        if (!TextUtils.isEmpty(locText)) {
            loc_tv.setText(locText);
            address_tv.setText(locText);
            if ("nullnullnull".equals(locText)) {
                AppContext.showToast("定位失败，请检查定位设置或稍后重试");
                over_iv.setVisibility(View.VISIBLE);
                topbar.getRight_tv().setEnabled(false);
            } else {
                over_iv.setVisibility(View.GONE);
                topbar.getRight_tv().setEnabled(true);
            }
        } else {
            AppContext.showToast("定位失败，请检查定位设置或稍后重试");
            over_iv.setVisibility(View.VISIBLE);
            topbar.getRight_tv().setEnabled(false);
        }
        locInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(addressView), locLatLng, (int) -TDevice.dpToPixel(30), null);
        mBaiduMap.showInfoWindow(locInfoWindow);

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        if (arg0 == locMarker) {
            mBaiduMap.showInfoWindow(locInfoWindow);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(locLatLng);
            mBaiduMap.animateMapStatus(u, 30);
        }
        return true;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        isRequestLoc = true;
        mLocClient.start();
    }

    @Override
    public void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mSuggestionSearch.destroy();
        super.onDestroy();
        locMarkerImage.recycle();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void shakeCenterMarker() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -30);
        animation.setDuration(200);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(1);
        centerMarker_iv.startAnimation(animation);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
        if (poiInfos != null && poiInfos.size() > 0) {
            String locText = poiInfos.get(0).address + poiInfos.get(0).name;
            if ("nullnullnull".equals(locText)) {
                AppContext.showToast("定位失败，请检查定位设置或稍后重试");
                over_iv.setVisibility(View.VISIBLE);
                topbar.getRight_tv().setEnabled(false);
            } else {
                over_iv.setVisibility(View.GONE);
                topbar.getRight_tv().setEnabled(true);
            }
            address_tv.setText(locText);
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();
        suggestionInfos.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.pt != null && info.key != null) {
                sugAdapter.add(info.key);
                suggestionInfos.add(info);
            }
        }
        showSuggestPopup();
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            AppContext.showToast("未找到结果");
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            List<PoiInfo> poiInfos = result.getAllPoi();
            for (int i = 0; i < poiInfos.size(); i++) {
                KLog.e(poiInfos.get(i).address);
                PoiInfo poiInfo = poiInfos.get(i);
                if (poiInfo.location != null) {
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(poiInfo.location);
                    mBaiduMap.animateMapStatus(u, 30);
                    reverseGeo(poiInfo.location);
                    break;
                }
            }
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;
            locText = location.getCity() + location.getDistrict() + location.getStreet();
            if (isRequestLoc) {
                isRequestLoc = false;
                locLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                initOverLay();
            }
        }
    }

    private void showSuggestPopup() {
        if (mPopupWindow == null) {
            ListView listView = (ListView) getLayoutInflater().inflate(R.layout.view_suggest_popup, null);
            listView.setAdapter(sugAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (suggestionInfos != null) {
                        SuggestionResult.SuggestionInfo suggestionInfo = suggestionInfos.get(position);
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(suggestionInfo.pt);
                        mBaiduMap.animateMapStatus(u);
                        reverseGeo(suggestionInfo.pt);
                        mPopupWindow.dismiss();
                    }
                }
            });
            mPopupWindow = new PopupWindow(listView, (int) (TDevice.getScreenWidth() - TDevice.dpToPixel(120)), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(false);
            mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));

        }
        if (sugAdapter.getCount() == 0) {
            mPopupWindow.dismiss();
            return;
        } else {
            sugAdapter.notifyDataSetChanged();
            mPopupWindow.showAsDropDown(searchArea, (int) TDevice.dpToPixel(15.4f), 0);
            keyword_et.requestFocus();
        }

    }

    private void reverseGeo(LatLng latLng) {
        shakeCenterMarker();
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        reverseGeoCodeOption.location(latLng);
        geoCoder.reverseGeoCode(reverseGeoCodeOption);
    }

}
