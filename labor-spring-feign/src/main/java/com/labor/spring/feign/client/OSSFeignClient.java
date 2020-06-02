package com.labor.spring.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.bean.Result;
import com.labor.spring.feign.FeignMultipartSupportConfig;

/***
 * get url from application.porperties
 * feign.url.oss=http://localhost:8080/oss
 */
@FeignClient(name = "oss", url = "${feign.url.oss}", configuration = FeignMultipartSupportConfig.class)
public interface OSSFeignClient {
	
	@RequestMapping(value = {"/rest/foreign/files"}, method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result createFile(@RequestPart("file") MultipartFile file);

	@RequestMapping(value = {"/rest/foreign/images"}, method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result createImage(@RequestPart("file") MultipartFile file);

//	@RequestMapping(value = {"/rest/foreign/files/{filename}"}, method = RequestMethod.GET)
//	public byte[] findByFilename(@PathVariable(value="filename") String filename);
//	
//	@RequestMapping(value = {"/rest/foreign/images/{filename}/origin"}, method = RequestMethod.GET)
//	public byte[] findImageOriginByFilename(@PathVariable(value="filename") String filename);
//	
//	@RequestMapping(value = {"/rest/foreign/images/{filename}"}, method = RequestMethod.GET)
//	public byte[] findImageByFilename(@PathVariable(value="filename") String filename);
	
	
}
