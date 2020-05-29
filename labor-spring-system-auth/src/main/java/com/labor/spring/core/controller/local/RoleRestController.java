package com.labor.spring.core.controller.local;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;
import com.labor.spring.core.entity.RolePermission;
import com.labor.spring.core.entity.Sysconfig;
import com.labor.spring.core.service.PermissionServiceIntf;
import com.labor.spring.core.service.RoleServiceIntf;
import com.labor.spring.core.service.SysconfigConstants;




@RestController
@RequestMapping("/rest/core/roles")
public class RoleRestController {
	
	@Autowired
	private RoleServiceIntf roleService;
	
	@Autowired
	private PermissionServiceIntf perService;

	@RequiresPermissions("role")
	@RequestMapping(value = { "" }, method = RequestMethod.POST)
	public Result create(
					@RequestBody Role role,
					BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String message = bindingResult.getFieldError().getDefaultMessage();
			return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(),message);
		}
		role.setStatus(CommonConstants.ACTIVE);
		return Result.success(roleService.save(role));
	}

	@RequiresPermissions("role")
	@RequestMapping(value = {"/{id}/permissions" }, method = RequestMethod.POST)
	public Result createPermissions(
					@PathVariable(value="id") Long id, 
					@RequestBody List<RolePermission> list) {
		if (id==null||id<=0) {
	    	return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), "the id is null."); 
	    }
		return Result.success(roleService.saveRolePermission(id,list));		
	}
	
	@RequiresPermissions("role")
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.PUT)
	public Result update(
					@PathVariable(value="id") Long id, 
					@RequestBody Role role,
					BindingResult bindingResult) {
		if (id!=null&&id>0) {
			role.setId(id);
	    } else {
	    	return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), "the id is null."); 
	    }
		if (bindingResult.hasErrors()) {
			String message = bindingResult.getFieldError().getDefaultMessage();
			return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(),message);
		}
		return Result.success(roleService.save(role));
	}
	
	@RequiresPermissions("role")
	@RequestMapping(value = { "/{id}"}, method = RequestMethod.GET)
	public Result findById(
					@PathVariable(value="id") Long id) {
		return Result.success(roleService.findById(id));
	}
	
	@RequiresPermissions("role")
	@RequestMapping(value = { "/sort-list" }, method = RequestMethod.GET)
	public Result findList(
					@RequestParam(value="sortby", defaultValue="status") String sortby) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		return Result.success(roleService.findList(sort));
	}
	
	@RequiresPermissions("role")
	@RequestMapping(value = { "/namestart-list"}, method = RequestMethod.GET)
	public Result findListByName(
					@RequestParam(value="namestart") String namestart) {
		return Result.success(roleService.findListByNameStartingWith(namestart));
	}
	
	@RequiresPermissions("role")
	@RequestMapping(value = {"/actived-list"}, method = RequestMethod.GET)
	public Result findListActived() {
		return Result.success(roleService.findListByStatus(CommonConstants.ACTIVE));
	}

	@RequiresPermissions("role")
	@RequestMapping(value = {"/{id}/permissions" }, method = RequestMethod.GET)
	public Result findPermissionListByRoleid(
					@PathVariable(value="id") Long id) {
		return Result.success(perService.findListByRoleid(id));		
	}
	
}
