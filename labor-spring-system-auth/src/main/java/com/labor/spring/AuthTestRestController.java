package com.labor.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.spring.bean.Result;

@RestController
@RequestMapping("/rest/test")
public class AuthTestRestController {
	
	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public Result test() {
		return Result.success("test");
	}
}
