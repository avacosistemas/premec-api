package ar.com.avaco.ws.rest.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ar.com.avaco.ws.dto.actividad.RegistroPreviewEmpleadoMensualDTO;
import ar.com.avaco.ws.dto.employee.liquidacion.PlantillaData;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.security.dto.EmpleadoFichadoDTO;
import ar.com.avaco.ws.service.CierreMesService;
import ar.com.avaco.ws.service.LiquidacionService;

@Controller
public class LiquidacionController {

	@Autowired
	private LiquidacionService liquidacionService;

	@RequestMapping(value = "/enviarLiquidacion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> procesarExcelFichado(@RequestBody PlantillaData liquidacion) {
		JSONResponse response = new JSONResponse();
		try {
			this.liquidacionService.generarExcel(liquidacion.getPeriodo(), liquidacion.getFueraConvenio(),
					liquidacion.getMensual(), liquidacion.getJornales());
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

}