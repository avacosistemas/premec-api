package ar.com.avaco.ws.rest.controller;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.NovedadesFichadoService;

@Controller
public class NovedadesFichadoRestController {

	private NovedadesFichadoService service;

	@RequestMapping(value = "/procesarExcel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> procesarExcelFichado() throws IOException {
		JSONResponse response = new JSONResponse();
		service.parsearExcelNovedadesFichado();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "novedadesFichadoService")
	public void setService(NovedadesFichadoService service) {
		this.service = service;
	}

}
