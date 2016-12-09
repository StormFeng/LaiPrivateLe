package com.lailem.app.utils;

import java.util.UUID;

public class UUIDUtils {
	/**
	 * 产生32位的uuid字符串
	 * 
	 * @return
	 */
	public static String uuid32() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
