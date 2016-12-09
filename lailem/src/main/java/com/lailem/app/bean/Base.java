package com.lailem.app.bean;

import java.io.Serializable;

/**
 * 实体基类
 */
public abstract class Base implements Serializable {

	protected int itemViewType;// 用于列表多类型类型条目显示

	public int getItemViewType() {
		return itemViewType;
	}

	public void setItemViewType(int itemViewType) {
		this.itemViewType = itemViewType;
	}

}
