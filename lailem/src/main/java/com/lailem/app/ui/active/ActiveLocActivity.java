package com.lailem.app.ui.active;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActiveLocActivity extends BaseActivity {

    public static final String BUNDLE_KEY_LAT = "lat";
    public static final String BUNDLE_KEY_LON = "lon";
    public static final String BUNDLE_KEY_ADDRESS = "address";
    public static final String BUNDLE_KEY_TYPE = "address_type";

    public static final int TYPE_ACTIVE = 1;
    public static final int TYPE_GROUP = 2;
    public static final int TYPE_OTHER = 3;
    private int markerResId = R.drawable.ic_map_marker;


    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.addressTitle)
    TextView addressTitle_tv;
    @Bind(R.id.address)
    TextView address_tv;


    private String title = "位置";
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    private UiSettings mUiSettings;
    private boolean isRequestLoc = true;
    private InfoWindow locInfoWindow;
    private BitmapDescriptor locMarkerImage;
    private BitmapDescriptor addressMarkerImage;
    private BDLocationListener myLocationListener = new MyLocationListener();
    private LatLng locLatLng;
    private String locText;
    private Marker addressMarker;
    private Marker locMarker;


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;
            if (isRequestLoc) {
                isRequestLoc = false;
                locText = location.getCity() + location.getDistrict() + location.getStreet();
                locLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                initOverLay();
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_active_loc);
        ButterKnife.bind(this);

        switch (_Bundle.getInt(BUNDLE_KEY_TYPE)) {
            case TYPE_ACTIVE:
                title = "活动位置";
                markerResId = R.drawable.ic_map_marker_active_loc;
                break;
            case TYPE_GROUP:
                title = "群组位置";
                markerResId = R.drawable.ic_map_marker_group_loc;
                break;
            case TYPE_OTHER:
                title = "位置";
                markerResId = R.drawable.ic_map_marker_other_loc;
                break;
        }

        initView();
    }

    private void initView() {
        topbar.setTitle(title).setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        addressTitle_tv.setText(title);
        address_tv.setText(_Bundle.getString(BUNDLE_KEY_ADDRESS));


        addressMarkerImage = BitmapDescriptorFactory.fromResource(markerResId);
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
        MapStatusUpdate u2 = MapStatusUpdateFactory.zoomTo(15);
        mBaiduMap.animateMapStatus(u2);

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

        initOverLay();

    }


    @OnClick(R.id.requestLoc)
    public void requestLoc() {
        isRequestLoc = true;
        mLocClient.requestLocation();
    }

    @OnClick(R.id.navi)
    public void clickNavi() {
        String lat = _Bundle.getString(BUNDLE_KEY_LAT);
        String lon = _Bundle.getString(BUNDLE_KEY_LON);
        Func.goToMap(this, lat, lon, title, _Bundle.getString(BUNDLE_KEY_ADDRESS));
    }


    private void initOverLay() {
        mBaiduMap.clear();

        View addressView = View.inflate(_activity, R.layout.view_loc_infowindow, null);
        TextView loc_tv = (TextView) addressView.findViewById(R.id.loc);
        if (!TextUtils.isEmpty(locText)) {
            loc_tv.setText(locText);
            if ("nullnullnull".equals(locText)) {
                KLog.i("定位失败，请检查定位设置或稍后重试");
            } else {
                OverlayOptions locOverlay = new MarkerOptions().position(locLatLng).icon(locMarkerImage).zIndex(5);
                locMarker = (Marker) (mBaiduMap.addOverlay(locOverlay));
                locInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(addressView), locLatLng, (int) -TDevice.dpToPixel(30), null);
                mBaiduMap.showInfoWindow(locInfoWindow);
            }
        } else {
            KLog.i("定位失败，请检查定位设置或稍后重试");
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (addressMarker != null) {
            builder.include((addressMarker).getPosition());
        }
        if (locMarker != null) {
            builder.include((locMarker).getPosition());
        }



        LatLng addressLatLng = new LatLng(Double.parseDouble(_Bundle.getString(BUNDLE_KEY_LAT)), Double.parseDouble(_Bundle.getString(BUNDLE_KEY_LON)));
        OverlayOptions ooB = new MarkerOptions().position(addressLatLng).icon(addressMarkerImage).zIndex(5);
        addressMarker = (Marker) (mBaiduMap.addOverlay(ooB));

        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newLatLngBounds(builder.build()));
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

    }

    @Override
    public void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        addressMarkerImage.recycle();
    }
}
