package com.lailem.app.bean;

public class ObjectWrapper extends Result {
	private Object object;

	public ObjectWrapper() {

	}

	public ObjectWrapper(Object object) {
		this.object = object;
	}

	public ObjectWrapper(Object object, int itemViewType) {
		this.object = object;
		this.itemViewType = itemViewType;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}
}
