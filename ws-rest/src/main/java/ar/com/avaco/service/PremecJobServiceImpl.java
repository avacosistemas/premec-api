package ar.com.avaco.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.avaco.entities.JobEnvioFormularioSap;
import ar.com.avaco.entities.JobReporteDiario;
import ar.com.avaco.ws.service.ActividadEPService;
import ar.com.avaco.ws.service.FormularioEPService;
import ar.com.avaco.ws.service.ReporteEPService;

@Service("premecJobService")
public class PremecJobServiceImpl implements PremecJobService {

	private static final JobKey ENVIO_FORMULARIO_SAP_JOB_KEY = new JobKey("envioFormularioSapJob",
			"envioFormularioSapGroup");

	private static final JobKey REPORTE_DIARIO_JOB_KEY = new JobKey("reporteDiarioJob", "reporteDiarioGroup");

	@Value("${cron.envioFormularioSap}")
	private String cronEnvioFormularioSap;

	@Value("${cron.reporteDiario}")
	private String cronReporteDiario;

	private static Scheduler scheduler;

	private ActividadEPService actividadEPService;

	private ReporteEPService reporteEPService;

	private FormularioEPService formularioEPService;

	private static final Logger LOGGER = Logger.getLogger(PremecJobService.class);

	@PostConstruct
	public void initScheduler() throws SchedulerException {

		if (scheduler == null) {

			LOGGER.debug("Inicio generacion procesos automaticos");

			scheduler = new StdSchedulerFactory().getScheduler();

			JobDataMap newJobDataMap = new JobDataMap();
			newJobDataMap.put("actividadEPService", actividadEPService);
			newJobDataMap.put("reporteEPService", reporteEPService);
			newJobDataMap.put("formularioEPService", formularioEPService);

			JobDetail jobReporteDiario = JobBuilder.newJob(JobReporteDiario.class)
					.withIdentity("reporteDiarioJob", "reporteDiarioGroup").usingJobData(newJobDataMap).build();
			Trigger triggerJobReporteDiario = TriggerBuilder.newTrigger()
					.withIdentity("reporteDiarioTrigger", "reporteDiarioGroup")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronReporteDiario)).build();
			scheduler.scheduleJob(jobReporteDiario, triggerJobReporteDiario);

			LOGGER.debug("Job Reporte Diario creado");

			JobDetail jobEnvioFormularioSap = JobBuilder.newJob(JobEnvioFormularioSap.class)
					.withIdentity("envioFormularioSapJob", "envioFormularioSapGroup").usingJobData(newJobDataMap)
					.build();
			Trigger triggerJobEnvioFormularioSap = TriggerBuilder.newTrigger()
					.withIdentity("envioFormularioSapTrigger", "envioFormularioSapGroup")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronEnvioFormularioSap)).build();
			scheduler.scheduleJob(jobEnvioFormularioSap, triggerJobEnvioFormularioSap);

			LOGGER.debug("Job Reporte Diario creado");

			scheduler.start();
//			scheduler.pauseAll();
		}
	}

	public void startStopEnvioFormularioSapJob() throws SchedulerException {
		startStopJob(ENVIO_FORMULARIO_SAP_JOB_KEY);
	}

	@Override
	public void startStopReporteDiarioJob() throws SchedulerException {
		startStopJob(REPORTE_DIARIO_JOB_KEY);
	}

	private void startStopJob(JobKey jobkey) throws SchedulerException {
		if (isJobRunning(jobkey)) {
			scheduler.pauseJob(jobkey);
		} else {
			scheduler.resumeJob(jobkey);
		}
	}

	@Override
	public boolean isEnvioFormularioSapJobRunning() throws SchedulerException {
		return isJobRunning(ENVIO_FORMULARIO_SAP_JOB_KEY);
	}

	@Override
	public boolean isReporteDiarioJobRunning() throws SchedulerException {
		return isJobRunning(REPORTE_DIARIO_JOB_KEY);
	}

	private boolean isJobRunning(JobKey jobkey) throws SchedulerException {
		JobDetail jobDetailReporteDiario = scheduler.getJobDetail(jobkey);
		List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetailReporteDiario.getKey());
		for (Trigger trigger : triggers) {
			TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			if (TriggerState.NORMAL.equals(triggerState)) {
				return true;
			}
		}
		return false;
	}

	@Resource(name = "reporteEPService")
	public void setReporteEPService(ReporteEPService reporteEPService) {
		this.reporteEPService = reporteEPService;
	}

	@Resource(name = "actividadService")
	public void setActividadEPService(ActividadEPService actividadEPService) {
		this.actividadEPService = actividadEPService;
	}

	@Resource(name = "formularioEPService")
	public void setFormularioEPService(FormularioEPService formularioEPService) {
		this.formularioEPService = formularioEPService;
	}

	public void setCronEnvioFormularioSap(String cronEnvioFormularioSap) {
		this.cronEnvioFormularioSap = cronEnvioFormularioSap;
	}

	public void setCronReporteDiario(String cronReporteDiario) {
		this.cronReporteDiario = cronReporteDiario;
	}

}
