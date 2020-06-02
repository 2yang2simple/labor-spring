//package com.labor.spring;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.web.servlet.ServletComponentScan;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//import com.labor.spring.feign.api.auth.AuthCacheService;
//import com.labor.spring.feign.api.auth.AuthCacheServiceLocalTestImpl;
//
//
//@SpringBootApplication
//@EnableFeignClients
//@ServletComponentScan
//@ComponentScan
//@EntityScan
//@EnableJpaRepositories
//public class LaborSpringCoreApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(LaborSpringCoreApplication.class, args);
//	}
//	
//	@Bean
//	public AuthCacheService authCacheService() {
//		
//		//production
////		AuthCacheServiceFeignRedisImpl authCacheService = new AuthCacheServiceFeignRedisImpl();
//		//test
////		AuthCacheServiceFeignImpl authCacheService = new AuthCacheServiceFeignImpl();
//		//development
//		AuthCacheServiceLocalTestImpl authCacheService = new AuthCacheServiceLocalTestImpl();
//		
//		return authCacheService;
//	}
//}
