package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 其他-图片素材-随机获取一张图片
 * 
 * @author MD-12
 *
 */
public class RandomMaterialBean extends Result {

	private PicMaterialBean picMaterialInfo;// object

	public PicMaterialBean getPicMaterialInfo() {
		return picMaterialInfo;
	}

	public void setPicMaterialInfo(PicMaterialBean picMaterialInfo) {
		this.picMaterialInfo = picMaterialInfo;
	}

	public static RandomMaterialBean parse(String json) throws AppException {
		RandomMaterialBean res = new RandomMaterialBean();
		try {
			res = gson.fromJson(json, RandomMaterialBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

}
