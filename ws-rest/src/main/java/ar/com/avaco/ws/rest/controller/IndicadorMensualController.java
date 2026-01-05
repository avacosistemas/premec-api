package ar.com.avaco.ws.rest.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ar.com.avaco.arc.sec.service.UsuarioService;
import ar.com.avaco.ws.dto.actividad.RegistroPreviewEmpleadoMensualDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.CierreMesService;

@Controller
public class IndicadorMensualController {

	private CierreMesService cierreMesService;

	@RequestMapping(value = "/indicadorMensual", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> indicadorMensual(@RequestParam String mes, @RequestParam String anio) {
		JSONResponse response = new JSONResponse();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		RegistroPreviewEmpleadoMensualDTO preview = this.cierreMesService.getRegistrosCierre(mes, anio, username);
		response.setData(preview);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/indicadorMensualGeneral", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> indicadorMensualGeneral(@RequestParam String mes, @RequestParam String anio) {
		JSONResponse response = new JSONResponse();
		RegistroPreviewEmpleadoMensualDTO preview = this.cierreMesService.getRegistrosCierreSinAgrupar(mes, anio);
		response.setData(preview);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "cierreMesService")
	public void setCierreMesService(CierreMesService cierreMesService) {
		this.cierreMesService = cierreMesService;
	}

}
