package ar.com.avaco.ws.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.arc.sec.repository.UsuarioRepository;
import ar.com.avaco.commons.exception.ErrorValidationException;
import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.utils.DateUtils;
import ar.com.avaco.ws.dto.actividad.HorasPorEmpleadoDTO;
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

	private ActivityService activityService;

	private final Logger logger = Logger.getLogger(this.getClass());

	@Value(value = "${exclusiones.actividades.calculo.horas.netas}")
	private String exclusionesActividadesCalculoHorasNetas;

	@Value(value = "${licencia.feriado}")
	private String licenciasFeriado;

	@Value(value = "${licencia.nojustificada}")
	private String licenciasNoJustificada;

	@Override
	public List<EmpleadoFichados> parsearExcelNovedadesFichado(byte[] archivoBytes) throws IOException {

		// Inicializo el listado
		List<EmpleadoFichados> empleadoFichadoList = new ArrayList<>();

		// Importo el archivo y lo convierto en libro de excel
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
					empleado.setNombre("No existe usuario con este legajo - ");
					empleado.setApellido(partes[1].trim());
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

					String comentarios = getCellValue(fila.getCell(12)).trim();
					if (comentarios == null || !comentarios.startsWith("FRAN")) {
						RegistroFichadoDTO registro = new RegistroFichadoDTO();
						registro.setDia(DateUtils.toString(
								DateUtils.toDate(fila.getCell(0).toString().trim(), "dd/MM/yy"), "dd/MM/yyyy"));
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
						registro.setTarde(getCellValue(fila.getCell(11)).trim());
						registro.setComentarios(comentarios);
						registros.add(registro);
					}
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

		Comparator<EmpleadoFichados> comparator = new Comparator<EmpleadoFichados>() {
			public int compare(EmpleadoFichados o1, EmpleadoFichados o2) {
				return o1.getEmpleado().getLegajo().compareTo(o2.getEmpleado().getLegajo());
			};
		};

		empleadoFichadoList.sort(comparator);

		return empleadoFichadoList;
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

		// Busco la fecha desde y hasta en todos los registros
		// Tambien armo un listado de todos los empleados

		Date desdeRango = null;
		Date hastaRango = null;
		List<Long> employeeIds = new ArrayList<>();

		// Recorro cada empleado y cada registro para saber la fecha desde minima y
		// hasta maxima de rango.
		for (EmpleadoFichados registro : registros) {

			// Obtengo el usuario SAP
			String usuarioSap = registro.getEmpleado().getUsuarioSap();

			// Si tiene usuario SAP
			if (StringUtils.isNotBlank(usuarioSap)) {

				// Agrego el empleado
				employeeIds.add(new Long(usuarioSap));

				// Voy tomando la fecha desde/hasta de todos los fichados en general de todos
				// los empleados
				for (RegistroFichadoDTO fichado : registro.getFichados()) {
					Date fechaFichado = DateUtils.toDate(fichado.getDia(), "dd/MM/yyyy");
					if (desdeRango == null || fechaFichado.before(desdeRango)) {
						desdeRango = fechaFichado;
					}

					if (hastaRango == null || hastaRango.before(fechaFichado)) {
						hastaRango = fechaFichado;
					}
				}
			}
		}

		// Pido la sumatoria de horas por dia por usuario de acuerdo a las actividades
		String desdeFormatedo = DateUtils.toString(desdeRango, "yyyyMMdd");
		String hastaFormateado = DateUtils.toString(hastaRango, "yyyyMMdd");
		
		List<HorasPorEmpleadoDTO> horasPorFechaEmpleado = this.activityService
				.obtenerHorasAgrupadasPorFechaEmpleado(employeeIds, desdeFormatedo, hastaFormateado,
						exclusionesActividadesCalculoHorasNetas);

		// Los meto en un mapa
		Map<String, HorasPorEmpleadoDTO> mapaSegundosEmpleadoDiaActividad = new HashMap<>();
		horasPorFechaEmpleado.stream().forEach(registro -> {
			// Key usuario + "-" + fecha (60-2025-05-22)
			String key = registro.getAttendEmpl() + "-" + DateUtils.toString(registro.getSeStartDat(), "dd/MM/yyyy");
			mapaSegundosEmpleadoDiaActividad.put(key, registro);
		});

		Map<String, ProjectManagementTimeSheetGetDTO> mapaEmpleadoPeriodo = new HashMap<>();

		Map<String, String> errores = new HashMap<>();

		// Por cada empleado
		for (EmpleadoFichados registroEmpleadoDTO : registros) {

			// Obtengo el usuario sap
			String usuarioSapString = registroEmpleadoDTO.getEmpleado().getUsuarioSap();
			
			// Si tiene usuario sap
			if (StringUtils.isNotBlank(usuarioSapString)) {

				// Obtengo usuario Sap en Long
				Long usuarioSap = Long.parseLong(usuarioSapString);

				// Busco el empleado en SAP
				// FIXME para optimizar armar un query que traiga todo y armar un mapa
				EmployeesInfoReponseSapDTO employee = employeeService.getById(usuarioSap);

				boolean isServicioTecnico = "SI".equals(employee.getServicioTecnico());
				
				// FIXME Comidas
				// Servicio Tecnico SI -> reviso comidas
				// Servicio Tecnico NO -> ver si en el día tiene al menos una actividad
				// que no sea de taller y que finalice despues de las 12

				// Por cada registro de horas por dia
				for (RegistroFichadoDTO fichadoDTO : registroEmpleadoDTO.getFichados()) {
					
					String key = usuarioSap + "-" + fichadoDTO.getDia();
					HorasPorEmpleadoDTO horasPorEmpleadoDTO = mapaSegundosEmpleadoDiaActividad.get(key);
					
					boolean isTieneAcNoTallerDespuesMediodia = horasPorEmpleadoDTO != null ? "Y".equals(horasPorEmpleadoDTO.getTieneActNoTallerDespuesMediodia()) : false;
					
					boolean aplicaComida = isTieneAcNoTallerDespuesMediodia || isServicioTecnico;
							
					boolean tieneEntrada1 = StringUtils.isNotBlank(fichadoDTO.getEntrada1());
					boolean tieneSalida1 = StringUtils.isNotBlank(fichadoDTO.getSalida1());
					boolean tienenES1 = tieneEntrada1 && tieneSalida1;

					boolean tieneEntrada2 = StringUtils.isNotBlank(fichadoDTO.getEntrada2());
					boolean tieneSalida2 = StringUtils.isNotBlank(fichadoDTO.getSalida2());
					boolean tienenES2 = tieneEntrada2 && tieneSalida2;

					boolean tienenES2CompletoIncompleto = !(tieneEntrada2 ^ tieneSalida2);

					// Si tiene horario de fin, entonces proceso el registro
					if (tienenES1 && tienenES2CompletoIncompleto
							&& !DateUtils.calcularDiferenciaHorario(fichadoDTO.getEntrada1(), fichadoDTO.getSalida1())
									.equals("00:00:00")) {

						// Calculo el periodo por cada registro porque puede haber fechas de varios
						// periodos
						String[] split = fichadoDTO.getDia().split("/");
						String month = split[1].trim();
						String year = split[2].trim();

						String fechaDesdePeriodo = year + month + "01";

						Calendar instance = Calendar.getInstance();
						instance.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
						instance.add(Calendar.MONTH, 1);
						instance.add(Calendar.DAY_OF_MONTH, -1);

						String fechaHastaPeriodo = DateUtils.toString(instance.getTime(), "yyyyMMdd");

						// Armo el entry del mapa
						String entryMapCabecera = generarEntryMapa(usuarioSap.toString(), year + month);

						// Obtengo del mapa el registro
						ProjectManagementTimeSheetGetDTO timesheet = mapaEmpleadoPeriodo.get(entryMapCabecera);

						// Si no lo tengo en el mapa
						if (timesheet == null) {
							// Quiere decir que es el primer registro

							// Primero lo pido a sap
							timesheet = this.timeSheetService.getTimeSheet(usuarioSap, fechaDesdePeriodo,
									fechaHastaPeriodo);
						}

						// Si todavia no hay cabecera de periodo tengo que crearla
						if (timesheet == null) {
							// Genero el timesheet en sap y obtengo el entry
							timesheet = new ProjectManagementTimeSheetGetDTO();
							Long timesheetabsentry = this.timeSheetService.generarTimeSheet(usuarioSap,
									fechaDesdePeriodo, fechaHastaPeriodo);
							timesheet.setAbsEntry(timesheetabsentry);
							timesheet.setDateFrom(fechaDesdePeriodo);
							timesheet.setDateTo(fechaHastaPeriodo);
							timesheet.setUserId(usuarioSap.toString());
						}

						Date fechaFichado = DateUtils.toDate(fichadoDTO.getDia(), "dd/MM/yyyy");
						String horaFichadoIngreso = fichadoDTO.getEntrada1();
						
						// Busco el registro repetido.
						List<ProjectManagementTimeSheetLineGetDTO> collect = timesheet.getLineas().stream()
								.filter(x -> buscarFechaHoraRepetida(fechaFichado, horaFichadoIngreso, x))
								.collect(Collectors.toList());

						// Si no hay registros lo agrego
						// FIXME HAY QUE VALIDAR TAMBIEN LOS HORARIOS PORQUE PUEDEN VENIR 1 DIA EN 2 REGISTROS CON DOS FICHADOS DIFERENTES
						// ESTO ES PORQUE UNA PARTE DEL DIA FUE DE LICENCIA Y EL OTRO NO.
						if (collect.isEmpty()) {

							// Obtengo el cantidad de lineas existentes + 1
							Long lineNum = timesheet.getLineas().size() + 1L;

							String fechaFichadoyyyyMMdd = DateUtils.toString(fechaFichado, "yyyyMMdd");

							// Obtengo el horario de ingreso determinado por SAP
							String horarioIngresoEmpleado = employee.getHoraInicio();

							if (horarioIngresoEmpleado == null) {
								errores.put(
										"Legajo " + registroEmpleadoDTO.getEmpleado().getLegajo() + " - Empleado "
												+ employee.getLastName() + " " + employee.getFirstName(),
										"No tiene horario de inicio cargado en SAP");
							} else {

								if (tienenES1 && tienenES2) {
									// Si tengo dos pares de entrada/salida tengo que traer las horas de las
									// actividades por dia partidas en esos horarios y generar 2 registros

									////////////////
									// Armo el primer par de entrada/salida
									////////////////

									// El total de fichado lo obtengo de la diferencia entre la primera
									// entrada/salida

									// Primero determino la hora de entrada. Si es posterior a la del empleado tomo
									// la posterior. Si llego temprano tomo la asignada al empleado.

									String diferenciaHorariaEntrada = DateUtils.calcularDiferenciaHorario(
											fichadoDTO.getEntrada1(), horarioIngresoEmpleado);

									String ingreso = horarioIngresoEmpleado;

									if (diferenciaHorariaEntrada == null) {
										ingreso = fichadoDTO.getEntrada1();
									}

									String totalFichadoHoras1 = DateUtils.calcularDiferenciaHorario(ingreso,
											fichadoDTO.getSalida1());
									Integer totalFichadoSegundos1 = DateUtils
											.convertirATotalSegundos(totalFichadoHoras1);

									// Obtengo el total de horas por actividades del día y el empleado pero usando
									// la consulta a sap y no el mapa
									Integer totalActividadesEnSegundos = 0;
									List<HorasPorEmpleadoDTO> totalHorasES1List = this.activityService
											.obtenerHorasAgrupadasPorFechaEmpleado(new Long(usuarioSap),
													fechaFichadoyyyyMMdd, fechaFichadoyyyyMMdd,
													fichadoDTO.getEntrada1(), fichadoDTO.getSalida1(),
													exclusionesActividadesCalculoHorasNetas);
									if (totalHorasES1List.size() == 1)
										totalActividadesEnSegundos = totalHorasES1List.get(0).getDuration();

									Integer nofacturable = totalFichadoSegundos1 - totalActividadesEnSegundos;

									// Armo el nuevo registro de la collection de dias
									ProjectManagementTimeSheetLineGetDTO timesheetline = generarTimeSheetLine(
											aplicaComida, fechaFichado, nofacturable, lineNum, ingreso,
											fichadoDTO.getSalida1(), fichadoDTO.getNormal(), fichadoDTO.getExtra50(),
											fichadoDTO.getExtra100(),
											fichadoDTO.getComentarios(), fichadoDTO.getEntrada1(),
											fichadoDTO.getTarde(), false);

									timesheet.getLineas().add(timesheetline);

									////////////////
									// Armo el segundo par de entrada/salida
									////////////////

									// El total de fichado lo obtengo de la diferencia entre la primera
									// entrada/salida
									String totalFichadoHoras2 = DateUtils.calcularDiferenciaHorario(
											fichadoDTO.getEntrada2(), fichadoDTO.getSalida2());
									Integer totalFichadoSegundos2 = DateUtils
											.convertirATotalSegundos(totalFichadoHoras2);

									Integer totalActividadesEnSegundos2 = 0;
									List<HorasPorEmpleadoDTO> totalHorasES2List = this.activityService
											.obtenerHorasAgrupadasPorFechaEmpleado(new Long(usuarioSap),
													fechaFichadoyyyyMMdd, fechaFichadoyyyyMMdd,
													fichadoDTO.getEntrada2(), fichadoDTO.getSalida2(),
													exclusionesActividadesCalculoHorasNetas);
									if (totalHorasES2List.size() == 1)
										totalActividadesEnSegundos2 = totalHorasES2List.get(0).getDuration();

									Integer nofacturable2 = totalFichadoSegundos2 - totalActividadesEnSegundos2;

									// Armo el nuevo registro de la collection de dias
									ProjectManagementTimeSheetLineGetDTO timesheetline2 = generarTimeSheetLine(
											aplicaComida, fechaFichado, nofacturable2, lineNum + 1,
											fichadoDTO.getEntrada2(), fichadoDTO.getSalida2(), null, null, null, null,
											fichadoDTO.getEntrada2(), null, true);

									timesheet.getLineas().add(timesheetline2);
									
									// Despues de agregar la linea a la collecion pongo el objeto en el mapa
									mapaEmpleadoPeriodo.put(entryMapCabecera, timesheet);

								} else {

									String diferenciaHorariaEntrada = DateUtils.calcularDiferenciaHorario(
											fichadoDTO.getEntrada1(), horarioIngresoEmpleado);

									String ingreso = horarioIngresoEmpleado;

									if (diferenciaHorariaEntrada == null) {
										ingreso = fichadoDTO.getEntrada1();
									}

									// Si tengo un solo par de entrada/salida uso el mapa creado con la sumatoria
									// general
									String totalFichadoHoras = DateUtils.calcularDiferenciaHorario(ingreso,
											fichadoDTO.getSalida1());
									Integer totalFichadoSegundos = DateUtils.convertirATotalSegundos(totalFichadoHoras);

									// Obtengo el total de horas por actividades del día y el empleado
									Integer totalActividadesEnSegundosD = horasPorEmpleadoDTO != null ? horasPorEmpleadoDTO.getDuration() : null;
									Integer totalActividadesEnSegundos = 0;
									if (totalActividadesEnSegundosD != null)
										totalActividadesEnSegundos = totalActividadesEnSegundosD.intValue();

									Integer nofacturableSegundos = totalFichadoSegundos - totalActividadesEnSegundos;

									// Armo el nuevo registro de la collection de dias
									ProjectManagementTimeSheetLineGetDTO timesheetline = generarTimeSheetLine(
											aplicaComida, fechaFichado, nofacturableSegundos, lineNum, ingreso,
											fichadoDTO.getSalida1(), fichadoDTO.getNormal(), fichadoDTO.getExtra50(),
											fichadoDTO.getExtra100(), fichadoDTO.getComentarios(),
											fichadoDTO.getEntrada1(), fichadoDTO.getTarde(), false);

									timesheet.getLineas().add(timesheetline);

									// Despues de agregar la linea a la collecion pongo el objeto en el mapa
									mapaEmpleadoPeriodo.put(entryMapCabecera, timesheet);

								}
							}
						}

					} else {
						errores.put("Legajo " + registroEmpleadoDTO.getEmpleado().getLegajo() + " Fecha "
								+ fichadoDTO.getDia(), "Horarios de Entrada y Salida incompletos");
					}
				}
			} else {
				errores.put("Legajo " + registroEmpleadoDTO.getEmpleado().getLegajo(),
						"No se procesa ya que esta no tiene mapeado usuario en sap");
			}
		}

		// Una vez que arme el mapa, envio a sap las lineas de cada uno usando un
		// request por empleado
		for (ProjectManagementTimeSheetGetDTO lineGroup : mapaEmpleadoPeriodo.values()) {
			try {
				this.timeSheetService.updateTimeSheetLines(lineGroup);
			} catch (ErrorValidationException e) {
				String error = "";
				for (Map.Entry<String, String> me : e.getErrors().entrySet()) {
					error += me.getKey() + ": " + me.getValue() + " ";
					;
				}
				;
				errores.put("Error al enviar fichaje para usuario sap " + lineGroup.getUserId(), error);

			} catch (Exception e) {
				errores.put("Error al enviar fichaje para usuario sap " + lineGroup.getUserId(),
						SapBusinessException.generarStackTraceString(e));
			}
		}

		if (!errores.isEmpty()) {
			throw new ErrorValidationException(errores);
		}

	}

	private boolean buscarFechaHoraRepetida(Date fechaFichado, String horaFichadoIngreso, ProjectManagementTimeSheetLineGetDTO x) {
		boolean coincideFecha = DateUtils.convertirSinTimeZome(x.getDate())
				.equals(DateUtils.toString(fechaFichado, "dd/MM/yyyy"));
		boolean coincideHorario = x.getHoraFichado().equals(horaFichadoIngreso);
		return coincideFecha && coincideHorario;
	}

	private ProjectManagementTimeSheetLineGetDTO generarTimeSheetLine(boolean aplicaComida, Date fechaFichado,
			Integer nofacturableSegundos, Long lineNum, String entrada1, String salida1, String normal, String extra50,
			String extra100, String comentarios, String horaFichado, String tarde, boolean segundoRegistro) {
		ProjectManagementTimeSheetLineGetDTO timesheetline = new ProjectManagementTimeSheetLineGetDTO();

		String codigo = "";

		if (StringUtils.isNotBlank(comentarios)) {
			codigo = comentarios.split(" ")[0].replace("-", "");
		}

		timesheetline.setDate(DateUtils.toString(fechaFichado, "yyyy-MM-dd"));

		timesheetline.setStartTime(entrada1);
		timesheetline.setEndTime(salida1);

		// FIXME cuando se cambie el proceso para ejecutarse en el preview
		// Si nofacturableSegundos < 0 -> mostrar un alerta. Porque no peude ser que
		// tenga mas horas
		// de actividad que de fichado.

		if (nofacturableSegundos > 0)
			timesheetline.setNonBillableTime(DateUtils.convertirSegundosAFormato(nofacturableSegundos));

		timesheetline.setHoraFichado(horaFichado);

		// Obtengo las claves de ferido e injustificado
		List<String> feriados = Arrays.asList(licenciasFeriado.split(","));
		List<String> injustificados = Arrays.asList(licenciasNoJustificada.split(","));

		if (!feriados.contains(codigo) && !injustificados.contains(codigo)) {
			// Si no es feriado ni tampoco injustificada, son normales y tambien cargo las
			// extras al 50 y al 100
			timesheetline.setHorasNormales(normal);
			timesheetline.setHorasExtras50(extra50);
			timesheetline.setHorasExtras100(extra100);
		} else {

			// Si es feriado o injustificada

			if (feriados.contains(codigo)) {
				// Si es feriado, cargo las horas de feriado
				timesheetline.setHorasFeriado(normal);
			} else if (injustificados.contains(comentarios)) {
				// Si es injustificado, cargo las horas de injustificadas
				timesheetline.setHorasInjustificadas(normal);
			}

			// Si es feriado y trabajo horas extras, tomo las horas extras como horas extras
			// de feriado
			if (feriados.contains(codigo) && StringUtils.isNotBlank(extra100)) {
				// Marco las horas extras como feriado
				timesheetline.setHorasExtrasFeriados(extra100);
			}

		}

		timesheetline.setTipoAusentismo(comentarios);
		timesheetline.setTarde(tarde);

		// Comida va solo para el primer registro
		if (!segundoRegistro) {
			// Pongo la comida como default en no
			timesheetline.setComida("No");

			// Sino hay ausentismo, entonces reviso si va la comida o no.
			if (StringUtils.isBlank(codigo) || (feriados.contains(codigo) && StringUtils.isNotBlank(extra100)))
				timesheetline.setComida(aplicaComida ? "Si" : "No");
		}

		timesheetline.setLineId(lineNum);
		return timesheetline;
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

	@Resource(name = "activityService")
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

}
