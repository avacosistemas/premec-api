package ar.com.avaco.ws.rest.reporte;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.http.ResponseEntity;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;

public abstract class InformeBuilder {

	public abstract ResponseEntity<JSONResponse> generarReporte() throws DocumentException, IOException;

	protected ActividadReporteDTO dto;
	protected final String path;
	protected Document document = new Document(PageSize.A4);
	protected PdfWriter writer; 
	
	public InformeBuilder(ActividadReporteDTO eldto, String informePath) throws FileNotFoundException, DocumentException {
		this.dto = eldto;
		this.path = informePath + "\\" + dto.getIdActividad().toString() + ".pdf";
		this.writer = PdfWriter.getInstance(document, new FileOutputStream(path));
	}
	
}
