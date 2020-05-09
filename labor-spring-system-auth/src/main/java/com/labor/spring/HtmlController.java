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
import com.labor.spring.core.controller.AbstractCoreController;



@Controller
public class HtmlController extends AbstractCoreController{
	
	private final String PROFILE_PREFIX = "01.";
	private final String SSO_PREFIX = "02.";
	private final String AUTH_PREFIX = "03.";

	

	
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
	
	

	
	
}
