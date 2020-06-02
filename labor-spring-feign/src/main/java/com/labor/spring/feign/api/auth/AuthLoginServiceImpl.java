package com.labor.spring.feign.api.auth;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labor.common.util.StringUtil;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.feign.api.auth.AuthCacheService;
import com.labor.spring.feign.api.auth.AuthLoginService;
import com.labor.spring.util.WebUtil;


@Service
@Primary
public class AuthLoginServiceImpl implements AuthLoginService{

	@Autowired
	private AuthCacheService authCacheService;	

	
	@Override
	public UserVO findLoginCache(String accessToken){
		UserVO ret = null;
		if (StringUtil.isEmpty(accessToken)) {
			return ret;
		}
		ret = authCacheService.findLoginCache(accessToken);
		if (ret==null) {
			ret = authCacheService.fetchLoginCache(accessToken);
		}
		return ret;
	}
	
	@Override
	public UserVO findLoginCacheCurrent() {
		UserVO ret = null;
		String accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
		ret = findLoginCache(accessToken);
		return ret;
	}
	
	@Override
	public UserVO getLoginCacheCurrent() {
		UserVO ret = null;
		String accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
		if (StringUtil.isEmpty(accessToken)) {
			return ret;
		}
		ret = authCacheService.findLoginCache(accessToken);
		return ret;
	}

	@Override
	public UserVO fetchLoginCache(String accessToken){
		UserVO ret = null;
		if (StringUtil.isEmpty(accessToken)) {
			return ret;
		}
		ret = authCacheService.fetchLoginCache(accessToken);
		return ret;
	}
	
	@Override
	public UserVO fetchLoginCacheCurrent() {
		UserVO ret = null;
		String accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
		ret = fetchLoginCache(accessToken);
		return ret;
	}
	
	@Override
	public Set<String> findUserPermissionsCurrent(){
		Set<String> ret = null;
		String accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
		if (StringUtil.isEmpty(accessToken)) {
			return ret;
		}
		ret = authCacheService.findLoginUserPermissions(accessToken);
		if (ret==null) {
			ret = authCacheService.fetchLoginUserPermissions(accessToken);
		}
		return ret;
	}

	@Override
	public Set<String> fetchUserPermissionsCurrent(){
		Set<String> ret = null;
		String accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
		if (StringUtil.isEmpty(accessToken)) {
			return ret;
		}
		ret = authCacheService.fetchLoginUserPermissions(accessToken);
		return ret;
	}



//	@Override
//	public User findUser(String accessToken) {
//		LoginCache lc = findLoginCache(accessToken);
//		if (lc==null) {
//			return null;
//		}
//		return userService.findByUuid(lc.getUserUuid());
//	}
	
//	//fetch user and refresh local user;
//	@Override
//	@Transactional
//	public User fetchUser(String accessToken) {
//		User ret = null;
//		User remoteuser = null;
//		remoteuser = authCacheService.fetchLoginUser(accessToken);
//		if (remoteuser==null) {
//			return null;
//		}
//		//update local user info;
//		User localuser = userService.findByUuid(remoteuser.getUuid());
//		
//		if (localuser!=null){
//			remoteuser.setId(localuser.getId());		
//			BeanUtils.copyProperties(remoteuser,localuser,IgnorePropertiesUtil.getNullPropertyNames(remoteuser));
//			LogManager.getLogger().info("updated a local user");
//		} else {		
//			//create a local user;
//			localuser = new User();
//			localuser.setId(remoteuser.getId()); 
//			localuser.setUuid(remoteuser.getUuid());
//			localuser.setSno(remoteuser.getSno());
//			localuser.setName(remoteuser.getName());
//			localuser.setWeixin(remoteuser.getWeixin());
//			localuser.setCellPhone(remoteuser.getCellPhone());
//			localuser.setEmail(remoteuser.getEmail());
//			localuser.setRealName(remoteuser.getRealName());
//			localuser.setRealNameEn(remoteuser.getRealNameEn());
//			localuser.setDescription(remoteuser.getDescription());
//			localuser.setStatus(remoteuser.getStatus());
//			localuser.setPwdmodify("");
//			localuser.setGoogleSecretKey("");
//
//			LogManager.getLogger().info("created a local user");
//		}
//		//local user no need to be unique; save directly
//		ret = userService.save(localuser);
//		return ret;
//	}
	
	@Override
	@Transactional
	public String fetchUserToken(String type,String code) {
		String ret = null;
		ret = authCacheService.fetchLoginUserToken(type,code);
		return ret;
	}
	
	@Override
	@Transactional
	public String create(
					String clientKey,
					String clientUuid,
					String type,
					String code,
					String name) {
		//return a token key saved in the cache;

		LogManager.getLogger().debug("******create*****");
		
		String ret = null;
		ret = authCacheService.createLogin(
					clientKey,
					clientUuid,
					type,
					code,
					name);
		
		return ret;
	}
	
	@Override
	@Transactional
	public void delete(String accessToken) {
		authCacheService.clearLoginCache(accessToken);
		authCacheService.clearLoginUserPermissions(accessToken);
		authCacheService.deleteLogin(accessToken);
	}
	


	@Override
	public void deleteCache(){
		authCacheService.clear();
	}

	@Override
	public String getTokenCache(String key) {
		String ret = authCacheService.getToken(key);
		authCacheService.clearToken(key);
		return ret;
	}

	@Override
	public String setTokenCache(String key, String token) {
		return authCacheService.setToken(key,token);
	}

	@Override
	public boolean isCurrentUserOrSuperUser(Long userid, String useruuid) {
		if (userid==null&&useruuid==null) {
			return false;
		}
		UserVO lc = findLoginCacheCurrent();
		if (lc!=null) {
			//current user is super user;
			if(StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER,lc.getUserName())){
				return true;
			}
			//userid is current user;
			if(userid!=null&&userid.equals(lc.getUserId())) {
				return true;
			}
			//uuid is current user;
			if(useruuid!=null&&StringUtil.isEqualedTrimLower(lc.getUserUuid(),useruuid)) {
				return true;
			}
		}
		return false;
	}
}
