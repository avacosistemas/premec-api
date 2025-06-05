package ar.com.avaco.ws.rest.reporte;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.h2.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

import ar.com.avaco.ws.dto.actividad.ActividadReporteDTO;
import ar.com.avaco.ws.dto.formulario.ItemCheckDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;

public class InformeBuilderChecklist extends InformeBuilder {

	private static final Font FONT_CABECERA_CHECK = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
			new BaseColor(43, 45, 114));

	private static final BaseColor COLOR_GRIS_FONDO = new BaseColor(27, 26, 57);

	private static final BaseColor COLOR_GRIS_BORDES = new BaseColor(204, 204, 204);

	private final static Font fontHeaderTable = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
	private final static Font fontHeaderTableChecks = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
			COLOR_GRIS_FONDO);
	private final static Font fontText = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

	public InformeBuilderChecklist(ActividadReporteDTO eldto, String informePath)
			throws FileNotFoundException, DocumentException {
		super(eldto, informePath);
	}

	public ResponseEntity<JSONResponse> generarReporte() throws DocumentException, IOException {

		try {

			writer.setPageEvent(new PDFEventHelper());

			document.open();
			document.setMargins(20, 20, 20, 70);
			document.add(new Paragraph(new Phrase(" ")));
			Paragraph p = new Paragraph();
			p.add(new Phrase("CHEQUEO DE ENTREGA",
					FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.WHITE)));
			p.setIndentationLeft(25);
			document.add(p);
			document.add(new Paragraph(new Phrase(" ")));
			document.add(new Paragraph(new Phrase(" ")));

			addHeader(document);

			addGrillaChecks(document);

			addFooter(document);

		} catch (DocumentException e) {
			e.printStackTrace();
			throw e;
		} finally {
			document.close();
		}
		JSONResponse response = new JSONResponse();
		response.setData(Files.readAllBytes(Paths.get(super.path)));
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	private PdfPCell getPDFPCell() {
		PdfPCell cell = new PdfPCell();
		cell.setUseAscender(true);
		cell.setBorder(0);
		cell.setMinimumHeight(15);
		cell.setBorderWidthBottom(1);
		cell.setBorderColorBottom(COLOR_GRIS_BORDES);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(3);
		cell.setPaddingLeft(new Float("7.3"));
		cell.setPaddingRight(new Float("7.3"));
		return cell;
	}

	private PdfPCell getPDFPCellBordeCompleto() {
		PdfPCell cell = new PdfPCell();
		cell.setUseAscender(true);
		cell.setBorder(0);
		cell.setMinimumHeight(15);
		cell.setBorderWidthBottom(1);
		cell.setBorderColorBottom(COLOR_GRIS_BORDES);
		cell.setBorderWidthLeft(1);
		cell.setBorderColorLeft(COLOR_GRIS_BORDES);
		cell.setBorderWidthRight(1);
		cell.setBorderColorRight(COLOR_GRIS_BORDES);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(3);
		cell.setPaddingLeft(new Float("7.3"));
		cell.setPaddingRight(new Float("7.3"));
		return cell;
	}

	private PdfPCell getPDFPCellBordeCompletoRellenoGris() {
		PdfPCell cell = new PdfPCell();
		cell.setUseAscender(true);
		cell.setBorder(0);
		cell.setMinimumHeight(15);
		cell.setBorderWidthTop(1);
		cell.setBorderColorTop(COLOR_GRIS_BORDES);
		cell.setBorderWidthLeft(1);
		cell.setBorderColorLeft(COLOR_GRIS_BORDES);
		cell.setBorderWidthRight(1);
		cell.setBorderColorRight(COLOR_GRIS_BORDES);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(8);
		cell.setPaddingLeft(new Float("7.3"));
		cell.setPaddingRight(new Float("7.3"));
		cell.setBackgroundColor(new BaseColor(238, 238, 238));
		return cell;
	}

	private void addSeccion(Document document, PdfPTable table, String titulo) throws DocumentException {
		addSeccion(document, table, titulo, false, false);
	}

	private void addSeccion(Document document, PdfPTable table, String titulo, boolean blue, boolean centered)
			throws DocumentException {
		Paragraph p = new Paragraph();
		PdfPTable tableBorder = new PdfPTable(1);
		tableBorder.setWidthPercentage(100);
		PdfPCell cellBorder = new PdfPCell();
		if (!StringUtils.isNullOrEmpty(titulo))
			if (blue)
				cellBorder.addElement(addHeaderBlue(titulo, 0));
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
		cellBorder.setPaddingBottom(15);
		tableBorder.addCell(cellBorder);
		tableBorder.setSpacingAfter(10);
		p.add(tableBorder);
		document.add(p);
	}

	private void addHeader(Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(new float[] { 30, 30, 20, 20 });
		table.setWidthPercentage(100);
		PdfPCell cell = getPDFPCell();
		cell.setPaddingBottom(8);

		cell.setPhrase(new Phrase("Marca y Modelo", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(this.dto.getCodigoArticulo(), fontText));
		cell.setColspan(3);
		table.addCell(cell);

		cell.setPaddingBottom(4);
		cell.setPaddingTop(8);
		cell.setBorderWidthBottom(0);
		cell.setColspan(1);

		cell.setPhrase(new Phrase("Serie", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(this.dto.getNroSerie(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Numero Interno", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(this.dto.getNroFabricante(), fontText));
		table.addCell(cell);

		cell.setBorder(0);

		addSeccion(document, table, null);

	}

	private void addFooter(Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(new float[] { 15, 15, 10, 17, 17, 25 });
		table.setWidthPercentage(100);
		PdfPCell cell = getPDFPCell();

		cell.setPaddingBottom(6);
		cell.setPaddingTop(6);
		cell.setBorderWidthBottom(0);

		cell.setPhrase(new Phrase("Num. Orden", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(this.dto.getIdActividad().toString(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Fecha", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(this.dto.getFecha(), fontText));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Verificada por", fontHeaderTable));
		table.addCell(cell);
		cell.setPhrase(new Phrase(this.dto.getEmpleado(), fontText));
		table.addCell(cell);

		cell.setBorder(0);

		addSeccion(document, table, null);

	}

	private void addSeccionChecks(Document document, PdfPTable table, String titulo, boolean blue, boolean centered)
			throws DocumentException {
		Paragraph p = new Paragraph();
		PdfPTable tableBorder = new PdfPTable(1);
		tableBorder.setWidthPercentage(100);
		PdfPCell cellBorder = new PdfPCell();
		if (!StringUtils.isNullOrEmpty(titulo))
			if (blue)
				cellBorder.addElement(addHeaderBlue(titulo, 0));
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
		cellBorder.setPaddingBottom(15);
		tableBorder.addCell(cellBorder);
		tableBorder.setSpacingAfter(10);
		p.add(tableBorder);
		document.add(p);
	}

	private void addGrillaChecks(Document document) throws DocumentException {

		List<String> keySet = new ArrayList<String>(dto.getChecks().keySet());
		Collections.sort(keySet);

		Image check = getCheckImage();

		for (String titulo : keySet) {

			PdfPTable tablechecklist = new PdfPTable(new float[] { 6, 35, 7, 7, 7, 38 });
			tablechecklist.setWidthPercentage(100);

			PdfPCell cellHeaderColspan2 = getHeaderCheck("", true);
			cellHeaderColspan2.setColspan(2);

			PdfPCell cellHeaderSi = getHeaderCheck("SI", true);
			PdfPCell cellHeaderNo = getHeaderCheck("NO", true);
			PdfPCell cellHeaderNP = getHeaderCheck("N/P", true);
			PdfPCell cellHeaderObservaciones = getHeaderCheck("Observaciones", false);

			tablechecklist.addCell(cellHeaderColspan2);
			tablechecklist.addCell(cellHeaderSi);
			tablechecklist.addCell(cellHeaderNo);
			tablechecklist.addCell(cellHeaderNP);
			tablechecklist.addCell(cellHeaderObservaciones);

			List<ItemCheckDTO> items = dto.getChecks().get(titulo);

			PdfPCell columnaRowspan = getPDFPCellBordeCompleto();
			columnaRowspan.setUseAscender(true);
			columnaRowspan.setPhrase(new Phrase(titulo, FONT_CABECERA_CHECK));
			columnaRowspan.setRotation(90);
			columnaRowspan.setHorizontalAlignment(Element.ALIGN_CENTER);
			columnaRowspan.setVerticalAlignment(Element.ALIGN_MIDDLE);
			columnaRowspan.setRowspan(items.size());

			tablechecklist.addCell(columnaRowspan);

			Iterator<ItemCheckDTO> iterator = items.iterator();

			ItemCheckDTO next = iterator.next();

			PdfPCell cellContentNombre = getPDFPCell();
			cellContentNombre.setPhrase(new Phrase(next.getNombre(), fontHeaderTable));
			tablechecklist.addCell(cellContentNombre);

			tablechecklist.addCell(getCeldaCheck(check, next.isOk()));
			tablechecklist.addCell(getCeldaCheck(check, next.isNoOK()));
			tablechecklist.addCell(getCeldaCheck(check, next.isNa()));

			PdfPCell cellContentObservaciones = getPDFPCellBordeCompleto();
			cellContentObservaciones.setPhrase(new Phrase(next.getObservaciones(), fontHeaderTable));
			tablechecklist.addCell(cellContentObservaciones);

			while (iterator.hasNext()) {

				next = iterator.next();

				cellContentNombre.setPhrase(new Phrase(next.getNombre(), fontHeaderTable));
				cellContentNombre.setNoWrap(false);
				
				tablechecklist.addCell(cellContentNombre);

				tablechecklist.addCell(getCeldaCheck(check, next.isOk()));
				tablechecklist.addCell(getCeldaCheck(check, next.isNoOK()));
				tablechecklist.addCell(getCeldaCheck(check, next.isNa()));

				cellContentObservaciones.setPhrase(new Phrase(next.getObservaciones(), fontHeaderTable));
				cellContentObservaciones.setNoWrap(false);
				tablechecklist.addCell(cellContentObservaciones);

			}
			addSeccionChecks(document, tablechecklist, null, true, true);
		}

	}

	private PdfPCell getHeaderCheck(String texto, boolean quitarBordeDerecho) {
		PdfPCell cellHeaderSi = getPDFPCellBordeCompletoRellenoGris();
		cellHeaderSi.setPhrase(new Phrase(texto, FONT_CABECERA_CHECK));
		cellHeaderSi.setHorizontalAlignment(Element.ALIGN_CENTER);
		if (quitarBordeDerecho)
			cellHeaderSi.setBorderWidthRight(0);
		return cellHeaderSi;
	}

	private PdfPCell getCeldaCheck(Image check, boolean val) {
		PdfPCell cellContent = getPDFPCell();
		if (val) {
			cellContent.addElement(check);
		} else {
			cellContent.setPhrase(new Phrase("", fontHeaderTable));
		}
		cellContent.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellContent.setBorderWidthLeft(1);
		cellContent.setBorderColorLeft(COLOR_GRIS_BORDES);
		return cellContent;
	}

	private Image getCheckImage() {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resourcecheck = classLoader.getResource("check.png");
		Image imagecheck = null;
		try {
			File filecheck = new File(resourcecheck.toURI());
			imagecheck = Image.getInstance(filecheck.getAbsolutePath());
			imagecheck.scaleToFit(15, 15);
			imagecheck.setAlignment(Element.ALIGN_CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagecheck;
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
		cellHeader.setBackgroundColor(COLOR_GRIS_FONDO);
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
			cb2.roundRectangle(rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
					rect.getHeight() - 3, 4);
			cb2.setColorStroke(COLOR_GRIS_BORDES);
			cb2.stroke();
		}
	}

}
