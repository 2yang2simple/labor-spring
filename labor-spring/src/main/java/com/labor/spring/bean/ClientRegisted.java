package com.labor.spring.bean;

import java.util.HashMap;


import com.labor.common.util.StringUtil;
import com.labor.spring.constants.WebConstants;

public class ClientRegisted {

	private final static HashMap<String,ClientInfo> REGISTED_CLIENTS = new HashMap<String,ClientInfo>();
//	private final static String CLIENTKEY_DEFAULT = "auth";
	
	static {
		
		//dev
//		REGISTED_CLIENTS.put("auth", 
//				new ClientInfo(WebConstants.FP_TYPE_CANVAS,
//								WebConstants.AUTH_TYPE_SAVEDIN_COOKIES,
//								"auth",
//								"de07c085bfe741caaef26e7b4adf0096",
//								"http://localhost:8080/auth/rest/feign/auth/logins/users/tokens/keys",
//								"http://localhost:8080/auth"));
//		
//		REGISTED_CLIENTS.put("ppp", 
//				new ClientInfo(WebConstants.FP_TYPE_CANVAS,
//								WebConstants.AUTH_TYPE_SAVEDIN_COOKIES,
//								"ppp",
//								"ad89f721e3da95b11378c52112eaa492",
//								"http://localhost:8080/ppp/rest/feign/auth/logins/users/tokens/keys",
//								"http://47.106.74.136/ppp"));
		
		REGISTED_CLIENTS.put("ppp-weixin", 
				new ClientInfo(WebConstants.FP_TYPE_APP_WEIXIN,
								WebConstants.AUTH_TYPE_SAVEDIN_APP,
								"ppp",
								"e18932510a6546c0a84a00e3219cbe2a",
								"http://47.106.74.136/ppp/rest/sso/access",
								"http://47.106.74.136/ppp"));
		//test
		REGISTED_CLIENTS.put("auth", 
				new ClientInfo(WebConstants.FP_TYPE_CANVAS,
								WebConstants.AUTH_TYPE_SAVEDIN_COOKIES,
								"auth",
								"de07c085bfe741caaef26e7b4adf0096",
								"http://localhost:8686/auth/rest/feign/auth/logins/users/tokens/keys",
								"http://localhost:8686/auth"));
		
		REGISTED_CLIENTS.put("ppp", 
				new ClientInfo(WebConstants.FP_TYPE_CANVAS,
								WebConstants.AUTH_TYPE_SAVEDIN_COOKIES,
								"ppp",
								"ad89f721e3da95b11378c52112eaa492",
								"http://localhost:8686/ppp/rest/feign/auth/logins/users/tokens/keys",
								"http://localhost:8686/ppp"));
		
	}
	
	public static ClientInfo getClientInfo(String key) {
		ClientInfo ret = null;
//		if (StringUtil.isEmpty(key)) {
//			key = CLIENTKEY_DEFAULT;
//		}
		ret =  REGISTED_CLIENTS.get(key);
		return ret;
	}
	
	public static boolean isExisted(String key) {
		boolean ret = false;
		ClientInfo ci = REGISTED_CLIENTS.get(key);
		if (ci!=null) {
			ret = true;
		}
		return ret;
	}
	
	public static String getCallback4AccessToken(String key) {
		String ret = null;
		ClientInfo ci = getClientInfo(key);
		if (ci!=null) {
			ret = ci.getCallback4AccessToken();
		} 
		return ret;
	}
	
	public static String getSecret(String key) {
		String ret = null;
		ClientInfo ci = getClientInfo(key);
		if (ci!=null) {
			ret = ci.getSecret();
		} 
		return ret;
	}
	
	public static String getFpType(String key) {
		String ret = null;
		ClientInfo ci = getClientInfo(key);
		if (ci!=null) {
			ret = ci.getFpType();
		} 
		return ret;
	}
	
	public static String getAuthType(String key) {
		String ret = null;
		ClientInfo ci = getClientInfo(key);
		if (ci!=null) {
			ret = ci.getAuthType();
		} 
		return ret;
	}
	
	public static String getPerType(String key) {
		String ret = null;
		ClientInfo ci = getClientInfo(key);
		if (ci!=null) {
			ret = ci.getPerType();
		} 
		return ret;
	}
}
