package com.labor.spring.system.oss.controller.vo;

import com.labor.spring.base.AbstractVO;

public class ObjectStorageVO extends AbstractVO{
	
	private byte[] bytes;
	
	private ObjectStorageType osType;
	
	private String type;
	
	private String path;
	
	private String name;
	
	private long size;

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ObjectStorageType getOsType() {
		return osType;
	}

	public void setOsType(ObjectStorageType osType) {
		this.osType = osType;
	}
	
	

}
