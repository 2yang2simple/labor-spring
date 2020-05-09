package com.labor.spring.task;

import java.io.File;
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
public class Demo1ScheduledTask extends AbstractScheduler{
	
	static {
//		Demo1ScheduledTask task = new Demo1ScheduledTask();
//		task.startCron();
	}


	
	private int dircount = 0;
	
	private void iterateDir(String dirpath, String parentid) {
		File file = new File(dirpath);
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}

		for (File f : files) {
			if (f.isFile()) {
				//do some file thing; tag = parentid,
				//System.err.println("file:"+parentid+"."+f.getName());
			} if (f.isDirectory()) {
				dircount++;
				System.err.println(""+parentid+"-"+f.getName());
				//do some insert get a new parentid
				String newparentid = parentid+"0";
				
				iterateDir(f.getAbsolutePath(),newparentid);
			}
		}
	}
	
	/**
	 * to do the task
	 * @return
	 */
	@Override
	public void doTask() {
		try {	
			
			LogManager.getLogger().debug("Demo1ScheduledTask is running...");

//			LogManager.getLogger().debug("Demo1ScheduledTask cron is: " + this.getCron());
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
