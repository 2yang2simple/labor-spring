package com.labor.spring.core.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.labor.spring.base.BaseController;

public abstract class AbstractCoreController extends BaseController {
	
	private final String CORE_PREFIX = "00.";

	@RequestMapping(value = { "/help"}, method = RequestMethod.GET)
	public String help(ModelMap map) {	
		map.addAttribute("message","");
		return "help";
	}
	@RequestMapping(value = { "/login"}, method = RequestMethod.GET)
	public String login(ModelMap map) {	
		map.addAttribute("message","");
		return "login";
	}
	@RequestMapping(value = { "/logout"}, method = RequestMethod.GET)
	public String logout(ModelMap map) {	
		map.addAttribute("message","");
		return "logout";
	}


	/******************
	 * core
	 */
	
	@RequestMapping(value = { "/core" }, method = RequestMethod.GET)
	public String toCoreIndex(ModelMap map) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/settings";
	}

	//labor setting
	@RequestMapping(value = { "/core/settings"}, method = RequestMethod.GET)
	public String toCoreSettingPage(ModelMap map) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/settings";
	}

	@RequestMapping(value = { "/core/asample/{page}" }, method = RequestMethod.GET)
	public String toAsamplePages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/asample/"+page;
	}
		
	@RequestMapping(value = { "/core/sysconfig/{page}" }, method = RequestMethod.GET)
	public String toCoreSysconfigPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/sysconfig/"+page;
	}
	

	
}
