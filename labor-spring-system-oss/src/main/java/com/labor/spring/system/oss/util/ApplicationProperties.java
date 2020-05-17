package com.labor.spring.system.oss.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.labor.spring.base.BaseProperties;

@Component
public class ApplicationProperties extends BaseProperties {
	
//	@Value("${attachments.dir}")
//	public String ATTACHMENTS_DIR;
//	
//	@Value("${documents.dir}")
//	public String DOCUMENTS_DIR;
	
//	@Value("${objectstorage.dir}")
//	public String OBJECTSTORAGE_DIR;
	
	@Value("${objectstorage.dir.images}")
	public String OBJECTSTORAGE_DIR_IMAGES;
	
	@Value("${objectstorage.dir.files}")
	public String OBJECTSTORAGE_DIR_FILES;
	
	@Value("${img.dir}")
	public String IMG_DIR;
	
	@Value("${img.file.404}")
	public String IMG_FILE_404;

	@Value("${img.file.watermark}")
	public String IMG_FILE_WATERMARK;
	
	@Value("${img.file.notexist}")
	public String IMG_FILE_NOTEXIST;


	
}
