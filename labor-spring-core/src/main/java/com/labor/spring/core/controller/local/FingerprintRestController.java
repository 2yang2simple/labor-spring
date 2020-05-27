package com.labor.spring.core.api.fingerprint;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.util.StringUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.core.api.sysconfig.SysconfigConstants;
import com.labor.spring.core.entity.Fingerprint;
import com.labor.spring.core.entity.FingerprintOnline;

@RestController
@RequestMapping("/rest/core/fingerprints")
public class FingerprintRestController {

	@Autowired
	private FingerprintServiceIntf fpService;
	
	@RequiresPermissions("fingerprint")
	@RequestMapping(value = {"/{id}/status/{status}"}, method = RequestMethod.PATCH)
	public Result updateStatus(
				@PathVariable(value="id") Integer id, 
				@PathVariable(value="status") String status) {
		Fingerprint fingerprint = new Fingerprint();
	    if (id!=null&&id>0) {
	    	fingerprint = fpService.findById(id);
	    	if (fingerprint!=null) {
		    	fingerprint.setStatus(status);
		    	return Result.success(fpService.save(fingerprint));
	    	} else {
	    		return Result.failure(ResultStatus.FAILURE_DATA_NOT_FOUND); 
	    	}
	    } else {
	    	return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), "the id is null;"); 
	    }
	}
	
	@RequiresPermissions("fingerprint")
	@RequestMapping(value = {"/sort-list"}, method = RequestMethod.GET)
	public Result findList(
				@RequestParam(value="sortby") String sortby) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		List<Fingerprint> ret  = fpService.findList(sort);
		return Result.success(ret);
	}
	
	@RequiresPermissions("fingerprint")
	@RequestMapping(value = {"/sort-page-list"}, method = RequestMethod.GET)
	public Result findList(
				@RequestParam(value="sortby") String sortby,
				@RequestParam(value="page", defaultValue="0") Integer page,
				@RequestParam(value="pagesize", defaultValue="0") Integer pagesize) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		Page<Fingerprint> ret  = fpService.findList(pageable);
		return Result.success(ret);
	}
	
	@RequiresPermissions("fingerprint")
	@RequestMapping(value = { "/valuestart-list"}, method = RequestMethod.GET)
	public Result findListByValue(
				@RequestParam(value="valuestart") String valuestart) {
		List<Fingerprint> ret  = fpService.findListByValueStartingWith(valuestart);
		return Result.success(ret);
	}

	@RequiresPermissions("fingerprint")
	@RequestMapping(value = { "/valuestart-sort-page-list"}, method = RequestMethod.GET)
	public Result findListByValue(
				@RequestParam(value="sortby") String sortby,
				@RequestParam(value="page", defaultValue="0") Integer page,
				@RequestParam(value="pagesize", defaultValue="0") Integer pagesize, 
				@RequestParam(value="valuestart") String valuestart) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		Page<Fingerprint> ret  = fpService.findListByValueStartingWith(valuestart,pageable);
		return Result.success(ret);
	}
	
	@RequiresPermissions("fingerprint")
	@RequestMapping(value = {"/online/page-list"}, method = RequestMethod.GET)
	public Result findOnlineList(
				@RequestParam(value="sortby") String sortby,
				@RequestParam(value="page", defaultValue="0") Integer page,
				@RequestParam(value="pagesize", defaultValue="0") Integer pagesize) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		Page<FingerprintOnline> ret  = fpService.findFoList(pageable);
		return Result.success(ret);
	}

	@RequiresPermissions("fingerprint")
	@RequestMapping(value = { "/online/valuestart-page-list"}, method = RequestMethod.GET)
	public Result findOnlineListByValue(
				@RequestParam(value="sortby") String sortby,
				@RequestParam(value="page", defaultValue="0") Integer page,
				@RequestParam(value="pagesize", defaultValue="0") Integer pagesize, 
				@RequestParam(value="valuestart") String valuestart) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		Page<FingerprintOnline> ret  = fpService.findFoListByValueStartingWith(valuestart,pageable);
		return Result.success(ret);
	}
	
	@RequiresPermissions("fingerprint")
	@RequestMapping(value = {"/online/{foid}"}, method = RequestMethod.DELETE)
	public Result deleteOnline(
				@PathVariable(value="foid") Integer foid) {
		fpService.deleteOnlineById(foid);
		return Result.success();
	}
	
}
