package ar.com.avaco.ws.service;

import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.DocumentException;

import ar.com.avaco.ws.dto.ActividadReporteDTO;

public interface ReporteEPService {

	public void enviarReporte(ActividadReporteDTO eldto) throws MalformedURLException, DocumentException, IOException;

	public void generarReporte(ActividadReporteDTO eldto) throws DocumentException, IOException;

}
