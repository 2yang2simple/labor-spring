package com.labor.spring.feign.api.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.labor.spring.constants.WebConstants;
import com.labor.spring.feign.api.auth.AuthLoginService;
import com.labor.spring.util.WebUtil;


/***
 * 		
 */
public class AuthInterceptor implements HandlerInterceptor {
	
	public static String LOGIN_URL = "/login";
	public static String LOGOUT_URL = "/logout";
	
	@Autowired
	protected AuthLoginService authLoginService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (isRequestMethodNeedLogin(request)) {

			String accessToken = WebUtil.getRequest(WebConstants.KEY_ACCESSTOKEN);
			UserVO lc = authLoginService.findLoginCache(accessToken);
			if(lc == null) { 
	            redirect(request,response,request.getContextPath()+LOGIN_URL);
	        	return false;
	        }
			
		}
	
        return true;
	}
	
	// non-get-method request must login first;
	private boolean isRequestMethodNeedLogin(HttpServletRequest request) {
		boolean ret = false;
		String method = request.getMethod();
		String needLoginMethods = "/post/patch/put/delete/";
		LogManager.getLogger().debug("[{}][{}]"+method,request.getRequestURI());
		if (method!=null&&needLoginMethods.indexOf(method.toLowerCase())>0){
			LogManager.getLogger().debug("***********.ture:"+needLoginMethods.indexOf(method.toLowerCase()));
			ret = true;
		}
		return ret;

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
