package com.labor.spring.core;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.ClassUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.base.BaseProperties;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.aop.AnnotationUtil;
import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.User;
import com.labor.spring.core.service.FingerprintServiceIntf;
import com.labor.spring.core.service.SysconfigConstants;
import com.labor.spring.core.service.SysconfigServiceIntf;
import com.labor.spring.core.service.UserServiceIntf;
import com.labor.spring.feign.client.auth.AuthFeignClient;
import com.labor.spring.util.WebUtil;

@Component
@Order(value = 1)
public class StarterRunner implements ApplicationRunner {
	
	@Autowired
	private SysconfigServiceIntf sysconfigService;
	@Autowired
	private FingerprintServiceIntf fingerprintService;
	@Autowired
	private BaseProperties baseProperties;

	
	@Override
    public void run(ApplicationArguments args) throws Exception {
		LogManager.getLogger().info("*****StarterRunner init service*****:");
		
		//init sysconfig
		sysconfigService.initialization();
		
		fingerprintService.deleteOnlineSession();
		
		GlobalInfo.CONTEXT_PATH = baseProperties.CONTEXT_PATH;


    }
	
	
	
}
