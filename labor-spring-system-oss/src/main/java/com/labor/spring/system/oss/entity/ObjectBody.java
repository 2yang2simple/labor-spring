package com.labor.spring.system.oss.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_oss_objectbody") 
public class ObjectBody extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -1391743265781849160L;
	
	@Id
    @GeneratedValue 
    @Column(name="ob_id")
    private Long id;
	
	@Column(name="ob_path")
    private String path;
	
	@Column(name="ob_md5")
    private String md5;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	

}
