package ar.com.avaco.ws.rest.security.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.ReporteEPService;

@RestController
public class ReporteRestController {

	private ReporteEPService reporteEPService;

	@RequestMapping(value = "/enviarreporte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> enviarReporte() {
		this.reporteEPService.enviarReporte();
		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "reporteEPService")
	public void setService(ReporteEPService service) {
		this.reporteEPService = service;
	}
}
