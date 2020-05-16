package com.labor.spring.constants;

import java.util.ArrayList;
import java.util.List;

public class WebConstants {
	
	public final static String STATUS_LOGIN_ACCESSED = "accessed";
	public final static String KEY_USER = "user";
	public final static String KEY_PERMISSION = "permission";
	public final static String KEY_FP = "fp";
	public final static String FP_TYPE_CANVAS = "web-canvas";
	public final static String FP_TYPE_APP_WEIXIN = "app-weixin";
	public final static String FP_TYPE_APP_IOS = "app-ios";
	
	//for REST api token
	public final static String KEY_CLIENTKEY = "client-key";
	public final static String KEY_CLIENTSECRET = "client-secret";
	public final static String KEY_TIMESTAMP = "timestamp";
	public final static String KEY_TIMESTAMPTOKEN = "timestamp-token";
	
	//auth_value for access 
	public final static String KEY_ACCESSTOKEN = "access-token";
	
//	public final static String AUTH_TYPE_SAVEDIN_SESSION = "session";
	public final static String AUTH_TYPE_SAVEDIN_COOKIES = "cookies";
//	public final static String AUTH_TYPE_SAVEDIN_REQUEST = "request";
	public final static String AUTH_TYPE_SAVEDIN_APP = "app";

	public final static String PERMISSIONS_ALLPASS = "*allpass*";
	
	//create a super user to do everything
	public final static String USERNAME_SUPER = "administrator";
	
	//grant some permissions to this role. every login user has these permissions.
	public final static String ROLENAME_COMMONUSER = "commonuser";
	public final static String ROLENAME_UNLOGINUSER = "unloginuser";//

	public static List<String> RESTCONTROLLER_PAKAGES = new ArrayList<String>();

	static {
//		RESTCONTROLLER_PAKAGES.add("com.labor.spring.web.controller.core");
		RESTCONTROLLER_PAKAGES.add("com.labor.spring");
//		RESTCONTROLLER_PAKAGES.add("com.auth.spring.web.controller");
//		RESTCONTROLLER_PAKAGES.add("com.auth.spring.web.controller.client");
//		RESTCONTROLLER_PAKAGES.add("com.auth.spring.web.controller.profile");
//		RESTCONTROLLER_PAKAGES.add("com.auth.spring.web.controller.sso");

	}
}
