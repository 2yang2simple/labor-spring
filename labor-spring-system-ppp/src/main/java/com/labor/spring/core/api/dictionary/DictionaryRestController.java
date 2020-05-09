package com.labor.spring.core.api.dictionary;


import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
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
import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;



@RestController
@RequestMapping("/rest/core/dictionaries")
public class DictionaryRestController {

	@Autowired
	private DictionaryServiceIntf dictionaryService;
	
	//create top dictionary from the constants
	@RequiresPermissions("dictionary")
	@RequestMapping(value = {"/tops-init"}, method = RequestMethod.POST)
	public Result createTopDictionarys() {
		return Result.success(dictionaryService.createTopDictionarys());
	}
	
	//create a object
	@RequiresPermissions("dictionary")
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestBody @Valid Dictionary dictionary, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    dictionary.setStatus(CommonConstants.ACTIVE);
	    Dictionary ret = dictionaryService.create(dictionary);
		return Result.success(ret);
	}
	//update the object
	@RequiresPermissions("dictionary")
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Result update(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid Dictionary dictionary, 
					BindingResult bindingResult) {
	    if (id!=null&&id>0) {
	    	dictionary.setId(id);
	    } else {
	    	return Result.failure(ResultCode.FAILURE_PARAM_INVALID, "the id is null;"); 
	    }
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    Dictionary ret = dictionaryService.update(dictionary);
		return Result.success(ret);
	}

	//update the object
	@RequiresPermissions("dictionary")
	@RequestMapping(value = {"/{id}/status/{status}"}, method = RequestMethod.PATCH)
	public Result updateStatus(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="status") String status) {
		Dictionary dictionary = new Dictionary();
	    if (id!=null&&id>0) {
	    	Optional<Dictionary> od = dictionaryService.findById(id);
	    	if (od.isPresent()) {
	    		dictionary = od.get();
	    		dictionary.setStatus(status);
		    	return Result.success(dictionaryService.update(dictionary));
	    	} else {
	    		return Result.failure(ResultCode.FAILURE_DATA_NOT_FOUND, ResultCode.MSG_FAILURE_DATA_NOT_FOUND); 
	    	}
	    } else {
	    	return Result.failure(ResultCode.FAILURE_PARAM_INVALID, "the id is null;"); 
	    }
	}

	//find  return in list
	@RequestMapping(value = {"/subs"}, method = RequestMethod.GET)
	public Result findSubsList(
					@RequestParam(value="code", required=false) String code, 
					@RequestParam(value="status", required=false) String status) {
		Result ret = null;
		if (StringUtil.isEmpty(code)&&StringUtil.isEmpty(status)) {
			ret = Result.success(dictionaryService.findSubList());
		} else {
			ret = Result.success(dictionaryService.findSubList(code, status));
		}
		return ret;
	}
	//find all return in list
	@RequestMapping(value = {"/tops"}, method = RequestMethod.GET)
	public Result findTopsList() {
		return Result.success(dictionaryService.findTopList());
	}
	
}
