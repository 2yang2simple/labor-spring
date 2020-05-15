package com.labor.spring.feign.auth;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.labor.spring.bean.LoginCache;
import com.labor.spring.core.entity.User;

public interface AuthCacheService {
	
	public String getToken(String key);
	public String setToken(String key, String token);
	public void clearToken(String key);

	public LoginCache findLoginCache(String accessToken);//get user from cache, if not exist, get it from auth DB, then save it in cache
	public LoginCache fetchLoginCache(String accessToken);//get user from auth DB, then save it in cache
	public void clearLoginCache(String accessToken);
	
	public Set<String> findLoginUserPermissions(String accessToken);
	public Set<String> fetchLoginUserPermissions(String accessToken);
	public void clearLoginUserPermissions(String accessToken);
	
	//clear all cache;
	public void clear();

	//for login logout
  	public User fetchLoginUser(String code);
	public String fetchLoginUserToken(String type,String code);
	public void deleteLogin( String code);
	public String createLogin(String clientKey,String clientUuid,String type,String code,String name);
	
	
}
