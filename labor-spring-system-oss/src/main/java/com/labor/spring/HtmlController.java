package com.labor.spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.labor.spring.base.BaseController;

@Controller
public class HtmlController extends BaseController{

	@RequestMapping(value = { "/help"}, method = RequestMethod.GET)
	public String help(ModelMap map) {	
		map.addAttribute("message","");
		return "help";
	}
	
}
