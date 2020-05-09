package com.labor.spring.system.ppp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class AbstractAccountController {


	private final String prefix = "00.";
	
	@RequestMapping(value = { "/account/login" }, method = RequestMethod.GET)
	public String toLoginPages(ModelMap map) {	
		map.addAttribute("message","");
		return prefix + "account/login";
	}
	
	@RequestMapping(value = { "/account/logout" }, method = RequestMethod.GET)
	public String toLogout(ModelMap map) {	
		map.addAttribute("message","");
		return prefix + "account/logout";
	}
	
	@RequestMapping(value = { "/account/anonymous" }, method = RequestMethod.GET)
	public String toAnonymous(ModelMap map) {	
		map.addAttribute("message","");
		return prefix + "account/anonymous";
	}
	
	//local login
	@RequestMapping(value = { "/auth/{page}" }, method = RequestMethod.GET)
	public String toAuthPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return prefix + "account/auth/"+page;
	}
	//sso login
	@RequestMapping(value = { "/account/sso/{page}" }, method = RequestMethod.GET)
	public String toSsoPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return prefix + "account/sso/"+page;
	}
}
