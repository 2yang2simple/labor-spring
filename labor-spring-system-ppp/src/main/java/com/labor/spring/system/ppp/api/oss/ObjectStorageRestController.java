package com.labor.spring.system.ppp.api.oss;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.FileUtil;
import com.labor.common.util.ImageUtil;
import com.labor.common.util.QRCodeUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.system.ppp.ApplicationProperties;
import com.labor.spring.system.ppp.api.gallery.GalleryServiceIntf;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.gallery.GalleryImage;
import com.labor.spring.system.ppp.entity.oss.Attachment;
import com.labor.spring.system.ppp.entity.oss.ObjectBody;
import com.labor.spring.system.ppp.entity.oss.ObjectHeader;
import com.labor.spring.util.WebUtil;
@RestController
@RequestMapping("/oss/objects")
public class ObjectStorageRestController {
	
	@Autowired
	private ApplicationProperties properties;
	
	@Autowired
	private ObjectStorageServiceIntf objectStorageService;
	@Autowired
	private GalleryServiceIntf galleryService;


	//create a object with entity;
	@RequiresPermissions("oss:create")
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
		return Result.success(objectStorageService.create(file));
	}
	//create a image with entity;
	@RequiresPermissions("oss:create")
	@RequestMapping(value = {"/images"}, method = RequestMethod.POST)
	public Result createImage(
					@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
	    // image will be compressed in service;
		return Result.success(objectStorageService.createImage(file));
	}

	@RequiresPermissions("oss:create")
	@RequestMapping(value = {"/galleries/{id}/images"}, method = RequestMethod.POST)
	public Result createGalleryImage(
					@PathVariable(value="id") Integer id, 
					@RequestParam(value="file", required=false) MultipartFile file) {
		GalleryImage ret = null;
		Gallery ga = galleryService.findById(id).orElse(null);
		if (ga!=null) {
			ret = galleryService.createImage(ga.getId(),file,1);
		}
		return Result.success(ret);
	}
	
	@RequestMapping(value = { "/filename-{filename}" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findByFilename(
					@PathVariable(value="filename") String filename) {
		return objectStorageService.findEntityByFilename(filename, null);
	}
	
	@RequestMapping(value = { "/images/{filename}/origin" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findImageOriginByFilename(
					@PathVariable(value="filename") String filename) {
		return objectStorageService.findImageEntityByFileName(filename,false,true,Double.valueOf(1),null,null);
//		return objectStorageService.findEntityByFilename(filename, ImageUtil.IMAGE_COMPRESSED_SUFFIX);
	}
	
	@RequestMapping(value = { "/images/{filename}" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findImageByFilename(
					@PathVariable(value="filename") String filename) {
		return objectStorageService.findImageEntityByFileName(filename,true,true,Double.valueOf(1),null,null);//compressed to avoid large file.
//		return objectStorageService.findEntityByFilename(filename, ImageUtil.IMAGE_COMPRESSED_SUFFIX);
	}

	
	@RequestMapping(value = {"/qr-code"}, method = RequestMethod.GET)
	public ResponseEntity<byte[]> qrCode(
						@RequestParam(value = "content", required=false) String content) {
		ResponseEntity<byte[]> ret = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=qr");
		HttpStatus statusCode = HttpStatus.OK;
		ret = new ResponseEntity<byte[]>(QRCodeUtil.createImageBytes(content, 200, 200), headers, statusCode);
		return ret;
	}

}
