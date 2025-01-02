package ar.com.avaco.ws.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.factory.ParentObjectIdNotFoundException;
import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.factory.RestTemplatePremec;
import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.utils.DateUtils;
import ar.com.avaco.ws.dto.ActividadPatch;
import ar.com.avaco.ws.dto.FormularioDTO;
import ar.com.avaco.ws.dto.FotoDTO;
import ar.com.avaco.ws.service.FormularioEPService;

@Service("formularioEPService")
public class FormularioEPServiceImpl implements FormularioEPService {

	@Value("${urlSAP}")
	private String urlSAP;

	@Value("${userSAP}")
	private String userSAP;

	@Value("${passSAP}")
	private String passSAP;

	@Value("${dbSAP}")
	private String dbSAP;

	@Value("${email.from}")
	private String from;

	@Value("${email.errores}")
	private String toErrores;

	@Value("${email.errores.cc}")
	private String toErroresCC;

	@Value("${informe.path}")
	private String informePath;

	@Value("${json.path}")
	private String jsonPath;

	@Value("${json.path.revision}")
	private String jsonPathRevision;

	@Value("${json.path.finalizado}")
	private String jsonPathFinalizado;

	@Value("${json.path.cerradas}")
	private String jsonPathCerradas;

	@Value("${json.delete.after.send}")
	private boolean deleteFileAfterSend;

	@Value("${json.quitar.cola.envio}")
	private String jsonPathQuitado;

	@Value("${enviroment.url}")
	private String enviromentURL;

	private MailSenderSMTPService mailService;

	private Gson gson = new Gson();

	private SAPWSUtils sapUtils;

	private RestTemplatePremec restTemplate;

	private static final Logger LOGGER = Logger.getLogger(FormularioEPService.class);
	
	private static final String ACTIVITIES_SELECT_PARENT_OBJECT_ID = "/Activities({id})?$select=ParentObjectId";
	private static final String SERVICE_CALLS_SELECT_SERVICE_CALL_ACTIVITIES = "/ServiceCalls({id})?$select=ServiceCallActivities";
	

	@Override
	public void grabarFormulario(FormularioDTO formularioDTO, String usuariosap) throws Exception {
		LOGGER.debug("Grabando formulario " + formularioDTO.getIdActividad());
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String pathname = jsonPath + "\\actividad-" + formularioDTO.getIdActividad() + "-usuariosap-" + usuariosap
				+ ".txt";
		try {
			String json = mapper.writeValueAsString(formularioDTO);
			File file = new File(pathname);
			FileUtils.writeStringToFile(file, json);
		} catch (JsonProcessingException e) {
			LOGGER.debug("No se pudo serializar el formulario " + formularioDTO.getIdActividad());
			e.printStackTrace();
			throw new Exception("No se pudo serializar el formulario " + formularioDTO.getIdActividad(), e);
		} catch (IOException e) {
			LOGGER.debug("No se pudo grabar el archivo" + pathname);
			e.printStackTrace();
			throw new Exception("No se pudo grabar el archivo " + pathname, e);
		}
	}

	@Override
	public void enviarFormulariosFromFiles() throws Exception {

		LOGGER.debug("Iniciando envio de formularios SAP");
		ObjectMapper mapper = new ObjectMapper();
		File folder = new File(jsonPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				Long activityCode = null;
				String fileName = null;
				try {
					String readFileToString = FileUtils.readFileToString(file);
					FormularioDTO formularioDTO = mapper.readValue(readFileToString, FormularioDTO.class);
					fileName = file.getName();
					String[] split = fileName.split("\\.")[0].split("-");
					String userId = split[3];
					String formularioId = split[1];
					activityCode = formularioDTO.getIdActividad();

					if (isActividadAbierta(activityCode)) {
						LOGGER.debug("##### Enviando formulario " + formularioId);
						// Se envia el formulario a sap
						enviarFormulario(formularioDTO, userId);
						LOGGER.debug("##### Formulario " + formularioId + " enviado");

						if (deleteFileAfterSend) {
							file.delete();
							LOGGER.debug("Archivo borrado luego del envio");
						} else {
							FileUtils.moveFileToDirectory(file, new File(jsonPathFinalizado), true);
						}
					} else {
						FileUtils.moveFileToDirectory(file, new File(jsonPathCerradas), true);
						String subject = "Se mueve la actividad " + activityCode + " a la carpeta de cerradas";
						String body = "Tomar las acciones necesarias y solicitar mover la actividad a la carpeta de pendientes";
						mailService.sendMail(from, toErrores, toErroresCC, subject, body.toString(), null);
					}

				} catch (ParentObjectIdNotFoundException e) {
					e.printStackTrace();
					String subject = "Error al enviar actividad " + activityCode + " a SAP";
					StringBuilder body = new StringBuilder("Actividad: " + activityCode.toString() + "<br>");
					body.append("La actividad " + activityCode.toString() + " no tiene parent object id asignado.<br>");
					body.append("Se mueve el archivo " + fileName + " a la carpeta " + jsonPathRevision + "<br>");
					body.append("Error: " + e.getMessage() + "<br>");
					if (e.getCause() != null) {
						body.append("Causa: " + e.getCause().toString() + "<br>");
					}
					mailService.sendMail(from, toErrores, toErroresCC, subject, body.toString(), null);
					FileUtils.moveFileToDirectory(file, new File(jsonPathRevision), true);
				} catch (Exception e) {
					e.printStackTrace();
					String subject = "Error al enviar actividad " + activityCode + " a SAP";
					StringBuilder body = new StringBuilder("Actividad: " + activityCode.toString() + "<br>");
					body.append("Error: " + e.getMessage() + "<br>");
					if (e.getCause() != null) {
						body.append("Causa: " + e.getCause().toString() + "<br>");
					}
					body.append(generarUrlQuitarColaEnvio(activityCode) + "<br>");
					mailService.sendMail(from, toErrores, toErroresCC, subject, body.toString(), null);
				}

			}
		}

	}

	private boolean isActividadAbierta(Long activityCode) throws Exception {

		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();

		String actividadUrl = urlSAP + "/Activities?$filter=Closed eq 'tNO' and ActivityCode eq " + activityCode;

		ResponseEntity<String> responseActividades = null;
		try {
			responseActividades = this.restTemplate.exchange(actividadUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
			String subject = "Error al obtener actividad " + actividadUrl
					+ " para determinar si esta abierta previo al envio a SAP ";
			StringBuilder body = new StringBuilder();
			body.append("URL " + actividadUrl + "<br>");
			body.append("Error: " + e.getMessage() + "<br>");
			body.append(generarUrlQuitarColaEnvio(activityCode) + "<br>");
			if (e.getCause() != null) {
				body.append("Causa: " + e.getCause().toString() + "<br>");
			}
			body.append(generarUrlQuitarColaEnvio(activityCode) + "<br>");
			mailService.sendMail(from, toErrores, toErroresCC, subject, body.toString(), null);
			throw e;
		}

		JsonObject array = gson.fromJson(responseActividades.getBody(), JsonObject.class);

		JsonArray jsonElement = array.getAsJsonArray("value");

		return jsonElement.size() > 0;

	}

	private Object generarUrlQuitarColaEnvio(Long activityCode) {
		String msj = "Si desea quitar la actividad de la cola de envíos, haga click ";
		msj = msj + "<a href='{enviroment}/quitarArchivoColaEnvio?actividadId={activityCode}' target='_blank'>aquí</a>"
				.replace("{enviroment}", enviromentURL).replace("{activityCode}", activityCode.toString());
		return msj;
	}

	@Override
	public void enviarFormulario(FormularioDTO formulario, String usuarioSAP) throws Exception {

		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();

		this.sapUtils = new SAPWSUtils(restTemplate, urlSAP);

		// Obtengo la actividad
		Long idActividad = formulario.getIdActividad();

		// Obtengo el parent object id que es el id de service call
		Long serviceCallId = getServiceCallId(idActividad, sapUtils);

		// Obtengo las actividades de la service call
		JsonArray serviceCallActivitiesJson = getServiceCallActivities(formulario, idActividad, serviceCallId);

		// Armo el mapa de la servicecall para actualizar las horas maquina
		Map<String, Object> serviceCallMap = generarServiceCallMap(formulario, idActividad, serviceCallId,
				serviceCallActivitiesJson);

		// Actualizo las horas maquina
		updateHorasMaquina(idActividad, serviceCallId, serviceCallMap);

		// Genero el patch de actividad
		ActividadPatch ap = generarActividadPatch(formulario);

		// Genero el map de attachments (para las fotos)
		Map<String, Object> attachmentMap = generarAttachmentMap(formulario, usuarioSAP, idActividad);

		// Envio los attachments (fotos) a sap
		enviarAttachmentsSap(idActividad, ap, attachmentMap);

		// Actualizo la actividad
		updateActividadSap(idActividad, ap);

		// Valido las horas maquina actuales contra las ultimas
		validarHorasMaquina(serviceCallId, Integer.parseInt(formulario.getHorasMaquina()));

	}

	@Override
	public void validarHorasMaquina(Long serviceCallId, int horasMaquina) throws Exception {

		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();

		String query = urlSAP + "/$crossjoin(ServiceCalls,ServiceContracts)?"
				+ "$expand=ServiceContracts($select=U_Hs_Contratadas)&"
				+ "$filter=ServiceCalls/ContractID eq ServiceContracts/ContractID "
				+ "and ServiceCalls/ServiceCallID eq {serviceCallId}";

		query = query.replace("{serviceCallId}", serviceCallId.toString());

		ResponseEntity<String> contractHours = restTemplate.doExchange(query, HttpMethod.GET, null,
				new ParameterizedTypeReference<String>() {
				});

		JsonElement jsonElementResp = ((com.google.gson.JsonObject) gson.fromJson(contractHours.getBody(),
				JsonObject.class)).get("value");

		if (jsonElementResp != null && !jsonElementResp.isJsonNull() && jsonElementResp.isJsonArray()
				&& jsonElementResp.getAsJsonArray().size() == 1) {

			int maxmensual = jsonElementResp.getAsJsonArray().get(0).getAsJsonObject().get("ServiceContracts")
					.getAsJsonObject().get("U_Hs_Contratadas").getAsInt();

			String actividadAnterior = "/$crossjoin(ServiceCalls/ServiceCallActivities,Activities)?"
					+ "$expand=ServiceCalls/ServiceCallActivities($select=ActivityCode,U_U_HsMaq,LineNum),"
					+ "Activities($select=ActivityCode,StartDate)&"
					+ "$filter=ServiceCalls/ServiceCallActivities/ActivityCode eq Activities/ActivityCode "
					+ "and ServiceCalls/ServiceCallID eq {serviceCallId} and (Activities/HandledBy eq null or Activities/HandledBy not eq 17 or Activities/HandledBy not eq 29) &"
					+ "$orderby=ServiceCalls/ServiceCallActivities/ActivityCode desc&$top=1&$skip=1";

			LOGGER.debug("ServiceCall: " + serviceCallId + " - Obteniendo Actividad previa");
			String serviceCallActivitiesUrl = urlSAP + actividadAnterior;
			serviceCallActivitiesUrl = serviceCallActivitiesUrl.replace("{serviceCallId}", serviceCallId.toString());
			LOGGER.debug(urlSAP);

			ResponseEntity<String> serviceCallActivities = null;
			serviceCallActivities = restTemplate.doExchange(serviceCallActivitiesUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});

			LOGGER.debug("ServiceCall: " + serviceCallId + " - Actividad previa obtenida");

			JsonElement jsonElementResp2 = ((com.google.gson.JsonObject) gson.fromJson(serviceCallActivities.getBody(),
					JsonObject.class)).get("value");

			if (jsonElementResp2 != null && !jsonElementResp2.isJsonNull() && jsonElementResp2.isJsonArray()
					&& jsonElementResp2.getAsJsonArray().size() == 1) {

				JsonElement jsonElement = jsonElementResp2.getAsJsonArray().get(0);

				int lineNum = jsonElement.getAsJsonObject().get("ServiceCalls/ServiceCallActivities")
						.getAsJsonObject().get("LineNum").getAsInt();
				
				int activityCodeAnterior = jsonElement.getAsJsonObject().get("ServiceCalls/ServiceCallActivities")
						.getAsJsonObject().get("ActivityCode").getAsInt();
				
				JsonElement jsonElementHsMaq = jsonElement.getAsJsonObject().get("ServiceCalls/ServiceCallActivities")
						.getAsJsonObject().get("U_U_HsMaq");
				
				int hsMaqAnterior = 0;
				if (!jsonElementHsMaq.isJsonNull()) {
					hsMaqAnterior = jsonElementHsMaq.getAsInt();
				} else if (lineNum != 0) {
					throw new Exception("La actividad " + activityCodeAnterior + " no tiene horas maquina cargadas y no es la primera de la servicecall " + serviceCallId);
				}

				String fecha = jsonElement.getAsJsonObject().get("Activities").getAsJsonObject().get("StartDate")
						.getAsString();

				Date fechaAnterior = DateUtils.toDate(fecha, "yyyy-MM-dd");
				Date fechaActual = DateUtils.getFechaYHoraActual();

				LocalDateTime date1 = LocalDateTime.ofInstant(fechaActual.toInstant(), ZoneId.systemDefault());
				LocalDateTime date2 = LocalDateTime.ofInstant(fechaAnterior.toInstant(), ZoneId.systemDefault());

				double diasDouble = Math.abs(new Long(Duration.between(date1, date2).toDays()).doubleValue());
				double horasDouble = new Long(horasMaquina - hsMaqAnterior);

				if (horasDouble < 0) {
					notificarReseteoHoras(serviceCallId, fechaAnterior, fechaActual, hsMaqAnterior, horasMaquina);
				} else {

					double promedio = horasDouble / diasDouble;

					double promedioMax = new Double(maxmensual) / (double) 30;

					if (promedio > promedioMax) {
						notificarHorasMaquinaSuperadas(serviceCallId, fechaAnterior, fechaActual, hsMaqAnterior,
								horasMaquina);
					}
				}
			}
		}

	}

	private void notificarHorasMaquinaSuperadas(Long serviceCallId, Date fechaAnterior, Date fechaActual,
			Integer hsMaqAnterior, Integer horasMaquina) {
		String subject = "Horas Maquina Superadas - ServiceCall " + serviceCallId + "<br>";
		String body = "Se ha detectado una posible superacion de horas maquinas mensual. <br>";
		body += "Fecha Actividad Anterior: {fechaultimaactividad} <br>".replace("{fechaultimaactividad}",
				DateUtils.toString(fechaAnterior, "dd/MM/yyyy"));
		body += "Horas Maquina Anterior: {horasanterior} <br>".replace("{horasanterior}", hsMaqAnterior.toString());
		body += "Fecha Actividad Actual: {fechaactual} <br>".replace("{fechaactual}",
				DateUtils.toString(fechaActual, "dd/MM/yyyy"));
		body += "Horas Maquina Actual: {horasactual} <br>".replace("{horasactual}", horasMaquina.toString());
		mailService.sendMail(from, from, subject, body, null);
	}

	private void notificarReseteoHoras(Long serviceCallId, Date fechaAnterior, Date fechaActual, Integer hsMaqAnterior,
			Integer horasMaquina) {
		String subject = "Horas Maquina Reseteadas - ServiceCall " + serviceCallId + "<br>";
		String body = "Se ha detectado una posible reset de horas maquinas. <br>";
		body += "Fecha Actividad Anterior: {fechaultimaactividad} <br>".replace("{fechaultimaactividad}",
				DateUtils.toString(fechaAnterior, "dd/MM/yyyy"));
		body += "Horas Maquina Anterior: {horasanterior} <br>".replace("{horasanterior}", hsMaqAnterior.toString());
		body += "Fecha Actividad Actual: {fechaactual} <br>".replace("{fechaactual}",
				DateUtils.toString(fechaActual, "dd/MM/yyyy"));
		body += "Horas Maquina Actual: {horasactual} <br>".replace("{horasactual}", horasMaquina.toString());
		mailService.sendMail(from, from, subject, body, null);
	}

	private void updateActividadSap(Long idActividad, ActividadPatch ap) throws Exception {
		
		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();
		
		LOGGER.debug("Actividad: " + idActividad + " - Actividad Patch Generado");
		try {
			LOGGER.debug("Actividad: " + idActividad + " - Actualizando Actividad");
			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(ap.getAsMap());
			String actividadUrl = urlSAP + "/Activities({id})".replace("{id}", idActividad.toString());
			this.restTemplate.doExchange(actividadUrl, HttpMethod.PATCH, httpEntity, Object.class);
			LOGGER.debug("Actividad Actualizada");
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[ENVIARFORMULARIO] Actividad: " + idActividad
					+ " - Error al intentar actualizar la actividad";
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				body.append(generarUrlQuitarColaEnvio(idActividad) + "<br>");
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}
	}

	private void enviarAttachmentsSap(Long idActividad, ActividadPatch ap, Map<String, Object> attachmentMap)
			throws Exception {
		
		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();
		
		try {
			LOGGER.debug("Actividad: " + idActividad + " - Enviando Attachments");

			String attachmentUrl = urlSAP + "/Attachments2";
			HttpEntity<Map<String, Object>> httpEntityAttach = new HttpEntity<>(attachmentMap);
			ResponseEntity<Object> attachmentRespose = null;
			attachmentRespose = this.restTemplate.doExchange(attachmentUrl, HttpMethod.POST, httpEntityAttach,
					Object.class);
			Object object = ((Map) attachmentRespose.getBody()).entrySet().toArray()[1];
			String attchEntry = (object.toString().split("="))[1];

			ap.setAttachmentEntry(attchEntry);
			LOGGER.debug("Actividad: " + idActividad + " - Attachments Enviados");
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[ENVIARFORMULARIO]  Actividad: " + idActividad + " - Error al intentar subir attachments";
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				body.append(generarUrlQuitarColaEnvio(idActividad) + "<br>");
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}
	}

	private Map<String, Object> generarAttachmentMap(FormularioDTO formulario, String usuarioSAP, Long idActividad)
			throws Exception {

		Map<String, Object> attachmentMap = new HashMap<>();

		try {
			String path = informePath + "\\fotosactividades\\" + formulario.getIdActividad();

			File folder = new File(path);
			if (folder.exists()) {
				File[] listFiles = folder.listFiles();
				for (File f : listFiles) {
					f.delete();
				}
				Path p = Paths.get(path);
				try {
					Files.deleteIfExists(p);
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception("No se pudo borrar el path " + path, e);
				}
			}

			try {
				Files.createDirectories(Paths.get(path));
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("No se pudo generar el path " + path, e);
			}

			List<Map<String, String>> archivos = new ArrayList<>();

			int i = 1;
			for (FotoDTO foto : formulario.getFotos()) {
				String[] split = foto.getNombre().split("\\.");
				String fileName = "FOTO-" + Calendar.getInstance().getTimeInMillis() + "-" + i;
				String fileExtension = split[split.length - 1];
				String pathname = path + "\\" + fileName + "." + fileExtension;
				try {
					FileUtils.writeByteArrayToFile(new File(pathname), foto.getArchivo());
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception("No se pudo generar la foto " + pathname, e);
				}

				Map<String, String> fotoMap = new HashMap<String, String>();
				fotoMap.put("FileExtension", fileExtension);
				fotoMap.put("FileName", fileName);
				fotoMap.put("SourcePath", path);
				fotoMap.put("UserID", usuarioSAP);

				archivos.add(fotoMap);

			}

			attachmentMap.put("Attachments2_Lines", archivos.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			String error = "[ENVIARFORMULARIO] No se pudo generar attachment map " + e.getMessage();
			StringBuilder body = new StringBuilder();
			body.append("Error: " + e.getMessage() + "<br>");
			if (e.getCause() != null) {
				body.append("Causa: " + e.getCause().toString() + "<br>");
			}
			body.append(generarUrlQuitarColaEnvio(formulario.getIdActividad()) + "<br>");
			mailService.sendMail(error, body.toString(), null);
			throw new Exception(error);
		}
		LOGGER.debug("Actividad: " + idActividad + " - Attachment Map Generado");
		return attachmentMap;
	}

	private void updateHorasMaquina(Long idActividad, Long parentObjectId, Map<String, Object> serviceCallMap)
			throws Exception {
		try {
			enviarHsMaquinaSap(idActividad, parentObjectId, serviceCallMap);
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[ENVIARFORMULARIO] Actividad: " + idActividad
					+ " - No se pudo enviar las hs maq por service call.";
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				body.append(generarUrlQuitarColaEnvio(idActividad) + "<br>");
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}
	}

	private Map<String, Object> generarServiceCallMap(FormularioDTO formulario, Long idActividad, Long parentObjectId,
			JsonArray serviceCallActivitiesJson) throws Exception {
		Map<String, Object> serviceCallMap;
		try {
			serviceCallMap = obtenerServiceCallSap(formulario, idActividad, parentObjectId, serviceCallActivitiesJson);
		} catch (Exception e) {
			e.printStackTrace();
			String error = "[ENVIARFORMULARIO] " + e.getMessage();
			StringBuilder body = new StringBuilder();
			body.append("Error: " + e.getMessage() + "<br>");
			if (e.getCause() != null) {
				body.append("Causa: " + e.getCause().toString() + "<br>");
			}
			body.append(generarUrlQuitarColaEnvio(idActividad) + "<br>");
			mailService.sendMail(error, body.toString(), null);
			throw new Exception(error);
		}
		return serviceCallMap;
	}

	private JsonArray getServiceCallActivities(FormularioDTO formulario, Long idActividad, Long parentObjectId)
			throws Exception {
		
		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();
		
		JsonArray serviceCallActivitiesJson;
		try {
			serviceCallActivitiesJson = this.getServiceCallActivities(idActividad, parentObjectId);
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[ENVIARFORMULARIO] Actividad: " + idActividad
					+ " - No se pudo obtener las service call activities con parent object id " + parentObjectId;
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				body.append(generarUrlQuitarColaEnvio(formulario.getIdActividad()) + "<br>");
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}
		return serviceCallActivitiesJson;
	}

	private Long getServiceCallId(Long idActividad, SAPWSUtils sapUtils) throws Exception {
		
		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();
		
		Long parentObjectId;
		try {
			parentObjectId = this.getParentObjectId(idActividad);
		} catch (ParentObjectIdNotFoundException e) {
			e.printStackTrace();
			String error = "[ENVIARFORMULARIO] Actividad: " + idActividad
					+ " - No se pudo obtener el parentObjectId en el json";
			StringBuilder body = new StringBuilder();
			body.append("Error: " + e.getMessage() + "<br>");
			if (e.getCause() != null) {
				body.append("Causa: " + e.getCause().toString() + "<br>");
			}
			body.append(generarUrlQuitarColaEnvio(idActividad) + "<br>");
			mailService.sendMail(error, body.toString(), null);
			throw new Exception(error);
		} catch (SapBusinessException e) {
			e.printStackTrace();
			String error = "[ENVIARFORMULARIO] Actividad: " + idActividad
					+ " - No se pudo obtener el parentObjectId desde sap";
			if (e.hasToSendMail()) {
				StringBuilder body = new StringBuilder();
				body.append("ErrorCode: " + e.getErrorCode() + "<br>");
				body.append("Error: " + e.getMessage() + "<br>");
				if (e.getCause() != null) {
					body.append("Causa: " + e.getCause().toString() + "<br>");
				}
				body.append(generarUrlQuitarColaEnvio(idActividad) + "<br>");
				mailService.sendMail(error, body.toString(), null);
			}
			throw new Exception(error);
		}
		return parentObjectId;
	}

	private void enviarHsMaquinaSap(Long idActividad, Long parentObjectId, Map<String, Object> serviceCallMap)
			throws SapBusinessException {
		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();
		LOGGER.debug("Actividad: " + idActividad + " - Enviando Hs Maquina a la service call");
		String serviceCallUrl = urlSAP + "/ServiceCalls({id})";
		LOGGER.debug(serviceCallUrl);

		String scUrl = serviceCallUrl.replace("{id}", parentObjectId.toString());
		HttpEntity<Map<String, Object>> httpEntityPatchServiceCall = new HttpEntity<>(serviceCallMap);
		this.restTemplate.doExchange(scUrl, HttpMethod.PATCH, httpEntityPatchServiceCall, Object.class);
	}

	private Map<String, Object> obtenerServiceCallSap(FormularioDTO formulario, Long idActividad, Long parentObjectId,
			JsonArray serviceCallActivitiesJson) throws Exception {
		LOGGER.debug("Actividad: " + idActividad + " - Buscando la ServiceCall Activities asociada");
		Map<String, Object> serviceCallMap = new HashMap<>();
		try {
			for (JsonElement x : serviceCallActivitiesJson) {
				JsonObject item = x.getAsJsonObject();
				String acco = item.get("ActivityCode").toString();
				if (acco.equals(idActividad.toString())) {
					int lineNum = item.get("LineNum").getAsInt();
					serviceCallMap = generateServiceCallMap(parentObjectId, idActividad, lineNum,
							formulario.getHorasMaquina());
					LOGGER.debug(
							"Actividad: " + idActividad + " - ServiceCall Activities Encontrada LineNum " + lineNum);
					break;
				}
			}
		} catch (Exception e) {
			throw new Exception("Actividad: " + idActividad + " - No se pudo obtener el servicecall activity asociada",
					e);
		}

		if (serviceCallMap.isEmpty()) {
			throw new Exception("Actividad: " + idActividad + " - No se pudo encontar el servicecall activity.");
		}

		LOGGER.debug("Actividad: " + idActividad + " - ServiceCall Activities obtenida correctamente");
		return serviceCallMap;
	}

	private Map<String, Object> generateServiceCallMap(Long parentObjectId, Long idActividad, int lineNum,
			String horasMaquina) {
		Map<String, Object> map = new HashMap<>();
		map.put("ServiceCallID", parentObjectId);

		Map<String, Object> serviceCallActivity = new HashMap<>();
		serviceCallActivity.put("LineNum", lineNum);
		serviceCallActivity.put("ActivityCode", idActividad);
		serviceCallActivity.put("U_U_HsMaq", Long.parseLong(horasMaquina));

		Map<String, Object>[] array = new HashMap[1];
		array[0] = serviceCallActivity;

		map.put("ServiceCallActivities", array);

		return map;
	}

	private ActividadPatch generarActividadPatch(FormularioDTO formulario) {
		ActividadPatch ap = new ActividadPatch();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");

		Date inicioDate = null;
		Date finDate = null;
		try {
			inicioDate = sdf.parse(formulario.getFechaHoraInicio());
			finDate = sdf.parse(formulario.getFechaHoraFin());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Con Cargo
		ap.setU_ConCargo(formulario.getConCargo());

		// Observaciones Generales
		ap.setNotes(formulario.getObservacionesGenerales());

		// Operario
		// Fecha de Inicio
		ap.setStartDate(sdfDate.format(inicioDate));
		// Hora de Inicio
		ap.setStartTime(sdfHour.format(inicioDate));
		// Fecha Fin
		ap.setEndDueDate(sdfDate.format(finDate));
		// Hora Fin
		ap.setEndTime(sdfHour.format(finDate));

		// Valoracion si actividad no es de taller
		if (formulario.getActividadTaller() == null || !formulario.getActividadTaller()) {
			// Valoracion
			ap.setU_Valoracion(formulario.getValoracion());
			// Comentarios
			ap.setU_ValoracionComent(formulario.getComentarios());
			// Nombre Supervisor
			ap.setU_NomSupervisor(formulario.getNombreSupervisor());
			// DNI Supervisor
			ap.setU_DniSupervisor(formulario.getDniSupervisor().toString());
		}

		// Tareas
		String tareas = gson.toJson(formulario.getCheckList());
		ap.setU_Tareasreal(tareas);

		// Repuestos
		ap.setU_Repuestos(gson.toJson(formulario.getRepuestos()));

		// Estado Finalizado
		ap.setU_Estado("Finalizada");

		// Si el tipo de actividad es Reparación (R) o Checklist (C)
		if (formulario.getTipoActividad().equals("R") || formulario.getTipoActividad().equals("C")) {

			// Checks formateados
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArray = (JsonArray) jsonParser.parse(tareas);

			Map<String, List<String>> map = new HashMap<>();

			jsonArray.forEach(x -> {
				JsonObject item = x.getAsJsonObject();
				if (!item.get("estado").getAsString().equals("No aplica")) {
					List<String> list = map.get(item.get("titulo").toString());
					if (list == null) {
						list = new ArrayList<String>();
					}
					list.add(item.get("nombre") + " - " + item.get("estado") + " - " + item.get("observaciones"));
					map.put(item.get("titulo").toString(), list);
				}
			});

			StringBuilder sb = new StringBuilder();
			map.keySet().forEach(x -> {
				sb.append(" --- " + x + " ---");
				sb.append(System.lineSeparator());
				List<String> list = map.get(x);
				list.forEach(y -> {
					sb.append(y);
					sb.append(System.lineSeparator());
				});
			});

			ap.setU_Tareas_Real(sb.toString());
		}
		return ap;
	}

	@Override
	public void quitarArchivoColaEnvio(long actividadId) throws Exception {

		LOGGER.debug("Quitando archivo de actividad " + actividadId + " de cola de envios.");
		File folder = new File(jsonPath);
		WildcardFileFilter wildcardFileFilter = new WildcardFileFilter("actividad-" + actividadId + "*.txt");
		String[] list = folder.list(wildcardFileFilter);
		if (list == null || list.length != 1) {
			throw new Exception(
					"No se encontro archivo de actividad " + actividadId + " para quitar de cola de envios");
		}

		File file = new File(jsonPath + "\\" + list[0]);

		if (file.exists()) {
			try {
				FileUtils.moveFileToDirectory(file, new File(jsonPathQuitado), true);
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
	}

	public Long getParentObjectId(Long idActividad) throws ParentObjectIdNotFoundException, SapBusinessException {
		
		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();
		
		LOGGER.debug("Obteniendo ParentObjectId (ServiceCallID) de la actividad " + idActividad);
		String url = urlSAP + ACTIVITIES_SELECT_PARENT_OBJECT_ID;
		url = url.replace("{id}", idActividad.toString());
		LOGGER.debug(url);
		ResponseEntity<String> responseParentObjectId = null;
		responseParentObjectId = restTemplate.doExchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<String>() {
				});

		JsonElement jsonElement = gson.fromJson(responseParentObjectId.getBody(), JsonObject.class)
				.get("ParentObjectId");
		if (jsonElement == null || jsonElement.isJsonNull()) {
			throw new ParentObjectIdNotFoundException(
					"Actividad: " + idActividad + " - No se pudo obtener el parentObjectId . URL " + url);
		}
		return jsonElement.getAsLong();
	}

	public JsonArray getServiceCallActivities(Long idActividad, Long parentObjectId) throws SapBusinessException  {
		
		this.restTemplate = new RestTemplateFactory(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP).get();
		
		LOGGER.debug("Actividad: " + idActividad + " - Obteniendo ServiceCall Activities");
		String serviceCallActivitiesUrl = urlSAP + SERVICE_CALLS_SELECT_SERVICE_CALL_ACTIVITIES;
		serviceCallActivitiesUrl = serviceCallActivitiesUrl.replace("{id}", parentObjectId.toString());
		LOGGER.debug(urlSAP);

		ResponseEntity<String> serviceCallActivities = null;
			serviceCallActivities = restTemplate.doExchange(serviceCallActivitiesUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<String>() {
					});

		LOGGER.debug("Actividad: " + idActividad + " - ServiceCall Activities Obtenidas");

		return gson.fromJson(serviceCallActivities.getBody(), JsonObject.class).get("ServiceCallActivities")
				.getAsJsonArray();
	}
	
	@Resource(name = "mailSenderSMTPService")
	public void setMailService(MailSenderSMTPService mailService) {
		this.mailService = mailService;
	}

}
