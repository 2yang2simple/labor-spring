package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_core_permission") 
public class Permission extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 8386906373021108386L;
	@Id
    @GeneratedValue 
    @Column(name="per_id")
    private Integer id;
	
	@Column(name="per_code")
    private String code;
	
	@Column(name="per_type")
    private String type;
	
    @Column(name="parent_id")
    private Integer parentid;
    
	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	} 
	
	
}
