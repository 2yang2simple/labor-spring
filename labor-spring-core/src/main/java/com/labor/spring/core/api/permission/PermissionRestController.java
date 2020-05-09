package com.labor.spring.core.api.permission;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.spring.bean.Result;
import com.labor.spring.core.entity.Permission;



@RestController
@RequestMapping("/rest/core/permissions")
public class PermissionRestController {
	@Autowired
	private PermissionServiceIntf perService;
	
	@RequiresPermissions("permission")
	@RequestMapping(value = {"/initialization"}, method = RequestMethod.POST)
	public Result initialization() {
		Integer ret = perService.initialization();		
		return Result.success(ret.toString());
	}

	@RequiresPermissions("permission")
	@RequestMapping(value = {"/actived-list" }, method = RequestMethod.GET)
	public Result findListActived() {
		List ret = perService.findListByStatus(CommonConstants.ACTIVE);		
		return Result.success(ret);
	}

}
