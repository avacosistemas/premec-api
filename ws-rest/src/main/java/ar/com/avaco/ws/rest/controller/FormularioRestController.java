package ar.com.avaco.ws.rest.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.ws.dto.FormularioDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.FormularioEPService;

@RestController
public class FormularioRestController {

	private FormularioEPService formularioEPService;
	
	@RequestMapping(value = "/formulario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> envioFormulario(@RequestBody FormularioDTO formularioDTO ) throws Exception {
		Usuario u = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.formularioEPService.enviarFormulario(formularioDTO,u.getUsername());
		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@Resource(name = "formularioEPService")
	public void setService(FormularioEPService service) {
		this.formularioEPService = service;
	}
}
