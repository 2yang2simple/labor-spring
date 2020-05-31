package com.labor.spring.system.oss.controller.local;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.labor.common.util.ImageUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.system.oss.controller.vo.ObjectStorageType;
import com.labor.spring.system.oss.controller.vo.ObjectStorageVO;
import com.labor.spring.system.oss.service.ObjectStorageService;
import com.labor.spring.system.oss.util.ApplicationProperties;
import com.labor.spring.system.oss.util.ObjectStorageUtil;
import com.labor.spring.util.WebUtil;


@RestController
@RequestMapping("/rest")
public class ObjectStorageRestController {
	
	@Autowired
	private ApplicationProperties properties;
	
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
		return Result.success(objectStorageService.createFile(ObjectStorageType.ALIYUN_OSS_FILE,file));
	}
	
	//create a image with entity;
	@RequestMapping(value = {"/images"}, method = RequestMethod.POST)
	public Result createImage(
					@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
	    // image will be compressed in service;
		return Result.success(objectStorageService.createImage(ObjectStorageType.ALIYUN_OSS_IMAGE,file));
	}
	
	
	@RequestMapping(value = {"/{query}"}, method = RequestMethod.GET)
	public void find(
					@PathVariable(value="query") String query) {
		fetchBytes(buildObjectStorage(query));
	}
	
	@RequestMapping(value = {"/files/{query}"}, method = RequestMethod.GET)
	public void findFile(
					@PathVariable(value="query") String query) {
		ObjectStorageVO os = buildObjectStorage(query);
		if (os == null) {
			return;
		}
		os.setBytes(getNasFileBytes(ObjectStorageType.NAS_FILE.getPath(),os.getPath(),os.getName(),os.getType()));	
		String contentType = "multipart/form-data";
		String contentDisposition = "attachment;fileName="+System.currentTimeMillis() + "." + os.getType();
		writeBytes2Response(os.getBytes(),contentType,contentDisposition);
	}
	
	@RequestMapping(value = {"/images/{query}"}, method = RequestMethod.GET)
	public void findImage(
					@PathVariable(value="query") String query) {
		ObjectStorageVO os = buildObjectStorage(query);
		if (os!=null) {
			byte[] imageBytes = getNasFileBytes(ObjectStorageType.NAS_IMAGE.getPath(),os.getPath(), os.getName(), ImageUtil.IMAGE_COMPRESSED_SUFFIX);
			if (imageBytes!=null) {
				imageBytes = resizeImage(imageBytes,os.getType(),Double.valueOf(1),null,null);
				os.setBytes(imageBytes);
			}
		}
		
		if (os == null || os.getBytes()==null) {
			os = buildImageNotExistObjectStorage();
		}
		
		String contentType = "image/"+os.getType();
		writeBytes2Response(os.getBytes(),contentType,null);
	}
	
	@RequestMapping(value = {"/images/{query}/origin"}, method = RequestMethod.GET)
	public void findImageOrigin(
					@PathVariable(value="query") String query) {
		ObjectStorageVO os = buildObjectStorage(query);
		if (os!=null) {
			byte[] imageBytes = getNasFileBytes(ObjectStorageType.NAS_IMAGE.getPath(),os.getPath(), os.getName(), null);
			if (imageBytes!=null) {
				imageBytes = resizeImage(imageBytes,os.getType(),Double.valueOf(1),null,null);
				os.setBytes(imageBytes);
			}
		}
		
		if (os == null || os.getBytes()==null) {
			os = buildImageNotExistObjectStorage();
		}
		
		String contentType = "image/"+os.getType();
		writeBytes2Response(os.getBytes(),contentType,null);
	}
	
	
	
	/**
	 * build a objectsotrage with null bytes
	 * @param query
	 * @return
	 */
	private ObjectStorageVO buildObjectStorage(String query) {
		ObjectStorageVO ret = null;
		//\20191211\95f444173ff249589a9051ef33e5c710.png
		
		//get info from url
		ret = ObjectStorageUtil.url2objectstorage(query);
		return ret;
	}
	
	/**
	 * fetch bytes and set to the object;
	 * @param os
	 * @return
	 */
	private ObjectStorageVO fetchBytes(ObjectStorageVO os) {
		if (os == null || os.getOsType() == null) {
			return os;
		}
		String contentType = "";
		String contentDisposition = "";
		
		switch (os.getOsType()) {
			case NAS_FILE:
				os.setBytes(getNasFileBytes(ObjectStorageType.NAS_FILE.getPath(),os.getPath(),os.getName(),null));	
				contentType = "multipart/form-data";
				contentDisposition = "attachment;fileName="+System.currentTimeMillis() + "." + os.getType();
				writeBytes2Response(os.getBytes(),contentType,contentDisposition);
				
				break;
			case NAS_IMAGE:
				byte[] imageBytes = getNasFileBytes(ObjectStorageType.NAS_IMAGE.getPath(),os.getPath(), os.getName(), ImageUtil.IMAGE_COMPRESSED_SUFFIX);
				if (imageBytes!=null) {
					imageBytes = resizeImage(imageBytes,os.getType(),Double.valueOf(1),null,null);
					os.setBytes(imageBytes);
				}
				if (os == null || os.getBytes()==null) {
					os = buildImageNotExistObjectStorage();
				}
				contentType = "image/"+os.getType();
				writeBytes2Response(os.getBytes(),contentType,null);
				
				break;
			case ALIYUN_OSS_FILE:
				break;
			case ALIYUN_OSS_IMAGE:
				break;
			default:
				break;
		}
		
		return os;
	}
	
	private byte[] getNasFileBytes(String ossPath, String path, String name, String ext) {
		byte[] ret = null;
		ossPath = properties.OBJECTSTORAGE_DIR + ossPath;
		File file = new File(ossPath + File.separator 
				+ path + File.separator 
				+ name
				+ (ext==null?"":ext));
		if (file.exists()) {
			LogManager.getLogger().debug("getBytes:"+ext);
		} else {
			file = new File(ossPath + File.separator 
							+ path + File.separator 
							+ name);
		}
		
		ret = FileUtil.file2Bytes(file);
		return ret;
	}
	
	// if not exist or error, return notexist.gif;
	private ObjectStorageVO buildImageNotExistObjectStorage() {
		ObjectStorageVO ret = new ObjectStorageVO();
		byte[] fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_FILE_NOTEXIST);
		if (fileBody!=null) {
			ret.setBytes(fileBody);
			ret.setName(properties.IMG_FILE_404);
			ret.setType("gif");
		}
		return ret;
	}
	private byte[] resizeImage(byte[] imageBytes, String imageType, Double accuracy,Integer height, Integer width) {
		byte[] ret = ImageUtil.resizeThumbnails(imageBytes, imageType, accuracy, height, width,
		WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_FILE_WATERMARK);
		return ret;
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
	
}
