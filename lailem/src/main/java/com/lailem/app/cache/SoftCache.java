package com.lailem.app.cache;

import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class SoftCache<T> {

	private Class<T> entityClass;
	protected int maxSize = 100;

	LruCache<Object, SoftReference<T>> cache;
	Method method;

	public SoftCache() {
		cache = new LruCache<Object, SoftReference<T>>(maxSize);
	}

	public SoftCache(String methodNameForGetKey) {
		cache = new LruCache<Object, SoftReference<T>>(maxSize);
		if (methodNameForGetKey != null && !"".equals(methodNameForGetKey.trim())) {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			try {
				method = Class.forName(entityClass.getName()).getDeclaredMethod(methodNameForGetKey);
				method.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
		cache.resize(maxSize);
	}

	public void clearMemory() {
		cache.evictAll();
	}

	public void put(Object key, T t) {
		cache.put(key, new SoftReference<T>(t));
	}

	public void put(T t) {
		try {
			Object key = method.invoke(t);
			cache.put(key, new SoftReference<T>(t));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public T get(Object key) {
		return cache.get(key).get();
	}

	public void removeByKey(Object key) {
		cache.remove(get(key));
	}
}
