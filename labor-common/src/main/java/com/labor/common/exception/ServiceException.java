package com.labor.common.exception;

public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = -1159166833357171007L;
	public ServiceException() {
		super("service exception.");
	}
	public ServiceException(String msg) {
		super(msg);
	}
	public ServiceException(Throwable cause) {
		super(cause);
	}
	public ServiceException(String msg, Throwable cause) {
	  super(msg, cause);
	}
}
