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
    private Integer id;
	
    @Column(name="user_id")
    private Integer userid;

    @Column(name="role_id")
    private Integer roleid;

    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	
	
}
