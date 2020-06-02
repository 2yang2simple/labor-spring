package com.labor.spring.feign.api.auth;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.base.BaseProperties;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.constants.WebConstants;

import com.labor.spring.feign.ObjectMapperUtil;
import com.labor.spring.feign.client.AuthFeignClient;
import com.labor.spring.util.WebUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
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
//@Primary
@EnableCaching
@CacheConfig(cacheNames = { "cache-auth" })
public class AuthCacheServiceFeignImpl implements AuthCacheService {

//	@Autowired
//	private AuthPermissionService authPermissionService;
	@Autowired
	private AuthFeignClient authFeignClient;
	@Autowired
	private BaseProperties baseProperties;
//	public static final String KEY_PREFIX_USER = "users-";
//	public static final String KEY_PREFIX_PERMISSIONS = "userpermissions-";
	
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
	public UserVO findLoginCache(String accessToken) {
		return null;
	}

	@CachePut(key =  "'users-'+#accessToken")
	@Override
	public UserVO fetchLoginCache(String accessToken) {
		System.err.println("----feign-cache--------fetchLoginUserUuid--" + accessToken);
		UserVO ret = null;
		HashMap<String, String> loginuser = fetchLoginUser(accessToken);
		if (loginuser!=null) {
			LogManager.getLogger().debug("loginuser id[{}], uuid[{}]:",loginuser.get("id"),loginuser.get("uuid"));
		
			//return derectly, local user id equals remote user id;
			ret = new UserVO();
			ret.setUserId(Long.valueOf(loginuser.get("id")));
			ret.setUserUuid(loginuser.get("uuid"));
			ret.setUserName(loginuser.get("name"));
			ret.setUserRealName(loginuser.get("realName"));
		}
		return ret;
	}

	@CacheEvict(key =  "'users-'+#accessToken")
	@Override
	public void clearLoginCache(String accessToken) {
		System.err.println("----feign-cache--------deleteByAccessToken--" + accessToken);
	}
	
	@Cacheable(key = "'userpermissions-'+#accessToken")
	@Override
	public Set<String> findLoginUserPermissions(String accessToken) {

		return null;
	}

	@CachePut(key = "'userpermissions-'+#accessToken")
	@Override
	public Set<String> fetchLoginUserPermissions(String accessToken) {
		System.err.println("----feign-cache--------fetchLoginUserPermissions--" + accessToken);
		Set<String> ret = new HashSet<String>();
		HashMap<String, String> puser = fetchLoginUser(accessToken);
		if (puser==null) {
			return ret;
		}
		//local
//		ret = authPermissionService.findUserPermissions(puser.getId(),puser.getName());
		//remote
		String pertype = baseProperties.CONTEXT_PATH;
		if (pertype!=null) {
			pertype = pertype.replace("/", "");
		}
		Result result = authFeignClient.fetchUserPermissions(pertype,puser.get("uuid"));
		if (ResultStatus.SUCCESS.code()==result.getCode()) {
			ret = ObjectMapperUtil.getObjectMapper()
					.convertValue(result.getData(),Set.class);
		}
		return ret;
	}

	@CacheEvict(key = "'userpermissions-'+#accessToken")
	@Override
	public void clearLoginUserPermissions(String accessToken) {
		System.err.println("----feign-cache--------clearLoginUserPermissions--" + accessToken);

		// local
//		accountService.deleteOnline(accessToken);

		// remote
		// call "/rest/account/token" TokenRestController.findOnlineProfile
	}

	@CacheEvict(allEntries = true)
	@Override
	public void clear() {
		System.err.println("----feign-cache--------clear all cache");
	}

	@Override
	public HashMap<String, String> fetchLoginUser(String accessToken) {
		HashMap<String, String> ret = null;
		Result result = authFeignClient.fetchLoginUser(accessToken);
		if (ResultStatus.SUCCESS.code()==result.getCode()) {
			ret = ObjectMapperUtil.getObjectMapper()
					.convertValue(result.getData(),HashMap.class);
		}
		return ret;
	}
	
	@Override
	public String fetchLoginUserToken(String type, String code) {
		String ret = null;
		Result result = authFeignClient.fetchLoginUserToken(type,code);
		if (ResultStatus.SUCCESS.code()==result.getCode()) {
			ret = (String)result.getData();
		}
		return ret;
	}

	@Override
	public void deleteLogin(String code) {
		authFeignClient.deleteLogin(code);
	}

	@Override
	public String createLogin(String clientKey, String clientUuid, String type, String code, String name) {
		String ret = null;
		Result result = authFeignClient.createLogin(clientKey, clientUuid, type, code, name);
		if (ResultStatus.SUCCESS.code()==result.getCode()) {
			ret = (String)result.getData();
		}
		return ret;
	}

	
	
}
