package ar.com.avaco.ws.rest.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ar.com.avaco.ws.dto.employee.liquidacion.PlantillaData;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.LiquidacionService;
import ar.com.avaco.ws.service.NovedadesContadorService;

@Controller
public class NovedadesContadorController {

	@Autowired
	private NovedadesContadorService novedadesContadorService;

	@Autowired
	private LiquidacionService liquidacionService;

	@RequestMapping(value = "/novedadescontadorpreview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> getNovedadesContadorPreview(@RequestParam String mes,
			@RequestParam String anio) {
		JSONResponse response = new JSONResponse();
		PlantillaData registrosCierre = this.novedadesContadorService.getRegistrosCierre(mes, anio);
		response.setData(registrosCierre);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/enviarnovedadescontador", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> enviarFichados(@RequestBody PlantillaData plantilla) throws IOException {
		JSONResponse response = new JSONResponse();
		liquidacionService.generarExcel(plantilla.getPeriodo(), plantilla.getFueraConvenio(), plantilla.getMensual(),
				plantilla.getJornales());
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

}
