package com.labor.spring.task;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.labor.base.subject.SubjectServiceImpl;
import com.labor.base.subject.SubjectServiceIntf;
import com.labor.common.service.ServiceTransactionProxy;
import com.labor.common.util.StringUtil;
import com.labor.spring.base.AbstractScheduler;

@Component
public class Demo2ScheduledTask extends AbstractScheduler{
	
	static {
//		Demo2ScheduledTask task = new Demo2ScheduledTask();
//		task.startCron();
	}
	
	/**
	 * to do the task
	 * @return
	 */
	@Override
	public void doTask() {
		try {	
			
			LogManager.getLogger().debug("Demo2ScheduledTask is running...");
//			LogManager.getLogger().debug("Demo2ScheduledTask cron is: " + this.getCron());
//			//test service
//			SubjectServiceIntf service = 
//					(SubjectServiceIntf)new ServiceTransactionProxy().bind(new SubjectServiceImpl());	
//			HashMap hm= new HashMap();
//			hm.put("sub_name1", "可园七期");
//			LogManager.getLogger().debug(service.listSubject(hm));
			Thread.sleep(5000);
			
		} catch (Exception e) {
			LogManager.getLogger().error("",e);
		}
	}
	
	/**
	 * fetch the new cron
	 * @return
	 */
	@Override
	public String fetchNewCron() {
		String ret = "";
		try {	
			//simulate do pull the data from database to set the new cron;
			ret =  "*/10 * * * * ?";
		} catch (Exception e) {
			LogManager.getLogger().error("",e);
		}
		return ret;
		
	}
	
}
