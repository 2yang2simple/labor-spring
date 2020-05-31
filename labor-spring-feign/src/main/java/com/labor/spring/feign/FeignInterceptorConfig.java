package com.labor.spring.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FeignInterceptorConfig implements WebMvcConfigurer {

	
	@Bean
	public FeignInterceptor feignInterceptor() {
	    return new FeignInterceptor();
	}

	    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    	registry.addInterceptor(feignInterceptor())
    					.addPathPatterns("/rest/foreign/**");
    }
    

}