package com.labor.spring.core.api.permission;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseProperties;
import com.labor.spring.bean.Result;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.aop.AnnotationUtil;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.feign.client.auth.AuthFeignClient;



@RestController
@RequestMapping("/rest/core/permissions")
public class PermissionRestController {
	@Autowired
	private PermissionServiceIntf perService;
	@Autowired
	private AuthFeignClient authFeignClient;
	@Autowired
	private BaseProperties baseProperties;
	
	@RequiresPermissions("permission")
	@RequestMapping(value = {"/initialization"}, method = RequestMethod.POST)
	public Result initialization() {
		Integer ret = perService.initialization();		
		return Result.success(ret.toString());
	}

	@RequestMapping(value = {"/init2auth"}, method = RequestMethod.GET)
	public Result init() {
		Set<String> permissions = new HashSet<String>();
		String clientKey = baseProperties.getContextName();
		if (!StringUtil.isEmpty(clientKey)) {
			List<String> list = WebConstants.RESTCONTROLLER_PAKAGES;
			for (int i=0;i<list.size();i++) {
				String packagename = list.get(i);
				Set<String> perms = AnnotationUtil.scanRequiresPermissionsValueFromRestController(packagename);
				if (perms!=null&&perms.size()>0) {
					permissions.addAll(perms);
				}
			}
			authFeignClient.refreshPermissions(clientKey, permissions);
		}	
		return Result.success(permissions.size());
	}
	
	@RequiresPermissions("permission")
	@RequestMapping(value = {"/actived-list" }, method = RequestMethod.GET)
	public Result findListActived() {
		List<Permission> ret = perService.findListByStatus(CommonConstants.ACTIVE);		
		return Result.success(ret);
	}

}
