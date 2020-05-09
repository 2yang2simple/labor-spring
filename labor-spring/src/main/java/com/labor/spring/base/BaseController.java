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
	
}
