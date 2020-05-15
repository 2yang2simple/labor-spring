package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_core_userfingerprint") 
public class UserFingerprint extends AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2456835616911209174L;
	@Id
    @GeneratedValue 
    @Column(name="uf_id")
    private Integer id;

	@Column(name="uf_rememberme")
    private String rememberMe; 
	
    @Column(name="user_id")
    private Integer userid; 
    
    @Column(name="fp_id")
    private Integer fpid;
    
    @Column(name="fp_value")
    private String value;

	@Column(name="fp_type")
    private String type;

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

	public Integer getFpid() {
		return fpid;
	}

	public void setFpid(Integer fpid) {
		this.fpid = fpid;
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

	public String getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}
    

}
