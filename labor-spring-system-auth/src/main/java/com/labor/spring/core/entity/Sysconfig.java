package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_core_sysconfig") 
public class Sysconfig extends AbstractEntity implements Serializable {


	private static final long serialVersionUID = 6600901179323872230L;

	@Id
    @GeneratedValue 
    @Column(name="sc_id")
    private Long id;
	
	@NotBlank(message = "the Key is empty.")
    @Column(name="sc_key")
    private String key; 
	
	@NotBlank(message = "the Value is empty.")
    @Column(name="sc_value")
    private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
    
    
}
