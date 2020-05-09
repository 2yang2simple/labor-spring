package com.labor.spring.system.ppp.api.gallery;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.labor.common.constants.CommonConstants;
import com.labor.common.util.FileUtil;
import com.labor.common.util.ImageUtil;
import com.labor.common.util.StringUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.feign.auth.AuthLoginService;
import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.api.oss.FileObject;
import com.labor.spring.system.ppp.api.oss.ObjectStorageServiceIntf;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.gallery.GalleryImage;
import com.labor.spring.system.ppp.entity.oss.ObjectHeader;
import com.labor.spring.system.ppp.util.ApplicationProperties;
import com.labor.spring.util.WebUtil;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

@RestController
@RequestMapping("/rest/galleries")
public class GalleryRestController {
	
	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private AuthLoginService authLoginService;
	@Autowired
	private GalleryServiceIntf galleryService;
	@Autowired
	private ObjectStorageServiceIntf objectStorageService;
	
	@RequiresPermissions("gallery:create")
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestBody @Valid Gallery gallery, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		return Result.success(galleryService.create(gallery));
	}
	@RequiresPermissions("gallery:create")
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Result update(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid Gallery gallery, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		return Result.success(galleryService.update(id,gallery,true));
	}
	
	@ResponseBody
	@RequiresPermissions("gallery:edit")
	@RequestMapping(value = {"/{id}/images"}, method = RequestMethod.POST)
	public Result createImages(
					@PathVariable(value="id") Integer id, 
					@RequestParam(value="files", required=false) MultipartFile[] files,
					@RequestParam(value="startOrder", defaultValue="0") Integer startOrder) {
		Gallery ga = galleryService.findById(id).orElse(null);
		if (!authLoginService.isCurrentUserOrSuperUser(Integer.valueOf(ga.getCreatedBy()),null)) {
	    	return Result.failure(ResultCode.FAILURE_PERMISSION_NOACCESS, ResultCode.MSG_FAILURE_PERMISSION_NOACCESS);
	    }
		startOrder = (startOrder==null)?0:startOrder;
		if (files!=null&&files.length>0) {
			int size = files.length;
			for (int i=0;i<size;i++) {
				startOrder++;
				galleryService.createImage(id,files[i],startOrder);
				LogManager.getLogger().debug("create image end:", i);
			}
		}
		return Result.success(ga);
	}
//	
//	@ResponseBody
//	@RequiresPermissions("gallery:edit")
//	@RequestMapping(value = {"/{id}/image"}, method = RequestMethod.POST)
//	public Result createImage(
//					@PathVariable(value="id") Integer id, 
//					@RequestParam(value="file", required=false) MultipartFile file,
//					@RequestParam(value="startOrder", defaultValue="0") Integer startOrder) {
//		Gallery ga = galleryService.findById(id).orElse(null);
//		if (!WebUtil.isLoginBySuperOrIsCurrentUser(Integer.valueOf(ga.getCreatedBy()))) {
//	    	return Result.failure(ResultCode.FAILURE_PERMISSION_NOACCESS, ResultCode.MSG_FAILURE_PERMISSION_NOACCESS);
//	    }
//		startOrder = (startOrder==null)?0:startOrder;
//		startOrder++;
//		return Result.success(galleryService.createImage(id,file,startOrder));
//	}
	
	@RequiresPermissions("gallery:edit")
	@RequestMapping(value = {"/{id}/images/{galleryImageId}"}, method = RequestMethod.PUT)
	public Result updateImage(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="galleryImageId") Integer galleryImageId, 
					@RequestBody @Valid GalleryImage galleryImage, 
					BindingResult bindingResult) {
		GalleryImage ret = null;
		if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		Gallery ga = galleryService.findById(id).orElse(null);
		if (!authLoginService.isCurrentUserOrSuperUser(Integer.valueOf(ga.getCreatedBy()),null)) {
	    	return Result.failure(ResultCode.FAILURE_PERMISSION_NOACCESS, ResultCode.MSG_FAILURE_PERMISSION_NOACCESS);
	    }
		galleryImage.setGaId(id);
	    galleryImage.setId(galleryImageId);
		ret = galleryService.updateImage(id, galleryImageId, galleryImage,true);
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/name-page-list"}, method = RequestMethod.GET)
	public Result findListByNameLike(
					@RequestParam(value="name", required=false) String name,
					@RequestParam(value="status", required=false) String status,
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, 30, sort);
		name = StringUtil.safeToString(name);
		status = StringUtil.safeToString(status);
		Page<Gallery> ret = galleryService.findListByNameLikeAndStatus(name,status,pageable);
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/uuid-{uuid}" }, method = RequestMethod.GET)
	public Result findByUuid(
					@PathVariable(value="uuid") String uuid) {
		return Result.success(galleryService.findByUuid(uuid).orElse(null));
	}

	@RequestMapping(value = { "/{uuid}/images" }, method = RequestMethod.GET)
	public Result findImageListByUuid(
					@PathVariable(value="uuid") String uuid,
					@RequestParam(value="status", required=false) String status,
					@RequestParam(value="capion", required=false) String capion) {
		List<GalleryImage> ret = galleryService.findImageListByUuidAndStatusAndCaptionLike(uuid,status,capion);
		if(ret!=null&&ret.size()>0) {
			GalleryImage gi = ret.get(0);
			if (authLoginService.isCurrentUserOrSuperUser(Integer.valueOf(gi.getCreatedBy()),null)) {
				
			}
		}
		return Result.success(ret);
	}
	
	@RequestMapping(value = { "/{uuid}/images/page-list" }, method = RequestMethod.GET)
	public Result findImageListByUuid(
					@PathVariable(value="uuid") String uuid,
					@RequestParam(value="status", required=false) String status,
					@RequestParam(value="capion", required=false) String capion,
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.ASC, sortby);
		Pageable pageable = PageRequest.of(page, 15, sort);
		Page<GalleryImage> ret = galleryService.findImageListByUuidAndStatusAndCaptionLike(uuid,status,capion,pageable);
		if(ret!=null&&ret.getContent()!=null&&ret.getContent().size()>0) {
			GalleryImage gi = ret.getContent().get(0);
			if (authLoginService.isCurrentUserOrSuperUser(Integer.valueOf(gi.getCreatedBy()),null)) {
				
			}
		}
		return Result.success(ret);
	}
	@RequestMapping(value = { "/images/{uuid}" }, method = RequestMethod.GET)
	public Result findImageByUuid(
					@PathVariable(value="uuid") String uuid) {
		GalleryImage ret = galleryService.findImageByImageUuid(uuid).orElse(null);
		return Result.success(ret);
	}
	
	@RequestMapping(value = { "/{uuid}/cover" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findCoverByUuid(
					@PathVariable(value="uuid") String uuid) {
		String fileName = "";
		GalleryImage gi = galleryService.findFistImageByUuid(uuid).orElse(null);
		if (gi!=null) {		
			fileName = gi.getFileName();
		}
		return objectStorageService.findImageEntityByFileName(fileName,true,true,null,null,null);
	}
	
	@RequestMapping(value = { "/images/{uuid}/entity" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findImageEntityOriginByUuid(
					@PathVariable(value="uuid") String uuid) {
		String fileName = "";
		GalleryImage gi = galleryService.findImageByImageUuid(uuid).orElse(null);
		if (gi!=null) {
			fileName = gi.getFileName();
		}
		return objectStorageService.findImageEntityByFileName(fileName,false,true,Double.valueOf(1),null,null);
	}
	
	@RequestMapping(value = { "/images/{uuid}/entity/thumbnail" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findImageEntityThumbnailByUuid(
					@RequestParam(value="accuracy", required=false) Double accuracy,
					@RequestParam(value="height", required=false) Integer height,
					@RequestParam(value="width", required=false) Integer width,
					@PathVariable(value="uuid") String uuid) {
		String fileName = "";
		GalleryImage gi = galleryService.findImageByImageUuid(uuid).orElse(null);
		if (gi!=null) {
			fileName = gi.getFileName();
		}
		return objectStorageService.findImageEntityByFileName(fileName,true,true,accuracy,height,width);
	}
	
//	private ResponseEntity<byte[]> findImageEntityByFileName(String fileName, boolean getThumbnail, Double accuracy,Integer height, Integer width){
//		ResponseEntity<byte[]> ret = null;
//		byte[] fileBody = null;
//		String attachment = "";
//
//		FileObject fo = null;
//		fo = objectStorageService.findFileObjectByFileName(fileName, ImageUtil.IMAGE_COMPRESSED_SUFFIX);
//		if (fo != null) {
//			if (getThumbnail) {
//				fileBody = ImageUtil.resizeThumbnails(fo.getBytes(),fo.getType(),accuracy, height, width,
//								WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_WATERMARK_FILE);
//			} else {
//				fileBody = fo.getBytes();
//			}
//			attachment = fileName + "." + fo.getType();
//		}
//		
//		if (fileBody == null) {
//			// if not exist or error, return 404.gif;
//			fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_NOT_EXIST);
//			attachment = properties.IMG_NOT_EXIST;
//		} 
//		if (fileBody == null) {
//			return ret;
//		}
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Disposition", "attachment;filename=" + attachment);
//		HttpStatus statusCode = HttpStatus.OK;
//		ret = new ResponseEntity<byte[]>(fileBody, headers, statusCode);
//		return ret;
//	}
	
//	private byte[] getThumbnailBytes(byte[] inputBytes, String fileType, Double accuracy, Integer height, Integer width) {
//		byte[] ret = null;
//		BufferedImage bufferedImage = FileUtil.bytes2BufferedImage(inputBytes);
//		if (bufferedImage != null) {
//			// thumb size will be below 300k
//			
//			LogManager.getLogger().error("accuracy"+accuracy+"|height"+height+"|width"+width);
//			ByteArrayOutputStream os = null;
//			try{
//				LogManager.getLogger().debug("xxxxxxxxwatermark"+WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_WATERMARK_FILE);
//				os = new ByteArrayOutputStream(); 
//				Builder<BufferedImage> builder = Thumbnails.of(bufferedImage);
//				
//				if (height!=null&&height>0) {
//					builder.height(height);
//				} else if (width!=null&&width>0) {
//					builder.width(width);
//				} else if (accuracy!=null&&accuracy>0) {
//					builder.scale(accuracy);
//					builder.outputQuality(accuracy);
//				} else {
//					long size = inputBytes.length / 1024;
//					if (size < 100) {
//						accuracy = 0.5;
//					} else if (size < 900) {
//						accuracy = 0.3;
//					} else if (size < 2047) {
//						accuracy = 0.2;
//					} else if (size < 3275) {
//						accuracy = 0.1;
//					} else {
//						accuracy = 0.1;
//					}
//					builder.scale(accuracy);
//					builder.outputQuality(accuracy);
//				}
//				builder.watermark(Positions.CENTER, ImageIO.read(
//									new File(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_WATERMARK_FILE)), 0.1f);
//				builder.outputFormat(fileType);
//				builder.toOutputStream(os);
//							
//				ret = os.toByteArray();
//				LogManager.getLogger().debug("xxxxxxxxinputBytes"+inputBytes.length);
//				LogManager.getLogger().debug("xxxxxxxoutputBytes"+ret.length);
//				
//			} catch(Exception ioe) {
//				LogManager.getLogger().error("", ioe);
//			} finally {
//	            try {
//	                if (null != os) {
//	                	os.close();
//	                }
//	            } catch (IOException ex) {
//	            	LogManager.getLogger().error("", ex);
//	            } 
//			}		
//		}
//		return ret;
//	}
}
