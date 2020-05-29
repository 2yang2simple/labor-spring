package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "tbl_core_rolepermission")
public class RolePermission implements Serializable {

	private static final long serialVersionUID = -4712561351641477832L;
	
	@Id
    @GeneratedValue 
    @Column(name="id")
    private Long id;
	
    @Column(name="role_id")
    private Long roleid;
    
    @Column(name="per_id")
    private Long perid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleid() {
		return roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public Long getPerid() {
		return perid;
	}

	public void setPerid(Long perid) {
		this.perid = perid;
	}

    
}
