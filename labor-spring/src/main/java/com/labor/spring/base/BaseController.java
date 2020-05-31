package com.labor.spring.base;

import java.util.HashMap;
import java.util.List;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public abstract class BaseController {

	@RequestMapping(value = { "", "/", "/index.html"}, method = RequestMethod.GET)
	public String index(ModelMap map) {	
		map.addAttribute("message","");
		return "index";
	}	
	@RequestMapping(value = { "/error/404"}, method = RequestMethod.GET)
	public String error404(ModelMap map) {	
		map.addAttribute("message","");
		return "/error/404";
	}	
	
	/******************
	 * public
	 */
	
	@RequestMapping(value = { "/public/{page}" }, method = RequestMethod.GET)
	public String toCorePublicPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return "public/"+page;
	}
	@RequestMapping(value = { "/public/noprivilege/{type}" }, method = RequestMethod.GET)
	public String toCorePublicNoPrivilegePages(ModelMap map,@PathVariable(value="type") String type) {	
		String message = "";
		if("0".equals(type)) {
			message = "Bad request.";
		}
		if("1".equals(type)) {
			message = "The client is not open, ask the admin to open it;";
		}
		if("2".equals(type)) {
			message = "This is only for superuser.";
		}
		map.addAttribute("message",message);
		return "public/noprivilege";
	}

}
