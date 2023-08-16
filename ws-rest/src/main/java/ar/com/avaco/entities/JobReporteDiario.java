package ar.com.avaco.entities;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.service.ActividadEPService;
import ar.com.avaco.ws.service.ReporteEPService;

@Service
public class JobReporteDiario implements Job {

	private ActividadEPService actividadEPService;
	private ReporteEPService reporteEPService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		actividadEPService = (ActividadEPService) context.getJobDetail().getJobDataMap().get("actividadEPService");
		reporteEPService = (ReporteEPService) context.getJobDetail().getJobDataMap().get("reporteEPService");
		
		try {
			List<ActividadReporteDTO> actividadesReporte = this.actividadEPService.getActividadesReporte();
			actividadesReporte.forEach(x -> {
				try {
					this.reporteEPService.enviarReporte(x);
					this.actividadEPService.marcarEnviado(x.getIdActividad());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Resource(name = "reporteEPService")
	public void setReporteEPService(ReporteEPService reporteEPService) {
		this.reporteEPService = reporteEPService;
	}

	@Resource(name = "actividadService")
	public void setActividadEPService(ActividadEPService actividadEPService) {
		this.actividadEPService = actividadEPService;
	}
	
}
