package com.labor.spring.system.auth.service;

import java.util.Set;

public interface AuthService {

	
	public Set<String> findUserPermissions(Long userid, String username);
	
	public Set<String> findUserPermissions(Long userid, String username, String type);
}
