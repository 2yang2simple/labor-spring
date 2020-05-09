package com.labor.spring.bean;

public enum ResultStatus {
	
	SUCCESS(1, "Succeed"),
	FAILURE(10001,"Failed."),
	FAILURE_EXCEPTION_RUNTIME(11000,"runtime exception."),
	FAILURE_EXCEPTION_SHIRO(12000,"shiro exception"),
	FAILURE_EXCEPTION_SEVICE(13000,"service exception."),
	FAILURE_PARAM_INVALID(20001,""),
	FAILURE_PARAM_NULL(20002,"parameter is null."),
	FAILURE_PARAM_NOTAIMAGE(20003,"not a image file."),
	FAILURE_PERMISSION_NOACCESS(30001,"no privilege."),
	FAILURE_PERMISSION_GOOGLEAUTH(30002,"google auth code is not match."),
	FAILURE_DATA_NOT_UNIQUE(40001,"data exist."),
	FAILURE_DATA_NOT_FOUND(40002,"data not found."),
	FAILURE_EXCEPTION_TEMPLATE(50000,"html template exception."),
	FAILURE_LOGIN(60000,"wrong name or password."),
	FAILURE_LOGIN_ACCOUNT_CLOSED(60001,"the account is closed."),
	FAILURE_LOGIN_ACCOUNT_NOTEXIST(60002,"the account is not exist."),
	FAILURE_LOGIN_TOKEN_ERROR(60003,"get token error."),
	FAILURE_LOGIN_ADMIN_FORBID(60004,"can not login as admin.");
	
	
	private final int code;

	private final String msg;

	ResultStatus(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int code() {
		return this.code;
	}

	public String msg() {
		return this.msg;
	}
	

}