package com.labor.spring.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseProperties {

	@Value("${server.servlet.context-path}")
	public String CONTEXT_PATH;
	@Value("${environment}")
	public String ENVIRONMENT;
	
	public String getContextName() {
		String ret = "";
		if (CONTEXT_PATH!=null) {
			ret = CONTEXT_PATH.replace("/", "");
		}
		return ret;
	}
	public String getContextPath() {
		String ret = "";
		ret = CONTEXT_PATH;
		return ret;
	}
}
