package com.labor.spring.system.oss.service;

import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.system.oss.controller.vo.ObjectStorageType;
import com.labor.spring.system.oss.entity.ObjectHeader;

public interface ObjectStorageService {
	

	public ObjectHeader createFile(ObjectStorageType ot, MultipartFile file);
	public ObjectHeader createFile(ObjectStorageType ot, byte[] fileBytes, String fileOriginalName);
	public ObjectHeader createImage(ObjectStorageType ot, MultipartFile file);
	public ObjectHeader createImage(ObjectStorageType ot, byte[] fileBytes, String fileOriginalName);
	
//	public ObjectHeader createHeader(ObjectHeader entity);
	
//	public ObjectBody createBody(byte[] fileBytes);
//	public ObjectBody createBody(byte[] fileBytes, String path, String md5, String ext); 
//	
//	public Optional<ObjectHeader> findHeaderByFileName(String fileName);
//	public Optional<ObjectBody> findBodyById(Integer id);
//	public Optional<ObjectBody> findBodyByMd5(String md5);
//	
//	public Long countByExample(ObjectBody entity);
	
//	public ObjectStorage findObjectStorageByFileName(String filename);
//	public ObjectStorage findObjectStorageByFileName(String filename, String ext);
//	public ObjectStorage findObjectStorageByFileName(String fileName, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width);
//	
//	public ObjectStorage findObjectStorage(String query);
//	public ObjectStorage findObjectStorage(String query, String ext);
//	public ObjectStorage findObjectStorage(String query, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width);

//	public byte[] findBytesByFileName(String filename);
//	public byte[] findBytesByFileName(String filename, String ext);
//	public byte[] findBytesByFileName(String fileName, boolean compressed, boolean getThumbnail, Double accuracy,Integer height, Integer width);

//	public ObjectStorage buildObjectStorage(String query);
	

//	public byte[] getFileBytes(String path, String name, String ext);
//	public byte[] getImageBytes(String path, String name, String ext);
}
