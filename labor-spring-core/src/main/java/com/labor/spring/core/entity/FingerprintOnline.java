package com.labor.spring.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_core_fingerprintonline") 
public class FingerprintOnline extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -4140212460246389063L;

	@Id
    @GeneratedValue 
    @Column(name="fo_id")
    private Long id;
	
    @Column(name="fp_id")
    private Long fpId;
	
    @Column(name="fp_value")
    private String fpValue; 
    
    @Column(name="fp_type")
    private String fpType;

    @Column(name="user_id")
    private Long userId;

    @Column(name="user_name")
    private String userName;
	
    @Column(name="auth_code")
    private String authCode; 
	
    @Column(name="auth_value")
    private String authValue; 
    
    @Column(name="auth_type")
    private String authType;


    @Column(name="session_id")
    private String sessionId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFpId() {
		return fpId;
	}

	public void setFpId(Long fpId) {
		this.fpId = fpId;
	}

	public String getFpValue() {
		return fpValue;
	}

	public void setFpValue(String fpValue) {
		this.fpValue = fpValue;
	}

	public String getFpType() {
		return fpType;
	}

	public void setFpType(String fpType) {
		this.fpType = fpType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAuthValue() {
		return authValue;
	}

	public void setAuthValue(String authValue) {
		this.authValue = authValue;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

    
    
}
