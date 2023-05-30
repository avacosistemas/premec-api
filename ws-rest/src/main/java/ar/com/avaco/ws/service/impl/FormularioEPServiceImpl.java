package ar.com.avaco.ws.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.ws.dto.ActividadPatch;
import ar.com.avaco.ws.dto.FormularioDTO;
import ar.com.avaco.ws.service.FormularioEPService;

@Service("formularioEPService")
public class FormularioEPServiceImpl implements FormularioEPService {

	@Value("${urlSAP}")
	private String urlSAP;

	public void enviarFormulario(FormularioDTO formulario, String username) throws Exception {

		ActividadPatch ap = new ActividadPatch();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");

		Date inicioDate = sdf.parse(formulario.getFechaHoraInicio());
		Date finDate = sdf.parse(formulario.getFechaHoraFin());

		ap.setStartDate(sdfDate.format(inicioDate));
		ap.setStartTime(sdfHour.format(inicioDate));

		ap.setEndDueDate(sdfDate.format(finDate));
		ap.setEndTime(sdfHour.format(finDate));
		
		ap.setU_Valoracion(formulario.getValoracion());
		ap.setU_ValoracionComent(formulario.getComentarios());
		ap.setU_Tareasreal(new Gson().toJson(formulario.getCheckList()));
		ap.setU_Repuestos(new Gson().toJson(formulario.getRepuestos()));
		ap.setU_NomSupervisor(formulario.getNombreSupervisor());
		ap.setU_DniSupervisor(formulario.getDniSupervisor().toString());
		ap.setNotes(formulario.getObservacionesGenerales());
		ap.setU_Estado("Finalizada");
		ap.setDocEntry(formulario.getIdActividad().toString());
		ap.setU_ConCargo(formulario.getConCargo());
		
		
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(ap.getAsMap());

		try {
			RestTemplate restTemplate = RestTemplateFactory.getInstance().getLoggedRestTemplate();

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

}
