package com.labor.spring.system.ppp.entity.project;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_project_gallery") 
public class ProjectGallery extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -3645725426464867012L;
	@Id
    @GeneratedValue 
    @Column(name="pg_id")
    private Integer id;
	
    @Column(name="proj_id")
    private Integer projectId;
	
    @Column(name="ga_id")
    private Integer gaId;
	
    @Column(name="pg_type")
    private String pgType;
    
    @Column(name="pg_status")
    private String pgStatus;

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

	public Integer getGaId() {
		return gaId;
	}

	public void setGaId(Integer gaId) {
		this.gaId = gaId;
	}

	public String getPgType() {
		return pgType;
	}

	public void setPgType(String pgType) {
		this.pgType = pgType;
	}

	public String getPgStatus() {
		return pgStatus;
	}

	public void setPgStatus(String pgStatus) {
		this.pgStatus = pgStatus;
	}

    

}
