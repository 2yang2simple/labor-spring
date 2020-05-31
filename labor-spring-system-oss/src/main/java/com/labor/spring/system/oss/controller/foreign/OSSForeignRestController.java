package com.labor.spring.system.oss.controller.foreign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.system.oss.controller.vo.ObjectStorageType;
import com.labor.spring.system.oss.service.ObjectStorageService;
import com.labor.spring.system.oss.util.ApplicationProperties;


@RestController
@RequestMapping("/rest/foreign")
public class OSSForeignRestController {

	@Autowired
	@Qualifier(value = "ObjectStorageServiceImpl")
	private ObjectStorageService objectStorageService;


	//create a object with entity;
	@RequestMapping(value = {"/files"}, method = RequestMethod.POST)
	public Result createFile(
					@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
		return Result.success(objectStorageService.createFile(ObjectStorageType.NAS_FILE,file));
	}
	
	//create a image with entity;
	@RequestMapping(value = {"/images"}, method = RequestMethod.POST)
	public Result createImage(
					@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
	    // image will be compressed in service;
		return Result.success(objectStorageService.createImage(ObjectStorageType.NAS_IMAGE,file));
	}

	@RequestMapping(value = { "/test" }, method = RequestMethod.GET)
	public Result test() {
		return Result.success("test");
	}
}
