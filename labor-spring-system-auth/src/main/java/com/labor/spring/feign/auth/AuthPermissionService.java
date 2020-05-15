package com.labor.spring.feign.auth;

import java.util.Set;

public interface AuthPermissionService {

	
	public Set<String> findUserPermissions(Integer userid, String username);
}
