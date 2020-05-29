package com.labor.spring.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.labor.spring.interceptor.FingerprintInterceptor;
import com.labor.spring.interceptor.LoggerInterceptor;
import com.labor.spring.interceptor.LoginInterceptor;
import com.labor.spring.interceptor.TimestampTokenInterceptor;

@Configuration
public class LaborWebMvcConfig implements WebMvcConfigurer, InitializingBean  {
	
	@Bean
	public LoginInterceptor loginInterceptor() {
	    return new LoginInterceptor();
	}
	@Bean
	public TimestampTokenInterceptor timestampTokenInterceptor() {
	    return new TimestampTokenInterceptor();
	}
	@Bean
	public FingerprintInterceptor fingerprintInterceptor() {
		return new FingerprintInterceptor();
	}
	@Bean
	public LoggerInterceptor loggerInterceptor() {
		return new LoggerInterceptor();
	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci =  new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

						
//    	registry.addInterceptor(loginInterceptor())
//    					.addPathPatterns("/**")
//    					.excludePathPatterns("/") 
//    					.excludePathPatterns("/rest/core/auth/**") 
//    					.excludePathPatterns("/rest/accounts/**") 
//    					.excludePathPatterns("/rest/profiles/**") 
//    					.excludePathPatterns("/rest/sso/**")
//						.excludePathPatterns("/core/auth/**")
//    					.excludePathPatterns("/profile/**") 
//    					.excludePathPatterns("/sso/**") 
//    					.excludePathPatterns("/error/**")
//    					.excludePathPatterns("/public/**")
//						.excludePathPatterns("/html/**")
//						.excludePathPatterns("/excel/**")
//						.excludePathPatterns("/img/**")
//						.excludePathPatterns("/layui/**")
//						.excludePathPatterns("/css/**")
//						.excludePathPatterns("/js/**");

//        registry.addInterceptor(fingerprintInterceptor())
//						.addPathPatterns("/**")
//						.excludePathPatterns("/")
//						.excludePathPatterns("/rest/**")
//						.excludePathPatterns("/oss/**")
//						.excludePathPatterns("/attachments/**")
//						//html
//						.excludePathPatterns("/rt/**")
//						.excludePathPatterns("/auth/**") //can login and sign up.
//						.excludePathPatterns("/asample/**") //can login and sign up.
//						.excludePathPatterns("/error/**")
//						.excludePathPatterns("/public/**")
//						.excludePathPatterns("/html/**")
//						.excludePathPatterns("/excel/**")
//						.excludePathPatterns("/img/**")
//						.excludePathPatterns("/layui/**")
//						.excludePathPatterns("/css/**")
//						.excludePathPatterns("/js/**");
        registry.addInterceptor(loggerInterceptor()).addPathPatterns("/rest/**");
//        registry.addInterceptor(timestampTokenInterceptor()).addPathPatterns("/rest/**");
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