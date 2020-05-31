package com.labor.spring.feign;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.web.servlet.HandlerInterceptor;

import com.labor.common.util.StringUtil;
import com.labor.spring.constants.WebConstants;

import com.labor.spring.util.WebUtil;


/***
 * 		
 */
public class FeignInterceptor implements HandlerInterceptor {
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String accessToken = WebUtil.getHeader(WebConstants.KEY_ACCESSTOKEN);
		String feignSecret = WebUtil.getHeader(FeignConstants.key);
		
		LogManager.getLogger().debug("--header:---{[]}", accessToken);
		LogManager.getLogger().debug("--header secret:---{[]}",feignSecret);
		
		if (!StringUtil.isEqualedTrimLower(FeignConstants.secret, feignSecret)) {
			redirect(request,response,request.getContextPath() + "/public/noprivilege/0");
		}

		return true;
	}
	

	public void redirect(HttpServletRequest request, HttpServletResponse response, String url) 
			throws IOException {
	
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            response.setHeader("REDIRECT", "REDIRECT");
            response.setHeader("CONTENTPATH", url);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else{
            response.sendRedirect(url);
        }
	}
}
