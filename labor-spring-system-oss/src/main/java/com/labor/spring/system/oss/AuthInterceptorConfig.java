package com.labor.spring.system.oss;

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
    					.excludePathPatterns("/rest/feign/auth/**") ;
						

    }
    

}