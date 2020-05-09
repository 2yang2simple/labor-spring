package com.labor.common.exception;

public class PermissionException extends RuntimeException{

	private static final long serialVersionUID = -3794839138677784883L;
	
	public PermissionException() {
	}
	public PermissionException(String msg) {
		super(msg);
	}
	public PermissionException(Throwable cause) {
		super(cause);
	}
	public PermissionException(String msg, Throwable cause) {
	  super(msg, cause);
	}
}
