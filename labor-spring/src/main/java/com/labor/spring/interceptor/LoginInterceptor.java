//package com.labor.spring.interceptor;
//
//import java.net.URLEncoder;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.logging.log4j.LogManager;
//
//import com.labor.common.util.StringUtil;
//import com.labor.spring.base.AbstractInterceptor;
//import com.labor.spring.constants.WebConstants;
//import com.labor.spring.util.WebUtil;
///***
// * 		
// */
//public class LoginInterceptor extends AbstractInterceptor{
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
////		User user = cacheService.findCurrentUserOnline(false);
////		LogManager.getLogger().debug("LoginInterceptor:"+user);
////		if(user == null) { 
////			LogManager.getLogger().debug("LoginInterceptor"+user);
////            redirect(request,response,request.getContextPath()+WebUtil.LOGIN_URL);
////        	return false;
////        }
//		
////		String redirecturl = URLEncoder.encode(request.getRequestURL()+"?"+request.getQueryString(), "utf-8");
////		String homeurl = request.getScheme() + "://" 
////								+ request.getServerName() + ":"  
////								+ request.getServerPort()
////								+ request.getContextPath();
////		String currenturl = homeurl	+ request.getRequestURI();
////
////		LogManager.getLogger().debug("redirecturl:"+redirecturl);
////		LogManager.getLogger().debug("currenturl:"+currenturl);
////	
////        if(isRequestMethodNeedLogin(request)) {
////        	if(WebUtil.getSessionAttribute(WebConstants.KEY_USER) == null) { 
////                redirect(request,response,loginurl+ "?redirecturl=" + homeurl);
////	        	return false;
////	        } 
////		}	
////        if(isRequestURINeedLogin(request)) {
////        	if(WebUtil.getSessionAttribute(WebConstants.KEY_USER) == null) { 
////                redirect(request,response,loginurl+ "?redirecturl=" + redirecturl);
////	        	return false;
////	        } 
////		}
////		LogManager.getLogger().debug("getRequestURI end:"+request.getRequestURI());
//		
//        return true;
//	}
//	// non-get-method request must login first;
//	private boolean isRequestMethodNeedLogin(HttpServletRequest request) {
//		boolean ret = false;
//		String method = request.getMethod();
//		String needLoginMethods = "/post/patch/put/delete/";
//		LogManager.getLogger().debug("***********.method:"+method);
//		if (method!=null&&needLoginMethods.indexOf(method.toLowerCase())>0){
//			LogManager.getLogger().debug("***********.ture:"+needLoginMethods.indexOf(method.toLowerCase()));
//			ret = true;
//		}
//		return ret;
//
//	}
//	
//	// non-get-method request must login first;
//	private boolean isRequestURINeedLogin(HttpServletRequest request) {
//		boolean ret = false;
//		String uri = request.getRequestURI();
//		if (StringUtil.isEmpty(uri)) {
//			return ret;
//		}
//		if (uri.indexOf("creator")>0 
//				||uri.indexOf("editor")>0
//				||uri.indexOf("editor")>0){
//			LogManager.getLogger().debug("***********true.uri:"+uri);
//			ret = true;
//		}
//		return ret;
//	}
//}
