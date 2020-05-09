package com.labor.spring.system.ppp.entity.document;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_doc") 
public class Document extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 5820452867623212392L;
	@Id
    @GeneratedValue 
    @Column(name="doc_id")
    private Integer id;
	
	@NotBlank(message = "the name is empty.")
    @Column(name="doc_name")
    private String name;
	
    @Column(name="doc_namepinyin")
    private String namePinyin;
    
    @Column(name="doc_filepath")
    private String filePath;
    
    @Column(name="doc_filemd5")
    private String fileMd5;
	
    @Column(name="doc_status")
    private String docStatus;

      
	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

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

	public String getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}
	
}
