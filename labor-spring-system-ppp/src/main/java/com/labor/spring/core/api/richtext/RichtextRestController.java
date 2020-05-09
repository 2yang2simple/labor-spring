package com.labor.spring.core.api.richtext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.labor.common.util.TokenUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;


@RestController
@RequestMapping("/rest/core/richtexts")
public class RichtextRestController {
	
	@Autowired
	private RichtextServiceIntf richtextService ;

	@RequiresPermissions("richtext")
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestBody @Valid Richtext richtext,
					BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String message = bindingResult.getFieldError().getDefaultMessage();
			return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
		}
		Richtext ret = richtextService.create(richtext);;
		return Result.success(ret);
	}
	
	@RequiresPermissions("richtext")
	@RequestMapping(value = {"/{id}" }, method = RequestMethod.PUT)
	public Result update(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid Richtext richtext,
					BindingResult bindingResult) {
	    if (id!=null&&id>0) {
	    	richtext.setId(id);
	    } else {
	    	return Result.failure(ResultCode.FAILURE_PARAM_INVALID, "the id is null;"); 
	    }
		if (bindingResult.hasErrors()) {
			String message = bindingResult.getFieldError().getDefaultMessage();
			return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
		}
		return Result.success(richtextService.update(richtext));
	}
	
	//update a part of the object
	@RequiresPermissions("richtext")
	@RequestMapping(value = {"/{id}/status/{status}"}, method = RequestMethod.PATCH)
	public Result updateStatus(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="status") String status) {
		Richtext richtext = new Richtext();
	    if (id!=null&&id>0) {
	    	richtext = richtextService.findById(id);
	    	if (richtext!=null) {
	    		richtext.setStatus(status);
		    	return Result.success(richtextService.update(richtext));
	    	} else {
	    		return Result.failure(ResultCode.FAILURE_DATA_NOT_FOUND, ResultCode.MSG_FAILURE_DATA_NOT_FOUND); 
	    	}
	    } else {
	    	return Result.failure(ResultCode.FAILURE_PARAM_INVALID, "the id is null;"); 
	    }
	}

	@RequestMapping(value = {"/uuid-{uuid}" }, method = RequestMethod.GET)
	public Result findByUuid(
					@PathVariable(value="uuid") String uuid) {
		return Result.success(richtextService.findByUuid(uuid));
	}
	

	@RequestMapping(value = {"/name-{name}" }, method = RequestMethod.GET)
	public Result findByName(
					@PathVariable(value="name") String name) {
		return Result.success(richtextService.findByName(name));
	}
	
	@RequiresPermissions("richtext")
	@RequestMapping(value = {"/namestart-list" }, method = RequestMethod.GET)
	public Result findListByName(
					@RequestParam(value="namestart") String namestart) {
		return Result.success(richtextService.findListByNameStartingWith(namestart));
	}

	@RequiresPermissions("richtext")
	@RequestMapping(value = {"/sort-list"}, method = RequestMethod.GET)
	public Result findList(
				@RequestParam(value="sortby") String sortby) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		List<Richtext> ret = richtextService.findList(sort);
		return Result.success(ret);
	}
}
