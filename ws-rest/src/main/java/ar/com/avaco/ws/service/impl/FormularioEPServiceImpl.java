package ar.com.avaco.ws.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.com.avaco.factory.RestTemplateFactory;
import ar.com.avaco.ws.dto.FormularioDTO;
import ar.com.avaco.ws.service.FormularioEPService;

@Service("formularioEPService")
public class FormularioEPServiceImpl implements FormularioEPService{

	@Value("${urlSAP}")
	private String urlSAP;
	
	public void enviarFormulario (FormularioDTO formulario, String username) throws Exception {
		RestTemplate restTemplate = null;
		restTemplate = RestTemplateFactory.getInstance().getLoggedRestTemplate();


		String actividadUrl = urlSAP + "/Activities({id})";

		
		
		
		ResponseEntity<String> responsePatchActividades = restTemplate.exchange(actividadUrl.replace("{id}", formulario.getIdActividad().toString()), HttpMethod.PATCH, null,
				new ParameterizedTypeReference<String>() {
				});
		
	}
	
	public void setUrlSAP(String urlSAP) {
		this.urlSAP = urlSAP;
	}
	
}
