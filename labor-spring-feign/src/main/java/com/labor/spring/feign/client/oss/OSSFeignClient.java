package com.labor.spring.feign.client.oss;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.bean.Result;

/***
 * get url from application.porperties
 * feign.url.oss=http://localhost:8080/oss
 */
@FeignClient(name = "oss", url = "${feign.url.oss}")
public interface OSSFeignClient {

	@RequestMapping(value = {"/rest/foreign/files"}, method = RequestMethod.POST)
	public Result createFile(@RequestParam("file") MultipartFile file);

	@RequestMapping(value = {"/rest/foreign/images"}, method = RequestMethod.POST)
	public Result createImage(@RequestParam("file") MultipartFile file);

	@RequestMapping(value = {"/rest/foreign/files/{filename}"}, method = RequestMethod.GET)
	public byte[] findByFilename(@PathVariable(value="filename") String filename);
	
	@RequestMapping(value = {"/rest/foreign/images/{filename}/origin"}, method = RequestMethod.GET)
	public byte[] findImageOriginByFilename(@PathVariable(value="filename") String filename);
	
	@RequestMapping(value = {"/rest/foreign/images/{filename}"}, method = RequestMethod.GET)
	public byte[] findImageByFilename(@PathVariable(value="filename") String filename);
	
	
}
