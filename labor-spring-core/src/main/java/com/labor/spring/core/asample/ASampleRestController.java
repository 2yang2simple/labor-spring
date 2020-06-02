package com.labor.spring.core.asample;

import java.util.ArrayList;
import java.util.Optional;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseRestController;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.core.service.SysconfigConstants;
import com.labor.spring.util.PropertyMapperUtil;

/**
 * use samples not sample;
 * @author Administrator
 *
 */

@RestController
@RequestMapping("/rest/core/samples")
public class ASampleRestController extends BaseRestController{

	@Autowired
	private ASampleServiceIntf asampleService;
	
	public static void main(String[] args) {
		try {
			ASample as = new ASample();
			ASampleVO asvo = new ASampleVO();
			
			as.setId(1);
			as.setName("test");
			asvo.setId(2);
			asvo.setDisplayName("test2");
			PropertyMapperUtil.copyProperties(as, asvo, true);
			LogManager.getLogger().debug("{},{}", as.getName(),as.getId());
			LogManager.getLogger().debug("{},{}", asvo.getDisplayName(),asvo.getId());

			as.setId(1);
			as.setName("test");
			asvo.setId(2);
			asvo.setDisplayName("test2");
			PropertyMapperUtil.copyProperties(as, asvo, false);
			LogManager.getLogger().debug("{},{}", as.getName(),as.getId());
			LogManager.getLogger().debug("{},{}", asvo.getDisplayName(),asvo.getId());
			
		} catch (Exception e) {
			LogManager.getLogger().error(e);
		}
		
    }
	
	@RequiresPermissions(value= {"user","fingerprint"}, logical=Logical.AND)
	@RequestMapping(value = {"/aopand"}, method = RequestMethod.GET)
	public Result aopand() {
		System.err.println("do aop");
		return Result.success("yes");
	}
	@RequiresPermissions(value= {"user","fingerprint"}, logical=Logical.OR)
	@RequestMapping(value = {"/aopor"}, method = RequestMethod.GET)
	public Result aopor() {
		System.err.println("do aop");
		return Result.success();
	}
	
	@RequestMapping(value = {"/test"}, method = RequestMethod.GET)
	public Result test() {
		System.err.println("test");
		return Result.success("yes");
	}
	
	//create a object
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestBody @Valid ASample asample, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), message);
	    }
		return Result.success(asampleService.create(asample));
	}
	//update the whole object
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Result update(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid ASample asample, 
					BindingResult bindingResult) {
	    if (id!=null&&id>0) {
	    	asample.setId(id);
	    } else {
	    	return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), "the id is null;"); 
	    }
		if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(),message);
	    }
		return Result.success(asampleService.update(asample));
	}
	//update a part of the object
	@RequestMapping(value = {"/{id}/status/{status}"}, method = RequestMethod.PATCH)
	public Result updateStatus(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="status") String status) {
		ASample asample = new ASample();
	    if (id!=null&&id>0) {
	    	Optional<ASample> oas = asampleService.findById(id);
	    	if (oas.isPresent()) {
	    		asample = oas.get();
	    		asample.setStatus(status);
		    	return Result.success(asampleService.update(asample));
	    	} else {
	    		return Result.failure(ResultStatus.FAILURE_DATA_NOT_FOUND); 
	    	}
	    } else {
	    	return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), "the id is null;"); 
	    }
	}

	//find single by identify parameter
	@RequestMapping(value = {"/uuid-{uuid}"}, method = RequestMethod.GET)
	public Result findByUuid(
					@PathVariable(value="uuid") String uuid) {
		Optional<ASample> oas = null;
		if(!StringUtil.isEmpty(uuid)) {
			ASample as = new ASample();
			as.setUuid(uuid);
			oas = asampleService.findByExample(as);
		}
		return Result.success(oas.get());

	}
	//find list by order
	@RequestMapping(value = {""}, method = RequestMethod.GET)
	public Result findList(
					@RequestParam(value="sortby", defaultValue="id") String sortby){
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		return Result.success(asampleService.findList(sort));
		
	}
	//find list by order
	@RequestMapping(value = {"/page-list"}, method = RequestMethod.GET)
	public Result findList(
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="page", defaultValue="0" ) Integer page,
					@RequestParam(value="pagesize",defaultValue="0" ) Integer pagesize) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		return Result.success(asampleService.findList(pageable));
	}
	
	//find list in pages by status 
	@RequestMapping(value = {"/status-page-list"}, method = RequestMethod.GET)
	public Result findListByStatus(
					@RequestParam(value="status", required=false) String status,
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="page", defaultValue="0" ) Integer page,
					@RequestParam(value="pagesize",defaultValue="0" ) Integer pagesize) {
		ASample as = new ASample();
		if(!StringUtil.isEmpty(status)) {
			as.setStatus(status);
		}
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		return Result.success(asampleService.findListByExample(as,pageable));
	}

	@RequestMapping(value = { "/namestart-page-list"}, method = RequestMethod.GET)
	public Result findListByName(
					@RequestParam(value="namestart") String namestart,
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="page", defaultValue="0" ) Integer page,
					@RequestParam(value="pagesize",defaultValue="0" ) Integer pagesize) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		Page<ASample> ret  = asampleService.findListByNameStartingWith(namestart,pageable);
		return Result.success(ret);
	}
	
	//find all sub objects in list
	@RequestMapping(value = {"/{id}/subs"}, method = RequestMethod.GET)
	public Result findSubsListByStatus(
					@PathVariable(value="id") Integer id,
					@RequestParam(value="status", required=false) String status) {
		return Result.success(new ArrayList<ASample>());
	}
}
