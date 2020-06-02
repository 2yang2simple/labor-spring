package com.labor.spring.feign.api.auth;

import java.io.Serializable;

import com.labor.common.util.StringUtil;
import com.labor.spring.constants.WebConstants;


public class UserVO implements Serializable {

	private static final long serialVersionUID = 6399183203898874627L;

	private Long userId;
	private String userName; 
	private String userRealName; 
	private String userUuid;
	
	
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
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
	
	
//	public static boolean isLoginBySuper(LoginCache currentuser) {
//		boolean ret = false;
//    	if (currentuser!=null&&StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER,currentuser.getUserName())){
//    		ret = true;
//    	}
//        return ret;
//	}
//	
//	public static boolean isLoginBySuperOrIsCurrentUser(LoginCache user,LoginCache currentuser) {
//		if (user!=null&&currentuser!=null) {
//			if(StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER,currentuser.getUserName())){
//				return true;
//			}
//			if(currentuser.getUserId().equals(user.getUserId())) {
//				return true;
//			}
//		}
//		return false;
//	}
}
