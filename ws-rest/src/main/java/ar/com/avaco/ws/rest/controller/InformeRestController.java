package ar.com.avaco.ws.rest.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ar.com.avaco.ws.rest.dto.JSONResponse;

@RestController
public class InformeRestController {

	private final static Font fontHeaderSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
	private final static Font fontHeaderTable = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
	private final static Font fontHeaderTableChecks = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
			BaseColor.WHITE);
	private final static Font fontText = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);

	@RequestMapping(value = "/reporte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> getReporte() throws DocumentException, MalformedURLException, IOException {

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("C:\\desarrollo\\iTextHelloWorld.pdf"));
		document.open();

		addLogo(document);

		addActividad(document);

		addInformacionDetallada(document);

		addDetalle(document);

		addTareasARealizar(document);

		addGrillaChecks(document);

		addObservacionesGenerales(document);

		addRepuestos(document);

		addOperarios(document);

		addValoracion(document);

		addComentariosValoracion(document);

		document.close();
		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	private void addValoracion(Document document) throws DocumentException {

		addHeader("VALORACION", document);

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(new float[] { 20, 60, 20 });

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Resultado", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Nombre Superior", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("DNI Superior", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Muy Bueno", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Martin Espinoza", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("15101901", fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
	}

	private void addActividad(Document document) throws DocumentException {

		addHeader("Actividad", document);

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(4);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Prioridad", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Alta", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Numero", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("123456", fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Asignado por", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Servicios", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Lamada Id", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("10855", fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Empleado", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Kevin Rolon", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("10/10/2020", fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase(""));
		table.addCell(cell);
		cell.setPhrase(new Phrase(""));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Hora", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("15:45", fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
	}

	private void addRepuestos(Document document) throws DocumentException {

		addHeader("REPUESTOS", document);

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(4);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Numero de Articulo", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Descripcion", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Cantidad", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Numero de Serie", fontHeaderTable));
		table.addCell(cell);

		for (int i = 1; i <= 3; i++) {
			cell.setPhrase(new Phrase("0001534" + i, fontText));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Repuesto " + i, fontText));
			table.addCell(cell);
			cell.setPhrase(new Phrase(new Integer(i * 3).toString(), fontText));
			table.addCell(cell);
			cell.setPhrase(
					new Phrase(new Integer(("nro" + i).toString().hashCode()).toString().substring(0, 5), fontText));
			table.addCell(cell);
		}

		p.add(table);

		document.add(p);
	}

	private void addOperarios(Document document) throws DocumentException {

		addHeader("OPERARIOS", document);

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(4);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Fecha Inicio", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Hora Inicio", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha Fin", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Hora Fin", fontHeaderTable));
		table.addCell(cell);

		cell.setPhrase(new Phrase("10/10/2020", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("15:50 ", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("10/10/2020", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("16:30 ", fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
	}

	private void addDetalle(Document document) throws DocumentException {

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(new float[] { 20, 80 });

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Detalle", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(
				new Phrase("No sube bien el elevador. Se frena a los 3 metros y deberia subir hasta 5.", fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
	}

	private void addComentariosValoracion(Document document) throws DocumentException {

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(new float[] { 20, 80 });

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Comentarios", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Muy buen servicio se pudo resolver todo.", fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
	}

	private void addTareasARealizar(Document document) throws DocumentException {

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(new float[] { 20, 80 });

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Tareas a Realizar", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(
				"Se debe reemplazar la pieza por otra que funcione bien porque la que tiene funciona mal por eso hay que cambiarla",
				fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
	}

	private void addObservacionesGenerales(Document document) throws DocumentException {

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(new float[] { 20, 80 });

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Observaciones Generales", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(
				"Observaciones Observaciones Observaciones Observaciones Observaciones Observaciones Observaciones Observaciones ",
				fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
	}

	private void addGrillaChecks(Document document) throws DocumentException {

		addHeader("COMPROBACIONES", document);

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(new float[] { 5, 35, 10, 50 });

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(18);
		cell.setBackgroundColor(BaseColor.DARK_GRAY);

		cell.setPhrase(new Phrase("", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Estado", fontHeaderTableChecks));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Comentarios", fontHeaderTableChecks));
		table.addCell(cell);

		addHeaderCheckGrilla("1", "SISTEMA ELECTRICO", table);
		agregarCheckGrilla("a", "Tambor de arranque", "ok", "", table);
		agregarCheckGrilla("b", "Alternador", "no aplica", "", table);
		agregarCheckGrilla("c", "Motor de arranque", "ok", "", table);
		agregarCheckGrilla("d", "Luces gral", "no ok", "Se debe cambiar", table);

		addHeaderCheckGrilla("2", "FRENOS", table);
		agregarCheckGrilla("a", "Comando de marcha", "ok", "", table);
		agregarCheckGrilla("b", "Fluido caja/diferencial", "no aplica", "", table);
		agregarCheckGrilla("c", "Electrovalvulas de marcha", "no ok", "Se apagan", table);

		p.add(table);

		document.add(p);
	}

	private void addHeaderCheckGrilla(String id, String titulo, PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(14);
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPhrase(new Phrase(id, fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(titulo, fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("", fontText));
		table.addCell(cell);
	}

	private void agregarCheckGrilla(String id, String titulo, String estado, String observaciones, PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(14);
		cell.setPhrase(new Phrase(id, fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(titulo, fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(estado, fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(observaciones, fontText));
		table.addCell(cell);
	}

	private void addInformacionDetallada(Document document) throws DocumentException {

		addHeader("Información Detallada", document);

		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(4);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(20);

		cell.setPhrase(new Phrase("Codigo Articulo", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("BT RRE 9955", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha Inicio", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("02/08/2022", fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Nro de Serie", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("A230", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Cliente", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PILISR SA", fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Nro Fabricante", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("356988A Llave", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Direccion", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Mexico 2929", fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Horas Máquina", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("90", fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Con Cargo", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Si", fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
	}

	private void addLogo(Document document)
			throws BadElementException, MalformedURLException, IOException, DocumentException {
		Image logo = Image.getInstance("C:\\desarrollo\\logopremec.png");
		logo.scaleToFit(300, 150);
		document.add(logo);
	}

	private void addHeader(String titulo, Document document) throws DocumentException {
		Paragraph p = new Paragraph();
		p.setFont(fontText);
		PdfPTable tableHeader = new PdfPTable(1);
		PdfPCell cellHeader = new PdfPCell();
		cellHeader.setHorizontalAlignment(1);
		cellHeader.setPhrase(new Phrase(titulo, fontHeaderSection));
		cellHeader.setFixedHeight(20);
		cellHeader.setBackgroundColor(BaseColor.BLACK);
		cellHeader.setBorder(0);
		tableHeader.addCell(cellHeader);
		p.add(tableHeader);
		document.add(new Paragraph(new Phrase("  ")));
		document.add(p);
		document.add(new Paragraph(new Phrase("  ")));
	}

}
