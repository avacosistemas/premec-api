package ar.com.avaco.ws.rest.security.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.ws.dto.ActividadTarjetaDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.ActividadEPService;

@RestController
public class ActividadRestController {

	private ActividadEPService actividadedService;

	@RequestMapping(value = "/actividades", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> getActividadesTarjeta(String fecha) {
		Usuario u = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ActividadTarjetaDTO> actividades = this.actividadedService.getActividades(fecha,u.getUsername());
		JSONResponse response = new JSONResponse();
		response.setData(actividades);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@Resource(name = "actividadService")
	public void setService(ActividadEPService service) {
		this.actividadedService = service;
	}
}


