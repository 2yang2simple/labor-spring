package com.labor.spring.feign.api.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseRestController;
import com.labor.spring.bean.Result;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.feign.client.AuthFeignClient;
import com.labor.spring.util.RequiresPermissionsUtil;


@RestController
@RequestMapping("/rest/feign/auth/permissions")
public class AuthPermissionRestController extends BaseRestController{

	@Autowired
	private AuthFeignClient authFeignClient;
	
	@RequestMapping(value = {"/init2auth"}, method = RequestMethod.GET)
	public Result init2auth() {
		Set<String> permissions = new HashSet<String>();
		String clientKey = baseProperties.getContextName();
		if (!StringUtil.isEmpty(clientKey)) {
			List<String> list = WebConstants.RESTCONTROLLER_PAKAGES;
			for (int i=0;i<list.size();i++) {
				String packagename = list.get(i);
				Set<String> perms = RequiresPermissionsUtil.scanRequiresPermissionsValueFromRestController(packagename);
				if (perms!=null&&perms.size()>0) {
					permissions.addAll(perms);
				}
			}
			authFeignClient.refreshPermissions(clientKey, permissions);
		}	
		return Result.success(permissions.size());
	}

	

}
