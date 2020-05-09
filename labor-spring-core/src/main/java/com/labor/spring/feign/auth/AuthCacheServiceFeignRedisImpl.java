package com.labor.spring.feign.auth;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.spring.bean.LoginCache;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.api.permission.PermissionServiceIntf;
import com.labor.spring.core.api.role.RoleServiceIntf;
import com.labor.spring.core.api.user.UserRepository;
import com.labor.spring.core.api.user.UserServiceIntf;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;
import com.labor.spring.core.entity.User;
import com.labor.spring.feign.client.auth.AuthFeignClient;
import com.labor.spring.feign.util.ObjectMapperUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 *
 */
@Service
//@Primary
public class AuthCacheServiceFeignRedisImpl implements AuthCacheService {

	@Autowired
	private AuthFeignClient authFeignClient;
	@Autowired
	private UserServiceIntf userService;
	@Autowired
	private AuthPermissionService authPermissionService;

	
	@Autowired
    private RedisTemplate redisTemplate;

	public static final String KEY_PREFIX_TOKEN = "tokens-";
	public static final String KEY_PREFIX_USER = "users-";
	public static final String KEY_PREFIX_PERMISSIONS = "userpermissions-";
 
	
	@Override
	public String getToken(String key) {
		String ret = null;
		ret = (String)redisTemplate.opsForValue().get(KEY_PREFIX_TOKEN+key);
		return ret;
	}
	@Override
	public String setToken(String key, String token) {
		redisTemplate.opsForValue().set(KEY_PREFIX_TOKEN+key, token);
		return token;
	}
	@Override
	public void clearToken(String key) {
		redisTemplate.delete(KEY_PREFIX_TOKEN+key);
	}
	
	@Override
	public LoginCache findLoginCache(String accessToken) {
		System.err.println("------redis-----findLoginCache--" + accessToken);
		LoginCache ret = null;   	
    	ret = redisTemplate.opsForValue().get(KEY_PREFIX_USER+accessToken)==null
    			?null:(LoginCache)redisTemplate.opsForValue().get(KEY_PREFIX_USER+accessToken);
    	return ret;
	}
	
	@Override
	public LoginCache fetchLoginCache(String accessToken) {
		System.err.println("------redis-----fetchLoginCache--" + accessToken);
		LoginCache ret = null;
		User loginuser = fetchLoginUser(accessToken);
		if (loginuser!=null) {
			LogManager.getLogger().debug("loginuser id[{}], uuid[{}]:",loginuser.getId(),loginuser.getUuid());
			User localuser = userService.findByUuid(loginuser.getUuid());
			if (localuser!=null) {
				LogManager.getLogger().debug("localuser id[{}], uuid[{}]:",localuser.getId(),localuser.getUuid());
				ret = new LoginCache();
				ret.setUserId(localuser.getId());
				ret.setUserUuid(localuser.getUuid());
				ret.setUserName(localuser.getName());
				ret.setUserRealName(localuser.getRealName());
			}			
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
    	ret = redisTemplate.opsForValue().get(KEY_PREFIX_PERMISSIONS+accessToken)==null
    			?null:(Set<String>)redisTemplate.opsForValue().get(KEY_PREFIX_PERMISSIONS+accessToken);
    	return ret;
	}
		
	@Override
	public Set<String> fetchLoginUserPermissions(String accessToken) {
		System.err.println("-------redis----fetchLoginUserPermissions--" + accessToken);
		Set<String> ret = new HashSet<String>();
		User puser = fetchLoginUser(accessToken);
		if (puser==null) {
			return ret;
		}
		ret = authPermissionService.findUserPermissions(puser.getId(),puser.getName());
		redisTemplate.opsForValue().set(KEY_PREFIX_PERMISSIONS+accessToken, ret);
		return ret;
	}
	
	@Override
	public void clearLoginUserPermissions(String accessToken) {
		redisTemplate.delete(KEY_PREFIX_PERMISSIONS+accessToken);
		
	}
	@Override
	public void clear() {
		
		
	}

	@Override
	public User fetchLoginUser(String accessToken) {
		User ret = null;
		Result result = authFeignClient.fetchLoginUser(accessToken);
		if (ResultStatus.SUCCESS.code()==result.getCode()) {
			ret = ObjectMapperUtil.getObjectMapper()
					.convertValue(result.getData(),User.class);
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
