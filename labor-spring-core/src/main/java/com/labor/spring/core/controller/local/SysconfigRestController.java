package com.labor.spring.core.controller.local;

import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.labor.spring.core.entity.Sysconfig;
import com.labor.spring.core.service.SysconfigServiceIntf;

@RestController
@RequestMapping("/rest/core/sysconfigs")
public class SysconfigRestController {

	@Autowired
	private SysconfigServiceIntf sysconfigService;

	@RequiresPermissions("sysconfig")
	@RequestMapping(value = { "" }, method = RequestMethod.POST)
	public Result create(
					@RequestBody @Valid Sysconfig sysconfig, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(),message);
	    }
		sysconfig.setStatus(CommonConstants.ACTIVE);
		Sysconfig ret = sysconfigService.save(sysconfig);
		return Result.success(ret);
	}
	
	@RequiresPermissions("sysconfig")
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.PUT)
	public Result update(
					@PathVariable(value="id") Long id, 
					@RequestBody @Valid Sysconfig sysconfig, 
					BindingResult bindingResult) {
	    if (id!=null&&id>0) {
		    sysconfig.setId(id);
	    } else {
	    	return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), "the id is null;"); 
	    }
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(),message);
	    }
		Sysconfig ret = sysconfigService.save(sysconfig);
		return Result.success(ret);
	}
	
	@RequiresPermissions("sysconfig")
	@RequestMapping(value = { "/sort-list"}, method = RequestMethod.GET)
	public Result findList(
					@RequestParam(value="sortby") String sortby) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		List<Sysconfig> ret  = sysconfigService.findList(sort);
		return Result.success(ret);
	}

	@RequiresPermissions("sysconfig")
	@RequestMapping(value = { "/keystart-list"}, method = RequestMethod.GET)
	public Result findListByKey(
					@RequestParam(value="keystart") String keystart) {
		List<Sysconfig> ret  = sysconfigService.findListByKeyStartingWith(keystart);
		return Result.success(ret);
	}
	
	
	@RequiresPermissions("sysconfig")
	@RequestMapping(value = {"/initialization" }, method = RequestMethod.GET)
	public Result initialization() {
		return Result.success(sysconfigService.initialization());
	}


	@RequiresPermissions("sysconfig")
	@RequestMapping(value = {"/defaults" }, method = RequestMethod.POST)
	public Result createDefaults() {
		return Result.success(sysconfigService.createDefaultSysconfig());
	}
}
