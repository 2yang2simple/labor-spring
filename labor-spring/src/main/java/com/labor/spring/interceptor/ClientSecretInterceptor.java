//package com.labor.spring.interceptor;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.logging.log4j.LogManager;
//import org.springframework.lang.Nullable;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.labor.common.constants.CommonConstants;
//import com.labor.common.util.StringUtil;
//import com.labor.common.util.TokenUtil;
//import com.labor.spring.base.AbstractInterceptor;
//import com.labor.spring.bean.ClientRegisted;
//import com.labor.spring.constants.WebConstants;
//import com.labor.spring.util.WebUtil;
//
//public class ClientSecretInterceptor extends AbstractInterceptor{
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		String redirecturl =  request.getContextPath() + "/public/noprivilege/0";
//		
//		String clientSecret = WebUtil.getCookie(WebConstants.KEY_CLIENTSECRET);
//		
//		
//		String timestamp = request.getHeader(WebConstants.KEY_TIMESTAMP);
//		String timestamptoken = request.getHeader(WebConstants.KEY_TIMESTAMPTOKEN);
//		String clientkey= request.getParameter("client-key");
//		String token = ClientRegisted.getSecret(clientkey);
//		
//		LogManager.getLogger().debug("|timestamp:"+timestamp+"|timestamptoken:"+timestamptoken);
//		if (!TokenUtil.isValidatedDateSaltingToken(timestamp,timestamptoken,token)) {
//			String g = request.getParameter("g");
//			String t = request.getParameter("t");
//			LogManager.getLogger().debug("g:"+g +"|t:"+t);
//			if (!TokenUtil.isValidatedDateSaltingToken(g,t,token)) {
//				redirect(request,response,redirecturl);
//				return false;
//			}
//		}
////		
////		LogManager.getLogger().debug("getRequestURI:"+request.getRequestURI());
////		//http://localhost:8080/subject?g=20190730174515&t=c1e55bad62cd6398f1cb642771627123
////		String g = request.getParameter("g");
////		String t = request.getParameter("t");
////		String d = request.getParameter("d");
////		String authorization = request.getHeader("authorization");
////		if (!StringUtil.isEmpty(d)) {
////			if(StringUtil.isEqualedTrimLower(SysconfigConstants.DEVICETYPE_ANDROID, d)) {
////				token = SysconfigConstants.AUTHCODE_ANDROID;
////			}
////		}
////		LogManager.getLogger().debug("g:"+g +"|t:"+t+"|authorization:"+authorization);
////		if (!TokenUtil.isValidatedDateSaltingToken(g,t,token)) {
////			redirect(request,response,redirecturl);
////			return false;
////		}
//
//
//		return true;
//	}
//	
//	
//}
