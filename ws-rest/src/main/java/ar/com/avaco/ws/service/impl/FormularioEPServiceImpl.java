package ar.com.avaco.ws.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ar.com.avaco.arc.core.service.MailSenderSMTPService;
import ar.com.avaco.entities.cliente.TipoActividad;
import ar.com.avaco.factory.ParentObjectIdNotFoundException;
import ar.com.avaco.factory.RestTemplateFactory;
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

	private MailSenderSMTPService mailService;

	private static final Logger LOGGER = Logger.getLogger(FormularioEPService.class);

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
					LOGGER.debug("##### Enviando formulario " + formularioId);
					activityCode = formularioDTO.getIdActividad();

					// Se envia el formulario a sap
					enviarFormulario(formularioDTO, userId);

					LOGGER.debug("##### Formulario " + formularioId + " enviado");
					file.delete();
					LOGGER.debug("Archivo borrado luego del envio");
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
					mailService.sendMail(from, toErrores, toErroresCC, subject, body.toString(), null);
				}

			}
		}

	}

	@Override
	public void enviarFormulario(FormularioDTO formulario, String usuarioSAP)
			throws ParentObjectIdNotFoundException, Exception {

		// Obtengo la actividad
		Long idActividad = formulario.getIdActividad();

		// Obtengo el rest template logueado usando credenciales de SAP
		RestTemplate restTemplate = RestTemplateFactory.getInstance(this.urlSAP, this.userSAP, this.passSAP, this.dbSAP)
				.getLoggedRestTemplate();
		LOGGER.debug("Actividad: " + idActividad + " - RestTemplate Generado");

		SAPWSUtils sapUtils = new SAPWSUtils(restTemplate, urlSAP);

		Long parentObjectId = sapUtils.getParentObjectId(idActividad);

		JsonArray serviceCallActivitiesJson = sapUtils.getServiceCallActivities(idActividad, parentObjectId);

		Map<String, Object> serviceCallMap = obtenerServiceCallSap(formulario, idActividad, parentObjectId,
				serviceCallActivitiesJson);

		enviarHsMaquinaSap(idActividad, restTemplate, parentObjectId, serviceCallMap);

		ActividadPatch ap = generarActividadPatch(formulario);

		Map<String, Object> attachmentMap = generarAttachmentMap(formulario, usuarioSAP);
		LOGGER.debug("Actividad: " + idActividad + " - Attachment Map Generado");
		enviarAttachmentsSap(idActividad, ap, attachmentMap, restTemplate);

		LOGGER.debug("Actividad: " + idActividad + " - Actividad Patch Generado");
		enviarActividadSap(idActividad, ap, restTemplate);

	}

	private void enviarActividadSap(Long idActividad, ActividadPatch ap, RestTemplate restTemplate) throws Exception {
		LOGGER.debug("Actividad: " + idActividad + " - Actualizando Actividad");
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(ap.getAsMap());
		String actividadUrl = urlSAP + "/Activities({id})".replace("{id}", idActividad.toString());
		try {
			restTemplate.exchange(actividadUrl, HttpMethod.PATCH, httpEntity, Object.class);
		} catch (RestClientException rce) {
			throw new Exception(
					"Actividad: " + idActividad + " - Error al intentar actualizar la actividad. URL " + actividadUrl,
					rce);
		}
		LOGGER.debug("Actividad Actualizada");
	}

	private void enviarHsMaquinaSap(Long idActividad, RestTemplate restTemplate, Long parentObjectId,
			Map<String, Object> serviceCallMap) throws Exception {
		LOGGER.debug("Actividad: " + idActividad + " - Enviando Hs Maquina a la service call");
		String serviceCallUrl = urlSAP + "/ServiceCalls({id})";
		LOGGER.debug(serviceCallUrl);

		String scUrl = serviceCallUrl.replace("{id}", parentObjectId.toString());
		HttpEntity<Map<String, Object>> httpEntityPatchServiceCall = new HttpEntity<>(serviceCallMap);
		try {
			restTemplate.exchange(scUrl, HttpMethod.PATCH, httpEntityPatchServiceCall, Object.class);
		} catch (RestClientException rce) {
			throw new Exception(
					"Actividad: " + idActividad + " - No se pudo enviar las hs maq por service call. URL " + scUrl,
					rce);
		}
		LOGGER.debug("Actividad: " + idActividad + " - Hs Maquina a la service call enviadas");
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

	private void enviarAttachmentsSap(Long idActividad, ActividadPatch ap, Map<String, Object> attachmentMap,
			RestTemplate restTemplate) throws Exception {
//		String actividadAttachmentUrl = urlSAP + "/Activities({idActividad})?$select=AttachmentEntry";
//		actividadAttachmentUrl = actividadAttachmentUrl.replace("{idActividad}", idActividad.toString());
//
//		ResponseEntity<String> attCountResult = null;
//		try {
//			attCountResult = restTemplate.exchange(actividadAttachmentUrl, HttpMethod.GET, null,
//					new ParameterizedTypeReference<String>() {
//					});
//		} catch (Exception e) {
//			e.printStackTrace();
//			String subject = "Error al obtener attachments previo al envio del formulario de la actividad "
//					+ idActividad;
//			StringBuilder body = new StringBuilder();
//			body.append("No se puso consultar el attachmentEntry de la actividad " + idActividad + ". URL: "
//					+ actividadAttachmentUrl);
//			body.append("Error: " + e.getMessage() + "<br>");
//			if (e.getCause() != null) {
//				body.append("Causa: " + e.getCause().toString() + "<br>");
//			}
//			mailService.sendMail(from, toErrores, toErroresCC, subject, body.toString(), null);
//			throw e;
//		}
//
//		Gson gson = new Gson();
//		JsonObject array = gson.fromJson(attCountResult.getBody(), JsonObject.class);
//
//		if (array.get("AttachmentEntry") != null && !array.get("AttachmentEntry").isJsonNull()) {
//			String subject = "Error: La actividad " + idActividad + " ya tiene attachments. Revisar errores previos."
//					+ "<br>";
//			StringBuilder body = new StringBuilder();
//			body.append("Error: La actividad " + idActividad
//					+ " ya tiene attachments. Revisar errores previos. Tal vez sea necesario borrar los attachments para que vuelvan a subir las fotos sin duplicarse.");
//			mailService.sendMail(from, toErrores, toErroresCC, subject, body.toString(), null);
//			throw new Exception("La actividad " + idActividad + " ya tiene attachments. Revisar errores previos.");
//		}

		LOGGER.debug("Actividad: " + idActividad + " - Enviando Attachments");

		String attachmentUrl = urlSAP + "/Attachments2";
		HttpEntity<Map<String, Object>> httpEntityAttach = new HttpEntity<>(attachmentMap);
		ResponseEntity<Object> attachmentRespose = null;
		try {
			attachmentRespose = restTemplate.exchange(attachmentUrl, HttpMethod.POST, httpEntityAttach, Object.class);
		} catch (RestClientException rce) {
			throw new Exception(
					"Actividad: " + idActividad + " - Error al intentar subir attachments. URL " + attachmentUrl, rce);
		}
		Object object = ((Map) attachmentRespose.getBody()).entrySet().toArray()[1];
		String attchEntry = (object.toString().split("="))[1];

		ap.setAttachmentEntry(attchEntry);
		LOGGER.debug("Actividad: " + idActividad + " - Attachments Enviados");
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

		Gson gson = new Gson();

		// Tareas
		String tareas = gson.toJson(formulario.getCheckList());
		ap.setU_Tareasreal(tareas);

		// Repuestos
		ap.setU_Repuestos(gson.toJson(formulario.getRepuestos()));

		// Estado Finalizado
		ap.setU_Estado("Finalizada");

		// Si el tipo de actividad es Reparación (R) o Checklist (C)
		if (formulario.getTipoActividad().equals(TipoActividad.REPARACION.getCodigo())
				|| formulario.getTipoActividad().equals(TipoActividad.CHECKLIST.getCodigo())) {

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

	private Map<String, Object> generarAttachmentMap(FormularioDTO formulario, String usuarioSAP) throws Exception {
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

		Map<String, Object> attachmentMap = new HashMap<>();

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
		return attachmentMap;
	}

	@Resource(name = "mailSenderSMTPService")
	public void setMailService(MailSenderSMTPService mailService) {
		this.mailService = mailService;
	}

}
