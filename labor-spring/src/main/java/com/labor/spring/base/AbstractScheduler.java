package com.labor.spring.base;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.labor.base.subject.SubjectServiceImpl;
import com.labor.base.subject.SubjectServiceIntf;
import com.labor.common.service.ServiceTransactionProxy;
import com.labor.common.util.StringUtil;
import com.labor.spring.config.LaborSchedulingConfig;

@Component
@Scope("prototype")
public abstract class AbstractScheduler {

    
	public abstract void doTask();

    public abstract String fetchNewCron();
    
    
	/***
	 * cron:
	 * *=every
	 * ,=certain time
	 * -=time range
	 * /=step eg.  "0 *0/5 * * * ?"  from 0, do once every 5 minutes.
		seconds 0~59
		Minutes 0~59
		Hours   0~23
		day     0~31 
		month 	0~11
		week 1-7  or SUN/MON/TUE/WED/THU/FRI/SAT
	 */
	private String cron = "0 */1 * * * ?";
	
	private boolean enabled = true;
	
    private ScheduledFuture future;
	
    public void startCron() {

        future = LaborSchedulingConfig.getThreadPoolTaskScheduler().schedule(createRunnableTask(), createTrigger());

    }
 
    public void stop() {
        if (future != null) {
            future.cancel(true);
        }
    }

	
    /***
	 * run the task
	 * @return
	 */

	private Runnable createRunnableTask() {
		return new Runnable() {
			@Override
			public void run() {		
				// do the task					
				if (enabled) {
					doTask();
					LogManager.getLogger().debug("XXXXXXXXXXXXXgetId: "+Thread.currentThread().getName()
							+"|"+Thread.currentThread().getId()+"|"+Thread.currentThread().activeCount()+"|"+Thread.currentThread().getState());
					LogManager.getLogger().debug("XXXXXXXXXXXXXgetPoolSize: "+LaborSchedulingConfig.getThreadPoolTaskScheduler().getPoolSize());
				}
			}
		};

	}

	/**
	 * set the new cron
	 * @return
	 */
	private Trigger createTrigger() {
		return new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				// set the new cron to the trigger;
				String newCron = fetchNewCron();
				if (StringUtil.isEmpty(newCron)) {
					enabled = false;
				} else {
					enabled = true;
					cron = newCron;
				}
				CronTrigger trigger = new CronTrigger(cron);
				Date nextExec = trigger.nextExecutionTime(triggerContext);
				return nextExec;
			}
		};
	}
	

	public String getCron() {
		return cron;
	}



	public void setCron(String cron) {
		this.cron = cron;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
