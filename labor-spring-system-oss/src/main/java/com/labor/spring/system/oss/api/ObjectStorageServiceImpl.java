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

import com.labor.spring.util.WebUtil;
@Service
public class ObjectStorageServiceImpl implements ObjectStorageServiceIntf{
	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private ObjectHeaderRepository objectHeaderRepository;
	@Autowired
	private ObjectBodyRepository objectBodyRepository;


	@Override
	@Transactional
	public ObjectHeader create(MultipartFile file) {
		ObjectHeader ret = null;
		if (file.isEmpty()) {
	    	return ret;// file not exist;
		}
		String name = file.getOriginalFilename();		
		try {
			byte[] bytes = FileUtil.stream2Bytes(file.getInputStream());
			
			ret = create(bytes,name);

		} catch (Exception e) {
			LogManager.getLogger().error("", e);
		} 
		return ret;
	}
	
	@Override
	@Transactional
	public ObjectHeader createImage(MultipartFile file) {
		ObjectHeader ret = null;
		if (file.isEmpty()) {
	    	return ret;// file not exist;
		}
		String name = file.getOriginalFilename();		
		try {
			byte[] bytes = FileUtil.stream2Bytes(file.getInputStream());
			
			ret = createImage(bytes,name);

		} catch (Exception e) {
			LogManager.getLogger().error("", e);
		} 
		return ret;
	}

	@Override
	@Transactional
	public ObjectHeader createImage(byte[] fileBytes, String fileOriginalName) {
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
			ret = create(rotateBytes,fileOriginalName);
		}
		
		//compress image to a entity
		if (rotateBytes!=null) {
			//image larger than 300k should be compressed
			compressedBytes = ImageUtil.resize(rotateBytes, fileType, ImageUtil.IMAGE_COMPRESSED_KBSIZE);
			if (ret!=null&&
					compressedBytes!=null&&
					compressedBytes.length<rotateBytes.length){
				//save only the compressed image body with ext for display
				createBody(compressedBytes, ret.getObjectBodyPath(), ret.getObjectBodyMd5(), ImageUtil.IMAGE_COMPRESSED_SUFFIX);
			}

		}
		return ret;
	}
	
	@Override
	@Transactional
	public ObjectHeader create(byte[] fileBytes, String fileOriginalName) {
		ObjectHeader ret = null;
		if (fileBytes == null) {
	    	return ret;// file not exist;
		}
		Long fileSize = Long.valueOf(fileBytes.length);

		/***** save the file , if user other osd service , do storage here; */
		ObjectBody objectBody = createBody(fileBytes);
		if (objectBody!=null) {
			/***** save the file header */
			ObjectHeader objectHeader = new ObjectHeader();
			// save the header to db;
			objectHeader.setName(fileOriginalName);
			objectHeader.setFilePath(objectBody.getPath());
			objectHeader.setFileName(TokenUtil.generateUUID());
			objectHeader.setFileType(FileUtil.getFileType(fileOriginalName));
			objectHeader.setFileSize(fileSize);
			//url set null when store file in internal server; set real url when store in external server, like easyimg etc.
			objectHeader.setUrl("");
			objectHeader.setObjectBodyId(objectBody.getId());
			objectHeader.setObjectBodyPath(objectBody.getPath());
			objectHeader.setObjectBodyMd5(objectBody.getMd5());
			ret = objectHeaderRepository.save(objectHeader);
		}
		return ret;		
	}

	@Override
	@Transactional
	public ObjectBody createBody(byte[] fileBytes) {
		ObjectBody ret = null;
		if (fileBytes == null) {
	    	return ret;// file not exist;
		}
		String md5 = TokenUtil.md5(fileBytes);
		String filePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
		ret = createBody(fileBytes, filePath, md5, null);
		return ret;
	}

	/***
	 * save body directly with the path and md5 name ; 
	 * can storage the thb or zip of the original file;
	 * like 500c899c529f409bb6b0269049d46c53.thb or 500c899c529f409bb6b0269049d46c53.zip 
	 */	
	@Override
	@Transactional
	public ObjectBody createBody(byte[] fileBytes, String path, String md5, String ext) {
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
			String fileFullPath = properties.OBJECTSTORAGE_DIR + File.separator + path + File.separator + md5;
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
	

	@Override
	public Optional<ObjectHeader> findHeaderByFileName(String fileName) {
		return objectHeaderRepository.findOneByFileName(fileName);
	}
	@Override
	public Optional<ObjectBody> findBodyById(Integer id) {
		return objectBodyRepository.findOneById(id);
	}
	@Override
	public Optional<ObjectBody> findBodyByMd5(String md5) {
		return objectBodyRepository.findOneByMd5(md5);
	}
	@Override
	public Long countByExample(ObjectBody entity){
		Example<ObjectBody> example =Example.of(entity);
		return objectBodyRepository.count(example);
	}
	
	@Override
	public ObjectStorage findObjectStorageByFileName(String filename, String ext) {
		ObjectStorage ret = null;
		Optional<ObjectHeader> oa = objectHeaderRepository.findOneByFileName(filename);
		if (oa.isPresent()) {
			ObjectHeader oh = oa.get();
			byte[] bytes = getBytes(oh.getObjectBodyPath(),oh.getObjectBodyMd5(),ext);
			ret = new ObjectStorage();
			ret.setBytes(bytes);
			ret.setType(oh.getFileType());
			ret.setName(oh.getFileName());
			ret.setSize(oh.getFileSize());
		}
		return ret;
	}
	
	@Override
	public byte[] findBytesByFileName(String filename) {
		return findBytesByFileName(filename, null);
	}
	
	@Override
	public byte[] findBytesByFileName(String filename, String ext){
		byte[] ret = null;
		Optional<ObjectHeader> oa = objectHeaderRepository.findOneByFileName(filename);
		if (oa.isPresent()) {
			ObjectHeader oh = oa.get();
			ret = getBytes(oh.getObjectBodyPath(),oh.getObjectBodyMd5(),ext);
		}
		return ret;
	}
	@Override
	public byte[] findBytesByFileName(String fileName, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width){
		byte[] fileBody = null;

		ObjectStorage os = null;
		os = findObjectStorageByFileName(fileName, (compressed?ImageUtil.IMAGE_COMPRESSED_SUFFIX:""));
		if (os != null) {
			if (getThumbnail) {
				System.err.println("::"+WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_WATERMARK_FILE);
				fileBody = ImageUtil.resizeThumbnails(os.getBytes(),os.getType(),accuracy, height, width,
								WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_WATERMARK_FILE);
			} else {
				fileBody = os.getBytes();
			}
		}
		
		return fileBody;
	}
	
	private byte[] getBytes(String path, String md5, String ext) {
		byte[] ret = null;
		File file = new File(properties.OBJECTSTORAGE_DIR + File.separator 
				+ path + File.separator 
				+ md5
				+ (ext==null?"":ext));
		if (file.exists()) {
			LogManager.getLogger().debug("getBytes:"+ext);
		} else {
			file = new File(properties.OBJECTSTORAGE_DIR + File.separator 
							+ path + File.separator 
							+ md5);
		}
		
		ret = FileUtil.file2Bytes(file);
		return ret;
	}
}
