package ar.com.avaco.ws.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.rest.controller.InformeBuilder;
import ar.com.avaco.ws.rest.security.service.ReporteService;
import ar.com.avaco.ws.service.ReporteEPService;

@Service("reporteEPService")
public class ReporteEPServiceImpl implements ReporteEPService{
	
	@Value("${informe.path}")
	private String informePath;
	
	private ReporteService reporteService;
	
	@Resource(name = "reporteService")
	public void setService(ReporteService reporteService) {
		this.reporteService = reporteService;
	}
	
	public void enviarReporte(ActividadReporteDTO eldto) throws MalformedURLException, DocumentException, IOException {
		InformeBuilder ib = new InformeBuilder(eldto);
		ib.generarReporte(informePath);
		reporteService.sendMail(eldto.getEmail(), eldto.getIdActividad().toString());
	}
}
