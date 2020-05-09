package com.labor.spring.system.ppp.entity.project;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_project_doc") 
public class ProjectDocument extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -3645725426464867012L;
	@Id
    @GeneratedValue 
    @Column(name="pd_id")
    private Integer id;
	
    @Column(name="proj_id")
    private Integer projectId;
	
    @Column(name="doc_id")
    private Integer docId;
	
    @Column(name="pd_type")
    private String pdType;
    
    @Column(name="pd_status")
    private String pdStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getPdType() {
		return pdType;
	}

	public void setPdType(String pdType) {
		this.pdType = pdType;
	}

	public String getPdStatus() {
		return pdStatus;
	}

	public void setPdStatus(String pdStatus) {
		this.pdStatus = pdStatus;
	}
    
    

}
