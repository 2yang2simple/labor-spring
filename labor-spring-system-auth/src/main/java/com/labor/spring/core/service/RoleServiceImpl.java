package com.labor.spring.core.api.role;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.ClassUtil;
import com.labor.spring.core.api.permission.PermissionRepository;
import com.labor.spring.core.api.sysconfig.SysconfigConstants;
import com.labor.spring.core.api.user.UserRoleRepository;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;
import com.labor.spring.core.entity.RolePermission;
import com.labor.spring.core.entity.Sysconfig;


@Service
public class RoleServiceImpl implements RoleServiceIntf{
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private RolePermissionRepository rolePermissionRepository;
	
	@Override
	@Transactional
	public Role save(Role role) {
		Role dbrole = roleRepository.findByNameAndStatusIgnoreCase(role.getName(),CommonConstants.ACTIVE);
		if (dbrole!=null&&dbrole.getId()!=null&&dbrole.getId()>0&&!dbrole.getId().equals(role.getId())) {
			return null;
		}
		return roleRepository.save(role);
	}
	
	@Override
	@Transactional
	public String saveRolePermission(Integer roleId, List<RolePermission> list) {
		String ret = null;
    	int count = 0;
    	if (list!=null&&list.size()>0){
			int size = list.size();
			rolePermissionRepository.deleteByRoleid(roleId);
			for (int i=0;i<size;i++) {
				RolePermission rp  = (RolePermission)list.get(i);
				rolePermissionRepository.save(rp);
				count = count+1;
			}
			ret = String.valueOf(count);
		}
    	return ret;
	}
	
	@Override
	public Role findById(Integer id) {
		return roleRepository.findById(id);
	}
	@Override
	public Role findByNameAndStatus(String name, String status) {
		return roleRepository.findByNameAndStatusIgnoreCase(name,status);
	}
	
	@Override
	public List<Role> findList(Sort sort) {
		return roleRepository.findAll(sort);
	}
	@Override
	public List<Role> findListByStatus(String status) {
		return roleRepository.findByStatus(status);
	}
	@Override
	public List<Role> findListByUserid(Integer userid){
		return roleRepository.findByUserid(userid);
	}
	@Override
	public List<Role> findListByNameStartingWith(String name) {
		return roleRepository.findByNameStartingWith(name);
	}
	
}
