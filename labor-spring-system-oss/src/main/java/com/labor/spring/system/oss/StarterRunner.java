package com.labor.spring.system.oss;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.labor.spring.base.BaseProperties;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.core.GlobalInfo;
import com.labor.spring.core.service.SysconfigServiceIntf;

@Component
@Order(value = 1)
public class StarterRunner implements ApplicationRunner {
	
	@Autowired
	private SysconfigServiceIntf sysconfigService;
	@Autowired
	private BaseProperties baseProperties;

	
	@Override
    public void run(ApplicationArguments args) throws Exception {
		LogManager.getLogger().info("*****StarterRunner init service*****:");
		//init sysconfig
		sysconfigService.initialization();
		
		GlobalInfo.CONTEXT_PATH = baseProperties.CONTEXT_PATH;
		
		String aliyunossAccessKey = sysconfigService.findValueByKey(AliyunOssUtil.SYSCONFIG_KEY);

		if(aliyunossAccessKey!=null) {
			String[] str = aliyunossAccessKey.split("\\|");
			if (str!=null&&str.length==2) {
				AliyunOssUtil.accessKeyId = str[0];
				AliyunOssUtil.accessKeySecret = str[1];
			} else {
				LogManager.getLogger().error("aliyunossAccessKey has wrong format.");
			}
		} else {
			LogManager.getLogger().error("aliyunossAccessKey is null.");
		}
    }
	
	
	
}
