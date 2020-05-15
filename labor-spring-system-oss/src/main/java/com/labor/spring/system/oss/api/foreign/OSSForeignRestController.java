package com.labor.spring.system.oss.api.foreign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.system.oss.api.ObjectStorage;
import com.labor.spring.system.oss.api.ObjectStorageServiceIntf;
import com.labor.spring.system.oss.util.ApplicationProperties;


@RestController
@RequestMapping("/rest/foreign")
public class OSSForeignRestController {
	
	@Autowired
	private ApplicationProperties properties;
	
	@Autowired
	private ObjectStorageServiceIntf objectStorageService;


	//create a object with entity;
	@RequestMapping(value = {"/files"}, method = RequestMethod.POST)
	public Result createFile(
					@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
		return Result.success(objectStorageService.create(file));
	}
	
	//create a image with entity;
	@RequestMapping(value = {"/images"}, method = RequestMethod.POST)
	public Result createImage(
					@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
	    // image will be compressed in service;
		return Result.success(objectStorageService.createImage(file));
	}

	@RequestMapping(value = { "/files/{filename}" }, method = RequestMethod.GET)
	public byte[] findFileByFilename(@PathVariable(value = "filename") String filename) {
		ObjectStorage ret = null;
		ret = objectStorageService.findObjectStorageByFileName(filename, null);
		if (ret == null) {
			return null;
		}
		return ret.getBytes();
	}
	
	@RequestMapping(value = { "/images/{filename}/origin" }, method = RequestMethod.GET)
	public byte[] findImageOriginByFilename(@PathVariable(value = "filename") String filename) {
		ObjectStorage ret = null;
		ret = objectStorageService.findObjectStorageByFileName(filename, false, true, Double.valueOf(1), null, null);
		if (ret == null) {
			return null;
		}
		return ret.getBytes();
	}

	@RequestMapping(value = { "/images/{filename}" }, method = RequestMethod.GET)
	public byte[] findImageByFilename(@PathVariable(value = "filename") String filename) {
		ObjectStorage ret = null;
		// compressed to avoid large file.
		ret = objectStorageService.findObjectStorageByFileName(filename, true, true, Double.valueOf(1), null, null);
		if (ret == null) {
			return null;
		}
		return ret.getBytes();

	}
	
}
