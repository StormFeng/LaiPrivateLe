package com.lailem.app.cache;

import android.support.v4.util.LruCache;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class Cache<T> {
	protected int maxSize = 100;
	private Class<T> entityClass;
	LruCache<Object, T> cache;
	Method method;

	public Cache() {
		cache = new LruCache<Object, T>(maxSize);
	}

	public Cache(String methodNameForGetKey) {
		cache = new LruCache<Object, T>(maxSize);
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
		cache.put(key, t);
	}

	public void put(T t) {
		try {
			Object key = method.invoke(t);
			cache.put(key, t);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public T get(Object key) {
		return cache.get(key);
	}

	public void remove(T t) {
		cache.remove(t);
	}

	public void removeByKey(Object key) {
		cache.remove(get(key));
	}


}
