package com.labor.spring.system.oss.api;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.labor.common.util.FileUtil;
import com.labor.common.util.ImageUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.system.oss.entity.ObjectBody;
import com.labor.spring.system.oss.entity.ObjectHeader;
import com.labor.spring.system.oss.util.ApplicationProperties;
import com.labor.spring.system.oss.util.ObjectHeaderUtil;
import com.labor.spring.util.WebUtil;
@Service
public class ObjectStorageServiceImpl implements ObjectStorageServiceIntf{
//	@Autowired
//	private ApplicationProperties properties;
	@Autowired
	private ObjectHeaderRepository objectHeaderRepository;
	@Autowired
	private ObjectBodyRepository objectBodyRepository;


	@Override
	@Transactional
	public ObjectHeader create(String ossPath, MultipartFile file) {
		ObjectHeader ret = null;
		if (file.isEmpty()) {
	    	return ret;// file not exist;
		}
		String name = file.getOriginalFilename();		
		try {
			byte[] bytes = FileUtil.stream2Bytes(file.getInputStream());
			
			ret = create(ossPath,bytes,name);

		} catch (Exception e) {
			LogManager.getLogger().error("", e);
		} 
		return ret;
	}
	
	@Override
	@Transactional
	public ObjectHeader create(String ossPath, byte[] fileBytes, String fileOriginalName) {
		ObjectHeader ret = null;
		if (fileBytes == null) {
	    	return ret;// file not exist;
		}
		Long fileSize = Long.valueOf(fileBytes.length);

		/***** save the file , if user other osd service , do storage here; */
		ObjectBody objectBody = createBody(ossPath,fileBytes);
		if (objectBody!=null) {
			/***** save the file header */
			ObjectHeader objectHeader = new ObjectHeader();
			String fileName = TokenUtil.generateUUID();
			String url = ObjectHeaderUtil.args2url(objectBody.getPath(), objectBody.getMd5(), FileUtil.getFileType(fileOriginalName));
			// save the header to db;
			objectHeader.setName(fileOriginalName);
			objectHeader.setFilePath(objectBody.getPath());
			objectHeader.setFileName(fileName);
			objectHeader.setFileType(FileUtil.getFileType(fileOriginalName));
			objectHeader.setFileSize(fileSize);
			//url set null when store file in internal server; set real url when store in external server, like easyimg etc.
			objectHeader.setUrl(url);
			objectHeader.setObjectBodyId(objectBody.getId());
			objectHeader.setObjectBodyPath(objectBody.getPath());
			objectHeader.setObjectBodyMd5(objectBody.getMd5());
			ret = objectHeaderRepository.save(objectHeader);
		}
		return ret;		
	}
	
	@Override
	@Transactional
	public ObjectHeader createImage(String ossPath, MultipartFile file) {
		ObjectHeader ret = null;
		if (file.isEmpty()) {
	    	return ret;// file not exist;
		}
		String name = file.getOriginalFilename();		
		try {
			byte[] bytes = FileUtil.stream2Bytes(file.getInputStream());
			
			ret = createImage(ossPath,bytes,name);

		} catch (Exception e) {
			LogManager.getLogger().error("", e);
		} 
		return ret;
	}

	@Override
	@Transactional
	public ObjectHeader createImage(String ossPath, byte[] fileBytes, String fileOriginalName) {
		ObjectHeader ret = null;
		if (fileBytes == null) {
	    	return ret;// file not exist;
		}
		
		byte[] rotateBytes = null;
		byte[] compressedBytes = null;
		String fileType = FileUtil.getFileType(fileOriginalName);
	
		//rotate image
		rotateBytes = ImageUtil.processRotateInfo(fileBytes,fileType);
		if (rotateBytes!=null) {
			//save the rotate image as the main image
			ret = create(ossPath,rotateBytes,fileOriginalName);
		}
		
		//compress image to a entity
		if (rotateBytes!=null) {
			//image larger than 300k should be compressed
			compressedBytes = ImageUtil.resize(rotateBytes, fileType, ImageUtil.IMAGE_COMPRESSED_KBSIZE);
			if (ret!=null&&
					compressedBytes!=null&&
					compressedBytes.length<rotateBytes.length){
				//save only the compressed image body with ext for display
				createBody(ossPath, compressedBytes, ret.getObjectBodyPath(), ret.getObjectBodyMd5(), ImageUtil.IMAGE_COMPRESSED_SUFFIX);
			}

		}
		return ret;
	}

	

//	@Override
//	public Optional<ObjectHeader> findHeaderByFileName(String fileName) {
//		return objectHeaderRepository.findOneByFileName(fileName);
//	}
//	@Override
//	public Optional<ObjectBody> findBodyById(Integer id) {
//		return objectBodyRepository.findOneById(id);
//	}
//	@Override
//	public Optional<ObjectBody> findBodyByMd5(String md5) {
//		return objectBodyRepository.findOneByMd5(md5);
//	}
//	@Override
//	public Long countByExample(ObjectBody entity){
//		Example<ObjectBody> example =Example.of(entity);
//		return objectBodyRepository.count(example);
//	}
	
//	@Override
//	public ObjectStorage findObjectStorageByFileName(String filename) {
//		return findObjectStorageByFileName(filename, null);
//	}
//	
//	@Override
//	public ObjectStorage findObjectStorageByFileName(String fileName, String ext) {
//		ObjectStorage ret = null;
//		System.err.println(fileName);
//		fileName = FileUtil.getFileName(fileName);
//		Optional<ObjectHeader> oa = objectHeaderRepository.findOneByFileName(fileName);
//		if (oa.isPresent()) {
//			ObjectHeader oh = oa.get();
//			byte[] bytes = getBytes(oh.getObjectBodyPath(),oh.getObjectBodyMd5(),ext);
//			ret = new ObjectStorage();
//			ret.setBytes(bytes);
//			ret.setType(oh.getFileType());
//			ret.setName(oh.getFileName());
//			ret.setSize(oh.getFileSize());
//		}
//		return ret;
//	}
//
//
//	@Override
//	public ObjectStorage findObjectStorageByFileName(String fileName, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width){
//		ObjectStorage ret = null;
//		fileName = FileUtil.getFileName(fileName);
//		ret = findObjectStorageByFileName(fileName, (compressed?ImageUtil.IMAGE_COMPRESSED_SUFFIX:""));
//		if (ret != null) {
//			if (getThumbnail) {
//				System.err.println("::"+WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_FILE_WATERMARK);
//				byte[] fileBody = ImageUtil.resizeThumbnails(ret.getBytes(),ret.getType(),accuracy, height, width,
//								WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_FILE_WATERMARK);
//				ret.setBytes(fileBody);
//			} 
//		}
//		return ret;
//	}
	
	
	
//	@Override
//	public ObjectStorage findObjectStorage(String query) {
//		return findObjectStorage(query, null);
//	}
//	@Override
//	public ObjectStorage findObjectStorage(String query, String ext) {
//		ObjectStorage ret = null;
//		ret = buildObjectStorage(query,ext, false, null,null,null);
//		return ret;
//	}
//	@Override
//	public ObjectStorage findObjectStorage(String query, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width){
//		ObjectStorage ret = null;
//		ret = buildObjectStorage(query, (compressed?ImageUtil.IMAGE_COMPRESSED_SUFFIX:""), getThumbnail, accuracy, height, width);
//		return ret;
//	}
	
	
	/**
	 * build a objectsotrage with null bytes
	 * @param query
	 * @return
	 */
	@Override
	public ObjectStorage buildObjectStorage(String query) {
		ObjectStorage ret = null;
		//\20191211\95f444173ff249589a9051ef33e5c710.png
		
		//get info from url
		ret = ObjectHeaderUtil.url2objectstorage(query);
		
		//get info form db
//		Optional<ObjectHeader> oa = objectHeaderRepository.findOneByUrl(query);
//		ObjectHeader oh = null;
//		if (oa.isPresent()) {
//			oh = oa.get();
//			ret = new ObjectStorage();
//			ret.setPath(oh.getObjectBodyPath());
//			ret.setName(oh.getObjectBodyMd5());
//			ret.setType(oh.getFileType());
//		}
		
		return ret;
	}
	

//	public byte[] resizeImage(byte[] imageBytes, String imageType, Double accuracy,Integer height, Integer width) {
//		byte[] ret = ImageUtil.resizeThumbnails(imageBytes, imageType, accuracy, height, width,
//		WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_FILE_WATERMARK);
//		return ret;
//	}
	
	
	
//	private ObjectStorage buildImageObjectStorage(String query, String ext, boolean getThumbnail, Double accuracy,Integer height, Integer width) {
//		ObjectStorage ret = null;
//		//\20191211\95f444173ff249589a9051ef33e5c710.png
//		
//		//get info from url
//		ret = ObjectHeaderUtil.url2objectstorage(query);
//		
//		//get info form db
////		Optional<ObjectHeader> oa = objectHeaderRepository.findOneByUrl(query);
////		ObjectHeader oh = null;
////		if (oa.isPresent()) {
////			oh = oa.get();
////			ret = new ObjectStorage();
////			ret.setPath(oh.getObjectBodyPath());
////			ret.setName(oh.getObjectBodyMd5());
////			ret.setType(oh.getFileType());
////		}
//		
//		if (ret!=null) {
//			byte[] bytes = getImageBytes(ret.getPath(),ret.getName(),ext);
//			if (bytes==null) {
//				return ret;
//			}
//			ret.setBytes(bytes);
//			
//			if (getThumbnail) {
////				System.err.println("::"+WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_WATERMARK_FILE);
//				byte[] fileBody = ImageUtil.resizeThumbnails(ret.getBytes(),ret.getType(),accuracy, height, width,
//								WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_FILE_WATERMARK);
//				ret.setBytes(fileBody);
//			} 
//		}
//		return ret;
//	}
	

	
	

//	
//	/***
//	 * get image bytes
//	 */
//	private byte[] getImageBytes(String path, String name, String ext) {
//		return getBytes(properties.OBJECTSTORAGE_DIR_IMAGES, path, name, ext);
//	}
//	
//	/***
//	 * get file bytes
//	 */
//	private byte[] getFileBytes(String path, String name, String ext) {
//		return getBytes(properties.OBJECTSTORAGE_DIR_FILES, path, name, ext);
//	}
	
	/**
	 * get object bytes
	 * @param ossPath
	 * @param path
	 * @param name
	 * @param ext
	 * @return
	 */
	@Override
	public byte[] getBytes(String ossPath, String path, String name, String ext) {
		byte[] ret = null;
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
	
//	/***
//	 * save image body
//	 * @param fileBytes
//	 * @return
//	 */
//	private ObjectBody createImageBody(byte[] fileBytes) {
//		ObjectBody ret = null;
//		ret = createBody(fileBytes, properties.OBJECTSTORAGE_DIR_IMAGES);
//		return ret;
//	}	
//	/***
//	 * save image body
//	 * @param fileBytes
//	 * @return
//	 */
//	private ObjectBody createFileBody(byte[] fileBytes) {
//		ObjectBody ret = null;
//		ret = createBody(fileBytes, properties.OBJECTSTORAGE_DIR_FILES);
//		return ret;
//	}
	
	/***
	 * save file body by bytes
	 * @param fileBytes
	 * @return
	 */
	private ObjectBody createBody(String ossPath, byte[] fileBytes) {
		ObjectBody ret = null;
		if (fileBytes == null) {
	    	return ret;// file not exist;
		}
		String md5 = TokenUtil.md5(fileBytes);
		String path = new SimpleDateFormat("yyyyMMdd").format(new Date());
		ret = createBody(ossPath, fileBytes, path, md5, null);
		return ret;
	}

	/***
	 * save body directly with the path and md5 name ; 
	 * can storage the thb or zip of the original file;
	 * like 500c899c529f409bb6b0269049d46c53.thb or 500c899c529f409bb6b0269049d46c53.zip 
	 */	
	private ObjectBody createBody(String ossPath, byte[] fileBytes, String path, String md5, String ext) {
		ObjectBody ret = null;
		if (fileBytes == null) {
	    	return ret;// file not exist;
		}
		if (!StringUtil.isEmpty(ext)) {
			md5=md5+ext;
		}
		Optional<ObjectBody> oob = objectBodyRepository.findOneByMd5(md5);
		if (oob.isPresent()) {
			ret = oob.get();
			LogManager.getLogger().debug("createBody: "+ret.getId()+" body exist.");
		} else {
			// create a path and save the bytes to file;
			String fileFullPath = ossPath + File.separator + path + File.separator + md5;
			File desFile = new File(fileFullPath);
			if (!desFile.getParentFile().exists()) {
				desFile.getParentFile().mkdirs();
			}			
			if(!FileUtil.writeFile(fileBytes, desFile)) {
				//save file failed;
				return ret;
			}
			//save the body in db
			ObjectBody ob = new ObjectBody();
			ob.setPath(path);
			ob.setMd5(md5);
			ret = objectBodyRepository.save(ob);
		}
		return ret;
	}
}
