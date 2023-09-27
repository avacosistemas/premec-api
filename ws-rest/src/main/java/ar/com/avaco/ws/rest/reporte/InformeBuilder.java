package ar.com.avaco.ws.rest.reporte;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.h2.util.StringUtils;
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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.dto.ItemCheckDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;

public class InformeBuilder {

	private static final BaseColor COLOR_GRIS_BORDES = new BaseColor(238, 238, 238);

	private final static Font fontHeaderTable = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
	private final static Font fontHeaderTableChecks = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
			new BaseColor(27,26,57));
	private final static Font fontText = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

	private ActividadReporteDTO dto;

	public InformeBuilder(ActividadReporteDTO eldto) {
		this.dto = eldto;
	}

	public ResponseEntity<JSONResponse> generarReporte(String informePath) throws DocumentException, IOException {

		Document document = new Document(PageSize.A4);
		
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(informePath + "\\" + dto.getIdActividad().toString() + ".pdf"));

			writer.setPageEvent(new PDFEventHelper());

			document.open();
			document.setMargins(20, 20, 20, 70);
			document.add(new Paragraph(new Phrase(" ")));
			Paragraph p = new Paragraph();
			p.add(new Phrase("INFORME TÉCNICO", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.WHITE)));
			p.setIndentationLeft(25);
			document.add(p);
			document.add(new Paragraph(new Phrase(" ")));
			document.add(new Paragraph(new Phrase(" ")));
			
			addActividad(document);

			addInformacionDetallada(document);

//			addDetalle(document);

//			addTareasARealizar(document);

			addGrillaChecks(document);

//			addObservacionesGenerales(document);

//			addRepuestos(document);

			addOperarios(document);

			addValoracion(document);

//			addComentariosValoracion(document);

// Se quita el incluir las imagenes en el informe por pedido del cliente 3/7/2023
//			addImages(document, informePath);

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
		File[] fileList = new File(informePath + "\\fotosactividades\\" + dto.getIdActividad()).listFiles();
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
		PdfPTable table = new PdfPTable(new float[] { 25, 50, 25 });
		table.setWidthPercentage(100);
		PdfPCell cell = getPDFPCell();
		
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
		cell.setBorder(0);
		
		cell.setPhrase(new Phrase("Comentarios", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getValoracionComentarios(), fontText));
		cell.setColspan(2);
		table.addCell(cell);

		addSeccion(document, table, "Valoración");
		
	}

	private void addActividad(Document document) throws DocumentException {
		PdfPTable table = generateTable(4);
		PdfPCell cell = getPDFPCell();

		cell.setPhrase(new Phrase("Prioridad", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getPrioridad(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Numero", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getNumero(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Supervisor", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getAsignadoPor(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Lamada Id", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getLlamadaID(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Técnico", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getEmpleado(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getFecha(), fontText));
		table.addCell(cell);

		cell.setBorder(0);
		
		cell.setPhrase(new Phrase(""));
		table.addCell(cell);
		cell.setPhrase(new Phrase(""));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Hora", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getHora(), fontText));
		table.addCell(cell);

		addSeccion(document, table, "Actividad");
	}

	private PdfPTable generateTable(int columns) {
		PdfPTable table = new PdfPTable(columns);
		table.setWidthPercentage(100);
		return table;
	}

	private PdfPCell getPDFPCell() {
		PdfPCell cell = new PdfPCell();
		cell.setUseAscender(true);
		cell.setBorder(0);
		cell.setMinimumHeight(25);
		cell.setBorderWidthBottom(1);
		cell.setBorderColorBottom(COLOR_GRIS_BORDES);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(10);
		cell.setPaddingLeft(new Float("7.3"));
		cell.setPaddingRight(new Float("7.3"));
		return cell;
	}

	private void addSeccion(Document document, PdfPTable table) throws DocumentException {
		addSeccion(document, table, null);
	}

	private void addSeccion(Document document, PdfPTable table, String titulo) throws DocumentException {
		addSeccion(document, table, titulo, false, false);
	}
	
	private void addSeccion(Document document, PdfPTable table, String titulo, boolean blue, boolean centered) throws DocumentException {
		Paragraph p = new Paragraph();
		PdfPTable tableBorder = new PdfPTable(1);
		tableBorder.setWidthPercentage(100);
		PdfPCell cellBorder = new PdfPCell();
		if (!StringUtils.isNullOrEmpty(titulo))
			if (blue)
				cellBorder.addElement(addHeaderBlue(titulo,0));
			else
				cellBorder.addElement(addHeader(titulo));
		if (centered)
			cellBorder.setHorizontalAlignment(Element.ALIGN_CENTER);
		Paragraph ptable = new Paragraph();
		ptable.add(table);
		cellBorder.addElement(ptable);
		cellBorder.setCellEvent(new RoundRectangle());
		cellBorder.setBorder(Rectangle.NO_BORDER);
		cellBorder.setPadding(20);
		cellBorder.setPaddingTop(15);
		cellBorder.setPaddingBottom(4);
		tableBorder.addCell(cellBorder);
		tableBorder.setSpacingAfter(10);
		p.add(tableBorder);
		document.add(p);
	}

	private void addRepuestos(Document document) throws DocumentException {

//		addHeader("REPUESTOS", document);

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

		PdfPTable table = generateTable(4);
		PdfPCell cell = getPDFPCell();

		cell.setPhrase(new Phrase("Fecha Inicio", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Hora Inicio", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Fecha Fin", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Hora Fin", fontHeaderTable));
		table.addCell(cell);

		cell.setBorder(0);
		
		cell.setPhrase(new Phrase(dto.getFechaInicioOperario(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getHoraInicioOperario(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getFechaFinoOperario(), fontText));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getHoraFinOperario(), fontText));
		table.addCell(cell);

		addSeccion(document, table, "Operarios");
	}

	private void addDetalle(Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(new float[] { 20, 80 });
		PdfPCell cell = getPDFPCell();
		cell.setPhrase(new Phrase("Detalle", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getDetalle(), fontText));
		table.addCell(cell);
		addSeccion(document, table);
	}

	private void addComentariosValoracion(Document document) throws DocumentException {
		Paragraph p = new Paragraph();

		PdfPTable table = new PdfPTable(new float[] { 20, 80 });
		PdfPCell cell = getPDFPCell();

		cell.setPhrase(new Phrase("Comentarios", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getValoracionComentarios(), fontText));
		table.addCell(cell);

		p.add(table);

		document.add(p);
		addSeccion(document, table);
	}

	private void addTareasARealizar(Document document) throws DocumentException {

		PdfPTable table = new PdfPTable(new float[] { 20, 80 });
		PdfPCell cell = getPDFPCell();
		cell.setPhrase(new Phrase("Tareas a Realizar", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getTareasARealizar(), fontText));
		table.addCell(cell);
		addSeccion(document, table);
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

//		document.newPage();
		
//		document.add(addHeaderBlue("COMPROBACIONES", new Float("53.5")));

		PdfPTable table = new PdfPTable(new float[] { 5, 35, 10 });
		table.setWidthPercentage(100);
		
		PdfPCell cell = new PdfPCell();
		
		cell.setUseAscender(true);
		cell.setBorder(0);
		cell.setFixedHeight(20);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(new BaseColor(238,238,238));
		
		cell.setPhrase(new Phrase("", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase("", fontHeaderTable));
		table.addCell(cell);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPhrase(new Phrase("Estado", fontHeaderTableChecks));
		table.addCell(cell);
//		cell.setPhrase(new Phrase("Comentarios", fontHeaderTableChecks));
//		table.addCell(cell);

		List<String> keySet = new ArrayList<String>(dto.getChecks().keySet());
		Collections.sort(keySet);

		Integer headerIndex = 1;

		for (String titulo : keySet) {

			addHeaderCheckGrilla(headerIndex.toString(), titulo, table);

			headerIndex++;

			List<ItemCheckDTO> items = dto.getChecks().get(titulo);

			int index = 65;
			for (ItemCheckDTO y : items) {
				String indexChar = String.valueOf((char) index);
				agregarCheckGrilla(indexChar, y.getNombre(), y.getEstado(), y.getObservaciones(), table, items.indexOf(y) == items.size() - 1);
				index++;
			};
		};

		addSeccion(document, table, "COMPROBACIONES", true, true);
	}

	private void addHeaderCheckGrilla(String id, String titulo, PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setUseAscender(true);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(0);
		cell.setFixedHeight(25);
		cell.setBorderColorBottom(new BaseColor(46, 42, 109));
		cell.setPaddingTop(10);
		cell.setBorderWidthBottom(1);
		cell.setPhrase(new Phrase(id, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new BaseColor(46, 42, 109))));
		table.addCell(cell);
		cell.setPhrase(new Phrase(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new BaseColor(46, 42, 109))));
		table.addCell(cell);
		cell.setPhrase(new Phrase("", fontText));
		table.addCell(cell);
//		cell.setPhrase(new Phrase("", fontText));
//		table.addCell(cell);
	}

	private void agregarCheckGrilla(String id, String titulo, String estado, String observaciones, PdfPTable table, boolean ultimo) {
		PdfPCell cell = new PdfPCell();
		cell.setUseAscender(true);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(0);
		cell.setFixedHeight(20);
		if (!ultimo) {
			cell.setBorderColorBottom(COLOR_GRIS_BORDES);
			cell.setBorderWidthBottom(1);
		}
		cell.setPhrase(new Phrase(id, fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(titulo, fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(estado, fontText));
		table.addCell(cell);
//		cell.setPhrase(new Phrase(observaciones, fontText));
//		table.addCell(cell);
	}

	private void addInformacionDetallada(Document document) throws DocumentException {

		PdfPTable table = generateTable(4);
		PdfPCell cell = getPDFPCell();

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
//		cell.setPhrase(new Phrase(dto.getConCargo() != null && dto.getConCargo().equals("Y") ? "Si" : "No", fontText));
		cell.setPhrase(new Phrase(dto.getConCargo()));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Detalle", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getDetalle(), fontText));
		cell.setColspan(3);
		table.addCell(cell);

		cell.setBorder(0);
		
		cell.setPhrase(new Phrase("Tareas a Realizar", fontHeaderTable));
		cell.setColspan(0);
		table.addCell(cell);
		cell.setPhrase(new Phrase(dto.getTareasARealizar(), fontText));
		cell.setColspan(3);
		table.addCell(cell);
		
		addSeccion(document, table, "Información Detallada");
	}

	private void addLogo(Document document, String informePath)
			throws BadElementException, MalformedURLException, IOException, DocumentException {
		Image logo = Image.getInstance(informePath + "\\logopremec.png");
		logo.scaleToFit(300, 150);
		document.add(logo);
	}
	private Element addHeader(String titulo) {
		return addHeader(titulo, 0);
	}
	
	private Element addHeader(String titulo, float paddingleftright) {

		Font fontHeaderSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new BaseColor(44, 45, 114));

		Paragraph p = new Paragraph();
		PdfPTable tableHeader = new PdfPTable(1);
		tableHeader.setWidthPercentage(100);
		PdfPCell cellHeader = new PdfPCell();

		cellHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellHeader.setPhrase(new Phrase(titulo.toUpperCase(), fontHeaderSection));
		cellHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellHeader.setBorder(0);
		cellHeader.setBackgroundColor(COLOR_GRIS_BORDES);
		cellHeader.setBorderColorBottom(new BaseColor(228, 228, 228));
		cellHeader.setBorderWidthBottom(1);
		cellHeader.setPadding(8);
//		cellHeader.setPaddingBottom(8);

		tableHeader.addCell(cellHeader);
		p.setIndentationLeft(paddingleftright);
		p.setIndentationRight(paddingleftright);
		p.add(tableHeader);
//		p.setSpacingAfter(4);
		return p;
	}

	private Element addHeaderBlue(String titulo, float paddingleftright) {
		
		Font fontHeaderSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
		
		Paragraph p = new Paragraph();
		PdfPTable tableHeader = new PdfPTable(1);
		tableHeader.setWidthPercentage(100);
		PdfPCell cellHeader = new PdfPCell();
		
		cellHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
		cellHeader.setPhrase(new Phrase(titulo.toUpperCase(), fontHeaderSection));
		cellHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellHeader.setBorder(0);
		cellHeader.setBackgroundColor(new BaseColor(27,26,57));
//		cellHeader.setBorderColorBottom(new BaseColor(228, 228, 228));
		cellHeader.setBorderWidthBottom(1);
		cellHeader.setPadding(8);
		cellHeader.setPaddingBottom(8);
		
		tableHeader.addCell(cellHeader);
		p.setIndentationLeft(paddingleftright);
		p.setIndentationRight(paddingleftright);
		p.add(tableHeader);
		p.setSpacingAfter(8);
		return p;
	}

	public class RoundRectangle implements PdfPCellEvent {
		public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.BASECANVAS];
			cb.roundRectangle(rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3, rect.getHeight() - 3,
					4);
			cb.setColorFill(BaseColor.WHITE);
			cb.fill();
			
			PdfContentByte cb2 = canvas[PdfPTable.LINECANVAS];
			cb2.roundRectangle(rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3, rect.getHeight() - 3,
					4);
			cb2.setColorStroke(COLOR_GRIS_BORDES);
			cb2.stroke();
		}
	}

}
