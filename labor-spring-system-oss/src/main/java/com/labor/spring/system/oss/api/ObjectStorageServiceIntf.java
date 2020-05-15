package com.labor.spring.system.oss.api;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.system.oss.entity.ObjectBody;
import com.labor.spring.system.oss.entity.ObjectHeader;

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
	
	public ObjectStorage findObjectStorageByFileName(String filename);
	public ObjectStorage findObjectStorageByFileName(String filename, String ext);
	public ObjectStorage findObjectStorageByFileName(String fileName, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width);
	
	public ObjectStorage findObjectStorage(String query);
	public ObjectStorage findObjectStorage(String query, String ext);
	public ObjectStorage findObjectStorage(String query, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width);

//	public byte[] findBytesByFileName(String filename);
//	public byte[] findBytesByFileName(String filename, String ext);
//	public byte[] findBytesByFileName(String fileName, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width);

	
}
