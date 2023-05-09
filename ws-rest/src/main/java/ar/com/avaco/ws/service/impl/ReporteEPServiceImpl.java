package ar.com.avaco.ws.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ar.com.avaco.ws.rest.security.service.ReporteService;
import ar.com.avaco.ws.service.ReporteEPService;

@Service("reporteEPService")
public class ReporteEPServiceImpl implements ReporteEPService{
	
	private ReporteService reporteService;
	
	@Resource(name = "reporteService")
	public void setService(ReporteService reporteService) {
		this.reporteService = reporteService;
	}
	
	public void enviarReporte() {
		reporteService.sendMail();
	}
}
