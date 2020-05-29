package com.labor.spring.system.oss.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_oss_objectheader") 
public class ObjectHeader extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 7335532153504358126L;
	
	@Id
    @GeneratedValue 
    @Column(name="oh_id")
    private Long id;
	
	@Column(name="oh_url",updatable = false)
    private String url;
	
    @Column(name="oh_name")
    private String name;
	
    @Column(name="oh_filepath")
    private String filePath;
    
    @Column(name="oh_filename")
    private String fileName;
	
    @Column(name="oh_filetype")
    private String fileType;
	
    @Column(name="oh_filesize")
    private Long fileSize;
    
    @Column(name="ob_id")
    private Long objectBodyId;
	
    @Column(name="ob_path")
    private String objectBodyPath;
    
    @Column(name="ob_md5")
    private String objectBodyMd5;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getObjectBodyId() {
		return objectBodyId;
	}

	public void setObjectBodyId(Long objectBodyId) {
		this.objectBodyId = objectBodyId;
	}

	public String getObjectBodyPath() {
		return objectBodyPath;
	}

	public void setObjectBodyPath(String objectBodyPath) {
		this.objectBodyPath = objectBodyPath;
	}

	public String getObjectBodyMd5() {
		return objectBodyMd5;
	}

	public void setObjectBodyMd5(String objectBodyMd5) {
		this.objectBodyMd5 = objectBodyMd5;
	}

}
