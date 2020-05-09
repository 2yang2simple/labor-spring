package com.labor.spring.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.util.WebUtil;

/***
http://localhost:8080/CarsiLogCenter_new/idpstat.jsp?action=idp.sptopn

request.getRequestURL() http://localhost:8080/CarsiLogCenter_new/idpstat.jsp
request.getRequestURI() /CarsiLogCenter_new/idpstat.jsp
request.getContextPath()/CarsiLogCenter_new
request.getServletPath() /idpstat.jsp
request.getQueryString()action=idp.sptopn
 *
 */
public abstract class AbstractInterceptor implements HandlerInterceptor {
	
	
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
	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
//		LogManager.getLogger().debug("postHandle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
//		LogManager.getLogger().debug("afterCompletion");
	}
	
}
