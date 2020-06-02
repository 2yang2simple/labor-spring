package com.labor.spring.core.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


//@Component
public class LoginCountListener implements HttpSessionListener {

	
	private static LoginCountListener loginCountListener;
	
//	@PostConstruct 
	public void init(){
		loginCountListener = this;
	}
	
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();		
		System.err.println("sessionCreated:"+session.getId());
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		System.err.println("sessionDestroyed:"+session.getId());
//		loginCountListener.fpService.deleteOnline(session.getId());
	}

}
