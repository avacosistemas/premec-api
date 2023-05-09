package ar.com.avaco.service;

import org.quartz.SchedulerException;

public interface PremecJobService {

	boolean isJobRunning() throws SchedulerException;

	void startStopJobService() throws SchedulerException, InterruptedException;

}
