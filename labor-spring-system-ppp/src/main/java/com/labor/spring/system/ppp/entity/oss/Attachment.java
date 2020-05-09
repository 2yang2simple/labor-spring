package com.labor.spring.system.ppp.entity.oss;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.labor.common.util.StringUtil;
import com.labor.spring.base.AbstractEntity;
import com.labor.spring.core.GlobalInfo;
import com.labor.spring.util.WebUtil;

@Entity
@Table(name = "tbl_oss_attachment") 
public class Attachment extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 7977843692536931792L;
	public static final String DOWNLOAD_URL = "/attachments/filename-";
	
	@Id
    @GeneratedValue 
    @Column(name="atta_id")
    private Integer id;
	
    @Column(name="atta_url",updatable = false)
    private String url;
	
    @Column(name="atta_name")
    private String name;
	
    @Column(name="atta_filepath")
    private String filePath;
    
    @Column(name="atta_filename")
    private String fileName;
	
    @Column(name="atta_filetype")
    private String fileType;
	
    @Column(name="atta_filesize")
    private Long fileSize;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getUrl() {
		if(StringUtil.isEmpty(url)) {
			return GlobalInfo.CONTEXT_PATH + DOWNLOAD_URL + fileName;
		} else {
			return url;
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
    
    
}
