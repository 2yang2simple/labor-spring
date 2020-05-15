package com.labor.spring.feign.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.api.permission.PermissionServiceIntf;
import com.labor.spring.core.api.role.RoleServiceIntf;
import com.labor.spring.core.api.user.UserServiceIntf;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;

@Service
public class AuthPermissionServiceImpl implements AuthPermissionService{
	
	@Autowired
	private PermissionServiceIntf perService;
	@Autowired
	private RoleServiceIntf roleService;
	
	public Set<String> findUserPermissions(Integer userid, String username){

		System.err.println("----AuthPermissionServiceImpl--------findUserPermissions--");
		Set<String> ret = new HashSet<String>();
		List<Permission> list;
		// if login by superuser. return all the permissions
		boolean issuperuser = false;
		int size;
		if (StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER, username)) {
			list = perService.findListByStatus(CommonConstants.ACTIVE);
			// if permission is null, then init permission from the package class;
			if (list != null && list.size() == 0) {
//				perService.initialization();	
				list = perService.findListByStatus(CommonConstants.ACTIVE);
			}
			issuperuser = true;
		} else {
			list = perService.findListByUserid(userid);
		}
		if (list != null) {
			size = list.size();
			for (int i = 0; i < size; i++) {
				Permission p = (Permission) list.get(i);
				ret.add(p.getCode());
			}
		}
		// if not superuser, add common permissions of common user.
		if (!issuperuser) {
			Role r = roleService.findByNameAndStatus(WebConstants.ROLENAME_COMMONUSER, CommonConstants.ACTIVE);
			if (r != null && r.getId() > 0) {
				list = perService.findListByRoleid(r.getId());
				if (list != null) {
					size = list.size();
					for (int i = 0; i < size; i++) {
						Permission p = (Permission) list.get(i);
						ret.add(p.getCode());
					}
				}
			}
		}
		
		return ret;
	}

}
