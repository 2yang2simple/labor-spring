//package com.labor.spring.config;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.apache.shiro.mgt.AuthorizingSecurityManager;
//import org.apache.shiro.mgt.DefaultSecurityManager;
//import org.apache.shiro.mgt.SecurityManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.labor.spring.common.WebUtil;
//
//@Configuration
//public class ShiroConfig {
//
//	@Bean
//	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//
//		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//		shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//		// anon anonymous, 
////		filterChainDefinitionMap.put("/core/auth/**", "anon");
////		filterChainDefinitionMap.put("/accounts/**", "anon");
////		filterChainDefinitionMap.put("/sso/**", "anon");
////		filterChainDefinitionMap.put("/rest/core/auth/**", "anon");
////		filterChainDefinitionMap.put("/rest/sso/**", "anon");
////		filterChainDefinitionMap.put("/rest/accounts/**", "anon");
////		
////		
////		filterChainDefinitionMap.put("/static/**", "anon");
////		filterChainDefinitionMap.put("/error/**", "anon");
////		filterChainDefinitionMap.put("/public/**", "anon");
////		filterChainDefinitionMap.put("/html/**", "anon");
////		filterChainDefinitionMap.put("/excel/**", "anon");
////		filterChainDefinitionMap.put("/img/**", "anon");
////		filterChainDefinitionMap.put("/css/**", "anon");
////		filterChainDefinitionMap.put("/js/**", "anon");
//		
//		//logout
////		filterChainDefinitionMap.put(WebUtil.LOGOUT_URL, "logout");
//		//login
//		shiroFilterFactoryBean.setLoginUrl(WebUtil.LOGIN_URL);
//		//redirect
//		shiroFilterFactoryBean.setSuccessUrl("/");
//		
//		/****
//		 * shirFilter is NOT used, see LaborWebMvcConfigurer;
//		 */
//		//authc need login,
////		filterChainDefinitionMap.put("/**", "authc");
////		filterChainDefinitionMap.put("/**==PUT", "authc");
////		filterChainDefinitionMap.put("/**==DELETE", "authc");
////		filterChainDefinitionMap.put("/**==PATCH", "authc");
//
//		//403
//		shiroFilterFactoryBean.setUnauthorizedUrl("/public/noprivilege/0");
//		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//		return shiroFilterFactoryBean;
//	}
//
//	@Bean
//	public ShiroAuthorizingRealm shiroRealm() {
//		ShiroAuthorizingRealm shiroRealm = new ShiroAuthorizingRealm();
//		shiroRealm.setCachingEnabled(true);
//		return shiroRealm;
//	}
//
//	@Bean
//	public SecurityManager securityManager() {
//		
//		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//		securityManager.setRealm(shiroRealm());
//		//DefaultWebSessionManager will disable ResourceUrlProvider
////		securityManager.setSessionManager(new DefaultWebSessionManager());
////		securityManager.setCacheManager(cacheManager);
////		securityManager.setSubjectDAO(shiroSubjectDAO());
//		
//		return securityManager;
//	}
//
//	/**
//	 * open @RequiresRoles,@RequiresPermissions)
//	 */
//	@Bean
//	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
//		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//		advisorAutoProxyCreator.setProxyTargetClass(true);
//		return advisorAutoProxyCreator;
//	}
//
//	/**
//	 * open @RequiresRoles,@RequiresPermissions)
//	 */
//	@Bean
//	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor 
//				= new AuthorizationAttributeSourceAdvisor();
//		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//		return authorizationAttributeSourceAdvisor;
//	}
//}
