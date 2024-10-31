package ar.com.avaco.ws.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.http.ResponseEntity;

import com.itextpdf.text.DocumentException;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;

public interface ReporteEPService {

	void enviarReporte(ActividadReporteDTO eldto) throws MalformedURLException, DocumentException, IOException;

	void generarReporteReparaciones(ActividadReporteDTO eldto) throws DocumentException, IOException;

	void generarReporteTestReparaciones();

	void generarReporteChecklist(ActividadReporteDTO eldto) throws DocumentException, IOException;

	void generarReporteTestChecklist();

	void generarReporteTestMantenimiento();

	void generarReporteTestPiezas();

	void generarReporteMantenimientoPiezas(ActividadReporteDTO eldto) throws DocumentException, IOException;

	ResponseEntity<JSONResponse> generarReporte(ActividadReporteDTO eldto) throws FileNotFoundException, DocumentException, IOException;

}
