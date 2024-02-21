package ar.com.avaco.ws.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.dto.ActividadTarjetaDTO;
import ar.com.avaco.ws.dto.ItemCheckDTO;
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

	private String employeeUrl;
	private String locationsUrl;
	private String serviceCallUrl;
	private String userUrl;
	private String businessPartnerUrl;
	private String actividadUrl;

	@PostConstruct
	public void onInit() {
		this.actividadUrl = urlSAP + "/Activities({id})";
		this.employeeUrl = urlSAP + "/EmployeesInfo({id})";
		this.locationsUrl = urlSAP + "/ActivityLocations?$filter=Code eq {id}";
		this.serviceCallUrl = urlSAP + "/ServiceCalls({id})";
		this.userUrl = urlSAP + "/Users({id})";
		this.businessPartnerUrl = urlSAP + "/BusinessPartners('{id}')";
	}

	@Override
	public List<ActividadReporteDTO> getActividadesReporte() throws Exception {

		RestTemplate restTemplate = RestTemplateFactory.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).getLoggedRestTemplate();

		
		// Se agrega validacion para levantar las actividades que no sean de taller.

		String actividadUrl = urlSAP + "/Activities?$filter=U_Estado eq 'Pendiente' and U_Taller eq 'N'";
		
		ResponseEntity<String> responseActividades = null;
		try {
			responseActividades = restTemplate.exchange(actividadUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});
		} catch (RestClientException rce) {
			LOGGER.error("Error al obtener las actividades para reporte");
			LOGGER.error(actividadUrl);
			LOGGER.error(rce.getMessage());
			throw rce;
		}

		Gson gson = new Gson();
		JsonObject array = gson.fromJson(responseActividades.getBody(), JsonObject.class);

		List<ActividadReporteDTO> actividades = new ArrayList<>();

		JsonArray asJsonArray = array.getAsJsonArray("value");
		
		LOGGER.debug("Iniciando proceso de envio de reporte");
		
		for (JsonElement element : asJsonArray) {
			LinkedTreeMap fromJson = gson.fromJson(element.getAsJsonObject().toString(), LinkedTreeMap.class);

			ActividadReporteDTO ardto = new ActividadReporteDTO();

			Double activityCode = Double.parseDouble(fromJson.get("ActivityCode").toString());
			ardto.setIdActividad(activityCode.longValue());

			LOGGER.debug("Procesando Actividad " + activityCode.longValue());
			
			Object parentObjectId = fromJson.get("ParentObjectId");
			if (parentObjectId == null) {
				throw new Exception("No se puede obtener el parentobjectid de la actividad " + activityCode
						+ " para el envio del reporte");
			}
			Long parentId = Double.valueOf(parentObjectId.toString()).longValue();
			ResponseEntity<String> responseServiceCall = null;
			String scurl = serviceCallUrl.replace("{id}", parentId.toString());
			try {
				responseServiceCall = restTemplate.exchange(scurl, HttpMethod.GET, null,
						new ParameterizedTypeReference<String>() {
						});
			} catch (RestClientException rce) {
				LOGGER.error("Error al obtener la service call para envio de reporte");
				LOGGER.error(scurl);
				LOGGER.error(rce.getMessage());
				throw rce;
			}
			JsonObject servicejson = gson.fromJson(responseServiceCall.getBody(), JsonObject.class);

			// Prioridad
			ardto.setPrioridad(fromJson.get("Priority").toString());
			Double ac = (Double) fromJson.get("ActivityCode");

			// Numero
			ardto.setNumero(String.valueOf(ac.intValue()));

			// Asignado Por
			if (fromJson.get("ResponseAssignee") != null) {
				Long asignadoPorId = Double.valueOf(fromJson.get("ResponseAssignee").toString()).longValue();
				String usrUrl = userUrl.replace("{id}", asignadoPorId.toString());
				ResponseEntity<String> responseUser = null;
				try {
					responseUser = restTemplate.exchange(usrUrl, HttpMethod.GET, null,
							new ParameterizedTypeReference<String>() {
							});
				} catch (RestClientException rce) {
					LOGGER.error("Error al obtener el usuario responsable para reporte");
					LOGGER.error(usrUrl);
					LOGGER.error(rce.getMessage());
					throw rce;
				}
				JsonObject userjson = gson.fromJson(responseUser.getBody(), JsonObject.class);
				ardto.setAsignadoPor(userjson.get("UserName").toString());
			} else {
				ardto.setAsignadoPor("--");
			}

			// Lamada Id
			ardto.setLlamadaID(servicejson.get("ServiceCallID").toString());

			// Empleado
			if (fromJson.get("HandledByEmployee") != null) {
				Long handledByEmployeeId = Double.valueOf(fromJson.get("HandledByEmployee").toString()).longValue();
				String heURL = employeeUrl.replace("{id}", handledByEmployeeId.toString());
				ResponseEntity<String> responseEmployee = null;
				try {
					responseEmployee = restTemplate.exchange(heURL, HttpMethod.GET, null,
							new ParameterizedTypeReference<String>() {
							});
				} catch (RestClientException rce) {
					LOGGER.error("Error al obtener el empleado para reporte");
					LOGGER.error(heURL);
					LOGGER.error(rce.getMessage());
					throw rce;
				}
				JsonObject employeejson = gson.fromJson(responseEmployee.getBody(), JsonObject.class);
				ardto.setEmpleado(
						employeejson.get("FirstName").getAsString() + " " + employeejson.get("LastName").getAsString());
			} else {
				ardto.setEmpleado("--");
			}

			// Fecha
			
			String fechastring = fromJson.get("StartDate").toString().substring(0,10);
			ardto.setFecha(fechastring);
			
			// Hora
			ardto.setHora(fromJson.get("ActivityTime").toString());

			// Codigo de articulo
			String itemCode = servicejson.get("ItemCode") == JsonNull.INSTANCE ? ""
					: servicejson.get("ItemCode").toString();
			ardto.setCodigoArticulo(itemCode);

			// Nro Serie
			ardto.setNroSerie(servicejson.get("InternalSerialNum").getAsString());

			// Cliente
			ardto.setCliente(servicejson.get("CustomerName").getAsString());

			// Nro Fabricante
			ardto.setNroFabricante(servicejson.get("ManufacturerSerialNum").toString());

			// Direccion
			Long locationId = Double.valueOf(fromJson.get("Location").toString()).longValue();
			String lourl = locationsUrl.replace("{id}", locationId.toString());
			ResponseEntity<String> responseLocation = null;
			try {
				responseLocation = restTemplate.exchange(lourl, HttpMethod.GET, null,
						new ParameterizedTypeReference<String>() {
						});
			} catch (RestClientException rce) {
				LOGGER.error("Error al obtener la direccion para reporte");
				LOGGER.error(lourl);
				LOGGER.error(rce.getMessage());
				throw rce;
			}
			JsonObject locationjson = gson.fromJson(responseLocation.getBody(), JsonObject.class);
			ardto.setDireccion(locationjson.getAsJsonArray("value").size() == 1
					? locationjson.getAsJsonArray("value").get(0).getAsJsonObject().get("Name").getAsString()
					: "No encontrada " + locationId.toString());

			// Hs Maquina
			String accoStr = String.valueOf(ac.longValue());
			JsonArray serviceCallActivities = servicejson.get("ServiceCallActivities").getAsJsonArray();
			serviceCallActivities.forEach(x -> {
				JsonObject item = x.getAsJsonObject();
				String acco = item.get("ActivityCode").toString();
				if (acco.equals(accoStr)) {
					JsonElement jsonElement = item.get("U_U_HsMaq");
					int hsmaq = jsonElement.isJsonNull() ? 0 : jsonElement.getAsInt();
					ardto.setHorasMaquina(hsmaq);
				}
			});

			// Con cargo
			ardto.setConCargo(
					fromJson.get("U_ConCargo") == null || !fromJson.get("U_ConCargo").equals("Y") ? "No" : "Si");

			// Detalle
			ardto.setDetalle(servicejson.get("Subject").getAsString());

			Object detailsObject = fromJson.get("Details");
			ardto.setTareasARealizar(detailsObject != null ? detailsObject.toString() : "");

			JsonParser jsonParser = new JsonParser();

			// Checks
			try {
				String tareas = new Gson().toJson(fromJson.get("U_Tareasreal"));
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
				LOGGER.debug("Problemas al procesar los checks de la actividad " + activityCode.longValue());
				throw e;
			}
			
			Object notesobj = fromJson.get("Notes");
			ardto.setObservacionesGenerales(notesobj == null ? "" : notesobj.toString());

			try {
				List<RepuestoDTO> listRepuestos = new ArrayList<>();
				String repuestos = new Gson().toJson(fromJson.get("U_Repuestos"));
				if (repuestos != null) {
					JsonElement parse = jsonParser.parse(repuestos);
					if (!parse.isJsonNull()) {
						String asString = parse.getAsString();
						String string = asString.toString();
						JsonArray jsonArrayRepuestos = jsonParser.parse(string)
								.getAsJsonArray();
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
				LOGGER.debug("Problemas al procesar los repuestos de la actividad " + activityCode.longValue());
				throw e;
			}
			
			ardto.setFechaInicioOperario(fromJson.get("StartDate").toString());
			ardto.setHoraInicioOperario(fromJson.get("StartTime").toString());
			ardto.setFechaFinoOperario(fromJson.get("EndDueDate").toString());
			ardto.setHoraFinOperario(fromJson.get("EndTime").toString());

			ardto.setValoracionResultado(fromJson.get("U_Valoracion").toString());
			ardto.setValoracionNombreSuperior(fromJson.get("U_NomSupervisor").toString());
			ardto.setValoracionDNISuperior(
					((Long) Double.valueOf(fromJson.get("U_DniSupervisor").toString()).longValue()).toString());
			Object object2 = fromJson.get("U_ValoracionComent");
			ardto.setValoracionComentarios(object2 != null ? object2.toString() : "Sin comentarios");

			String customerCode = servicejson.get("CustomerCode").getAsString();
			String bpContactCode = servicejson.get("ContactCode").getAsString();
			ResponseEntity<String> responseBusinessPartner = null;
			String bpurl = businessPartnerUrl.replace("{id}", customerCode);
			try {
				responseBusinessPartner = restTemplate.exchange(bpurl, HttpMethod.GET, null,
						new ParameterizedTypeReference<String>() {
						});
			} catch (RestClientException rce) {
				LOGGER.error("Error al obtener el business partner para reporte");
				LOGGER.error(bpurl);
				LOGGER.error(rce.getMessage());
				throw rce;
			}
			JsonObject responseBPjson = gson.fromJson(responseBusinessPartner.getBody(), JsonObject.class);
			LinkedTreeMap fromJsonBP = gson.fromJson(responseBPjson.getAsJsonObject().toString(), LinkedTreeMap.class);
			List<LinkedTreeMap> mailobj = (List<LinkedTreeMap>) fromJsonBP.get("ContactEmployees");

			String email = null;

			for (LinkedTreeMap x : mailobj) {
				Integer parseDouble2 = ((Double) Double.parseDouble(x.get("InternalCode").toString())).intValue();
				if (parseDouble2.toString().equals(bpContactCode)) {
					Object o = x.get("E_Mail");
					if (o != null)
						email = o.toString();
				}
			}

			ardto.setEmail(email);

			actividades.add(ardto);
		}
		return actividades;
	}

	@Override
	public List<ActividadTarjetaDTO> getActividades(String fecha, String username) throws Exception {

		RestTemplate restTemplate = null;
		restTemplate = RestTemplateFactory.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).getLoggedRestTemplate();

		String usuarioSAP = usuarioService.getUsuarioSAP(username);
		SimpleDateFormat sdfinput = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfoutput = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = sdfinput.parse(fecha);
		String fechaActividad = "'" + sdfoutput.format(parse) + "'";

		ResponseEntity<String> responseActividades = obtenerActividadesPorUsuarioYFecha(restTemplate, usuarioSAP,
				fechaActividad);

		Gson gson = new Gson();
		JsonObject array = gson.fromJson(responseActividades.getBody(), JsonObject.class);

		List<ActividadTarjetaDTO> actividades = new ArrayList<>();

		JsonArray asJsonArray = array.getAsJsonArray("value");
		for (JsonElement element : asJsonArray) {
			LinkedTreeMap fromJson = gson.fromJson(element.getAsJsonObject().toString(), LinkedTreeMap.class);

			ActividadTarjetaDTO atdto = new ActividadTarjetaDTO();
			
			// Campo U_Taller para determinar si la actividad es en taller o cliente
			atdto.setActividadTaller(fromJson.get("U_Taller") == null || fromJson.get("U_Taller").equals("Y") ? true : false);
			
			atdto.setPrioridad(fromJson.get("Priority").toString());
			Double activityCode = Double.parseDouble(fromJson.get("ActivityCode").toString());
			atdto.setIdActividad(activityCode.longValue());
			Double ac = (Double) fromJson.get("ActivityCode");
			atdto.setNumero(String.valueOf(ac.intValue()));

			String fechastring = fromJson.get("StartDate").toString().substring(0,10);
			atdto.setFecha(fechastring);
			
			atdto.setHora(fromJson.get("ActivityTime").toString());
			Object object = fromJson.get("Details");
			atdto.setTareasARealizar(object != null ? object.toString() : "");
			atdto.setConCargo(
					fromJson.get("U_ConCargo") == null || !fromJson.get("U_ConCargo").equals("Y") ? false : true);

			if (fromJson.get("HandledByEmployee") != null) {
				Long handledByEmployeeId = Double.valueOf(fromJson.get("HandledByEmployee").toString()).longValue();
				ResponseEntity<String> responseEmployee = null;
				String eurl = employeeUrl.replace("{id}", handledByEmployeeId.toString());
				try {
					responseEmployee = restTemplate.exchange(eurl, HttpMethod.GET, null,
							new ParameterizedTypeReference<String>() {
							});
				} catch (RestClientException rce) {
					LOGGER.error("Error al obtener el empleado");
					LOGGER.error(eurl);
					LOGGER.error(rce.getMessage());
					throw rce;
				}
				JsonObject employeejson = gson.fromJson(responseEmployee.getBody(), JsonObject.class);
				atdto.setEmpleado(
						employeejson.get("FirstName").getAsString() + " " + employeejson.get("LastName").getAsString());
			} else {
				atdto.setEmpleado("--");
			}

			if (atdto.getActividadTaller()) {
				Long locationId = Double.valueOf(fromJson.get("Location").toString()).longValue();
				String lurl = locationsUrl.replace("{id}", locationId.toString());
				ResponseEntity<String> responseLocation = null;
				try {
					responseLocation = restTemplate.exchange(lurl, HttpMethod.GET, null,
							new ParameterizedTypeReference<String>() {
							});
				} catch (RestClientException rce) {
					LOGGER.error("Error al obtener el domicilio");
					LOGGER.error(lurl);
					LOGGER.error(rce.getMessage());
					throw rce;
				}
				JsonObject locationjson = gson.fromJson(responseLocation.getBody(), JsonObject.class);
				atdto.setDireccion(locationjson.getAsJsonArray("value").size() == 1
						? locationjson.getAsJsonArray("value").get(0).getAsJsonObject().get("Name").getAsString()
						: "No encontrada " + locationId.toString());

			} else {
				atdto.setDireccion("");
			}

			// Obtengo la service call usando prentobjectid
			Long parentId = Double.valueOf(fromJson.get("ParentObjectId").toString()).longValue();
			String surl = serviceCallUrl.replace("{id}", parentId.toString());
			ResponseEntity<String> responseServiceCall = null;
			try {
				responseServiceCall = restTemplate.exchange(surl, HttpMethod.GET, null,
						new ParameterizedTypeReference<String>() {
						});
			} catch (RestClientException rce) {
				LOGGER.error("Error al obtener el servicio");
				LOGGER.error(surl);
				LOGGER.error(rce.getMessage());
				throw rce;
			}
			JsonObject servicejson = gson.fromJson(responseServiceCall.getBody(), JsonObject.class);

			if (fromJson.get("ResponseAssignee") != null) {
				Long asignadoPorId = Double.valueOf(fromJson.get("ResponseAssignee").toString()).longValue();
				ResponseEntity<String> responseUser = null;
				String uurl = userUrl.replace("{id}", asignadoPorId.toString());
				try {
					responseUser = restTemplate.exchange(uurl, HttpMethod.GET, null,
							new ParameterizedTypeReference<String>() {
							});
				} catch (RestClientException rce) {
					LOGGER.error("Error al obtener el responsable");
					LOGGER.error(uurl);
					LOGGER.error(rce.getMessage());
					throw rce;
				}
				JsonObject userjson = gson.fromJson(responseUser.getBody(), JsonObject.class);
				atdto.setAsignadoPor(userjson.get("UserName").toString());
			} else {
				atdto.setAsignadoPor("--");
			}

			atdto.setLlamadaID(servicejson.get("ServiceCallID").toString());
			
			String itemCode = servicejson.get("ItemCode") == JsonNull.INSTANCE ? ""
					: servicejson.get("ItemCode").toString();
			atdto.setCodigoArticulo(itemCode);
			atdto.setDetalle(servicejson.get("Subject").getAsString());
			atdto.setNroSerie(servicejson.get("InternalSerialNum").getAsString());
			
			if (atdto.getActividadTaller()) {
				atdto.setCliente(servicejson.get("CustomerName").getAsString());
			} else {
				atdto.setCliente("");
			}
			atdto.setNroFabricante(servicejson.get("ManufacturerSerialNum").toString());

			String accoStr = String.valueOf(activityCode.longValue());
			JsonArray serviceCallActivities = servicejson.get("ServiceCallActivities").getAsJsonArray();
			serviceCallActivities.forEach(x -> {
				JsonObject item = x.getAsJsonObject();
				String acco = item.get("ActivityCode").toString();
				if (acco.equals(accoStr)) {
					JsonElement jsonElement = item.get("U_U_HsMaq");
					int hsmaq = jsonElement.isJsonNull() ? 0 : jsonElement.getAsInt();
					atdto.setHorasMaquina(hsmaq);
				}
			});

			actividades.add(atdto);
		}

		return actividades;
	}

	private ResponseEntity<String> obtenerActividadesPorUsuarioYFecha(RestTemplate restTemplate, String usuarioSAP,
			String fechaActividad) {
		String actividadUrl = urlSAP + "/Activities?$filter=HandledByEmployee eq " + usuarioSAP + " and StartDate eq "
				+ fechaActividad + " and U_Estado eq 'Pendiente'";
		ResponseEntity<String> responseActividades = null;
		try {
			responseActividades = restTemplate.exchange(actividadUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});
		} catch (RestClientException rce) {
			LOGGER.error(
					"Error al obtener las actividades del empleado " + usuarioSAP + " para el dia " + fechaActividad);
			LOGGER.error(actividadUrl);
			LOGGER.error(rce.getMessage());
			throw rce;
		}
		return responseActividades;
	}

	@Override
	public void marcarEnviado(Long idActividad) throws Exception {
		RestTemplate restTemplate = RestTemplateFactory.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).getLoggedRestTemplate();

		Map<String, Object> map = new HashMap<>();
		map.put("U_Estado", "Enviado");
//		map.put("DocEntry", idActividad);
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map);
		String url = actividadUrl.replace("{id}", idActividad.toString());
		try {
			restTemplate.exchange(url, HttpMethod.PATCH, httpEntity, Object.class);
		} catch (RestClientException rce) {
			LOGGER.error("Error al actualizar el estado de la actividad " + idActividad);
			LOGGER.error(url);
			LOGGER.error(rce.getMessage());
			throw rce;
		}
	}

	@Resource(name = "usuarioService")
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
}
