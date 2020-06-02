package com.labor.spring.feign.api.auth;

import java.lang.reflect.Method;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.labor.common.util.StringUtil;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.feign.api.auth.AuthLoginService;

@Aspect
@Component
public class RequiresPermissionsAspec {
	
	@Autowired
	private AuthLoginService authLoginService;
	
//	@Pointcut("@annotation(com.labor.spring.aop.annotation.LaborRequiresPermissions)")
//    public void laborRequiresPermissions(){
//		
//	}
	
	@Pointcut("@annotation(org.apache.shiro.authz.annotation.RequiresPermissions)")
    public void requiresPermissions(){
		
	}
	

	@Before("requiresPermissions()")
    public void Before(JoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresPermissions rp = method.getAnnotation(RequiresPermissions.class);
        String[] permsNeed = rp.value();
        Logical permsNeedLogical = rp.logical();
        		
        if (permsNeed==null||permsNeed.length==0) {
        	return ;
        }
        
		Set<String> permissions;
		permissions = authLoginService.findUserPermissionsCurrent();
		StringBuffer permsHad = new StringBuffer();
		if (permissions==null||permissions.size()==0) {
			throw new RequiresPermissionsException();
		} else {
			permsHad.append("|");
			for (String perm:permissions) {
				if(StringUtil.isEqualedTrimLower(WebConstants.PERMISSIONS_ALLPASS, perm)) {
					return;
				}
				permsHad.append(perm).append("|");
			}
		}

		boolean hasPermission = hasPermission(permsNeed, permsHad.toString(), permsNeedLogical);
		if (!hasPermission) {
			throw new RequiresPermissionsException();
		}
    }
	
	public boolean hasPermission(String[] permsNeed, String permsHad, Logical permsNeedLogical) {
		
        if (permsNeed==null||permsNeed.length==0) {
        	return true;
        }
		if (StringUtil.isEmpty(permsHad)) {
			return false;
		}
		if (permsNeedLogical==null) {
			permsNeedLogical = Logical.AND;
		}
		
		if (permsNeedLogical==Logical.AND) {
			for (int i=0;i<permsNeed.length;i++) {
				LogManager.getLogger().debug("perms and:[{}]",permsNeed[i]);
				if (permsHad.indexOf(permsNeed[i])<0) {
					LogManager.getLogger().debug("perms and:[{}]--[{}]",permsNeed[i],permsHad.toString());
					return false;
				}
			}
			return true;
		} else {
			for (int i=0;i<permsNeed.length;i++) {
				LogManager.getLogger().debug("perms or:[{}]",permsNeed[i]);
				if (permsHad.indexOf(permsNeed[i])>=0) {
					LogManager.getLogger().debug("perms or:[{}]--[{}]",permsNeed[i],permsHad.toString());
					return true;
				}
			}
			return false;
		}
		
		
	}
	
	public static void main(String[] args) {
		String allperms="|test|user|fingerprint|";
		System.err.println(allperms.indexOf("|tdest|"));
	}
}