package ar.com.avaco.ws.rest.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.dto.ItemCheckDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;

public class InformeBuilder {

	private final static Font fontHeaderSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
	private final static Font fontHeaderTable = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
	private final static Font fontHeaderTableChecks = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
			BaseColor.WHITE);
	private final static Font fontText = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);

	private ActividadReporteDTO dto;

	public InformeBuilder(ActividadReporteDTO eldto) {
		this.dto = eldto;
	}

	public ResponseEntity<JSONResponse> generarReporte(String informePath) throws DocumentException, IOException {

		Document document = new Document();
		try {
			PdfWriter.getInstance(document,
					new FileOutputStream(informePath + dto.getIdActividad().toString() + ".pdf"));
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

			addImages(document, informePath);

		} catch (DocumentException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			document.close();
		}
		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	private void addImages(Document document, String informePath) {
		File[] fileList = new File(informePath + "fotosactividades\\" + dto.getIdActividad()).listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				Image img;
				try {
					Paragraph p = new Paragraph();
					img = Image.getInstance(file.getAbsolutePath());
					float fitWidth = img.getWidth() > 700 ? 700 : img.getWidth();
					img.scaleToFit(fitWidth, fitWidth * img.getWidth() / img.getHeight());
					img.setAlignment(Element.ALIGN_CENTER);
					p.add(img);
					document.add(p);
				} catch (IOException | DocumentException e) {
					document.close();
					e.printStackTrace();
				}
			}
		}
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
		cell.setPhrase(new Phrase(dto.getValoracionResultado(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getValoracionNombreSuperior(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getValoracionDNISuperior(), fontText));
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
		cell.setPhrase(new Phrase(dto.getPrioridad(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Numero", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getNumero(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Asignado por", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getAsignadoPor(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Lamada Id", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getLlamadaID(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Empleado", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getEmpleado(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getFecha(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase(""));
		table.addCell(cell);
		cell.setPhrase(new Phrase(""));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Hora", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getHora(), fontText));
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

		dto.getRepuestos().forEach(x -> {
			cell.setPhrase(new Phrase(x.getNroArticulo(), fontText));
			table.addCell(cell);
			cell.setPhrase(new Phrase(x.getDescripcion(), fontText));
			table.addCell(cell);
			cell.setPhrase(new Phrase(String.valueOf(x.getCantidad()), fontText));
			table.addCell(cell);
			cell.setPhrase(new Phrase(x.getNroSerie(), fontText));
			table.addCell(cell);
		});

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

		cell.setPhrase(new Phrase(dto.getFechaInicioOperario(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getHoraInicioOperario(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getFechaFinoOperario(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getHoraFinOperario(), fontText));
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
		cell.setPhrase(new Phrase(dto.getDetalle(), fontText));
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
		cell.setPhrase(new Phrase(dto.getValoracionComentarios(), fontText));
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
		cell.setPhrase(new Phrase(dto.getTareasARealizar(), fontText));
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
		cell.setPhrase(new Phrase(dto.getObservacionesGenerales(), fontText));
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

		List<String> keySet = new ArrayList<String>(dto.getChecks().keySet());
		Collections.sort(keySet);

		keySet.forEach(titulo -> {

			Integer headerIndex = 1;

			addHeaderCheckGrilla(headerIndex.toString(), titulo, table);

			headerIndex++;

			List<ItemCheckDTO> items = dto.getChecks().get(titulo);

			items.forEach(y -> {
				int index = 65;
				String indexChar = String.valueOf((char) index);
				agregarCheckGrilla(indexChar, y.getNombre(), y.getEstado(), y.getObservaciones(), table);
			});
		});

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
		cell.setPhrase(new Phrase(dto.getCodigoArticulo(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha Inicio", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getFecha(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Nro de Serie", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getNroSerie(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Cliente", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getCliente(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Nro Fabricante", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getNroFabricante(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Direccion", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getDireccion(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Horas Máquina", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(String.valueOf(dto.getHorasMaquina()), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Con Cargo", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getConCargo() != null && dto.getConCargo().equals("Y") ? "Si" : "No", fontText));
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
