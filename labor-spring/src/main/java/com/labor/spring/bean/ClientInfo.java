package com.labor.spring.bean;

import java.io.Serializable;

public class ClientInfo implements Serializable {
	
	private static final long serialVersionUID = 5929871223017286342L;
	//client type , ios_app, web, weixin, etc
	private String fpType;
	//token store type, store in cooike, weixin, app, etc
	private String authType;
	private String perType;
	
	//client secret
	private String secret;
	//callback uri after login;
	private String callback4AccessToken;
	//callback uri when secret missing;
	private String callback4ClientSecret;
	
	public ClientInfo(
				String pFpType,
				String pAuthType,
				String pPerType,
				String pSecret,
				String pCallback4AccessToken,
				String pCallback4ClientSecret) {
		
		fpType=pFpType;
		authType=pAuthType;
		perType=pPerType;
		secret=pSecret;
		callback4AccessToken=pCallback4AccessToken;
		callback4ClientSecret=pCallback4ClientSecret;
	}
	
	public String getFpType() {
		return fpType;
	}
	public void setFpType(String fpType) {
		this.fpType = fpType;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getCallback4AccessToken() {
		return callback4AccessToken;
	}

	public void setCallback4AccessToken(String callback4AccessToken) {
		this.callback4AccessToken = callback4AccessToken;
	}

	public String getCallback4ClientSecret() {
		return callback4ClientSecret;
	}

	public void setCallback4ClientSecret(String callback4ClientSecret) {
		this.callback4ClientSecret = callback4ClientSecret;
	}

	public String getPerType() {
		return perType;
	}

	public void setPerType(String perType) {
		this.perType = perType;
	}

}
