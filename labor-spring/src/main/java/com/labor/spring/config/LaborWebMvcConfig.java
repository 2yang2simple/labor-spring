package com.labor.spring.config;

import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Configuration
public class LaborWebMvcConfig implements WebMvcConfigurer, InitializingBean  {
	
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci =  new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    
    /**
	 * see CorsFilter.java
	 */
//    @Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")
//				.allowedMethods("*")
//				.allowedHeaders("*")
//				.allowedOrigins("*")
//				.allowCredentials(true)
//				.maxAge(18000L);
//	}
    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return slr;
    }
    
    
    
    @Autowired(required = false)
    private ObjectMapper obj;

    private SimpleModule getSimpleModule() {
        /**
         * convert long to string to avoid losing the accuracy in json
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        simpleModule.addSerializer(Double.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Double.TYPE, ToStringSerializer.instance);
        return simpleModule;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (obj != null) {
            SimpleModule simpleModule = getSimpleModule();
            obj.registerModule(simpleModule);
        }
    }

}