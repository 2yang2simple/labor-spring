package com.labor.spring.feign.auth;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestParam;

import com.labor.spring.bean.LoginCache;
import com.labor.spring.core.entity.User;

public interface AuthLoginService {
	
	public String getTokenCache(String key);
	public String setTokenCache(String key, String token);
	
	public LoginCache findLoginCache(String accessToken);
	public LoginCache findLoginCacheCurrent();
	public LoginCache getLoginCacheCurrent();
	
	public LoginCache fetchLoginCache(String accessToken);
	public LoginCache fetchLoginCacheCurrent();
	
	public Set<String> fetchUserPermissionsCurrent();
	public Set<String> findUserPermissionsCurrent();
	
	public User findUser(String accessToken);
	public User fetchUser(String accessToken);
	public String fetchUserToken(String type,String code);
	
	public boolean isCurrentUserOrSuperUser(Long userid, String useruuid);
	
	public String create(
					String clientKey,
					String clientUuid,
					String type,
					String code,
					String name);

	public void delete(String accessToken);
	public void deleteCache();
}
