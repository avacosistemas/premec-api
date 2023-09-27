package ar.com.avaco.entities;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.service.ActividadEPService;
import ar.com.avaco.ws.service.ReporteEPService;

@Service
public class JobReporteDiario implements Job {

	private ActividadEPService actividadEPService;
	private ReporteEPService reporteEPService;

	private final static Logger LOGGER = Logger.getLogger(JobReporteDiario.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		actividadEPService = (ActividadEPService) context.getJobDetail().getJobDataMap().get("actividadEPService");
		reporteEPService = (ReporteEPService) context.getJobDetail().getJobDataMap().get("reporteEPService");

		LOGGER.debug("Iniciando proceso de envio de reportes de actividades");
		try {
			List<ActividadReporteDTO> actividadesReporte = this.actividadEPService.getActividadesReporte();
			actividadesReporte.forEach(x -> {
				try {
				
					LOGGER.debug("Iniciando envio reporte actividad " + x.getIdActividad());
					this.reporteEPService.enviarReporte(x);
					LOGGER.debug("Reporte actividad " + x.getIdActividad() + " enviado");
					
					LOGGER.debug("Marcando actividad " + x.getIdActividad() + " como enviada en SAP");
					this.actividadEPService.marcarEnviado(x.getIdActividad());
					LOGGER.debug("Actividad " + x.getIdActividad() + " marcada como enviada en SAP");
				
				} catch (SapBusinessException e) {
					LOGGER.debug("No se pudo marcar la actividad " + x.getIdActividad() + " como enviada");
					LOGGER.debug(e.getMessage());
					e.printStackTrace();

				} catch (Exception e) {
					LOGGER.debug("No se pudo enviar el reporte de la actividad " + x.getIdActividad());
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			LOGGER.debug("No se pudo obtener el listado de actividades para enviar reporte");
			e.printStackTrace();
		}
		LOGGER.debug("Finalizado proceso de envio de reportes de actividades");
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
