package com.labor.spring.system.ppp.api.oss;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.FileUtil;
import com.labor.common.util.ImageUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.system.ppp.ApplicationProperties;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.gallery.GalleryImage;
import com.labor.spring.system.ppp.entity.oss.ObjectBody;
import com.labor.spring.system.ppp.entity.oss.ObjectHeader;
import com.labor.spring.util.WebUtil;
@Service
public class ObjectStorageServiceImpl implements ObjectStorageServiceIntf{
	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private ObjectHeaderRepository objectHeaderRepository;
	@Autowired
	private ObjectBodyRepository objectBodyRepository;
	@Autowired
	private ObjectMapRepository objectMapRepository;

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

//	@Override
//	@Transactional
//	public ObjectHeader createImage(MultipartFile file) {
//		ObjectHeader ret = null;
//		if (file.isEmpty()) {
//	    	return ret;// file not exist;
//		}
//		String fileOriginalName = file.getOriginalFilename();		
//		String fileType = FileUtil.getFileType(fileOriginalName);
//		try {
//			byte[] bytes = FileUtil.stream2Bytes(file.getInputStream());
//			//rotate image
//			bytes = ImageUtil.processRotateInfo(FileUtil.stream2Bytes(file.getInputStream()),fileType);
//			ret = create(bytes,fileOriginalName);		
//			//compress image to a entity
//			if (bytes!=null&&ret!=null) {
//				//image larger than 300k should be compressed
//				byte[] newbytes;
//				newbytes = ImageUtil.resize(bytes, fileType, ImageUtil.IMAGE_COMPRESSED_KBSIZE);
//				if (newbytes!=null&&newbytes.length<bytes.length){
//					//save the compressed image with ext 
//					createBody(newbytes, ret.getObjectBodyPath(), ret.getObjectBodyMd5(), ImageUtil.IMAGE_COMPRESSED_SUFFIX);
//				}
//			}
//		} catch (Exception e) {
//			LogManager.getLogger().error("", e);
//		} 
//		return ret;
//	}

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
	
//	@Override
//	@Transactional
//	public ObjectHeader createHeader(ObjectHeader entity) {
//		ObjectHeader ret = null;
//		if (entity!=null) {
//			entity.setId(null);
//			entity.setStatus(CommonConstants.ACTIVE);
//			return objectHeaderRepository.save(entity);
//		}
//		return ret;
//	}


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
	public FileObject findFileObjectByFileName(String filename, String ext) {
		FileObject ret = null;
		Optional<ObjectHeader> oa = objectHeaderRepository.findOneByFileName(filename);
		if (oa.isPresent()) {
			ObjectHeader oh = oa.get();
			byte[] bytes = getBytes(oh.getObjectBodyPath(),oh.getObjectBodyMd5(),ext);
			ret = new FileObject();
			ret.setBytes(bytes);
			ret.setType(oh.getFileType());
			ret.setName(oh.getName());
			ret.setSize(oh.getFileSize());
		}
		return ret;
	}
	
	@Override
	public byte[] findBytesByFileName(String filename) {
		byte[] ret = null;
		Optional<ObjectHeader> oa = objectHeaderRepository.findOneByFileName(filename);
		if (oa.isPresent()) {
			ObjectHeader oh = oa.get();
			ret = getBytes(oh.getObjectBodyPath(),oh.getObjectBodyMd5(),null);
		}
		return ret;
	}
	@Override
	public ResponseEntity<byte[]> findEntityByFilename(String filename, String ext){
		ResponseEntity<byte[]> ret = null;
		byte[] fileBody = null;
		String attachment = "";
		FileObject fo = null;
		fo = findFileObjectByFileName(filename, ext);
		if (fo != null) {
			fileBody = fo.getBytes();
			attachment = filename + "."+ fo.getType();
		} 
		if (fileBody == null) {
			// if not exist or error, return 404.gif;
			fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_404_FILE);
			attachment = properties.IMG_404_FILE;
		}
		if (fileBody == null) {
			return ret;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=" + attachment);
		HttpStatus statusCode = HttpStatus.OK;
		ret = new ResponseEntity<byte[]>(fileBody, headers, statusCode);
		return ret;
	}
	@Override
	public ResponseEntity<byte[]> findImageEntityByFileName(String fileName, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width){
		ResponseEntity<byte[]> ret = null;
		byte[] fileBody = null;
		String attachment = "";

		FileObject fo = null;
		fo = findFileObjectByFileName(fileName, (compressed?ImageUtil.IMAGE_COMPRESSED_SUFFIX:""));
		if (fo != null) {
			if (getThumbnail) {
				fileBody = ImageUtil.resizeThumbnails(fo.getBytes(),fo.getType(),accuracy, height, width,
								WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_WATERMARK_FILE);
			} else {
				fileBody = fo.getBytes();
			}
			attachment = fileName + "." + fo.getType();
		}
		
		if (fileBody == null) {
			// if not exist or error, return notexist.gif;
			fileBody = FileUtil.file2Bytes(WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_NOT_EXIST);
			attachment = properties.IMG_NOT_EXIST;
		} 
		if (fileBody == null) {
			return ret;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=" + attachment);
		HttpStatus statusCode = HttpStatus.OK;
		ret = new ResponseEntity<byte[]>(fileBody, headers, statusCode);
		return ret;
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
