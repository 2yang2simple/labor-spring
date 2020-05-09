package com.labor.spring.system.ppp.entity.tag;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_tag") 
public class Tag extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -4500742405548370647L;

	@Id
    @GeneratedValue 
    @Column(name="tag_id")
    private Integer id;
	
    @Column(name="tag_type")
    private String type;
    
    @Column(name="tag_name")
    private String name;
    
    @Column(name="tag_namepinyin")
    private String namePinyin;
    
    @Column(name="parent_id")
    private Integer parentId;

	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
    
}
