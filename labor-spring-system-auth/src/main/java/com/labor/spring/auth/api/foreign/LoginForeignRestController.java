package com.labor.spring.auth.api.foreign;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.labor.common.exception.ParameterException;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.auth.service.LoginService;
import com.labor.spring.bean.ClientInfo;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.entity.FingerprintOnline;
import com.labor.spring.core.entity.User;
import com.labor.spring.feign.auth.AuthConstants;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/rest/foreign/logins")
public class LoginForeignRestController {

	@Autowired
	protected LoginService loginService;	

	
	@ApiOperation("Login a user")
	@ApiImplicitParams({
	@ApiImplicitParam(name="client-key",value="client name",dataType="String", paramType = "query"),
	@ApiImplicitParam(name="client-uuid",value="client uuid",dataType="String", paramType = "query"),
	@ApiImplicitParam(name="type",value="authetication type: sms/weixin",dataType="String", paramType = "query"),
	@ApiImplicitParam(name="code",value="sms code or weiixn passcode",dataType="String", paramType = "query"),
	@ApiImplicitParam(name="name",value="sms or weixin name",dataType="String", paramType = "query")})
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestParam(value="client-key", required=true)String clientKey,
					@RequestParam(value="client-uuid", required=true)String clientUuid,
					@RequestParam(value="type", required=true)String type,
					@RequestParam(value="code", required=true)String code,
					@RequestParam(value="name", required=true)String name) {
		
		LogManager.getLogger().debug("--auth-login---"+name);
		
		String ret = null;
		
		ClientInfo ci = ClientRegisted.getClientInfo(clientKey);
		if (ci==null) {
			throw new ParameterException("client not registed.");
		}

		FingerprintOnline fo = null;
		if (StringUtil.isEqualedTrimLower(type, AuthConstants.LOGINTYPE_CELLPHONE)) {
			//name is the cellphone number
			//code is the number sent to cellphone, also stored in db;
			//validate the code and name
			if(!StringUtil.isNumeric(name)){
				return Result.failure(ResultStatus.FAILURE_PARAM_INVALID);
			}
			//create a account by cellphone
			fo = loginService.create(ci.getFpType(), clientUuid, ci.getAuthType(), TokenUtil.generateUUID(), "cellphone", name);
		
			
			
    	} else if (StringUtil.isEqualedTrimLower(type, AuthConstants.LOGINTYPE_WEIXIN)) {
    		//name is the weixin number
    		
    		//code is the sessionkey, openid etc something weixin returned
    		
    		//validate the code and name
    	}  else if (StringUtil.isEqualedTrimLower(type, AuthConstants.LOGINTYPE_EMAIL)) {
    		//name is the email
    		
    		//code is the code received by email
    		
    		//validate the code and name
    	} else {
    		return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(),"type error.");
    	}
		
		//callback to save token on consumers
		if (fo!=null&&!StringUtil.isEmpty(fo.getAuthCode())) {
			LogManager.getLogger().debug("--auth-login---"+3);
			
			//administrator can not login on the app;
			if (StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER,fo.getUserName())
					&&!StringUtil.isEqualedTrimLower(WebConstants.AUTH_TYPE_SAVEDIN_COOKIES,ci.getAuthType())){
				loginService.delete(fo.getAuthValue());
				LogManager.getLogger().trace("Administrator can only be logged on from the WEB client. [{}]",fo.getAuthValue());
				return Result.failure(ResultStatus.FAILURE_LOGIN_ADMIN_FORBID);
			}
			
			RestTemplate restT = new RestTemplate();     
	        String url = ci.getCallback4AccessToken() + "?auth-code=" + fo.getAuthCode();
	        url = TokenUtil.addTimestampTokenUrl(url,ci.getSecret());
	        Result result = restT.postForObject(url, null, Result.class);
	        
	        LogManager.getLogger().debug("--auth-login---"+url);
	        if (result.getCode() == ResultStatus.SUCCESS.code()) {			
	        	String accessTokenKey = (String) result.getData();
	        	LogManager.getLogger().debug("--auth-login---"+accessTokenKey);
	        	if (!StringUtil.isEmpty(accessTokenKey)) {
	        		return Result.success(accessTokenKey);
	        	}
	        }
	        LogManager.getLogger().debug("--auth-login---"+6);
		}
		//return auth-code
//		if (fo!=null&&!StringUtil.isEmpty(fo.getAuthCode())) {
//	        ret =  fo.getAuthCode();
//		}
		
		return Result.success(ret);
	}
	
	@ApiOperation("Loggout a user")
	@ApiImplicitParam(name="code",value="access-token",dataType="String", paramType = "query")
	@RequestMapping(value = { "" }, method = RequestMethod.DELETE)
	public void delete(
					@RequestParam(value = "code", required = true) String code) {
		loginService.delete(code);
	}
	
	@ApiOperation("Fetch a user")
	@ApiImplicitParam(name="code",value="access-token",dataType="String", paramType = "query")
  	@RequestMapping(value = {"/users"}, method = RequestMethod.GET)
  	public Result fetchUser(
  					@RequestParam(value = "code", required = true) String code) {
  		User ret = null;
  		ret = loginService.findUserByAuthValue(code);
  		if (ret!=null) {
  			ret.setPwdmodify(null);
  			ret.setGoogleSecretKey(null);
  		}
  		return Result.success(ret);
  	}
  	
	@ApiOperation("Fetch a user access-token")
	@ApiImplicitParams({
	@ApiImplicitParam(name="type",value="request-token,refresh-token",dataType="String", paramType = "query"),
	@ApiImplicitParam(name="code",value="auth-code,access-token",dataType="String", paramType = "query")})
	@RequestMapping(value = { "/users/tokens" }, method = RequestMethod.POST)
	public Result fetchUserToken(
					@RequestParam(value = "type", required = true) String type,
					@RequestParam(value = "code", required = true) String code) {

		String ret = null;
		FingerprintOnline fo = null;
		if (StringUtil.isEqualedTrimLower(type, "request-token")) {
			fo = loginService.requestAuthValue(code);
			if (fo != null) {
				ret = fo.getAuthValue();
			}
			
		} else if (StringUtil.isEqualedTrimLower(type, "refresh-token")) {
			fo = loginService.refreshAuthValue(code);
			if (fo != null) {
				ret = fo.getAuthValue();
			}
		} 
		
		return Result.success(ret);
	}
	
	
}
