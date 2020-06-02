package com.labor.spring.feign.api.oss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.feign.client.OSSFeignClient;


@RestController
@RequestMapping("/rest/feign/oss")
public class OSSRestController {
	
	@Autowired
	private OSSFeignClient ossFeignClient;

	//create a object with entity;
	@RequestMapping(value = {"/files"}, method = RequestMethod.POST)
	public Result createFile(
					@RequestPart("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
	    Result ret = null;
	    ret = ossFeignClient.createFile(file);
		return ret;
	}
	
	//create a image with entity;
	@RequestMapping(value = {"/images"}, method = RequestMethod.POST)
	public Result createImage(
					@RequestPart("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
	    Result ret = null;
	    ret = ossFeignClient.createImage(file);
		return ret;
	}
	
	
	
}
