package com.labor.spring.system.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.labor.spring.feign.api.auth.AuthInterceptor;

@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer {
	
	@Bean
	public AuthInterceptor authInterceptor() {
	    return new AuthInterceptor();
	}

	    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    	registry.addInterceptor(authInterceptor())
    					.addPathPatterns("/rest/**")
    					.excludePathPatterns("/") 
    					.excludePathPatterns("/rest/foreign/**") 
    					.excludePathPatterns("/rest/feign/auth/**") 
    					.excludePathPatterns("/rest/core/samples/**") 
    					.excludePathPatterns("/rest/core/auth/**")
    					.excludePathPatterns("/rest/profiles/**") 
    					.excludePathPatterns("/rest/sso/**");
						

    }
    

}