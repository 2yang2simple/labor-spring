package com.labor.spring.system.ppp.entity.oss;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_oss_objectmap") 
public class ObjectMap extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -2749555658680832927L;

	@Id
    @GeneratedValue 
    @Column(name="om_id")
    private Integer id;

	@Column(name="oh_id")
    private Integer objectHeaderId;
	
    @Column(name="om_typeid")
    private Integer typeId;
	
    @Column(name="om_type")
    private String type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getObjectHeaderId() {
		return objectHeaderId;
	}

	public void setObjectHeaderId(Integer objectHeaderId) {
		this.objectHeaderId = objectHeaderId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    
}
