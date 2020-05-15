package com.labor.spring.system.oss.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.labor.common.util.FileUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.system.oss.util.ApplicationProperties;
import com.labor.spring.util.WebUtil;


@RestController
@RequestMapping("")
public class ObjectStorageRestController {
	
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
	
	@RequestMapping(value = {"/files/{query}"}, method = RequestMethod.GET)
	public void findFile(
					@PathVariable(value="query") String query) {
		ObjectStorage os = objectStorageService.findObjectStorage(query, null);
		if (os == null) {
			return;
		}
		String contentType = "multipart/form-data";
		String contentDisposition = "attachment;fileName="+System.currentTimeMillis() + "." + os.getType();
		writeBytes2Response(os.getBytes(),contentType,contentDisposition);
	}
	
	@RequestMapping(value = {"/images/{query}/origin"}, method = RequestMethod.GET)
	public void findImageOrigin(
					@PathVariable(value="query") String query) {
		ObjectStorage os = objectStorageService.findObjectStorage(query,false,true,Double.valueOf(1),null,null);
		if (os == null) {
			// if not exist or error, return 404.gif;
			byte[] fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
			os = new ObjectStorage();
			os.setBytes(fileBody);
			os.setName(properties.IMG_404_FILE);
			os.setType("png");
		}
		String contentType = "image/"+os.getType();
		writeBytes2Response(os.getBytes(),contentType,null);
	}
	
	@RequestMapping(value = {"/images/{query}"}, method = RequestMethod.GET)
	public void findImage(
					@PathVariable(value="query") String query) {
		ObjectStorage os = objectStorageService.findObjectStorage(query,true,true,Double.valueOf(1),null,null);
		if (os == null) {
			// if not exist or error, return 404.gif;
			byte[] fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
			os = new ObjectStorage();
			os.setBytes(fileBody);
			os.setName(properties.IMG_404_FILE);
			os.setType("png");
		}
		String contentType = "image/"+os.getType();
		writeBytes2Response(os.getBytes(),contentType,null);
	}
	
	private void writeBytes2Response(byte[] bytes, String contentType, String contentDisposition) {
		OutputStream out = null;
		try {
			HttpServletResponse response = WebUtil.getResponse();
			if (!StringUtil.isEmpty(contentType)) {
				response.setContentType(contentType);
			}
			if (!StringUtil.isEmpty(contentDisposition)) {
				response.setHeader("Content-Disposition", contentDisposition);   
			}
			out = response.getOutputStream();
			out.write(bytes);
			out.flush();
		} catch (IOException ioe){
			LogManager.getLogger().error(ioe);
		} finally {
			if (out!=null) {
				try {
					out.close();
				} catch (IOException ioe){
					LogManager.getLogger().error(ioe);
				}
			}
		}
	}
	
//	private ResponseEntity<byte[]> createResponseEntity(byte[] fileBody, String fileName){
//		ResponseEntity<byte[]> ret = null;
//		if (fileBody == null) {
//			return ret;
//		}
//		if (StringUtil.isEmpty(fileName)) {
//			fileName = TokenUtil.generateUNum();
//		}
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Disposition", "attachment;filename=" + fileName);
//		HttpStatus statusCode = HttpStatus.OK;
//		ret = new ResponseEntity<byte[]>(fileBody, headers, statusCode);
//		return ret;
//		
//	}
	
}
