package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_core_fingerprint") 
public class Fingerprint extends AbstractEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3058558708272541201L;

	@Id
    @GeneratedValue 
    @Column(name="fp_id")
    private Long id;
	
    @Column(name="fp_value")
    private String value; 
    
    @Column(name="fp_type")
    private String type;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	} 
    
    
}
