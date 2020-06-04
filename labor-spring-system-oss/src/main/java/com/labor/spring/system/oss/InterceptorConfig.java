package com.labor.spring.system.oss;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.labor.spring.feign.api.auth.AuthInterceptor;
import com.labor.spring.interceptor.TimestampTokenInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Bean
	public AuthInterceptor authInterceptor() {
	    return new AuthInterceptor();
	}
	@Bean
	public TimestampTokenInterceptor timestampTokenInterceptor() {
	    return new TimestampTokenInterceptor();
	}
	    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    	registry.addInterceptor(authInterceptor())
    					.addPathPatterns("/rest/**")
    					.excludePathPatterns("/rest/feign/auth/**") ;
    	registry.addInterceptor(timestampTokenInterceptor())
						.addPathPatterns("/rest/**")
    					.excludePathPatterns("/rest/foreign/**") 
    					.excludePathPatterns("/rest/feign/auth/**") ;		

    }
    

}