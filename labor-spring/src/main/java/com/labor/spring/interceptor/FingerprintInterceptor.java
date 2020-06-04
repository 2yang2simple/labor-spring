//package com.labor.spring.interceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.logging.log4j.LogManager;
//import org.springframework.lang.Nullable;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.labor.common.constants.CommonConstants;
//import com.labor.spring.base.AbstractInterceptor;
//import com.labor.spring.constants.WebConstants;
//import com.labor.spring.util.WebUtil;
//
//public class FingerprintInterceptor extends AbstractInterceptor{
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//
//		String redirecturl =  request.getContextPath() + "/";
//		LogManager.getLogger().debug("getRequestURI:"+request.getRequestURI());
////		if(WebUtil.getSessionAttribute(WebConstants.KEY_FP) == null) {
////        	//super user got the passport
//////	        if(isLoginBySuper(request)) {
//////	        	return true;
//////	        }
////    		LogManager.getLogger().debug("fingerprint redirect:"
////    									+"|"+"getScheme:"+request.getScheme()+"|"
////    									+"getServerName:"+request.getServerName()+"|"
////    									+"getServerPort:"+request.getServerPort()+"|"
////    									+"getContextPath:"+request.getContextPath()+"|"
////    									+"getRequestURI:"+request.getRequestURI()+"|");
////        	redirect(request,response,redirecturl);
////
////        	return false;
////        } 
//		LogManager.getLogger().debug("getRequestURI end:"+request.getRequestURI());
//        return true;
//
//		
//	}
//}
