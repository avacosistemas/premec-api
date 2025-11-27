package ar.com.avaco.ws.rest.controller;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ar.com.avaco.ws.dto.ArchivoDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.security.dto.EmpleadoFichadoDTO;
import ar.com.avaco.ws.service.NovedadesFichadoService;

@Controller
public class NovedadesFichadoRestController {

	@Value(value = "${path.recibo.prueba}")
	private String pathPruebas;

	private NovedadesFichadoService service;

	@RequestMapping(value = "/procesarExcel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> procesarExcelFichado(@RequestBody ArchivoDTO archivo) throws IOException {
		JSONResponse response = new JSONResponse();
		response.setData(service.parsearExcelNovedadesFichado(archivo.getFile()));
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/enviarFichados", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> enviarFichados(@RequestBody EmpleadoFichadoDTO fichados) throws IOException {
		JSONResponse response = new JSONResponse();
		service.enviarFichados(fichados.getFichados());
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "novedadesFichadoService")
	public void setService(NovedadesFichadoService service) {
		this.service = service;
	}

}
