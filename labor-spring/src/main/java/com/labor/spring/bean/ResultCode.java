package com.labor.spring.bean;

public class ResultCode {
	
	public final static int SUCCESS=1;
	public final static int FAILURE=10001;
	public final static int FAILURE_EXCEPTION_RUNTIME=11000;
	public final static int FAILURE_EXCEPTION_SHIRO=12000;
	public final static int FAILURE_PARAM_INVALID=20001;
	public final static int FAILURE_PARAM_NULL=20002;
	public final static int FAILURE_PARAM_NOTAIMAGE=20003;
	public final static int FAILURE_PERMISSION_NOACCESS=30001;
	public final static int FAILURE_PERMISSION_GOOGLEAUTH=30002;
	public final static int FAILURE_DATA_NOT_UNIQUE=40001;
	public final static int FAILURE_DATA_NOT_FOUND=40002;
	public final static int FAILURE_EXCEPTION_TEMPLATE=50000;
	public final static int FAILURE_LOGIN=60000;
	public final static int FAILURE_LOGIN_ACCOUNT_CLOSED=60001;
	public final static int FAILURE_LOGIN_ACCOUNT_NOTEXIST=60002;
	public final static int FAILURE_LOGIN_TOKEN_ERROR=60003;
	public final static int FAILURE_LOGIN_ADMIN_FORBID=60004;
	
	public final static String MSG_SUCCESS="Succeed.";
	public final static String MSG_FAILURE="Failed.";
	public final static String MSG_FAILURE_DATA_NOT_FOUND="data not found.";
	public final static String MSG_FAILURE_DATA_NOT_UNIQUE="data exist.";
	public final static String MSG_FAILURE_PARAM_NULL="parameter is null.";
	public final static String MSG_FAILURE_PARAM_NOTAIMAGE="not a image file.";
	public final static String MSG_FAILURE_PERMISSION_NOACCESS="no privilege.";
	public final static String MSG_FAILURE_PERMISSION_GOOGLEAUTH="google auth code is not match.";
	public final static String MSG_FAILURE_EXCEPTION_TEMPLATE="html template exception.";
	public final static String MSG_FAILURE_LOGIN="wrong name or password.";
	public final static String MSG_FAILURE_LOGIN_ACCOUNT_CLOSED="the account is closed.";
	public final static String MSG_FAILURE_LOGIN_ACCOUNT_NOTEXIST="the account is not exist.";
	public final static String MSG_FAILURE_LOGIN_TOKEN_ERROR="get token error.";
	public final static String MSG_FAILURE_LOGIN_ADMIN_FORBID="can not login as admin.";


}