package ar.com.avaco.ws.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import ar.com.avaco.ws.dto.actividad.ActividadReporteDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.reporte.InformeBuilder;
import ar.com.avaco.ws.rest.reporte.InformeBuilderChecklist;
import ar.com.avaco.ws.rest.reporte.InformeBuilderEntrega;
import ar.com.avaco.ws.rest.reporte.InformeBuilderPiezasRepararMantenimientoMaquinaria;
import ar.com.avaco.ws.rest.reporte.InformeBuilderReparaciones;
import ar.com.avaco.ws.rest.security.service.ReporteService;
import ar.com.avaco.ws.service.ReporteEPService;

@Service("reporteEPService")
public class ReporteEPServiceImpl implements ReporteEPService {

	@Value("${informe.path}")
	private String informePath;

	@Value("${email.bodyMail}")
	private String bodyMail;
	
	@Value("${email.subjectMail}")
	private String subjectMail;
	
	@Value("${email.bodyMailEntrega}")
	private String bodyMailEntrega;
	
	@Value("${email.subjectMailEntrega}")
	private String subjectMailEntrega;
	
	private String body;
	
	private String subject;
	
	private ReporteService reporteService;

	@Override
	public void enviarReporte(ActividadReporteDTO eldto) throws MalformedURLException, DocumentException, IOException {
		generarReporte(eldto);
		reporteService.sendMail(eldto.getEmail(), eldto.getIdActividad().toString(), body, subject);
	}
	
	@Override
	public ResponseEntity<JSONResponse> generarReporte(ActividadReporteDTO eldto)
			throws FileNotFoundException, DocumentException, IOException {
		InformeBuilder ib = null;

		switch (eldto.getTipoActividad()) {
		case "EE":
		case "EC":
		case "EP":
			ib = new InformeBuilderEntrega(eldto, informePath);
			body = bodyMailEntrega;
			subject = subjectMailEntrega;
			break;
		case "R":
			ib = new InformeBuilderReparaciones(eldto, informePath);
			body = bodyMail;
			subject = subjectMail;
			break;
		case "C":
			ib = new InformeBuilderChecklist(eldto, informePath);
			body = bodyMail;
			subject = subjectMail;
			break;
		case "P":
			ib = new InformeBuilderPiezasRepararMantenimientoMaquinaria(eldto, informePath);
			body = bodyMail;
			subject = subjectMail;
			break;
		case "M":
			ib = new InformeBuilderPiezasRepararMantenimientoMaquinaria(eldto, informePath);
			body = bodyMail;
			subject = subjectMail;
			break;
		default:
			break;
		}

		return ib.generarReporte();
	}

	@Resource(name = "reporteService")
	public void setService(ReporteService reporteService) {
		this.reporteService = reporteService;
	}

}
