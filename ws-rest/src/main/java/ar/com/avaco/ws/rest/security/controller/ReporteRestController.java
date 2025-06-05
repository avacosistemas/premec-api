package ar.com.avaco.ws.rest.security.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.ws.dto.actividad.ActividadReporteDTO;
import ar.com.avaco.ws.rest.dto.JSONResponse;
import ar.com.avaco.ws.service.ActividadEPService;
import ar.com.avaco.ws.service.ReporteEPService;

@RestController
public class ReporteRestController {

	private ReporteEPService reporteEPService;
	private ActividadEPService actividadedEPService;

	@RequestMapping(value = "/enviarreporte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> enviarReporte() {
		try {
			List<ActividadReporteDTO> actividadesReporte = this.actividadedEPService.getActividadesReporte();
			actividadesReporte.forEach(x -> {
				try {
					this.reporteEPService.enviarReporte(x);
					this.actividadedEPService.marcarEnviado(x.getIdActividad());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/descargarreporte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> descargarReporte(Long idActividad) throws Exception {
		ActividadReporteDTO actividadReporte = this.actividadedEPService.getActividadReporte(idActividad);
		ResponseEntity<JSONResponse> generarReporte = this.reporteEPService.generarReporte(actividadReporte);
		return generarReporte;
	}

	@RequestMapping(value = "/marcarenviada", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONResponse> marcarEnviada(@RequestParam Long idActividad) {

		try {
			this.actividadedEPService.marcarEnviado(idActividad);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONResponse response = new JSONResponse();
		response.setStatus(JSONResponse.OK);
		return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
	}

	@Resource(name = "reporteEPService")
	public void setReporteEPService(ReporteEPService reporteEPService) {
		this.reporteEPService = reporteEPService;
	}

	@Resource(name = "actividadService")
	public void setActividadedEPService(ActividadEPService actividadedEPService) {
		this.actividadedEPService = actividadedEPService;
	}

}
