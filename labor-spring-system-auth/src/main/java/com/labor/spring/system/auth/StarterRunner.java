package com.labor.spring.system.auth;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.labor.spring.base.BaseProperties;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.core.GlobalInfo;
import com.labor.spring.core.service.FingerprintServiceIntf;
import com.labor.spring.core.service.SysconfigServiceIntf;

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
		
		/***
		 * get password, secret, etc from db to static ;
		 */
		ClientRegisted.putClientInfo(
				"auth|web-canvas|cookies|de07c085bfe741caaef26e7b4adf0096|http://localhost:8080/auth/rest/feign/auth/logins/users/tokens/keys|http://localhost:8080/auth");


    }
	
	
	
}
