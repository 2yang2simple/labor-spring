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
	
	@RequestMapping(value = {"/files/{filename}"}, method = RequestMethod.GET)
	public void findFileByFilename(
					@PathVariable(value="filename") String filename) {
		OutputStream out = null;
		ObjectStorage os = null;
		try {
			HttpServletResponse response = WebUtil.getResponse();
			out = response.getOutputStream();
			os =objectStorageService.findObjectStorageByFileName(filename, null);
			if (os == null) {
				return;
			}
			response.setHeader("Content-Disposition", "attachment;fileName="+os.getName() + "." + os.getType());   
			response.setContentType("multipart/form-data");   
			out.write(os.getBytes());
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
		
//		byte[] fileBody = objectStorageService.findBytesByFileName(filename, null);
//		String attachment = "";
//		if (fileBody == null) {
//			// if not exist or error, return 404.gif;
//			fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
//			attachment = properties.IMG_404_FILE;
//		}
//		return createResponseEntity(fileBody, attachment);
	}
	
	@RequestMapping(value = {"/images/{filename}/origin"}, method = RequestMethod.GET)
	public void findImageOriginByFilename(
					@PathVariable(value="filename") String filename) {

		OutputStream out = null;
		byte[] fileBody = null;
		try {
			HttpServletResponse response = WebUtil.getResponse();
			response.setContentType("image/jpeg");
			out = response.getOutputStream();
			fileBody =objectStorageService.findBytesByFileName(filename,false,true,Double.valueOf(1),null,null);
			if (fileBody == null) {
				// if not exist or error, return 404.gif;
				fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
			}
			out.write(fileBody);
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
		
//		byte[] fileBody = objectStorageService.findBytesByFileName(filename,false,true,Double.valueOf(1),null,null);
//		String attachment = "";
//		if (fileBody == null) {
//			// if not exist or error, return 404.gif;
//			fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
//			attachment = properties.IMG_404_FILE;
//		}
//		return createResponseEntity(fileBody, attachment);

	}
	
	@RequestMapping(value = {"/images/{filename}"}, method = RequestMethod.GET)
	public void findImageByFilename(
					@PathVariable(value="filename") String filename) {
		OutputStream out = null;
		byte[] fileBody = null;
		try {
			HttpServletResponse response = WebUtil.getResponse();
			response.setContentType("image/jpeg");
			out = response.getOutputStream();
			fileBody = objectStorageService.findBytesByFileName(filename,true,true,Double.valueOf(1),null,null);
			if (fileBody == null) {
				// if not exist or error, return 404.gif;
				fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
			}
			out.write(fileBody);
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

//		byte[] fileBody = objectStorageService.findBytesByFileName(filename,true,true,Double.valueOf(1),null,null);
//		String attachment = "";
//		if (fileBody == null) {
//			// if not exist or error, return 404.gif;
//			fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
//			attachment = properties.IMG_404_FILE;
//		}
//		return createResponseEntity(fileBody, attachment);
		
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
