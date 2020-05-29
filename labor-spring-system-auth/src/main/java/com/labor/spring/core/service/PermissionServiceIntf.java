package com.labor.spring.core.service;

import java.util.List;
import java.util.Set;

import com.labor.spring.core.entity.Permission;


public interface PermissionServiceIntf {

	public Integer initialization();
	public List<Permission> findListByStatus(String status);
	public List<Permission> findListByRoleid(Long roleid);
	public List<Permission> findListByUserid(Long userid);
	
	public List<Permission> findListByStatus(String status, String type);
	public List<Permission> findListByRoleid(Long roleid, String type);
	public List<Permission> findListByUserid(Long userid, String type);
	
	public Permission save(Permission permission);
	public void refreshByType(Set<String> permissions, String type);
}
