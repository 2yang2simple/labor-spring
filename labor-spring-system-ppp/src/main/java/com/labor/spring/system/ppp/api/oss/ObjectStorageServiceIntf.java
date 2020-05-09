package com.labor.spring.system.ppp.api.oss;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.system.ppp.entity.oss.ObjectBody;
import com.labor.spring.system.ppp.entity.oss.ObjectHeader;

public interface ObjectStorageServiceIntf {
	

	public ObjectHeader create(MultipartFile file);
	public ObjectHeader createImage(MultipartFile file);
	public ObjectHeader create(byte[] fileBytes, String fileOriginalName);
	public ObjectHeader createImage(byte[] fileBytes, String fileOriginalName);
//	public ObjectHeader createHeader(ObjectHeader entity);
	
	public ObjectBody createBody(byte[] fileBytes);
	public ObjectBody createBody(byte[] fileBytes, String path, String md5, String ext); 
	
	public Optional<ObjectHeader> findHeaderByFileName(String fileName);
	public Optional<ObjectBody> findBodyById(Integer id);
	public Optional<ObjectBody> findBodyByMd5(String md5);
	
	public Long countByExample(ObjectBody entity);

	public FileObject findFileObjectByFileName(String filename, String ext);
	public byte[] findBytesByFileName(String filename);
	public ResponseEntity<byte[]> findEntityByFilename(String filename, String ext);
	public ResponseEntity<byte[]> findImageEntityByFileName(String fileName, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width);

	
}
