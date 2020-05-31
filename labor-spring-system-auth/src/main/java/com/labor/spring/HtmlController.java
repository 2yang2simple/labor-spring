package com.labor.spring;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseController;




@Controller
public class HtmlController extends BaseController{
	
	private final String PROFILE_PREFIX = "01.";
	private final String SSO_PREFIX = "02.";
	private final String AUTH_PREFIX = "03.";
	
	
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

	
	/***********************
	 * auth
	 */	
	@RequestMapping(value = { "/sso/{page}"}, method = RequestMethod.GET)
	public String toSSOPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return SSO_PREFIX+"sso/"+page;
	}		
	@RequestMapping(value = { "/profile/{page}"}, method = RequestMethod.GET)
	public String toProfilePages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return PROFILE_PREFIX+"profile/"+page;
	}			
	
	@RequestMapping(value = { "/auth/{page}"}, method = RequestMethod.GET)
	public String toAuthPages(ModelMap map,@PathVariable(value="page") String page) {	
		map.addAttribute("message","");
		return AUTH_PREFIX+"auth/"+page;
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
