package com.labor.spring.core.service;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.ClassUtil;
import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseProperties;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.aop.AnnotationUtil;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Sysconfig;
import com.labor.spring.util.IgnorePropertiesUtil;

@Service
public class PermissionServiceImpl implements PermissionServiceIntf {

	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private BaseProperties baseProperties;
	
	@Override
	public List<Permission> findListByStatus(String status) {
		List<Permission> ret = null;
		ret = permissionRepository.findByStatusOrderByTypeAsc(status);
		return ret;
	}
	
	@Override
	public List<Permission> findListByRoleid(Integer roleid) {
		List<Permission> ret = null;
		ret = permissionRepository.findByRoleid(roleid);
		return ret;
	}
	
	@Override
	public List<Permission> findListByUserid(Integer userid) {
		List<Permission> ret = null;
		ret = permissionRepository.findByUserid(userid);
		return ret;
	}
	
	@Override
	public List<Permission> findListByStatus(String status, String type) {
		List<Permission> ret = null;
		ret = permissionRepository.findByStatusAndTypeOrderByTypeAsc(status, type);
		return ret;
	}
	
	@Override
	public List<Permission> findListByRoleid(Integer roleid, String type) {
		List<Permission> ret = null;
		ret = permissionRepository.findByRoleidAndType(roleid, type);
		return ret;
	}
	
	@Override
	public List<Permission> findListByUserid(Integer userid, String type) {
		List<Permission> ret = null;
		ret = permissionRepository.findByUseridAndType(userid, type);
		return ret;
	}
	
	@Override
	@Transactional
	public Permission save(Permission permission) {	
		Permission ret = null;
		if (permission==null) {
			return ret;
		}
		if (StringUtil.isEmpty(permission.getCode())){
			return ret;
		}
		Permission per = null;
		if (StringUtil.isEmpty(permission.getType())) {
			return ret;
		}
		
		per = permissionRepository.findByCodeAndType(permission.getCode().toLowerCase()
													,permission.getType().toLowerCase());
		if (per==null) {
			per = permission;
		} else {
			BeanUtils.copyProperties(permission,per,IgnorePropertiesUtil.getNullPropertyNames(permission));
		}
		ret = permissionRepository.save(per);
		return ret;
	}
	
	@Override
	@Transactional
	public void refreshByType(Set<String> permissions, String type) {
		if (permissions!=null&&permissions.size()>0) {
			if (!StringUtil.isEmpty(type)) {
				permissionRepository.inactivePermissions(type);
				for (String code: permissions) {
					Permission per = new Permission();
					per.setStatus(CommonConstants.ACTIVE);
					per.setCode(code);
					per.setType(type);
					save(per);
				}
			}
		}

	}
	@Override
	@Transactional
	public Integer initialization() {
		Set<String> allperms = new HashSet<String>();
		String pertype = baseProperties.getContextName();
		if (!StringUtil.isEmpty(pertype)) {
			List<String> list = WebConstants.RESTCONTROLLER_PAKAGES;
			for (int i=0;i<list.size();i++) {
				String packagename = list.get(i);
				Set<String> perms = AnnotationUtil.scanRequiresPermissionsValueFromRestController(packagename);
				if (perms!=null&&perms.size()>0) {
					allperms.addAll(perms);
				}
			}
			refreshByType(allperms, pertype);
		}
		LogManager.getLogger().debug("pertype: [{}]",pertype);
		return allperms.size();
	}
	
//	
//	@Override
//	@Transactional
//	public Integer initialization() {
//		Integer ret =0;
//		//first set all permission to inactive;
//		String pertype = baseProperties.getContextName();
//		if (!StringUtil.isEmpty(pertype)) {
//			permissionRepository.inactivePermissions(pertype);
//			List<String> list = WebConstants.RESTCONTROLLER_PAKAGES;
//			for (int i=0;i<list.size();i++) {
//				String packagename = list.get(i);
//				ret = ret + initPermissions2DB(packagename,pertype);
//
//			}
//		}
//		LogManager.getLogger().debug("pertype: [{}]",pertype);
//		return ret;
//	}
//
//	private Integer initPermissions2DB(String packagename, String pertype) {
//		int ret =0;
//		try {
//			Set set = ClassUtil.findClasses(packagename);
//			for (Object obj : set) {
//				if (obj instanceof Class<?>) {
//					Class<?> clazz = (Class<?>) obj;
//					ret = ret + saveMethodRequiresPermissionsValueFromClassRestController(clazz, pertype);
//					
//				}
//			}
//
//		} catch (ClassNotFoundException e) {
//			LogManager.getLogger().error("",e);
//		}
//		return ret;
//	}
//	
//	private int saveMethodRequiresPermissionsValueFromClassRestController(Class<?> clazz, String ptype) throws ClassNotFoundException {
//		int ret = 0;
//		RestController declaredAnnotation = clazz.getDeclaredAnnotation(RestController.class);
//		//if class is the rest controller then 
//		if (declaredAnnotation != null&&declaredAnnotation.value() != null) {
//			Method[] declaredMethods = clazz.getDeclaredMethods();
//
//			for (Method declaredMethod : declaredMethods) {
//			
//				RequiresPermissions fieldAnnotation = declaredMethod.getDeclaredAnnotation(RequiresPermissions.class);
//				//get requires permission value of the method  
//				if (fieldAnnotation != null&&fieldAnnotation.value()!=null) {
//
//					int len = fieldAnnotation.value().length;
//					for (int i = 0; i < len; i++) {
//						LogManager.getLogger().debug(clazz.getName()+"|"
//														+declaredMethod.getName()+"|"
//														+"value"+i+"|"
//														+fieldAnnotation.value()[i]);
//						//save to db
//						String pcode = fieldAnnotation.value()[i];
//						Permission permission = permissionRepository.findByCodeAndType(pcode,ptype);
//						if(permission!=null&&permission.getId()!=null&&permission.getId()>0) {
//							//existed, set status active
//							permission.setStatus(CommonConstants.ACTIVE);
//							save(permission);
//						} else {
//							permission = new Permission();
//							permission.setCode(pcode);
//							permission.setType(ptype);
//							permission.setStatus(CommonConstants.ACTIVE);
//							save(permission);
//							ret++;
//							
//						}
//						
//						
//					}
//				}
//
//			}
//
//		}
//		return ret;
//	}
}
