package com.lailem.app.tpl;

import android.content.Context;
import android.location.Geocoder;

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
import com.baidu.mapapi.search.poi.PoiSortType;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.SelectLoc;
import com.sina.weibo.sdk.openapi.models.Geo;

import java.util.ArrayList;
import java.util.List;

public class SelectLocDataSource extends BaseListDataSource<Object> implements OnGetPoiSearchResultListener, OnGetGeoCoderResultListener {

    private PoiSearch mPoiSearch;
    private PoiNearbySearchOption nearbySearchOption;
    private boolean isWait;
    private ArrayList<Object> models;

    private GeoCoder geoCoder;
    private ReverseGeoCodeOption reverseGeoCodeOption;

    public SelectLocDataSource(Context context) {
        super(context);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.keyword("道路");
        nearbySearchOption.radius(10000);// 检索半径，单位是米
        nearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);

        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        reverseGeoCodeOption = new ReverseGeoCodeOption();

    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        models = new ArrayList<Object>();
//		nearbySearchOption.pageNum(page);
        isWait = true;
//		mPoiSearch.searchNearby(nearbySearchOption);
        geoCoder.reverseGeoCode(reverseGeoCodeOption);
        while (isWait) {
            Thread.sleep(100);
        }
        hasMore = models.size() > 0;
        this.page = page;
        return models;
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
//		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//			List<PoiInfo> list = result.getAllPoi();
//			for (int i = 0; i < list.size(); i++) {
//				models.add(new SelectLoc(list.get(i)));
//			}
//		}
//		isWait = false;
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult arg0) {

    }

    public void setLocation(double latitude, double longitude) {
        nearbySearchOption.location(new LatLng(latitude, longitude));
        reverseGeoCodeOption.location(new LatLng(latitude, longitude));
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        List<PoiInfo> list = reverseGeoCodeResult.getPoiList();
        for (int i = 0; i < list.size(); i++) {
            models.add(new SelectLoc(list.get(i)));
        }

        isWait = false;
    }
}
