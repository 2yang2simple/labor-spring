package com.labor.spring.feign.auth;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ParameterException;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.bean.ClientInfo;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.bean.LoginCache;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.api.fingerprint.FingerprintOnlineRepository;
import com.labor.spring.core.api.fingerprint.FingerprintRepository;
import com.labor.spring.core.api.fingerprint.FingerprintServiceIntf;
import com.labor.spring.core.api.permission.PermissionServiceIntf;
import com.labor.spring.core.api.role.RoleServiceIntf;
import com.labor.spring.core.api.sysconfig.SysconfigConstants;
import com.labor.spring.core.api.user.UserServiceIntf;
import com.labor.spring.core.entity.Fingerprint;
import com.labor.spring.core.entity.FingerprintOnline;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;
import com.labor.spring.core.entity.User;
import com.labor.spring.feign.client.auth.AuthFeignClient;
import com.labor.spring.feign.util.ObjectMapperUtil;
import com.labor.spring.util.WebUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@Service
@EnableCaching
@CacheConfig(cacheNames = { "cache-auth" })
public class AuthCacheServiceLocalImpl implements AuthCacheService {

	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private UserServiceIntf userService;	
	@Autowired
	private AuthPermissionService authPermissionService;
	@Autowired
	private FingerprintServiceIntf fpService;


	public static final String NAME_CACHE = "cache-auth";
	public static final String KEY_PREFIX_TOKEN = "tokens-";
	public static final String KEY_PREFIX_USER = "users-";
	public static final String KEY_PREFIX_PERMISSIONS = "userpermissions-";
	
	@Cacheable(key = "'tokens-'+#key")
	@Override
	public String getToken(String key) {
		return null;
	}
	@CachePut(key =  "'tokens-'+#key")
	@Override
	public String setToken(String key, String token) {
		return token;
	}
	@CacheEvict(key =  "'tokens-'+#key")
	@Override
	public void clearToken(String key) {
	}
	
	@Cacheable(key = "'users-'+#accessToken")
	@Override
	public LoginCache findLoginCache(String accessToken) {
		return null;
	}

	@CachePut(key =  "'users-'+#accessToken")
	@Override
	public LoginCache fetchLoginCache(String accessToken) {
		System.err.println("----local-cache--------fetchLoginCache--"+ WebUtil.getClassPath() + accessToken);
		LoginCache ret = null;
		User loginuser = fetchLoginUser(accessToken);
		if (loginuser!=null) {
			LogManager.getLogger().debug("localuser id[{}], uuid[{}]:",loginuser.getId(),loginuser.getUuid());
			ret = new LoginCache();
			ret.setUserId(loginuser.getId());
			ret.setUserUuid(loginuser.getUuid());
			ret.setUserName(loginuser.getName());
			ret.setUserRealName(loginuser.getRealName());
		}
		return ret;
	}

	@CacheEvict(key =  "'users-'+#accessToken")
	@Override
	public void clearLoginCache(String accessToken) {
		System.err.println("----local-cache--------clearLoginCache--" + accessToken);
	}
	
	@Cacheable(key = "'userpermissions-'+#accessToken")
	@Override
	public Set<String> findLoginUserPermissions(String accessToken) {

		return null;
	}

	@CachePut(key = "'userpermissions-'+#accessToken")
	@Override
	public Set<String> fetchLoginUserPermissions(String accessToken) {
		System.err.println("----local-cache--------fetchLoginUserPermissions--" + accessToken);
		Set<String> ret = new HashSet<String>();
		User puser = fetchLoginUser(accessToken);
		if (puser==null) {
			return ret;
		}
		ret = authPermissionService.findUserPermissions(puser.getId(),puser.getName());
		return ret;
	}

	@CacheEvict(key = "'userpermissions-'+#accessToken")
	@Override
	public void clearLoginUserPermissions(String accessToken) {
		System.err.println("----local-cache--------clearLoginUserPermissions--" + accessToken);
	}

	@CacheEvict(allEntries = true)
	@Override
	public void clear() {
		System.err.println("----local-cache--------clear all cache");
	}

	@Override
	public User fetchLoginUser(String accessToken) {
		User ret = null;
		FingerprintOnline fo = null;
		fo = fpService.findFoByAuthValue(accessToken);
		if (fo!=null) {
			ret = userService.findById(fo.getUserId());
		}
		if (ret!=null) {
			ret.setPwdmodify(null);
			ret.setGoogleSecretKey(null);
		}
		return ret;
	} 
	
	@Override
	public String fetchLoginUserToken(String type, String code) {
		String ret = null;
		FingerprintOnline fo = null;
		fo = fpService.findFoByAuthValue(code);
		if (fo==null) {
			return ret;
		}
		ret = fo.getAuthValue();
		return ret;
	}

	@Override
	public void deleteLogin(String code) {
		fpService.deleteOnlineByAuthValue(code);
	}

	@Override
	public String createLogin(String clientKey, String clientUuid, String type, String code, String name) {
	
		String accessTokenKey = TokenUtil.generateUUID();
		String accessToken = TokenUtil.generateUUID();
		
		ClientInfo ci = ClientRegisted.getClientInfo(clientKey);
		if (ci==null) {
			throw new ParameterException("clientKey error.");
		}

		User dbuser = null;

		if (StringUtil.isEqualedTrimLower(type, AuthConstants.LOGINTYPE_CELLPHONE)) {
			//name is the cellphone number
			//code is the number sent to cellphone, also stored in db;
			//validate the code and name 
//			if(!StringUtil.isNumeric(name)){
//				throw new ParameterException("name invalid.");
//			}
			//create a account by cellphone
			
			//login by phone;
			if (dbuser==null) {
				dbuser = userService.findByCellPhone(name);
			}
			//login by name;
			if (dbuser==null) {
				dbuser = userService.findByName(name);
			}

			
    	} else if (StringUtil.isEqualedTrimLower(type, AuthConstants.LOGINTYPE_WEIXIN)) {
    		//name is the weixin number
    		
    		//code is the sessionkey, openid etc something weixin returned
    		
    		//validate the code and name
    	}  else if (StringUtil.isEqualedTrimLower(type, AuthConstants.LOGINTYPE_EMAIL)) {
    		//name is the email
    		
    		//code is the code received by email
    		
    		//validate the code and name
    	} else {
    		throw new ParameterException("type error.");
    	}
		
		//user is not exist
		if (dbuser==null) {
			throw new ServiceException("account is not exist.");
			
		//user exist , account is not open and is not super user
		} else if (!CommonConstants.ACTIVE.equals(dbuser.getStatus())
					&&!StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER,dbuser.getName())){
			throw new ServiceException("account is closed.");
		} 
		
		Fingerprint dbfp = fpService.create(clientUuid, ci.getFpType());
		if ( dbfp!= null) {
			userService.createUserFingerprint(dbfp.getValue(), dbfp.getType(), dbfp.getId(), dbuser.getId(),CommonConstants.INACTIVE);
		}
		//create fingerprint online
		FingerprintOnline fo = new FingerprintOnline();
		fo.setUserId(dbuser.getId());
		fo.setUserName(dbuser.getName());
		fo.setFpId(dbfp.getId());
		fo.setFpValue(clientUuid);
		fo.setFpType(ci.getFpType());
		fo.setAuthCode(WebConstants.STATUS_LOGIN_ACCESSED);
		fo.setAuthType(ci.getAuthType());
		fo.setAuthValue(accessToken);
		fpService.saveOnline(dbuser.getId(),ci.getFpType(),clientUuid,ci.getAuthType(),accessToken,fo,true);
		
		Cache cache = cacheManager.getCache(NAME_CACHE);
		if (cache==null) {
			throw new ServiceException(NAME_CACHE+" cache not found.");
		}
		cache.put(KEY_PREFIX_TOKEN+accessTokenKey, accessToken);

		return accessTokenKey;
	}

	
	
}
