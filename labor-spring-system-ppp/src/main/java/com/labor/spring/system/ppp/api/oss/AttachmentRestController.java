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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.FileUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.system.ppp.entity.oss.Attachment;
import com.labor.spring.system.ppp.util.ApplicationProperties;

import com.labor.spring.util.WebUtil;
@RestController
@RequestMapping("/attachments")
public class AttachmentRestController {
	
	@Autowired
	private ApplicationProperties properties;
	
	@Autowired
	private AttachmentServiceIntf attachmentService;
	
	//create a object without entity
	@RequestMapping(value = {"/info"}, method = RequestMethod.POST)
	public Result createInfo(
					@RequestBody @Valid Attachment attachment) {
		Attachment ret;
		if (attachment==null||StringUtil.isEmpty(attachment.getUrl())) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, "the url is empty;");
		}
		String fileName = TokenUtil.generateUUID();
		attachment.setFileName(fileName);
	    ret = attachmentService.create(attachment);
		return Result.success(ret);
	}
	
	//find single by identify code
	@RequestMapping(value = {"/info/filename-{filename}"}, method = RequestMethod.GET)
	public Result findInfoByFilename(
					@PathVariable(value="filename") String filename) {
		Optional<Attachment> oa = attachmentService.findByFilename(filename);
		return Result.success(oa.get());

	}
	
	//create a object with entity;
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestParam("file") MultipartFile file) {
		Attachment ret;
	    if (file.isEmpty()) {
	    	return Result.failure(ResultCode.FAILURE_PARAM_NULL, ResultCode.MSG_FAILURE_PARAM_NULL);
		}
	    Attachment attachment = saveFile(file);
	    ret = attachmentService.create(attachment);
		return Result.success(ret);
	}
	
	@RequestMapping(value = { "/filename-{filename}" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findByFilename(
					@PathVariable(value="filename") String filename) {
		ResponseEntity<byte[]> ret = null;
		byte[] fileBody = null;
		fileBody = getFileStreamByFilename(filename);
		if (fileBody == null) {
			// if not exist or error, return 404.gif;
			fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
		} 
		if (fileBody == null) {
			return ret;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=" + filename);
		HttpStatus statusCode = HttpStatus.OK;
		ret = new ResponseEntity<byte[]>(fileBody, headers, statusCode);
		return ret;
	}

	
	//find file saved in attachment table, and return file stream 
	private byte[] getFileStreamByFilename(String fileName) {
		byte[] ret = null;
		Optional<Attachment> oa = attachmentService.findByFilename(fileName);
		if (oa.isPresent()) {
			Attachment attachment = oa.get();
			ret = FileUtil.file2Bytes(getRealFilePath(attachment));
		}
		return ret;
	}
	

	//return real file path;
	private String getRealFilePath(Attachment attachment) {
		return properties.ATTACHMENTS_DIR + File.separator 
				+ attachment.getFilePath() + File.separator 
				+ attachment.getFileName() + "." 
				+ attachment.getFileType();
	}
		
	//file storage
	private Attachment saveFile(MultipartFile file) {
		Attachment ret = null;
		if (file.isEmpty()) {
	    	return ret;// file not exist;
		}
		String name = file.getOriginalFilename();
		Long fileSize = file.getSize();
		String filePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String fileName = TokenUtil.generateUUID();
		String fileType = FileUtil.getFileType(name);
		
		String url = properties.ATTACHMENTS_DIR + File.separator + filePath + File.separator + fileName + "." + fileType;
		File desFile = new File(url);
		
		if (!desFile.getParentFile().exists()) {
			desFile.getParentFile().mkdirs();
		}
		try {

			// Then save the pic file to dm, after transfer, the MultipartFile will be none;
			file.transferTo(desFile);
			Attachment attachment = new Attachment();
			// save the pic data to db;
			attachment.setName(name);
			attachment.setFilePath(filePath);
			attachment.setFileName(fileName);
			attachment.setFileType(fileType);
			attachment.setFileSize(fileSize);
			//url set null when store file in internal server; set real url when store in external server, like easyimg etc.
			attachment.setUrl("");
			ret = attachment;

		} catch (IOException ioe) {
			LogManager.getLogger().error("", ioe);
		} 
		return ret;
	}

}
