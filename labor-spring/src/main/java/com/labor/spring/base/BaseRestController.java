package com.labor.spring.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BaseRestController {
	
	@Autowired
	protected BaseProperties baseProperties;
	
	
}
