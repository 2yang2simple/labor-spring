package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.logging.log4j.LogManager;

import com.labor.spring.base.AbstractEntityLongId;

@Entity
@Table(name = "tbl_core_userhistory")

public class UserHistory extends AbstractEntityLongId implements Serializable {

	private static final long serialVersionUID = -2134326618804138834L;
	
	@Id
    @GeneratedValue 
    @Column(name="his_id")
    private Long id;
	
    @Column(name="user_id")
    private Long userid;

	@Column(name="user_cellphone")
    private String cellPhone; 

    @Column(name="user_weixin")
    private String weixin; 
    
    @Column(name="user_sno")
    private String sno; 
    
    @Column(name="user_name")
    private String name; 
    
    @Column(name="user_realname")
    private String realName; 
    
    @Column(name="user_realnameen")
    private String realNameEn; 
    
    @Column(name="user_email")
    private String email; 
    
    @Column(name="user_clienttk")
    private String clienttk; 
    
    @Column(name="user_clientip")
    private String clientip;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealNameEn() {
		return realNameEn;
	}

	public void setRealNameEn(String realNameEn) {
		this.realNameEn = realNameEn;
	}

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

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getClienttk() {
		return clienttk;
	}

	public void setClienttk(String clienttk) {
		this.clienttk = clienttk;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}
    
    
    
}
