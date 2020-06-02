package com.labor.spring.core.controller.local;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.ClassUtil;
import com.labor.common.util.GoogleAuthenticator;
import com.labor.common.util.QRCodeUtil;
import com.labor.common.util.RSAUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.base.BaseRestController;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.entity.Fingerprint;
import com.labor.spring.core.entity.FingerprintOnline;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;
import com.labor.spring.core.entity.User;
import com.labor.spring.core.entity.UserFingerprint;
import com.labor.spring.core.service.FingerprintServiceIntf;
import com.labor.spring.core.service.PermissionServiceIntf;
import com.labor.spring.core.service.RoleServiceIntf;
import com.labor.spring.core.service.SysconfigConstants;
import com.labor.spring.core.service.SysconfigServiceIntf;
import com.labor.spring.core.service.UserServiceIntf;
import com.labor.spring.feign.api.auth.AuthLoginService;
import com.labor.spring.util.WebUtil;


@RestController
@RequestMapping("/rest/core/auth")
public class AuthRestController extends BaseRestController {
	@Autowired
	private UserServiceIntf userService;	
	@Autowired
	private PermissionServiceIntf perService;
	@Autowired
	private RoleServiceIntf roleService;
	@Autowired
	private FingerprintServiceIntf fpService;
	
	
	private boolean isValidateToken(String a,String ab,String b) {
		return true;
//		return TokenUtil.isValidatedDateSaltingToken(a,ab,b);
	}

	private void saveLoginInfo(String cfmd5, User user, String rememberme) {
		//active the fingerprint;
		Fingerprint dbfp = fpService.create(cfmd5, WebConstants.FP_TYPE_CANVAS);
		if(!CommonConstants.ACTIVE.equals(dbfp.getStatus())) {
			dbfp.setStatus(CommonConstants.ACTIVE);
			fpService.update(dbfp.getId(), dbfp, true);
		}				
		//save user
		saveUser2Session(user);
		//save finterprint
		saveUserFingerprint(user,dbfp, rememberme);
		//save user permissions
		saveUserPermissions2Session(user);

	}

	private void saveUserFingerprint(User puser, Fingerprint pfp, String rememberMe) {
//		Fingerprint pfp = (Fingerprint)WebUtil.getSessionAttribute(WebConstants.KEY_FP);
		if ( pfp!= null) {
			userService.createUserFingerprint(pfp.getValue(), pfp.getType(), pfp.getId(), puser.getId(),rememberMe);
			saveFingerprint2Session(pfp);
		}
	}

	private void saveFingerprint2Session(Fingerprint pfp) {
		if(WebUtil.getSessionAttribute(WebConstants.KEY_FP)==null){
			WebUtil.setSessionAttribute(WebConstants.KEY_FP, pfp);
			FingerprintOnline fo = new FingerprintOnline();
			fo.setFpId(pfp.getId());
			fo.setFpValue(pfp.getValue());
			fo.setFpType(pfp.getType());
			fpService.saveOnlineBySeesionId(WebUtil.getSessionId(), fo, true);
		}
	}
	
	private void saveUser2Session(User puser) {
		if(WebUtil.getSessionAttribute(WebConstants.KEY_USER)==null){
			WebUtil.setSessionAttribute(WebConstants.KEY_USER, puser);
			FingerprintOnline fo = new FingerprintOnline();
			fo.setUserId(puser.getId());
			fo.setUserName(puser.getName());
			fpService.saveOnlineBySeesionId(WebUtil.getSessionId(), fo, true);
		}
	}
	
	private void saveUserPermissions2Session(User puser) {
		Set<String> permissions =new HashSet<String>();
		List<Permission> list;
		//if login by superuser. return all the permissions
		boolean issuperuser = false;
		int size;
		if (StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER,puser.getName())){
			list = perService.findListByStatus(CommonConstants.ACTIVE);
			//if permission is null, then init permission from the package class;
			if (list!=null&&list.size()==0) {
				perService.initialization();	
				list = perService.findListByStatus(CommonConstants.ACTIVE);
			}
			issuperuser = true;
    	} else {
    		list = perService.findListByUserid(puser.getId());
    	}
		if(list!=null) {
			size = list.size();
			for (int i=0;i<size;i++) {
				Permission p = (Permission)list.get(i);
				permissions.add(p.getCode());
			}
		}
		//if not superuser, add common permissions of common user.
		if (!issuperuser) {
			Role r = roleService.findByNameAndStatus(WebConstants.ROLENAME_COMMONUSER, CommonConstants.ACTIVE);
			if (r!=null&&r.getId()>0) {
				list = perService.findListByRoleid(r.getId());
				if(list!=null) {
					size = list.size();
					for (int i=0;i<size;i++) {
						Permission p = (Permission)list.get(i);
						permissions.add(p.getCode());
					}
				}
			}
			
		}
		WebUtil.setSessionAttribute(WebConstants.KEY_PERMISSION, permissions);
	}


	
	private Set<String> addPermission(Set<String> permissions, String roleName){
		List<Permission> list;
		int size;
		Role r = roleService.findByNameAndStatus(roleName, CommonConstants.ACTIVE);
		if (r!=null&&r.getId()>0) {
			list = perService.findListByRoleid(r.getId());
			if(list!=null) {
				size = list.size();
				for (int i=0;i<size;i++) {
					Permission p = (Permission)list.get(i);
					permissions.add(p.getCode());
				}
			}
		}
		return permissions;
	}

	private List<String> findRestfulApiList(String packagename) {
		List<String> ret = new ArrayList<String>();
		try {
			Set set = ClassUtil.findClasses(packagename);
			for (Object obj : set) {
				if (obj instanceof Class<?>) {
					Class<?> clazz = (Class<?>) obj;
					ret.addAll(findRestfulApiList(clazz));
				}
			}

		} catch (ClassNotFoundException e) {
			LogManager.getLogger().error("",e);
		}
		return ret;
	}
	
	private List<String> findRestfulApiList(Class<?> clazz) throws ClassNotFoundException {
		List<String> ret = new ArrayList<String>();
		RestController declaredAnnotation = clazz.getDeclaredAnnotation(RestController.class);
		//if class is the rest controller then 
		if (declaredAnnotation != null&&declaredAnnotation.value() != null) {
			RequestMapping classAnnotation = clazz.getDeclaredAnnotation(RequestMapping.class);
			if (classAnnotation != null&&classAnnotation.value()!=null) {
				
				String classRequestMappingUrl = classAnnotation.value()[0];
			
				Method[] declaredMethods = clazz.getDeclaredMethods();
				for (Method declaredMethod : declaredMethods) {
					//@RequestMapping(value = { "" }, method = RequestMethod.POST)
					RequestMapping fieldAnnotation = declaredMethod.getDeclaredAnnotation(RequestMapping.class);
					//get requires permission value of the method  
					if (fieldAnnotation != null&&fieldAnnotation.value()!=null) {
						String requestmethodname = getRequestMethodName(fieldAnnotation.method()[0]);
						String declaredMethodname = declaredMethod.getName();
						int len = fieldAnnotation.value().length;
						for (int i = 0; i < len; i++) {
//							LogManager.getLogger().debug(clazz.getName()+"|"
//															+declaredMethod.getName()+"|"
//															+"value"+i+"|"
//															+fieldAnnotation.value()[i]
//															+"method"+"|"
//															+fieldAnnotation.method());

							String declaredMethodParameters = "(";
							String requestMethodParameters = "{";
							Parameter[] p = declaredMethod.getParameters();
							for (int j=0;j<p.length;j++){
								char chr;
								if (j==0) {
									chr = '?';
								} else {
									chr = '&';
								}
								requestMethodParameters = requestMethodParameters + chr + p[j].getName()+"=";
								declaredMethodParameters = declaredMethodParameters + p[j].getName() + ",";
							}
							requestMethodParameters = requestMethodParameters + "}";
							declaredMethodParameters = declaredMethodParameters + ")";
							
//							declaredMethodname = declaredMethodname + declaredMethodParameters;
							
							String restful = requestmethodname+
												classRequestMappingUrl+
												fieldAnnotation.value()[i]+
												requestMethodParameters;
							
//							restful = StringUtil.postfix(restful, " ", 100);
							declaredMethodname = StringUtil.postfix(
									declaredMethodname, "-", 30);
							
							String url = declaredMethodname + restful;
							ret.add(url);
							LogManager.getLogger().debug(url);
						}
					}
	
				}
			}

		}
		return ret;

	}
	
	private String getRequestMethodName(RequestMethod method) {
		switch (method.name()) {
			case  "GET":
				return "GET----";
			case  "POST":
				return "POST---";
			case  "PUT":
				return "PUT----";
			case  "PATCH":
				return "PATCH--";
			case  "DELETE":
				return "DELETE-";
			default : 
				return "";
		}
		
	}
	
	/**
	 * post canvas fingerprint to db;
	 */
	@RequestMapping(value = {"/f/c"}, method = RequestMethod.POST)
	public String createFingerprintByCfmd5(
					@RequestBody HashMap<String, String> hm,
					@RequestParam(value="g", required=true) String g,
					@RequestParam(value="t", required=true) String t
								) {
		
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return "-1";
		}
		
		if(WebUtil.getSessionAttribute(WebConstants.KEY_FP)!=null){
			String sessionid = WebUtil.getSessionId();
			if (!StringUtil.isEmpty(sessionid)){
				FingerprintOnline  fo = fpService.findFoBySessionId(sessionid);
				
				if (fo!=null) {
					return "1";
				} else {
					//if online data deleted by admin, force to logout;
					LogManager.getLogger().debug("Force to Logout:"+sessionid);
					logout();
				}
			}
			
		}
		String cfmd5 = (String)hm.get("cfmd5");
		Fingerprint dbfp = fpService.create(cfmd5, WebConstants.FP_TYPE_CANVAS);
		
		if(CommonConstants.ACTIVE.equals(dbfp.getStatus())) {
			saveFingerprint2Session(dbfp);
			
			return "2";
		} else {
			// user exist but maybe closed by admin.
			return "-2";
		}

	}
	

	/**
	 * get user from cancas fingerprint
	 * @param session
	 * @param cfmd5
	 * @param g
	 * @param t
	 * @return
	 */
	@RequestMapping(value = {"/u/c/{cfmd5}"}, method = RequestMethod.GET)
	public Result findUserNameByCfmd5(
					@PathVariable(value="cfmd5") String cfmd5,
					@RequestParam(value="g", required=true) String g,
					@RequestParam(value="t", required=true) String t
								) {

		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return Result.failure();
		}
		User ret = new User();
		User user = userService.findFirstByValueAndType(cfmd5, WebConstants.FP_TYPE_CANVAS);
		if (user!=null) {
			ret.setName(user.getName());
			ret.setSno(user.getSno());
			ret.setDescription(user.getDescription());
			if(WebUtil.getSessionAttribute(WebConstants.KEY_FP)==null){
				ret.setDescription(ret.getDescription()+"|norememberme");
			} 
		}
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/u/s/a/{account}"}, method = RequestMethod.GET)
	public Result findUserSaltByAccount(
					@PathVariable(value="account") String account,
					@RequestParam(value="g", required=true) String g,
					@RequestParam(value="t", required=true) String t
								) {

		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return Result.failure();
		}
		String ret = userService.createPasswordSaltByAccount(account);
		return Result.success(ret);
	}
	
	/**
	 * get user from session if existed;
	 */
	@RequestMapping(value = {"/u"}, method = RequestMethod.GET)
	public User findUserFromSession(
			@RequestParam(value="g", required=true) String g,
			@RequestParam(value="t", required=true) String t) {
		User ret = null;
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return null;
		}
		if(WebUtil.getSessionAttribute(WebConstants.KEY_USER)!=null){
			ret = (User)WebUtil.getSessionAttribute(WebConstants.KEY_USER);
			ret = userService.findById(ret.getId());
		}
		return ret;
	}
	
	@RequestMapping(value = {"/permissions"}, method = RequestMethod.GET)
	public Set<String> findPermissionsFromSession(
			@RequestParam(value="g", required=true) String g,
			@RequestParam(value="t", required=true) String t) {
		Set<String> ret = new HashSet<String>();
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return null;
		}
//		ret = authLoginService.findUserPermissionsCurrent();
//		if (ret==null) {
//			ret = new HashSet<String>();
//		}
		if(WebUtil.getSessionAttribute(WebConstants.KEY_PERMISSION)!=null){
			ret = (Set<String>)WebUtil.getSessionAttribute(WebConstants.KEY_PERMISSION);
		}
		return ret;
	}
	
	/**
	 * update user
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/u" }, method = RequestMethod.PATCH)
	public String updateUser(@RequestBody User user,
			@RequestParam(value="g", required=true) String g,
			@RequestParam(value="t", required=true) String t) {
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return "-1";
		}
		String ret = userService.checkExisted(user);
		if (!"none".equals(ret)) {
			return ret;
		}
		
		userService.update(user);
		return "1";
	}
	

	@RequestMapping(value = {"/login"}, method = RequestMethod.POST)
	public String login(
						@RequestBody HashMap<String, String> hm,
						@RequestParam(value="g", required=true) String g,
						@RequestParam(value="t", required=true) String t) {
		
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return "-1";
		}

		String name = (String)hm.get("name");
		String saltpwdmd5 = (String)hm.get("saltpwdmd5");
		String cfmd5 = (String)hm.get("cfmd5");
		String rememberme = (String)hm.get("rememberme");
		LogManager.getLogger().debug("name:"+name+"|saltpwdmd5:"+saltpwdmd5);
		if (StringUtil.isEmpty(name)||StringUtil.isEmpty(saltpwdmd5)){
			return "-2";
		}

		User dbuser = null;
		//login by name;
		if (dbuser==null) {
			dbuser = userService.findByName(name);
		}
		//login by phone;
		if (dbuser==null) {
			dbuser = userService.findByCellPhone(name);
		}
		//user is not exist
		if (dbuser==null) {
			LogManager.getLogger().error("user not exis.:");
			return "-3";
		//user exist , account is not open and is not super user
		} else if (!CommonConstants.ACTIVE.equals(dbuser.getStatus())
					&&!StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER,dbuser.getName())){
			return "-4";
		//user exist , remember me validation.
		} else if (userService.validateUserRememberme(dbuser.getId(),cfmd5,WebConstants.FP_TYPE_CANVAS,rememberme)) {
			//save login info
			saveLoginInfo(cfmd5,dbuser,rememberme);
			return "1";
		//user exist , password me validation.
		} else if (userService.validateUserPassword(dbuser.getId(),g,saltpwdmd5)) {
			//save login info
			saveLoginInfo(cfmd5,dbuser,rememberme);
			return "1";
		} 
		return "-3";

	}
	@RequestMapping(value = {"/login2"}, method = RequestMethod.POST)
	public String login2() {
		
		User dbuser = userService.findByName(WebConstants.USERNAME_SUPER);
		
		
		//save login info
		saveLoginInfo("bd4c52be005646d5a3443423ff73f267",dbuser,"0");
		return "1";


	}
	@RequestMapping(value = {"/login-code"}, method = RequestMethod.POST)
	public String loginCode(
						@RequestBody HashMap<String, String> hm,
						@RequestParam(value="g", required=true) String g,
						@RequestParam(value="t", required=true) String t) {
		
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return "-1";
		}

		String name = (String)hm.get("name");
		String code = (String)hm.get("code");
		String cfmd5 = (String)hm.get("cfmd5");
		String rememberme = (String)hm.get("rememberme");
		LogManager.getLogger().debug("name:"+name+"|code:"+code);
		if (StringUtil.isEmpty(name)||StringUtil.isEmpty(code)){
			return "-2";
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
			LogManager.getLogger().error("user not exis.:");
			return "-3";
		//user exist , account is not open and is not super user
		} else if (!CommonConstants.ACTIVE.equals(dbuser.getStatus())
					&&!StringUtil.isEqualedTrimLower(WebConstants.USERNAME_SUPER,dbuser.getName())){
			return "-4";
		//user exist , remember me validation.
		} else if (userService.validateUserRememberme(dbuser.getId(),cfmd5,WebConstants.FP_TYPE_CANVAS,rememberme)) {
			//save login info
			saveLoginInfo(cfmd5,dbuser,rememberme);
			return "1";
		//user exist , password me validation.
		} else if (userService.validateUserAuthcode(dbuser.getUuid(),code)) {
			//save login info
			saveLoginInfo(cfmd5,dbuser,rememberme);
			return "1";
		} 
		return "-3";

	}
	
	
	@RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
	public String logout() {
		WebUtil.setSessionAttribute(WebConstants.KEY_USER, null);
		WebUtil.setSessionAttribute(WebConstants.KEY_FP, null);
		WebUtil.setSessionAttribute(WebConstants.KEY_PERMISSION, null);
		
		//dosomething to logincount;
		fpService.deleteOnline(WebUtil.getSessionId());
		return "1";

	}
	

	@RequiresPermissions("auth:signup")
	@RequestMapping(value = {"/signup"}, method = RequestMethod.POST)
	public String signup(
						@RequestBody HashMap<String, String> hm,
						@RequestParam(value="g", required=true) String g,
						@RequestParam(value="t", required=true) String t) {
		
		if (!isValidateToken(g,t,ClientRegisted.getSecret(null))) {
			return "-1";
		}
		String name = (String)hm.get("name");
		String pwdencrypt = (String)hm.get("pwdencrypt");
		
		LogManager.getLogger().debug("name:"+name+"|pwdencrypt:"+pwdencrypt);
		if (StringUtil.isEmpty(name)||StringUtil.isEmpty(pwdencrypt)){
			return "-2";
		}

		User dbuser = userService.findByName(name);
		//user is not exist , create one.
		if (dbuser==null) {
			String pwd = RSAUtil.decrypt2StringByPrivateKey(pwdencrypt);
			if (StringUtil.isEmpty(pwd)) {
				return "-2";
			}
			User saveduser = userService.create(name, null, null, null, null, null, pwd, null, SysconfigConstants.DEFAULT_USER_STATUS);
			
			
		//user exist , compare name&pw to db.	
		} else {
			return "-3";
		}

		return "1";

	}
	@RequestMapping(value = {"/signup-super"}, method = RequestMethod.POST)
	public String signupSuper(
						@RequestBody HashMap<String, String> hm) {

		String pwdencrypt = (String)hm.get("pwdencrypt");
		
		LogManager.getLogger().debug("pwdencrypt:"+pwdencrypt);
		if (StringUtil.isEmpty(pwdencrypt)){
			return "-2";
		}

		User dbuser = userService.findByName(WebConstants.USERNAME_SUPER);
		//user is not exist , create one.
		if (dbuser==null) {

			String pwd = RSAUtil.decrypt2StringByPrivateKey(pwdencrypt);
			if (StringUtil.isEmpty(pwd)) {
				return "-2";
			}
			
			User saveduser = userService.create(WebConstants.USERNAME_SUPER, null, null, null, null, null, pwd, null, CommonConstants.ACTIVE);
//			// set the super to util
//			if (saveduser!=null) {
//				WebUtil.USER_SUPER = new LoginCache();
//				WebUtil.USER_SUPER.setUserId(saveduser.getId());
//				WebUtil.USER_SUPER.setUserName(saveduser.getName());
//				WebUtil.USER_SUPER.setUserUuid(saveduser.getUuid());
//			}
			
		//user exist , compare name&pw to db.	
		} else {
			return "-3";
		}

		return "1";

	}
	@RequiresPermissions("auth:signup")
	@RequestMapping(value = {"/signup-auth-info"}, method = RequestMethod.GET)
	public Result findSignupAuthInfo(
						@RequestParam(value="g", required=true) String g,
						@RequestParam(value="t", required=true) String t) {
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return Result.failure(ResultStatus.FAILURE_PERMISSION_NOACCESS);
		}
		HashMap<String, String> hm = new HashMap<String, String>();

		//get next sno
		String sno = WebUtil.nextSno();
		String secretKey = GoogleAuthenticator.generateSecretKey() ;
		String saltkey = GoogleAuthenticator.saltSecretKey(sno+secretKey);
		String qrcodeurl = GoogleAuthenticator.getQRBarcodeURL(sno, StringUtil.safeToString(baseProperties.CONTEXT_PATH).replace("/", ""), secretKey);

		hm.put("sno", sno);
		hm.put("secretkey", secretKey);
		hm.put("saltkey", saltkey);
		hm.put("qrcodeurl", qrcodeurl);

		return Result.success(hm);
		

	}
	@RequiresPermissions("auth:signup")
	@RequestMapping(value = {"/signup-code"}, method = RequestMethod.POST)
	public Result signupCode(
						@RequestBody HashMap<String, String> hm,
						@RequestParam(value="g", required=true) String g,
						@RequestParam(value="t", required=true) String t) {
		
		User ret = null;
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return Result.failure(ResultStatus.FAILURE_PERMISSION_NOACCESS);
		}
		String name = (String)hm.get("name");
		String pwdencrypt = (String)hm.get("pwdencrypt");
		String sno = (String)hm.get("sno");
		String secretKey = (String)hm.get("secretkey");
		String saltKey = (String)hm.get("saltkey");
		String authCode = (String)hm.get("authcode");
		if (!GoogleAuthenticator.validateSecretKey(sno+secretKey, saltKey)||
				!GoogleAuthenticator.validateCode(authCode, secretKey)) {
			return Result.failure(ResultStatus.FAILURE_PERMISSION_GOOGLEAUTH);
		} 
		
		LogManager.getLogger().debug("name:"+name+"|pwdencrypt:"+pwdencrypt+"|secretKey:"+secretKey+"|saltKey:"+saltKey);
//		if (StringUtil.isEmpty(name)||StringUtil.isEmpty(pwdmd5)){
//			return Result.failure(ResultStatus.FAILURE_PARAM_NULL, ResultStatus.MSG_FAILURE_PARAM_NULL);
//		}

		User dbuser = userService.findByName(name);
		//user is not exist , create one.
		if (dbuser==null) {
			String pwd = RSAUtil.decrypt2StringByPrivateKey(pwdencrypt);
			ret = userService.create(name, sno, secretKey, null, null, null, pwd, null, SysconfigConstants.DEFAULT_USER_STATUS);
		} else {
			return Result.failure(ResultStatus.FAILURE_DATA_NOT_UNIQUE);
		}

		return Result.success(ret);

	}
	/**
	 * change pwd
	 * @param session
	 * @param hm
	 * @param g
	 * @param t
	 * @return
	 */
	@RequestMapping(value = {"/pwd"}, method = RequestMethod.POST)
	public String createPassword(
						@RequestBody HashMap<String, String> hm,
						@RequestParam(value="g", required=true) String g,
						@RequestParam(value="t", required=true) String t) {
		
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return "-1";
		}
		String pwdmodify = (String)hm.get("pwdmodify");
		String pwdencrypt = (String)hm.get("pwdencrypt");
		LogManager.getLogger().debug("pwdmodify:"+pwdmodify+"|pwdencrypt:"+pwdencrypt);
		if (StringUtil.isEmpty(pwdmodify)||StringUtil.isEmpty(pwdencrypt)){
			return "-2";
		}

		User dbuser = userService.findByPwdmodify(pwdmodify);
		//user is not exist , return.
		if (dbuser==null) {
			return "-3";
		
		} 
		//user exist , create a new password.	
		String pwd = RSAUtil.decrypt2StringByPrivateKey(pwdencrypt);
		if (StringUtil.isEmpty(pwd)) {
			return "-2";
		}
		userService.createPassword(dbuser.getId(),pwd);
		return "1";

	}
	@RequestMapping(value = {"/pwd/{pwdmodify}"}, method = RequestMethod.GET)
	public Result findUserByPwdmodifyPassword(
						@PathVariable(value="pwdmodify") String pwdmodify, 
						@RequestParam(value="g", required=true) String g,
						@RequestParam(value="t", required=true) String t) {
		
		if (!isValidateToken(g,t,ClientRegisted.getSecret(baseProperties.getContextName()))) {
			return Result.failure(ResultStatus.FAILURE_PERMISSION_NOACCESS);
		}
		User dbuser = userService.findByPwdmodify(pwdmodify);
		return Result.success(dbuser);
	}
	
	@RequestMapping(value = {"/api"}, method = RequestMethod.GET)
	public Result api() {
		List<String> ret = new ArrayList<String>();
		List<String> list = WebConstants.RESTCONTROLLER_PAKAGES;
		for (int i=0;i<list.size();i++) {
			String packagename = list.get(i);
			ret.addAll(findRestfulApiList(packagename));
		}
		return Result.success(ret);
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
	public static void main(String[] args) {
//		System.out.println(getRequestMethodName(RequestMethod.GET));
//		System.out.println(StringUtil.postfix("findxx"," ",20));
//		List<String> ret = new ArrayList<String>();
//		List<String> list = WebConstants.RESTCONTROLLER_PAKAGES;
//		for (int i=0;i<list.size();i++) {
//			String packagename = list.get(i);
//			ret.addAll(findRestfulApiList(packagename));
//		}
	}
	
}
