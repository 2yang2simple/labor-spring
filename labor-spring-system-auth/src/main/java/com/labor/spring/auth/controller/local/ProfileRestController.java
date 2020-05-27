package com.labor.spring.auth.controller.local;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.util.GoogleAuthenticator;
import com.labor.common.util.QRCodeUtil;
import com.labor.common.util.RSAUtil;
import com.labor.common.util.StringUtil;
import com.labor.spring.auth.service.AccessService;
import com.labor.spring.auth.service.LoginService;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.util.WebUtil;
import com.labor.spring.core.entity.FingerprintOnline;
import com.labor.spring.core.entity.User;


@RestController
@RequestMapping("/rest/profiles")
public class ProfileRestController extends AccessBaseRestController{

	@Autowired
	protected LoginService loginService;	
	
	public static void main(String args[]) {
		long now = System.currentTimeMillis();  
		Date lastUpdateDate = new Date();
		lastUpdateDate.setDate(25);
		long lastUpdateDateLongValue = 0;
		if (lastUpdateDate!=null) {
			lastUpdateDateLongValue = lastUpdateDate.getTime();
		}
		long gaptime = now-lastUpdateDateLongValue;
		System.err.println(now);
		System.err.println(lastUpdateDateLongValue);
		System.err.println(gaptime);
		if(gaptime>(long)60000) {
			//1分钟
			System.err.println("more than 1 min");
		}

	}
	private User getCurrentUser() {
		User ret = null;
		String accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
		if (StringUtil.isEmpty(accessToken)) {
			return ret;
		}
		FingerprintOnline fo = loginService.find(accessToken);
		if (fo!=null) {
			ret = userService.findById(fo.getUserId());
		}
		return ret;
	}
	@RequestMapping(value = {"/passwords"}, method = RequestMethod.POST)
	public Result updateUserPassword(
						HttpServletRequest request,
						HttpServletResponse response,
						@RequestBody HashMap<String, String> hm) {

		String pwdmodify = (String)hm.get("pwdmodify");
		String pwdencrypt = (String)hm.get("pwdencrypt");
		LogManager.getLogger().debug("pwdmodify:"+pwdmodify+"|pwdencrypt:"+pwdencrypt);
		if (StringUtil.isEmpty(pwdmodify)||StringUtil.isEmpty(pwdencrypt)){
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(), "password must not be empty."); 
		}
		User dbuser = userService.findByPwdmodify(pwdmodify);
		//user is not exist , return.
		if (dbuser==null) {
			return Result.failure(ResultStatus.FAILURE_DATA_NOT_FOUND.code(), "The URL is expired."); 
		} 
		//user exist , create a new password.	
		String pwd = RSAUtil.decrypt2StringByPrivateKey(pwdencrypt);
		if (StringUtil.isEmpty(pwd)) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(), "password must not be empty."); 
		}
		userService.createPassword(dbuser.getId(),pwd);
		return Result.success();

	}
	//this method is in danger
	@RequestMapping(value = {"/users/{account}/salt"}, method = RequestMethod.GET)
	public Result findUserPasswordSaltByAccount(
					@PathVariable(value="account") String account) {
		String ret = userService.createPasswordSaltByAccount(account);
		return Result.success(ret);
	}
    //return user inf by authcode 
  	@RequestMapping(value = {"/users"}, method = RequestMethod.GET)
  	public Result findUserByOnetimeCode(
  					HttpServletRequest request,
  					HttpServletResponse response,
  					@RequestParam(value="authcode", required=true) String authcode) {
  		User user = accessByOnetimeCode(authcode);
  		if (user!=null) {
  	  		LogManager.getLogger().info("access user:"+((user==null)?"null":user.getName())+"|IP:"+WebUtil.getIpAddress(request));
  		}
  		return Result.success(user);
  	}
	@RequestMapping(value = {"/users/{pwdmodify}"}, method = RequestMethod.GET)
	public Result findUserByPwdmodifyPassword(
						@PathVariable(value="pwdmodify") String pwdmodify) {
		User dbuser = userService.findByPwdmodify(pwdmodify);
		if (dbuser==null) {
			return Result.failure(ResultStatus.FAILURE_DATA_NOT_FOUND.code(), "The URL is expired."); 
		} 
		long now = System.currentTimeMillis();  
		Date lastUpdateDate = dbuser.getLastUpdateDate();
		long lastUpdateDateLongValue = 0;
		if (lastUpdateDate!=null) {
			lastUpdateDateLongValue = lastUpdateDate.getTime();
		}
		long gaptime = now-lastUpdateDateLongValue;
		if(gaptime>(long)6000000) {
			//100分钟
			LogManager.getLogger().debug("more than 100 mins");
			userService.updatePasswordModifyCode(dbuser.getId(), "");
			return Result.failure(ResultStatus.FAILURE_DATA_NOT_FOUND.code(),"The url is expired.");
		}
		
		return Result.success(dbuser);
	}
	@RequestMapping(value = {"/users/{pwdmodify}/auth-qr-code-url"}, method = RequestMethod.GET)
	public Result findUserQRUrlByPwdmodifyPassword(
						@PathVariable(value="pwdmodify") String pwdmodify) {
		User dbuser = userService.findByPwdmodify(pwdmodify);
		if(dbuser!=null) {
			String sk = userService.findSecretKey(dbuser.getUuid());
			if(!StringUtil.isEmpty(sk)) {
				return Result.success(GoogleAuthenticator.getQRBarcodeURL(dbuser.getSno(), StringUtil.safeToString(baseProperties.CONTEXT_PATH).replace("/", ""), sk));
			}
		}
		return Result.failure(ResultStatus.FAILURE_DATA_NOT_FOUND);
	}

	//
	@RequestMapping(value = {"/users/current"}, method = RequestMethod.GET)
	public Result findCurrentUser() {
		User ret = null;
		ret = getCurrentUser();
		return Result.success(ret);
	}

	@RequestMapping(value = {"/users/current"}, method = RequestMethod.PATCH)
	public Result updateCurrentUser(
					HttpServletRequest request,
					HttpServletResponse response,
					@RequestBody User puser) {
		User user = null;
		String userexistmsg = userService.checkExisted(puser);
		if (!"none".equals(userexistmsg)) {
			return Result.failure(ResultStatus.FAILURE_DATA_NOT_UNIQUE.code(), userexistmsg); 
		}
		User currentuser = getCurrentUser();
		if (currentuser!=null&&currentuser.getId()!=null&&(currentuser.getId().equals(puser.getId()))) {
			user = userService.update(puser);
		}
		return Result.success(user);
	}
	@RequestMapping(value = { "/users/current/qr-code-url"}, method = RequestMethod.GET)
	public Result findCurrentUserQRUrl(
					HttpServletRequest request,
					HttpServletResponse response) {

		User currentuser = getCurrentUser();
		if(currentuser!=null) {
			String sk = userService.findSecretKey(currentuser.getUuid());
			if(!StringUtil.isEmpty(sk)) {
				return Result.success(GoogleAuthenticator.getQRBarcodeURL(currentuser.getSno(), StringUtil.safeToString(baseProperties.CONTEXT_PATH).replace("/", ""), sk));
			}
		}
		return Result.failure(ResultStatus.FAILURE_DATA_NOT_FOUND);
	}

	@RequestMapping(value = {"/qr-code"}, method = RequestMethod.GET)
	public ResponseEntity<byte[]> createQrCode(
						@RequestParam(value = "content", required=false) String content) {
		ResponseEntity<byte[]> ret = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=qr");
		HttpStatus statusCode = HttpStatus.OK;
		ret = new ResponseEntity<byte[]>(QRCodeUtil.createImageBytes(content, 200, 200), headers, statusCode);
		return ret;
	}
  	
  	
  	
}
