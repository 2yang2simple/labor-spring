//package com.labor.spring.config;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.subject.Subject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.labor.spring.auth.AuthLoginService;
//import com.labor.spring.common.WebConstants;
//import com.labor.spring.common.WebUtil;
//import com.labor.spring.service.core.fingerprint.FingerprintServiceIntf;
//import com.labor.spring.service.core.permission.PermissionServiceIntf;
//
//
///****
// * doGetAuthenticationInfo is NOT used, see LoginInterceptor;
// * 
// * doGetAuthorizationInfo is used for permission.
// */
//
//@Service  
//@Transactional 
//public class ShiroAuthorizingRealm extends AuthorizingRealm {
//	
//	@Autowired
//	@Lazy //solve the cacheable failed problem.
//	private AuthLoginService authAccountService;
//
//	/*******
//	 * 
//	 * not in used.
//	 * 
//	 * login forever;
//	 * 
//	 */
//	@Override
//	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) 
//						throws AuthenticationException {
//		SimpleAuthenticationInfo ret = null;
//		LogManager.getLogger().debug("XXXXXXXXXXXXAuthen");
//
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//
//		if (token.getUsername()!=null) {
//			LogManager.getLogger().debug("XXXXXXXXXXXXAuthen:getPrincipal:"
//										+token.getPrincipal()
//										+"|getCredentials:"+token.getCredentials()
//										+"|getName:"+getName());
//	        ret = new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
//	
//		} 
//		
//		return ret;
//	}
//
//	@Override
//	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		SimpleAuthorizationInfo ret = new SimpleAuthorizationInfo();
//
//		Set<String> permissions;
//		permissions = authAccountService.findUserPermissionsCurrent(false);
//		
//		if (permissions==null) {
//			LogManager.getLogger().debug("doGetAuthorizationInfo:");
//			permissions = new HashSet<String>();
//		} else {
//			LogManager.getLogger().debug("doGetAuthorizationInfo:"+permissions.size());
//		}
//		
////		if(WebUtil.getSessionAttribute(WebConstants.KEY_PERMISSION)!=null){
////			permissions = (Set<String>)WebUtil.getSessionAttribute(WebConstants.KEY_PERMISSION);
////			LogManager.getLogger().debug("XXXXXXXXXXXAuthorInSession:"+permissions.size());
////		} else {
////			permissions =new HashSet<String>();
//////			List<Permission> list 
//////				= perService.findByUserid(Integer.valueOf(principals.getPrimaryPrincipal().toString()));
//////			int size = list.size();
//////			for (int i=0;i<size;i++) {
//////				Permission p = (Permission)list.get(i);
//////				permissions.add(p.getCode());
//////			}
//////			session.setAttribute(CommonConstants.SESSION_KEY_PERMISSION, permissions);
//////			LogManager.getLogger().debug("XXXXXXXXXXXAuthorFetchDB:"+permissions.size());
////		}
//		
//		ret.setStringPermissions(permissions);
//		
//		return ret;
//	}
//
//
//}
