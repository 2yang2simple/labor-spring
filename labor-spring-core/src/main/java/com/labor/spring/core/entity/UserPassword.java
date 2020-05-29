package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_core_userpassword")
public class UserPassword  extends AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3074066962328708802L;
	@Id
    @GeneratedValue 
    @Column(name="pw_id")
    private Long id;
	
    @Column(name="user_id")
    private Long userid; 
    
    @Column(name="user_pwd")
    private String pwd;
    
    @Column(name="user_pwdmd5")
    private String pwdmd5;
    
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
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPwdmd5() {
		return pwdmd5;
	}
	public void setPwdmd5(String pwdmd5) {
		this.pwdmd5 = pwdmd5;
	}
}
