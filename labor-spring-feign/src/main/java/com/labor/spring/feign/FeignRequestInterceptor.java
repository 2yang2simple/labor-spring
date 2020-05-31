package com.labor.spring.feign;

import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.labor.spring.constants.WebConstants;
import com.labor.spring.util.WebUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
    
	@Override
    public void apply(RequestTemplate requestTemplate) {
        //"access-token"
		LogManager.getLogger().debug("--FeignConfig:---"+WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN));
        requestTemplate.header(WebConstants.KEY_ACCESSTOKEN, WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN));
        requestTemplate.header(FeignConstants.key, FeignConstants.secret);
    }


}
