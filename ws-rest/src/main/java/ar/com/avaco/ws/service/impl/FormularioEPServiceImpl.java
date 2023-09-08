package ar.com.avaco.ws.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.ws.dto.ActividadPatch;
import ar.com.avaco.ws.dto.FormularioDTO;
import ar.com.avaco.ws.dto.FotoDTO;
import ar.com.avaco.ws.service.FormularioEPService;

@Service("formularioEPService")
public class FormularioEPServiceImpl implements FormularioEPService {

	@Value("${urlSAP}")
	private String urlSAP;

	@Value("${informe.path}")
	private String informePath;

	@Value("${json.path}")
	private String jsonPath;

	private static final Logger LOGGER = Logger.getLogger(FormularioEPService.class);

	public void enviarFormulario(FormularioDTO formulario, String usuarioSAP) throws Exception {

		LOGGER.debug("Generando Actividad Patch");
		ActividadPatch ap = generarActividadPatch(formulario);
		LOGGER.debug("Actividad Patch Generado");

		LOGGER.debug("Generando Attachment Map");
		Map<String, Object> attachmentMap = generarAttachmentMap(formulario, usuarioSAP);
		LOGGER.debug("Attachment Map Generado");

		String attachmentUrl = urlSAP + "/Attachments2";

		RestTemplate restTemplate = RestTemplateFactory.getInstance().getLoggedRestTemplate();

		HttpEntity<Map<String, Object>> httpEntityAttach = new HttpEntity<>(attachmentMap);
		LOGGER.debug("Enviando Attachments");
		ResponseEntity<Object> attachmentRespose = restTemplate.exchange(attachmentUrl, HttpMethod.POST,
				httpEntityAttach, Object.class);
		LOGGER.debug("Attachments Enviados");

		Object object = ((Map) attachmentRespose.getBody()).entrySet().toArray()[1];
		String attchEntry = (object.toString().split("="))[1];

		ap.setAttachmentEntry(attchEntry);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(ap.getAsMap());

		String actividadUrl = urlSAP + "/Activities({id})";

		LOGGER.debug("Actualizando Actividad " + formulario.getIdActividad());
		restTemplate.exchange(actividadUrl.replace("{id}", formulario.getIdActividad().toString()), HttpMethod.PATCH,
				httpEntity, Object.class);
		LOGGER.debug("Actividad Actualizada");

	}

	private ActividadPatch generarActividadPatch(FormularioDTO formulario) throws ParseException {
		ActividadPatch ap = new ActividadPatch();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");

		Date inicioDate = sdf.parse(formulario.getFechaHoraInicio());
		Date finDate = sdf.parse(formulario.getFechaHoraFin());

		// Con Cargo
		ap.setU_ConCargo(formulario.getConCargo());

		ap.setU_HsMaq(formulario.getHorasMaquina());
		
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

		// Valoracion
		// Valoracion
		ap.setU_Valoracion(formulario.getValoracion());
		// Comentarios
		ap.setU_ValoracionComent(formulario.getComentarios());
		// Nombre Supervisor
		ap.setU_NomSupervisor(formulario.getNombreSupervisor());
		// DNI Supervisor
		ap.setU_DniSupervisor(formulario.getDniSupervisor().toString());

		// Tareas
		String tareas = new Gson().toJson(formulario.getCheckList());
		ap.setU_Tareasreal(tareas);

		// Repuestos
		ap.setU_Repuestos(new Gson().toJson(formulario.getRepuestos()));

		// Estado Finalizado
		ap.setU_Estado("Finalizada");

		// Id de actividad
		ap.setDocEntry(formulario.getIdActividad().toString());

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
		return ap;
	}

	private Map<String, Object> generarAttachmentMap(FormularioDTO formulario, String usuarioSAP) throws IOException {
		String path = informePath + "\\fotosactividades\\" + formulario.getIdActividad();
		Files.createDirectories(Paths.get(path));

		Map<String, Object> attachmentMap = new HashMap<>();

		List<Map<String, String>> archivos = new ArrayList<>();

		int i = 1;
		for (FotoDTO foto : formulario.getFotos()) {
			String[] split = foto.getNombre().split("\\.");
			String fileName = "FOTO-" + Calendar.getInstance().getTimeInMillis() + "-" + i;
			String fileExtension = split[split.length - 1];
			String pathname = path + "\\" + fileName + "." + fileExtension;
			FileUtils.writeByteArrayToFile(new File(pathname), foto.getArchivo());

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
			throw new Exception("No se pudo serializar el formulario " + formularioDTO.getIdActividad());
		} catch (IOException e) {
			LOGGER.debug("No se pudo grabar el archivo" + pathname);
			e.printStackTrace();
			throw new Exception("No se pudo grabar el archivo " + pathname);
		}
	}

	@Override
	public void enviarFormulariosFromFiles() throws Exception {
		
		List<String> errors = new ArrayList<>();
		
		LOGGER.debug("Iniciando envio de formularios");
		ObjectMapper mapper = new ObjectMapper();
		File folder = new File(jsonPath);
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				try {
					String readFileToString = FileUtils.readFileToString(file);
					FormularioDTO formularioDTO = mapper.readValue(readFileToString, FormularioDTO.class);
					String[] split = file.getName().split("\\.")[0].split("-");
					String userId = split[3];
					String formularioId = split[1];
					LOGGER.debug("Enviando formulario " + formularioId);
					enviarFormulario(formularioDTO, userId);
					LOGGER.debug("Formulario " + formularioId + " enviado");
					file.delete();
					LOGGER.debug("Archivo borrado luego del envio");
				} catch (IOException e) {
					LOGGER.error("No se puede leer el archivo " + file.getName());
					LOGGER.error(e.getMessage());
					LOGGER.error(e.getStackTrace());
					errors.add("No se puede leer el archivo " + file.getName());
				} catch (Exception e) {
					LOGGER.error("No se pudo enviar el formulario " + file.getName());
					LOGGER.error(e.getMessage());
					LOGGER.error(e.getStackTrace());
					errors.add("No se pudo enviar el formulario " + file.getName());
				}

			}
		}
		
		if (errors.size() > 0) {
			throw new Exception(errors.stream().map(String::valueOf).collect(Collectors.joining(",")));
		}
		
	}

	public void setUrlSAP(String urlSAP) {
		this.urlSAP = urlSAP;
	}

	public void setInformePath(String informePath) {
		this.informePath = informePath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

}
