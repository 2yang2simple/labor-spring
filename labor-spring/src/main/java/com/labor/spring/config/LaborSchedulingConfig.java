package com.labor.spring.config;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.labor.common.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Configuration
@EnableScheduling
public class LaborSchedulingConfig implements SchedulingConfigurer {

	

	private static ThreadPoolTaskScheduler scheduler;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		TaskScheduler taskScheduler = getThreadPoolTaskScheduler();
		taskRegistrar.setTaskScheduler(taskScheduler);
	}

//	@Bean(destroyMethod = "shutdown", name="threadPoolTaskScheduler")
//    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
//		ThreadPoolTaskScheduler	scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(20);
//        scheduler.setThreadNamePrefix("Labor-Executor-");
//        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        scheduler.setWaitForTasksToCompleteOnShutdown(true);
//        scheduler.setAwaitTerminationSeconds(60);
//        LogManager.getLogger().debug(scheduler.getPoolSize());
//        return scheduler;
//    }


    public static ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
		if (scheduler==null) {
			scheduler = new ThreadPoolTaskScheduler();
	        scheduler.setPoolSize(20);
	        scheduler.setThreadNamePrefix("Labor-Scheduler-");
	        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	        scheduler.setWaitForTasksToCompleteOnShutdown(true);
	        scheduler.setAwaitTerminationSeconds(60);
	        scheduler.initialize();
		}
        return scheduler;
    }


}