package com.labor.spring.bean;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = -4977078875923799466L;

	private int code;

	private String msg;

	private Object data;

	public Result() {
	}

	@Override
	public String toString() {
		return "code: "+ code + ", msg: " + msg;
	}
	
	public Result(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	

	public Result(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Result(ResultStatus status) {
		this.code = status.code();
		this.msg = status.msg();
		
	}
	public static Result success() {
		Result result = new Result(ResultStatus.SUCCESS);
		return result;
	}
	
	public static Result success(Object data) {
		Result result = new Result();
		if (data!=null) {
			result = new Result(ResultStatus.SUCCESS);
			result.setData(data);
		} else {
			result = new Result(ResultStatus.FAILURE_DATA_NOT_FOUND);
		}		
		return result;
	}
	
	public static Result failure() {
		return new Result(ResultStatus.FAILURE);
	}
	
	public static Result failure(ResultStatus status) {
		return new Result(status);
	}
	
	public static Result failure(String msg) {
		Result result = new Result();
		result.setCode(ResultStatus.FAILURE.code());
		result.setMsg(msg);
		return result;
	}

	public static Result failure(int code, String msg) {
		Result result = new Result();
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}

	public static Result failure(int code, String msg, Object data) {
		Result result = new Result();
		result.setCode(code);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
