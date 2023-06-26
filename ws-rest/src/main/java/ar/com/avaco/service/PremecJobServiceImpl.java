package ar.com.avaco.service;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import ar.com.avaco.entities.JobReporteDiario;


@Service("premecJobService")
public class PremecJobServiceImpl implements PremecJobService {

	private static Scheduler scheduler;
	
	public PremecJobServiceImpl() throws SchedulerException {
		if (scheduler == null) {
			scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail job1 = JobBuilder.newJob(JobReporteDiario.class).withIdentity("reporteDiarioJob", "premecGroup")
					.build();
			Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("cronTrigger", "premecGroup")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * ? * * *")).build();
			scheduler.scheduleJob(job1, trigger1);
			scheduler.start();
			scheduler.pauseAll();
		}
	}

	@Override
	public void startStopJobService() throws SchedulerException, InterruptedException {
		if (isJobRunning()) {
			scheduler.pauseAll();
		} else {
			scheduler.resumeAll();
		}
	}

	@Override
	public boolean isJobRunning() throws SchedulerException {
		JobKey jobKey = new JobKey("reporteDiarioJob", "premecGroup");
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
		for (Trigger trigger : triggers) {
			TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			if (TriggerState.NORMAL.equals(triggerState)) {
				return true;
			}
		}
		return false;
	}

}
