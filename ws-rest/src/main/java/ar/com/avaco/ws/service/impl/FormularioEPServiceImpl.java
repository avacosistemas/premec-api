package ar.com.avaco.ws.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.ws.dto.ActividadPatch;
import ar.com.avaco.ws.dto.FormularioDTO;
import ar.com.avaco.ws.service.FormularioEPService;

@Service("formularioEPService")
public class FormularioEPServiceImpl implements FormularioEPService {

	@Value("${urlSAP}")
	private String urlSAP;

	@Value("${informe.path}")
	private String informePath;

	public void enviarFormulario(FormularioDTO formulario, String usuarioSAP) throws Exception {

		ActividadPatch ap = new ActividadPatch();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");

		Date inicioDate = sdf.parse(formulario.getFechaHoraInicio());
		Date finDate = sdf.parse(formulario.getFechaHoraFin());

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

		String path = informePath + "\\fotosactividades\\" + formulario.getIdActividad();
		Files.createDirectories(Paths.get(path));

		String attachmentUrl = urlSAP + "/Attachments2";

		Map<String, Object> attachmentMap = new HashMap<>();

		List<Map<String, String>> archivos = new ArrayList<>();

		int i = 1;
		formulario.getFotos().forEach(foto -> {
			try {
				String[] split = foto.getNombre().split("\\.");
				String fileName = "FOTO-" + Calendar.getInstance().getTimeInMillis() + "-" + i;
				String fileExtension = split[split.length - 1];
				String pathname = path + "\\" + fileName + "." + fileExtension;
				FileUtils.writeByteArrayToFile(new File(pathname), foto.getArchivo());

				Map<String, String> fotoMap = new HashMap<>();
				fotoMap.put("FileExtension", fileExtension);
				fotoMap.put("FileName", fileName);
				fotoMap.put("SourcePath", path);
				fotoMap.put("UserID", usuarioSAP);

				archivos.add(fotoMap);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		attachmentMap.put("Attachments2_Lines", archivos.toArray());

		try {

			RestTemplate restTemplate = RestTemplateFactory.getInstance().getLoggedRestTemplate();

			HttpEntity<Map<String, Object>> httpEntityAttach = new HttpEntity<>(attachmentMap);
			ResponseEntity<Object> attachmentRespose = restTemplate.exchange(attachmentUrl, HttpMethod.POST,
					httpEntityAttach, Object.class);

			Gson gson = new Gson();
			Object object = ((Map) attachmentRespose.getBody()).entrySet().toArray()[1];
			String attchEntry = (object.toString().split("="))[1];

			ap.setAttachmentEntry(attchEntry);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(ap.getAsMap());

			String actividadUrl = urlSAP + "/Activities({id})";

			restTemplate.exchange(actividadUrl.replace("{id}", formulario.getIdActividad().toString()),
					HttpMethod.PATCH, httpEntity, Object.class);

		} catch (Exception e) {
			throw e;
		}
	}

	public void setUrlSAP(String urlSAP) {
		this.urlSAP = urlSAP;
	}

	public void setInformePath(String informePath) {
		this.informePath = informePath;
	}

}
