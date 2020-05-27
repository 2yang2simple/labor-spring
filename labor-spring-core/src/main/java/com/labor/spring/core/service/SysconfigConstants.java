package com.labor.spring.core.service;

import com.labor.common.constants.CommonConstants;


public class SysconfigConstants {


//	public static String AUTHCODE_WEB 		="ad89f721e3da95b11378c52112eaa492";
//	public static String AUTHCODE_ANDROID 	="ad8cfd21e3da95b11378c52112eaa492";
//	public static String DEVICETYPE_ANDROID = "android";
	/**
	 * getConfig(key)
	 */
	public static String KEY_FINGERPRINT_STATUS = "DEFAULT_FINGERPRINT_STATUS";
	public static String KEY_USER_STATUS = "DEFAULT_USER_STATUS";
	public static String KEY_PAGE_SIZE = "DEFAULT_PAGE_SIZE";
	public static String KEY_INDEX_RICHTEXT_UUID = "INDEX_RICHTEXT_UUID";
	
	
	/***
	 * default values
	 */
	public static int DEFAULT_PAGE_SIZE = 50;
	public static String DEFAULT_USER_STATUS = CommonConstants.INACTIVE;
	public static String DEFAULT_FINGERPRINT_STATUS = CommonConstants.INACTIVE;

}
