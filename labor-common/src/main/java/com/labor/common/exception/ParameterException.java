package com.labor.common.exception;

public class ParameterException extends RuntimeException{

	private static final long serialVersionUID = -1159166833357171007L;
	public ParameterException() {
		super("wrong parameter.");
	}
	public ParameterException(String msg) {
		super(msg);
	}
	public ParameterException(Throwable cause) {
		super(cause);
	}
	public ParameterException(String msg, Throwable cause) {
	  super(msg, cause);
	}
}
