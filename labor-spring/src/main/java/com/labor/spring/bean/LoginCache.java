package com.labor.spring.bean;

import java.io.Serializable;

public class LoginCache implements Serializable {

	private static final long serialVersionUID = 6399183203898874627L;

	private Integer userId;
	private String userName; 
	private String userRealName; 
	private String userUuid;
	
	
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	} 
	
	
}
