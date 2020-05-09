package com.labor.common.exception;

public class DaoException extends RuntimeException {

	private static final long serialVersionUID = 8593486576463015889L;
	public DaoException() {
	}
	public DaoException(String msg) {
		super(msg);
	}
	public DaoException(Throwable cause) {
		super(cause);
	}
	public DaoException(String msg, Throwable cause) {
	  super(msg, cause);
	}
}
