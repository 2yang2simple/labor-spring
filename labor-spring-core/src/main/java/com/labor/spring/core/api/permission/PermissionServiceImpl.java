package com.labor.spring.core.api.permission;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.ClassUtil;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.api.sysconfig.SysconfigConstants;
import com.labor.spring.core.entity.Permission;

@Service
public class PermissionServiceImpl implements PermissionServiceIntf {

	@Autowired
	private PermissionRepository permissionRepository;
	
	@Override
	public List<Permission> findListByStatus(String status) {
		List ret = null;
		ret = permissionRepository.findByStatusOrderByCodeDesc(status);
		return ret;
	}
	
	@Override
	public List<Permission> findListByRoleid(Integer roleid) {
		List ret = null;
		ret = permissionRepository.findByRoleid(roleid);
		return ret;
	}
	
	@Override
	public List<Permission> findListByUserid(Integer userid) {
		List ret = null;
		ret = permissionRepository.findByUserid(userid);
		return ret;
	}
	
	@Override
	@Transactional
	public Integer initialization() {
		Integer ret =0;
		//first set all permission to inactive;
		permissionRepository.inactivePermissions();
		List<String> list = WebConstants.RESTCONTROLLER_PAKAGES;
		for (int i=0;i<list.size();i++) {
			String packagename = list.get(i);
			ret = ret + initPermissions2DB(packagename);
		}
		return ret;
	}

	private Integer initPermissions2DB(String packagename) {
		int ret =0;
		try {
			Set set = ClassUtil.findClasses(packagename);
			for (Object obj : set) {
				if (obj instanceof Class<?>) {
					Class<?> clazz = (Class<?>) obj;
					ret = ret + saveMethodRequiresPermissionsValueFromClassRestController(clazz);
					
				}
			}

		} catch (ClassNotFoundException e) {
			LogManager.getLogger().error("",e);
		}
		return ret;
	}
	
	private int saveMethodRequiresPermissionsValueFromClassRestController(Class<?> clazz) throws ClassNotFoundException {
		int ret = 0;
		RestController declaredAnnotation = clazz.getDeclaredAnnotation(RestController.class);
		//if class is the rest controller then 
		if (declaredAnnotation != null&&declaredAnnotation.value() != null) {
			Method[] declaredMethods = clazz.getDeclaredMethods();

			for (Method declaredMethod : declaredMethods) {
			
				RequiresPermissions fieldAnnotation = declaredMethod.getDeclaredAnnotation(RequiresPermissions.class);
				//get requires permission value of the method  
				if (fieldAnnotation != null&&fieldAnnotation.value()!=null) {

					int len = fieldAnnotation.value().length;
					for (int i = 0; i < len; i++) {
						LogManager.getLogger().debug(clazz.getName()+"|"
														+declaredMethod.getName()+"|"
														+"value"+i+"|"
														+fieldAnnotation.value()[i]);
						//save to db
						String pcode = fieldAnnotation.value()[i];
						Permission permission = permissionRepository.findByCode(pcode);
						if(permission!=null&&permission.getId()!=null&&permission.getId()>0) {
							//existed, set status active
							permission.setStatus(CommonConstants.ACTIVE);
							permissionRepository.save(permission);
						} else {
							permission = new Permission();
							permission.setCode(pcode);
							permission.setStatus(CommonConstants.ACTIVE);
							permissionRepository.save(permission);
							ret++;
							
						}
						
						
					}
				}

			}

		}
		return ret;

	}
}
