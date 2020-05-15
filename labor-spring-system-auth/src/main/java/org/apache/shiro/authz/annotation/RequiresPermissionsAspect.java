package org.apache.shiro.authz.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Aspect
@Component
public class RequiresPermissionsAspect {
	
	@Pointcut("@annotation(org.apache.shiro.authz.annotation.RequiresPermissions)")
    public void annotationPoinCut(){
		
	}
	
	@Before("annotationPoinCut()")
    public void Before(JoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresPermissions rp = method.getAnnotation(RequiresPermissions.class);
        System.err.println("requiresPermissions:"+rp.value());
    }

//    @Before("execution(* com.cenobitor.aop.service.DemoMethodService.*(..))")
//    public void before(JoinPoint joinPoint){
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        System.out.println("方法规则式拦截,"+method.getName());
//    }
}
