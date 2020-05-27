package com.labor.spring.auth.service;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.util.WebUtil;
import com.labor.spring.core.entity.Fingerprint;
import com.labor.spring.core.entity.FingerprintOnline;
import com.labor.spring.core.entity.User;
import com.labor.spring.core.entity.UserFingerprint;
import com.labor.spring.core.service.FingerprintServiceIntf;
import com.labor.spring.core.service.UserServiceIntf;

@Service
public class AccessServiceImpl implements AccessService {
	@Autowired
	private UserServiceIntf userService;	
	@Autowired
	private FingerprintServiceIntf fpService;
	
	@Override
	public User validateNameAndPassword(String name, String saltpwdmd5, String cfmd5, String rememberme, String timestamp) {
		User ret = null;
		if (StringUtil.isEmpty(name)||StringUtil.isEmpty(saltpwdmd5)||StringUtil.isEmpty(timestamp)){
			return ret;
		}
		User dbuser = null;
		//login by name;
		if (dbuser==null) {
			dbuser = userService.findByName(name);
		}
		//login by sno;
		if (dbuser==null) {
			dbuser = userService.findBySno(name);
		}
		//login by phone;
		if (dbuser==null) {
			dbuser = userService.findByCellPhone(name);
		}
		//user is not exist
		if (dbuser==null) {
			LogManager.getLogger().error("user not exist.");
			return ret;
		//user exist 
		} else if (userService.validateUserRememberme(dbuser.getId(),cfmd5,WebConstants.FP_TYPE_CANVAS,rememberme)) {
			ret = dbuser;
		//user exist , password me validation.
		} else if (userService.validateUserPassword(dbuser.getId(),timestamp,saltpwdmd5)) {
			ret = dbuser;
		} 
		return ret;
	}
	
	@Override
	public User validateNameAndCode(String name, String code, String cfmd5, String rememberme) {
		User ret = null;
		if (StringUtil.isEmpty(name)||StringUtil.isEmpty(code)){
			return ret;
		}
		User dbuser = null;
		//login by name;
		if (dbuser==null) {
			dbuser = userService.findByName(name);
		}
		//login by sno;
		if (dbuser==null) {
			dbuser = userService.findBySno(name);
		}
		//login by phone;
		if (dbuser==null) {
			dbuser = userService.findByCellPhone(name);
		}
		//user is not exist
		if (dbuser==null) {
			LogManager.getLogger().error("user not exist.");
			return ret;
		//user exist 
		} else if (userService.validateUserRememberme(dbuser.getId(),cfmd5,WebConstants.FP_TYPE_CANVAS,rememberme)) {
			ret = dbuser;
		//user exist .
		} else if (userService.validateUserAuthcode(dbuser.getUuid(),code)) {
			ret = dbuser;
		} 
		return ret;
	}
	@Override
	@Transactional
	public FingerprintOnline createOnline(Integer userId, 
									String userName, 
									String authValue, 
									String authType, 
									String fpValue, 
									String fpType, 
									String rememberme) {
		FingerprintOnline ret = null;
		//create fingerprint
		Fingerprint fp = fpService.create(fpValue, fpType);
		if(!CommonConstants.ACTIVE.equals(fp.getStatus())) {
			fp.setStatus(CommonConstants.ACTIVE);
			fpService.update(fp.getId(), fp, true);
		}				

		//create user fingerprint
		userService.createUserFingerprint(fp.getValue(), fp.getType(), fp.getId(), userId, CommonConstants.INACTIVE);
		
		//create fingerprint online
		FingerprintOnline fo = new FingerprintOnline();
		fo.setUserId(userId);
		fo.setUserName(userName);
		fo.setFpId(fp.getId());
		fo.setFpValue(fp.getValue());
		fo.setFpType(fp.getType());
		String authCode = TokenUtil.generateUUID();
		fo.setAuthCode(authCode);
		fo.setAuthType(authType);
		fo.setAuthValue(authValue);
		ret = fpService.saveOnline(userId,fpType,fpValue,authType,authValue,fo, true);
		return ret;
	}
	
	@Override
	@Transactional
	public FingerprintOnline updateOnlineAuthcode(String authValue, String authType) {
		FingerprintOnline ret = null;
		FingerprintOnline fo = fpService.findFoByAuthValueAndAuthType(authValue,authType);
    	if (fo!=null&&fo.getUserId()!=null&&fo.getUserId()>0&&fo.getUserName()!=null) {
    		String authcode = TokenUtil.generateUUID();
    		fo.setAuthCode(authcode);
    		ret = fpService.saveOnline(null,null,null,authType, authValue, fo, true);
    	}
		return ret;
	}
	
	@Override
	@Transactional
	public FingerprintOnline updateOnlineAccessed(String authCode) {
		FingerprintOnline ret = null;
		if (StringUtil.isEmpty(authCode)||StringUtil.isEqualedTrimLower("accessed", authCode)) {
			return ret;
		}
		FingerprintOnline fo = fpService.findFoByAuthCode(authCode);
		if (fo==null) {
			return ret;
		}
		//set token disposable;
		fo.setAuthCode(WebConstants.STATUS_LOGIN_ACCESSED);
		ret = fpService.saveOnline(null,null,null,fo.getAuthType(),fo.getAuthValue(),fo, true);
		return ret;
	}
	
	@Override
	@Transactional
	public void deleteOnline(String authValue, String authType) {
		fpService.deleteOnlineByAuthValueAndAuthType(authValue,authType);

	}
	@Override
	public FingerprintOnline findOnline(String authValue, String authType) {
		FingerprintOnline ret = null;
		ret = fpService.findFoByAuthValueAndAuthType(authValue,authType);
		return ret;
	}
	

}
