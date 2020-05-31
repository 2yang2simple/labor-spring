package com.labor.spring.system.oss.controller.vo;

public enum ObjectStorageType {
	
	NAS_FILE("10"),		
	NAS_IMAGE("11"),
	ALIYUN_OSS_FILE("20"),
	ALIYUN_OSS_IMAGE("21");
	
	private final String type;
	
	ObjectStorageType(String ptype) {
		this.type = ptype;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getPath() {
		switch(type){
		    case "10" :	
		    	return "files";
		    case "11" :
		    	return "images";
		    case "20" :
		    	return "files";
		    case "21" :
		    	return "images";
		    default : 		    	
		    	return null;
		}
	}
	public static ObjectStorageType create(String type) {
		switch(type){
		    case "10" :	
		    	return NAS_FILE;
		    case "11" :
		    	return NAS_IMAGE;
		    case "20" :
		    	return ALIYUN_OSS_FILE;
		    case "21" :
		    	return ALIYUN_OSS_IMAGE;
		    default : 		    	
		    	return null;
		}
	}
		
}
