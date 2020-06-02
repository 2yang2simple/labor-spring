package com.labor.spring;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.labor.common.util.FileUtil;
import com.labor.common.util.ImageUtil;
import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseController;
import com.labor.spring.core.controller.AbstractCoreController;
import com.labor.spring.system.oss.AliyunOssUtil;
import com.labor.spring.system.oss.ApplicationProperties;
import com.labor.spring.system.oss.controller.vo.ObjectStorageType;
import com.labor.spring.system.oss.controller.vo.ObjectStorageUtil;
import com.labor.spring.system.oss.controller.vo.ObjectStorageVO;
import com.labor.spring.util.WebUtil;

@Controller
public class HtmlController extends AbstractCoreController{
	
	@Autowired
	private ApplicationProperties properties;
		
	@ResponseBody
	@RequestMapping(value = {"/{query}"}, method = RequestMethod.GET)
	public ModelAndView find(
					@PathVariable(value="query") String query) {
		ModelAndView mv = null;
		ObjectStorageVO os = buildObjectStorage(query);
		if (os == null || os.getOsType() == null) {
			mv = new ModelAndView("/error/404");
			return mv;
		}
		String contentType = "";
		String contentDisposition = "";

		switch (os.getOsType()) {
			case NAS_FILE:
				os.setBytes(getNasFileBytes(ObjectStorageType.NAS_FILE.getPath(),os.getPath(),os.getName(),null));	
				contentType = "multipart/form-data";
				contentDisposition = "attachment;fileName="+System.currentTimeMillis() + "." + os.getType();
				if (os == null || os.getBytes()==null) {
					return null;
				}
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
				mv = new ModelAndView("redirect:"+AliyunOssUtil.ossUrl+"/"+ObjectStorageUtil.url2fullpath(query));
				return mv;
			case ALIYUN_OSS_IMAGE:
				mv = new ModelAndView("redirect:"+AliyunOssUtil.ossUrl+"/"+ObjectStorageUtil.url2fullpath(query)+AliyunOssUtil.process_style_watermark);
				return mv;
			default:
				break;
		}
		
//		if (fetchBytes(osv) == null) {
//			return "404 not found";
//		};
		return mv;
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
	
	private byte[] getNasFileBytes(String ossPath, String path, String name, String ext) {
		byte[] ret = null;
		ossPath = properties.OBJECTSTORAGE_DIR + File.separator + ossPath;
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
