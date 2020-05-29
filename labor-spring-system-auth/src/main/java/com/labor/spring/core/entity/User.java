package com.labor.spring.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.apache.logging.log4j.LogManager;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.labor.common.constants.CommonConstants;
import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_core_user") 
public class User extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = -8326446655561277937L;
	
	@Id
    @Column(name="user_id")
    private Long id;
	
    @Column(name="user_cellphone")
    private String cellPhone; 
    
    @Column(name="user_weixin")
    private String weixin; 
    
    @Column(name="user_sno",updatable = false)
    private String sno; 
    
	@NotBlank(message = "the name is empty.")
    @Column(name="user_name")
    private String name; 
    
    @Column(name="user_realname")
    private String realName; 
    
    @Column(name="user_realnameen")
    private String realNameEn; 

    //forget password to modify; if 0 normal, else match pwdmodify to modify.
    @Column(name="user_pwdmodify")
    private String pwdmodify; 
    
    @Column(name="user_email")
    private String email; 
    
    @Column(name="user_googlesecretkey",updatable = false)
    private String googleSecretKey; 

    
    
	public String getGoogleSecretKey() {
//		return googleSecretKey;
		return "hidden";
	}
	public void setGoogleSecretKey(String googleSecretKey) {
		this.googleSecretKey = googleSecretKey;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	public String getPwdmodify() {
		return pwdmodify;
	}

	public void setPwdmodify(String pwdmodify) {
		this.pwdmodify = pwdmodify;
	}

	public String getRealName() {
		return (realName==null)?"":realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealNameEn() {
		return (realNameEn==null)?"":realNameEn;
	}

	public void setRealNameEn(String realNameEn) {
		this.realNameEn = realNameEn;
	}
	   

 
}