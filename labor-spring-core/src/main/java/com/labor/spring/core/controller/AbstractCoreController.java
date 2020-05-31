package com.labor.spring.core.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
	
//	/******************
//	 * public
//	 */
//	
//	@RequestMapping(value = { "/public/{page}" }, method = RequestMethod.GET)
//	public String toCorePublicPages(ModelMap map,@PathVariable(value="page") String page) {	
//		map.addAttribute("message","");
//		return CORE_PREFIX + "core/public/"+page;
//	}
//	@RequestMapping(value = { "/public/noprivilege/{type}" }, method = RequestMethod.GET)
//	public String toCorePublicNoPrivilegePages(ModelMap map,@PathVariable(value="type") String type) {	
//		String message = "";
//		if("0".equals(type)) {
//			message = "Bad request.";
//		}
//		if("1".equals(type)) {
//			message = "The client is not open, ask the admin to open it;";
//		}
//		if("2".equals(type)) {
//			message = "This is only for superuser.";
//		}
//		map.addAttribute("message",message);
//		return CORE_PREFIX+ "core/public/noprivilege";
//	}

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
	
	@RequestMapping(value = { "/core/user/{page}" }, method = RequestMethod.GET)
	public String toCoreUserPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/user/"+page;
	}
	@RequestMapping(value = { "/core/role/{page}" }, method = RequestMethod.GET)
	public String toCoreRolePages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/role/"+page;
	}

	@RequestMapping(value = { "/core/fingerprint/{page}" }, method = RequestMethod.GET)
	public String toCoreFingerprintPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/fingerprint/"+page;
	}
	
	@RequestMapping(value = { "/core/sysconfig/{page}" }, method = RequestMethod.GET)
	public String toCoreSysconfigPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/sysconfig/"+page;
	}
	
	@RequestMapping(value = { "/core/auth/{page}" }, method = RequestMethod.GET)
	public String toCoreAuthPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return CORE_PREFIX + "core/auth/"+page;
	}
	
	
}
