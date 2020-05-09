package com.labor.spring.feign.auth;

import java.util.Arrays;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.labor.spring.feign.auth.AuthInterceptor;
import com.labor.spring.interceptor.FingerprintInterceptor;
import com.labor.spring.interceptor.LoggerInterceptor;
import com.labor.spring.interceptor.LoginInterceptor;
import com.labor.spring.interceptor.TimestampTokenInterceptor;

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
						
//    	registry.addInte
    }
    

}