package ar.com.avaco.ws.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.ibm.icu.util.Calendar;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.entities.cliente.TipoActividad;
import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.factory.RestTemplatePremec;
import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.dto.ActividadTarjetaDTO;
import ar.com.avaco.ws.dto.ItemCheckDTO;
import ar.com.avaco.ws.dto.RegistroMonitorDTO;
import ar.com.avaco.ws.dto.RepuestoDTO;
import ar.com.avaco.ws.service.ActividadEPService;

@Service("actividadService")
public class ActividadEPServiceImpl implements ActividadEPService {

	private static final Logger LOGGER = Logger.getLogger(ActividadEPServiceImpl.class);

	private UsuarioService usuarioService;

	@Value("${urlSAP}")
	private String urlSAP;
	@Value("${userSAP}")
	private String userSAP;
	@Value("${passSAP}")
	private String passSAP;
	@Value("${dbSAP}")
	private String dbSAP;

	@Value("${monitor.palabras.filtro}")
	private String palabrasFiltroMonitor;
	
	private String employeeUrl;
	private String locationsUrl;
	private String serviceCallUrl;
	private String userUrl;
	private String businessPartnerUrl;
	private String actividadUrl;

	private Gson gson = new Gson();

	private MailSenderSMTPService mailService;

	private List<String> exclusiones;

	@PostConstruct
	public void onInit() {
		this.actividadUrl = urlSAP + "/Activities({id})";
		this.employeeUrl = urlSAP + "/EmployeesInfo({id})";
		this.locationsUrl = urlSAP + "/ActivityLocations?$filter=Code eq {id}";
		this.serviceCallUrl = urlSAP + "/ServiceCalls({id})";
		this.userUrl = urlSAP + "/Users({id})";
		this.businessPartnerUrl = urlSAP + "/BusinessPartners('{id}')";
		
		String[] split = palabrasFiltroMonitor.split(",");
		for (int i = 0; i <= split.length -1; i++) { split[i] = split[i].toUpperCase().trim(); }
		exclusiones = Lists.newArrayList(split);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActividadReporteDTO> getActividadesReporte() throws Exception {

		RestTemplatePremec restTemplate = RestTemplateFactory
				.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).getLoggedRestTemplate();

		// Se agrega validacion para levantar las actividades que no sean de taller.
		String actividadUrl = urlSAP + "/Activities?$filter=U_Estado eq 'Aprobada' and Closed eq 'tNO'";

		// Obtengo las actividades
		ResponseEntity<String> responseActividades = null;
		try {
			responseActividades = restTemplate.doExchange(actividadUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "Error al obtener actividades para envio de reportes";
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("URL: " + actividadUrl + "<br>");
				body.append("Codigo Error: " + e.getErrorCode() + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}

		// Creo el jsonobject
		JsonObject array = gson.fromJson(responseActividades.getBody(), JsonObject.class);

		// Inicializo el listado de actividades donde voy a ir agregando las actividades
		// a enviar el reporte
		List<ActividadReporteDTO> actividades = new ArrayList<>();

		// Obtengo el campo value donde esta el array de actividades
		JsonArray asJsonArray = array.getAsJsonArray("value");

		LOGGER.debug("Iniciando proceso de envio de reporte");

		// Itero por cada actividad del jsonarray
		for (JsonElement element : asJsonArray) {

			// Genero el dto de actividadreporte
			ActividadReporteDTO ardto = new ActividadReporteDTO();

			// Convierto la actividad en un mapa
			LinkedTreeMap<String, Object> fromJson = gson.fromJson(element.getAsJsonObject().toString(),
					LinkedTreeMap.class);

			// Obtengo Activity Code
			Double activityCode = (Double) fromJson.get(FieldUtils.ACTIVITY_CODE);

			try {

				LOGGER.debug("--------- Procesando Actividad " + activityCode.longValue());

				ardto.setIdActividad(activityCode.longValue());

				// Obtengo el parent id que uso para obtener la service call
				Long parentId = FieldUtils.getLong(fromJson, FieldUtils.PARENT_OBJECT_ID, true);

				// Armo la url de la service call
				String surl = serviceCallUrl.replace("{id}", parentId.toString());

				ResponseEntity<String> responseServiceCall = null;
				try {
					responseServiceCall = restTemplate.doExchange(surl, HttpMethod.GET, null,
							new ParameterizedTypeReference<String>() {
							});
				} catch (SapBusinessException e) {
					e.printStackTrace();
					String error = "[REPORTE] No se pudo obtener el service call con id " + parentId;
					if (e.hasToSendMail()) {
						StringBuilder body = new StringBuilder("Servicecall: " + parentId + "<br>");
						body.append("Actividad: " + ardto.getIdActividad() + "<br>");
						body.append("URL: " + surl + "<br>");
						body.append("Codigo Error: " + e.getErrorCode() + "<br>");
						body.append("Error: " + e.getMessage() + "<br>");
						if (e.getCause() != null) {
							body.append("Causa: " + e.getCause().toString() + "<br>");
						}
						mailService.sendMail(error, body.toString(), null);
					}
					throw new Exception(error);
				}

				LinkedTreeMap<String, Object> servicejson = gson.fromJson(responseServiceCall.getBody(),
						LinkedTreeMap.class);

				// Prioridad
				ardto.setPrioridad(FieldUtils.getString(fromJson, FieldUtils.PRIORITY, false));

				// Numero
				ardto.setNumero(activityCode.toString());

				// Es taller
				Boolean esTaller = FieldUtils.getBoolean(fromJson, FieldUtils.U_TALLER, true);
				ardto.setEsTaller(esTaller);

				// Tipo de actividad
				String tipoActividad = FieldUtils.getString(fromJson, FieldUtils.TIPO_ACTIVIDAD, true);
				ardto.setTipoActividad(tipoActividad);

				// Ajuste solicitado por Walter, si la actividad es de cliente, siempre va a ser
				// de reparaci�n 22/4/24
				if (!esTaller) {
					ardto.setTipoActividad(TipoActividad.REPARACION.getCodigo());
				}

				// Asignado Por
				String asignadoPor = "";
				Long asignadoPorId = FieldUtils.getLong(servicejson, FieldUtils.RESPONSE_ASSIGNEE, false);
				if (asignadoPorId != null) {
					ResponseEntity<String> responseUser = null;
					String uurl = userUrl.replace("{id}", asignadoPorId.toString());
					try {
						responseUser = restTemplate.doExchange(uurl, HttpMethod.GET, null,
								new ParameterizedTypeReference<String>() {
								});
					} catch (SapBusinessException e) {
						e.printStackTrace();
						String error = "[REPORTE] No se pudo obtener el empleado con id " + asignadoPorId;
						if (e.hasToSendMail()) {
							StringBuilder body = new StringBuilder("Empleado id: " + asignadoPorId + "<br>");
							body.append("URL: " + uurl + "<br>");
							body.append("Codigo Error: " + e.getErrorCode());
							body.append("Error: " + e.getMessage() + "<br>");
							if (e.getCause() != null) {
								body.append("Causa: " + e.getCause().toString() + "<br>");
							}
							mailService.sendMail(error, body.toString(), null);
						}
						throw new Exception(error);
					}
					JsonObject userjson = gson.fromJson(responseUser.getBody(), JsonObject.class);
					asignadoPor = userjson.get("UserName").toString();
				}
				ardto.setAsignadoPor(asignadoPor);

				// Llamada Id
				ardto.setLlamadaID(parentId.toString());

				// Empleado
				// Id del Empleado Responsable
				Long handledByEmployeeId = FieldUtils.getLong(fromJson, FieldUtils.HANDLED_BY_EMPLOYEE, false);

				String empleado = "";
				if (handledByEmployeeId != null) {
					ResponseEntity<String> responseEmployee = null;
					String eurl = employeeUrl.replace("{id}", handledByEmployeeId.toString());
					try {
						responseEmployee = restTemplate.doExchange(eurl, HttpMethod.GET, null,
								new ParameterizedTypeReference<String>() {
								});
					} catch (SapBusinessException e) {
						e.printStackTrace();
						String error = "[REPORTE] Error al obtener el employee " + handledByEmployeeId;
						if (e.hasToSendMail()) {
							StringBuilder body = new StringBuilder("Empleado id: " + asignadoPorId + "<br>");
							body.append("URL: " + eurl + "<br>");
							body.append("Codigo Error: " + e.getErrorCode());
							body.append("Error: " + e.getMessage() + "<br>");
							if (e.getCause() != null) {
								body.append("Causa: " + e.getCause().toString() + "<br>");
							}
							mailService.sendMail(error, body.toString(), null);
						}
						throw new Exception(error);
					}
					JsonObject employeejson = gson.fromJson(responseEmployee.getBody(), JsonObject.class);
					String fn = employeejson.get(FieldUtils.FIRST_NAME).getAsString();
					String ln = employeejson.get(FieldUtils.LAST_NAME).getAsString();
					empleado = fn + " " + ln;
				}
				ardto.setEmpleado(empleado);

				// Fecha
				ardto.setFecha(FieldUtils.getString(fromJson, FieldUtils.START_DATE, true, 0, 10));

				// Hora
				ardto.setHora(FieldUtils.getString(fromJson, FieldUtils.ACTIVITY_TIME, false));

				// Codigo de articulo
				String itemCode = servicejson.get(FieldUtils.ITEM_CODE) == JsonNull.INSTANCE ? ""
						: servicejson.get(FieldUtils.ITEM_CODE).toString();
				ardto.setCodigoArticulo(itemCode);

				// Nro Serie
				ardto.setNroSerie(FieldUtils.getString(servicejson, FieldUtils.INTERNAL_SERIAL_NUM, false));

				// Cliente
				String cliente = FieldUtils.getString(servicejson, FieldUtils.CUSTOMER_NAME, true);
				ardto.setCliente(cliente);

				// Nro Fabricante
				ardto.setNroFabricante(FieldUtils.getString(servicejson, FieldUtils.MANUFACTURER_SERIAL_NUM, false));

				// Direccion
				String direccion = "";

				if (!esTaller) {

					// Location
					Long locationId = FieldUtils.getLong(fromJson, FieldUtils.LOCATION, true);
					if (locationId > 0) {
						String lurl = locationsUrl.replace("{id}", locationId.toString());
						ResponseEntity<String> responseLocation = null;
						try {
							responseLocation = restTemplate.doExchange(lurl, HttpMethod.GET, null,
									new ParameterizedTypeReference<String>() {
									});
						} catch (SapBusinessException e) {
							e.printStackTrace();
							String error = "[REPORTE] No se pudo obtener la location con id " + locationId;
							if (e.hasToSendMail()) {
								StringBuilder body = new StringBuilder("Empleado id: " + asignadoPorId + "<br>");
								body.append("URL: " + lurl + "<br>");
								body.append("Codigo Error: " + e.getErrorCode());
								body.append("Error: " + e.getMessage() + "<br>");
								if (e.getCause() != null) {
									body.append("Causa: " + e.getCause().toString() + "<br>");
								}
								mailService.sendMail(error, body.toString(), null);
							}
							throw new Exception(error);
						}
						JsonObject locationjson = gson.fromJson(responseLocation.getBody(), JsonObject.class);
						if (locationjson.getAsJsonArray("value").size() == 1) {
							direccion = locationjson.getAsJsonArray("value").get(0).getAsJsonObject().get("Name")
									.getAsString();
						} else {
							throw new Exception("No se pudo obtener la location con id " + locationId
									+ " despues de la ejecuci�n en SAP. URL: " + lurl);
						}
					}
				}
				ardto.setDireccion(direccion);

				// Hs Maquina
				List<LinkedTreeMap<String, Object>> serviceCallActivities = (List<LinkedTreeMap<String, Object>>) servicejson
						.get("ServiceCallActivities");
				for (LinkedTreeMap<String, Object> x : serviceCallActivities) {
					Long acco = FieldUtils.getLong(x, FieldUtils.ACTIVITY_CODE, true);
					if (activityCode.longValue() == acco.longValue()) {
						Integer hm = FieldUtils.getInteger(x, FieldUtils.U_U_HS_MAQ, false);
						ardto.setHorasMaquina(hm != null ? hm : 0);
					}
				}

				// Con cargo
				ardto.setConCargo(FieldUtils.getBoolean(fromJson, FieldUtils.U_CON_CARGO, true));

				// Detalle
				ardto.setDetalle(FieldUtils.getString(servicejson, FieldUtils.SUBJECT, false));

				// Tareas a Realizar
				ardto.setTareasARealizar(FieldUtils.getString(fromJson, FieldUtils.DETAILS, false));

				JsonParser jsonParser = new JsonParser();

				// Checks
				try {
					String tareas = new Gson().toJson(fromJson.get(FieldUtils.U_TAREASREAL));
					Map<String, List<ItemCheckDTO>> map = new HashMap<>();
					if (tareas != null) {
						JsonElement parse = jsonParser.parse(tareas);
						if (!parse.isJsonNull()) {
							JsonArray jsonArray = jsonParser.parse(parse.getAsString().toString()).getAsJsonArray();
							jsonArray.forEach(x -> {
								JsonObject item = x.getAsJsonObject();
								if (!item.get("estado").getAsString().equals("No aplica")) {
									String titulo = item.get("titulo").getAsString();
									List<ItemCheckDTO> list = map.get(titulo);
									if (list == null) {
										list = new ArrayList<ItemCheckDTO>();
									}
									ItemCheckDTO e = new ItemCheckDTO();
									e.setEstado(item.get("estado").getAsString());
									e.setNombre(item.get("nombre").getAsString());
									e.setObservaciones(item.get("observaciones").getAsString());
									e.setTitulo(titulo);
									list.add(e);
									map.put(titulo, list);
								}
							});
						}
					}
					ardto.setChecks(map);

				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception(
							"[REPORTE] Problemas al procesar los checks de la actividad " + activityCode.longValue(),
							e);
				}

				// Observaciones Generales
				ardto.setObservacionesGenerales(FieldUtils.getString(fromJson, FieldUtils.NOTES, false));

				// Repuestos
				try {
					List<RepuestoDTO> listRepuestos = new ArrayList<>();
					String repuestos = new Gson().toJson(fromJson.get("U_Repuestos"));
					if (repuestos != null) {
						JsonElement parse = jsonParser.parse(repuestos);
						if (!parse.isJsonNull()) {
							String asString = parse.getAsString();
							String string = asString.toString();
							JsonArray jsonArrayRepuestos = jsonParser.parse(string).getAsJsonArray();
							jsonArrayRepuestos.forEach(x -> {
								JsonObject item = x.getAsJsonObject();
								String cantidad = item.get("cantidad").getAsString();
								String descripcion = item.get("descripcion").getAsString();
								String nroArticulo = item.get("nroArticulo").getAsString();
								String nroSerie = item.get("nroSerie").getAsString();
								RepuestoDTO rep = new RepuestoDTO();
								rep.setCantidad(Double.parseDouble(cantidad));
								rep.setDescripcion(descripcion);
								rep.setNroArticulo(nroArticulo);
								rep.setNroSerie(nroSerie);
								listRepuestos.add(rep);
							});
						}
					}
					ardto.setRepuestos(listRepuestos);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception(
							"[REPORTE] Problemas al procesar los repuestos de la actividad " + activityCode.longValue(),
							e);
				}

				ardto.setFechaInicioOperario(FieldUtils.getString(fromJson, FieldUtils.START_DATE, true));
				ardto.setHoraInicioOperario(FieldUtils.getString(fromJson, FieldUtils.START_TIME, true));
				ardto.setFechaFinoOperario(FieldUtils.getString(fromJson, FieldUtils.END_DUE_DATE, true));
				ardto.setHoraFinOperario(FieldUtils.getString(fromJson, FieldUtils.END_TIME, true));

				if (!esTaller) {
					ardto.setValoracionResultado(FieldUtils.getString(fromJson, FieldUtils.U_VALORACION, true));
					ardto.setValoracionNombreSuperior(
							FieldUtils.getString(fromJson, FieldUtils.U_NOMBRE_SUPERVISOR, true));
					ardto.setValoracionDNISuperior(FieldUtils.getString(fromJson, FieldUtils.U_DNI_SUPERVISOR, true));

					String valoracion = FieldUtils.getString(fromJson, FieldUtils.U_VALORACION_COMENT, false);
					valoracion = !StringUtils.isBlank(valoracion) ? valoracion : "Sin Comentarios";
					ardto.setValoracionComentarios(valoracion);
				}

				String customerCode = FieldUtils.getString(servicejson, FieldUtils.CUSTOMER_CODE, true);
				Integer bpContactCode = FieldUtils.getInteger(servicejson, FieldUtils.CONTACT_CODE, true);

				ResponseEntity<String> responseBusinessPartner = null;
				String bpurl = businessPartnerUrl.replace("{id}", customerCode);
				try {
					responseBusinessPartner = restTemplate.doExchange(bpurl, HttpMethod.GET, null,
							new ParameterizedTypeReference<String>() {
							});
				} catch (SapBusinessException e) {
					e.printStackTrace();
					String error = "[REPORTE] Error al obtener el business partner " + customerCode;
					if (e.hasToSendMail()) {
						StringBuilder body = new StringBuilder("Empleado id: " + asignadoPorId + "<br>");
						body.append("URL: " + bpurl + "<br>");
						body.append("Codigo Error: " + e.getErrorCode());
						body.append("Error: " + e.getMessage() + "<br>");
						if (e.getCause() != null) {
							body.append("Causa: " + e.getCause().toString() + "<br>");
						}
						mailService.sendMail(error, body.toString(), null);
					}
					throw new Exception(error);
				}

				JsonObject responseBPjson = gson.fromJson(responseBusinessPartner.getBody(), JsonObject.class);
				LinkedTreeMap<String, Object> fromJsonBP = gson.fromJson(responseBPjson.getAsJsonObject().toString(),
						LinkedTreeMap.class);
				List<LinkedTreeMap<String, Object>> mailobj = (List<LinkedTreeMap<String, Object>>) fromJsonBP
						.get("ContactEmployees");

				String email = null;

				for (LinkedTreeMap<String, Object> x : mailobj) {
					Integer parseDouble2 = ((Double) Double.parseDouble(x.get("InternalCode").toString())).intValue();
					if (parseDouble2.equals(bpContactCode)) {
						Object o = x.get("E_Mail");
						if (o != null)
							email = o.toString();
					}
				}

				ardto.setEmail(email);
				actividades.add(ardto);
			} catch (Exception e) {
				// Cualquier exception que haya la catcheo para poder continuar con el siguiente
				// sin cortar el proceso
				e.printStackTrace();
				String subject = "[REPORTE] Error al obtener actividad " + activityCode.longValue()
						+ " para envio de reporte";
				StringBuilder body = new StringBuilder();
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				mailService.sendMail(subject, body.toString(), null);
			}
		}
		return actividades;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ActividadTarjetaDTO> getActividades(String fecha, String username) throws Exception {

		// Armo el rest template logueado con credenciales de sap
		RestTemplatePremec restTemplate = RestTemplateFactory
				.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).getLoggedRestTemplate();

		// Obtengo el usuario sap del usuario logueado
		String usuarioSAP = usuarioService.getUsuarioSAP(username);

		SimpleDateFormat sdfinput = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfoutput = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = sdfinput.parse(fecha);
		String currentDate = sdfoutput.format(parse);
		String fechaActividad = "'" + currentDate + "'";

		// Inicializo la lista de actividades
		List<ActividadTarjetaDTO> actividades = new ArrayList<>();

		ResponseEntity<String> responseActividades = null;

		String actividadUrl = urlSAP + "/Activities?$filter=HandledByEmployee eq " + usuarioSAP + " and StartDate eq "
				+ fechaActividad + " and U_Estado eq 'Pendiente' and Closed eq 'tNO'";
		try {
			responseActividades = restTemplate.doExchange(actividadUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[TARJETA] Error al obtener actividades del usuario " + username + " para la fecha "
					+ currentDate;
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("Fecha: " + currentDate + "<br>");
				body.append("URL: " + actividadUrl + "<br>");
				body.append("Usuario: " + username + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}

		// Parseo la respuesta a un json
		JsonObject jsonResponse = gson.fromJson(responseActividades.getBody(), JsonObject.class);

		// Pido el campo value que contiene el array de actividades
		JsonArray actividadesJsonArray = jsonResponse.getAsJsonArray("value");

		// Por cada actividad
		for (JsonElement element : actividadesJsonArray) {

			Long activityCode = null;

			try {

				// Convierto la actividad en un mapa
				Map<String, Object> fromJson = gson.fromJson(element.getAsJsonObject().toString(), LinkedTreeMap.class);

				// Inicializo la actividad
				ActividadTarjetaDTO atdto = new ActividadTarjetaDTO();

				// Activity Code
				activityCode = FieldUtils.getActivityCode(fromJson, username);
				atdto.setIdActividad(activityCode);

				// Actividad de Taller o Cliente
				atdto.setActividadTaller(FieldUtils.getBoolean(fromJson, FieldUtils.U_TALLER, true));

				// Tipo de actividad
				String tipoActividad = FieldUtils.getString(fromJson, FieldUtils.TIPO_ACTIVIDAD, true);
				atdto.setTipoActividad(tipoActividad);

				if (!atdto.getActividadTaller()) {
					// Ajuste solicitado por Walter, si la actividad es de cliente, siempre va a ser
					// de reparaci�n 22/4/24
					atdto.setTipoActividad(TipoActividad.REPARACION.getCodigo());
				}

				// Prioridad
				atdto.setPrioridad(FieldUtils.getString(fromJson, FieldUtils.PRIORITY, false));

				// Numero igual activity code
				atdto.setNumero(activityCode.toString());

				// Fecha
				atdto.setFecha(FieldUtils.getString(fromJson, FieldUtils.START_DATE, true, 0, 10));

				// Hora
				atdto.setHora(FieldUtils.getString(fromJson, FieldUtils.ACTIVITY_TIME, false));

				// Tareas a realizar
				atdto.setTareasARealizar(FieldUtils.getString(fromJson, FieldUtils.DETAILS, false));

				// Con cargo
				atdto.setConCargo(FieldUtils.getBoolean(fromJson, FieldUtils.U_CON_CARGO, true));

				// Id del Empleado Responsable
				Long handledByEmployeeId = FieldUtils.getLong(fromJson, FieldUtils.HANDLED_BY_EMPLOYEE, false);

				// Empleado
				String empleado = "";
				if (handledByEmployeeId != null) {
					ResponseEntity<String> responseEmployee = null;
					String eurl = employeeUrl.replace("{id}", handledByEmployeeId.toString());
					try {
						responseEmployee = restTemplate.doExchange(eurl, HttpMethod.GET, null,
								new ParameterizedTypeReference<String>() {
								});
					} catch (SapBusinessException e) {
						e.printStackTrace();
						String error = "[TARJETA] Error al obtener el employee " + handledByEmployeeId;
						if (e.hasToSendMail()) {
							StringBuilder body = new StringBuilder();
							body.append("ErrorCode: " + e.getErrorCode() + "<br>");
							body.append("URL: " + eurl + "<br>");
							body.append("Error: " + e.getMessage() + "<br>");
							if (e.getCause() != null) {
								body.append("Causa: " + e.getCause().toString() + "<br>");
							}
							mailService.sendMail(error, body.toString(), null);
						}
						throw new Exception(error);
					}
					JsonObject employeejson = gson.fromJson(responseEmployee.getBody(), JsonObject.class);
					String fn = employeejson.get(FieldUtils.FIRST_NAME).getAsString();
					String ln = employeejson.get(FieldUtils.LAST_NAME).getAsString();
					empleado = fn + " " + ln;
				}
				atdto.setEmpleado(empleado);

				String direccion = "";

				// Si es una actividad en un cliente...
				if (!atdto.getActividadTaller()) {

					// Location
					Long locationId = FieldUtils.getLong(fromJson, FieldUtils.LOCATION, true);

					if (locationId > 0) {
						String lurl = locationsUrl.replace("{id}", locationId.toString());
						ResponseEntity<String> responseLocation = null;
						try {
							responseLocation = restTemplate.doExchange(lurl, HttpMethod.GET, null,
									new ParameterizedTypeReference<String>() {
									});
						} catch (SapBusinessException e) {
							e.printStackTrace();
							String error = "[TARJETA] No se pudo obtener la location con id " + locationId;
							if (e.hasToSendMail()) {
								StringBuilder body = new StringBuilder();
								body.append("ErrorCode: " + e.getErrorCode() + "<br>");
								body.append("URL: " + lurl + "<br>");
								body.append("Error: " + e.getMessage() + "<br>");
								if (e.getCause() != null) {
									body.append("Causa: " + e.getCause().toString() + "<br>");
								}
								mailService.sendMail(error, body.toString(), null);
							}
							throw new Exception(error);
						}

						JsonObject locationjson = gson.fromJson(responseLocation.getBody(), JsonObject.class);
						if (locationjson.getAsJsonArray("value").size() == 1) {
							direccion = locationjson.getAsJsonArray("value").get(0).getAsJsonObject().get("Name")
									.getAsString();
						} else {
							throw new Exception(
									"No se pudo obtener la location con id " + locationId + ". URL: " + lurl);
						}
					}
				}
				atdto.setDireccion(direccion);

				// Obtengo la service call usando prentobjectid
				Long parentId = FieldUtils.getLong(fromJson, FieldUtils.PARENT_OBJECT_ID, true);
				String surl = serviceCallUrl.replace("{id}", parentId.toString());
				ResponseEntity<String> responseServiceCall = null;
				try {
					responseServiceCall = restTemplate.doExchange(surl, HttpMethod.GET, null,
							new ParameterizedTypeReference<String>() {
							});
				} catch (SapBusinessException e) {
					e.printStackTrace();
					String error = "[TARJETA] No se pudo obtener el service call con id " + parentId;
					if (e.hasToSendMail()) {
						StringBuilder body = new StringBuilder();
						body.append("ErrorCode: " + e.getErrorCode() + "<br>");
						body.append("URL: " + surl + "<br>");
						body.append("Error: " + e.getMessage() + "<br>");
						if (e.getCause() != null) {
							body.append("Causa: " + e.getCause().toString() + "<br>");
						}
						mailService.sendMail(error, body.toString(), null);
					}
					throw new Exception(error);
				}

				// Convierto el service call en un mapa
				Map<String, Object> servicejson = gson.fromJson(responseServiceCall.getBody(), LinkedTreeMap.class);

				// Asignado por
				String asignadoPor = "";
				Long asignadoPorId = FieldUtils.getLong(servicejson, FieldUtils.RESPONSE_ASSIGNEE, false);
				if (asignadoPorId != null) {
					ResponseEntity<String> responseUser = null;
					String uurl = userUrl.replace("{id}", asignadoPorId.toString());
					try {
						responseUser = restTemplate.doExchange(uurl, HttpMethod.GET, null,
								new ParameterizedTypeReference<String>() {
								});
					} catch (SapBusinessException e) {
						e.printStackTrace();
						String error = "[TARJETA] No se pudo obtener el empleado con id  " + asignadoPorId;
						if (e.hasToSendMail()) {
							StringBuilder body = new StringBuilder();
							body.append("ErrorCode: " + e.getErrorCode() + "<br>");
							body.append("URL: " + uurl + "<br>");
							body.append("Error: " + e.getMessage() + "<br>");
							if (e.getCause() != null) {
								body.append("Causa: " + e.getCause().toString() + "<br>");
							}
							mailService.sendMail(error, body.toString(), null);
						}
						throw new Exception(error);
					}
					JsonObject userjson = gson.fromJson(responseUser.getBody(), JsonObject.class);
					asignadoPor = userjson.get("UserName").toString();
				}
				atdto.setAsignadoPor(asignadoPor);

				// Es el parent object id
				atdto.setLlamadaID(parentId.toString());

				// El c�digo de articulo
				atdto.setCodigoArticulo(FieldUtils.getString(servicejson, FieldUtils.ITEM_CODE, true));

				// Detalle de la service call
				atdto.setDetalle(FieldUtils.getString(servicejson, FieldUtils.SUBJECT, false));

				// Nro de serie
				atdto.setNroSerie(FieldUtils.getString(servicejson, FieldUtils.INTERNAL_SERIAL_NUM, false));

				// Cliente
				String cliente = "";
				if (!atdto.getActividadTaller()) {
					cliente = FieldUtils.getString(servicejson, FieldUtils.CUSTOMER_NAME, true);
				}
				atdto.setCliente(cliente);

				// Nro Fabricante
				atdto.setNroFabricante(FieldUtils.getString(servicejson, FieldUtils.MANUFACTURER_SERIAL_NUM, false));

				// Busco las horas maquina
				List<LinkedTreeMap<String, Object>> serviceCallActivities = (List<LinkedTreeMap<String, Object>>) servicejson
						.get("ServiceCallActivities");
				for (LinkedTreeMap<String, Object> x : serviceCallActivities) {
					Long acco = FieldUtils.getLong(x, FieldUtils.ACTIVITY_CODE, true);
					if (activityCode.equals(acco)) {
						Integer hm = FieldUtils.getInteger(x, FieldUtils.U_U_HS_MAQ, false);
						atdto.setHorasMaquina(hm != null ? hm : 0);
					}
				}
				actividades.add(atdto);
			} catch (Exception e) {
				e.printStackTrace();
				String subject = "[TARJETA] Error al obtener actividad " + activityCode.longValue() + " del usuario "
						+ username + " para la fecha " + currentDate;
				StringBuilder body = new StringBuilder("Actividad: " + activityCode.toString() + "<br>");
				body.append("Fecha: " + currentDate + "<br>");
				body.append("Usuario: " + username + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				mailService.sendMail(subject, body.toString(), null);
			}
		}

		return actividades;

	}

	@Override
	public void marcarEnviado(Long idActividad) throws Exception {
		RestTemplatePremec restTemplate = RestTemplateFactory
				.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).getLoggedRestTemplate();

		Map<String, Object> map = new HashMap<>();
		map.put("U_Estado", "Enviado");
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map);
		String url = actividadUrl.replace("{id}", idActividad.toString());
		try {
			restTemplate.doExchange(url, HttpMethod.PATCH, httpEntity, Object.class);
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[MARCAR ENVIADO] Error al actualizar actividad " + idActividad
					+ " para marcarla como enviada";
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("URL: " + url + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}
	}

	@Override
	public List<RegistroMonitorDTO> getActividadesMonitor() throws Exception {

		// Armo el rest template logueado con credenciales de sap
		RestTemplatePremec restTemplate = RestTemplateFactory
				.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).getLoggedRestTemplate();

		SimpleDateFormat sdfoutput = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = Calendar.getInstance().getTime();
		String currentDate = sdfoutput.format(parse);
		String fechaActividad = "'" + currentDate + "'";

		// Inicializo la lista de actividades
		List<RegistroMonitorDTO> actividades = new ArrayList<>();

		ResponseEntity<String> responseActividades = null;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Prefer", "odata.maxpagesize=0");

		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		
		String actividadUrl = urlSAP + "/$crossjoin(Activities,EmployeesInfo,ServiceCalls)"
				+ "?$expand=Activities($select=ActivityCode,HandledByEmployee,StartDate,U_Estado,ActivityTime,Details),"
				+ "EmployeesInfo($select=EmployeeID,LastName, FirstName),"
				+ "ServiceCalls($select=ServiceCallID,CustomerName)"
				+ "&$filter=Activities/HandledByEmployee eq EmployeesInfo/EmployeeID "
				+ "and Activities/ParentObjectId eq ServiceCalls/ServiceCallID "
				+ "and HandledByEmployee ne null and StartDate eq " + fechaActividad;
		
		try {
			responseActividades = restTemplate.doExchange(actividadUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<String>() {
					});
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[MONITOR] Error al obtener actividades para el monitor para la fecha "
					+ currentDate;
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("Fecha: " + currentDate + "<br>");
				body.append("URL: " + actividadUrl + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}

		// Parseo la respuesta a un json
		JsonObject jsonResponse = gson.fromJson(responseActividades.getBody(), JsonObject.class);

		// Pido el campo value que contiene el array de actividades
		JsonArray actividadesJsonArray = jsonResponse.getAsJsonArray("value");

		// Por cada actividad
		for (JsonElement element : actividadesJsonArray) {

			Long activityCode = null;

			try {

				// Convierto la actividad en un mapa
				Map<String, Object> fromJson = gson.fromJson(element.getAsJsonObject().toString(), LinkedTreeMap.class);

				Map<String, Object> actividad = (Map<String, Object>) fromJson.get("Activities");
				Map<String, Object> empleado = (Map<String, Object>) fromJson.get("EmployeesInfo");
				Map<String, Object> serviceCalls = (Map<String, Object>) fromJson.get("ServiceCalls");
				
				activityCode = FieldUtils.getLong(actividad, FieldUtils.ACTIVITY_CODE, true);
				
				// Inicializo la actividad
				RegistroMonitorDTO atdto = new RegistroMonitorDTO();

				// Numero igual activity code
				atdto.setNumero(activityCode.toString());

				// Fecha
				atdto.setFecha(FieldUtils.getString(actividad, FieldUtils.START_DATE, true, 0, 10));

				// U_Estado
				atdto.setEstado(FieldUtils.getString(actividad, FieldUtils.U_ESTADO, true));
				
				// Hora
				atdto.setHora(FieldUtils.getString(actividad, FieldUtils.ACTIVITY_TIME, false));

				// Tareas a realizar
				atdto.setTareasARealizar(FieldUtils.getString(actividad, FieldUtils.DETAILS, false));

				// Empleado
				String nombre = FieldUtils.getString(empleado, FieldUtils.FIRST_NAME, true);
				String apellido = FieldUtils.getString(empleado, FieldUtils.LAST_NAME, true);
				atdto.setEmpleado(nombre + " " + apellido);
				
				// Cliente
				atdto.setCliente(FieldUtils.getString(serviceCalls, FieldUtils.CUSTOMER_NAME, true));

				if (!exclusiones.contains(atdto.getTareasARealizar().toUpperCase().trim())) {
					actividades.add(atdto);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				String subject = "[MONITOR] Error al obtener actividad " + activityCode.longValue() + " para la fecha " + currentDate;
				StringBuilder body = new StringBuilder("Actividad: " + activityCode.toString() + "<br>");
				body.append("Fecha: " + currentDate + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				mailService.sendMail(subject, body.toString(), null);
			}
		}

		return actividades;

	}

	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Resource(name = "mailSenderSMTPService")
	public void setMailService(MailSenderSMTPService mailService) {
		this.mailService = mailService;
	}
}
