package com.labor.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@ServletComponentScan
@ComponentScan
@EntityScan
@EnableJpaRepositories
public class LaborSpringSystemOSSApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaborSpringSystemOSSApplication.class, args);	
	}
	
	
}
