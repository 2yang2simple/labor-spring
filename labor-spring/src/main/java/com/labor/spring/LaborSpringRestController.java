package com.labor.spring;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.labor.spring.base.BaseRestController;
import com.labor.spring.bean.Result;


@RestController
@RequestMapping("/rest")
public class LaborSpringRestController extends BaseRestController {
	
	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public Result info() {
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("contextpath", baseProperties.CONTEXT_PATH);
		hm.put("environment", baseProperties.ENVIRONMENT);
		
		return Result.success(hm);
	}
	
}
