package com.labor.spring.core.api.role;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;
import com.labor.spring.core.entity.RolePermission;
import com.labor.spring.core.entity.Sysconfig;

public interface RoleServiceIntf {
	
	public Role save(Role role);
	public String saveRolePermission(Integer roleId, List<RolePermission> list);
	
	public Role findById(Integer id);
	public Role findByNameAndStatus(String name, String status);
	
	public List<Role> findList(Sort sort);
	public List<Role> findListByNameStartingWith(String name);
	public List<Role> findListByStatus(String status);
	public List<Role> findListByUserid(Integer userid);
	
}
