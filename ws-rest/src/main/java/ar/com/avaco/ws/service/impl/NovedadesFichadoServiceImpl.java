package ar.com.avaco.ws.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.repository.UsuarioRepository;
import ar.com.avaco.ws.rest.security.dto.RegistroFichadoDTO;
import ar.com.avaco.ws.service.AbstractSapService;
import ar.com.avaco.ws.service.NovedadesFichadoService;

@Service("novedadesFichadoService")
public class NovedadesFichadoServiceImpl extends AbstractSapService implements NovedadesFichadoService {

	private UsuarioRepository usuarioRepository;

	public void setearHorasProductivas(List<String> legajos) {
		
		// Obtengo los usuarios en base a los legajos
		
		List<Usuario> usuarios = usuarioRepository.findByLegajoIn(legajos);
		
		List<String> idsSap = usuarios.stream().map(Usuario::getUsuariosap).collect(Collectors.toList());
		
		String idsSapString = String.join(",", idsSap);
		
		// Traigo el driver para la query
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		// Datos de conexion
		String url = "jdbc:sqlserver://;serverName=vm-sap;port=1433;databaseName=" + super.dbSAP;
		String user = "sa";
		String password = "SAPB1Admin";

		// Query que trae las horas productivas agrupadas por empleado/fecha
		String query = " SELECT T1.AttendEmpl as 'CodEmp', " + 
				"T1.Recontact as 'Fecha', sum(T1.duration/3600) as 'HorasProductivas' " + 
				"FROM OCLG T1 where T1.AttendEmpl is not null and T1.AttendEmpl in ("+ idsSapString +") " + 
				"and T1.Recontact >= '2020-08-20' and T1.Recontact <= '2025-08-25'" + 
				"group by T1.AttendEmpl, T1.Recontact order by T1.AttendEmpl, T1.Recontact";

			
//			Connection conn = DriverManager.getConnection(url, user, password);
//			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//
//			// Por cada registro
//			while (rs.next()) {
//
//				
//				
//			}
//
//			rs.close();

		
	}

	@Override
	public Map<Usuario, List<RegistroFichadoDTO>> parsearExcelNovedadesFichado() throws IOException {

		// Leo el archivo (cambiarlo por parametro)

		FileInputStream file = new FileInputStream(new File("D:\\desarrollo\\premec\\fichado.xlsx"));
		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(0);

		Map<Usuario, List<RegistroFichadoDTO>> empleados = new HashMap<Usuario, List<RegistroFichadoDTO>>();

		for (int i = 0; i <= sheet.getLastRowNum();) {
			Row headerRow = sheet.getRow(i);
			if (headerRow == null || !getCellValue(headerRow.getCell(0)).equalsIgnoreCase("legajo")) {
				i++;
				continue;
			}

			// Extraer legajo y nombre
			String legajoNombre = getCellValue(headerRow.getCell(1));
			String[] partes = legajoNombre.split(" ");
			String legajo = partes[0].trim();

			Usuario usuario = usuarioRepository.findByLegajo(Integer.parseInt(legajo));

			// Fila siguiente: cabecera de registros
			i += 2;
			List<RegistroFichadoDTO> registros = new ArrayList<>();

			while (i <= sheet.getLastRowNum()) {
				Row fila = sheet.getRow(i);
				if (fila == null || isRowEmpty(fila)) {
					i++; // saltamos fila vacía
					break;
				}

				RegistroFichadoDTO registro = new RegistroFichadoDTO();
				registro.setDia(getCellValue(fila.getCell(0)));
				registro.setFecha(getCellValue(fila.getCell(1)));
				registro.setEntrada1(getCellValue(fila.getCell(2)));
				registro.setSalida1(getCellValue(fila.getCell(3)));
				registro.setEntrada2(getCellValue(fila.getCell(4)));
				registro.setSalida2(getCellValue(fila.getCell(5)));
				registro.setTotalDia(getCellValue(fila.getCell(6)));
				registro.setDescanso(getCellValue(fila.getCell(7)));
				registro.setNormal(getCellValue(fila.getCell(8)));
				registro.setExtra50(getCellValue(fila.getCell(9)));
				registro.setExtra100(getCellValue(fila.getCell(10)));
				registro.setHoraNoTipificada(getCellValue(fila.getCell(11)));
				registro.setTarde(getCellValue(fila.getCell(12)));
				registro.setComentarios(getCellValue(fila.getCell(13)));

				registros.add(registro);
				i++;
			}

			empleados.put(usuario, registros);
			// Salto fila de totales si está presente
			i++;
		}

		workbook.close();
		file.close();

		return empleados;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
			}
			return String.valueOf((int) cell.getNumericCellValue());
		default:
			return "";
		}
	}

	private static boolean isRowEmpty(Row row) {
		for (Cell cell : row) {
			if (cell != null && cell.getCellType() != CellType.BLANK)
				return false;
		}
		return true;
	}

	@Resource(name = "usuarioRepository")
	public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

}
