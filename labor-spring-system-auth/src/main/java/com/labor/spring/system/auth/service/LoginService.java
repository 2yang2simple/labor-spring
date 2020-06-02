package com.labor.spring.system.auth.service;

import com.labor.spring.core.entity.FingerprintOnline;
import com.labor.spring.core.entity.User;

/***
 * login a user
 * @author Administrator
 *
 */
public interface LoginService {
	
	//create login user;
	public FingerprintOnline create(String fpType,
								String fpValue,
								String authType,
								String authValue,
								String accountType, 
								String accountValue);
	
	public FingerprintOnline find(String authValue);
	
	//delete login user
	public void delete(String authValue);
	
	//refresh token
	public FingerprintOnline refreshAuthValue(String authValue);
	
	//access token
	public FingerprintOnline requestAuthValue(String authCode);
    
	public User findUserByAuthValue(String authValue);

}
