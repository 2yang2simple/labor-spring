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
    private Integer id;
	
    @Column(name="role_id")
    private Integer roleid;
    
    @Column(name="per_id")
    private Integer perid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public Integer getPerid() {
		return perid;
	}

	public void setPerid(Integer perid) {
		this.perid = perid;
	}

    
}
