package com.labor.spring.feign.auth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.base.BaseRestController;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.bean.LoginCache;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.api.permission.PermissionServiceIntf;
import com.labor.spring.core.api.role.RoleServiceIntf;
import com.labor.spring.core.api.user.UserServiceIntf;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;
import com.labor.spring.core.entity.User;
import com.labor.spring.feign.auth.AuthCacheService;
import com.labor.spring.feign.auth.AuthLoginService;
import com.labor.spring.feign.client.auth.AuthFeignClient;
import com.labor.spring.util.IgnorePropertiesUtil;
import com.labor.spring.util.WebUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/feign/auth/logins")
public class AuthLoginRestController extends BaseRestController{

	@Autowired
	private AuthLoginService authLoginService;

	/*********************
	 * for client
	 */
	@ApiOperation("login by cellphone")
	@RequestMapping(value = {"/cellphone"}, method = RequestMethod.POST)
	public Result createByCellPhone(
					@RequestParam(value="client-key", required=false)String clientKey,
					@RequestParam(value="client-uuid", required=true)String clientUuid,
					@RequestParam(value="name", required=true)String name,
					@RequestParam(value="code", required=true)String code
					
			) {
		HashMap<String, String> ret = null;
		
//		String clientKey = ClientRegisted.CLIENTKEY_DEFAULT;
//		String clientUuid = "d10e0baa65b43c58d477adcf89e0798a";
//		String type = "sms";
//		String code = "123456";
//		String name = "17655554344";
		String authCode = null;
		String accessToken = null;
		LogManager.getLogger().debug("******create*****");
		if (StringUtil.isEmpty(clientKey)) {
			clientKey = baseProperties.getContextName();
		}
		String key = authLoginService.create(clientKey, clientUuid, AuthConstants.LOGINTYPE_CELLPHONE, code, name);

		LogManager.getLogger().debug("******create*****[{}]",key);
		accessToken = authLoginService.getTokenCache(key);
		LogManager.getLogger().debug("get access token from cache key[{}] and token[{}]",key,accessToken);
		
		if (StringUtil.isEmpty(accessToken)){
			return Result.failure(ResultStatus.FAILURE_LOGIN_ACCOUNT_NOTEXIST); 
		}
		
		ret = save2cookie(accessToken);
		
		return Result.success(ret);
	}
	
	@ApiOperation("loggout")
	@RequestMapping(value = {""}, method = RequestMethod.DELETE)
	public Result delete() {
		String accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
		if (StringUtil.isEmpty(accessToken)) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL); 
		}
		authLoginService.delete(accessToken);

		WebUtil.doLogoutShiro();
		
		WebUtil.setCookie(WebConstants.KEY_ACCESSTOKEN, "loggout",0);
		
		return Result.success();
	}
	
	
	/*********************
	 * for common use
	 */

	private HashMap<String, String> save2cookie(String accessToken) {
		HashMap<String, String> ret = null;

		LoginCache lc = authLoginService.findLoginCache(accessToken);
		if (lc==null) {
			return ret; 
		}
		
		//shiro doGetAuthenticationInfo login
		WebUtil.doLoginShiro(accessToken);
		
		//save to cookie
//		String sessionid = WebUtil.getSessionId();
		//subject.login will save sessionid to cookie
//		WebUtil.setCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME, sessionid);	
		
		if (StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER, lc.getUserName())){
			//super user cookie expired in 10 minutes.
    		WebUtil.setCookie(WebConstants.KEY_ACCESSTOKEN, accessToken,600);
		} else {
    		WebUtil.setCookie(WebConstants.KEY_ACCESSTOKEN, accessToken);
		}	
		
		//return to response
		ret = new HashMap<String, String>();
		ret.put(WebConstants.KEY_ACCESSTOKEN, accessToken);
//		ret.put(ShiroHttpSession.DEFAULT_SESSION_ID_NAME, sessionid);
		
		LogManager.getLogger().debug("login return query value: [{}]", 
//															ShiroHttpSession.DEFAULT_SESSION_ID_NAME + "=" + sessionid + "&"+
																	 WebConstants.KEY_ACCESSTOKEN + "=" + accessToken);
		return ret;
	}
	
	
	@ApiOperation("callback url registed in auth return a key for access token")
	@ApiImplicitParam(name="auth-code",value="auth-code",dataType="String", paramType = "path")
	@RequestMapping(value = {"/users/tokens/keys"}, method = RequestMethod.POST)
	public Result requestUserTokenKey(
					@RequestParam(value="auth-code") String authCode) {
	
		String ret = null;
		String accessToken = authLoginService.fetchUserToken("request-token",authCode);
		if (StringUtil.isEmpty(accessToken)){
			return null;
		}
		User user = authLoginService.fetchUser(accessToken);
		
		if (user!=null) {		
			//save accessToken to cache;
			ret = TokenUtil.generateUUID();
			authLoginService.setTokenCache(ret, accessToken);
			LogManager.getLogger().debug("save access token to cache key[{}] and token[{}]",ret,accessToken);
			return Result.success(ret);
		} else {
			return Result.failure(ResultStatus.FAILURE_LOGIN_ACCOUNT_NOTEXIST);
		}
		
	}
	
	
//	@RequestMapping(value = {"/users"}, method = RequestMethod.PUT)
//	public Result refreshUser(
//					@RequestParam(value = "access-token", required = false) String accessToken) {
//		User ret = null;
//		if (StringUtil.isEmpty(accessToken)) {
//			accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
//		}
//		if (StringUtil.isEmpty(accessToken)) {
//			return Result.failure(ResultStatus.FAILURE_PARAM_NULL); 
//		}
//		User remoteuser = authLoginService.fetchUser(accessToken);
//		if (remoteuser==null) {
//			return null;
//		}
//		//update local user info;
//		User localuser = userService.findByName(remoteuser.getName());
//		
//		if (localuser!=null){
//			remoteuser.setId(localuser.getId());		
//			BeanUtils.copyProperties(remoteuser,localuser,IgnorePropertiesUtil.getNullPropertyNames(remoteuser));
//
//			ret = userService.update(localuser);
//		} else {		
//
//			ret = userService.create(remoteuser.getName(), remoteuser.getSno(), "", remoteuser.getCellPhone(), remoteuser.getWeixin(), remoteuser.getEmail(), "", remoteuser.getUuid(), CommonConstants.ACTIVE);
//		}
//
//        return Result.success(ret);
//	}
	
	@RequestMapping(value = {"/users/current"}, method = RequestMethod.GET)
	public Result findUserCurrent(
					@RequestParam(value="fetch", required=false) String fetch) {
		LoginCache ret = null;
		if (StringUtil.isEqualedTrimLower("true", fetch)) {
			ret = authLoginService.fetchLoginCacheCurrent();
		} else {
			ret = authLoginService.findLoginCacheCurrent();
		}
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/users/permissions/current"}, method = RequestMethod.GET)
	public Result findUserPermissionsCurrent(
					@RequestParam(value="fetch", required=false) String fetch) {
		Set<String> ret = null;
		
		if (StringUtil.isEqualedTrimLower("true", fetch)) {
			ret = authLoginService.fetchUserPermissionsCurrent();
		} else {
			ret = authLoginService.findUserPermissionsCurrent();
		}
		return Result.success(ret);
		
	}
	
	@RequestMapping(value = {"/cache"}, method = RequestMethod.DELETE)
	public Result deleteCache() {
		authLoginService.deleteCache();
		return Result.success();
	}
	

}
