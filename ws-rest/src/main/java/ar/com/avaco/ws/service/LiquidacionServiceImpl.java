package ar.com.avaco.ws.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.ws.dto.employee.liquidacion.FueraConvenio;
import ar.com.avaco.ws.dto.employee.liquidacion.Jornal;
import ar.com.avaco.ws.dto.employee.liquidacion.Mensual;

@Service("liquidacionService")
public class LiquidacionServiceImpl implements LiquidacionService {

	@Value("${contador.mail}")
	private String contadorMail;

	private CellStyle estiloCabecera;

	private CellStyle estiloDatos;

	@Autowired
	private MailSenderSMTPService mailSender;

	public void generarEstiloCabecera(Workbook workbook) {
		estiloCabecera = workbook.createCellStyle();
		Font fuenteCabecera = workbook.createFont();
		fuenteCabecera.setBold(true);
		fuenteCabecera.setItalic(true);
		estiloCabecera.setFont(fuenteCabecera);
		estiloCabecera.setBorderTop(BorderStyle.THIN);
		estiloCabecera.setBorderBottom(BorderStyle.THIN);
		estiloCabecera.setBorderLeft(BorderStyle.THIN);
		estiloCabecera.setBorderRight(BorderStyle.THIN);
		estiloCabecera.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		estiloCabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}

	public void generarEstiloDatos(Workbook workbook) {
		// Estilo para celdas de datos (solo bordes)
		estiloDatos = workbook.createCellStyle();
		estiloDatos.setBorderTop(BorderStyle.THIN);
		estiloDatos.setBorderBottom(BorderStyle.THIN);
		estiloDatos.setBorderLeft(BorderStyle.THIN);
		estiloDatos.setBorderRight(BorderStyle.THIN);
		estiloDatos.setWrapText(true);
	}

	@Override
	public void generarExcel(String periodo, List<FueraConvenio> fueraConvenio, List<Mensual> mensuales,
			List<Jornal> jornales) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		generarEstiloCabecera(workbook);
		generarEstiloDatos(workbook);

		generarHojaFueraConvenio(periodo, fueraConvenio, workbook);
		generarHojaJornalesl(periodo, jornales, workbook);
		generarHojaMensual(periodo, mensuales, workbook);

		File file = File.createTempFile("Novedades " + periodo, ".xlsx");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			workbook.write(fos);
		}

		List<File> archivos = new ArrayList<>();
		archivos.add(file);

		mailSender.sendMail("reportesservicios@premecsa.com.ar", contadorMail, null,
				"Premec - Novedades Sueldos" + periodo, "", archivos);

		workbook.close();

	}

	private void generarHojaFueraConvenio(String periodo, List<FueraConvenio> fueraConvenio, Workbook workbook) {
		// --- Solapa FueraConvenio ---
		Sheet sheet = workbook.createSheet("Fuera de Convenio");

		// B2 = "Periodo"
		agregarPeriodo(periodo, sheet);

		// Header
		String[] columnas = { "Legajo", "Apellido", "Nombre", "CUIL", "Categoría", "Feriado", "Tarde", "Adelanto de sueldo",
				"Prestamos", "Gratificaciones/Aumentos", "Novedades", "Ausencias" };
		agregarHeader(sheet, columnas);

		// Datos
		int cellindexRow = 0;
		int fila = 4; // fila 5 = index 4
		for (FueraConvenio a : fueraConvenio) {
			Row row = sheet.createRow(fila++);
			agregarCelda(row, ++cellindexRow, a.getLegajo());
			agregarCelda(row, ++cellindexRow, a.getApellido());
			agregarCelda(row, ++cellindexRow, a.getNombre());
			agregarCelda(row, ++cellindexRow, a.getCuil());
			agregarCelda(row, ++cellindexRow, a.getCategoria());
			agregarCelda(row, ++cellindexRow, a.getFeriado());
			agregarCelda(row, ++cellindexRow, a.getTarde());
			agregarCelda(row, ++cellindexRow, a.getAdelantoSueldo());
			agregarCelda(row, ++cellindexRow, a.getPrestamos());
			agregarCelda(row, ++cellindexRow, a.getGratificaciones());
			agregarCelda(row, ++cellindexRow, a.getNovedades());
			agregarCelda(row, ++cellindexRow, a.getAusencias().replace(",", "\n"));
			cellindexRow = 0;
		}

		int lastColumn = sheet.getRow(3).getLastCellNum(); // fila de cabecera
		for (int i = 0; i < lastColumn; i++) {
			sheet.autoSizeColumn(i);
		}

	}

	private void generarHojaMensual(String periodo, List<Mensual> mensuales, Workbook workbook) {
		// --- Solapa FueraConvenio ---
		Sheet sheet = workbook.createSheet("Mensual");

		// B2 = "Periodo"
		agregarPeriodo(periodo, sheet);

		// Header
		String[] columnas = { "Legajo", "Apellido", "Nombre", "CUIL", "Categoría", "Valor Hora", "Feriado", "Tarde",
				"Hs Normales", "hsn100% Feriado", "Adelanto de sueldo", "Préstamos", "Hs 50%", "Hs100%",
				"Gratificaciones/Aumentos", "Novedades", "Premio", "Ausencias" };
		agregarHeader(sheet, columnas);

		// Datos
		int cellindexRow = 0;
		int fila = 4; // fila 5 = index 4
		for (Mensual a : mensuales) {
			Row row = sheet.createRow(fila++);
			agregarCelda(row, ++cellindexRow, a.getLegajo());
			agregarCelda(row, ++cellindexRow, a.getApellido());
			agregarCelda(row, ++cellindexRow, a.getNombre());
			agregarCelda(row, ++cellindexRow, a.getCuil());
			agregarCelda(row, ++cellindexRow, a.getCategoria());
			agregarCelda(row, ++cellindexRow, a.getValorHora());
			agregarCelda(row, ++cellindexRow, a.getFeriado());
			agregarCelda(row, ++cellindexRow, a.getTarde());
			agregarCelda(row, ++cellindexRow, a.getHsNormales());
			agregarCelda(row, ++cellindexRow, a.getFeriadoExtra());
			agregarCelda(row, ++cellindexRow, a.getAdelantoSueldo());
			agregarCelda(row, ++cellindexRow, a.getPrestamos());
			agregarCelda(row, ++cellindexRow, a.getHs50());
			agregarCelda(row, ++cellindexRow, a.getHs100());
			agregarCelda(row, ++cellindexRow, a.getGratificaciones());
			agregarCelda(row, ++cellindexRow, a.getNovedades());
			agregarCelda(row, ++cellindexRow, a.getPremio());
			agregarCelda(row, ++cellindexRow, a.getAusencias().replace(",", "\n"));
			cellindexRow = 0;
		}

		int lastColumn = sheet.getRow(3).getLastCellNum(); // fila de cabecera
		for (int i = 0; i < lastColumn; i++) {
			sheet.autoSizeColumn(i);
		}

	}

	private void generarHojaJornalesl(String periodo, List<Jornal> jornales, Workbook workbook) {
		// --- Solapa FueraConvenio ---
		Sheet sheet = workbook.createSheet("Jornal");

		agregarPeriodo(periodo, sheet);

		// Header
		String[] columnas = { "Legajo", "Apellido", "Nombre", "CUIL", "Categoría", "Valor Hora", "Feriado", "Tarde", 
				"Hs Normales", "hsn100% Feriado", "Adelanto de sueldo", "Comida", "Hs. Prod.", "Préstamos", "Hs 50%", "Hs100%",
				"Gratificaciones/Aumentos", "Novedades", "Premio", "Ausencias" };
		agregarHeader(sheet, columnas);

		// Datos
		int cellindexRow = 0;
		int fila = 4; // fila 5 = index 4
		for (Jornal a : jornales) {
			Row row = sheet.createRow(fila++);
			agregarCelda(row, ++cellindexRow, a.getLegajo());
			agregarCelda(row, ++cellindexRow, a.getApellido());
			agregarCelda(row, ++cellindexRow, a.getNombre());
			agregarCelda(row, ++cellindexRow, a.getCuil());
			agregarCelda(row, ++cellindexRow, a.getCategoria());
			agregarCelda(row, ++cellindexRow, a.getValorHora());
			agregarCelda(row, ++cellindexRow, a.getFeriado());
			agregarCelda(row, ++cellindexRow, a.getTarde());
			agregarCelda(row, ++cellindexRow, a.getHsNormales());
			agregarCelda(row, ++cellindexRow, a.getFeriadoExtra());
			agregarCelda(row, ++cellindexRow, a.getAdelantoSueldo());

			agregarCelda(row, ++cellindexRow, a.getComida());
			agregarCelda(row, ++cellindexRow, a.getHsProductivas());
			
			
			agregarCelda(row, ++cellindexRow, a.getPrestamos());
			agregarCelda(row, ++cellindexRow, a.getHs50());
			agregarCelda(row, ++cellindexRow, a.getHs100());
			agregarCelda(row, ++cellindexRow, a.getGratificaciones());
			agregarCelda(row, ++cellindexRow, a.getNovedades());
			agregarCelda(row, ++cellindexRow, a.getPremio());
			agregarCelda(row, ++cellindexRow, a.getAusencias().replace(",", "\n"));
			cellindexRow = 0;
		}

		int lastColumn = sheet.getRow(3).getLastCellNum(); // fila de cabecera
		for (int i = 0; i < lastColumn; i++) {
			sheet.autoSizeColumn(i);
		}

	}

	private void agregarCelda(Row row, int column, Object value) {
		if (value == null)
			value = "";
		Cell createCell = row.createCell(column);
		createCell.setCellValue(value.toString());
		createCell.setCellStyle(estiloDatos);
	}

	private void agregarPeriodo(String periodo, Sheet sheet) {
		Row row2 = sheet.createRow(1); // fila 2 = index 1
		Cell cellB2 = row2.createCell(1); // columna B = index 1
		cellB2.setCellValue("Periodo");
		cellB2.setCellStyle(estiloCabecera);
		Cell cellC2 = row2.createCell(2); // columna C = index 2
		cellC2.setCellValue(periodo);
		cellC2.setCellStyle(estiloDatos);
	}

	private void agregarHeader(Sheet sheet, String[] columnas) {
		Row header = sheet.createRow(3); // fila 4 = index 3
		for (int i = 0; i < columnas.length; i++) {
			Cell celda = header.createCell(i + 1); // columna B = 1
			celda.setCellValue(columnas[i]);
			celda.setCellStyle(estiloCabecera); // aplica estilo a cada celda
		}
	}

}
