package ar.com.avaco.ws.rest.controller;

import java.util.Base64;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.arc.sec.domain.Usuario;
import ar.com.avaco.factory.SapBusinessException;
import ar.com.avaco.ws.dto.ActividadReporteDTO;
import ar.com.avaco.ws.dto.ActividadTarjetaDTO;
import ar.com.avaco.ws.dto.RegistroHorasMaquinaDTO;
import ar.com.avaco.ws.dto.RegistroInformeServicioDTO;
import ar.com.avaco.ws.dto.RegistroMonitorDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.ActividadEPService;

@RestController
public class ActividadRestController {

	private ActividadEPService actividadedService;

	@RequestMapping(value = "/encodearServiceCall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> encodearServiceCall(@RequestParam Long serviceCallId) {
		JSONResponse response = new JSONResponse();
		Base64.Encoder encoder = Base64.getEncoder();
		String encodedString = encoder.encodeToString(serviceCallId.toString().getBytes());
		response.setData(encodedString);
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/actividadesPorServiceCall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> getServicios(@RequestParam String serviceCallId) {
		JSONResponse response = new JSONResponse();
		try {
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] decodedBytes = decoder.decode(serviceCallId);
	        String decodedString = new String(decodedBytes);
	        Long scId = Long.parseLong(decodedString);
			RegistroInformeServicioDTO actividadesServiceCall = this.actividadedService.getActividadesServiceCall(scId);
			response.setData(actividadesServiceCall);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/monitor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> getMonitor(@RequestParam String skip) {
		JSONResponse response = new JSONResponse();
		List<RegistroMonitorDTO> actividades;
		try {
			actividades = this.actividadedService.getActividadesMonitor(skip);
			response.setData(actividades);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/actividades", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> getActividadesTarjeta(String fecha) {
		JSONResponse response = new JSONResponse();
		Usuario u = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ActividadTarjetaDTO> actividades;
		try {
//			actividades = this.actividadedService.getActividades(fecha, u.getUsername());
			actividades = this.actividadedService.getActividadesCrossJoin(fecha, u.getUsername());
			response.setData(actividades);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/actividadesReporte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> getActividadesReporte() {
		JSONResponse response = new JSONResponse();
		List<ActividadReporteDTO> actividades;
		try {
			actividades = this.actividadedService.getActividadesReporte();
			response.setData(actividades);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/horasMaquinaReporte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> getHorasMaquinaReporte(String internalSerialNum) {
		JSONResponse response = new JSONResponse();
		List<RegistroHorasMaquinaDTO> actividades;
		try {
			actividades = this.actividadedService.getHorasMaquinaReporte(internalSerialNum);
			response.setData(actividades);
			response.setStatus(JSONResponse.OK);
		} catch (Exception e) {
			response.setStatus(JSONResponse.ERROR);
			response.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "actividadService")
	public void setActividadedService(ActividadEPService actividadedService) {
		this.actividadedService = actividadedService;
	}

}
