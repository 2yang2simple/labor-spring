package com.labor.spring.feign.api.auth;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseProperties;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.constants.WebConstants;

import com.labor.spring.feign.ObjectMapperUtil;
import com.labor.spring.feign.api.auth.AuthCacheService;
import com.labor.spring.feign.client.AuthFeignClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 *
 */
@Service
@EnableCaching
public class AuthCacheServiceFeignRedisImpl implements AuthCacheService {
	
	
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private AuthFeignClient authFeignClient;
//	@Autowired
//	private AuthPermissionService authPermissionService;
	@Autowired
	private BaseProperties baseProperties;
	
	@Autowired
    private RedisTemplate redisTemplate;
	
	public static final String NAME_CACHE = "cache-auth";
	public static final String KEY_PREFIX_TOKEN = "tokens-";
	public static final String KEY_PREFIX_USER = "users-";
	public static final String KEY_PREFIX_PERMISSIONS = "userpermissions-";
 
	private Cache getCache() {
		Cache cache = cacheManager.getCache(NAME_CACHE);
		if (cache==null) {
			throw new ServiceException(NAME_CACHE+" not exist.");
		}
		return cache;
	}
	
	@Override
	public String getToken(String key) {
		String ret = null;
		ret = (String)getCache().get(KEY_PREFIX_TOKEN+key, String.class);
//		ret = (String)redisTemplate.opsForValue().get(KEY_PREFIX_TOKEN+key);
		return ret;
	}
	@Override
	public String setToken(String key, String token) {
		getCache().put(KEY_PREFIX_TOKEN+key, token);
//		redisTemplate.opsForValue().set(KEY_PREFIX_TOKEN+key, token);
		return token;
	}
	@Override
	public void clearToken(String key) {
		getCache().evict(KEY_PREFIX_TOKEN+key);
//		redisTemplate.delete(KEY_PREFIX_TOKEN+key);
	}
	
	@Override
	public UserVO findLoginCache(String accessToken) {
		System.err.println("------redis-----findLoginCache--" + accessToken);
		UserVO ret = null;   	
    	ret = redisTemplate.opsForValue().get(KEY_PREFIX_USER+accessToken)==null
    			?null:(UserVO)redisTemplate.opsForValue().get(KEY_PREFIX_USER+accessToken);
    	return ret;
	}
	
	@Override
	public UserVO fetchLoginCache(String accessToken) {
		System.err.println("------redis-----fetchLoginCache--" + accessToken);
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
		redisTemplate.opsForValue().set(KEY_PREFIX_USER+accessToken, ret);
		return ret;
	}
	@Override
	public void clearLoginCache(String accessToken) {
		redisTemplate.delete(KEY_PREFIX_USER+accessToken);	
	}
	@Override
	public Set<String> findLoginUserPermissions(String accessToken) {
		System.err.println("------redis-----findLoginUserPermissions--" + accessToken);
		Set<String> ret = null;   	
		
		//use cache	
		ret = getCache().get(KEY_PREFIX_PERMISSIONS+accessToken,Set.class)==null
				?null:getCache().get(KEY_PREFIX_PERMISSIONS+accessToken,Set.class);
				
		//use redis
//    	ret = redisTemplate.opsForValue().get(KEY_PREFIX_PERMISSIONS+accessToken)==null
//    			?null:(Set<String>)redisTemplate.opsForValue().get(KEY_PREFIX_PERMISSIONS+accessToken);
    	return ret;
	}
		
	@Override
	public Set<String> fetchLoginUserPermissions(String accessToken) {
		System.err.println("------redis-----fetchLoginUserPermissions--" + accessToken);
		Set<String> ret = new HashSet<String>();
		HashMap<String, String> puser = fetchLoginUser(accessToken);
		if (puser==null) {
			return ret;
		}
		//local
//		ret = authPermissionService.findUserPermissions(puser.getId(),puser.getName());
		//remote
		String clientKey = baseProperties.getContextName();
		Result result = authFeignClient.fetchUserPermissions(clientKey,puser.get("uuid"));
		if (ResultStatus.SUCCESS.code()==result.getCode()) {
			ret = ObjectMapperUtil.getObjectMapper()
					.convertValue(result.getData(),Set.class);
		}
		
		//use cache;
		getCache().put(KEY_PREFIX_PERMISSIONS+accessToken, ret);
		
		//use redis
//		redisTemplate.opsForValue().set(KEY_PREFIX_PERMISSIONS+accessToken, ret);
		
		return ret;
	}
	
	@Override
	public void clearLoginUserPermissions(String accessToken) {
		//use cache;
		getCache().evict(KEY_PREFIX_PERMISSIONS+accessToken);
		//use redis
//		redisTemplate.delete(KEY_PREFIX_PERMISSIONS+accessToken);
		
	}
	@Override
	public void clear() {
		
		
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
