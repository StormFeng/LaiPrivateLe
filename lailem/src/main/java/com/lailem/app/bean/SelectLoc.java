package com.lailem.app.bean;

import com.baidu.mapapi.search.core.PoiInfo;

public class SelectLoc extends Base {

	private PoiInfo poiInfo;

	public SelectLoc(PoiInfo poiInfo) {
		this.poiInfo = poiInfo;
	}

	public PoiInfo getPoiInfo() {
		return poiInfo;
	}

	public void setPoiInfo(PoiInfo poiInfo) {
		this.poiInfo = poiInfo;
	}

}
