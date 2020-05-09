package com.labor.spring.core.api.permission;

import java.util.List;

import com.labor.spring.core.entity.Permission;


public interface PermissionServiceIntf {

	public Integer initialization();
	public List<Permission> findListByStatus(String status);
	public List<Permission> findListByRoleid(Integer roleid);
	public List<Permission> findListByUserid(Integer userid);
}
