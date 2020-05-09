package com.labor.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;


/**
 * add md5 suffix to js, css etc static files, avoid the browser cache;
 * @author Administrator
 *
 */
@ControllerAdvice
public class LaborResourceUrlProvider {

    @Autowired
    private ResourceUrlProvider  resourceUrlProvider;

    @ModelAttribute("urls")
    public ResourceUrlProvider  urls() {
        return this.resourceUrlProvider ;
    }

}