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

	private ActividadEPService actividadedEPService;
	private ReporteEPService reporteEPService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("ejecutando job");
		try {
			List<ActividadReporteDTO> actividadesReporte = this.actividadedEPService.getActividadesReporte();
			actividadesReporte.forEach(x -> {
				try {
					this.reporteEPService.enviarReporte(x);
					this.actividadedEPService.marcarEnviado(x.getIdActividad());
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
	public void setActividadedEPService(ActividadEPService actividadedEPService) {
		this.actividadedEPService = actividadedEPService;
	}

}
