package ar.com.avaco.ws.rest.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.ws.dto.FormularioDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.FormularioEPService;

@RestController
public class FormularioRestController {

	private FormularioEPService formularioEPService;

	@RequestMapping(value = "/formulario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> saveFormulario(@RequestBody FormularioDTO formularioDTO) {
		JSONResponse response = new JSONResponse();
		Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			this.formularioEPService.grabarFormulario(formularioDTO, u.getUsuariosap());
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/formulariosEnviarJsonSap", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> formulariosEnviar() {
		JSONResponse response = new JSONResponse();
		try {
			this.formularioEPService.enviarFormulariosFromFiles();
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/quitarArchivoColaEnvio", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> quitarArchivoColaEnvio(@RequestParam long actividadId) {
		JSONResponse response = new JSONResponse();
		try {
			this.formularioEPService.quitarArchivoColaEnvio(actividadId);
			response.setData("Archivo de actividad " + actividadId + " quitado de cola de envios con exito");
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@Resource(name = "formularioEPService")
	public void setService(FormularioEPService service) {
		this.formularioEPService = service;
	}
}
