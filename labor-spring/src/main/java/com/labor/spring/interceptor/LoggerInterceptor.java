//package com.labor.spring.interceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.logging.log4j.LogManager;
//
//import com.labor.common.constants.CommonConstants;
//import com.labor.common.util.StringUtil;
//import com.labor.spring.base.AbstractInterceptor;
//import com.labor.spring.constants.WebConstants;
//import com.labor.spring.util.WebUtil;
//
//public class LoggerInterceptor extends AbstractInterceptor{
//	
//	
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//
////		LogManager.getLogger().debug("getRequestURI:"+request.getRequestURI());
//		String fullurl = request.getScheme() + "://" 
//				+ request.getServerName() + ":"  
//				+ request.getServerPort()
//				+ request.getRequestURI();
//
//		String userAgent = request.getHeader("user-agent");
//		String origin = request.getHeader("origin");
//		String referer = request.getHeader("referer");
//		
//		String username = "";
////		User user = cacheService.findCurrentUserOnline(false);
////		if(user != null) {
////			username = user.getName();
////        }
//		LogManager.getLogger().info("********"
//								+"IP:"+WebUtil.getIpAddress(request)+" | "
//								+"NAME:" +username
//								+"userAgent:" +userAgent+" | "
////								+"referer:" +referer+" | "
////								+"origin:" +origin+" | "
//								+"fullurl:" +fullurl+" | ");
//
////		LogManager.getLogger().debug("getRequestURI end:"+request.getRequestURI());
//        return true;
//
//		
//	}
//	
//
//	
//}