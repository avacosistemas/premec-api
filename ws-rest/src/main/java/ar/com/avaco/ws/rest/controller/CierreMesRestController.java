package ar.com.avaco.ws.rest.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ar.com.avaco.ws.dto.actividad.RegistroPreviewEmpleadoMensualDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.rest.security.dto.EmpleadoFichadoDTO;
import ar.com.avaco.ws.service.CierreMesService;

@Controller
public class CierreMesRestController {

	private CierreMesService cierreMesService;

	@RequestMapping(value = "/cierremespreview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> procesarExcelFichado(@RequestParam String mes, @RequestParam String anio) {
		JSONResponse response = new JSONResponse();
		List<RegistroPreviewEmpleadoMensualDTO> preview = this.cierreMesService.getRegistrosCierre(mes, anio);
		response.setData(preview);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/cierremespreview", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> enviarFichados(@RequestBody List<RegistroPreviewEmpleadoMensualDTO> cierre, @RequestParam String mes, @RequestParam String anio) throws IOException {
		JSONResponse response = new JSONResponse();
		this.cierreMesService.cerrarMes(cierre, anio, mes);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "cierreMesService")
	public void setCierreMesService(CierreMesService cierreMesService) {
		this.cierreMesService = cierreMesService;
	}

}
