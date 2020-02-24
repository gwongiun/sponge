package com.ly.add.sponge.common.utils;

import java.util.UUID;

/**   
 * UUID获取工具类
 * @author cgd  
 * @date 2014年9月1日 下午6:20:00 
 */
public class UUIDUtil {
	
	/**
	 * 获取一个随机的UUID
	 * */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取一个随机的UUID(不包含"-")
	 * */
	public static String getUUID2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
