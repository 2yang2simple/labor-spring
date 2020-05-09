package com.labor.spring.auth.service;

import com.labor.spring.core.entity.FingerprintOnline;
import com.labor.spring.core.entity.User;

public interface AccessService {
	
	public User validateNameAndCode(
					String name, String code, String cfmd5, String rememberme);
	public User validateNameAndPassword(
					String name, String saltpwdmd5, String cfmd5, String rememberme, String timestamp);
	public FingerprintOnline createOnline(
					Integer userId, 
					String userName, 
					String authValue, 
					String authType, 
					String fpValue, 
					String fpType, 
					String rememberme);
	public FingerprintOnline updateOnlineAuthcode(String authValue, String authType);

	public FingerprintOnline updateOnlineAccessed(String authCode);

	public FingerprintOnline findOnline(String authValue, String authType);

	public void deleteOnline(String authValue, String authType);
}
