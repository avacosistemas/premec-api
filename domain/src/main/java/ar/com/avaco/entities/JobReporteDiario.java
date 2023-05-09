package ar.com.avaco.entities;

import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobReporteDiario implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("JobReporteDiario --->>> Hello user! Time is " + Calendar.getInstance().toString());  
	}

}
