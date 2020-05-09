package com.labor.spring.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseProperties {

	@Value("${server.servlet.context-path}")
	public String CONTEXT_PATH;
	@Value("${environment}")
	public String ENVIRONMENT;
	
	
}
