package ar.com.avaco.service;

import org.quartz.SchedulerException;

public interface PremecJobService {

	void startStopEnvioFormularioSapJob() throws SchedulerException;

	void startStopReporteDiarioJob() throws SchedulerException;

	boolean isEnvioFormularioSapJobRunning() throws SchedulerException;

	boolean isReporteDiarioJobRunning() throws SchedulerException;

}
