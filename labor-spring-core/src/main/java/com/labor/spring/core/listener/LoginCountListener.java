package com.labor.spring.core.listener;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.service.FingerprintServiceIntf;
import com.labor.spring.util.WebUtil;


//@Component
public class LoginCountListener implements HttpSessionListener {
//	@Autowired
	private FingerprintServiceIntf fpService;	
	
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
