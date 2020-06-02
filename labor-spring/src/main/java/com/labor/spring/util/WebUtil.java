package com.labor.spring.util;


import java.io.FileNotFoundException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;

public class WebUtil {
	
//	public static String CONTEXT_PATH = "";
//	public static String CLASS_PATH = "";
//	public static String DOCUMENTS_DIR = "";
//	public static String ATTACHMENTS_DIR = "";
//	public static String OBJECTSTORAGE_DIR = "";
//	
//	public static String LOGIN_URL = "/auth/login";
//	public static String LOGOUT_URL = "/auth/logout";
//	
//	public static LoginCache USER_SUPER;
//	private static long USER_SNO_MAX = 0;
	
	public static String nextSno() {
//		USER_SNO_MAX = USER_SNO_MAX + 1;
//		return StringUtil.prefixZero(String.valueOf(USER_SNO_MAX), 10);
		return TokenUtil.generateUNum();
	}


//	public static void setCurrentSno(String sno) {
//		if (USER_SNO_MAX>0) {
//			return;
//		}
//		try {
//			USER_SNO_MAX = Long.valueOf(sno);
//		} catch (NumberFormatException e) {
//			LogManager.getLogger().error("sno:"+sno,e);
//		}
//	}
	
	public static void main(String[] args) {
//		WebUtil.setCurrentSno("00000023");
//		System.err.println(WebUtil.nextSno());
	}
	
	
	public static Object getSessionAttribute(String key) {
		Object ret = null;
		LogManager.getLogger().debug("*****getSession begin:" + ret);

		HttpSession session = getHttpSession(false);
		if (session != null && session.getAttribute(key) != null) {
			LogManager.getLogger().debug("*****getSession not null:" + ret);
			ret = session.getAttribute(key);
		}
		return ret;

	}

	public static void setSessionAttribute(String name, Object value) {
		LogManager.getLogger().debug("*****setSession begin:" + name);
		HttpSession session = getHttpSession(true);
		if (session != null) {
			LogManager.getLogger().debug("*****setSession not null:" + name);
			if (value != null) {
				LogManager.getLogger().debug("*****setSession set attr:" + name);
				session.setAttribute(name, value);
			} else {
				LogManager.getLogger().debug("*****setSession remove attr:" + name);
				session.removeAttribute(name);
			}
		}
		
	}
	
	public static String getSessionId() {
		String ret = null;
		HttpSession session = getHttpSession(false);
		if (session!=null) {
			ret = session.getId().toString();
		}
		return ret;
	}
	
//	public static Session getShiroSession(boolean create) {
//		Session ret = null;
//		//get session from shiro
//		Subject subject= SecurityUtils.getSubject();
//		if (subject!=null) {
//			ret = subject.getSession(create);
//		}
//		return ret;
//	}	
	
	public static HttpSession getHttpSession(boolean create) {
		HttpSession ret = null;
		//get session from request
		HttpServletRequest request = getRequest();
		if (request!=null) {
			ret = request.getSession(create);
		}
		return ret;
	}

//	public static String getSessionIdCreatedIfNotExist() {
//		String ret = null;
//		Subject subject= SecurityUtils.getSubject();
//		if (subject!=null) {
//			Session session = subject.getSession(false);
//			if (session==null) {
//				session = subject.getSession(true);
//				LogManager.getLogger().debug("*****session created:"+session.getId());
//			}
//			ret = session.getId().toString();
//		}
//		return ret;
//	}
//	
//	public static User getCurrentUser() {
//		User ret = null;
//		if(getSessionAttribute(WebConstants.KEY_USER) != null) {
//        	User user = (User)getSessionAttribute(WebConstants.KEY_USER);
//			ret = user;
//		}
//		return ret;
//	}

	
//	public static String getClassPath() {
//		return CLASS_PATH;
//		String ret = "";
//		try {
//			ret = ResourceUtils.getURL("classpath:").getPath();
//		} catch (FileNotFoundException e) {
//			LogManager.getLogger().error("" + e);
//		}
//		return ret;
//	}

	public static String getIpAddress(HttpServletRequest request) {
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if (!StringUtil.isEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
			int index = XFor.indexOf(",");
			if (index != -1) {
				return XFor.substring(0, index);
			} else {
				return XFor;
			}
		}
		XFor = Xip;
		if (!StringUtil.isEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
			return XFor;
		}
		if (StringUtil.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtil.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtil.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtil.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtil.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}
	
//	public static String getCookie(HttpServletRequest request,String cookieName){
//        Cookie[] cookies =  request.getCookies();
//        if(cookies != null){
//            for(Cookie cookie : cookies){
//                if(cookie.getName().equals(cookieName)){
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//	
//    public static void setCookie(HttpServletResponse response, String cookieName,String value){
//        Cookie cookie = new Cookie(cookieName,value);
//        cookie.setPath("/");
//        cookie.setMaxAge(259200); //30days
//        cookie.setHttpOnly(true);
//        response.addCookie(cookie);
//    }
//
//    public static void setCookie(HttpServletResponse response, String cookieName,String value,int seconds){
//        Cookie cookie = new Cookie(cookieName,value);
//        cookie.setPath("/");
//        cookie.setMaxAge(seconds); //30days
//        cookie.setHttpOnly(true);
//        response.addCookie(cookie);
//    }
    
    public static void setCookie(String cookieName,String value){
        Cookie cookie = new Cookie(cookieName,value);
        cookie.setPath("/");
        cookie.setMaxAge(259200); //30days
        cookie.setHttpOnly(true);
        HttpServletResponse response = getResponse();
		if (response==null) {
			return;
		}
        response.addCookie(cookie);
    }

    public static void setCookie(String cookieName,String value,int seconds){
        Cookie cookie = new Cookie(cookieName,value);
        cookie.setPath("/");
        cookie.setMaxAge(seconds); //30days
        cookie.setHttpOnly(true);
        HttpServletResponse response = getResponse();
		if (response==null) {
			return;
		}
		response.addCookie(cookie);
    }
       
	public static String getCookie(String cookieName){
		HttpServletRequest request = getRequest();
		if (request==null) {
			return null;
		}
        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
	
	public static String getHeader(String name){
		String ret = null;
		HttpServletRequest request = getRequest();
		if (request==null) {
			return null;
		}
        ret =  request.getHeader(name);
        return ret;
    }
	public static String getParameter(String name){
		String ret = null;
		HttpServletRequest request = getRequest();
		if (request==null) {
			return null;
		}
        ret =  request.getParameter(name);
        return ret;
    }
	
	//get value from parameter, header, or cookie in request
	public static String getRequest(String name){
		String ret = null;
		ret = getCookie(name);
		if (StringUtil.isEmpty(ret)) {
			ret = getHeader(name);
		}
		if (StringUtil.isEmpty(ret)) {
			ret = getParameter(name);
		}
		return ret;		
	}
	
    private static HttpServletRequest getRequest() {
    	HttpServletRequest ret = null;
		ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (sra!=null) {
			ret = sra.getRequest();
		}
		return ret;
	}

	
    public static HttpServletResponse getResponse() {
    	HttpServletResponse ret = null;
		ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (sra!=null) {
			ret = sra.getResponse();
		}
		return ret;
	}
    
	public static String getClassPath() {
		String ret = null;
	    try {
			ret = ResourceUtils.getURL("classpath:").getPath();
		} catch (FileNotFoundException e) {
			LogManager.getLogger().error("" + e);
		}
	    return ret;
	}

}
