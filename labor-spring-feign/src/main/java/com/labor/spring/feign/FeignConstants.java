package com.labor.spring.feign;

import cn.hutool.core.util.IdUtil;

public class FeignConstants {
	

	public static String key = "5ed3523fae202d6dd47b52f1";
	public static String secret = "dcd8d9a9abb8449d8dddf7540cf16981";
	
	public static void main(String[] args) {
		System.err.println(IdUtil.simpleUUID());
		System.err.println(IdUtil.randomUUID());
		System.err.println(IdUtil.objectId());
	}
}
