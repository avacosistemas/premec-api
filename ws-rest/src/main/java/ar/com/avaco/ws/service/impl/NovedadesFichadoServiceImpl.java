package ar.com.avaco.ws.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.repository.UsuarioRepository;
import ar.com.avaco.utils.DateUtils;
import ar.com.avaco.ws.dto.employee.EmployeesInfoReponseSapDTO;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetGetDTO;
import ar.com.avaco.ws.dto.timesheet.ProjectManagementTimeSheetLineGetDTO;
import ar.com.avaco.ws.rest.security.dto.EmpleadoFichados;
import ar.com.avaco.ws.rest.security.dto.EmpleadoSap;
import ar.com.avaco.ws.rest.security.dto.RegistroFichadoDTO;
import ar.com.avaco.ws.service.AbstractSapService;
import ar.com.avaco.ws.service.NovedadesFichadoService;

@Service("novedadesFichadoService")
public class NovedadesFichadoServiceImpl extends AbstractSapService implements NovedadesFichadoService {

	private UsuarioRepository usuarioRepository;

	private TimeSheetService timeSheetService;

	private EmployeeService employeeService;

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
		String query = " SELECT T1.AttendEmpl as 'CodEmp', "
				+ "T1.Recontact as 'Fecha', sum(T1.duration/3600) as 'HorasProductivas' "
				+ "FROM OCLG T1 where T1.AttendEmpl is not null and T1.AttendEmpl in (" + idsSapString + ") "
				+ "and T1.Recontact >= '2020-08-20' and T1.Recontact <= '2025-08-25'"
				+ "group by T1.AttendEmpl, T1.Recontact order by T1.AttendEmpl, T1.Recontact";

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
	public List<EmpleadoFichados> parsearExcelNovedadesFichado(byte[] archivoBytes) throws IOException {

		List<EmpleadoFichados> empleadoFichadoList = new ArrayList<>();

		try (InputStream inputStream = new ByteArrayInputStream(archivoBytes)) {
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			Map<EmpleadoSap, List<RegistroFichadoDTO>> empleados = new HashMap<EmpleadoSap, List<RegistroFichadoDTO>>();

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

				EmpleadoSap empleado = new EmpleadoSap();
				if (usuario != null) {
					empleado.setApellido(usuario.getApellido());
					empleado.setNombre(usuario.getNombre());
					empleado.setUsername(usuario.getUsername());
					empleado.setUsuarioSap(usuario.getUsuariosap());
				} else {
					empleado.setNombre("No existe usuario en el admin con este legajo");
				}
				empleado.setLegajo(Long.parseLong(legajo));

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
					registro.setDia(DateUtils.toString(DateUtils.toDate(fila.getCell(0).toString().trim(), "dd/MM/yy"),
							"dd/MM/yyyy"));
					registro.setFecha(getCellValue(fila.getCell(1)).trim());
					registro.setEntrada1(getCellValue(fila.getCell(2)).trim());
					registro.setSalida1(getCellValue(fila.getCell(3)).trim());
					registro.setEntrada2(getCellValue(fila.getCell(4)).trim());
					registro.setSalida2(getCellValue(fila.getCell(5)).trim());
					registro.setTotalDia(getCellValue(fila.getCell(6)).trim());
					registro.setDescanso(getCellValue(fila.getCell(7)).trim());
					registro.setNormal(getCellValue(fila.getCell(8)).trim());
					registro.setExtra50(getCellValue(fila.getCell(9)).trim());
					registro.setExtra100(getCellValue(fila.getCell(10)).trim());
					registro.setHoraNoTipificada(getCellValue(fila.getCell(11)).trim());
					registro.setTarde(getCellValue(fila.getCell(12)).trim());
					registro.setComentarios(getCellValue(fila.getCell(13)).trim());

					registros.add(registro);
					i++;
				}

				empleados.put(empleado, registros);
				// Salto fila de totales si está presente
				i++;
			}

			workbook.close();
			// file.close();

			empleados.forEach((x, y) -> empleadoFichadoList.add(new EmpleadoFichados(x, y)));
		}
		return empleadoFichadoList;
	}

	public void procesar() {

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

	@Override
	public void enviarFichados(List<EmpleadoFichados> registros) {

		Map<String, ProjectManagementTimeSheetGetDTO> mapaEmpleadoPeriodo = new HashMap<>();

		// Por cada empleado
		for (EmpleadoFichados registro : registros) {

			// Obtengo usuario Sap
			String usuarioSap = registro.getEmpleado().getUsuarioSap();

			// Busco el empleado en SAP
			EmployeesInfoReponseSapDTO employee = employeeService.getById(Long.parseLong(usuarioSap));

			boolean isServicioTecnico = "SI".equals(employee.getServicioTecnico());

			// Por cada registro de horas por dia
			for (RegistroFichadoDTO fichado : registro.getFichados()) {

				String salida1 = fichado.getSalida1();
				boolean hayHorarioFin = salida1 != null && StringUtils.isNotEmpty(salida1);

				// Si tiene horario de fin, entonces proceso el registro
				if (hayHorarioFin) {

					// Calculo el periodo por cada registro porque puede haber fechas de varios
					// periodos
					String[] split = fichado.getDia().split("/");
					String month = split[1].trim();
					String year = split[2].trim();

					String fechaDesde = year + month + "01";

					Calendar instance = Calendar.getInstance();
					instance.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
					instance.add(Calendar.MONTH, 1);
					instance.add(Calendar.DAY_OF_MONTH, -1);

					String fechaHasta = DateUtils.toString(instance.getTime(), "yyyyMMdd");

					// Armo el entry del mapa
					String entryMapCabecera = generarEntryMapa(usuarioSap, year + month);

					// Obtengo del mapa el registro
					ProjectManagementTimeSheetGetDTO timesheet = mapaEmpleadoPeriodo.get(entryMapCabecera);

					// Si no lo tengo en el mapa
					if (timesheet == null) {
						// Quiere decir que es el primer registro

						// Primero lo pido a sap
						timesheet = this.timeSheetService.getTimeSheet(usuarioSap, fechaDesde, fechaHasta);
					}

					// Si todavia no hay cabecera de periodo tengo que crearla
					if (timesheet == null) {
						// Genero el timesheet en sap y obtengo el entry
						timesheet = new ProjectManagementTimeSheetGetDTO();
						Long timesheetabsentry = this.timeSheetService.generarTimeSheet(usuarioSap, fechaDesde,
								fechaHasta);
						timesheet.setAbsEntry(timesheetabsentry);
						timesheet.setDateFrom(fechaDesde);
						timesheet.setDateTo(fechaHasta);
						timesheet.setUserId(usuarioSap);
					}

					Date fechaFichado = DateUtils.toDate(fichado.getDia(), "dd/MM/yyyy");

					// Busco si existe al menos 1 registro para ese día.
					List<ProjectManagementTimeSheetLineGetDTO> collect = timesheet.getLineas().stream()
							.filter(x -> DateUtils.toString(x.getDate(), "dd/MM/yyyy")
									.equals(DateUtils.toString(fechaFichado, "dd/MM/yyyy")))
							.collect(Collectors.toList());

					// Si no hay registros lo agrego
					if (collect.isEmpty()) {

						// Obtengo el cantidad de lineas existentes + 1
						Long lineNum = timesheet.getLineas().size() + 1L;

						// Armo el nuevo registro de la collection de dias
						ProjectManagementTimeSheetLineGetDTO timesheetline = new ProjectManagementTimeSheetLineGetDTO();

						timesheetline.setDate(fechaFichado);

						timesheetline.setStartTime(fichado.getEntrada1());
						timesheetline.setEndTime(salida1);

						String diferencia = calcularDiferenciaHorarioEntrada(fichado.getEntrada1(),
								employee.getHoraInicio());
						timesheetline.setNonBillableTime(diferencia);

						timesheetline.setHorasNormales(fichado.getNormal());
						timesheetline.setHorasExtras50(fichado.getExtra50());
						timesheetline.setHorasExtras100(fichado.getExtra100());
						timesheetline.setHorasExtrasFeriados(fichado.getHoraNoTipificada());
						timesheetline.setTipoAusentismo(fichado.getComentarios());

						timesheetline.setComida(isServicioTecnico ? "Si" : "No");

						timesheetline.setLineId(lineNum);

						timesheet.getLineas().add(timesheetline);

						// Despues de agregar la linea a la collecion pongo el objeto en el mapa
						mapaEmpleadoPeriodo.put(entryMapCabecera, timesheet);
					}

				}
			}

		}

		// Una vez que arme el mapa, envio a sap las lineas de cada uno usando un
		// request por empleado
		mapaEmpleadoPeriodo.values().forEach(lineGroup -> this.timeSheetService.updateTimeSheetLines(lineGroup));

	}

	private String calcularDiferenciaHorarioEntrada(String fichado, String horarioIngreso) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime horaFichado = LocalTime.parse(fichado.trim(), formatter);
		LocalTime horaIngreso = LocalTime.parse(horarioIngreso.trim() + ":00");

		String diferencia = null;

		Duration d = Duration.between(horaFichado, horaIngreso);

		if (!d.isZero() && !d.isNegative()) {
			long hours = d.toHours();
			long minutes = d.toMinutes() % 60;
			long seconds = d.getSeconds() % 60;
			diferencia = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}
		return diferencia;

	}

	private String generarEntryMapa(String usuarioSap, String dia) {
		return usuarioSap + "-" + dia;
	}

	@Resource(name = "usuarioRepository")
	public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Resource(name = "timeSheetService")
	public void setTimeSheetService(TimeSheetService timeSheetService) {
		this.timeSheetService = timeSheetService;
	}

	@Resource(name = "employeeService")
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
