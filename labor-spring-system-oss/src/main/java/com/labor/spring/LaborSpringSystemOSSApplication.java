package com.labor.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.labor.spring.feign.api.auth.AuthCacheService;
import com.labor.spring.feign.api.auth.AuthCacheServiceFeignImpl;
import com.labor.spring.feign.api.auth.AuthCacheServiceFeignRedisImpl;
import com.labor.spring.feign.api.auth.AuthCacheServiceLocalTestImpl;

@SpringBootApplication
@EnableFeignClients
@ServletComponentScan
@ComponentScan
@EntityScan
@EnableJpaRepositories
public class LaborSpringSystemOSSApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaborSpringSystemOSSApplication.class, args);	
	}
	
	//config login cache. redis/cache(remote feign) or cache(local);
	@Bean
	public AuthCacheService authCacheService() {
		
		//production
//		AuthCacheServiceFeignRedisImpl authCacheService = new AuthCacheServiceFeignRedisImpl();
//		test
//		AuthCacheServiceFeignImpl authCacheService = new AuthCacheServiceFeignImpl();
		//development
		AuthCacheServiceLocalTestImpl authCacheService = new AuthCacheServiceLocalTestImpl();
		
		return authCacheService;
	}
}
