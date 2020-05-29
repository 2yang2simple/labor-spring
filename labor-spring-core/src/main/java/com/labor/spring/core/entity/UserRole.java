package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "tbl_core_userrole")
public class UserRole implements Serializable {

	private static final long serialVersionUID = -1664139408554005811L;

	@Id
    @GeneratedValue 
    @Column(name="id")
    private Long id;
	
    @Column(name="user_id")
    private Long userid;

    @Column(name="role_id")
    private Long roleid;

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getRoleid() {
		return roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}
	
	
}
