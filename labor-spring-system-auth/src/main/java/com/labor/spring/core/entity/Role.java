package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_core_role") 
public class Role extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -1466375614137873099L;
	@Id
    @GeneratedValue 
    @Column(name="role_id")
    private Long id;
    
	@NotBlank(message = "the name is empty.")
	@Column(name="role_name")
    private String name; 

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    
}
